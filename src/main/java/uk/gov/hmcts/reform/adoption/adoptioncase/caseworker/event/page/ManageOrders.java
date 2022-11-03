package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.HearingNotices;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.HearingNotices.HEARING_DATE_TO_BE_SPECIFIED_IN_THE_FUTURE;

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
            .mandatory(CaseData::getManageOrderActivity)
            .page("mangeOrders2")
            .pageLabel("Manage orders and directions")
            .showCondition("manageOrderActivity=\"createOrder\"")
            .mandatory(CaseData::getManageOrderType)
            .done();
        getGatekeepingOrderPage(pageBuilder);
        getFirstDirectionsPage(pageBuilder);
        getSecondDirectionsPage(pageBuilder);
    }

    /**
     * Helper method to support page design and flow to List complete list of Optional Fields.
     *
     * @param pageBuilder - Application PageBuilder for the event pages
     */
    private void getGatekeepingOrderPage(PageBuilder pageBuilder) {
        pageBuilder.page("manageOrders3", this::midEvent)
            .showCondition("manageOrderType=\"caseManagementOrder\"")
            .pageLabel("Create case management (gatekeeping) order")
            .label("LabelPreamble31", "### Preamble")
            .optional(CaseData::getPreambleDetails)
            .label("LabelAllocation32", "### Allocation")
            .optional(CaseData::getAllocationJudge)
            .label("LabelHearings33", "### Hearings")
            .optional(CaseData::getHearingNotices)
            .label("LabelModeOfHearing34", "### Mode of hearing")
            .optional(CaseData::getModeOfHearing)
            .label("LabelLA35", "### Local authority")
            .optional(CaseData::getSelectedLocalAuthority)
            .label("LabelLA36", "### Cafcass")
            .optional(CaseData::getCafcass)
            .label("LabelAttendance37", "### Attendance")
            .optional(CaseData::getAttendance)
            .label("LabelLeaveToOppose38", "### Leave to oppose")
            .optional(CaseData::getLeaveToOppose)
            .label("LabelAdditionalPara39", "### Additional paragraphs")
            .label("LabelAdditionalParaValue30", "You can add any additional directions or paragraphs on a later screen.")
            .label("LabelCostOrders301", "### Cost orders")
            .optional(CaseData::getCostOrders)
            .done();
    }

    /**
     * Helper method to support page design and flow to Display respective options for Part1 fields selection.
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
                   "allocationJudge=\"*\"")
            .label("LabelAllocationValue422", "The case is allocated to [Name of the judge].",
                   "allocationJudge=\"allocatePreviousProceedingsJudge\"")
            .mandatory(CaseData::getAllocatedJudge, "allocationJudge=\"allocatePreviousProceedingsJudge\"")
            .label("LabelAllocationValue423", "The proceedings are reallocated to [Name of Judge].",
                   "allocationJudge=\"reallocateJudge\"")
            .label("LabelNameOfJudge424", "### Name of judge",
                   "allocationJudge=\"reallocateJudge\"")
            .mandatory(CaseData::getNameOfJudge, "allocationJudge=\"reallocateJudge\"")
            //            HEARINGS - FIRST HEARING
            .label("LabelHearings431", "### Hearings",
                   "hearingNoticesCONTAINS\"listForFirstHearing\" OR hearingNoticesCONTAINS\"listForFurtherHearings\" "
                       + "OR hearingNoticesCONTAINS\"hearingDateToBeSpecifiedInTheFuture\" OR modeOfHearingCONTAINS\"setModeOfHearing\"")
            .label("LabelHearings432", "The application is listed for a first hearing on [date] "
                       + "at [time] (with a time estimate of [length]) at [court name].",
                   "hearingNoticesCONTAINS\"listForFirstHearing\"")
            .label("LabelHearings433", "### Date and time",
                   "hearingNoticesCONTAINS\"listForFirstHearing\"")
            .mandatory(CaseData::getDateAndTimeFirstHearing, "hearingNoticesCONTAINS\"listForFirstHearing\"")
            .label("LabelHearings434", "### Length of hearing",
                   "hearingNoticesCONTAINS\"listForFirstHearing\"")
            .mandatory(CaseData::getLengthOfHearingFirstHearing, "hearingNoticesCONTAINS\"listForFirstHearing\"")
            .label("LabelNameOfCourt435", "### Name of court",
                   "hearingNoticesCONTAINS\"listForFirstHearing\"")
            .mandatory(CaseData::getNameOfCourtFirstHearing,
                       "hearingNoticesCONTAINS\"listForFirstHearing\"")
            //            HEARINGS - FURTHER HEARING
            .label("LabelHearings441", "The application is listed for a [listing type] on [date] at "
                       + "[time] with a time estimate of [length] at [court name].",
                   "hearingNoticesCONTAINS\"listForFurtherHearings\"")
            .label("LabelNameOfCourt442", "### Listing type",
                   "hearingNoticesCONTAINS\"listForFurtherHearings\"")
            .mandatory(CaseData::getListingTypeFurtherHearing, "hearingNoticesCONTAINS\"listForFurtherHearings\"")
            .label("LabelHearings443", "### Date and time",
                   "hearingNoticesCONTAINS\"listForFurtherHearings\"")
            .mandatory(CaseData::getDateAndTimeFurtherHearing, "hearingNoticesCONTAINS\"listForFurtherHearings\"")
            .label("LabelHearings444", "### Length of hearing",
                   "hearingNoticesCONTAINS\"listForFurtherHearings\"")
            .mandatory(CaseData::getLengthOfHearingFurtherHearing, "hearingNoticesCONTAINS\"listForFurtherHearings\"")
            .label("LabelNameOfCourt445", "### Name of court",
                   "hearingNoticesCONTAINS\"listForFurtherHearings\"")
            .mandatory(CaseData::getNameOfCourtFurtherHearing,
                       "hearingNoticesCONTAINS\"listForFurtherHearings\"")
            //            HEARINGS - HEARING DATE IN FUTURE
            .label("LabelHearings451", "The application is listed for [listing type] on a date and time to be fixed by the court.",
                   "hearingNoticesCONTAINS\"hearingDateToBeSpecifiedInTheFuture\"")
            .label("LabelNameOfCourt452", "### Listing type",
                   "hearingNoticesCONTAINS\"hearingDateToBeSpecifiedInTheFuture\"")
            .mandatory(CaseData::getListingTypeHearingInFutureDate, "hearingNoticesCONTAINS\"hearingDateToBeSpecifiedInTheFuture\"")
            //            HEARINGS - MODE OF HEARINGS
            .label("LabelNameOfCourt461", "### Mode of hearing",
                   "modeOfHearingCONTAINS\"setModeOfHearing\"")
            .mandatory(CaseData::getModeOfHearings, "modeOfHearingCONTAINS\"setModeOfHearing\"")
            //            LOCAL AUTHORITY
            .label("LabelLocalAuthority471", "### Local authority",
                   "selectedLocalAuthorityCONTAINS\"fileAdoptionAgencyReport\"")
            .label("LabelLocalAuthority472", "The Local Authority shall:<br>"
                       + "<p>**1.** By HH:MM on DD:MM:YY file the Annex A report; and<br>"
                       + "**2.** By HH:MM on DD:MM:YY (date in 14 days from the date above) detailing the following:</p>"
                       + "&emsp;**a.** The date the Local Authority most recently ascertained the views of the "
                       + "birth parents in relation to this application;"
                       + "<br>&emsp;**b.** The steps taken to confirm the current accuracy of the addresses provided? "
                       + "Whether the addresses provided for the birth parents upon issue of the application are accurate?"
                       + "<br>&emsp;**c.** Whether the parents are aware of the date of the hearing in section 3 above?",
                   "selectedLocalAuthorityCONTAINS\"fileAdoptionAgencyReport\"")
            .label("LabelLocalAuthority473", "### Date and time for option 1",
                   "selectedLocalAuthorityCONTAINS\"fileAdoptionAgencyReport\"")
            .mandatory(CaseData::getDateAndTimeForOption1, "selectedLocalAuthorityCONTAINS\"fileAdoptionAgencyReport\"")
            .label("LabelLocalAuthority474", "### Date and time for option 2",
                   "selectedLocalAuthorityCONTAINS\"fileAdoptionAgencyReport\"")
            .mandatory(CaseData::getTimeForOption2, "selectedLocalAuthorityCONTAINS\"fileAdoptionAgencyReport\"")
            //            CAFCASS REPORTING OFFICER
            .label("LabelCafcass481", "### Cafcass",
                   "cafcassCONTAINS\"reportingOfficer\" OR cafcassCONTAINS\"childrensGuardian\"")
            .label("LabelCafcass482", "<p>[Cafcass/Cafcass Cymru] are directed to appoint a reporting officer. "
                       + "The reporting officer is requested to file a brief report together with the relevant consent forms "
                       + "witnessing the consent of the birth parents by [time] on [date].</p>"
                       + "<p>The Adoption Agency (Annex A) report may be disclosed to the reporting officer.</p>",
                   "cafcassCONTAINS\"reportingOfficer\"")
            .label("LabelCafcass483", "### Select reporting officer",
                   "cafcassCONTAINS\"reportingOfficer\"")
            .mandatory(CaseData::getReportingOfficer, "cafcassCONTAINS\"reportingOfficer\"")
            .label("LabelCafcass484", "### Date and time",
                   "cafcassCONTAINS\"reportingOfficer\"")
            .mandatory(CaseData::getDateAndTimeRO, "cafcassCONTAINS\"reportingOfficer\"")
            //            CAFCASS CHILDRENS GUARDIAN
            .label("LabelCafcass485", "<p>The child is made a respondent to the application and a children's guardian "
                       + "is appointed and [Cafcass/Cafcass Cymru] shall use their best endeavours to appoint the same guardian "
                       + "appointed within the previous proceedings case number [case number].</p>",
                   "cafcassCONTAINS\"childrensGuardian\"")
            .label("LabelCafcass486", "### Select reporting officer",
                   "cafcassCONTAINS\"childrensGuardian\"")
            .mandatory(CaseData::getChildrensGuardian, "cafcassCONTAINS\"childrensGuardian\"")
            .label("LabelCafcass487", "### Case number",
                   "cafcassCONTAINS\"childrensGuardian\"")
            .mandatory(CaseData::getCaseNumberCG, "cafcassCONTAINS\"childrensGuardian\"")
            .done();
    }

    /**
     * Helper method to support page design and flow to Display respective options for Part1 fields selection.
     * Attendance, Leave To Oppose, Additional Paragraphs and Cost Orders
     * @param pageBuilder - Application PageBuilder for the event pages
     */
    private void getSecondDirectionsPage(PageBuilder pageBuilder) {
        pageBuilder.page("manageOrders5")
            //          ATTENDANCE DETAILS
            .pageLabel("Case management order second directions")
            .label("LabelAttendance51", "### Attendance",
                   "attendanceCONTAINS\"applicantsAttendance\" OR attendanceCONTAINS\"childAttendance\" "
                       + "OR attendanceCONTAINS\"localAuthorityAttendance\" OR attendanceCONTAINS\"birthParentsAttendance\"")
            .label("LabelAttendanceValue52", "Choose the direction for the attendees",
                   "attendanceCONTAINS\"applicantsAttendance\" OR attendanceCONTAINS\"childAttendance\" "
                       + "OR attendanceCONTAINS\"localAuthorityAttendance\" OR attendanceCONTAINS\"birthParentsAttendance\"")
            //          APPLICANT ATTENDANCE
            .label("LabelApplicantAttendance511", "### Applicant attendance",
                   "attendanceCONTAINS\"applicantsAttendance\"")
            .mandatory(CaseData::getApplicantAttendance, "attendanceCONTAINS\"applicantsAttendance\"")
            //          CHILD ATTENDANCE
            .label("LabelChildAttendance521", "### Child attendance",
                   "attendanceCONTAINS\"childAttendance\"")
            .mandatory(CaseData::getChildAttendance, "attendanceCONTAINS\"childAttendance\"")
            //          LA ATTENDANCE
            .label("LabelChildAttendance531", "### Local authority attendance",
                   "attendanceCONTAINS\"localAuthorityAttendance\"")
            .mandatory(CaseData::getLaAttendance, "attendanceCONTAINS\"localAuthorityAttendance\"")
            //          BIRTH PARENT ATTENDANCE
            .label("LabelChildAttendance541", "### Birth parent attendance",
                   "attendanceCONTAINS\"birthParentsAttendance\"")
            .mandatory(CaseData::getBirthParentAttendance, "attendanceCONTAINS\"birthParentsAttendance\"")
            .done();
    }

    /**
    * Event method to validate the right selection options are selected by the User as per the Requirements.
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
        Set<HearingNotices> selectedHearingNotices = caseData.getHearingNotices();

        if (isNotEmpty(selectedHearingNotices)
            && selectedHearingNotices.contains(HEARING_DATE_TO_BE_SPECIFIED_IN_THE_FUTURE)
            && (isNotEmpty(caseData.getModeOfHearing()) || selectedHearingNotices.size() > 1)) {
            errors.add(ERROR_CHECK_HEARINGS_SELECTION);
        }
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .errors(errors)
            .build();
    }
}
