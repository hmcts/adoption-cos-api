package uk.gov.hmcts.reform.adoption.notification.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.adoption.common.service.SendNotificationService;
import uk.gov.hmcts.reform.adoption.service.event.ApplicationSubmitNotificationEvent;

@Slf4j
@Component
public class ApplicationSubmittedNotificationEventHandler {

    private SendNotificationService sendNotificationService;

    @Autowired
    public ApplicationSubmittedNotificationEventHandler(SendNotificationService sendNotificationService) {
        this.sendNotificationService = sendNotificationService;
    }

    @EventListener
    public void sendNotificationPostApplicationSubmission(ApplicationSubmitNotificationEvent applicationSubmitNotificationEvent) {

        log.info("ApplicationSubmittedNotificationEventHandler triggered");
        sendNotificationService.sendNotifications(
            applicationSubmitNotificationEvent.caseData());
        log.info("Sent Notifications after citizen application submission for CaseID: {}",
                 applicationSubmitNotificationEvent.caseData().getId());

    }


}
