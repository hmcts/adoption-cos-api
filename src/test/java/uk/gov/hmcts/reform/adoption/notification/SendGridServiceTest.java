package uk.gov.hmcts.reform.adoption.notification;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@ExtendWith(MockitoExtension.class)
public class SendGridServiceTest {

    @Mock
    private CaseDocumentClient caseDocumentClient;

    @Mock
    IdamService idamService;

    @Mock
    private AuthTokenGenerator authTokenGenerator;

    @Mock
    private SendGrid sendGrid;

    @InjectMocks
    SendgridService sendgridService;



    @Test
    public void sendEmail() throws IOException {
        String subject = "TEST_SUBJECT";
        AdoptionDocument adoptionDocumentDocmosis = new AdoptionDocument();
        adoptionDocumentDocmosis.setDocumentType(DocumentType.APPLICATION_LA_SUMMARY_EN);
        adoptionDocumentDocmosis.setDocumentFileId("5fc03087-d265-11e7-b8c6-83e29cd24f4c");
        ListValue<AdoptionDocument> listValue = new ListValue<AdoptionDocument>();
        listValue.setValue(adoptionDocumentDocmosis);
        List<ListValue<AdoptionDocument>> listAdoptionDocument = new ArrayList<>();
        listAdoptionDocument.add(listValue);
        CaseData caseData = caseData();
        caseData.setDocumentsGenerated(listAdoptionDocument);
        AdoptionDocument laUploadedDocument = new AdoptionDocument();
        Document document = new Document();
        document.setFilename("TEST_FILE_NAME");
        document.setUrl("TEST_URL/5fc03087-d265-11e7-b8c6-83e29cd24f4c");
        laUploadedDocument.setDocumentLink(document);
        ListValue<AdoptionDocument> documentListValue = new ListValue<>();
        documentListValue.setValue(laUploadedDocument);
        List<ListValue<AdoptionDocument>> laUploadedDocumentList = new ArrayList<>();
        laUploadedDocumentList.add(documentListValue);
        caseData.setLaDocumentsUploaded(laUploadedDocumentList);

        ResponseEntity<Resource> resource = new ResponseEntity<Resource>(
            new ByteArrayResource(new byte[]{}), HttpStatus.OK);
        when(idamService.retrieveSystemUpdateUserDetails()).thenReturn(new User(StringUtils.EMPTY, UserDetails.builder().build()));
        when(authTokenGenerator.generate()).thenReturn(StringUtils.EMPTY);
        when(caseDocumentClient.getDocumentBinary(anyString(), anyString(),any())).thenReturn(resource);


        Response response = new Response();
        response.setStatusCode(200);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        Assertions.assertThrows(IOException.class, () -> {
            sendgridService.sendEmail(caseData, "TEST_SUBJECT", DocumentType.APPLICATION_LA_SUMMARY_EN);
        });
        //verify(sendGrid,times(1)).api(request);
    }
}
