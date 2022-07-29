package uk.gov.hmcts.reform.adoption.config.launchdarkly;

import com.launchdarkly.sdk.LDUser;
import com.launchdarkly.sdk.server.interfaces.LDClientInterface;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LaunchDarklyClientTest {
    private static final String SDK_KEY = "launch darkly sdk key";
    private static final String FAKE_FEATURE = "launch darkly adoption feature";

    @Mock
    private LaunchDarkClientFactory ldClientFactory;

    @Mock
    private LDClientInterface ldClient;

    @Mock
    private LDUser ldUser;

    private LaunchDarklyClient launchDarklyClient;

    @Test
    public void testFeatureEnabled() {
        when(ldClientFactory.create(eq(SDK_KEY), anyBoolean())).thenReturn(ldClient);
        launchDarklyClient = new LaunchDarklyClient(ldClientFactory, SDK_KEY, true);
        when(ldClient.boolVariation(eq(FAKE_FEATURE), any(LDUser.class), anyBoolean())).thenReturn(true);
        assertTrue(launchDarklyClient.isFeatureEnabled(FAKE_FEATURE, ldUser));
    }

    @Test
    public void testFeatureDisabled() {
        when(ldClientFactory.create(eq(SDK_KEY), anyBoolean())).thenReturn(ldClient);
        launchDarklyClient = new LaunchDarklyClient(ldClientFactory, SDK_KEY, true);
        when(ldClient.boolVariation(eq(FAKE_FEATURE), any(LDUser.class), anyBoolean())).thenReturn(false);
        assertFalse(launchDarklyClient.isFeatureEnabled(FAKE_FEATURE, ldUser));
    }
}
