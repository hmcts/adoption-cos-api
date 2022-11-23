package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

public class CheckAndSendOrders implements CcdPageConfiguration {
    @Override
    public void addTo(PageBuilder pageBuilder) {

        pageBuilder.page("checkAndSendOrders1")
            .pageLabel("Check And Send Orders")
            .label("checkAndSendOrdersLabel1","## Orders for review")
            .mandatory(CaseData::getCheckAndSendOrderDropdownList)
            .done()
            .build();
    }
}
