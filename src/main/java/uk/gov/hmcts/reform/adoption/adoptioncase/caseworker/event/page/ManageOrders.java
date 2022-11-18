package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.DynamicListElement;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.AdoptionOrderData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ManageOrdersData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Children;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.ManageOrdersData.HearingNotices.HEARING_DATE_TO_BE_SPECIFIED_IN_THE_FUTURE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.ADOPTION_AGENCY_STR;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.APPLICANT_SOCIAL_WORKER_STR;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.CHILD_SOCIAL_WORKER_STR;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.COMMA;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.OTHER_ADOPTION_AGENCY_STR;

/**
 * Contains method to add Page Configuration for ExUI.
 * Display the Manage orders Details screen with all required fields.
 */
public class ManageOrders implements CcdPageConfiguration {

    public static final String ERROR_CHECK_HEARINGS_SELECTION = "Please check your selection for Hearings";

    /**
     * Overridden method to define page design and flow for entire event / Journey.
     *
     * @param pageBuilder - Application PageBuilder for the event pages
     */
    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder.page("manageOrders1")
            .complex(CaseData::getManageOrdersData)
            .mandatory(ManageOrdersData::getManageOrderActivity)
            .done()
            .page("mangeOrders2", this::midEventForDynamicList)
            .showCondition("manageOrderActivity=\"createOrder\"")
            .pageLabel("Manage orders and directions")
            .complex(CaseData::getManageOrdersData)
            .mandatory(ManageOrdersData::getManageOrderType, "manageOrderActivity=\"createOrder\"")
            .done()
            .done();
        getGatekeepingOrderPage(pageBuilder);
        getFinalOrderPage(pageBuilder);
        getFirstDirectionsPage(pageBuilder);
        getSecondDirectionsPage(pageBuilder);
        getServePartiesPage(pageBuilder);
    }

    /**
     * Helper method to support page design and flow to display complete list of Optional Fields for further selection.
     * Case Management Order
     *
     * @param pageBuilder - Application PageBuilder for the event pages
     */
    private void getGatekeepingOrderPage(PageBuilder pageBuilder) {
        pageBuilder.page("manageOrders3", this::midEvent)
            .showCondition("manageOrderType=\"caseManagementOrder\"")
            .complex(CaseData::getManageOrdersData)
            .pageLabel("Create case management (gatekeeping) order")
            .label("LabelPreamble31", "### Preamble", "manageOrderType=\"caseManagementOrder\"", true)
            .optional(ManageOrdersData::getPreambleDetails)
            .label("LabelAllocation32", "### Allocation")
            .optionalNoSummary(ManageOrdersData::getAllocationJudge)
            .label("LabelDateOrderMade76","### Date order made")
            .mandatory(ManageOrdersData::getDateOrderMade)
            .label("LabelHearings33", "### Hearings")
            .optionalNoSummary(ManageOrdersData::getHearingNotices)
            .label("LabelModeOfHearing34", "### Mode of hearing")
            .optionalNoSummary(ManageOrdersData::getModeOfHearing)
            .label("LabelLA35", "### Local authority")
            .optionalNoSummary(ManageOrdersData::getSelectedLocalAuthority)
            .label("LabelLA36", "### Cafcass")
            .optionalNoSummary(ManageOrdersData::getCafcass)
            .label("LabelAttendance37", "### Attendance")
            .optionalNoSummary(ManageOrdersData::getAttendance)
            .label("LabelLeaveToOppose38", "### Leave to oppose")
            .optionalNoSummary(ManageOrdersData::getLeaveToOppose)
            .label("LabelAdditionalPara39", "### Additional paragraphs")
            .label("LabelAdditionalParaValue30", "You can add any additional directions or paragraphs on a later screen.")
            .label("LabelCostOrders301", "### Cost orders")
            .optionalNoSummary(ManageOrdersData::getCostOrders)
            .done()
            .done();
    }

    /**
     * Helper method to support page design and flow to Display Fields for first directions screen.
     * PreambleDetails, AllocationJudge, HearingNotices, ModeOfHearing, LocalAuthority and CAFCASS
     * @param pageBuilder - Application PageBuilder for the event pages
     */
    private void getFirstDirectionsPage(PageBuilder pageBuilder) {
        pageBuilder.page("manageOrders4")
            .showCondition("preambleDetails=\"*\" OR allocationJudge=\"*\" "
                               + "OR hearingNoticesCONTAINS\"listForFirstHearing\" "
                               + "OR hearingNoticesCONTAINS\"listForFurtherHearings\" "
                               + "OR hearingNoticesCONTAINS\"hearingDateToBeSpecifiedInTheFuture\" "
                               + "OR modeOfHearingCONTAINS\"setModeOfHearing\" "
                               + "OR selectedLocalAuthorityCONTAINS\"fileAdoptionAgencyReport\" "
                               + "OR cafcassCONTAINS\"reportingOfficer\" "
                               + "OR cafcassCONTAINS\"childrensGuardian\"")
            //          PREAMBLE DETAILS
            .pageLabel("Case management order first directions")
            .label("LabelAdditionalParaValue411", "Review the paragraphs to be inserted into the order. "
                + "These are based on the options you chose on the previous page. "
                + "If you would like to change an option, go back to the previous page.")
            .label("LabelPreamble412", "### Preamble", "preambleDetails=\"*\"")
            .label("LabelPreambleValue413", "${preambleDetails}", "preambleDetails=\"*\"")
            //          ALLOCATION DETAILS
            .label("LabelAllocation421", "### Allocation",
                   "allocationJudge=\"*\"", true)
            .label("LabelAllocationValue422", "The case is allocated to [Name of the judge].",
                   "allocationJudge=\"allocatePreviousProceedingsJudge\"", true)
            .mandatory(CaseData::getAllocatedJudge, "allocationJudge=\"allocatePreviousProceedingsJudge\"")
            .label("LabelAllocationValue423", "The proceedings are reallocated to [Name of Judge].",
                   "allocationJudge=\"reallocateJudge\"", true)
            .label("LabelNameOfJudge424", "### Name of judge",
                   "allocationJudge=\"reallocateJudge\"")
            .complex(CaseData::getManageOrdersData)
            .mandatory(ManageOrdersData::getNameOfJudge, "allocationJudge=\"reallocateJudge\"")
            //            HEARINGS - FIRST HEARING
            .label("LabelHearings431", "### Hearings",
                   "hearingNoticesCONTAINS\"listForFirstHearing\" "
                       + "OR hearingNoticesCONTAINS\"listForFurtherHearings\" "
                       + "OR hearingNoticesCONTAINS\"hearingDateToBeSpecifiedInTheFuture\" "
                       + "OR modeOfHearingCONTAINS\"setModeOfHearing\"", true)
            .label("LabelHearings432", "The application is listed for a first hearing on [date] "
                       + "at [time] (with a time estimate of [length]) at [court name].",
                   "hearingNoticesCONTAINS\"listForFirstHearing\"", true)
            .label("LabelHearings433", "### Date and time",
                   "hearingNoticesCONTAINS\"listForFirstHearing\"", true)
            .mandatory(ManageOrdersData::getDateAndTimeFirstHearing, "hearingNoticesCONTAINS\"listForFirstHearing\"")
            .label("LabelHearings434", "### Length of hearing",
                   "hearingNoticesCONTAINS\"listForFirstHearing\"", true)
            .mandatory(ManageOrdersData::getLengthOfHearingFirstHearing, "hearingNoticesCONTAINS\"listForFirstHearing\"")
            .label("LabelNameOfCourt435", "### Name of court",
                   "hearingNoticesCONTAINS\"listForFirstHearing\"", true)
            .done()
            .mandatory(CaseData::getNameOfCourtFirstHearing,
                       "hearingNoticesCONTAINS\"listForFirstHearing\"")
            //            HEARINGS - FURTHER HEARING
            .label("LabelHearings441", "The application is listed for a [listing type] on [date] at "
                       + "[time] with a time estimate of [length] at [court name].",
                   "hearingNoticesCONTAINS\"listForFurtherHearings\"", true)
            .label("LabelNameOfCourt442", "### Listing type",
                   "hearingNoticesCONTAINS\"listForFurtherHearings\"", true)
            .complex(CaseData::getManageOrdersData)
            .mandatory(ManageOrdersData::getListingTypeFurtherHearing, "hearingNoticesCONTAINS\"listForFurtherHearings\"")
            .label("LabelHearings443", "### Date and time",
                   "hearingNoticesCONTAINS\"listForFurtherHearings\"", true)
            .mandatory(ManageOrdersData::getDateAndTimeFurtherHearing, "hearingNoticesCONTAINS\"listForFurtherHearings\"")
            .label("LabelHearings444", "### Length of hearing",
                   "hearingNoticesCONTAINS\"listForFurtherHearings\"", true)
            .mandatory(ManageOrdersData::getLengthOfHearingFurtherHearing,
                       "hearingNoticesCONTAINS\"listForFurtherHearings\"")
            .label("LabelNameOfCourt445", "### Name of court",
                   "hearingNoticesCONTAINS\"listForFurtherHearings\"", true)
            .done()
            .mandatory(CaseData::getNameOfCourtFurtherHearing,
                       "hearingNoticesCONTAINS\"listForFurtherHearings\"")
            //            HEARINGS - HEARING DATE IN FUTURE
            .label("LabelHearings451", "The application is listed for [listing type] on a date and time to be fixed by the court.",
                   "hearingNoticesCONTAINS\"hearingDateToBeSpecifiedInTheFuture\"", true)
            .label("LabelNameOfCourt452", "### Listing type",
                   "hearingNoticesCONTAINS\"hearingDateToBeSpecifiedInTheFuture\"", true)
            .complex(CaseData::getManageOrdersData)
            .mandatory(ManageOrdersData::getListingTypeHearingInFutureDate,
                       "hearingNoticesCONTAINS\"hearingDateToBeSpecifiedInTheFuture\"")
            //            HEARINGS - MODE OF HEARINGS
            .label("LabelNameOfCourt461", "### Mode of hearing",
                   "modeOfHearingCONTAINS\"setModeOfHearing\"", true)
            .mandatory(ManageOrdersData::getModeOfHearings,
                       "modeOfHearingCONTAINS\"setModeOfHearing\"")
            //            LOCAL AUTHORITY
            .label("LabelLocalAuthority471", "### Local authority",
                   "selectedLocalAuthorityCONTAINS\"fileAdoptionAgencyReport\"", true)
            .label("LabelLocalAuthority472", "The Local Authority shall:<br>"
                       + "<p>**1.** By [time] on [date] file the Adoption Agency Report (Annex A); and<br>"
                       + "**2.** By [time] on the (date in 14 days from the date above) file a statement detailing the following:</p>"
                       + "&emsp;**a.** The date the Local Authority most recently ascertained the views of the "
                       + "birth parents in relation to this application;"
                       + "<br>&emsp;**b.** The steps taken to confirm the current accuracy of the addresses provided? "
                       + "Whether the addresses provided for the birth parents upon issue of the application are accurate?"
                       + "<br>&emsp;**c.** Whether the parents are aware of the date of the hearing mentioned in this order?",
                   "selectedLocalAuthorityCONTAINS\"fileAdoptionAgencyReport\"", true)
            .label("LabelLocalAuthority473", "### Date and time for option 1",
                   "selectedLocalAuthorityCONTAINS\"fileAdoptionAgencyReport\"", true)
            .mandatory(ManageOrdersData::getDateAndTimeForOption1,
                       "selectedLocalAuthorityCONTAINS\"fileAdoptionAgencyReport\"")
            .label("LabelLocalAuthority474", "### Date and time for option 2",
                   "selectedLocalAuthorityCONTAINS\"fileAdoptionAgencyReport\"", true)
            .mandatory(ManageOrdersData::getTimeForOption2,
                       "selectedLocalAuthorityCONTAINS\"fileAdoptionAgencyReport\"")
            //            CAFCASS REPORTING OFFICER
            .label("LabelCafcass481", "### Cafcass",
                   "cafcassCONTAINS\"reportingOfficer\" "
                       + "OR cafcassCONTAINS\"childrensGuardian\"", true)
            .label("LabelCafcass482", "<p>[Cafcass/Cafcass Cymru] are directed to appoint a reporting officer. "
                       + "The reporting officer is requested to file a brief report together with the relevant consent forms "
                       + "witnessing the consent of the birth parents by [time] on [date].</p>"
                       + "<p>The Adoption Agency (Annex A) report may be disclosed to the reporting officer.</p>",
                   "cafcassCONTAINS\"reportingOfficer\"", true)
            .label("LabelCafcass483", "### Select reporting officer",
                   "cafcassCONTAINS\"reportingOfficer\"", true)
            .mandatory(ManageOrdersData::getReportingOfficer,
                       "cafcassCONTAINS\"reportingOfficer\"")
            .label("LabelCafcass484", "### Date and time",
                   "cafcassCONTAINS\"reportingOfficer\"", true)
            .mandatory(ManageOrdersData::getDateAndTimeRO,
                       "cafcassCONTAINS\"reportingOfficer\"")
            //            CAFCASS CHILDRENS GUARDIAN
            .label("LabelCafcass485", "<p>The child is made a respondent to the application and a children's guardian "
                       + "is appointed and [Cafcass/Cafcass Cymru] shall use their best endeavours to appoint the same guardian "
                       + "appointed within the previous proceedings case number [case number].</p>",
                   "cafcassCONTAINS\"childrensGuardian\"", true)
            .label("LabelCafcass486", "### Select reporting officer",
                   "cafcassCONTAINS\"childrensGuardian\"", true)
            .mandatory(ManageOrdersData::getChildrensGuardian,
                       "cafcassCONTAINS\"childrensGuardian\"")
            .label("LabelCafcass487", "### Case number",
                   "cafcassCONTAINS\"childrensGuardian\"", true)
            .mandatory(ManageOrdersData::getCaseNumberCG, "cafcassCONTAINS\"childrensGuardian\"")
            .done()
            .done();
    }

    /**
     * Helper method to support page design and flow to Display Fields for second directions screen.
     * Attendance, Leave To Oppose, Additional Paragraphs and Cost Orders
     * @param pageBuilder - Application PageBuilder for the event pages
     */
    private void getSecondDirectionsPage(PageBuilder pageBuilder) {
        pageBuilder.page("manageOrders5")
            .showCondition("manageOrderType=\"caseManagementOrder\"")
            //          ATTENDANCE DETAILS
            .pageLabel("Case management order second directions")
            .label("LabelAttendance51", "### Attendance",
                   "attendanceCONTAINS\"applicantsAttendance\" "
                       + "OR attendanceCONTAINS\"childAttendance\" "
                       + "OR attendanceCONTAINS\"localAuthorityAttendance\" "
                       + "OR attendanceCONTAINS\"birthParentsAttendance\"")
            .label("LabelAttendanceValue52", "Choose the direction for the attendees",
                   "attendanceCONTAINS\"applicantsAttendance\" "
                       + "OR attendanceCONTAINS\"childAttendance\" "
                       + "OR attendanceCONTAINS\"localAuthorityAttendance\" "
                       + "OR attendanceCONTAINS\"birthParentsAttendance\"")
            //          APPLICANT ATTENDANCE
            .label("LabelApplicantAttendance511", "### Applicant attendance",
                   "attendanceCONTAINS\"applicantsAttendance\"", true)
            .complex(CaseData::getManageOrdersData)
            .mandatory(ManageOrdersData::getApplicantAttendance,
                       "attendanceCONTAINS\"applicantsAttendance\"")
            //          CHILD ATTENDANCE
            .label("LabelChildAttendance521", "### Child attendance",
                   "attendanceCONTAINS\"childAttendance\"", true)
            .mandatory(ManageOrdersData::getChildAttendance,
                       "attendanceCONTAINS\"childAttendance\"", true)
            //          LA ATTENDANCE
            .label("LabelChildAttendance531", "### Local authority attendance",
                   "attendanceCONTAINS\"localAuthorityAttendance\"", true)
            .mandatory(ManageOrdersData::getLaAttendance,
                       "attendanceCONTAINS\"localAuthorityAttendance\"")
            //          BIRTH PARENT ATTENDANCE
            .label("LabelChildAttendance541", "### Birth parent attendance",
                   "attendanceCONTAINS\"birthParentsAttendance\"", true)
            .mandatory(ManageOrdersData::getBirthParentAttendance,
                       "attendanceCONTAINS\"birthParentsAttendance\"")
            //          LEAVE TO OPPOSE
            .label("LabelLeaveToOppose551", "### Leave to oppose",
                   "leaveToOpposeCONTAINS\"leaveToOppose\"", true)
            .label("LabelLeaveToOppose552", "<p>A birth parent who wants to oppose the making of an adoption order or seek "
                       + "contact with the child is invited to notify the court in writing by (14 days from the date of this order).</p>"
                       + "<p>A birth parent can only oppose the making of an Adoption Order if "
                       + "they have first been given permission to do so by the Court.</p>"
                       + "<p>A birth parent who wishes to apply to the court for permission to oppose the making of an adoption order must "
                       + "send a statement to the court setting out how circumstances have changed since the date when the Placement "
                       + "Order was made. If you have any certificates or other documents of courses completed or work, you have done "
                       + "these should be attached to the statement.</p>"
                       + "<p>The statement should then also explain to the court why it would be in the best interests of the child for "
                       + "you to be given permission to oppose the adoption.</p>"
                       + "<p>If no such application is made the case may proceed to a final hearing which may be dealt with without "
                       + "attendance of the parties unless such an attendance is requested by any of the parties. If such a "
                       + "request is made, the hearing may be conducted remotely unless this cannot be fairly undertaken.</p>",
                   "leaveToOpposeCONTAINS\"leaveToOppose\"", true)
            //          ADDITIONAL PARAGRAPHS
            .label("LabelAdditionalPara561", "### Additional paragraphs", null, true)
            .mandatory(ManageOrdersData::getAdditionalPara)
            //          COST ORDERS
            .label("LabelCostOrders571", "### Cost orders", "costOrdersCONTAINS\"noOrderForCosts\"", true)
            .label("LabelCostOrders572", "No order for costs.", "costOrdersCONTAINS\"noOrderForCosts\"", true)
            //          ORDERED BY
            .label("LabelCostOrders581", "### Ordered by", null, true)
            .mandatory(ManageOrdersData::getOrderedBy)
            .done()
            .done();
    }

    /**
     * Helper method to support page design and flow to Display Fields for serve parties screen.
     * Recipients
     * @param pageBuilder - Application PageBuilder for the event pages
     */
    private void getServePartiesPage(PageBuilder pageBuilder) {
        pageBuilder.page("manageOrders6")
            .showCondition("manageOrderType=\"caseManagementOrder\"")
            .pageLabel("Case management order recipients")
            .complex(CaseData::getManageOrdersData)
            .label("LabelRecipients661", "#### Select who to serve the order to", null, true)
            .mandatory(ManageOrdersData::getRecipientsList)
            .done()
            .done();
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
            .mandatory(AdoptionOrderData::getDateOrderMade)
            .label("LabelOrderedBy73","### Placement of the child", null, true)
            .mandatory(AdoptionOrderData::getPlacementOfTheChildList)
            .done()
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
            .label("pageLabel82","### Has place of birth been proved?", null, true)
            .mandatory(AdoptionOrderData::getPlaceOfBirthProved)
            .label("pageLabel83","#### Choose the type of certificate","placeOfBirthProved=\"Yes\"", true)
            .mandatory(AdoptionOrderData::getTypeOfCertificate,"placeOfBirthProved=\"Yes\"")
            .label("pageLabel84","#### Choose the country of birth","placeOfBirthProved=\"Yes\"", true)
            .mandatory(AdoptionOrderData::getCountryOfBirthForPlaceOfBirthYes,"placeOfBirthProved=\"Yes\"")
            .mandatory(AdoptionOrderData::getOtherCountryOfOriginForPlaceOfBirthYes,
                       "countryOfBirthForPlaceOfBirthYes=\"outsideTheUK\" AND placeOfBirthProved=\"Yes\"")
            .label("pageLabel85","#### Choose a probable birth location","placeOfBirthProved=\"No\"", true)
            .mandatory(AdoptionOrderData::getCountryOfBirthForPlaceOfBirthNo,"placeOfBirthProved=\"No\"")
            .mandatory(AdoptionOrderData::getOtherCountryOfOriginForPlaceOfBirthNo,
                       "countryOfBirthForPlaceOfBirthNo=\"outsideTheUK\" AND placeOfBirthProved=\"No\"")
            .label("pageLabel86","### Is time of birth known?", null, true)
            .mandatory(AdoptionOrderData::getTimeOfBirthKnown)
            .label("pageLabel87","#### Time of birth","timeOfBirthKnown=\"Yes\"", true)
            .mandatory(AdoptionOrderData::getTimeOfBirth,"timeOfBirthKnown=\"Yes\"")
            .label("pageLabel88","### Birth adoption registration number", null,  true)
            .mandatory(AdoptionOrderData::getBirthAdoptionRegistrationNumber)
            .label("pageLabel89","### Birth/Adoption registration date", null, true)
            .mandatory(AdoptionOrderData::getAdoptionRegistrationDate)
            .label("pageLabel810","### Registration district", null, true)
            .mandatory(AdoptionOrderData::getRegistrationDistrict)
            .label("pageLabel811","### Registration sub-district",null, true)
            .mandatory(AdoptionOrderData::getRegistrationSubDistrict)
            .label("pageLabel812","### Registration county",null, true)
            .mandatory(AdoptionOrderData::getRegistrationCounty)
            .done()
            .done();
    }

    /**
     * Event method to validate the right selection options done by the User as per the Requirements for Hearing Notices.
     * Gatekeeping Order Page
     *
     * @param detailsBefore - Application CaseDetails for the previous page
     * @param details - Application CaseDetails for the present page
     * @return - AboutToStartOrSubmitResponse updated to use on further pages.
     */
    public AboutToStartOrSubmitResponse<CaseData, State> midEvent(
        CaseDetails<CaseData, State> details,
        CaseDetails<CaseData, State> detailsBefore
    ) {
        CaseData caseData = details.getData();
        final List<String> errors = new ArrayList<>();
        caseData.setAllocatedJudge(detailsBefore.getData().getAllocatedJudge());
        Set<ManageOrdersData.HearingNotices> selectedHearingNotices = caseData.getManageOrdersData().getHearingNotices();

        if (isNotEmpty(selectedHearingNotices)
            && selectedHearingNotices.contains(HEARING_DATE_TO_BE_SPECIFIED_IN_THE_FUTURE)
            && (isNotEmpty(caseData.getManageOrdersData().getModeOfHearing()) || selectedHearingNotices.size() > 1)) {
            errors.add(ERROR_CHECK_HEARINGS_SELECTION);
        }

        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .errors(errors)
            .build();
    }

    public AboutToStartOrSubmitResponse<CaseData, State> midEventForDynamicList(
        CaseDetails<CaseData, State> details,
        CaseDetails<CaseData, State> detailsBefore
    ) {
        CaseData caseData = details.getData();
        details.getData().getChildren().setFirstNameAfterAdoption(detailsBefore.getData().getChildren().getFirstNameAfterAdoption());
        details.getData().getChildren().setLastNameAfterAdoption(detailsBefore.getData().getChildren().getLastNameAfterAdoption());
        List<DynamicListElement> listElements = new ArrayList<>();



        if (caseData.getAdopAgencyOrLA() != null) {
            DynamicListElement adoptionAgency = DynamicListElement.builder()
                .label(String.join(COMMA, caseData.getAdopAgencyOrLA().getAdopAgencyOrLaName(),
                                   caseData.getAdopAgencyOrLA().getAdopAgencyTown(),
                                   caseData.getAdopAgencyOrLA().getAdopAgencyPostcode()))
                .code(UUID.nameUUIDFromBytes(ADOPTION_AGENCY_STR.getBytes()))
                .build();

            listElements.add(adoptionAgency);
        }

        if (YesOrNo.YES.equals(caseData.getHasAnotherAdopAgencyOrLAinXui())) {
            DynamicListElement otherAdoptionAgency = DynamicListElement.builder()
                .label(String.join(COMMA, caseData.getOtherAdoptionAgencyOrLA().getAgencyOrLaName(),
                                   caseData.getOtherAdoptionAgencyOrLA().getAgencyAddress().getPostTown(),
                                   caseData.getOtherAdoptionAgencyOrLA().getAgencyAddress().getPostCode()))
                .code(UUID.nameUUIDFromBytes(OTHER_ADOPTION_AGENCY_STR.getBytes()))
                .build();

            listElements.add(otherAdoptionAgency);
        }

        if (caseData.getChildSocialWorker() != null) {
            DynamicListElement childLocalAuthority = DynamicListElement.builder()
                .label(String.join(COMMA, caseData.getChildSocialWorker().getSocialWorkerName(),
                                   caseData.getChildSocialWorker().getSocialWorkerTown(),
                                   caseData.getChildSocialWorker().getSocialWorkerPostcode()))
                .code(UUID.nameUUIDFromBytes(CHILD_SOCIAL_WORKER_STR.getBytes()))
                .build();
            listElements.add(childLocalAuthority);
        }

        if (caseData.getApplicantSocialWorker() != null) {
            DynamicListElement applicantLocalAuthority = DynamicListElement.builder()
                .label(String.join(COMMA, caseData.getApplicantSocialWorker().getSocialWorkerName(),
                                   caseData.getApplicantSocialWorker().getSocialWorkerTown(),
                                   caseData.getApplicantSocialWorker().getSocialWorkerPostcode()))
                .code(UUID.nameUUIDFromBytes(APPLICANT_SOCIAL_WORKER_STR.getBytes()))
                .build();
            listElements.add(applicantLocalAuthority);
        }

        caseData.getAdoptionOrderData().setPlacementOfTheChildList(DynamicList.builder()
                .listItems(listElements).value(DynamicListElement.EMPTY).build());

        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .build();
    }
}
