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
            .showCondition("manageOrderType=\"caseManagementOrder\"")
            .pageLabel("Create case management (gatekeeping) order")
            .label("LabelPreamble3-Heading", "### Preamble")
            .optional(CaseData::getPreambleDetails)
            .label("LabelAllocation3-Heading", "### Allocation")
            .optional(CaseData::getAllocationJudge)
            .label("LabelHearing3-Heading", "### Hearings")
            .optional(CaseData::getHearingNotices)
            .label("LabelModeOfHearing3-Heading", "### Mode of hearing")
            .optional(CaseData::getModeOfHearing)
            .label("LabelLA3-Heading", "### Local authority")
            .optional(CaseData::getSelectedLocalAuthority)
            .label("LabelAttendance3-Heading", "### Attendance")
            .optional(CaseData::getAttendance)
            .label("LabelLeaveToOppose3-Heading", "### Leave to oppose")
            .optional(CaseData::getLeaveToOppose)
            .label("LabelAdditionalPara3-Heading", "### Additional paragraphs")
            .label("LabelAdditionalParaValue3-Heading", "You can add any additional directions or paragraphs on a later screen.")
            .label("LabelCostOrders3-Heading", "### Cost orders")
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
