package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.OrderData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.SelectedOrder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

public class CheckAndSendOrders implements CcdPageConfiguration {

    public static final String caseManageOrder = "Case Management Order (gatekeeping order)";

    @Override
    public void addTo(PageBuilder pageBuilder) {

        pageBuilder.page("checkAndSendOrders1", this::midEventCall)
            .pageLabel("## Orders for review")
            .label("checkAndSendOrdersLabel1","## Orders for review")
            .mandatory(CaseData::getCheckAndSendOrderDropdownList)
            .done()
            .build();

        pageBuilder.page("checkAndSendOrder2")
            .label("checkAndSendOrdersLabel2","## Review Order")
            .complex(CaseData::getSelectedOrder)
            .readonlyNoSummary(SelectedOrder::getReviewDocumentLink1)
            .readonlyNoSummary(SelectedOrder::getReviewDocumentLink2)
            .label("checkAndSendOrdersLabel5","### These recipients have been selected to receive this order",
                   null, false)
            .readonlyNoSummary(
                SelectedOrder::getOrderType,
                "checkAndSendOrderDropdownListCONTAINS\" " + caseManageOrder + " \"")
            .readonlyNoSummary(SelectedOrder::getAdoptionOrderRecipients,
                               "orderType=\"caseManagementOrder\"")
            .readonlyNoSummary(SelectedOrder::getGeneralDirectionOrderRecipients,
                               "orderType=\"generalDirectionsOrder\"")
            .readonlyNoSummary(SelectedOrder::getFinalOrderRecipientsA76,
                               "orderType=\"finalAdoptionOrder\"")
            .readonlyNoSummary(SelectedOrder::getFinalOrderRecipientsA206,
                               "orderType=\"finalAdoptionOrder\"")
            .done();

        pageBuilder.page("checkAndSendOrder3")
            .label("checkAndSendOrdersLabel3","## Review Order")
            .mandatory(CaseData::getOrderCheckAndSend)
            .done();
    }

    private AboutToStartOrSubmitResponse<CaseData, State> midEventCall(CaseDetails<CaseData, State> caseData,
                                                                       CaseDetails<CaseData, State> caseData1) {

        var data = caseData.getData();
        var commonOrderItem =   data.getCommonOrderList().stream().filter(item ->
                                                             item.getValue().getOrderId()
                                                             .equalsIgnoreCase(data.getCheckAndSendOrderDropdownList()
                                                             .getValueCode().toString())).findFirst();
        if (commonOrderItem.isPresent()) {
            var selectedItem = new SelectedOrder();
            OrderData orderData = commonOrderItem.get().getValue();
            selectedItem.setFinalOrderRecipientsA76(orderData.getFinalOrderRecipientsA76());
            selectedItem.setFinalOrderRecipientsA206(orderData.getFinalOrderRecipientsA206());
            selectedItem.setOrderType(orderData.getManageOrderType());
            selectedItem.setAdoptionOrderRecipients(orderData.getAdoptionOrderRecipients());
            selectedItem.setReviewDocumentLink1(orderData.getDocumentReview1());
            selectedItem.setReviewDocumentLink2(orderData.getDocumentReview2());
            selectedItem.setGeneralDirectionOrderRecipients(orderData.getGeneralDirectionOrderRecipients());
            selectedItem.setOrderStatus(orderData.getStatus());
            data.setSelectedOrder(selectedItem);
        }
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(data)
            .build();
    }
}
