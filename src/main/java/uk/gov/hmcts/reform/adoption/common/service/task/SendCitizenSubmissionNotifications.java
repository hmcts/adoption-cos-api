package uk.gov.hmcts.reform.adoption.common.service.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.adoption.citizen.notification.ApplicationSubmittedNotification;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.task.CaseTask;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.AwaitingDocuments;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.AwaitingHWFDecision;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Submitted;

@Component
@Slf4j
public class SendCitizenSubmissionNotifications implements CaseTask {

    @Autowired
    private ApplicationSubmittedNotification applicationSubmittedNotification;

    @Override
    public CaseDetails<CaseData, State> apply(final CaseDetails<CaseData, State> caseDetails) {

        final CaseData caseData = caseDetails.getData();
        final Long caseId = caseDetails.getId();
        final State state = caseDetails.getState();

        if (!caseData.getApplication().isSolicitorApplication()) {
            sendCitizenNotifications(caseData, caseId, state);
        }

        return caseDetails;
    }

    private void sendCitizenNotifications(final CaseData caseData, final Long caseId, final State state) {

        if (Submitted.equals(state)
            || AwaitingDocuments.equals(state)
            || AwaitingHWFDecision.equals(state)) {

            log.info("Sending application submitted notification to applicant 1 for case : {}", caseId);
            applicationSubmittedNotification.sendToApplicant1(caseData, caseId);
            if (!caseData.getApplicationType().isSole()) {

                log.info("Sending application submitted notification to applicant 2 for case : {}", caseId);
                applicationSubmittedNotification.sendToApplicant2(caseData, caseId);
            }
        }
    }
}
