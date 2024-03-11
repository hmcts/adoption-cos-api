package uk.gov.hmcts.reform.adoption.notification;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference;
import uk.gov.hmcts.reform.adoption.common.config.EmailTemplatesConfig;
import uk.gov.hmcts.reform.adoption.idam.IdamService;

import java.util.HashMap;
import java.util.Map;

import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.HYPHENATED_REF;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.LOCAL_AUTHORITY_SUBMIT_TO_COURT_ALERT;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.CHILD_FULL_NAME;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.LA_PORTAL_URL;

@Component
@Slf4j
// todo - still OK to implement this interface - does everything?
public class LocalAuthorityAlertToSubmitToCourt implements ApplicantNotification {

    @Autowired
    IdamService idamService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CommonContent commonContent;

    @Autowired
    private EmailTemplatesConfig emailTemplatesConfig;




    @Override
    public void sendLocalAuthorityAlertToSubmitToCourt(final CaseData caseData, final Long id) {
        log.info("Alerting Local Authority to submit case : {} to court.", id);

        final String childLocalAuthorityEmailAddress = caseData.getChildSocialWorker().getLocalAuthorityEmail();
        final String applicantLocalAuthorityEmailAddress = caseData.getApplicantSocialWorker().getLocalAuthorityEmail();

        notificationService.sendEmail(
                childLocalAuthorityEmailAddress,
                LOCAL_AUTHORITY_SUBMIT_TO_COURT_ALERT,
                templateVarsForLocalAuthority(caseData),
                LanguagePreference.ENGLISH
        );

        notificationService.sendEmail(
                applicantLocalAuthorityEmailAddress,
                LOCAL_AUTHORITY_SUBMIT_TO_COURT_ALERT,
                templateVarsForLocalAuthority(caseData),
                LanguagePreference.ENGLISH
        );
    }

    // Todo: This has been copied from ApplicationSubmittedNotification.  Should be a util?
    // Sonar lint is highlighting this issue, line 63
    private Map<String, Object> templateVarsForLocalAuthority(CaseData caseData) {
        Map<String, Object> templateVars = new HashMap<>();
        templateVars.put(HYPHENATED_REF, caseData.getHyphenatedCaseRef());
        String applicantFullName = caseData.getChildren().getFirstName() + " " + caseData.getChildren().getLastName();
        templateVars.put(CHILD_FULL_NAME, StringUtils.replace(applicantFullName, "'","\\'"));
        templateVars.put(LA_PORTAL_URL, emailTemplatesConfig.getTemplateVars().get(LA_PORTAL_URL));
        return templateVars;
    }
}
