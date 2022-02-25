package uk.gov.hmcts.reform.adoption.common.notification;

import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference;
import uk.gov.hmcts.reform.adoption.common.config.EmailTemplatesConfig;
import uk.gov.hmcts.reform.adoption.notification.ApplicantNotification;
import uk.gov.hmcts.reform.adoption.notification.CommonContent;
import uk.gov.hmcts.reform.adoption.notification.NotificationService;

import java.util.HashMap;

import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.TEST_EMAIL;

public class ApplicantIssuedNotification implements ApplicantNotification {

    @Autowired
    NotificationService notificationService;

    @Autowired
    private CommonContent commonContent;

    @Autowired
    private EmailTemplatesConfig config;

    @Override
    public void sendToApplicant1(final CaseData caseData, final Long caseId) {
        final String email = caseData.getApplicant1().getEmail();
        final LanguagePreference languagePreference = caseData.getApplicant1().getLanguagePreference();
        notificationService.sendEmail(
            email,
            TEST_EMAIL, // TODO Update template here
            new HashMap<>(), // TODO replace with template vars once template is ready
            languagePreference
        );
    }
}
