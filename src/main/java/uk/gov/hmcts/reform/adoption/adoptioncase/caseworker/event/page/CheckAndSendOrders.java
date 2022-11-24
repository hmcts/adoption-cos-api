package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.*;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

import java.util.Optional;

public class CheckAndSendOrders implements CcdPageConfiguration {
    @Override
    public void addTo(PageBuilder pageBuilder) {

        pageBuilder.page("checkAndSendOrders1", this::midEventCall)
            .pageLabel("## Orders for review")
            //.label("checkAndSendOrdersLabel1","## Orders for review")
            .label("checkAndSendOrderLabel2","### Select the order you want to review",null,true)
            .mandatory(CaseData::getCheckAndSendOrderDropdownList)
            .done()
            .build();

        pageBuilder.page("checkAndSendOrders2")
            .label("checkAndSendOrdersLabel2","## Review Order")
            .readonly(CaseData::getDocumentReview)
            .label("checkAndSendOrdersLabel5","## These recipients have been selected to receive this order",
                   null, true)
            .readonly(CaseData::getManageOrderSelecType, "checkAndSendOrderDropdownListCONTAINS\"Case Management Order (gatekeeping order)\"")
            .readonly(CaseData::getAdoptionOrderRecipients,  "manageOrderSelecType=\"Case Management Order (gatekeeping order)\"")
            .readonly(CaseData::getFinalOrderRecipients, "manageOrderSelecType=\"Final Adoption Order\"")
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
        Optional<ListValue<ManageOrdersData>> gatekeepingOrderItem =  data.getManageOrderList().stream()
            .filter(item -> item.getValue().getOrderId()
                .equalsIgnoreCase(data.getCheckAndSendOrderDropdownList().getValueCode().toString())).findFirst();
        if (gatekeepingOrderItem.isEmpty()) {
            Optional<ListValue<DirectionsOrderData>> directionOrderItem =  data.getDirectionsOrderList().stream()
                .filter(item -> item.getValue().getOrderId()
                    .equalsIgnoreCase(data.getCheckAndSendOrderDropdownList().getValueCode().toString())).findFirst();

            if (directionOrderItem.isEmpty()) {
                Optional<ListValue<AdoptionOrderData>> finalAdoptionItem =  data.getAdoptionOrderList().stream()
                    .filter(item -> item.getValue().getOrderId()
                        .equalsIgnoreCase(data.getCheckAndSendOrderDropdownList().getValueCode().toString()))
                    .findFirst();
                data.setFinalOrderRecipients(finalAdoptionItem.get().getValue().getRecipientsListA206());
                data.setManageOrderSelecType(ManageOrdersData.ManageOrderType.FINAL_ADOPTION_ORDER.getLabel());
            }
        } else {
            data.setAdoptionOrderRecipients(gatekeepingOrderItem.get().getValue().getRecipientsList());
            data.setManageOrderSelecType(ManageOrdersData.ManageOrderType.CASE_MANAGEMENT_ORDER.getLabel());
        }
        System.out.println("TYPE"+ data.getManageOrderSelecType());
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(data)
            .build();
    }
}
