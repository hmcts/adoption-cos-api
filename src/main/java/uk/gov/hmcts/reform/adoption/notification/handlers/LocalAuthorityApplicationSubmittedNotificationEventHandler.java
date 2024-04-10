package uk.gov.hmcts.reform.adoption.notification.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.adoption.common.service.SendNotificationService;
import uk.gov.hmcts.reform.adoption.service.event.LocalAuthorityApplicationSubmitNotificationEvent;

@Slf4j
@Component
public class LocalAuthorityApplicationSubmittedNotificationEventHandler {


    private final SendNotificationService sendNotificationService;

    @Autowired
    public LocalAuthorityApplicationSubmittedNotificationEventHandler(SendNotificationService sendNotificationService) {
        this.sendNotificationService = sendNotificationService;
    }

    @EventListener
    public void sendNotificationPostLocalAuthoritySubmission(LocalAuthorityApplicationSubmitNotificationEvent
                                                                  localAuthorityApplicationSubmitNotificationEvent) {

        log.info("LocalAuthorityApplicationSubmittedNotificationEventHandler triggered");
        sendNotificationService.sendNotifications(
                localAuthorityApplicationSubmitNotificationEvent.caseData());
        log.info("After Local Authority application submission for CaseID: {}",
                localAuthorityApplicationSubmitNotificationEvent.caseData().getId());

    }


}
