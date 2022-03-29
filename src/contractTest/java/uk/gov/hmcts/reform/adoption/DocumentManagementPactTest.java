package uk.gov.hmcts.reform.adoption;

//import au.com.dius.pact.consumer.dsl.PactDslJsonBody;

import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.annotations.PactFolder;
import com.google.common.collect.Maps;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.adoption.document.DocumentManagementClient;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;

import java.io.IOException;
import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(PactConsumerTestExt.class)
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@PactTestFor(providerName = "em_dm_store", port = "5006")
@PactFolder("pacts")
@SpringBootTest({
    "document_management.url : http://localhost:5006"
})
public class DocumentManagementPactTest {
    public static final String SOME_SERVICE_AUTHORIZATION_TOKEN = "ServiceToken";
    private static final String USER_ID = "id";
    private static final String USER_ROLES = "admin";
    private static final String DOCUMENT_ID = "6c3c3906-2b51-468e-8cbb-a4002eded076";
    private static final String AUTH_TOKEN = "Bearer someAuthToken";

    @MockBean
    private AuthTokenGenerator authTokenGenerator;
    @Autowired
    private DocumentManagementClient documentApi;

    @Pact(provider = "em_dm_store", consumer = "adoption_cos_api")
    public RequestResponsePact downloadBinaryPact(PactDslWithProvider builder) throws IOException {
        Map<String, String> headers = Maps.newHashMap();
        headers.put("Authorization", AUTH_TOKEN);
        headers.put("ServiceAuthorization", SOME_SERVICE_AUTHORIZATION_TOKEN);
        headers.put("user-roles", USER_ROLES);
        headers.put("user-id", USER_ID);

        return builder
            .given("I have existing document")
            .uponReceiving("a request for download the document")
            .path("/documents/" + DOCUMENT_ID + "/binary")
            .method("GET")
            .headers(headers)
            .willRespondWith()
            .status(200)
            .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "downloadBinaryPact")
    public void verifyPactFragment() throws JSONException {
        when(authTokenGenerator.generate()).thenReturn(SOME_SERVICE_AUTHORIZATION_TOKEN);
        ResponseEntity<?> response = documentApi.downloadBinary(
            AUTH_TOKEN,
            SOME_SERVICE_AUTHORIZATION_TOKEN,
            USER_ROLES,
            USER_ID,
            DOCUMENT_ID
        );
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
    }
}
