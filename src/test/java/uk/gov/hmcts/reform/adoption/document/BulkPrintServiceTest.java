package uk.gov.hmcts.reform.adoption.document;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.adoption.document.model.GeneratedDocumentInfo;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.ccd.document.am.feign.CaseDocumentClient;
import uk.gov.hmcts.reform.sendletter.api.LetterWithPdfsRequest;
import uk.gov.hmcts.reform.sendletter.api.SendLetterApi;
import uk.gov.hmcts.reform.sendletter.api.SendLetterResponse;

import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class BulkPrintServiceTest {

    @Mock
    private SendLetterApi sendLetterApi;

    @Mock
    private CaseDocumentClient caseDocumentClient;

    @InjectMocks
    private BulkPrintService bulkPrintService;

    @Mock
    private AuthTokenGenerator authTokenGenerator;

    private UUID uuid;

    private GeneratedDocumentInfo generatedDocumentInfo;
    private String authToken;
    private String s2sToken;

    @Before
    public void setUp() {
        uuid = randomUUID();
        authToken = "auth-token";
        s2sToken = "s2sToken";
        generatedDocumentInfo = GeneratedDocumentInfo.builder()
            .url("TestUrl")
            .createdOn("somedate")
            .binaryUrl("binaryUrl")
            .mimeType("xyz")
            .hashToken("testHashToken")
            .build();
    }

    @Test
    public void senLetterServiceWithValidInput() {
        Resource expectedResource = new ClassPathResource("task-list-markdown.md");
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<Resource> expectedResponse = new ResponseEntity<>(expectedResource, headers, HttpStatus.OK);
        SendLetterResponse sendLetterResponse = new SendLetterResponse(uuid);
        when(sendLetterApi.sendLetter(any(), any(LetterWithPdfsRequest.class))).thenReturn(sendLetterResponse);

        when(authTokenGenerator.generate()).thenReturn(s2sToken);
        when(caseDocumentClient.getDocumentBinary(authToken, s2sToken, "TestUrl"))
            .thenReturn(expectedResponse);
        assertEquals(bulkPrintService.send("123", authToken, "abc",
                                           List.of(generatedDocumentInfo)), uuid);

    }

    @Test(expected = NullPointerException.class)
    public void senLetterServiceWithInValidInput() {
        assertEquals(bulkPrintService.send("123", authToken, "abc", null), uuid);

    }

}
