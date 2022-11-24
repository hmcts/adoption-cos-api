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
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.DynamicListElement;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ManageOrdersData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.AdoptionOrderData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.OrderStatus;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @Test
    void caseworkerCheckAndSendOrdersAboutToStart() {
        ManageOrdersData manageOrdersData1 = getManageOrderData();
        ManageOrdersData manageOrdersData2 = getManageOrderData();
        ManageOrdersData manageOrdersData3 = getManageOrderData();
        List<ListValue<ManageOrdersData>> manageOrderList = new ArrayList<>();
        var caseDetails = getCaseDetails();
        CaseData data = caseDetails.getData();
        manageOrderList = data.archiveManageOrdersHelper(manageOrderList, manageOrdersData1);
        data.archiveManageOrdersHelper(manageOrderList, manageOrdersData2);
        data.archiveManageOrdersHelper(manageOrderList, manageOrdersData3);
        data.setManageOrderList(manageOrderList);
        List<ListValue<AdoptionOrderData>> adoptionOrderList = new ArrayList<>();
        AdoptionOrderData adoptionOrderData1 = getAdoptionOrderData();
        adoptionOrderList = data.archiveManageOrdersHelper(adoptionOrderList, adoptionOrderData1);
        AdoptionOrderData adoptionOrderData2 = getAdoptionOrderData();
        adoptionOrderData2.setSubmittedDateAdoptionOrder(LocalDateTime.now());
        data.archiveManageOrdersHelper(adoptionOrderList, adoptionOrderData2);
        AdoptionOrderData adoptionOrderData3 = getAdoptionOrderData();
        adoptionOrderData3.setSubmittedDateAdoptionOrder(LocalDateTime.now());
        data.archiveManageOrdersHelper(adoptionOrderList, adoptionOrderData3);
        data.setAdoptionOrderList(adoptionOrderList);
        var result = caseworkerCheckAndSendOrders.aboutToStart(caseDetails);
        assertThat(result.getData().getCheckAndSendOrderDropdownList().getListItems().size()).isEqualTo(6);
    }

    @Test
    void caseworkerAboutToSubmit_OK() {
        ManageOrdersData manageOrdersData1 = getManageOrderData();
        manageOrdersData1.setOrderId(UUID.randomUUID().toString());
        manageOrdersData1.setManageOrderType(CASE_MANAGEMENT_ORDER);
        List<ListValue<ManageOrdersData>> manageOrderList = new ArrayList<>();

        var caseDetails = getCaseDetails();
        CaseData data = caseDetails.getData();

        manageOrderList = data.archiveManageOrdersHelper(manageOrderList, manageOrdersData1);
        data.setManageOrderList(manageOrderList);
        data.setManageOrderSelecType(CASE_MANAGEMENT_ORDER);
        prepareCheckAndSendDropdownList(manageOrderList, manageOrdersData1.getOrderId(),data);

        var result = caseworkerCheckAndSendOrders.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getManageOrderList().get(0).getValue().getOrderStatus()).isEqualTo(OrderStatus.SERVED);
        assertThat(result.getData().getManageOrderSelecType()).isNull();
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
    private void prepareCheckAndSendDropdownList(List<ListValue<ManageOrdersData>>
                                                     manageOrderList, String orderId, CaseData data) {
        List<DynamicListElement> listElements = new ArrayList<>();
        manageOrderList.forEach(order -> {
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
