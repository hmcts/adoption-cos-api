package uk.gov.hmcts.reform.adoption.notification;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Applicant;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference;
import uk.gov.hmcts.reform.adoption.common.config.EmailTemplatesConfig;
import uk.gov.hmcts.reform.adoption.document.CaseDocumentClient;
import uk.gov.hmcts.reform.adoption.document.DocumentType;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.service.notify.NotificationClientException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.BLANK_SPACE;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.DATE_SUBMITTED;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.HYPHENATED_REF;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.NO;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.YES;
import static uk.gov.hmcts.reform.adoption.notification.CommonContent.SUBMISSION_RESPONSE_DATE;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.LOCAL_AUTHORITY_APPLICATION_SUBMITTED;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.APPLICANT_APPLICATION_SUBMITTED;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.APPLICATION_SUBMITTED_TO_LOCAL_AUTHORITY;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.LOCAL_AUTHORITY_APPLICATION_SUBMITTED_ACKNOWLEDGE_CITIZEN;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.LOCAL_COURT_APPLICATION_SUBMITTED;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.LOCAL_COURT_APPLICATION_SUBMITTED_BY_LOCAL_AUTHORITY;
import static uk.gov.hmcts.reform.adoption.notification.FormatUtil.DATE_TIME_FORMATTER;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.ADOPTION_CUI_MULTI_CHILDREN_URL;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.APPLICANT_1_FULL_NAME;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.LA_PORTAL_URL;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.DRAFT_LOCAL_COURT_EMAIL_SENDGRID_SUBJECT_LINE1;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.LOCAL_COURT_EMAIL_SENDGRID_SUBJECT_LINE1;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.LOCAL_COURT_EMAIL_SENDGRID_SUBJECT_LINE2;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.LOCAL_COURT_NAME;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.APPLICANT_2_FULL_NAME;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.HAS_SECOND_APPLICANT;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.CHILD_FULL_NAME;


@Component
@Slf4j
public class ApplicationSubmittedNotification implements ApplicantNotification {


    @Autowired
    IdamService idamService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CommonContent commonContent;

    @Autowired
    private EmailTemplatesConfig emailTemplatesConfig;

    @Autowired
    private AuthTokenGenerator authTokenGenerator;

    @Autowired
    private CaseDocumentClient caseDocumentClient;

    @Autowired
    SendgridService sendgridService;

    @Override
    public void sendToApplicants(final CaseData caseData, final Long id) {
        log.info("Sending application submitted notification to applicants for case : {}", id);

        final String applicant1Email = caseData.getApplicant1().getEmailAddress();
        final String applicant2Email = caseData.getApplicant2().getEmailAddress();
        final LanguagePreference applicant1LanguagePreference = caseData.getApplicant1().getLanguagePreference();

        Map<String, Object> templateVars = templateVars(caseData, id, caseData.getApplicant1(), caseData.getApplicant2());
        notificationService.sendEmail(
            applicant1Email,
            APPLICANT_APPLICATION_SUBMITTED,
            templateVars,
            applicant1LanguagePreference != null
                ? applicant1LanguagePreference : LanguagePreference.ENGLISH
        );

        if (StringUtils.isNotBlank(applicant2Email)) {
            final LanguagePreference applicant2LanguagePreference = caseData.getApplicant2().getLanguagePreference();

            notificationService.sendEmail(
                applicant2Email,
                APPLICANT_APPLICATION_SUBMITTED,
                templateVars,
                applicant2LanguagePreference != null
                    ? applicant2LanguagePreference : LanguagePreference.ENGLISH
            );
        }
    }

    @Override
    public void sendToCaseWorker(final CaseData caseData, final Long id) {
        log.info("Sending application submitted notification to case worker for case : {}", id);

        notificationService.sendEmail(
            caseData.getApplicant1().getEmail(),
            APPLICANT_APPLICATION_SUBMITTED,
            templateVars(caseData, id, caseData.getApplicant1(), caseData.getApplicant2()),
            caseData.getApplicant1().getLanguagePreference()
        );
    }


    @Override
    public void sendToLocalAuthorityPostApplicantSubmission(final CaseData caseData, final Long id) {
        log.info("Sending application submitted notification to local authority post Applicant Submission for case : {}", id);

        final String childLocalAuthorityEmailAddress = caseData.getChildSocialWorker().getLocalAuthorityEmail();
        final String applicantLocalAuthorityEmailAddress = caseData.getApplicantSocialWorker().getLocalAuthorityEmail();

        notificationService.sendEmail(
            childLocalAuthorityEmailAddress,
            APPLICATION_SUBMITTED_TO_LOCAL_AUTHORITY,
            templateVarsForLocalAuthority(caseData),
            LanguagePreference.ENGLISH
        );

        notificationService.sendEmail(
            applicantLocalAuthorityEmailAddress,
            APPLICATION_SUBMITTED_TO_LOCAL_AUTHORITY,
            templateVarsForLocalAuthority(caseData),
            LanguagePreference.ENGLISH
        );
    }

    @Override
    public void sendToLocalAuthorityPostLocalAuthoritySubmission(final CaseData caseData, final Long id) {
        log.info("Sending application submitted notification to local authority post "
                     + "Local Authority application Submission for case : {}", id);

        final String childLocalAuthorityEmailAddress = caseData.getChildSocialWorker().getLocalAuthorityEmail();
        final String applicantLocalAuthorityEmailAddress = caseData.getApplicantSocialWorker().getLocalAuthorityEmail();

        notificationService.sendEmail(
            childLocalAuthorityEmailAddress,
            LOCAL_AUTHORITY_APPLICATION_SUBMITTED,
            templateVarsForLocalAuthority(caseData),
            LanguagePreference.ENGLISH
        );

        notificationService.sendEmail(
            applicantLocalAuthorityEmailAddress,
            LOCAL_AUTHORITY_APPLICATION_SUBMITTED,
            templateVarsForLocalAuthority(caseData),
            LanguagePreference.ENGLISH
        );
    }


    @Override
    public void sendToLocalCourt(final CaseData caseData, final Long id) {
        log.info("Sending application submitted notification to local authority for case : {}", id);

        notificationService.sendEmail(
            caseData.getFamilyCourtEmailId(),
            LOCAL_COURT_APPLICATION_SUBMITTED,
            templateVarsLocalCourt(caseData, id),
            LanguagePreference.ENGLISH
        );

        try {
            log.info("Calling SendGrid method inside "
                         + "sendToLocalCourt for case : {}", id);
            String subject = DRAFT_LOCAL_COURT_EMAIL_SENDGRID_SUBJECT_LINE1 + caseData.getHyphenatedCaseRef()
                + LOCAL_COURT_EMAIL_SENDGRID_SUBJECT_LINE2
                + caseData.getChildren().getFirstName() + BLANK_SPACE + caseData.getChildren().getLastName();
            sendgridService.sendEmail(caseData, subject, caseData.getApplicant1().getLanguagePreference()
                .equals(LanguagePreference.ENGLISH) ? DocumentType.APPLICATION_SUMMARY_EN : DocumentType.APPLICATION_SUMMARY_CY);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private Map<String, Object> templateVars(CaseData caseData, Long id, Applicant applicant1, Applicant applicant2) {
        Map<String, Object> templateVars = commonContent.mainTemplateVars(caseData, id, applicant1, applicant2);
        templateVars.put(SUBMISSION_RESPONSE_DATE, caseData.getDueDate() != null
            ? caseData.getDueDate().format(DATE_TIME_FORMATTER) : LocalDate.now().format(DATE_TIME_FORMATTER));
        templateVars.put(HYPHENATED_REF, caseData.getHyphenatedCaseRef());
        templateVars.put(APPLICANT_1_FULL_NAME, caseData.getApplicant1().getFirstName() + " " + caseData.getApplicant1().getLastName());
        templateVars.put(LOCAL_COURT_NAME, caseData.getFamilyCourtName());
        if (caseData.getApplicant2() != null && StringUtils.isNotBlank(caseData.getApplicant2().getEmailAddress())) {
            templateVars.put(
                APPLICANT_2_FULL_NAME,
                caseData.getApplicant2().getFirstName() + " " + caseData.getApplicant2().getLastName()
            );
            templateVars.put(HAS_SECOND_APPLICANT, YES);
        } else {
            templateVars.put(HAS_SECOND_APPLICANT, NO);
            templateVars.put(APPLICANT_2_FULL_NAME, StringUtils.EMPTY);
        }
        templateVars.put(ADOPTION_CUI_MULTI_CHILDREN_URL, emailTemplatesConfig.getTemplateVars().get(ADOPTION_CUI_MULTI_CHILDREN_URL));
        return templateVars;
    }


    private Map<String, Object> templateVarsForLocalAuthority(CaseData caseData) {
        Map<String, Object> templateVars = new HashMap<>();
        templateVars.put(HYPHENATED_REF, caseData.getHyphenatedCaseRef());
        templateVars.put(CHILD_FULL_NAME, caseData.getChildren().getFirstName() + " " + caseData.getChildren().getLastName());
        templateVars.put(LA_PORTAL_URL, emailTemplatesConfig.getTemplateVars().get(LA_PORTAL_URL));
        return templateVars;
    }

    public void sendToApplicantsPostLocalAuthoritySubmission(CaseData caseData, Long caseId) {
        log.info("Sending Local Authority application submitted notification to applicants for case : {}", caseId);

        final String applicant1Email = caseData.getApplicant1().getEmailAddress();
        final String applicant2Email = caseData.getApplicant2().getEmailAddress();
        final LanguagePreference applicant1LanguagePreference = caseData.getApplicant1().getLanguagePreference();

        Map<String, Object> templateVars = templateVars(caseData, caseId, caseData.getApplicant1(), caseData.getApplicant2());
        notificationService.sendEmail(
            applicant1Email,
            LOCAL_AUTHORITY_APPLICATION_SUBMITTED_ACKNOWLEDGE_CITIZEN,
            templateVars,
            applicant1LanguagePreference != null
                ? applicant1LanguagePreference : LanguagePreference.ENGLISH
        );

        if (StringUtils.isNotBlank(applicant2Email)) {
            final LanguagePreference applicant2LanguagePreference = caseData.getApplicant2().getLanguagePreference();

            notificationService.sendEmail(
                applicant2Email,
                LOCAL_AUTHORITY_APPLICATION_SUBMITTED_ACKNOWLEDGE_CITIZEN,
                templateVars,
                applicant2LanguagePreference != null
                    ? applicant2LanguagePreference : LanguagePreference.ENGLISH
            );
        }
    }

    @Override
    public void sendToLocalCourtPostLocalAuthoritySubmission(final CaseData caseData, final Long id)
        throws NotificationClientException, IOException {
        log.info("Sending notification to Local Courts after application submitted by Local Authority for case : {}", id);

        notificationService.sendEmail(
            caseData.getFamilyCourtEmailId(),
            LOCAL_COURT_APPLICATION_SUBMITTED_BY_LOCAL_AUTHORITY,
            templateVarsLocalCourt(caseData, id),
            LanguagePreference.ENGLISH
        );

        try {
            log.info("Calling SendGrid method inside "
                         + "sendToLocalCourtPostLocalAuthoritySubmission for case : {}", id);
            String subject = LOCAL_COURT_EMAIL_SENDGRID_SUBJECT_LINE1 + caseData.getHyphenatedCaseRef()
                + LOCAL_COURT_EMAIL_SENDGRID_SUBJECT_LINE2
                + caseData.getChildren().getFirstName() + BLANK_SPACE + caseData.getChildren().getLastName();
            sendgridService.sendEmail(caseData, subject, DocumentType.APPLICATION_LA_SUMMARY_EN);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> templateVarsLocalCourt(CaseData caseData, Long id) {
        Map<String, Object> templateVars = new HashMap<>();
        templateVars.put(HYPHENATED_REF, caseData.getHyphenatedCaseRef());
        templateVars.put(LOCAL_COURT_NAME, caseData.getFamilyCourtName());
        templateVars.put(CHILD_FULL_NAME, caseData.getChildren().getFirstName() + BLANK_SPACE + caseData.getChildren().getLastName());
        templateVars.put(DATE_SUBMITTED, Optional.ofNullable(caseData.getApplication().getDateSubmitted())
            .orElse(LocalDate.now()).format(DATE_TIME_FORMATTER));
        templateVars.put(LA_PORTAL_URL, emailTemplatesConfig.getTemplateVars().get(LA_PORTAL_URL));
        return templateVars;
    }

}
