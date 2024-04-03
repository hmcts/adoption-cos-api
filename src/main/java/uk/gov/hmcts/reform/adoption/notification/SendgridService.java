package uk.gov.hmcts.reform.adoption.notification;

import com.sendgrid.Attachments;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.document.CaseDocumentClient;
import uk.gov.hmcts.reform.adoption.document.DocumentType;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionDocument;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.UUID;

import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.LOCAL_COURT_EMAIL_SENDGRID_ATTACHMENT_MIME_TYPE;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.LOCAL_COURT_EMAIL_SENDGRID_CONTENT_BODY;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.LOCAL_COURT_EMAIL_SENDGRID_CONTENT_TYPE;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.LOCAL_COURT_EMAIL_SENDGRID_DISPOSITION_ATTACHMENT;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.LOCAL_COURT_EMAIL_SENDGRID_ENDPOINT;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SendgridService {

    @Value("${send-grid.api-key}")
    private String apiKey;

    @Autowired
    private CaseDocumentClient caseDocumentClient;

    @Autowired
    private IdamService idamService;

    @Autowired
    private AuthTokenGenerator authTokenGenerator;

    @Value("${send-grid.notify-from-email}")
    private String sendGridNotifyFromEmail;

    @Retryable(backoff = @Backoff(delay = 10, maxDelay = 1200, multiplier = 10))
    public void sendEmail(CaseData caseData, String subject, DocumentType documentType) throws IOException {
        String caseIdForLogging =
            caseData.getHyphenatedCaseRef() != null ? caseData.getHyphenatedCaseRef().replace("-","") : null;

        log.info("SendgridService.sendEmail: Starting for case : {}", caseIdForLogging);

        Content content = new Content(LOCAL_COURT_EMAIL_SENDGRID_CONTENT_TYPE, LOCAL_COURT_EMAIL_SENDGRID_CONTENT_BODY);
        log.info("SendgridService.sendEmail: Sendgrid email for case : {} to be sent to court address: {} ",
                 caseIdForLogging, caseData.getFamilyCourtEmailId());

        Mail mail = new Mail(new Email(sendGridNotifyFromEmail), subject, new Email(caseData.getFamilyCourtEmailId()), content);

        AdoptionDocument adoptionDocument = caseData.getDocumentsGenerated().stream().map(ListValue::getValue)
            .filter(item -> item.getDocumentType().equals(documentType))
            .findFirst().orElse(null);

        final String authorisation = idamService.retrieveSystemUpdateUserDetails().getAuthToken();
        String serviceAuthorization = authTokenGenerator.generate();

        log.info("SendgridService.sendEmail: About to call getDocumentBinary method to fetch document binary for case : {}",
                 caseIdForLogging);
        Attachments attachments = new Attachments();
        attachGeneratedDocuments(attachments, mail, adoptionDocument, authorisation, serviceAuthorization, caseIdForLogging);
        attachUploadedDocuments(caseData, attachments, mail, authorisation, serviceAuthorization, caseIdForLogging);

        log.info("SendgridService.sendEmail: About to send email for case : {}", caseIdForLogging);
        SendGrid sg = getSendGrid(caseIdForLogging);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint(LOCAL_COURT_EMAIL_SENDGRID_ENDPOINT);
            request.setBody(mail.build());
            sg.api(request);
            log.info("SendgridService.sendEmail: Notification email to Local Court sent successfully for case : {}", caseIdForLogging);

            //TODO for testing @Retryable: REMOVE
            exceptionCauser();
        } catch (IOException ex) {
            log.error("SendgridService.sendEmail: Notification email request attempt to Local Court failed for case : {}",
                      caseIdForLogging,ex);
            throw ex;
        }
    }

    void exceptionCauser() {  //TODO remove
        int zero = 0;
        int notGoingToHappen = 12 / zero;
        log.info("This should have thrown an ArithmeticException: " + notGoingToHappen);
    }

    @Recover //TODO comment out to investigate unhandled exceptions
    public void recover(Exception ex, CaseData caseData) {
        String caseIdForLogging =
            caseData.getHyphenatedCaseRef() != null ? caseData.getHyphenatedCaseRef().replace("-","") : null;
        log.error("SendgridService.recover: Notification email to Local Court failed for case : {}",
                  caseIdForLogging, ex);
        //TODO: find out what happens with unhandled exceptions
        //TODO: rethrow ex - if adoption has a way of handling unhandled exceptions and sending to monitoring this would be ideal
        //throw ex;
    }

    private void attachGeneratedDocuments(Attachments attachments, Mail mail, AdoptionDocument adoptionDocument,
                                          String authorisation, String serviceAuthorization, final String caseIdForLogging) {
        log.info("SendgridService.attachGeneratedDocuments: Starting for case : {}", caseIdForLogging);
        if (adoptionDocument != null) {
            Resource document = caseDocumentClient.getDocumentBinary(
                authorisation,
                serviceAuthorization,
                UUID.fromString(adoptionDocument.getDocumentFileId())).getBody();
            log.info("SendgridService.attachGeneratedDocuments: call to getDocumentBinary method successful for case : {}",
                     caseIdForLogging);
            String data = null;
            try (InputStream inputStream = document.getInputStream()) {
                byte[] documentContents = inputStream.readAllBytes();
                data = Base64.getEncoder().encodeToString(documentContents);
            } catch (Exception e) {
                log.error("SendgridService.attachGeneratedDocuments: DocumentId {} could not be read for case {}",
                          adoptionDocument.getDocumentFileId(), caseIdForLogging, e);
            }
            attachments.setContent(data);
            attachments.setFilename(adoptionDocument.getDocumentFileName());
            attachments.setType(LOCAL_COURT_EMAIL_SENDGRID_ATTACHMENT_MIME_TYPE);
            attachments.setDisposition(LOCAL_COURT_EMAIL_SENDGRID_DISPOSITION_ATTACHMENT);
            mail.addAttachments(attachments);
            log.info("SendgridService.attachGeneratedDocuments: Document attached successfully for case : {}", caseIdForLogging);
        } else {
            log.info("SendgridService.attachGeneratedDocuments: Document not found for CUI Docmosis for case : {}", caseIdForLogging);
        }
    }

    private void attachUploadedDocuments(CaseData caseData, Attachments attachments, Mail mail,
                                         String authorisation, String serviceAuthorization, final String caseIdForLogging) {
        log.info("SendgridService.attachUploadedDocuments: Starting for case : {}", caseIdForLogging);
        if (caseData.getLaDocumentsUploaded() != null) {
            caseData.getLaDocumentsUploaded().stream().map(ListValue::getValue)
                .forEach(item -> fetchAndAttachDoc(item, attachments, mail, authorisation, serviceAuthorization, caseIdForLogging));
        }
        log.info("SendgridService.attachUploadedDocuments: Finished for case : {}", caseIdForLogging);
    }

    private void fetchAndAttachDoc(AdoptionDocument item, Attachments attachments,
                                   Mail mail, String authorisation, String serviceAuthorization, final String caseIdForLogging) {
        String documentId = StringUtils.substringAfterLast(item.getDocumentLink().getUrl(), "/");
        log.info("SendgridService.fetchAndAttachDoc: documentId: {} starting for case : {}", documentId, caseIdForLogging);
        log.info("SendgridService.fetchAndAttachDoc: About to call getDocumentBinary method "
                     + "to fetch uploaded document(s) binary for case : {}", caseIdForLogging);
        ResponseEntity<Resource> resource =  caseDocumentClient.getDocumentBinary(
            authorisation, serviceAuthorization, UUID.fromString(documentId));
        log.info("SendgridService.fetchAndAttachDoc: After calling caseDocumentClient "
                     + "service with status code {}:", resource.getStatusCode());
        Resource uploadedDocument = resource.getBody();
        if (uploadedDocument != null) {
            String data = null;
            try (InputStream inputStream = uploadedDocument.getInputStream()) {
                byte[] documentContents = inputStream.readAllBytes();
                data = Base64.getEncoder().encodeToString(documentContents);
            } catch (Exception e) {
                log.error("SendgridService.fetchAndAttachDoc: Document could not be read for case : {}", caseIdForLogging, e);
            }

            attachments.setContent(data);
            attachments.setFilename(item.getDocumentFileName());
            attachments.setDisposition(LOCAL_COURT_EMAIL_SENDGRID_DISPOSITION_ATTACHMENT);
            mail.addAttachments(attachments);
            log.info("SendgridService.fetchAndAttachDoc:  Document attached successfully for case : {}", caseIdForLogging);
        } else {
            log.info("SendgridService.fetchAndAttachDoc: Document not found with uuid : {} for case : {}",
                     UUID.fromString(item.getDocumentFileId()), caseIdForLogging);
        }
    }

    SendGrid getSendGrid(final String caseIdForLogging) {
        SendGrid sendGrid = null;
        try {
            sendGrid = new SendGrid(apiKey);
        } catch (Exception ex) {
            log.error("ApplicationSubmittedNotification.getSendGrid: SendGrid instantiation failed for case : {} ",
                      caseIdForLogging, ex);
        }
        return sendGrid;
    }

}
