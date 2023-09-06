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

    public void sendEmail(CaseData caseData, String subject, DocumentType documentType) throws IOException {
        log.info("Inside sendEmail method of SendGrid class for case : {}", caseData.getHyphenatedCaseRef());
        Content content = new Content(LOCAL_COURT_EMAIL_SENDGRID_CONTENT_TYPE, LOCAL_COURT_EMAIL_SENDGRID_CONTENT_BODY);

        log.info("Sendgrid email to be sent to court address: {} ",caseData.getFamilyCourtEmailId());
        sendGridNotifyFromEmail = "ca@mail-prl-nonprod.aat.platform.hmcts.net";
        Mail mail = new Mail(new Email(sendGridNotifyFromEmail), subject, new Email("mohit.vijay@hmcts.net"), content);
        AdoptionDocument adoptionDocument = caseData.getDocumentsGenerated().stream().map(ListValue::getValue)
            .filter(item -> item.getDocumentType().equals(documentType))
            .findFirst().orElse(null);
        final String authorisation = idamService.retrieveSystemUpdateUserDetails().getAuthToken();
        String serviceAuthorization = authTokenGenerator.generate();
        log.info("About to call getDocumentBinary method to fetch document binary");
        Attachments attachments = new Attachments();
        attachGeneratedDocuments(attachments, mail, adoptionDocument, authorisation, serviceAuthorization);
        attachUploadedDocuments(caseData, attachments, mail, authorisation, serviceAuthorization);

        log.info("before sending email for case : {}", caseData.getHyphenatedCaseRef());
        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint(LOCAL_COURT_EMAIL_SENDGRID_ENDPOINT);
            request.setBody(mail.build());
            sg.api(request);
            log.info("Notification email to Local Court sent successfully");
        } catch (IOException ex) {
            log.error("Notification email to Local Court failed {}",ex.getMessage());
        }
    }

    private void attachGeneratedDocuments(Attachments attachments, Mail mail, AdoptionDocument adoptionDocument,
                                          String authorisation, String serviceAuthorization) {
        if (adoptionDocument != null) {
            Resource document = caseDocumentClient.getDocumentBinary(
                authorisation,
                serviceAuthorization,
                UUID.fromString(adoptionDocument.getDocumentFileId())).getBody();
            log.info("call to getDocumentBinary method successful");
            String data = null;
            try (InputStream inputStream = document.getInputStream()) {
                byte[] documentContents = inputStream.readAllBytes();
                data = Base64.getEncoder().encodeToString(documentContents);
            } catch (Exception e) {
                log.error("Document could not be read");
            }
            attachments.setContent(data);
            attachments.setFilename(adoptionDocument.getDocumentFileName());
            attachments.setType(LOCAL_COURT_EMAIL_SENDGRID_ATTACHMENT_MIME_TYPE);
            attachments.setDisposition(LOCAL_COURT_EMAIL_SENDGRID_DISPOSITION_ATTACHMENT);
            mail.addAttachments(attachments);
        } else {
            log.info("Document not found for CUI Docmosis");
        }
    }

    private void attachUploadedDocuments(CaseData caseData, Attachments attachments,
                                         Mail mail, String authorisation, String serviceAuthorization) {
        if (caseData.getLaDocumentsUploaded() != null) {
            caseData.getLaDocumentsUploaded().stream().map(ListValue::getValue)
                .forEach(item -> fetchAndAttachDoc(item, attachments, mail, authorisation, serviceAuthorization));
        }
    }

    private void fetchAndAttachDoc(AdoptionDocument item, Attachments attachments,
                                   Mail mail, String authorisation, String serviceAuthorization) {
        String url = StringUtils.substringAfterLast(item.getDocumentLink().getUrl(), "/");
        log.info("About to call getDocumentBinary method to fetch uploaded document(s) binary");
        ResponseEntity<Resource> resource =  caseDocumentClient.getDocumentBinary(
            authorisation, serviceAuthorization, UUID.fromString(url));
        log.info("After calling caseDocumentClient "
                     + "service with status code {}:", resource.getStatusCode());
        Resource uploadedDocument = resource.getBody();
        if (uploadedDocument != null) {
            String data = null;
            try (InputStream inputStream = uploadedDocument.getInputStream()) {
                byte[] documentContents = inputStream.readAllBytes();
                data = Base64.getEncoder().encodeToString(documentContents);
            } catch (Exception e) {
                log.error("Document could not be read");
            }

            attachments.setContent(data);
            attachments.setFilename(item.getDocumentFileName());
            attachments.setDisposition(LOCAL_COURT_EMAIL_SENDGRID_DISPOSITION_ATTACHMENT);
            mail.addAttachments(attachments);
        } else {
            log.info("Document not found with uuid : {}", UUID.fromString(item.getDocumentFileId()));
        }
    }
}
