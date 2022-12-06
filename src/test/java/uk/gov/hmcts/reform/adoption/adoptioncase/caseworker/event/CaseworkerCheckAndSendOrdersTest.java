package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.ccd.sdk.ResolvedCCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.Event;
import uk.gov.hmcts.ccd.sdk.api.HasRole;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.DynamicListElement;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.OrderCheckAndSend;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.SelectedOrder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ManageOrdersData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.AdoptionOrderData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.OrderData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.OrderStatus;

import java.lang.reflect.InvocationTargetException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.Instant;
import java.time.ZoneId;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.commons.util.ReflectionUtils.findMethod;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseworkerCheckAndSendOrders.CASEWORKER_CHECK_AND_SEND_ORDERS;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.ManageOrdersData.ManageOrderType.CASE_MANAGEMENT_ORDER;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@ExtendWith(MockitoExtension.class)
class CaseworkerCheckAndSendOrdersTest {

    @Mock
    private Clock clock;

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


    @Test
    void caseworkerCheckAndSendOrdersAboutToStart() {
        OrderData orderData1 = getCommonOrderData();
        OrderData orderData2 = getCommonOrderData();
        OrderData orderData3 = getCommonOrderData();
        List<ListValue<OrderData>> manageOrderList = new ArrayList<>();
        var caseDetails = getCaseDetails();
        CaseData data = caseDetails.getData();
        manageOrderList = data.archiveManageOrdersHelper(manageOrderList, orderData1);
        data.archiveManageOrdersHelper(manageOrderList, orderData2);
        data.archiveManageOrdersHelper(manageOrderList, orderData3);
        data.setCommonOrderList(manageOrderList);

        var result = caseworkerCheckAndSendOrders.aboutToStart(caseDetails);
        assertThat(result.getData().getCheckAndSendOrderDropdownList().getListItems().size()).isEqualTo(3);
    }

    @Test
    void caseworkerAboutToSubmit_OK() {
        OrderData orderData1 = getCommonOrderData();
        ManageOrdersData manageOrderData1 = getManageOrderData();
        manageOrderData1.setOrderId(UUID.randomUUID().toString());

        orderData1.setManageOrderType(CASE_MANAGEMENT_ORDER);
        orderData1.setOrderId(manageOrderData1.getOrderId());
        List<ListValue<OrderData>> commonOrderList = new ArrayList<>();
        List<ListValue<ManageOrdersData>> manageOrderList = new ArrayList<>();

        var caseDetails = getCaseDetails();
        CaseData data = caseDetails.getData();
        data.setOrderCheckAndSend(OrderCheckAndSend.SERVE_THE_ORDER);
        manageOrderList = data.archiveManageOrdersHelper(manageOrderList, manageOrderData1);
        commonOrderList = data.archiveManageOrdersHelper(commonOrderList, orderData1);
        data.setCommonOrderList(commonOrderList);
        data.setManageOrderList(manageOrderList);
        var item = new SelectedOrder();
        item.setOrderType(CASE_MANAGEMENT_ORDER);
        data.setSelectedOrder(item);
        prepareCheckAndSendDropdownList(commonOrderList, manageOrderData1.getOrderId(),data);
        final var instant = Instant.now();
        final var zoneId = ZoneId.systemDefault();
        final var expectedDate = LocalDate.ofInstant(instant, zoneId);
        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(zoneId);
        var result = caseworkerCheckAndSendOrders.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getManageOrderList().get(0).getValue().getOrderStatus()).isEqualTo(OrderStatus.SERVED);
        assertThat(result.getData().getSelectedOrder()).isNull();
    }


    private OrderData getCommonOrderData() {
        OrderData orderData = new OrderData().builder().manageOrderType(CASE_MANAGEMENT_ORDER).build();
        orderData.setSubmittedDateAndTimeOfOrder(LocalDateTime.now());
        orderData.setOrderId(UUID.randomUUID().toString());
        return orderData;
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

    @NotNull
    private void prepareCheckAndSendDropdownList(List<ListValue<OrderData>>
                                                     commonOrderList, String orderId, CaseData data) {
        List<DynamicListElement> listElements = new ArrayList<>();
        commonOrderList.forEach(order -> {
            DynamicListElement orderInfo = DynamicListElement.builder().label(
                order.getValue().getManageOrderType().getLabel()).code(
                UUID.fromString(order.getValue().getOrderId())).build();
            listElements.add(orderInfo);
        });
        var element = DynamicListElement.builder().code(UUID.fromString(orderId)).build();
        data.setCheckAndSendOrderDropdownList(DynamicList.builder().listItems(listElements)
                                                  .value(element).build());
    }

}
