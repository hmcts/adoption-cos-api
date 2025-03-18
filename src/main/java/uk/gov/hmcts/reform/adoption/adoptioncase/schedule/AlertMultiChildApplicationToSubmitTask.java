package uk.gov.hmcts.reform.adoption.adoptioncase.schedule;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.existsQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Draft;
import static uk.gov.hmcts.reform.adoption.adoptioncase.service.CcdSearchService.CREATED_DATE;

@Component
@Slf4j
@RequiredArgsConstructor
public class AlertMultiChildApplicationToSubmitTask implements Runnable {


    @Autowired
    private CcdSearchService ccdSearchService;

    @Autowired
    private IdamService idamService;

    @Autowired
    private AuthTokenGenerator authTokenGenerator;

    @Autowired
    private MultiChildSubmitAlertEmailNotification multiChildSubmitAlertEmailNotification;

    @Autowired
    private CaseDetailsConverter caseDetailsConverter;


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
        log.info("Idam user name: " + user.getUserDetails().getEmail());
        final String serviceAuthorization = authTokenGenerator.generate();

        final BoolQueryBuilder query = boolQuery()
            .must(existsQuery(CREATED_DATE))
            .filter(rangeQuery(CREATED_DATE)
                        .gte(LocalDate.now())
                        .lte(LocalDate.now()));
        log.info("AlertMultiChildApplicationToSubmitTask Scheduled task is executed");

        final List<CaseDetails> casesInDraftNeedingReminder =
            ccdSearchService.searchForAllCasesWithQuery(Draft, query, user, serviceAuthorization);
        Map<String, List<CaseDetails>> emailCounts = new HashMap<>();

        for (final CaseDetails caseDetails : casesInDraftNeedingReminder) {
            log.info("AlertMultiChildApplicationToSubmitTask case details are present: " + caseDetails.getId());
            String applicantEmail = (String) caseDetails.getData().get("applicant1Email");
            List<CaseDetails> caseList = emailCounts.get(applicantEmail);
            if (!CollectionUtils.sizeIsEmpty(caseList)) {
                log.info("adding case to the map {}", caseDetails.getId());
                caseList.add(caseDetails);
                log.info("count of the case list {}", caseList.size());
                emailCounts.put(applicantEmail, caseList);
                log.info("map count {}", emailCounts.size());
            } else {
                log.info("No case added to the map till now for the user {}", applicantEmail);
                log.info("adding case to the map {}", caseDetails.getId());
                List<CaseDetails> caseListForUser = getNewCaseList();
                caseListForUser.add(caseDetails);
                log.info("count of the case list {}", caseListForUser.size());
                emailCounts.put(applicantEmail, caseListForUser);
                log.info("map count {}", emailCounts.size());
            }

        }
        log.info("case list size {}",emailCounts.size());
        emailCounts.forEach((id, caseLists) -> {
            log.info("case list for user {} count {}",id,caseLists.size());
            if (caseLists.size() > NumberUtils.INTEGER_ONE) {
                caseLists.forEach(caseDe -> {
                    log.info("state of the case {} for case id {}",caseDe.getId(),caseDe.getState());
                    if (State.Draft.toString().equals(caseDe.getState())) {
                        sendReminderToApplicantsIfEligible(caseDe);
                        log.info("case for the user " + id + " " + caseDe.getId());
                    }
                });
            }
        });


    }

    private List<CaseDetails> getNewCaseList() {
        return new ArrayList<>();
    }

    private void sendReminderToApplicantsIfEligible(CaseDetails caseDetails) {

        uk.gov.hmcts.ccd.sdk.api.CaseDetails<CaseData, State> caseData = caseDetailsConverter.convertToCaseDetailsFromReformModel(
            caseDetails);

        multiChildSubmitAlertEmailNotification.sendToApplicants(caseData.getData(), caseDetails.getId());
    }


}
