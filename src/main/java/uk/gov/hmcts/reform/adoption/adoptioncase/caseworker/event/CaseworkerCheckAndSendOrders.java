package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.DynamicListElement;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.CheckAndSendOrders;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.OrderStatus;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.CHECK_N_SEND_ORDER_DATE_FORMAT;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.COMMA;


@Component
@Slf4j
public class CaseworkerCheckAndSendOrders implements CCDConfig<CaseData, State, UserRole> {


    /**
     * The constant CASEWORKER_CHECK_AND_SEND_ORDERS.
     */
    public static final String CASEWORKER_CHECK_AND_SEND_ORDERS = "caseworker-check-and-send-orders";

    private final CcdPageConfiguration checkAndSendOrders = new CheckAndSendOrders();

    private static final String check_and_send_orders = "Check and send orders";

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
        configBuilder.grant(State.Draft,
                            Permissions.READ_UPDATE,
                            UserRole.CASE_WORKER,
                            UserRole.COURT_ADMIN,
                            UserRole.LEGAL_ADVISOR,
                            UserRole.DISTRICT_JUDGE
        );
        return new PageBuilder(configBuilder.event(CASEWORKER_CHECK_AND_SEND_ORDERS).forAllStates().name(
            check_and_send_orders).showSummary().aboutToStartCallback(this::aboutToStart).grant(
            Permissions.CREATE_READ_UPDATE,
            UserRole.CASE_WORKER
        ).grant(Permissions.CREATE_READ_UPDATE, UserRole.DISTRICT_JUDGE));
    }


    /**
     * Method to fetch Check and send order list.
     *
     * @param details is type of Case Data
     * @return will return about to start response
     */
    public AboutToStartOrSubmitResponse<CaseData, State> aboutToStart(CaseDetails<CaseData, State> details) {
        CaseData caseData = details.getData();

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
        return AboutToStartOrSubmitResponse.<CaseData, State>builder().data(caseData).build();
    }

}
