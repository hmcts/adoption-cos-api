package uk.gov.hmcts.reform.adoption.notification;

import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.document.CaseDocumentClient;
import uk.gov.hmcts.reform.adoption.document.DocumentType;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionDocument;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.idam.client.models.User;
import uk.gov.hmcts.reform.idam.client.models.UserDetails;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@ExtendWith(MockitoExtension.class)
class SendGridServiceTest {

    @Mock
    private CaseDocumentClient caseDocumentClient;

    @Mock
    IdamService idamService;

    @Mock
    private AuthTokenGenerator authTokenGenerator;

    @Mock
    private SendGrid sendGrid;

    @InjectMocks
    @Spy  //ADOP-2324: Added to enable mocking of SendGrid
    SendgridService sendgridService;

    @Test
    void sendEmail() throws IOException {
        String caseId = "1234-1234-1234-1234";
        CaseData caseData = caseData();
        caseData.setHyphenatedCaseRef(caseId);
        caseData.setDocumentsGenerated(getDocumentsGenerated());
        caseData.setLaDocumentsUploaded(getLaDocumentsUploaded());

        ResponseEntity<Resource> resource = new ResponseEntity<>(
            new ByteArrayResource(new byte[]{}), HttpStatus.OK);
        when(idamService.retrieveSystemUpdateUserDetails()).thenReturn(new User(
            StringUtils.EMPTY,
            UserDetails.builder().build()
        ));
        when(authTokenGenerator.generate()).thenReturn(StringUtils.EMPTY);
        when(caseDocumentClient.getDocumentBinary(anyString(), anyString(), any())).thenReturn(resource);

        String caseIdForLogging = "1234123412341234";
        when(sendgridService.getSendGrid(caseIdForLogging)).thenReturn(sendGrid);
        Response response = new Response();
        response.setStatusCode(200);
        when(sendGrid.api(any(Request.class))).thenReturn(response);

        String subject = "TEST_SUBJECT";
        //                Assertions.assertDoesNotThrow(() -> {
        //                    sendgridService.sendEmail(caseData, subject, DocumentType.APPLICATION_LA_SUMMARY_EN);
        //                });
        Assertions.assertThrows(ArithmeticException.class, () -> {
            sendgridService.sendEmail(caseData, subject, DocumentType.APPLICATION_LA_SUMMARY_EN);
        });  //TODO revert following testing in preview
        verify(sendgridService, times(1)).exceptionCauser();

        verify(caseDocumentClient, times(2)).getDocumentBinary(anyString(), anyString(), any());
    }

    @Test
    void testSendEmail_whenNoDocument() throws IOException {
        String caseId = "1234-1234-1234-1234";
        CaseData caseData = caseData();
        caseData.setHyphenatedCaseRef(caseId);
        caseData.setLaDocumentsUploaded(getLaDocumentsUploaded());

        caseData.setDocumentsGenerated(new ArrayList<>());

        ResponseEntity<Resource> resource = new ResponseEntity<>(
            new ByteArrayResource(new byte[]{}), HttpStatus.OK);
        when(idamService.retrieveSystemUpdateUserDetails()).thenReturn(new User(
            StringUtils.EMPTY,
            UserDetails.builder().build()
        ));
        when(authTokenGenerator.generate()).thenReturn(StringUtils.EMPTY);
        when(caseDocumentClient.getDocumentBinary(anyString(), anyString(), any())).thenReturn(resource);

        when(sendgridService.getSendGrid(anyString())).thenReturn(sendGrid);
        Response response = new Response();
        response.setStatusCode(200);
        when(sendGrid.api(any(Request.class))).thenReturn(response);

        String subject = "TEST_SUBJECT";
        //                Assertions.assertDoesNotThrow(() -> {
        //                    sendgridService.sendEmail(caseData, subject, DocumentType.APPLICATION_LA_SUMMARY_EN);
        //                });
        Assertions.assertThrows(ArithmeticException.class, () -> {
            sendgridService.sendEmail(caseData, subject, DocumentType.APPLICATION_LA_SUMMARY_EN);
        });  //TODO revert following testing in preview

        verify(caseDocumentClient, times(1)).getDocumentBinary(anyString(), anyString(), any());
    }

    @Test
    void attachGeneratedDocuments_shouldCatchException() throws IOException {
        String caseId = "1234-1234-1234-1234";
        CaseData caseData = caseData();
        caseData.setHyphenatedCaseRef(caseId);
        caseData.setDocumentsGenerated(getDocumentsGenerated());
        caseData.setLaDocumentsUploaded(getLaDocumentsUploaded());

        when(idamService.retrieveSystemUpdateUserDetails()).thenReturn(new User(
            StringUtils.EMPTY,
            UserDetails.builder().build()
        ));
        when(authTokenGenerator.generate()).thenReturn(StringUtils.EMPTY);
        when(caseDocumentClient.getDocumentBinary(
            anyString(),
            anyString(),
            any()
        )).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        String caseIdForLogging = "1234123412341234";
        when(sendgridService.getSendGrid(caseIdForLogging)).thenReturn(sendGrid);
        Response response = new Response();
        response.setStatusCode(200);
        when(sendGrid.api(any(Request.class))).thenReturn(response);

        String subject = "TEST_SUBJECT";
        //                Assertions.assertDoesNotThrow(() -> {
        //                    sendgridService.sendEmail(caseData, subject, DocumentType.APPLICATION_LA_SUMMARY_EN);
        //                });
        Assertions.assertThrows(ArithmeticException.class, () -> {
            sendgridService.sendEmail(caseData, subject, DocumentType.APPLICATION_LA_SUMMARY_EN);
        });  //TODO revert following testing in preview
    }

    @Test
    void shouldThrowExceptionWhenResourceIsNullTestSendEmail() throws IOException {
        String caseId = "1234-1234-1234-1234";
        CaseData caseData = caseData();
        caseData.setHyphenatedCaseRef(caseId);
        caseData.setDocumentsGenerated(getDocumentsGenerated());
        caseData.setLaDocumentsUploaded(getLaDocumentsUploaded());

        ResponseEntity<Resource> resource = new ResponseEntity<>(
            new ByteArrayResource(new byte[]{}), HttpStatus.OK);
        when(idamService.retrieveSystemUpdateUserDetails()).thenReturn(new User(
            StringUtils.EMPTY,
            UserDetails.builder().build()
        ));
        when(authTokenGenerator.generate()).thenReturn(StringUtils.EMPTY);
        when(caseDocumentClient.getDocumentBinary(anyString(), anyString(), any())).thenReturn(resource);
        ReflectionTestUtils.setField(resource.getBody(), "byteArray", null);

        String caseIdForLogging = "1234123412341234";
        when(sendgridService.getSendGrid(caseIdForLogging)).thenReturn(sendGrid);
        Response response = new Response();
        response.setStatusCode(200);
        when(sendGrid.api(any(Request.class))).thenReturn(response);

        String subject = "TEST_SUBJECT";
        //                Assertions.assertDoesNotThrow(() -> {
        //                    sendgridService.sendEmail(caseData, subject, DocumentType.APPLICATION_LA_SUMMARY_EN);
        //                });
        Assertions.assertThrows(ArithmeticException.class, () -> {
            sendgridService.sendEmail(caseData, subject, DocumentType.APPLICATION_LA_SUMMARY_EN);
        });  //TODO revert following testing in preview
    }

    @Test
    void recover_doesNotThrow_whenCaseIdNull() {
        CaseData caseData = caseData();
        caseData.setHyphenatedCaseRef(null);
        caseData.setDocumentsGenerated(getDocumentsGenerated());
        caseData.setLaDocumentsUploaded(getLaDocumentsUploaded());

        Assertions.assertDoesNotThrow(() -> sendgridService.recover(new IOException(), caseData));
    }

    @Test
    void sendEmail_throws_whenSendGridApithrows() throws IOException {
        String caseId = "1234-1234-1234-1234";
        CaseData caseData = caseData();
        caseData.setHyphenatedCaseRef(caseId);
        caseData.setDocumentsGenerated(getDocumentsGenerated());
        caseData.setLaDocumentsUploaded(getLaDocumentsUploaded());

        ResponseEntity<Resource> resource = new ResponseEntity<>(
            new ByteArrayResource(new byte[]{}), HttpStatus.OK);
        when(idamService.retrieveSystemUpdateUserDetails()).thenReturn(new User(
            StringUtils.EMPTY,
            UserDetails.builder().build()
        ));
        when(authTokenGenerator.generate()).thenReturn(StringUtils.EMPTY);
        when(caseDocumentClient.getDocumentBinary(anyString(), anyString(), any())).thenReturn(resource);

        String caseIdForLogging = "1234123412341234";
        when(sendgridService.getSendGrid(caseIdForLogging)).thenReturn(sendGrid);
        when(sendGrid.api(any(Request.class))).thenThrow(IOException.class);

        String subject = "TEST_SUBJECT";
        Assertions.assertThrows(IOException.class, () -> {
            sendgridService.sendEmail(caseData, subject, DocumentType.APPLICATION_LA_SUMMARY_EN);
        });
        verify(sendgridService, times(0)).exceptionCauser();
    }

    @Test
    void removeThisTest_exceptionCauser() throws IOException { //TODO remove
        String caseId = "1234-1234-1234-1234";
        CaseData caseData = caseData();
        caseData.setHyphenatedCaseRef(caseId);
        caseData.setDocumentsGenerated(getDocumentsGenerated());
        caseData.setLaDocumentsUploaded(getLaDocumentsUploaded());

        ResponseEntity<Resource> resource = new ResponseEntity<>(
            new ByteArrayResource(new byte[]{}), HttpStatus.OK);
        when(idamService.retrieveSystemUpdateUserDetails()).thenReturn(new User(
            StringUtils.EMPTY,
            UserDetails.builder().build()
        ));
        when(authTokenGenerator.generate()).thenReturn(StringUtils.EMPTY);
        when(caseDocumentClient.getDocumentBinary(anyString(), anyString(), any())).thenReturn(resource);

        String caseIdForLogging = "1234123412341234";
        when(sendgridService.getSendGrid(caseIdForLogging)).thenReturn(sendGrid);
        Response response = new Response();
        response.setStatusCode(200);
        when(sendGrid.api(any(Request.class))).thenReturn(response);
        doThrow(ArithmeticException.class).when(sendgridService).exceptionCauser();

        String subject = "TEST_SUBJECT";
        Assertions.assertThrows(ArithmeticException.class, () -> {
            sendgridService.sendEmail(caseData, subject, DocumentType.APPLICATION_LA_SUMMARY_EN);
        });
    }

    private List<ListValue<AdoptionDocument>> getDocumentsGenerated() {
        AdoptionDocument adoptionDocumentDocmosis = new AdoptionDocument();
        adoptionDocumentDocmosis.setDocumentType(DocumentType.APPLICATION_LA_SUMMARY_EN);
        adoptionDocumentDocmosis.setDocumentFileId("5fc03087-d265-11e7-b8c6-83e29cd24f4c");
        ListValue<AdoptionDocument> listValue = new ListValue<>();
        listValue.setValue(adoptionDocumentDocmosis);
        List<ListValue<AdoptionDocument>> listAdoptionDocument = new ArrayList<>();
        listAdoptionDocument.add(listValue);
        return listAdoptionDocument;
    }

    private List<ListValue<AdoptionDocument>> getLaDocumentsUploaded() {
        Document document = new Document();
        document.setFilename("TEST_FILE_NAME");
        document.setUrl("TEST_URL/5fc03087-d265-11e7-b8c6-83e29cd24f4c");

        AdoptionDocument laUploadedDocument = new AdoptionDocument();
        laUploadedDocument.setDocumentFileId(UUID.randomUUID().toString());
        laUploadedDocument.setDocumentLink(document);

        ListValue<AdoptionDocument> documentListValue = new ListValue<>();
        documentListValue.setValue(laUploadedDocument);

        List<ListValue<AdoptionDocument>> laUploadedDocumentList = new ArrayList<>();
        laUploadedDocumentList.add(documentListValue);
        return laUploadedDocumentList;
    }
}
