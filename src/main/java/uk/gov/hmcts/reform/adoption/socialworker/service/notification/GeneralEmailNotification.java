package uk.gov.hmcts.reform.adoption.socialworker.service.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.notification.CommonContent;
import uk.gov.hmcts.reform.adoption.notification.EmailTemplateName;
import uk.gov.hmcts.reform.adoption.notification.NotificationService;

import java.util.Map;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference.ENGLISH;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.GENERAL_EMAIL_PETITIONER;

@Component
@Slf4j
public class GeneralEmailNotification {

    public static final String GENERAL_EMAIL_DETAILS = "general email details";
    public static final String GENERAL_OTHER_RECIPIENT_NAME = "general other recipient name";

    @Autowired
    private CommonContent commonContent;

    @Autowired
    private NotificationService notificationService;

    public void send(final CaseData caseData, final Long caseId) {
        log.info("Sending General Email Notification for case id: {}", caseId);

        String emailTo = null;
        EmailTemplateName templateId;

        Map<String, String> templateVars = templateVars(caseData, caseId);

        log.info("Sending General Email Notification to petitioner for case id: {}", caseId);
        emailTo = caseData.getApplicant1().getEmail();
        templateId = GENERAL_EMAIL_PETITIONER;

        if (null == emailTo) {
            log.info("Email address is not available for template id {} and case {} ", templateId, caseId);
        } else {
            notificationService.sendEmail(
                emailTo,
                templateId,
                templateVars,
                ENGLISH
            );
            log.info("Successfully sent general email notification for case id: {}", caseId);
        }
    }

    private Map<String, String> templateVars(final CaseData caseData, final Long caseId) {
        final Map<String, String> templateVars = commonContent.basicTemplateVars(caseData, caseId);
        templateVars.put(GENERAL_OTHER_RECIPIENT_NAME, caseData.getGeneralEmail().getGeneralEmailOtherRecipientName());
        templateVars.put(GENERAL_EMAIL_DETAILS, caseData.getGeneralEmail().getGeneralEmailDetails());
        return templateVars;
    }
}
