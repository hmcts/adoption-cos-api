package uk.gov.hmcts.reform.adoption.notification;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Applicant;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference;
import uk.gov.hmcts.reform.adoption.idam.IdamService;

import java.util.Map;

import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.NO;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.YES;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.MULTI_CHILD_SUBMIT_APPLICATION_EMAIL_ALERT;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.APPLICANT_1_FULL_NAME;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.APPLICANT_2_FULL_NAME;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.HAS_MULTIPLE_APPLICANT;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.HAS_SECOND_APPLICANT;

@Component
@Slf4j
public class MultiChildSubmitAlertEmailNotification implements ApplicantNotification {

    @Autowired
    IdamService idamService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CommonContent commonContent;





    @Override
    public void sendToApplicants(final CaseData caseData, final Long id) {
        log.info("Sending MULTI_CHILD_SUBMIT_APPLICATION_EMAIL_ALERT notification to applicants for case : {}", id);

        final String applicant1Email = caseData.getApplicant1().getEmailAddress();
        final String applicant2Email = caseData.getApplicant2().getEmailAddress();
        final LanguagePreference applicant1LanguagePreference = caseData.getApplicant1().getLanguagePreference();

        Map<String, Object> templateVars = templateVars(caseData, id, caseData.getApplicant1(), caseData.getApplicant2());
        if (StringUtils.isNotBlank(applicant1Email)) {
            notificationService.sendEmail(
                applicant1Email,
                MULTI_CHILD_SUBMIT_APPLICATION_EMAIL_ALERT,
                templateVars,
                applicant1LanguagePreference != null
                    ? applicant1LanguagePreference : LanguagePreference.ENGLISH
            );
        }
        log.info("notification sent to applicant 1 : {}", id);

        if (StringUtils.isNotBlank(applicant2Email)) {
            final LanguagePreference applicant2LanguagePreference = caseData.getApplicant2().getLanguagePreference();

            notificationService.sendEmail(
                applicant2Email,
                MULTI_CHILD_SUBMIT_APPLICATION_EMAIL_ALERT,
                templateVars,
                applicant2LanguagePreference != null
                    ? applicant2LanguagePreference : LanguagePreference.ENGLISH
            );
        }
        if (StringUtils.isEmpty(applicant1Email) && StringUtils.isEmpty(applicant2Email)) {
            final String defaultEmailAddress = caseData.getApplicant1().getEmail();
            notificationService.sendEmail(
                defaultEmailAddress,
                MULTI_CHILD_SUBMIT_APPLICATION_EMAIL_ALERT,
                templateVars,
                applicant1LanguagePreference != null
                    ? applicant1LanguagePreference : LanguagePreference.ENGLISH
            );
        }
    }

    private Map<String, Object> templateVars(CaseData caseData, Long id, Applicant applicant1, Applicant applicant2) {
        Map<String, Object> templateVars = commonContent.mainTemplateVars(caseData, id, applicant1, applicant2);
        templateVars.put(APPLICANT_1_FULL_NAME, caseData.getApplicant1().getFirstName() + " " + caseData.getApplicant1().getLastName());
        if (isApplicantInfoExists(caseData.getApplicant2())) {
            templateVars.put(
                APPLICANT_2_FULL_NAME,
                caseData.getApplicant2().getFirstName() + " " + caseData.getApplicant2().getLastName()
            );
            templateVars.put(HAS_SECOND_APPLICANT, YES);
        } else {
            templateVars.put(HAS_SECOND_APPLICANT, NO);
            templateVars.put(APPLICANT_2_FULL_NAME, StringUtils.EMPTY);
        }
        if (isApplicantInfoExists(caseData.getApplicant1()) && isApplicantInfoExists(caseData.getApplicant2())) {
            templateVars.put(HAS_MULTIPLE_APPLICANT, YES);
        } else {
            templateVars.put(HAS_MULTIPLE_APPLICANT, NO);
        }

        return templateVars;
    }

    private boolean isApplicantInfoExists(Applicant applicant) {
        return applicant != null && (StringUtils.isNotBlank(applicant.getFirstName())
            || StringUtils.isNotBlank(applicant.getLastName()));
    }
}
