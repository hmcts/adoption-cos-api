package uk.gov.hmcts.reform.adoption.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference;
import uk.gov.hmcts.reform.adoption.common.config.EmailTemplatesConfig;

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
public class LocalAuthorityAlertToSubmitToCourt {

    private final NotificationService notificationService;

    private final EmailTemplatesConfig emailTemplatesConfig;

    public void sendLocalAuthorityAlertToSubmitToCourt(final CaseData caseData, final Long id) {
        log.info("Alerting Local Authority to submit case : {} to court.", id);

        final String childLocalAuthorityEmailAddress = caseData.getChildSocialWorker().getLocalAuthorityEmail();
        final String applicantLocalAuthorityEmailAddress = caseData.getApplicantSocialWorker().getLocalAuthorityEmail();

        EmailValidator validator = EmailValidator.getInstance();

        if (StringUtils.isBlank(childLocalAuthorityEmailAddress) || !validator.isValid(childLocalAuthorityEmailAddress)) {
            log.error(
                "Child local authority could not be alerted to submit case {} because the email address ({}) was invalid",
                id,
                childLocalAuthorityEmailAddress
            );
        } else {
            notificationService.sendEmail(
                childLocalAuthorityEmailAddress,
                LOCAL_AUTHORITY_SUBMIT_TO_COURT_ALERT,
                getTemplateVarsForLocalAuthority(caseData),
                LanguagePreference.ENGLISH
            );
        }

        if (StringUtils.isBlank(applicantLocalAuthorityEmailAddress) || !validator.isValid(applicantLocalAuthorityEmailAddress)) {
            log.error("Applicant local authority could not be alerted to submit case {} because the email address ({}) was invalid",
                      id,
                      applicantLocalAuthorityEmailAddress
            );
            return;
        }
        notificationService.sendEmail(
            applicantLocalAuthorityEmailAddress,
            LOCAL_AUTHORITY_SUBMIT_TO_COURT_ALERT,
            getTemplateVarsForLocalAuthority(caseData),
            LanguagePreference.ENGLISH
        );
    }

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
