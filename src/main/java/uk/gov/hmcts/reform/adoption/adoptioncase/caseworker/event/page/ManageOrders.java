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
            .done();
    }

    public AboutToStartOrSubmitResponse<CaseData, State> midEvent(
        CaseDetails<CaseData, State> details,
        CaseDetails<CaseData, State> detailsBefore
    ) {
        CaseData caseData = details.getData();
        final List<String> errors = new ArrayList<>();

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
