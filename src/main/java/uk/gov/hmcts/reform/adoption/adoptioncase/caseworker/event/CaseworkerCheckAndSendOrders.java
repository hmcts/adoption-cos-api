package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.DynamicListElement;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.common.CaseEventCommonMethods;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ManageOrdersData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.OrderCheckAndSend;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.*;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MessageSendDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.DirectionsOrderData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.OrderData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.CheckAndSendOrders;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.document.CaseDataDocumentService;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.idam.client.models.User;

import javax.servlet.http.HttpServletRequest;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.COMMA;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.SEND_N_REPLY_USER_JUDGE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.SEND_N_REPLY_USER_DEFAULT;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.CHECK_N_SEND_ORDER_DATE_FORMAT;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.FINAL_ADOPTION_ORDER_A76;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.FINAL_ADOPTION_ORDER_A76_FILE_NAME;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.FINAL_ADOPTION_ORDER_A76_DRAFT_FILE_NAME;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.FINAL_ADOPTION_ORDER_A206;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.FINAL_ADOPTION_ORDER_A206_FILE_NAME;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.FINAL_ADOPTION_ORDER_A206_DRAFT_FILE_NAME;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.PDF_EXT;


@Component
@Slf4j
public class CaseworkerCheckAndSendOrders implements CCDConfig<CaseData, State, UserRole> {

    @Autowired
    private CaseDataDocumentService caseDataDocumentService;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IdamService idamService;

    @Autowired
    private HttpServletRequest request;

    /**
     * The constant CASEWORKER_CHECK_AND_SEND_ORDERS.
     */
    public static final String CASEWORKER_CHECK_AND_SEND_ORDERS = "caseworker-check-and-send-orders";

    private final CcdPageConfiguration checkAndSendOrders = new CheckAndSendOrders();

    private static final String check_and_send_orders = "Check and send orders";

    @Autowired
    private Clock clock;

    @Override
    public void configure(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        log.info("Inside configure method for Event {}", CASEWORKER_CHECK_AND_SEND_ORDERS);
        var pageBuilder = addEventConfig(configBuilder);
        checkAndSendOrders.addTo(pageBuilder);
    }

    /**
     * Helper method to make custom changes to the CCD Config in order to add the event to respective Page Configuration.
     *
     * @param configBuilder - Base CCD Config Builder updated to add Event for Page
     * @return - PageBuilder updated to use on overridden method.
     */
    private PageBuilder addEventConfig(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.grant(State.Draft, Permissions.READ_UPDATE, UserRole.CASE_WORKER, UserRole.COURT_ADMIN,
                            UserRole.LEGAL_ADVISOR, UserRole.DISTRICT_JUDGE);
        return new PageBuilder(configBuilder
                                   .event(CASEWORKER_CHECK_AND_SEND_ORDERS)
                                   .forAllStates()
                                   .name(check_and_send_orders)
                                   .showSummary()
                                   .aboutToStartCallback(this::aboutToStart)
                                   .grant(Permissions.CREATE_READ_UPDATE, UserRole.CASE_WORKER)
                                   .grant(Permissions.CREATE_READ_UPDATE, UserRole.DISTRICT_JUDGE)
                                   .aboutToSubmitCallback(this::aboutToSubmit));
    }

    /**
     * Method to fetch Check and send order list.
     *
     * @param details is type of Case Data
     * @return will return about to start response
     */
    public AboutToStartOrSubmitResponse<CaseData, State> aboutToStart(CaseDetails<CaseData, State> details) {
        CaseData caseData = details.getData();
        final User caseworkerUser = idamService.retrieveUser(request.getHeader(AUTHORIZATION));
        List<DynamicListElement> listElements = new ArrayList<>();
        if (caseData.getCommonOrderList() != null) {
            caseData.getCommonOrderList().forEach(order -> {
                if (order.getValue().getStatus() != OrderStatus.SERVED) {
                    DynamicListElement orderInfo = DynamicListElement.builder()
                        .label(order.getValue().getSubmittedDateAndTimeOfOrder().format(
                                DateTimeFormatter.ofPattern(
                                    CHECK_N_SEND_ORDER_DATE_FORMAT)).concat(COMMA)
                                   .concat(order.getValue().getManageOrderType().getLabel())).code(
                            UUID.fromString(order.getValue().getOrderId())).build();
                    listElements.add(orderInfo);
                }
            });
        }
        caseData.setCheckAndSendOrderDropdownList(DynamicList.builder().listItems(listElements).value(DynamicListElement.EMPTY).build());

        caseData.setLoggedInUserRole(caseworkerUser.getUserDetails().getRoles()
                                         .contains(UserRole.DISTRICT_JUDGE.getRole())
                                         ? SEND_N_REPLY_USER_JUDGE : SEND_N_REPLY_USER_DEFAULT);
        return AboutToStartOrSubmitResponse.<CaseData, State>builder().data(caseData).build();
    }

    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(CaseDetails<CaseData, State>
                                            caseDetails, CaseDetails<CaseData, State> caseDetailsBefore) {
        var caseData = caseDetails.getData();
        final User caseworkerUser = idamService.retrieveUser(request.getHeader(AUTHORIZATION));
        switch (caseData.getSelectedOrder().getOrderType()) {
            case CASE_MANAGEMENT_ORDER:
                Optional<ListValue<ManageOrdersData>> gatekeepingOrderItem =  caseData.getManageOrderList().stream()
                    .filter(item -> item.getValue().getOrderId()
                        .equalsIgnoreCase(caseData.getCheckAndSendOrderDropdownList().getValueCode().toString()))
                    .findFirst();
                caseData.setManageOrdersData(gatekeepingOrderItem.get().getValue());
                gatekeepingOrderItem.get().getValue().setOrderStatus(caseData.getOrderCheckAndSend().equals(
                    OrderCheckAndSend.SERVE_THE_ORDER) ? OrderStatus.SERVED : OrderStatus.RETURN_FOR_AMENDMENTS);
                break;
            case GENERAL_DIRECTIONS_ORDER:
                Optional<ListValue<DirectionsOrderData>> directionOrderItem =  caseData.getDirectionsOrderList().stream()
                    .filter(item -> item.getValue().getOrderId()
                        .equalsIgnoreCase(caseData.getCheckAndSendOrderDropdownList().getValueCode().toString()))
                    .findFirst();
                directionOrderItem.get().getValue().setGeneralDirectionOrderStatus(caseData.getOrderCheckAndSend().equals(
                    OrderCheckAndSend.SERVE_THE_ORDER) ? OrderStatus.SERVED : OrderStatus.RETURN_FOR_AMENDMENTS);
                break;
            case FINAL_ADOPTION_ORDER:
                Optional<ListValue<AdoptionOrderData>> finalAdoptionItem =  caseData.getAdoptionOrderList().stream()
                    .filter(item -> item.getValue().getOrderId()
                        .equalsIgnoreCase(caseData.getCheckAndSendOrderDropdownList().getValueCode().toString()))
                    .findFirst();
                AdoptionOrderData orderListItem = finalAdoptionItem.get().getValue();
                caseData.setAdoptionOrderData(orderListItem);
                orderListItem.setOrderStatus(caseData.getOrderCheckAndSend().equals(
                    OrderCheckAndSend.SERVE_THE_ORDER) ? OrderStatus.SERVED : OrderStatus.RETURN_FOR_AMENDMENTS);
                orderListItem.setDraftDocumentA76(null);
                orderListItem.setDraftDocumentA206(null);
                break;
            default:
                break;
        }
        Optional<ListValue<OrderData>> commonOrderListItem =  caseData.getCommonOrderList().stream()
            .filter(item -> item.getValue().getOrderId()
                .equalsIgnoreCase(caseData.getCheckAndSendOrderDropdownList().getValueCode().toString()))
            .findFirst();
        OrderData orderListItem = commonOrderListItem.get().getValue();
        orderListItem.setStatus(caseData.getOrderCheckAndSend().equals(
            OrderCheckAndSend.SERVE_THE_ORDER) ? OrderStatus.SERVED : OrderStatus.RETURN_FOR_AMENDMENTS);
        orderListItem.setDateServed(LocalDate.now(clock));
        if (orderListItem.getStatus().equals(OrderStatus.SERVED)) {
            @SuppressWarnings("unchecked")
            Map<String, Object> templateContent =
                objectMapper.convertValue(caseData, Map.class);
            if (isNotEmpty(orderListItem.getDocumentReview1()) && orderListItem.getDocumentReview1().getFilename()
                .equals(FINAL_ADOPTION_ORDER_A76_DRAFT_FILE_NAME + PDF_EXT)) {
                orderListItem.setDocumentReview1(
                        caseDataDocumentService.renderDocument(
                            templateContent,
                            caseDetails.getId(),
                            FINAL_ADOPTION_ORDER_A76,
                            LanguagePreference.ENGLISH,
                            FINAL_ADOPTION_ORDER_A76_FILE_NAME));
            }
            if (isNotEmpty(orderListItem.getDocumentReview2()) && orderListItem.getDocumentReview2().getFilename()
                .equals(FINAL_ADOPTION_ORDER_A206_DRAFT_FILE_NAME + PDF_EXT)) {
                orderListItem.setDocumentReview2(
                        caseDataDocumentService.renderDocument(
                            templateContent,
                            caseDetails.getId(),
                            FINAL_ADOPTION_ORDER_A206,
                            LanguagePreference.ENGLISH,
                            FINAL_ADOPTION_ORDER_A206_FILE_NAME));
            }
        } else if (commonOrderListItem.get().getValue().getStatus().equals(OrderStatus.RETURN_FOR_AMENDMENTS)) {
            caseData.setMessageAction(MessageSendDetails.MessagesAction.SEND_A_MESSAGE);
            CaseEventCommonMethods.updateMessageList(caseData, caseworkerUser);
        }
        caseData.setManageOrdersData(null);
        caseData.setDirectionsOrderData(null);
        caseData.setAdoptionOrderData(null);
        caseData.setSelectedOrder(null);
        caseData.setLoggedInUserRole(null);
        return AboutToStartOrSubmitResponse.<CaseData, State>builder().data(caseData).build();
    }
}
