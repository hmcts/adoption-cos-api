package uk.gov.hmcts.reform.adoption.caseworker;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.hmcts.reform.adoption.testutil.FunctionalTest;

import java.util.Map;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.json;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_EXTRA_FIELDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;
import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseworkerCaseNote.CASEWORKER_ADD_CASE_NOTE;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.ABOUT_TO_SUBMIT_URL;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.expectedResponse;

@SpringBootTest
public class CaseworkerAddNoteFT extends FunctionalTest {

    private static final String REQUEST = "classpath:casedata/ccd-callback-casedata-application-payment-ready.json";

    private static final String RESPONSE = "classpath:casedata/response-application-payment-ready.json";

    @Test
    public void shouldUpdateCaseDataWithNotesWhenAboutToSubmitCallbackIsInvoked() throws Exception {
        Map<String, Object> request = caseData(REQUEST);

        Response response = triggerCallback(request, CASEWORKER_ADD_CASE_NOTE, ABOUT_TO_SUBMIT_URL);

        assertThat(response.getStatusCode()).isEqualTo(OK.value());

        assertThatJson(response.asString())
            .when(IGNORING_EXTRA_FIELDS)
            .when(IGNORING_ARRAY_ORDER)
            .isEqualTo(json(expectedResponse(RESPONSE)));
    }
}
