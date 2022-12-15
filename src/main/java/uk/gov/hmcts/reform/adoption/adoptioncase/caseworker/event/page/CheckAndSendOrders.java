package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
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
            .label("checkAndSendOrdersLabel2","## Review order")
            .complex(CaseData::getSelectedOrder)
            .readonlyNoSummary(SelectedOrder::getReviewDocumentLink)
            .label("checkAndSendOrdersLabel5","### These recipients have been selected to receive this order",
                   null, false)
            .readonlyNoSummary(
                SelectedOrder::getOrderType,
                "checkAndSendOrderDropdownListCONTAINS\" " + caseManageOrder + " \"")
            .readonlyNoSummary(SelectedOrder::getAdoptionOrderRecipients,
                               "orderType=\"caseManagementOrder\"")
            .readonlyNoSummary(SelectedOrder::getFinalOrderRecipientsA76,
                               "orderType=\"finalAdoptionOrder\"")
            .readonlyNoSummary(SelectedOrder::getFinalOrderRecipientsA206,
                               "orderType=\"finalAdoptionOrder\"")
            .done();

        pageBuilder.page("checkAndSendOrder3")
            .label("checkAndSendOrdersLabel3","## Review order")
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
            selectedItem.setFinalOrderRecipientsA76(commonOrderItem.get().getValue().getFinalOrderRecipientsA76());
            selectedItem.setFinalOrderRecipientsA206(commonOrderItem.get().getValue().getFinalOrderRecipientsA206());
            selectedItem.setOrderType(commonOrderItem.get().getValue().getManageOrderType());
            selectedItem.setAdoptionOrderRecipients(commonOrderItem.get().getValue().getAdoptionOrderRecipients());
            selectedItem.setReviewDocumentLink(commonOrderItem.get().getValue().getDocumentReview());
            selectedItem.setOrderStatus(commonOrderItem.get().getValue().getStatus());
            data.setSelectedOrder(selectedItem);
        }
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(data)
            .build();
    }
}
