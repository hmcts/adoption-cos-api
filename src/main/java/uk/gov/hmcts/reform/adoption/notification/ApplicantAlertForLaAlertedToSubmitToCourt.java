package uk.gov.hmcts.reform.adoption.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.BLANK_SPACE;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.DATE_SUBMITTED;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.HYPHENATED_REF;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.NO;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.YES;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.NOTIFY_APPLICANT_LA_REMINDED_TO_SUBMIT_ALERT;
import static uk.gov.hmcts.reform.adoption.notification.FormatUtil.DATE_TIME_FORMATTER;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.APPLICANT_1_FULL_NAME;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.APPLICANT_2_FULL_NAME;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.CHILD_FULL_NAME;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.HAS_SECOND_APPLICANT;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.LOCAL_COURT_NAME;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApplicantAlertForLaAlertedToSubmitToCourt {

    private final NotificationService notificationService;

    private static final String APPLICANT_1_ERROR =
        "Applicant 1 could not be alerted that LA has been alerted to submit case {}: Invalid email address '{}'";

    private static final String APPLICANT_2_ERROR =
        "Applicant 2 could not be alerted that LA has been alerted to submit case {}: Invalid email address '{}'";

    public void sendApplicantAlertForLaAlertedToSubmitToCourt(final CaseData caseData, final Long id) {
        final String applicant1Email = caseData.getApplicant1().getEmailAddress();
        final String applicant2Email = caseData.getApplicant2().getEmailAddress();
        final Map<String, Object> templateVars = getTemplateVarsForLocalAuthority(caseData);

        log.info("Alerting Applicant that LA has been alerted to submit case : {} to court.", id);

        validateAndSendEmailAlert(applicant1Email, id, templateVars, APPLICANT_1_ERROR);

        if (hasSecondApplicant(caseData)) {
            validateAndSendEmailAlert(applicant2Email, id, templateVars, APPLICANT_2_ERROR);
        }
    }

    private Map<String, Object> getTemplateVarsForLocalAuthority(CaseData caseData) {
        Map<String, Object> templateVars = new HashMap<>();

        String hyphenatedCaseRef = caseData.getHyphenatedCaseRef();
        String familyCourtName = caseData.getFamilyCourtName();

        templateVars.put(HYPHENATED_REF, hyphenatedCaseRef);
        templateVars.put(CHILD_FULL_NAME,
                         caseData.getChildren().getFirstName() + BLANK_SPACE + caseData.getChildren().getLastName());
        templateVars.put(APPLICANT_1_FULL_NAME, caseData.getApplicant1().getFirstName() + " "
            + caseData.getApplicant1().getLastName());
        templateVars.put(LOCAL_COURT_NAME, familyCourtName);
        templateVars.put(DATE_SUBMITTED, Optional.ofNullable(caseData.getApplication().getDateSubmitted())
            .orElse(LocalDate.now()).format(DATE_TIME_FORMATTER));

        if (hasSecondApplicant(caseData)) {
            templateVars.put(
                APPLICANT_2_FULL_NAME,
                caseData.getApplicant2().getFirstName() + " " + caseData.getApplicant2().getLastName()
            );
            templateVars.put(HAS_SECOND_APPLICANT, YES);
        } else {
            templateVars.put(HAS_SECOND_APPLICANT, NO);
            templateVars.put(APPLICANT_2_FULL_NAME, StringUtils.EMPTY);
        }

        return templateVars;
    }

    private void validateAndSendEmailAlert(String emailAddress, Long id, Map<String, Object> templateVar,
                                           String errorMsg) {
        EmailValidator validator = EmailValidator.getInstance();

        if (StringUtils.isBlank(emailAddress) || !validator.isValid(emailAddress)) {
            log.error(errorMsg, id, emailAddress);
        } else {
            notificationService.sendEmail(
                emailAddress,
                NOTIFY_APPLICANT_LA_REMINDED_TO_SUBMIT_ALERT,
                templateVar,
                LanguagePreference.ENGLISH
            );
        }
    }

    private boolean hasSecondApplicant(CaseData caseData) {
        return caseData.getApplicant2() != null && StringUtils.isNotBlank(caseData.getApplicant2().getEmailAddress());
    }
}
