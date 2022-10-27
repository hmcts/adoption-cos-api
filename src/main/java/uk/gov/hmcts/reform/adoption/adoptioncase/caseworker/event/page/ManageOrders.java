package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ManageOrders implements CcdPageConfiguration {

    public static final String ERROR_CHECK_HEARINGS_SELECTION = "Please check your selection for Hearings";

    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder.page("manageOrders1")
            .mandatory(CaseData::getManageOrderActivity)
            .page("mangeOrders2")
            .pageLabel("Manage orders and directions")
            .showCondition("manageOrderActivity=\"createOrder\"")
            .mandatory(CaseData::getManageOrderType)
            .page("manageOrders3", this::midEvent)
            .done();

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
            .label("LabelAttendance36", "### Attendance")
            .optional(CaseData::getAttendance)
            .label("LabelLeaveToOppose37", "### Leave to oppose")
            .optional(CaseData::getLeaveToOppose)
            .label("LabelAdditionalPara38", "### Additional paragraphs")
            .label("LabelAdditionalParaValue39", "You can add any additional directions or paragraphs on a later screen.")
            .label("LabelCostOrders30", "### Cost orders")
            .optional(CaseData::getCostOrders)
            .page("manageOrders4")
            .showCondition("preambleDetails=\"*\" OR allocationJudge=\"allocatePreviousProceedingsJudge\" "
                               + "OR allocationJudge=\"reallocateJudge\"")
            .pageLabel("Case management order first directions")
            .label("LabelAdditionalParaValue4-Heading", "Review the paragraphs to be inserted into the order. "
                + "These are based on the options you chose on the previous page. "
                + "If you would like to change an option, go back to the previous page.")
            .label("LabelPreamble4-Heading", "### Preamble", "preambleDetails=\"*\"")
            .label("LabelPreambleValue4-Heading", "${preambleDetails}", "preambleDetails=\"*\"")
            .label("LabelAllocation4-Heading", "### Allocation",
                   "allocationJudge=\"*\"")
            .label("LabelAllocationValue14-Heading", "The case is allocated to [Name of the judge].",
                   "allocationJudge=\"allocatePreviousProceedingsJudge\"")
            .mandatory(CaseData::getAllocatedJudge, "allocationJudge=\"allocatePreviousProceedingsJudge\"")
            .label("LabelAllocationValue24-Heading", "The proceedings are reallocated to [Name of Judge].",
                   "allocationJudge=\"reallocateJudge\"")
            .label("LabelNameOfJudge-Heading", "### Name of judge",
                   "allocationJudge=\"reallocateJudge\"")
            .mandatory(CaseData::getNameOfJudge, "allocationJudge=\"reallocateJudge\"")
            .done();

        pageBuilder.page("manageOrders4")
            .showCondition("preambleDetails=\"*\" OR allocationJudge=\"*\" "
                + "OR hearingNoticesCONTAINS\"listForFirstHearing\" OR hearingNoticesCONTAINS\"listForFurtherHearings\" "
                + "OR hearingNoticesCONTAINS\"hearingDateToBeSpecifiedInTheFuture\" OR modeOfHearingCONTAINS\"setModeOfHearing\" "
                + "OR selectedLocalAuthorityCONTAINS\"fileAdoptionAgencyReport\"")
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
            .label("LabelAllocationValue422", "The case is allocated to His Honour Judge ${allocatedJudge}",
                   "allocationJudge=\"allocatePreviousProceedingsJudge\"")
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
