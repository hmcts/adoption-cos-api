package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

/**
 * Contains method to add Page Configuration for ExUI.
 * Display the Manage orders Details screen with all required fields.
 */
public class ManageOrders implements CcdPageConfiguration {
    @Override
    public void addTo(PageBuilder pageBuilder) {

        pageBuilder.page("manageOrders").done();

    }
}
