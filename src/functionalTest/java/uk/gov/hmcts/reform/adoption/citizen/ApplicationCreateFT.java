package uk.gov.hmcts.reform.adoption.citizen;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import uk.gov.hmcts.reform.adoption.testutil.FunctionalTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.event.CitizenCreateApplication.CITIZEN_CREATE;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.ABOUT_TO_SUBMIT_URL;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@TestPropertySource("classpath:application.yaml")
@SpringBootTest
public class ApplicationCreateFT  extends FunctionalTest {

    private static final String REQUEST = "classpath:casedata/ccd-callback-casedata-application.json";

    @Test
    public void shouldCreateCaseInCcdForApplication() throws IOException {
        Map<String, Object> request = caseData(REQUEST);

        Response response = triggerCallback(request, CITIZEN_CREATE, ABOUT_TO_SUBMIT_URL);

        assertThat(response.getStatusCode()).isEqualTo(OK.value());
        assertThat(response.jsonPath().getString("data.status")).isEqualTo("Draft");
        assertThat(response.jsonPath().getString("data.typeOfAdoption")).isEqualTo("Post-placement");
        assertThat(response.jsonPath().getString("data.hyphenatedCaseRef")).isEqualTo("1234-5678-9012-3456");
        assertThat(response.jsonPath().getString("data.dssQuestion1")).isEqualTo("First Name");
        assertThat(response.jsonPath().getString("data.dssQuestion2")).isEqualTo("Last Name");
        assertThat(response.jsonPath().getString("data.dssQuestion3")).isEqualTo("Date of Birth");
        assertThat(response.jsonPath().getString("data.dssAnswer1")).isEqualTo("case_data.childrenFirstName");
        assertThat(response.jsonPath().getString("data.dssAnswer2")).isEqualTo("case_data.childrenLastName");
        assertThat(response.jsonPath().getString("data.dssAnswer3")).isEqualTo("case_data.childrenDateOfBirth");
        assertThat(response.jsonPath().getString("data.dssHeaderDetails")).isEqualTo("Child Details");
    }

    @Test
    public void shouldReturnBadRequestForMalformedCaseDetailsIdBoundaries() throws IOException {
        Map<String, Object> request = caseData(REQUEST);
        triggerCallback(request, CITIZEN_CREATE, ABOUT_TO_SUBMIT_URL);

        String fifteenDigitCaseId = "123456789012345"; // 15 digits
        String seventeenDigitCaseId = "12345678901234567"; // 17 digits

        List<Map<String, Object>> malformedCaseDetailsIdRequests = List.of(
            buildMalformedRequestWithMissingCaseDetailsId(),
            buildMalformedRequestWithNullCaseDetailsId(),
            buildMalformedRequestWithCaseDetailsId("non-numeric-id"),
            buildMalformedRequestWithCaseDetailsId(fifteenDigitCaseId),
            buildMalformedRequestWithCaseDetailsId(seventeenDigitCaseId)
        );

        for (Map<String, Object> malformedRequest : malformedCaseDetailsIdRequests) {
            Response response = triggerMalformedCallback(malformedRequest);

            assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST.value());
            assertThat(response.jsonPath().getInt("status")).isEqualTo(400);
            assertThat(response.jsonPath().getString("title")).isNotBlank();
            assertThat(response.jsonPath().getString("detail")).isNotBlank();
            assertThat(response.jsonPath().getString("instance")).contains("/callbacks/about-to-submit");
            assertThat((Object) response.jsonPath().get("data")).isNull();

            assertMalformedProblemDetails(
                response.jsonPath().getInt("status"),
                response.jsonPath().getString("title"),
                response.jsonPath().getString("detail"),
                response.jsonPath().getString("instance")
            );
            assertSuccessDataEnvelopeAbsent(response.jsonPath().get("data"));
        }
    }

    private Response triggerMalformedCallback(Map<String, Object> malformedRequestBody) {
        String targetInstance = System.getenv("TEST_URL");
        if (targetInstance == null || targetInstance.isBlank()) {
            targetInstance = "http://localhost:4550";
        }

        return RestAssured.given()
            .relaxedHTTPSValidation()
            .baseUri(targetInstance)
            .header("ServiceAuthorization", serviceAuthenticationGenerator.generate())
            .header(AUTHORIZATION, idamTokenGenerator.generateIdamTokenForSystem())
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .body(malformedRequestBody)
            .when()
            .post(ABOUT_TO_SUBMIT_URL);
    }

    private void assertMalformedProblemDetails(int status, String title, String detail, String instance) {
        assertThat(status).isEqualTo(BAD_REQUEST.value());
        assertThat(title).isNotBlank();
        assertThat(detail).isNotBlank();
        assertThat(instance).contains("/callbacks/about-to-submit");
    }

    private void assertSuccessDataEnvelopeAbsent(Object dataEnvelope) {
        assertThat(dataEnvelope).isNull();
    }

    private Map<String, Object> buildMalformedRequestWithMissingCaseDetailsId() {
        Map<String, Object> caseDetails = new HashMap<>();
        caseDetails.put("id", "1234567890123456");
        caseDetails.remove("id");

        Map<String, Object> caseDetailsBefore = new HashMap<>();
        caseDetailsBefore.put("id", "1234567890123456");
        caseDetailsBefore.remove("id");

        Map<String, Object> malformedRequestBody = new HashMap<>();
        malformedRequestBody.put("event_id", CITIZEN_CREATE);
        malformedRequestBody.put("case_details", caseDetails);
        malformedRequestBody.put("case_details_before", caseDetailsBefore);
        return malformedRequestBody;
    }

    private Map<String, Object> buildMalformedRequestWithNullCaseDetailsId() {
        Map<String, Object> caseDetails = new HashMap<>();
        caseDetails.put("id", null);

        Map<String, Object> caseDetailsBefore = new HashMap<>();
        caseDetailsBefore.put("id", null);

        Map<String, Object> malformedRequestBody = new HashMap<>();
        malformedRequestBody.put("event_id", CITIZEN_CREATE);
        malformedRequestBody.put("case_details", caseDetails);
        malformedRequestBody.put("case_details_before", caseDetailsBefore);
        return malformedRequestBody;
    }

    private Map<String, Object> buildMalformedRequestWithCaseDetailsId(Object caseId) {
        Map<String, Object> caseDetails = new HashMap<>();
        caseDetails.put("id", caseId);

        Map<String, Object> caseDetailsBefore = new HashMap<>();
        caseDetailsBefore.put("id", caseId);

        Map<String, Object> malformedRequestBody = new HashMap<>();
        malformedRequestBody.put("event_id", CITIZEN_CREATE);
        malformedRequestBody.put("case_details", caseDetails);
        malformedRequestBody.put("case_details_before", caseDetailsBefore);
        return malformedRequestBody;
    }
}
