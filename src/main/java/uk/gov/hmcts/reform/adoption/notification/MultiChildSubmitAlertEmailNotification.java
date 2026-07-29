package uk.gov.hmcts.reform.adoption.notification;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Applicant;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference;
import uk.gov.hmcts.reform.adoption.idam.IdamService;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.NO;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.MULTI_CHILD_SUBMIT_APPLICATION_EMAIL_ALERT;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.APPLICANT_1_FULL_NAME;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.HAS_MULTIPLE_APPLICANT;

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
        log.info("Sending MULTI_CHILD_SUBMIT_APPLICATION_EMAIL_ALERT notification to applicants for caseid : {}", id);

        final String applicant1Email = StringUtils.isNotBlank(caseData.getApplicant1().getEmailAddress())
                ? caseData.getApplicant1().getEmailAddress() : caseData.getApplicant1().getEmail();

        Map<String, Object> applicant1TemplateVars = generateTemplateVars(caseData.getApplicant1());

        notificationService.sendEmail(
            applicant1Email,
            MULTI_CHILD_SUBMIT_APPLICATION_EMAIL_ALERT,
            applicant1TemplateVars,
            getLanguagePreference(caseData.getApplicant1())
        );

        log.info("Multi_Child notification sent to applicant 1 for caseid {}", id);

        if (Objects.nonNull(caseData.getApplicant2())
            && StringUtils.isNotBlank(caseData.getApplicant2().getEmailAddress())) {

            final String applicant2Email = caseData.getApplicant2().getEmailAddress();
            Map<String, Object> applicant2TemplateVars = generateTemplateVars(caseData.getApplicant2());

            notificationService.sendEmail(
                applicant2Email,
                MULTI_CHILD_SUBMIT_APPLICATION_EMAIL_ALERT,
                applicant2TemplateVars,
                getLanguagePreference(caseData.getApplicant2())
            );
            log.info("Multi_Child notification sent to applicant 2 for caseid {}", id);
        }
    }

    private Map<String, Object> generateTemplateVars(Applicant applicant) {
        Map<String, Object> templateVars = new HashMap<>();

        templateVars.put(APPLICANT_1_FULL_NAME, generateApplicantFullName(
            applicant.getFirstName(),
            applicant.getLastName(),
            getLanguagePreference(applicant)
        ));
        // Simplified: emails are now only addressed to the applicant they are sent to.
        // HAS_MULTIPLE_APPLICANT can be stripped out of the notify template when next reworked.
        templateVars.put(HAS_MULTIPLE_APPLICANT, NO);

        return templateVars;
    }

    private String generateApplicantFullName(String firstName, String lastName, LanguagePreference languagePreference) {
        //TODO Welsh translation:
        String defaultWording = languagePreference == LanguagePreference.WELSH ? "ymgeisydd" : "applicant";
        String fullName = Stream.of(firstName, lastName)
            .filter(StringUtils::isNotBlank)
            .map(String::trim)
            .collect(Collectors.joining(" "));
        return StringUtils.defaultIfBlank(fullName, defaultWording);
    }

    private LanguagePreference getLanguagePreference(Applicant applicant) {
        return applicant.getLanguagePreference() != null
            ? applicant.getLanguagePreference() : LanguagePreference.ENGLISH;
    }
}
