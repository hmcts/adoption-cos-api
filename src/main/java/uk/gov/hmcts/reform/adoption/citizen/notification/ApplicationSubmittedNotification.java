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
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.APPLICATION_SUBMITTED;
import static uk.gov.hmcts.reform.adoption.notification.FormatUtil.DATE_TIME_FORMATTER;

@Component
@Slf4j
public class ApplicationSubmittedNotification {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CommonContent commonContent;

    public void sendToApplicant1(CaseData caseData, Long id) {
        log.info("Sending application submitted notification to applicant 1 for case : {}", id);

        notificationService.sendEmail(
            caseData.getApplicant1().getEmail(),
            APPLICATION_SUBMITTED,
            templateVars(caseData, id, caseData.getApplicant1(), caseData.getApplicant1()),
            caseData.getApplicant1().getLanguagePreference()
        );
    }

    private Map<String, String> templateVars(CaseData caseData, Long id, Applicant applicant, Applicant partner) {
        Map<String, String> templateVars = commonContent.mainTemplateVars(caseData, id, applicant, partner);
        templateVars.put(SUBMISSION_RESPONSE_DATE, caseData.getDueDate().format(DATE_TIME_FORMATTER));
        return templateVars;
    }
}
