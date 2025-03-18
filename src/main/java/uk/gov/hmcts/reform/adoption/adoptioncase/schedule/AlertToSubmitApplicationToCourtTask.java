package uk.gov.hmcts.reform.adoption.adoptioncase.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.adoption.adoptioncase.service.CcdSearchService;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.adoption.notification.LocalAuthorityAlertToSubmitToCourt;
import uk.gov.hmcts.reform.adoption.systemupdate.CaseDetailsConverter;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.idam.client.models.User;

import java.time.LocalDate;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.existsQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Draft;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Submitted;
import static uk.gov.hmcts.reform.adoption.adoptioncase.service.CcdSearchService.CREATED_DATE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.service.CcdSearchService.STATE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.service.CcdSearchService.SUBMITTED_DATE;


@Component
@Slf4j  //NOSONAR test code to check CRON fires
@RequiredArgsConstructor
public class AlertToSubmitApplicationToCourtTask implements Runnable {

    @Autowired
    private CcdSearchService ccdSearchService;

    @Autowired
    private IdamService idamService;

    @Autowired
    private AuthTokenGenerator authTokenGenerator;

    @Autowired
    private LocalAuthorityAlertToSubmitToCourt localAuthorityAlertToSubmitToCourt;

    @Autowired
    private CaseDetailsConverter caseDetailsConverter;

    @Value("${cron.alertSubmitToCourt.offsetDays:15}")
    public  int emailAlertOffsetDays;

    @Override
    public void run() { //NOSONAR test code to check CRON fires

        log.info("AlertLAToSubmitApplicationToCourtTask is firing"); //NOSONAR test code to check CRON fires

        final User user = idamService.retrieveSystemUpdateUserDetails();
        final String serviceAuthorization = authTokenGenerator.generate();

        final BoolQueryBuilder query = boolQuery()
                .must(matchQuery(STATE, Submitted))
                .must(existsQuery(SUBMITTED_DATE))
                .filter(rangeQuery(SUBMITTED_DATE)
                        .gte(LocalDate.now().minusDays(emailAlertOffsetDays))
                        .lte(LocalDate.now().minusDays(emailAlertOffsetDays)));
        log.info("AlertLAToSubmitApplicationToCourtTask Scheduled task is executed");

        final List<CaseDetails> casesInDraftNeedingReminder =
                ccdSearchService.searchForAllCasesWithQuery(Submitted, query, user, serviceAuthorization);

        for (final CaseDetails caseDetails : casesInDraftNeedingReminder) {
            log.info("AlertLAToSubmitApplicationToCourtTask case details are present: " + caseDetails.getId());
            sendLocalAuthorityAlertToSubmitToCourt(caseDetails);
        }

    }

    private void sendLocalAuthorityAlertToSubmitToCourt(CaseDetails caseDetails) {
        log.info(
                "sendLocalAuthorityAlertToSubmitToCourt being called for case id: {}",
                caseDetails.getId()
        ); //NOSONAR test code to check CRON fires

        // uk.gov.hmcts.ccd.sdk.api.CaseDetails<CaseData, State> caseData =
        //     caseDetailsConverter.convertToCaseDetailsFromReformModel(
        //                caseDetails
        //      );
        // localAuthorityAlertToSubmitToCourt.sendLocalAuthorityAlertToSubmitToCourt(
        //     caseData.getData(),
        //     caseDetails.getId()
        //  );

    }

}
