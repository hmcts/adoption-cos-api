package uk.gov.hmcts.reform.adoption.common.service.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.task.CaseTask;
import uk.gov.hmcts.reform.adoption.notification.ApplicationSubmittedNotification;
import uk.gov.hmcts.reform.adoption.notification.NotificationDispatcher;
import uk.gov.service.notify.NotificationClientException;

import java.io.IOException;
import java.util.EnumSet;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Submitted;

@Component
@Slf4j
public class SendCitizenSubmissionNotifications implements CaseTask {

    // @Autowired
    // private ApplicationOutstandingActionNotification applicationOutstandingActionNotification;

    @Autowired
    private ApplicationSubmittedNotification applicationSubmittedNotification;

    @Autowired
    private NotificationDispatcher notificationDispatcher;

    @Override
    public CaseDetails<CaseData, State> apply(final CaseDetails<CaseData, State> caseDetails) {

        final CaseData caseData = caseDetails.getData();
        final Long caseId = caseDetails.getId();
        final State state = caseDetails.getState();

        if (EnumSet.of(Submitted).contains(state)) {
            log.info("Sending application submitted notifications for case : {}", caseId);
            try {
                notificationDispatcher.send(applicationSubmittedNotification, caseData, caseId);
            } catch (NotificationClientException | IOException e) {
                log.error("Couldn't send notifications");
            }
        }

        //log.info("Sending outstanding action notification if awaiting documents for case : {}", caseId);
        //notificationDispatcher.send(applicationOutstandingActionNotification, caseData, caseId);

        return caseDetails;
    }
}
