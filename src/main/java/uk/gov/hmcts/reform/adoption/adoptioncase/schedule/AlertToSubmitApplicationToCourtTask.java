package uk.gov.hmcts.reform.adoption.adoptioncase.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.service.CcdSearchService;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.adoption.notification.LocalAuthorityAlertToSubmitToCourt;
import uk.gov.hmcts.reform.adoption.systemupdate.CaseDetailsConverter;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.idam.client.models.User;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.existsQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Submitted;
import static uk.gov.hmcts.reform.adoption.adoptioncase.service.CcdSearchService.STATE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.service.CcdSearchService.SUBMITTED_DATE;


@Component
@Slf4j
@RequiredArgsConstructor
public class AlertToSubmitApplicationToCourtTask implements Runnable {

    private final CcdSearchService ccdSearchService;

    private final IdamService idamService;

    private final AuthTokenGenerator authTokenGenerator;

    private final LocalAuthorityAlertToSubmitToCourt localAuthorityAlertToSubmitToCourt;

    private final CaseDetailsConverter caseDetailsConverter;

    @Value("${cron.alertSubmitToCourt.offsetDays:15}") //TODO change application.yaml back to 15
    public int emailAlertOffsetDays;

    @Override
    public void run() {

        final User user = idamService.retrieveSystemUpdateUserDetails();
        final String serviceAuthorization = authTokenGenerator.generate();

        final LocalDate offsetDate = ZonedDateTime.now().toLocalDate().minusDays(emailAlertOffsetDays);

        final BoolQueryBuilder query = boolQuery()
                .must(matchQuery(STATE, Submitted))
                .must(existsQuery(SUBMITTED_DATE))
                .filter(rangeQuery(SUBMITTED_DATE)
                        .gte(offsetDate)
                        .lte(offsetDate));
        log.info("AlertLAToSubmitApplicationToCourtTask Scheduled task is executed for cases submitted on {}", offsetDate);

        final List<CaseDetails> casesNeedingReminder =
                ccdSearchService.searchForAllCasesWithQuery(Submitted, query, user, serviceAuthorization);

        for (final CaseDetails caseDetails : casesNeedingReminder) {
            log.info("AlertLAToSubmitApplicationToCourtTask case details are present: {}", caseDetails.getId());
            sendLocalAuthorityAlertToSubmitToCourt(caseDetails);
        }

    }

    private void sendLocalAuthorityAlertToSubmitToCourt(CaseDetails caseDetails) {
        uk.gov.hmcts.ccd.sdk.api.CaseDetails<CaseData, State> caseData =
             caseDetailsConverter.convertToCaseDetailsFromReformModel(
                 caseDetails
             );
        localAuthorityAlertToSubmitToCourt.sendLocalAuthorityAlertToSubmitToCourt(
            caseData.getData(),
            caseDetails.getId()
        );

    }

}
