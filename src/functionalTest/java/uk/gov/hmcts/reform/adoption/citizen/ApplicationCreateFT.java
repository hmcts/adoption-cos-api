package uk.gov.hmcts.reform.adoption.citizen;

import io.restassured.response.Response;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.ABOUT_TO_SUBMIT_URL;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.expectedResponse;

@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@ExtendWith(SerenityJUnit5Extension.class)
public class ApplicationCreateFT  extends FunctionalTest {

    private static final String REQUEST = "classpath:casedata/ccd-callback-casedata-application.json";
    private static final String RESPONSE = "classpath:casedata/response-application.json";

    @Test
    public void shouldCreateCaseInCcdForApplication() throws IOException {
        Map<String, Object> request = caseData(REQUEST);

        Response response = triggerCallback(request, CITIZEN_CREATE, ABOUT_TO_SUBMIT_URL);

        assertThat(response.getStatusCode()).isEqualTo(OK.value());

        assertThatJson(response.asString())
            .when(IGNORING_EXTRA_FIELDS)
            .when(IGNORING_ARRAY_ORDER)
            .isEqualTo(json(expectedResponse(RESPONSE)));
    }
}
