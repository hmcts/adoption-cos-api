package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.DynamicListElement;
import uk.gov.hmcts.reform.adoption.adoptioncase.common.CaseEventCommonMethods;
import uk.gov.hmcts.reform.adoption.adoptioncase.common.CommonPageBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.SelectedOrder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CheckAndSendOrders implements CcdPageConfiguration {

    public static final String caseManageOrder = "Case Management Order (gatekeeping order)";

    @Override
    public void addTo(PageBuilder pageBuilder) {

        pageBuilder.page("checkAndSendOrders1", this::midEventCall)
            .pageLabel("## Orders for review")
            .label("checkAndSendOrdersLabel1","## Orders for review")
            .readonly(CaseData::getLoggedInUserRole, "checkAndSendOrdersLabel1=\"caseManagementOrder\"")
            .mandatory(CaseData::getCheckAndSendOrderDropdownList)
            .done()
            .build();

        pageBuilder.page("checkAndSendOrder2")
            .label("checkAndSendOrdersLabel2","## Review Order")
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
            .label("checkAndSendOrdersLabel3","## Review Order")
            .mandatory(CaseData::getOrderCheckAndSend)
            .done();
        CommonPageBuilder.sendOrReplyCommonPage(pageBuilder, "orderCheckAndSend=\"returnForAmendments\"");
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
        List<DynamicListElement> listElements = new ArrayList<>();
        CaseEventCommonMethods.prepareDocumentList(data).forEach(item -> listElements.add(DynamicListElement.builder()
                                                                         .label(item.getDocumentLink().getFilename())
                                                                         .code(UUID.fromString(item.getMessageId())).build()));
        data.setAttachDocumentList(DynamicList.builder().listItems(listElements).value(DynamicListElement.EMPTY).build());
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(data)
            .build();
    }
}
