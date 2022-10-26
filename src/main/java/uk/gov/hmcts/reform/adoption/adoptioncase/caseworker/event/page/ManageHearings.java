package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import org.apache.commons.collections4.CollectionUtils;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.DynamicListElement;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ManageHearingDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.BLANK_SPACE;

/**
 * Contains method to add Page Configuration for ExUI.
 * Display the Manage hearings Details screen with all required fields.
 */
public class ManageHearings implements CcdPageConfiguration {

    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder.page("manageOrders1", this::midEvent)
            .mandatory(CaseData::getManageHearingOptions)
            .page("manageOrders2")
            .showCondition("manageHearingOptions=\"addNewHearing\"")
            .label("addNewHearing1", "## Add new hearing")
            .complex(CaseData::getManageHearingDetails)
            .mandatory(ManageHearingDetails::getTypeOfHearing)
            .mandatory(ManageHearingDetails::getHearingDateAndTime)
            .mandatory(ManageHearingDetails::getLengthOfHearing)
            .mandatory(ManageHearingDetails::getJudge)
            .mandatory(ManageHearingDetails::getCourt)
            .optional(ManageHearingDetails::getIsInterpreterNeeded)
            .mandatory(ManageHearingDetails::getMethodOfHearing)
            .optional(ManageHearingDetails::getAccessibilityRequirements)
            .optional(ManageHearingDetails::getHearingDirections)
            .done()
            .page("manageOrders3")
            .showCondition("manageHearingOptions=\"addNewHearing\"")
            .mandatory(CaseData::getRecipientsInTheCase)
            .page("manageOrders4")
            .showCondition("manageHearingOptions=\"vacateHearing\"")
            .label("vacateHearingLabel1","## Vacate a hearing")
            .mandatory(CaseData::getHearingsList)
            .page("manageOrders5")
            .showCondition("manageHearingOptions=\"vacateHearing\"")
            .mandatory(CaseData::getReasonForVacatingHearing)
            .page("manageOrders6")
            .showCondition("manageHearingOptions=\"vacateHearing\"")
            .label("relistingLabel1","## Relisting")
            .mandatory(CaseData::getIsTheHearingNeedsRelisting)
            .done();
    }

    private AboutToStartOrSubmitResponse<CaseData, State> midEvent(CaseDetails<CaseData, State> details,
                                                                   CaseDetails<CaseData, State> detailsBefore) {
        CaseData caseData = details.getData();
        List<DynamicListElement> listElements = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(caseData.getNewHearings())) {
            caseData.getNewHearings().forEach(hearing -> {
                DynamicListElement listElement = DynamicListElement.builder()
                    .label(String.join(BLANK_SPACE, hearing.getValue().getTypeOfHearing(),
                                       "-",
                                       hearing.getValue().getHearingDateAndTime().format(DateTimeFormatter.ofPattern(
                                           "dd MMM yyyy',' hh:mm:ss a")).replace("pm", "PM").replace("am", "PM")
                    )).code(UUID.fromString(hearing.getValue().getHearingId()))
                    .build();
                listElements.add(listElement);
            });

        }
        caseData.setHearingsList(DynamicList.builder().listItems(listElements).value(DynamicListElement.EMPTY).build());
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .build();
    }
}
