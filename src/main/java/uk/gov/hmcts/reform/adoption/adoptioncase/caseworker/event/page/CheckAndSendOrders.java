package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

public class CheckAndSendOrders implements CcdPageConfiguration {

    public static final String caseManageGateKeepingOrder = "Case Management Order (gatekeeping order)";

    @Override
    public void addTo(PageBuilder pageBuilder) {

        pageBuilder.page("checkAndSendOrders1",this::midEventCall)
            .pageLabel("## Orders for review")
            .label("checkAndSendOrderLabel2","### Select the order you want to review",null,true)
            .mandatory(CaseData::getCheckAndSendOrderDropdownList)
            .done()
            .build();

        pageBuilder.page("checkAndSendOrders2")
            .label("checkAndSendOrdersLabel2","## Review Order")
            .readonly(CaseData::getDocumentReview)
            .label("checkAndSendOrdersLabel5","## These recipients have been selected to receive this order",
                   null, true)
            .readonly(CaseData::getManageOrderSelecType,
                      "checkAndSendOrderDropdownListCONTAINS\" " + caseManageGateKeepingOrder + " \"")
            .readonly(CaseData::getAdoptionOrderRecipients,  "manageOrderSelecType=\"caseManagementOrder\"")
            .readonly(CaseData::getFinalOrderRecipients, "manageOrderSelecType=\"finalAdoptionOrder\"")
            .done();

        pageBuilder.page("checkAndSendOrders3")
            .label("checkAndSendOrdersLabel3","## Review Order")
            .label("checkAndSendOrdersLabel4","## Do you want to server the order or return for amendments?",
                   null, true)
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
            data.setFinalOrderRecipients(commonOrderItem.get().getValue().getFinalOrderRecipients());
            data.setManageOrderSelecType(commonOrderItem.get().getValue().getManageOrderType());
            data.setAdoptionOrderRecipients(commonOrderItem.get().getValue().getAdoptionOrderRecipients());
            data.setDocumentReview(commonOrderItem.get().getValue().getDocumentReview());
        }
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(data)
            .build();
    }
}
