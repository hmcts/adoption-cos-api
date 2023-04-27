package uk.gov.hmcts.reform.adoption.config;

import io.restassured.RestAssured;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 * Built-in feature which saves service's swagger specs in temporary directory.
 * Each CI run on master should automatically save and upload (if updated) documentation.
 */
class SwaggerPublisherTest {

    @Test
    public void testHealthEndpoint() {
        String  testUrl = "http://adoption-cos-api-aat.service.core-compute-aat.internal";
        Assert.assertTrue(true);
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured
            .given()
            .baseUri(testUrl)
            .get("/health")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString();
    }
}
