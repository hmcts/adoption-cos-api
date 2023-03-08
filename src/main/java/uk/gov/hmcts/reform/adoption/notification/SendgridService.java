
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.document.CaseDocumentClient;
import uk.gov.hmcts.reform.adoption.document.DocumentType;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionDocument;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SendgridService {

    public static final String PRL_RPA_NOTIFICATION = "Private Reform Law CCD Notification ";
    @Value("${send-grid.api-key}")
    private String apiKey;

    @Autowired
    private CaseDocumentClient caseDocumentClient;

    @Autowired
    IdamService idamService;

    @Autowired
    private AuthTokenGenerator authTokenGenerator;

    public void sendEmail(CaseData caseData) throws IOException {

        log.info("<<<<<<<<<<<>>>>>>>>>>   Inside sendEmail method of SendGrid class for case : {}", caseData.getHyphenatedCaseRef());
        //log.info("<<<<<<<>>>>>>  SendAPI ket: {}", apiKey);
        String subject = "Sample Test Subject" + ".pdf";
        Content content = new Content("text/plain", " Some Sample text Body");
        Attachments attachments = new Attachments();
        Mail mail = new Mail(new Email("ca@mail-prl-nonprod.aat.platform.hmcts.net"), subject, new Email("mohit.vijay@hmcts.net"), content);
        AdoptionDocument adoptionDocument = caseData.getDocumentsGenerated().stream().map(item -> item.getValue())
            .filter(item -> item.getDocumentType().equals(DocumentType.APPLICATION_SUMMARY_EN))
            .findFirst().orElse(null);
        final String authorisation = idamService.retrieveSystemUpdateUserDetails().getAuthToken();
        String serviceAuthorization = authTokenGenerator.generate();
        if (adoptionDocument != null) {
            log.info("<<<<<<<<<<<>>>>>>>>>>   adoptionDocument is not null for case : {}", caseData.getHyphenatedCaseRef());
            //String data = Base64.getEncoder().encodeToString(adoptionDocument.toString().getBytes());
            //log.info("<<<<<<<<<<<>>>>>>>>>>   adoptionDocument byte : {}", data);

            Resource document = caseDocumentClient.getDocumentBinary(authorisation,
                                                                     serviceAuthorization,
                                                                     UUID.fromString(adoptionDocument.getDocumentFileId())).getBody();
            String data = null;
            try (InputStream inputStream = document.getInputStream()) {
                if (inputStream != null) {
                    byte[] documentContents = inputStream.readAllBytes();
                    data = Base64.getEncoder().encodeToString(documentContents);
                }
            } catch (Exception e) {
                log.error("Document could not be read");
            }
            attachments.setContent(data);
            attachments.setFilename(adoptionDocument.getDocumentFileName());
            attachments.setType("application/pdf");
            attachments.setDisposition("attachment");
            mail.addAttachments(attachments);
        }

        if (caseData.getLaDocumentsUploaded() != null) {
            List<AdoptionDocument> uploadedDocumentsUrls = caseData.getLaDocumentsUploaded().stream().map(item -> item.getValue())
                //.map(item -> item.)
                .collect(Collectors.toList());

            log.info("<<<<<<<>>>>>>  Uploaded Documents size:  {}", uploadedDocumentsUrls.size());
            for (AdoptionDocument item : uploadedDocumentsUrls) {
                Resource uploadedDocument = caseDocumentClient.getDocumentBinary(authorisation,
                                                                                 serviceAuthorization,
                                                                                 UUID.fromString(item.getDocumentFileId())).getBody();
                String data = null;
                if (uploadedDocument != null) {
                    log.info("Document found with uuid : {}", UUID.fromString(item.getDocumentFileId()));
                    log.info("Document found with file name : {}", item.getDocumentFileName());
                    byte[] uploadedDocumentContents = uploadedDocument.getInputStream().readAllBytes();
                    data = Base64.getEncoder().encodeToString(uploadedDocumentContents);
                    attachments.setContent(data);
                    attachments.setFilename(item.getDocumentFileName());
                    String documentType = item.getDocumentFileName().split(".")[1];
                    log.info("Document type : {}", documentType);
                    switch (documentType) {
                        case "pdf":
                            attachments.setType("application/pdf");
                            break;

                        case "jpg", "jpeg":
                            attachments.setType("image/jpeg");
                            break;

                        case "png":
                            attachments.setType("image/png");
                            break;

                        case "doc":
                            attachments.setType("application/msword");
                            break;

                        case "docx":
                            attachments.setType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                            break;

                        case "tif", "tiff":
                            attachments.setType("image/tiff");
                            break;

                        default:
                            log.info("default called for switch with file type as: " + documentType);
                    }
                    //attachments.setType("application/pdf");
                    attachments.setDisposition("attachment");
                    mail.addAttachments(attachments);
                } else {
                    log.info("Document not found with uuid : {}", UUID.fromString(item.getDocumentFileId()));
                }
            }
        }



        //mail.addAttachments(attachments);
        //mail.addAttachments(attachments);
        log.info("<<<<<<<<<<<>>>>>>>>>>   before sending email for case : {}", caseData.getHyphenatedCaseRef());
        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
            log.info("Notification to RPA sent successfully");
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        }
    }
}
