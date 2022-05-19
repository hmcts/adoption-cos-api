package uk.gov.hmcts.reform.adoption.notification;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Applicant;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference;
import uk.gov.hmcts.reform.adoption.document.DocumentManagementClient;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.service.notify.NotificationClientException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.HYPHENATED_REF;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.NO;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.YES;
import static uk.gov.hmcts.reform.adoption.notification.CommonContent.SUBMISSION_RESPONSE_DATE;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.APPLICANT_APPLICATION_SUBMITTED;
import static uk.gov.hmcts.reform.adoption.notification.FormatUtil.DATE_TIME_FORMATTER;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.APPLICANT_1_FULL_NAME;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.APPLICANT_2_FULL_NAME;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.HAS_SECOND_APPLICANT;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.LOCAL_COURT_NAME;

@Component
@Slf4j
public class ApplicationSubmittedNotification implements ApplicantNotification {

    //    @Value("${idam.systemupdate.username}")
    //    private String systemUpdateUserName;

    @Autowired
    IdamService idamService;

    //    @Autowired
    //    private AuthTokenGenerator authTokenGenerator;

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
                applicant1LanguagePreference != null
                    ? applicant1LanguagePreference : LanguagePreference.ENGLISH
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
        //        TODO
        //        notificationService.sendEmail(
        //            caseData.getFamilyCourtEmailId(),
        //            LOCAL_COURT_APPLICATION_SUBMITTED,
        //            templateVarsLocalCourt(caseData, id),
        //            LanguagePreference.ENGLISH
        //        );
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

        return templateVars;
    }

    //    private Map<String, Object> templateVarsLocalCourt(CaseData caseData, Long id)
    //        throws IOException, NotificationClientException {
    //        Map<String, Object> templateVars = new HashMap<>();
    //        templateVars.put(HYPHENATED_REF, caseData.getHyphenatedCaseRef());
    //        templateVars.put(DATE_SUBMITTED, Optional.ofNullable(caseData.getApplication().getDateSubmitted())
    //            .orElse(LocalDateTime.now()).format(DATE_TIME_FORMATTER));
    //        int count = 0;
    //        for (count = 1; count < 11; count++) {
    //            templateVars.put(DOCUMENT_EXISTS + count, NO);
    //            templateVars.put(DOCUMENT + count, StringUtils.EMPTY);
    //        }
    //        templateVars.put(DOCUMENT_EXISTS_CHECK, NO);
    //
    //        final String authorisation = idamService.retrieveSystemUpdateUserDetails().getAuthToken();
    //        String serviceAuthorization = authTokenGenerator.generate();
    //
    //        AdoptionDocument adoptionDocument = caseData.getDocumentsGenerated().stream().map(item -> item.getValue())
    //            .filter(item -> item.getDocumentType().equals(DocumentType.APPLICATION_SUMMARY_EN))
    //            .findFirst().orElse(null);
    //
    //        if (adoptionDocument != null) {
    //            log.info("Test for adoption document: {} and fileID: {}", adoptionDocument.getDocumentFileName(),
    //                     adoptionDocument.getDocumentFileId());
    //            Resource document = dmClient.downloadBinary(authorisation,
    //                                                        serviceAuthorization,
    //                                                        UserRole.CASE_WORKER.getRole(),
    //                                                        systemUpdateUserName,
    //                                                        StringUtils.substringAfterLast(
    //                                                            adoptionDocument.getDocumentLink().getUrl(), "/")
    //            ).getBody();
    //
    //            if (document != null) {
    //                try (InputStream inputStream = document.getInputStream()) {
    //                    if (inputStream != null) {
    //                        byte[] documentContents = inputStream.readAllBytes();
    //                        templateVars.put(APPLICATION_DOCUMENT_URL, prepareUpload(documentContents));
    //                    }
    //                } catch (Exception e) {
    //                    log.error("Document could not be read");
    //                }
    //            }
    //        }
    //        if (caseData.getApplicant1DocumentsUploaded() != null) {
    //            List<String> uploadedDocumentsUrls = caseData.getApplicant1DocumentsUploaded().stream().map(item -> item.getValue())
    //                .map(item -> StringUtils.substringAfterLast(item.getDocumentLink().getUrl(), "/"))
    //                .collect(Collectors.toList());
    //
    //            count = 1;
    //            for (String item : uploadedDocumentsUrls) {
    //                Resource uploadedDocument = dmClient.downloadBinary(authorisation, serviceAuthorization,
    //                                                                    UserRole.CASE_WORKER.getRole(),
    //                                                                    systemUpdateUserName, item).getBody();
    //                if (uploadedDocument != null) {
    //                    byte[] uploadedDocumentContents = uploadedDocument.getInputStream().readAllBytes();
    //                    templateVars.put(DOCUMENT_EXISTS + count, YES);
    //                    templateVars.put(DOCUMENT + count++, prepareUpload(uploadedDocumentContents));
    //                }
    //            }
    //        }
    //        return templateVars;
    //    }
}
