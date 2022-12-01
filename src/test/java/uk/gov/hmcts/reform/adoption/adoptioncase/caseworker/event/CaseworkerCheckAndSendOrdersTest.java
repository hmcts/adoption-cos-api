package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.ccd.sdk.ResolvedCCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.Event;
import uk.gov.hmcts.ccd.sdk.api.HasRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.AdoptionOrderData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ManageOrdersData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.commons.util.ReflectionUtils.findMethod;
import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseworkerCheckAndSendOrders.CASEWORKER_CHECK_AND_SEND_ORDERS;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.ManageOrdersData.ManageOrderType.CASE_MANAGEMENT_ORDER;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@ExtendWith(MockitoExtension.class)
class CaseworkerCheckAndSendOrdersTest {


    @InjectMocks
    private CaseworkerCheckAndSendOrders caseworkerCheckAndSendOrders;

    @Test
    void caseworkerCheckAndSendOrdersConfigure() {

        final ConfigBuilderImpl<CaseData, State, UserRole> configBuilder = createCaseDataConfigBuilder();
        caseworkerCheckAndSendOrders.configure(configBuilder);
        assertThat(getEventsFrom(configBuilder).values())
            .extracting(Event::getId)
            .contains(CASEWORKER_CHECK_AND_SEND_ORDERS);
    }


    @NotNull
    private AdoptionOrderData getAdoptionOrderData() {
        AdoptionOrderData adoptionOrderData = new AdoptionOrderData();
        adoptionOrderData.setSubmittedDateAdoptionOrder(LocalDateTime.now());
        adoptionOrderData.setOrderId(UUID.randomUUID().toString());
        return adoptionOrderData;
    }

    private ManageOrdersData getManageOrderData() {
        ManageOrdersData manageOrdersData = new ManageOrdersData().builder().manageOrderType(CASE_MANAGEMENT_ORDER).build();
        manageOrdersData.setSubmittedDateManageOrder(LocalDateTime.now());
        manageOrdersData.setOrderId(UUID.randomUUID().toString());
        return manageOrdersData;
    }

    public static ConfigBuilderImpl<CaseData, State, UserRole> createCaseDataConfigBuilder() {
        return new ConfigBuilderImpl<>(new ResolvedCCDConfig<>(
            CaseData.class,
            State.class,
            UserRole.class,
            new HashMap<>(),
            ImmutableSet.copyOf(State.class.getEnumConstants())
        ));
    }

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

    private CaseDetails<CaseData, State> getCaseDetails() {
        return CaseDetails.<CaseData, State>builder()
            .data(caseData())
            .id(1L)
            .build();
    }

}
