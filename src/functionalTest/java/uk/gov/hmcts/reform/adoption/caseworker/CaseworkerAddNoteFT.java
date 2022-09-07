package uk.gov.hmcts.reform.adoption.caseworker;

import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.hmcts.reform.adoption.testutil.FunctionalTest;


@SpringBootTest
public class CaseworkerAddNoteFT extends FunctionalTest {

    private static final String REQUEST = "classpath:casedata/ccd-callback-casedata-application-payment-ready.json";

    private static final String RESPONSE = "classpath:casedata/response-application-payment-ready.json";

    /*@Test
    public void shouldUpdateCaseDataWithNotesWhenAboutToSubmitCallbackIsInvoked() throws Exception {
        Map<String, Object> request = caseData(REQUEST);

        Response response = triggerCallback(request, CASEWORKER_ADD_CASE_NOTE, ABOUT_TO_SUBMIT_URL);

        *//*assertThat(response.getStatusCode()).isEqualTo(OK.value());

        assertThatJson(response.asString())
            .when(IGNORING_EXTRA_FIELDS)
            .when(IGNORING_ARRAY_ORDER)
            .isEqualTo(json(expectedResponse(RESPONSE)));*//*
    }*/
}
