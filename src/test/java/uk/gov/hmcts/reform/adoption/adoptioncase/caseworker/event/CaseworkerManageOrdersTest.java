package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.ccd.sdk.ResolvedCCDConfig;
import uk.gov.hmcts.ccd.sdk.api.Event;
import uk.gov.hmcts.ccd.sdk.api.HasRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.idam.IdamService;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.time.Clock;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.commons.util.ReflectionUtils.findMethod;
import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseworkerManageOrders.CASEWORKER_MANAGE_ORDERS;

/**
 * The type Caseworker manage orders test.
 */
@ExtendWith(MockitoExtension.class)
class CaseworkerManageOrdersTest {

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private Clock clock;

    @Mock
    private IdamService idamService;

    @InjectMocks
    private CaseworkerManageOrders caseworkerManageOrders;

    /**
     * Caseworker manage orders configure test.
     */
    @Test
    void caseworkerManageOrdersConfigureTest() {
        final ConfigBuilderImpl<CaseData, State, UserRole> configBuilder = createCaseDataConfigBuilder();
        caseworkerManageOrders.configure(configBuilder);
        assertThat(getEventsFrom(configBuilder).values())
            .extracting(Event::getId)
            .contains(CASEWORKER_MANAGE_ORDERS);

    }

    /**
     * Gets events from.
     *
     * @param <T>           the type parameter
     * @param <S>           the type parameter
     * @param <R>           the type parameter
     * @param configBuilder the config builder
     * @return the events from
     */
    @SuppressWarnings({"unchecked"})
    public static <T, S, R extends HasRole> Map<String, Event<T, R, S>> getEventsFrom(
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


    /**
     * Create case data config builder config builder.
     *
     * @return the config builder
     */
    public static ConfigBuilderImpl<CaseData, State, UserRole> createCaseDataConfigBuilder() {
        return new ConfigBuilderImpl<>(new ResolvedCCDConfig<>(
            CaseData.class,
            State.class,
            UserRole.class,
            new HashMap<>(),
            ImmutableSet.copyOf(State.class.getEnumConstants())));
    }
}
