package uk.gov.hmcts.reform.adoption.config.launchdarkly;

import com.launchdarkly.sdk.server.interfaces.LDClientInterface;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LaunchDarkClientFactoryTest {
    private LaunchDarkClientFactory factory;

    @Test
    public void testCreate() {
        factory = new LaunchDarkClientFactory();
        LDClientInterface client = factory.create("test key", true);
        assertNotNull(client);
    }
}
