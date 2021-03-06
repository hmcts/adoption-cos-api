package uk.gov.hmcts.reform.adoption;

import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslJsonRootValue;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.google.common.collect.ImmutableMap;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.idam.client.models.TokenRequest;
import uk.gov.hmcts.reform.idam.client.models.TokenResponse;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

public class IdamApiConsumerTest extends IdamConsumerTestBase {

    @Pact(provider = "idamApi_oidc", consumer = "adoption_cos_api")
    public RequestResponsePact generatePactForUserInfo(PactDslWithProvider builder) throws JSONException {

        return builder
            .given("userinfo is requested")
            .uponReceiving("A request for a UserInfo from Adoption COS API")
            .path("/o/userinfo")
            .method("GET")
            .matchHeader(HttpHeaders.AUTHORIZATION, SOME_AUTHORIZATION_TOKEN)
            .willRespondWith()
            .status(200)
            .body(createUserDetailsResponse())
            .toPact();
    }

    @Pact(provider = "idamApi_oidc", consumer = "adoption_cos_api")
    public RequestResponsePact generatePactForToken(PactDslWithProvider builder) {

        Map<String, String> responseheaders = ImmutableMap.<String, String>builder()
            .put("Content-Type", "application/json")
            .build();

        return builder
            .given("a token is requested")
            .uponReceiving("Provider receives a POST /o/token request from Adoption COS API")
            .path("/o/token")
            .method(HttpMethod.POST.toString())
            .body("redirect_uri=http%3A%2F%2Fwww.dummy-pact-service.com%2Fcallback"
                      + "&client_id=adoption-web"
                      + "&grant_type=password"
                      + "&username=" + caseworkerUsername
                      + "&password=" + caseworkerPwd
                      + "&client_secret=" + clientSecret
                      + "&scope=openid profile roles",
                  APPLICATION_FORM_URLENCODED_VALUE)
            .willRespondWith()
            .status(HttpStatus.OK.value())
            .headers(responseheaders)
            .body(createAuthResponse())
            .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "generatePactForUserInfo")
    public void verifyIdamUserDetailsRolesPactUserInfo() {
        UserInfo userInfo = idamApi.retrieveUserInfo(SOME_AUTHORIZATION_TOKEN);
        assertNotNull(userInfo.getUid());
        assertNotNull(userInfo.getSub());
        assertNotNull(userInfo.getGivenName());
        assertNotNull(userInfo.getFamilyName());
        assertNotNull(userInfo.getRoles());
        assertTrue(userInfo.getRoles().size() > 0);

    }

    @Test
    @PactTestFor(pactMethod = "generatePactForToken")
    public void verifyIdamUserDetailsRolesPactToken() {

        TokenResponse token = idamApi.generateOpenIdToken(buildTokenRequestMap());
        assertNotNull("Token is expected", token.accessToken);
    }

    private TokenRequest buildTokenRequestMap() {
        return new TokenRequest(
            "adoption-web",
            clientSecret,
            "password",
            "http://www.dummy-pact-service.com/callback",
            caseworkerUsername,
            caseworkerPwd,
            "openid profile roles",
            null, null);
    }


    private PactDslJsonBody createUserDetailsResponse() {

        return new PactDslJsonBody()
            .stringType("sub", "61")
            .stringType("uid", "adop_pact_user@mailinator.com")
            .stringType("givenName", "Test")
            .stringType("familyName", "User")
            .minArrayLike("roles", 1, PactDslJsonRootValue.stringType("citizen"), 1);
    }

    private PactDslJsonBody createAuthResponse() {
        return new PactDslJsonBody()
            .stringType("access_token", "eyJ0eXAiOiJKV1QiLCJ6aXAiOiJOT05FIiwia2lkI")
            .stringType("scope", "openid roles profile");
    }

}
