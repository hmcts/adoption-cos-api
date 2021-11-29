package uk.gov.hmcts.reform.adoption.citizen.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.adoption.common.config.EmailTemplatesConfig;
import uk.gov.hmcts.reform.adoption.divorcecase.model.Applicant;
import uk.gov.hmcts.reform.adoption.divorcecase.model.CaseData;
import uk.gov.hmcts.reform.adoption.notification.CommonContent;
import uk.gov.hmcts.reform.adoption.notification.NotificationService;

import java.util.Map;

import static uk.gov.hmcts.reform.adoption.notification.CommonContent.SIGN_IN_DISSOLUTION_URL;
import static uk.gov.hmcts.reform.adoption.notification.CommonContent.SIGN_IN_DIVORCE_URL;
import static uk.gov.hmcts.reform.adoption.notification.CommonContent.SIGN_IN_URL_NOTIFY_KEY;
import static uk.gov.hmcts.reform.adoption.notification.CommonContent.SUBMISSION_RESPONSE_DATE;
import static uk.gov.hmcts.reform.adoption.notification.CommonContent.isDivorce;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.JOINT_APPLICANT1_APPLICANT1_CHANGES_MADE;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.JOINT_APPLICANT2_APPLICANT1_CHANGES_MADE;
import static uk.gov.hmcts.reform.adoption.notification.FormatUtil.DATE_TIME_FORMATTER;

@Component
@Slf4j
public class Applicant1ResubmitNotification {

    public static final String THEIR_EMAIL_ADDRESS = "their email address";

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CommonContent commonContent;

    @Autowired
    private EmailTemplatesConfig configVars;

    public void sendToApplicant1(CaseData caseData, Long id) {
        log.info("Sending applicant 1 made changes notification to applicant 1 for case : {}", id);

        notificationService.sendEmail(
            caseData.getApplicant1().getEmail(),
            JOINT_APPLICANT1_APPLICANT1_CHANGES_MADE,
            applicant1TemplateVars(caseData, id),
            caseData.getApplicant1().getLanguagePreference()
        );
    }

    public void sendToApplicant2(CaseData caseData, Long id) {
        log.info("Sending applicant 1 made changes notification to applicant 2 for case : {}", id);

        notificationService.sendEmail(
            caseData.getApplicant2EmailAddress(),
            JOINT_APPLICANT2_APPLICANT1_CHANGES_MADE,
            applicant2TemplateVars(caseData, id),
            caseData.getApplicant2().getLanguagePreference()
        );
    }

    private Map<String, String> applicant1TemplateVars(CaseData caseData, Long id) {
        Map<String, String> templateVars = resubmitTemplateVars(caseData, id, caseData.getApplicant1(), caseData.getApplicant2());
        templateVars.put(THEIR_EMAIL_ADDRESS, caseData.getApplicant2EmailAddress());
        return templateVars;
    }

    private Map<String, String> applicant2TemplateVars(CaseData caseData, Long id) {
        Map<String, String> templateVars = resubmitTemplateVars(caseData, id, caseData.getApplicant2(), caseData.getApplicant1());
        String signInLink = configVars.getTemplateVars().get(isDivorce(caseData) ? SIGN_IN_DIVORCE_URL : SIGN_IN_DISSOLUTION_URL);
        templateVars.put(SIGN_IN_URL_NOTIFY_KEY, signInLink + "/applicant2/check-your-joint-application");
        return templateVars;
    }

    private Map<String, String> resubmitTemplateVars(CaseData caseData, Long id, Applicant applicant, Applicant partner) {
        Map<String, String> templateVars = commonContent.mainTemplateVars(caseData, id, applicant, partner);
        templateVars.put(SUBMISSION_RESPONSE_DATE, caseData.getDueDate().format(DATE_TIME_FORMATTER));
        return templateVars;
    }
}
