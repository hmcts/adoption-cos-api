package uk.gov.hmcts.reform.adoption.citizen;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import uk.gov.hmcts.reform.adoption.testutil.FunctionalTest;

@TestPropertySource("classpath:application.yaml")
@SpringBootTest
public class ApplicationPaymentFT extends FunctionalTest {

    private static final String REQUEST = "classpath:casedata/ccd-callback-casedata-application-payment-added.json";
    private static final String REQUEST_FAILED_PAYMENT = "classpath:casedata/ccd-callback-casedata-application-payment-failed.json";
    private static final String REQUEST_FAILED_VALIDATION = "classpath:casedata/ccd-callback-casedata-application-validation-failed.json";
    private static final String RESPONSE = "classpath:casedata/response-application-payment-added.json";
    private static final String RESPONSE_FAILED_VALIDATION = "classpath:casedata/response-application-validation-failed.json";
    private static final String RESPONSE_FAILED_PAYMENT = "classpath:casedata/response-application-payment-failed.json";

    //    @Test
    //    public void shouldUpdateCaseStateInCcdToSubmittedAndGenerateDocumentAndNotifications() throws IOException {
    //        Map<String, Object> request = caseData(REQUEST);
    //
    //        Response response = triggerCallback(request, CITIZEN_ADD_PAYMENT, ABOUT_TO_SUBMIT_URL);
    //
    //        assertThat(response.getStatusCode()).isEqualTo(OK.value());
    //
    //        assertThatJson(response.asString())
    //            .when(IGNORING_EXTRA_FIELDS)
    //            .when(IGNORING_ARRAY_ORDER)
    //            .when(IGNORING_EXTRA_ARRAY_ITEMS)
    //            .isEqualTo(json(expectedResponse(RESPONSE)));
    //    }

    //    @Test
    //    public void shouldUpdateCaseStateInCcdToDraftInCaseOfFailedPaymentAndNoDocumentsGeneratedAndNoNotificationsSent()
    //        throws IOException {
    //        Map<String, Object> request = caseData(REQUEST_FAILED_PAYMENT);
    //
    //        Response response = triggerCallback(request, CITIZEN_ADD_PAYMENT, ABOUT_TO_SUBMIT_URL);
    //
    //        assertThat(response.getStatusCode()).isEqualTo(OK.value());
    //
    //        assertThatJson(response.asString())
    //            .when(IGNORING_EXTRA_FIELDS)
    //            .when(IGNORING_ARRAY_ORDER)
    //            .isEqualTo(json(expectedResponse(RESPONSE_FAILED_PAYMENT)));
    //    }
    //
    //
    //    @Test
    //    public void shouldThrowErrorIfValidationFailsAndNoDocumentsGeneratedAndNoNotificationsSent()
    //        throws IOException {
    //        Map<String, Object> request = caseData(REQUEST_FAILED_VALIDATION);
    //
    //        Response response = triggerCallback(request, CITIZEN_ADD_PAYMENT, ABOUT_TO_SUBMIT_URL);
    //
    //        assertThat(response.getStatusCode()).isEqualTo(OK.value());
    //
    //        assertThatJson(response.asString())
    //            .when(IGNORING_EXTRA_FIELDS)
    //            .when(IGNORING_ARRAY_ORDER)
    //            .isEqualTo(json(expectedResponse(RESPONSE_FAILED_VALIDATION)));
    //    }
}
