package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ManageHearingDetails;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

/**
 * Contains method to add Page Configuration for ExUI.
 * Display the Manage hearings Details screen with all required fields.
 */
@Slf4j
@Component
public class ManageHearings implements CcdPageConfiguration {

    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder.page("manageHearing1")
            .mandatory(CaseData::getManageHearingOptions)
            .done();

        pageBuilder.page("manageHearing2")
            .showCondition("manageHearingOptions=\"vacateHearing\"")
            .label("vacateHearingLabel1","## Vacate a hearing", "manageHearingOptions=\"vacateHearing\"")
            .mandatory(CaseData::getHearingListThatCanBeVacated,"manageHearingOptions=\"vacateHearing\"")
            .done();

        pageBuilder.page("manageHearing2.1")
            .showCondition("manageHearingOptions= \"adjournHearing\"")
            .label("vacateHearingLabel2","## Adjourn a hearing", "manageHearingOptions=\"adjournHearing\"")
            .mandatory(CaseData::getHearingListThatCanBeAdjourned)
            .done();

        pageBuilder.page("manageHearing3")
            .showCondition("manageHearingOptions=\"vacateHearing\"")
            .mandatory(CaseData::getReasonForVacatingHearing)
            .done();

        pageBuilder.page("manageHearing31")
            .showCondition("manageHearingOptions=\"adjournHearing\"")
            .mandatory(CaseData::getReasonForAdjournHearing)
            .done();

        pageBuilder.page("manageHearing4")
            .showCondition("manageHearingOptions=\"vacateHearing\" OR manageHearingOptions=\"adjournHearing\"")
            .label("relistingLabel1","## Relisting")
            .mandatory(CaseData::getIsTheHearingNeedsRelisting)
            .done();

        pageBuilder.page("manageHearing5")
            .showCondition("manageHearingOptions=\"addNewHearing\" OR isTheHearingNeedsRelisting=\"Yes\"")
            .label("addNewHearing2", "## Add new hearing")
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
            .done();
    }
}
