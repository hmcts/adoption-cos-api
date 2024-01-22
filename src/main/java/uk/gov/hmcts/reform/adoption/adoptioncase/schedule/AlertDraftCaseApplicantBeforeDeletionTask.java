package uk.gov.hmcts.reform.adoption.adoptioncase.schedule;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.service.CcdSearchService;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.adoption.notification.DraftApplicationExpiringNotification;
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
import static uk.gov.hmcts.reform.adoption.adoptioncase.service.CcdSearchService.CREATED_DATE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.service.CcdSearchService.STATE;

@Component
@Slf4j
@RequiredArgsConstructor
public class AlertDraftCaseApplicantBeforeDeletionTask implements Runnable {


    @Autowired
    private CcdSearchService ccdSearchService;

    @Autowired
    private IdamService idamService;

    @Autowired
    private AuthTokenGenerator authTokenGenerator;

    @Autowired
    private DraftApplicationExpiringNotification draftApplicationExpiringNotification;

    @Autowired
    private CaseDetailsConverter caseDetailsConverter;

    @Value("${cron.alertDraftApplicant.offsetDays:69}")
    public  int emailAlertOffsetDays;


    /**
     * When an objectclear implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     *
     * <p>The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

        final User user = idamService.retrieveSystemUpdateUserDetails();
        final String serviceAuthorization = authTokenGenerator.generate();

        final BoolQueryBuilder query = boolQuery()
            .must(matchQuery(STATE, Draft))
            .must(existsQuery(CREATED_DATE))
            .filter(rangeQuery(CREATED_DATE)
                        .gte(LocalDate.now().minusDays(emailAlertOffsetDays))
                        .lte(LocalDate.now().minusDays(emailAlertOffsetDays)));
        log.info("Scheduled task is executed");

        final List<CaseDetails> casesInDraftNeedingReminder =
            ccdSearchService.searchForAllCasesWithQuery(Draft, query, user, serviceAuthorization);

        for (final CaseDetails caseDetails : casesInDraftNeedingReminder) {
            log.info("case details are present: " + caseDetails.getId());
            sendReminderToApplicantsIfEligible(caseDetails);
        }

    }

    private void sendReminderToApplicantsIfEligible(CaseDetails caseDetails) {

        uk.gov.hmcts.ccd.sdk.api.CaseDetails<CaseData, State> caseData = caseDetailsConverter.convertToCaseDetailsFromReformModel(
            caseDetails);
        draftApplicationExpiringNotification.sendToApplicants(caseData.getData(), caseDetails.getId());

    }
}
