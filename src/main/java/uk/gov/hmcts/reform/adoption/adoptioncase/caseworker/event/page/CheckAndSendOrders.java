package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.SelectedOrder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.service.CommonPageBuilder;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

public class CheckAndSendOrders implements CcdPageConfiguration {

    public static final String caseManageGateKeepingOrder = "Case Management Order (gatekeeping order)";

    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder.page("checkAndSendOrders1", this::midEventCall)
            .pageLabel("## Orders for review")
            .label("checkAndSendOrdersLabel1","## Orders for review")
            .mandatory(CaseData::getCheckAndSendOrderDropdownList)
            .done()
            .build();

        pageBuilder.page("pageNote")
            .complex(CaseData::getSelectedOrder)
            .readonlyNoSummary(SelectedOrder::getDocumentReview)
            .readonlyNoSummary(
                SelectedOrder::getOrderType,
                "checkAndSendOrderDropdownListCONTAINS\" " + caseManageGateKeepingOrder + " \"")
            .readonlyNoSummary(SelectedOrder::getAdoptionOrderRecipients,
                      "orderType=\"caseManagementOrder\"")
            .readonlyNoSummary(SelectedOrder::getFinalOrderRecipients,
                      "orderType=\"finalAdoptionOrder\"")
            .done();

        pageBuilder.page("checkAndSendOrders3")
            .label("checkAndSendOrdersLabel3","## Review Order")
            .label("checkAndSendOrdersLabel4","## Do you want to server the order or return for amendments?",
                   null, true)
            .mandatory(CaseData::getOrderCheckAndSend)
            .done();

        CommonPageBuilder.commonPage(pageBuilder, "orderCheckAndSend=\"returnForAmendments\"");
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
            selectedItem.setFinalOrderRecipients(commonOrderItem.get().getValue().getFinalOrderRecipients());
            selectedItem.setOrderType(commonOrderItem.get().getValue().getManageOrderType());
            selectedItem.setAdoptionOrderRecipients(commonOrderItem.get().getValue().getAdoptionOrderRecipients());
            selectedItem.setDocumentReview(commonOrderItem.get().getValue().getDocumentReview());
            selectedItem.setOrderStatus(commonOrderItem.get().getValue().getStatus());
            data.setSelectedOrder(selectedItem);
        }
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(data)
            .build();
    }
}
