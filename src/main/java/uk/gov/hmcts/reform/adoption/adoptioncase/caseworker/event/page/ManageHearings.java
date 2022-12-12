package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
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
            .page("manageHearing4")
            .showCondition("manageHearingOptions=\"vacateHearing\" OR manageHearingOptions=\"adjournHearing\"")
            .label("relistingLabel1","## Relisting")
            .mandatory(CaseData::getIsTheHearingNeedsRelisting)
            .done()
            .build();
    }
}
