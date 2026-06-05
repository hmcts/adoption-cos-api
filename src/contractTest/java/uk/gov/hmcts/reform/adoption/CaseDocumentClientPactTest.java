package uk.gov.hmcts.reform.adoption;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactBuilder;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.adoption.document.CaseDocumentClient;

import java.util.Map;
import java.util.UUID;

@ExtendWith(PactConsumerTestExt.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@PactTestFor(providerName = "case-document-am-api", port = "4452")
@SpringBootTest(properties = {
    "case_document_am.url=http://localhost:4452"
})
public class CaseDocumentClientPactTest {

    private static final String SOME_SERVICE_AUTHORIZATION_TOKEN = "ServiceToken";
    private static final String DOCUMENT_ID = "6c3c3906-2b51-468e-8cbb-a4002eded076";
    private static final String AUTH_TOKEN = "Bearer someAuthToken";

    @Autowired
    private CaseDocumentClient caseDocumentClient;

    @Pact(provider = "case-document-am-api", consumer = ContractTestConstants.CONSUMER_NAME)
    public V4Pact downloadBinaryPact(PactBuilder builder) {
        return builder
            .usingLegacyDsl()
            .given("I have existing document")
            .uponReceiving("a request to download the document")
            .path("/cases/documents/" + UUID.fromString(DOCUMENT_ID) + "/binary")
            .method("GET")
            .headers(Map.of(
                "Authorization", AUTH_TOKEN,
                "ServiceAuthorization", SOME_SERVICE_AUTHORIZATION_TOKEN
            ))
            .willRespondWith()
            .status(200)
            .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "downloadBinaryPact")
    void verifyDownloadBinary(MockServer mockServer) throws JSONException {
        ResponseEntity<?> response = caseDocumentClient.getDocumentBinary(
            AUTH_TOKEN,
            SOME_SERVICE_AUTHORIZATION_TOKEN,
            UUID.fromString(DOCUMENT_ID)
        );

        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
    }
}
