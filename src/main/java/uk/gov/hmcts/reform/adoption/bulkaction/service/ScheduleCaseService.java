package uk.gov.hmcts.reform.adoption.bulkaction.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.adoption.bulkaction.ccd.BulkActionState;
import uk.gov.hmcts.reform.adoption.bulkaction.data.BulkActionCaseData;
import uk.gov.hmcts.reform.adoption.bulkaction.task.BulkCaseCaseTaskFactory;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.idam.client.models.User;

import static uk.gov.hmcts.reform.adoption.systemupdate.event.SystemUpdateCaseWithCourtHearing.SYSTEM_UPDATE_CASE_COURT_HEARING;
import static uk.gov.hmcts.reform.adoption.systemupdate.event.SystemUpdateCaseWithPronouncementJudge.SYSTEM_UPDATE_CASE_PRONOUNCEMENT_JUDGE;

@Service
@Slf4j
public class ScheduleCaseService {

    @Autowired
    private AuthTokenGenerator authTokenGenerator;

    @Autowired
    private IdamService idamService;

    @Autowired
    private BulkCaseCaseTaskFactory bulkCaseCaseTaskFactory;

    @Autowired
    private BulkCaseProcessingService bulkCaseProcessingService;

    @Async
    public void updateCourtHearingDetailsForCasesInBulk(final CaseDetails<BulkActionCaseData, BulkActionState> bulkCaseDetails,
                                                        final String authorization) {

        final User user = idamService.retrieveUser(authorization);
        final String serviceAuth = authTokenGenerator.generate();

        bulkCaseProcessingService.updateAllBulkCases(
            bulkCaseDetails,
            SYSTEM_UPDATE_CASE_COURT_HEARING,
            bulkCaseCaseTaskFactory.getCaseTask(bulkCaseDetails.getData(), SYSTEM_UPDATE_CASE_COURT_HEARING),
            user,
            serviceAuth);
    }

    @Async
    public void updatePronouncementJudgeDetailsForCasesInBulk(final CaseDetails<BulkActionCaseData, BulkActionState> bulkCaseDetails,
                                                              final String authorization) {

        final User user = idamService.retrieveUser(authorization);
        final String serviceAuth = authTokenGenerator.generate();

        bulkCaseProcessingService.updateAllBulkCases(
            bulkCaseDetails,
            SYSTEM_UPDATE_CASE_PRONOUNCEMENT_JUDGE,
            bulkCaseCaseTaskFactory.getCaseTask(bulkCaseDetails.getData(), SYSTEM_UPDATE_CASE_PRONOUNCEMENT_JUDGE),
            user,
            serviceAuth);
    }
}
