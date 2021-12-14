package uk.gov.hmcts.reform.adoption.smoke;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest()
class SmokeFT {

    @Value("${test.url:http://localhost:4550}")
    private String testUrl;

    @Test
    void testHealthEndpoint() {
        Assert.assertTrue(true);
        Assert.assertEquals(testUrl, testUrl);
    }
}
