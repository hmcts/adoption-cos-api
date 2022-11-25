package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.DirectionsOrderData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ManageOrdersData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.AdoptionOrderData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

import java.util.Optional;

public class CheckAndSendOrders implements CcdPageConfiguration {

    public static final String caseManageGateKeepingOrder = "Case Management Order (gatekeeping order)";

    @Override
    public void addTo(PageBuilder pageBuilder) {

        pageBuilder.page("checkAndSendOrders1",this::midEventCall)
            .pageLabel("Orders for review")
            //.label("checkAndSendOrdersLabel1","## Orders for review")
            .label("checkAndSendOrderLabel2","### Select the order you want to review",null,true)
            .mandatory(CaseData::getCheckAndSendOrderDropdownList)
            .done()
            .build();

        pageBuilder.page("checkAndSendOrders2")
            .pageLabel("Review Order")
            .readonlyNoSummary(CaseData::getDocumentReview)
            .label("checkAndSendOrdersLabel5","## These recipients have been selected to receive this order",
                   null, false)
            .readonlyNoSummary(CaseData::getManageOrderSelecType,
                      "checkAndSendOrderDropdownListCONTAINS\" " + caseManageGateKeepingOrder + " \"")
            .readonlyNoSummary(CaseData::getAdoptionOrderRecipients,  "manageOrderSelecType=\"caseManagementOrder\"")
            .readonlyNoSummary(CaseData::getFinalOrderRecipients, "manageOrderSelecType=\"finalAdoptionOrder\"")
            .done();

        pageBuilder.page("checkAndSendOrders3")
            .pageLabel("Review Order")
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
                data.setDocumentReview(finalAdoptionItem.get().getValue().getDraftDocument());
                data.setFinalOrderRecipients(finalAdoptionItem.get().getValue().getRecipientsListA206());
                data.setManageOrderSelecType(ManageOrdersData.ManageOrderType.FINAL_ADOPTION_ORDER);
            } else {
                data.setDocumentReview(directionOrderItem.get().getValue().getDraftDocument());
                data.setManageOrderSelecType(ManageOrdersData.ManageOrderType.GENERAL_DIRECTIONS_ORDER);
            }
        } else {
            data.setDocumentReview(gatekeepingOrderItem.get().getValue().getDraftDocument());
            data.setAdoptionOrderRecipients(gatekeepingOrderItem.get().getValue().getRecipientsList());
            data.setManageOrderSelecType(ManageOrdersData.ManageOrderType.CASE_MANAGEMENT_ORDER);
        }
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(data)
            .build();
    }
}
