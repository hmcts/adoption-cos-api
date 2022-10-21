package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ManageHearingDetails;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

public class ManageHearings implements CcdPageConfiguration {

    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder.page("manageOrders1")
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
            .mandatory(ManageHearingDetails::getIsInterpreterNeeded)
            .mandatory(ManageHearingDetails::getMethodOfHearing)
            .mandatory(ManageHearingDetails::getAccessibilityRequirements)
            .mandatory(ManageHearingDetails::getHearingDirections)
            .done()
            .page("manageOrders3")
            .showCondition("manageHearingOptions=\"addNewHearing\"")
            .mandatory(CaseData::getRecipientsInTheCase)
            .done();
    }
}
