package uk.gov.hmcts.reform.adoption.adoptioncase.event;

import com.google.common.collect.ImmutableSet;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.ccd.sdk.ResolvedCCDConfig;
import uk.gov.hmcts.ccd.sdk.api.Event;
import uk.gov.hmcts.ccd.sdk.api.HasRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.idam.client.models.User;
import uk.gov.hmcts.reform.idam.client.models.UserDetails;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.platform.commons.util.ReflectionUtils.findMethod;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.DISTRICT_JUDGE;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_AUTHORIZATION_TOKEN;

public abstract class EventTest {

    public ConfigBuilderImpl<CaseData, State, UserRole> createCaseDataConfigBuilder() {
        return new ConfigBuilderImpl<>(new ResolvedCCDConfig<>(
            CaseData.class,
            State.class,
            UserRole.class,
            new HashMap<>(),
            ImmutableSet.copyOf(State.class.getEnumConstants())
        ));
    }

    @SuppressWarnings({"unchecked"})
    public <T, S, R extends HasRole> Map<String, Event<T, R, S>> getEventsFrom(
        final ConfigBuilderImpl<T, S, R> configBuilder) {

        return (Map<String, Event<T, R, S>>) findMethod(ConfigBuilderImpl.class, "getEvents")
            .map(method -> {
                try {
                    method.setAccessible(true);
                    return method.invoke(configBuilder);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new AssertionError("Unable to invoke ConfigBuilderImpl.class method getEvents", e);
                }
            })
            .orElseThrow(() -> new AssertionError("Unable to find ConfigBuilderImpl.class method getEvents"));
    }

    private User getCaseworkerUser(List<String> roles) {
        UserDetails userDetails = UserDetails
            .builder()
            .forename("testFname")
            .roles(roles)
            .surname("testSname")
            .build();

        return new User(TEST_AUTHORIZATION_TOKEN, userDetails);
    }

    public User getCaseworkerUser() {
        return getCaseworkerUser(List.of());
    }

    public User getJudgeUser() {
        return getCaseworkerUser(List.of(DISTRICT_JUDGE.getRole()));
    }
}
