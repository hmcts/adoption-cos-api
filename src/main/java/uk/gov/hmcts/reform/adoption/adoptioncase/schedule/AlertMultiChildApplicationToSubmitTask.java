package uk.gov.hmcts.reform.adoption.adoptioncase.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.service.CcdSearchService;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.adoption.notification.MultiChildSubmitAlertEmailNotification;
import uk.gov.hmcts.reform.adoption.systemupdate.CaseDetailsConverter;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.idam.client.models.User;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.existsQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Draft;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.LaSubmitted;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Submitted;
import static uk.gov.hmcts.reform.adoption.adoptioncase.service.CcdSearchService.CREATED_DATE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.service.CcdSearchService.SUBMITTED_DATE;

@Component
@Slf4j
@RequiredArgsConstructor
public class AlertMultiChildApplicationToSubmitTask implements Runnable {

    private final CcdSearchService ccdSearchService;

    private final IdamService idamService;

    private final AuthTokenGenerator authTokenGenerator;

    private final MultiChildSubmitAlertEmailNotification multiChildSubmitAlertEmailNotification;

    private final CaseDetailsConverter caseDetailsConverter;

    @Override
    public void run() {
        final User user = idamService.retrieveSystemUpdateUserDetails();
        final String serviceAuthorization = authTokenGenerator.generate();
        final LocalDate today = LocalDate.now(ZoneId.of("Europe/London"));

        final BoolQueryBuilder createdTodayQuery = dateEqualsQuery(CREATED_DATE, today);
        final BoolQueryBuilder submittedTodayQuery = dateEqualsQuery(SUBMITTED_DATE, today);

        log.info("AlertMultiChildApplicationToSubmitTask scheduled task is executed");

        final List<CaseDetails> draftCasesCreatedToday =
            searchCasesByStates(List.of(Draft), createdTodayQuery, user, serviceAuthorization);

        final List<CaseDetails> casesSubmittedToday =
            searchCasesByStates(List.of(Submitted, LaSubmitted), submittedTodayQuery, user, serviceAuthorization);

        if (draftCasesCreatedToday.isEmpty() || casesSubmittedToday.isEmpty()) {
            log.info("No cases met critera for alert ({} draft cases created today, {} cases submitted today)",
                     draftCasesCreatedToday.size(), casesSubmittedToday.size());
            return;
        }

        final Set<String> applicant1EmailsForCasesSubmittedToday = casesSubmittedToday.stream()
            .map(caseDetails -> (String) caseDetails.getData().get("applicant1Email"))
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        log.info(
            "Looking for any of {} applicant1Emails (from cases Submitted today) in {} Draft cases",
            applicant1EmailsForCasesSubmittedToday.size(), draftCasesCreatedToday.size()
        );

        draftCasesCreatedToday.forEach(caseDetails -> {
            String applicant1Email = caseDetails.getData().get("applicant1Email").toString();
            if (applicant1EmailsForCasesSubmittedToday.contains(applicant1Email)) {
                sendReminderToApplicantsIfEligible(caseDetails);
                log.info("Attempted to send reminder for case id {}", caseDetails.getId());
                applicant1EmailsForCasesSubmittedToday.remove(applicant1Email);
            }
        });
    }

    private void sendReminderToApplicantsIfEligible(final CaseDetails caseDetails) {
        final uk.gov.hmcts.ccd.sdk.api.CaseDetails<CaseData, State> caseData =
            caseDetailsConverter.convertToCaseDetailsFromReformModel(caseDetails);

        multiChildSubmitAlertEmailNotification.sendToApplicants(caseData.getData(), caseDetails.getId());
    }

    private BoolQueryBuilder dateEqualsQuery(String fieldName, LocalDate date) {
        return boolQuery()
            .must(existsQuery(fieldName))
            .filter(rangeQuery(fieldName).gte(date).lte(date));
    }

    private List<CaseDetails> searchCasesByStates(
        List<State> states,
        BoolQueryBuilder query,
        User user,
        String serviceAuthorization
    ) {
        return states.stream()
            .flatMap(state -> ccdSearchService.searchForAllCasesWithQuery(state, query, user, serviceAuthorization).stream())
            .toList();
    }
}
