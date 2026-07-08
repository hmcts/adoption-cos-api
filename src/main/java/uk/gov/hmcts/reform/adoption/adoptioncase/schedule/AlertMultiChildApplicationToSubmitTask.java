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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        final BoolQueryBuilder queryCreatedDate = boolQuery()
            .must(existsQuery(CREATED_DATE))
            .filter(rangeQuery(CREATED_DATE)
                        .gte(LocalDate.now())
                        .lte(LocalDate.now()));

        final BoolQueryBuilder querySubmittedDate = boolQuery()
            .must(existsQuery(SUBMITTED_DATE))
            .filter(rangeQuery(SUBMITTED_DATE)
                        .gte(LocalDate.now())
                        .lte(LocalDate.now())
            );

        log.info("AlertMultiChildApplicationToSubmitTask scheduled task is executed");

        final List<CaseDetails> draftCasesCreatedToday = Stream.of(Draft)
            .flatMap(state -> ccdSearchService
                .searchForAllCasesWithQuery(state, queryCreatedDate, user, serviceAuthorization)
                .stream())
            .toList();

        final List<CaseDetails> casesSubmittedOrLaSubmittedToday = Stream.of(Submitted, LaSubmitted)
            .flatMap(state -> ccdSearchService
                .searchForAllCasesWithQuery(state, querySubmittedDate, user, serviceAuthorization)
                .stream())
            .toList();

        final Map<String, List<CaseDetails>> casesByApplicantEmail =
            draftCasesCreatedToday.stream()
                .filter(caseDetails -> caseDetails.getData().get("applicant1Email") != null)
                .collect(Collectors.groupingBy(
                    caseDetails -> (String) caseDetails.getData().get("applicant1Email")
                ));

        log.info(
            "Checking the case lists of {} unique applicant1Emails for Draft multi-child cases",
            casesByApplicantEmail.size()
        );

        casesByApplicantEmail.values().stream()
            .filter(this::hasDraftCase)
            .filter(this::hasSubmittedOrLaSubmittedCase)
            .map(this::getFirstDraftCase)
            .forEach(caseDetails -> {
                sendReminderToApplicantsIfEligible(caseDetails);
                log.info("Attempted to send reminder for case id {}", caseDetails.getId());
            });
    }

    private boolean hasDraftCase(final List<CaseDetails> caseList) {
        return caseList.stream()
            .anyMatch(caseDetails -> Draft.name().equals(caseDetails.getState()));
    }

    private boolean hasSubmittedOrLaSubmittedCase(final List<CaseDetails> caseList) {
        return caseList.stream()
            .anyMatch(caseDetails ->
                          Submitted.name().equals(caseDetails.getState())
                              || LaSubmitted.name().equals(caseDetails.getState())
            );
    }

    private CaseDetails getFirstDraftCase(final List<CaseDetails> caseList) {
        return caseList.stream()
            .filter(caseDetails -> Draft.name().equals(caseDetails.getState()))
            .findFirst()
            .orElseThrow();
    }

    private void sendReminderToApplicantsIfEligible(final CaseDetails caseDetails) {
        final uk.gov.hmcts.ccd.sdk.api.CaseDetails<CaseData, State> caseData =
            caseDetailsConverter.convertToCaseDetailsFromReformModel(caseDetails);

        multiChildSubmitAlertEmailNotification.sendToApplicants(caseData.getData(), caseDetails.getId());
    }
}
