package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;


import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.DirectionsOrderData;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

public class GeneralDirectionOrders implements CcdPageConfiguration {

    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder.page("manageOrderGD1")
            .showCondition("manageOrderType=\"generalDirectionsOrder\"")
            .pageLabel("General directions order")
            .label("generalDirectionOrderLabel2", "## General directions order")
            .label("generalDirectionOrderLabel1", "## Enter hearing details")
            .complex(CaseData::getDirectionsOrderData)
            .label("generalDirectionOrderBy", "### Order by", null, true)
            .mandatory(DirectionsOrderData::getDirectionOrderBy)
            .label("generalDirectionDateOfOrderMade", "### Date order made", null, true)
            .mandatory(DirectionsOrderData::getDateOfGeneralDirectionOrderMade)
            .label("generalDirectionPreamble", "### Preamble", null, true)
            .optional(DirectionsOrderData::getGeneralDirectionOrderPreamble)
            .label("generalDirectionBodyOfOrder", "### Body of the order", null, true)
            .optional(DirectionsOrderData::getGeneralDirectionBodyOfTheOrder)
            .label("generalDirectionCostOrders", "### Cost orders", null, true)
            .optional(DirectionsOrderData::getGeneralDirectionCostOfOrder)
            .label("generalDirectionIncludedInTheOrder", "### Direction to include in the order",
                   null, true
            )
            .optional(DirectionsOrderData::getGeneraDirectionsIncluded)
            .label(
                "generalDirectionOrderTypeYouNeedToSend",
                "### Is there another order type you need to send instead?",
                null,
                true
            )
            .mandatory(DirectionsOrderData::getIsThereAnyOrderTypeYouNeedToSend)
            .mandatory(DirectionsOrderData::getGeneralDirectionOrderTypes, "isThereAnyOrderTypeYouNeedToSend=\"Yes\"")
            .done()
            .page("manageOrderGD2")
            .showCondition(
                "manageOrderType=\"generalDirectionsOrder\" AND generalDirectionOrderTypes=\"generalDirectionProductionOrder\"")
            .label("manageOrderGdProductOrderTitle","## Production order")
            .label("manageOrderGdGov", "<p>To The Governor<br>"
                + "HMP{insert address}.</p>")
            .complex(CaseData::getDirectionsOrderData)
            .label("manageOrderGdGovLab", "### Name of prison", null, true)
            .mandatory(DirectionsOrderData::getGeneralDirectionNameOfPrison)
            .label("manageOrderGdGovLabAddOfPrison", "### Address of prison", null, true)
            .mandatory(DirectionsOrderData::getGeneralDirectionAddressOfPrison)
            .label("manageOrderGdGovNameOfPrisoner", "<p>Please arrange for the prisoner {insert name} <br>"
                + "(Prisoner number:{insert prisoner number}) to attend at {insert hearing venue} on<br>"
                + "{insert heating date} at {insert hearing time} for a hearing in the above case</p>")
            .label("manageOrderGdGovLabNameOfPrisoner", "### Name of prisoner", null, true)
            .mandatory(DirectionsOrderData::getGeneralDirectionNameOfThePrisoner)
            .label("manageOrderGdGovLabPrisonerNum", "### Prisoner number", null, true)
            .mandatory(DirectionsOrderData::getGeneralDirectionPrisonerNumber)
            .label("manageOrderGdGovLabHearingVen", "### Hearing Venue", null, true)
            .mandatory(DirectionsOrderData::getGeneralDirectionHearingVenue)
            .label("manageOrderGdGovDateTime", "### Hearing time and date", null, true)
            .mandatory(DirectionsOrderData::getGeneralDirectionHearingDateTime)
            .done();

    }
}
