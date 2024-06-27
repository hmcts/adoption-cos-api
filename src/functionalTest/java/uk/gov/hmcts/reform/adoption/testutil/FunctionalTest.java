package uk.gov.hmcts.reform.adoption.testutil;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.ccd.client.CoreCaseDataApi;
import uk.gov.hmcts.reform.ccd.client.model.CallbackRequest;
import uk.gov.hmcts.reform.ccd.client.model.CaseDataContent;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.ccd.client.model.Event;
import uk.gov.hmcts.reform.ccd.client.model.StartEventResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.Adoption.CASE_TYPE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.Adoption.JURISDICTION;
import static uk.gov.hmcts.reform.adoption.adoptioncase.event.CitizenCreateApplication.CITIZEN_CREATE;

@Slf4j
@TestPropertySource("classpath:application.yaml")
public abstract class FunctionalTest {

    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2021, 04, 28, 1, 0);

    @Autowired
    protected IdamTokenGenerator idamTokenGenerator;

    @Autowired
    protected CoreCaseDataApi coreCaseDataApi;

    @Autowired
    protected ServiceAuthenticationGenerator serviceAuthenticationGenerator;

    @Autowired
    protected ObjectMapper objectMapper;

    private final String targetInstance =
        StringUtils.defaultIfBlank(
            System.getenv("TEST_URL"),
            "http://localhost:4550"
        );

    private final RequestSpecification restAssuredInstance = RestAssured.given().relaxedHTTPSValidation().baseUri(targetInstance);


    protected CaseDetails createCaseInCcd() {
        String userToken = idamTokenGenerator.generateIdamTokenForSystem();
        String s2sTokenForCosApi = serviceAuthenticationGenerator.generate("adoption_cos_api");
        String userId = idamTokenGenerator.getUserDetailsFor(userToken).getId();
        StartEventResponse startEventResponse = startEventForCreateCase(
            userToken,
            s2sTokenForCosApi,
            userId
        );

        CaseDataContent caseDataContent = CaseDataContent.builder()
            .eventToken(startEventResponse.getToken())
            .event(Event.builder()
                       .id(CITIZEN_CREATE)
                       .summary("Create draft case")
                       .description("Create draft case for functional tests")
                       .build())
            .data(Map.of(
                "applicant1SolicitorName", "functional test",
                "applicant1LanguagePreferenceWelsh", "NO"
            ))
            .build();

        return submitNewCase(caseDataContent, userToken, s2sTokenForCosApi, userId);
    }

    private StartEventResponse startEventForCreateCase(
        String userToken,
        String s2sToken,
        String userId
    ) {
        // not including in try catch to fail fast the method
        return coreCaseDataApi.startForCaseworker(
            userToken,
            s2sToken,
            userId,
            JURISDICTION,
            CASE_TYPE,
            CITIZEN_CREATE
        );
    }

    private CaseDetails submitNewCase(
        CaseDataContent caseDataContent,
        String solicitorToken,
        String s2sToken,
        String solicitorUserId
    ) {
        // not including in try catch to fast fail the method
        return coreCaseDataApi.submitForCaseworker(
            solicitorToken,
            s2sToken,
            solicitorUserId,
            JURISDICTION,
            CASE_TYPE,
            true,
            caseDataContent
        );
    }

    protected Response triggerCallback(Map<String, Object> caseData, String eventId, String url) throws IOException {
        CallbackRequest request = CallbackRequest
            .builder()
            .eventId(eventId)
            .caseDetailsBefore(
                CaseDetails
                    .builder()
                    .id(1234567890123456L)
                    .data(caseData)
                    .createdDate(LOCAL_DATE_TIME)
                    .caseTypeId(CASE_TYPE)
                    .build())
            .caseDetails(
                CaseDetails
                    .builder()
                    .id(1234567890123456L)
                    .data(caseData)
                    .createdDate(LOCAL_DATE_TIME)
                    .caseTypeId(CASE_TYPE)
                    .build()
            )
            .build();

        return triggerCallback(request, url);
    }


    protected Response triggerCallback(CallbackRequest request, String url) throws IOException {
        return restAssuredInstance
            .header("ServiceAuthorization", serviceAuthenticationGenerator.generate())
            .header(AUTHORIZATION, idamTokenGenerator.generateIdamTokenForSystem())
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .body(request)
            .when()
            .post(url);
    }


    protected CaseData getCaseData(Map<String, Object> data) {
        return objectMapper.convertValue(data, CaseData.class);
    }
}
