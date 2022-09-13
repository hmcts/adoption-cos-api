package uk.gov.hmcts.reform.adoption.caseworker;

import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.hmcts.reform.adoption.testutil.FunctionalTest;


@SpringBootTest
public class CaseworkerAddNoteFT extends FunctionalTest {

    private static final String REQUEST = "classpath:casedata/request/request-caseworker-add-notes-about-to-submit.json";

    private static final String RESPONSE = "classpath:casedata/response/response-caseworker-add-notes-about-to-submit.json";

    /*@Test
    public void shouldUpdateCaseDataWithNotesWhenAboutToSubmitCallbackIsInvoked() throws Exception {
        Map<String, Object> request = caseData(REQUEST);

        Response response = triggerCallback(request, CASEWORKER_ADD_CASE_NOTE, ABOUT_TO_SUBMIT_URL);

        assertThat(response.getStatusCode()).isEqualTo(OK.value());

        System.out.println("<<<<<<<<<<STRING>>>>>>>> " + response.asString());
        System.out.println("<<<<<<<<<<JSON>>>>>>>> " + json(response.asString()));
        assertThatJson(response.asString())
            .when(IGNORING_EXTRA_FIELDS)
            .when(IGNORING_ARRAY_ORDER)
            .isEqualTo(json(expectedResponse(RESPONSE)));
    }*/
}
