package uk.gov.hmcts.reform.adoption.citizen.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.notification.CommonContent;
import uk.gov.hmcts.reform.adoption.notification.NotificationService;
import uk.gov.hmcts.reform.idam.client.models.UserDetails;

import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.SAVE_SIGN_OUT;

@Component
public class SaveAndSignOutNotificationHandler {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CommonContent commonContent;

    public void notifyApplicant(CaseData caseData, UserDetails user) {
        final var invite = caseData.getCaseInvite();
        final var isTriggeredByApp2 = invite != null && user.getId().equals(invite.getApplicant2UserId());
        final var self = isTriggeredByApp2 ? caseData.getApplicant2() : caseData.getApplicant1();
        final var partner = isTriggeredByApp2 ? caseData.getApplicant1() : caseData.getApplicant2();

        notificationService.sendEmail(
            user.getEmail(),
            SAVE_SIGN_OUT,
            commonContent.mainTemplateVars(caseData, null, self, partner),
            self.getLanguagePreference()
        );
    }
}
