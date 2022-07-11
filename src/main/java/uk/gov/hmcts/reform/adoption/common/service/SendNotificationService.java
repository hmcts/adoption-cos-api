package uk.gov.hmcts.reform.adoption.common.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.task.CaseTaskRunner;
import uk.gov.hmcts.reform.adoption.common.service.task.SendSubmissionNotifications;

@Service
public class SendNotificationService {


    @Autowired
    private SendSubmissionNotifications sendSubmissionNotifications;

    public CaseDetails<CaseData, State> sendNotifications(final CaseDetails<CaseData, State> caseDetails) {
        return CaseTaskRunner.caseTasks(sendSubmissionNotifications).run(caseDetails);
    }
}
