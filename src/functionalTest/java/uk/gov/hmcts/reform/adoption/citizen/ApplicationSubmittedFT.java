package uk.gov.hmcts.reform.adoption.citizen;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import uk.gov.hmcts.reform.adoption.testutil.FunctionalTest;

import java.io.IOException;
import java.util.Map;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.json;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_EXTRA_FIELDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;
import static uk.gov.hmcts.reform.adoption.adoptioncase.event.CitizenCreateApplication.CITIZEN_CREATE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.event.CitizenSubmitApplication.CITIZEN_SUBMIT;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.ABOUT_TO_SUBMIT_URL;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.expectedResponse;

@TestPropertySource("classpath:application.yaml")
@SpringBootTest
public class ApplicationSubmittedFT extends FunctionalTest {

    private static final String REQUEST = "classpath:casedata/ccd-callback-casedata-application-payment-ready.json";
    private static final String REQUEST_NOT_READY = "classpath:casedata/ccd-callback-casedata-application-payment-not-ready.json";
    private static final String RESPONSE = "classpath:casedata/response-application-payment-ready.json";
    private static final String RESPONSE_NOT_READY = "classpath:casedata/response-application-payment-not-ready.json";

    @Test
    public void shouldUpdateCaseInCcdToAwaitingPaymentState() throws IOException {
        Map<String, Object> request = caseData(REQUEST);

        Response responseCreate = triggerCallback(request, CITIZEN_CREATE, ABOUT_TO_SUBMIT_URL);
        assertThat(responseCreate.getStatusCode()).isEqualTo(OK.value());

        Response response = triggerCallback(request, CITIZEN_SUBMIT, ABOUT_TO_SUBMIT_URL);
        assertThat(response.getStatusCode()).isEqualTo(OK.value());

        assertThatJson(response.asString())
            .when(IGNORING_EXTRA_FIELDS)
            .when(IGNORING_ARRAY_ORDER)
            .isEqualTo(json(expectedResponse(RESPONSE)));
    }

    @Test
    public void shouldThrowErrorAndNotUpdateState() throws IOException {
        Map<String, Object> request = caseData(REQUEST_NOT_READY);

        Response responseCreate = triggerCallback(request, CITIZEN_CREATE, ABOUT_TO_SUBMIT_URL);
        assertThat(responseCreate.getStatusCode()).isEqualTo(OK.value());

        Response response = triggerCallback(request, CITIZEN_SUBMIT, ABOUT_TO_SUBMIT_URL);
        assertThat(response.getStatusCode()).isEqualTo(OK.value());

        assertThatJson(response.asString())
            .when(IGNORING_EXTRA_FIELDS)
            .when(IGNORING_ARRAY_ORDER)
            .isEqualTo(json(expectedResponse(RESPONSE_NOT_READY)));
    }


    @Test
    public void shouldNotUpdateCaseInCcdToAwaitingPaymentState() throws IOException {
        Map<String, Object> request = caseData(REQUEST_NOT_READY);

        Response response = triggerCallback(request, CITIZEN_SUBMIT, ABOUT_TO_SUBMIT_URL);
        assertThat(response.getStatusCode()).isEqualTo(OK.value());

        assertThatJson(response.asString())
            .when(IGNORING_EXTRA_FIELDS)
            .when(IGNORING_ARRAY_ORDER)
            .isEqualTo(json(expectedResponse(RESPONSE_NOT_READY)));
    }
}
