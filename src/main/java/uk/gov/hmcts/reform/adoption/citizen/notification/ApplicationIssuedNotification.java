package uk.gov.hmcts.reform.adoption.citizen.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Applicant;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.notification.CommonContent;
import uk.gov.hmcts.reform.adoption.notification.NotificationService;

import java.util.Map;

import static uk.gov.hmcts.reform.adoption.notification.CommonContent.SUBMISSION_RESPONSE_DATE;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.SOLE_APPLICANT_APPLICATION_ACCEPTED;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.SOLE_APPLICANT_PARTNER_HAS_NOT_RESPONDED;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.SOLE_RESPONDENT_APPLICATION_ACCEPTED;
import static uk.gov.hmcts.reform.adoption.notification.FormatUtil.DATE_TIME_FORMATTER;

@Component
@Slf4j
public class ApplicationIssuedNotification {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CommonContent commonContent;

    public void sendToSoleApplicant1(CaseData caseData, Long id) {
        log.info("Sending sole application issued notification to applicant 1 for case : {}", id);

        notificationService.sendEmail(
            caseData.getApplicant1().getEmail(),
            SOLE_APPLICANT_APPLICATION_ACCEPTED,
            null,
            caseData.getApplicant1().getLanguagePreference()
        );
    }

    public void sendToSoleRespondent(CaseData caseData, Long id) {
        log.info("Sending sole application issued notification to respondent for case : {}", id);

        notificationService.sendEmail(
            null,
            SOLE_RESPONDENT_APPLICATION_ACCEPTED,
            null,
            caseData.getApplicant1().getLanguagePreference()
        );
    }

    public void sendReminderToSoleRespondent(CaseData caseData, Long id) {
        log.info("Sending reminder to respondent to register for case : {}", id);

        notificationService.sendEmail(
            null,
            SOLE_RESPONDENT_APPLICATION_ACCEPTED,
            null,
            caseData.getApplicant1().getLanguagePreference()
        );
    }

    public void sendPartnerNotRespondedToSoleApplicant(CaseData caseData, Long id) {
        log.info("Sending the respondent has not responded notification to the applicant for case : {}", id);

        notificationService.sendEmail(
            caseData.getApplicant1().getEmail(),
            SOLE_APPLICANT_PARTNER_HAS_NOT_RESPONDED,
            commonTemplateVars(caseData, id, caseData.getApplicant1(), null),
            caseData.getApplicant1().getLanguagePreference()
        );
    }

    private Map<String, String> commonTemplateVars(final CaseData caseData, Long id, Applicant applicant, Applicant partner) {
        final Map<String, String> templateVars = commonContent.mainTemplateVars(caseData, id, applicant, partner);
        templateVars.put(SUBMISSION_RESPONSE_DATE, caseData.getDueDate().format(DATE_TIME_FORMATTER));
        return templateVars;
    }
}
