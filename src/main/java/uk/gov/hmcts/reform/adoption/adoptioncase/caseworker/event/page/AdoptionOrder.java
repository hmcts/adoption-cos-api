package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.reform.adoption.adoptioncase.model.Children;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.AdoptionOrderData;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

/**
 * Contains method to add Page Configuration for ExUI.
 * Display the Manage orders Details screen with all required fields.
 */
public class AdoptionOrder implements CcdPageConfiguration {


    public static final String ERROR_CHECK_RECIPIENTS_SELECTION = "Recipients of Final adoption order is required";
    public static final String FIRST_APPLICANT_NOT_APPLICABLE = "First Applicant not applicable for the case";
    public static final String SECOND_APPLICANT_NOT_APPLICABLE = "Second Applicant not applicable for the case";
    public static final String BIRTH_MOTHER_NOT_APPLICABLE = "Recipient birth mother not applicable for the case";
    public static final String BIRTH_FATHER_NOT_APPLICABLE = "Recipient birth father not applicable for the case";
    public static final String LEGAL_GUARDIAN_NOT_APPLICABLE = "Legal guardian (CAFCASS) not applicable for the case";
    public static final String CHILDS_LA_NOT_APPLICABLE = "Child local authority not applicable for the case";
    public static final String APPLICANTS_LA_NOT_APPLICABLE = "Applicants local authority not applicable for the case";
    public static final String ADOP_AGENCY_NOT_APPLICABLE = "Adoption agency not applicable for the case";
    public static final String OTHER_ADOP_AGENCY_NOT_APPLICABLE = "Other adoption agency not applicable for the case";
    public static final String OTHER_PARENT_AGENCY_NOT_APPLICABLE = "Other person with parental responsibility not applicable for the case";

    /**
     * Overridden method to define page design and flow for entire event / Journey.
     *
     * @param pageBuilder - Application PageBuilder for the event pages
     */
    @Override
    public void addTo(PageBuilder pageBuilder) {
        getFinalOrderPage(pageBuilder);
    }

    /**
     * Helper method to support page design and flow to display complete list of Optional Fields for further selection.
     * Final Adoption Order
     *
     * @param pageBuilder - Application PageBuilder for the event pages
     */
    private void getFinalOrderPage(PageBuilder pageBuilder) {
        pageBuilder.page("manageOrders7")
            .showCondition("manageOrderType=\"finalAdoptionOrder\"")
            .label("pageLabel71","Please select all the relevant options for this order."
                +  " The directions attached to each option can be reviewed on the next screens. "
                +  "You can change your options by returning to the previous screens.")
            .complex(CaseData::getAdoptionOrderData)
            .label("LabelPreamble71", "### Preamble", null, true)
            .optional(AdoptionOrderData::getPreambleDetailsFinalAdoptionOrder)
            .label("LabelOrderedBy72","### Ordered by", null, true)
            .mandatory(AdoptionOrderData::getOrderedByFinalAdoptionOrder)
            .label("LabelDateOrderMade76","### Date order made", null, true)
            .mandatory(AdoptionOrderData::getDateOrderMadeFinalAdoptionOrder)
            .label("LabelOrderedBy73","### Placement of the child", null, true)
            .done()
            .mandatory(CaseData::getPlacementOfTheChildList)
            .complex(CaseData::getChildren)
            .label("LabelChildFullNameAfterAdoption74","### Child's full name after adoption", null, true)
            .mandatory(Children::getFirstNameAfterAdoption)
            .mandatory(Children::getLastNameAfterAdoption)
            .done()
            .complex(CaseData::getAdoptionOrderData)
            .label("LabelCostOrders75","### Cost orders", null, true)
            .optional(AdoptionOrderData::getCostOrdersFinalAdoptionOrder)
            .done();

        pageBuilder.page("manageOrders8")
            .showCondition("manageOrderType=\"finalAdoptionOrder\"")
            .complex(CaseData::getAdoptionOrderData)
            .label("pageLabel81","## Final adoption order date and place of birth")
            .label("pageLabel82","### Has date of birth been proved?", null, true)
            .mandatory(AdoptionOrderData::getDateOfBirthProved)
            .label("pageLabel83","### Has place of birth been proved?", null, true)
            .mandatory(AdoptionOrderData::getPlaceOfBirthProved)
            .label("pageLabel84","#### Choose the type of certificate","placeOfBirthProved=\"Yes\"", true)
            .mandatory(AdoptionOrderData::getTypeOfCertificate,"placeOfBirthProved=\"Yes\"")
            .label("pageLabel85","#### Choose the country of birth","placeOfBirthProved=\"Yes\"", true)
            .mandatory(AdoptionOrderData::getCountryOfBirthForPlaceOfBirthYes,"placeOfBirthProved=\"Yes\"")
            .mandatory(AdoptionOrderData::getOtherCountryOfOriginForPlaceOfBirthYes,
                       "countryOfBirthForPlaceOfBirthYes=\"outsideTheUK\" AND placeOfBirthProved=\"Yes\"")
            .label("pageLabel86","#### Choose a probable birth location","placeOfBirthProved=\"No\"", true)
            .mandatory(AdoptionOrderData::getCountryOfBirthForPlaceOfBirthNo,"placeOfBirthProved=\"No\"")
            .mandatory(AdoptionOrderData::getOtherCountryOfOriginForPlaceOfBirthNo,
                       "countryOfBirthForPlaceOfBirthNo=\"outsideTheUK\" AND placeOfBirthProved=\"No\"")
            .label("pageLabel87","### Is time of birth known?", null, true)
            .mandatory(AdoptionOrderData::getTimeOfBirthKnown)
            .label("pageLabel88","#### Time of birth","timeOfBirthKnown=\"Yes\"", true)
            .mandatory(AdoptionOrderData::getTimeOfBirth,"timeOfBirthKnown=\"Yes\"")
            .label("pageLabel89","### Birth adoption registration number", null,  true)
            .mandatory(AdoptionOrderData::getBirthAdoptionRegistrationNumber)
            .label("pageLabel810","### Birth/Adoption registration date", null, true)
            .mandatory(AdoptionOrderData::getAdoptionRegistrationDate)
            .label("pageLabel811","### Registration district", null, true)
            .mandatory(AdoptionOrderData::getRegistrationDistrict)
            .label("pageLabel812","### Registration sub-district",null, true)
            .mandatory(AdoptionOrderData::getRegistrationSubDistrict)
            .label("pageLabel813","### Registration county",null, true)
            .mandatory(AdoptionOrderData::getRegistrationCounty)
            .done()
            .done();
    }
}
