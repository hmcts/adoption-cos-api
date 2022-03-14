package uk.gov.hmcts.reform.adoption.notification;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Applicant;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.document.DocumentManagementClient;
import uk.gov.hmcts.reform.adoption.document.DocumentType;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionDocument;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.service.notify.NotificationClientException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.APPLICATION_DOCUMENT_URL;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.DATE_SUBMITTED;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.DOCUMENT_EXISTS;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.DOCUMENT;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.DOCUMENT_EXISTS_CHECK;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.HYPHENATED_REF;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.NO;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.YES;
import static uk.gov.hmcts.reform.adoption.notification.CommonContent.SUBMISSION_RESPONSE_DATE;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.APPLICANT_APPLICATION_SUBMITTED;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.LOCAL_COURT_APPLICATION_SUBMITTED;
import static uk.gov.hmcts.reform.adoption.notification.FormatUtil.DATE_TIME_FORMATTER;
import static uk.gov.service.notify.NotificationClient.prepareUpload;

@Component
@Slf4j
public class ApplicationSubmittedNotification implements ApplicantNotification {

    @Value("${idam.systemupdate.username}")
    private String systemUpdateUserName;

    @Value("#{${pilot-courts}}")
    private Map<String, String> pilotCourts;

    @Autowired
    IdamService idamService;

    @Autowired
    private AuthTokenGenerator authTokenGenerator;

    @Autowired
    DocumentManagementClient dmClient;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CommonContent commonContent;

    @Override
    public void sendToApplicants(final CaseData caseData, final Long id) {
        log.info("Sending application submitted notification to applicants for case : {}", id);

        final String applicant1Email = caseData.getApplicant1().getEmailAddress();
        final String applicant2Email = caseData.getApplicant2().getEmailAddress();
        final LanguagePreference applicant1LanguagePreference = caseData.getApplicant1().getLanguagePreference();
        final LanguagePreference applicant2LanguagePreference = caseData.getApplicant2().getLanguagePreference();

        Map<String, Object> templateVars = templateVars(caseData, id, caseData.getApplicant1(), caseData.getApplicant2());
        notificationService.sendEmail(
            applicant1Email,
            APPLICANT_APPLICATION_SUBMITTED,
            templateVars,
            applicant1LanguagePreference != null
                ? applicant1LanguagePreference : LanguagePreference.ENGLISH
        );

        if (StringUtils.isNotBlank(applicant2Email)) {
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
    public void sendToLocalAuthority(final CaseData caseData, final Long id) {
        log.info("Sending application submitted notification to local authority for case : {}", id);

        notificationService.sendEmail(
            caseData.getApplicant1().getEmail(),
            APPLICANT_APPLICATION_SUBMITTED,
            templateVars(caseData, id, caseData.getApplicant1(), caseData.getApplicant2()),
            caseData.getApplicant1().getLanguagePreference() != null
                ? caseData.getApplicant1().getLanguagePreference() : LanguagePreference.ENGLISH
        );
    }


    @Override
    public void sendToLocalCourt(final CaseData caseData, final Long id) throws NotificationClientException, IOException {
        log.info("Sending application submitted notification to local authority for case : {}", id);

        notificationService.sendEmail(
            caseData.getFamilyCourtEmailId(),
            LOCAL_COURT_APPLICATION_SUBMITTED,
            templateVarsLocalCourt(caseData, id),
            caseData.getApplicant1().getLanguagePreference() != null
                ? caseData.getApplicant1().getLanguagePreference() : LanguagePreference.ENGLISH
        );
    }

    private Map<String, Object> templateVars(CaseData caseData, Long id, Applicant applicant1, Applicant applicant2) {
        Map<String, Object> templateVars = commonContent.mainTemplateVars(caseData, id, applicant1, applicant2);
        templateVars.put(SUBMISSION_RESPONSE_DATE, caseData.getDueDate() != null
            ? caseData.getDueDate().format(DATE_TIME_FORMATTER) : LocalDate.now().format(DATE_TIME_FORMATTER));

        return templateVars;
    }

    private Map<String, Object> templateVarsLocalCourt(CaseData caseData, Long id)
        throws IOException, NotificationClientException {
        Map<String, Object> templateVars = new HashMap<>();
        templateVars.put(HYPHENATED_REF, caseData.getHyphenatedCaseRef());
        templateVars.put(DATE_SUBMITTED, Optional.ofNullable(caseData.getApplication().getDateSubmitted())
            .orElse(LocalDateTime.now()).format(DATE_TIME_FORMATTER));
        int i = 0;
        for (i = 1; i < 11; i++) {
            templateVars.put(DOCUMENT_EXISTS + i, NO);
            templateVars.put(DOCUMENT + i, StringUtils.EMPTY);
        }
        templateVars.put(DOCUMENT_EXISTS_CHECK, NO);

        final String authorisation = idamService.retrieveSystemUpdateUserDetails().getAuthToken();
        String serviceAuthorization = authTokenGenerator.generate();

        AdoptionDocument adoptionDocument = caseData.getDocumentsGenerated().stream().map(item -> item.getValue())
            .filter(item -> item.getDocumentType().equals(DocumentType.APPLICATION_SUMMARY))
            .findFirst().orElse(null);

        Resource document = dmClient.downloadBinary(authorisation, serviceAuthorization, UserRole.CASE_WORKER_SYSTEM.getRole(),
                                                    systemUpdateUserName, adoptionDocument.getDocumentFileId()).getBody();
        byte[] documentContents = document.getInputStream().readAllBytes();

        templateVars.put(APPLICATION_DOCUMENT_URL, prepareUpload(documentContents));

        if (caseData.getApplicant1DocumentsUploaded() != null) {
            List<String> uploadedDocumentsUrls = caseData.getApplicant1DocumentsUploaded().stream().map(item -> item.getValue())
                .map(item -> item.getDocumentFileId())
                .collect(Collectors.toList());

            i = 1;
            for (String item : uploadedDocumentsUrls) {
                Resource uploadedDocument = dmClient.downloadBinary(authorisation, serviceAuthorization,
                                                                    UserRole.CASE_WORKER_SYSTEM.getRole(),
                                                                    systemUpdateUserName, item).getBody();
                byte[] uploadedDocumentContents = uploadedDocument.getInputStream().readAllBytes();
                templateVars.put(DOCUMENT_EXISTS + i, YES);
                templateVars.put(DOCUMENT + i++, prepareUpload(uploadedDocumentContents));
            }
        }
        return templateVars;
    }
}
