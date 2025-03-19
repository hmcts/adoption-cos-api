package uk.gov.hmcts.reform.adoption.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference;
import uk.gov.hmcts.reform.adoption.common.config.EmailTemplatesConfig;
import uk.gov.hmcts.reform.adoption.idam.IdamService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.BLANK_SPACE;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.DATE_SUBMITTED;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.HYPHENATED_REF;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.LOCAL_AUTHORITY_SUBMIT_TO_COURT_ALERT;
import static uk.gov.hmcts.reform.adoption.notification.FormatUtil.DATE_TIME_FORMATTER;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.CHILD_FULL_NAME;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.LA_PORTAL_URL;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.LOCAL_COURT_NAME;

@Component
@Slf4j
@RequiredArgsConstructor
// todo - still OK to implement this interface - does everything?
public class LocalAuthorityAlertToSubmitToCourt implements ApplicantNotification {

    private final IdamService idamService;

    private final NotificationService notificationService;

    private final EmailTemplatesConfig emailTemplatesConfig;

    @Override
    public void sendLocalAuthorityAlertToSubmitToCourt(final CaseData caseData, final Long id) {
        log.info("Alerting Local Authority to submit case : {} to court.", id);

        final String childLocalAuthorityEmailAddress = caseData.getChildSocialWorker().getLocalAuthorityEmail();
        final String applicantLocalAuthorityEmailAddress = caseData.getApplicantSocialWorker().getLocalAuthorityEmail();

        notificationService.sendEmail(
                childLocalAuthorityEmailAddress,
                LOCAL_AUTHORITY_SUBMIT_TO_COURT_ALERT,
                getTemplateVarsForLocalAuthority(caseData),
                LanguagePreference.ENGLISH
        );

        notificationService.sendEmail(
                applicantLocalAuthorityEmailAddress,
                LOCAL_AUTHORITY_SUBMIT_TO_COURT_ALERT,
                getTemplateVarsForLocalAuthority(caseData),
                LanguagePreference.ENGLISH
        );
    }

    // This has been copied from ApplicationSubmittedNotification.  Should be a util?
    // The original function has linting issues that this version addresses.
    private Map<String, Object> getTemplateVarsForLocalAuthority(CaseData caseData) {
        Map<String, Object> templateVars = new HashMap<>();

        String hyphenatedCaseRef = caseData.getHyphenatedCaseRef();
        String familyCourtName = caseData.getFamilyCourtName();

        templateVars.put(HYPHENATED_REF, hyphenatedCaseRef);
        templateVars.put(LOCAL_COURT_NAME, familyCourtName);
        templateVars.put(CHILD_FULL_NAME, caseData.getChildren().getFirstName() + BLANK_SPACE + caseData.getChildren().getLastName());
        templateVars.put(DATE_SUBMITTED, Optional.ofNullable(caseData.getApplication().getDateSubmitted())
                .orElse(LocalDate.now()).format(DATE_TIME_FORMATTER));
        templateVars.put(LA_PORTAL_URL, emailTemplatesConfig.getTemplateVars().get(LA_PORTAL_URL));
        return templateVars;
    }
}
