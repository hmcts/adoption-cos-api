package uk.gov.hmcts.reform.adoption.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

/**
 * Default endpoints per application.
 */
@RestController
@Slf4j
public class RootController {

    @Value("azure.application-insights.instrumentation-key1")
    String appInsights;

    @Value("azure.application-insights.instrumentation-key")
    String appInsightsk;

    /**
     * Root GET endpoint.
     *
     * <p>Azure application service has a hidden feature of making requests to root endpoint when
     * "Always On" is turned on.
     * This is the endpoint to deal with that and therefore silence the unnecessary 404s as a response code.
     *
     * @return Welcome message from the service.
     */
    @GetMapping("/")
    public ResponseEntity<String> welcome() {
        return ok("Welcome to adoption-cos-api RootController: " + System.getenv("APP_INSIGHTS_KEY")
                      + "key1:" + appInsights + " key: " + appInsightsk);
    }
}
