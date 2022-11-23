package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

public class CheckAndSendOrders implements CcdPageConfiguration {
    @Override
    public void addTo(PageBuilder pageBuilder) {

        pageBuilder.page("checkAndSendOrders1")
            .pageLabel("## Orders for review")
            //.label("checkAndSendOrdersLabel1","## Orders for review")
            .label("checkAndSendOrderLabel2","### Select the order you want to review",null,true)
            .mandatory(CaseData::getCheckAndSendOrderDropdownList)
            .done()
            .build();
    }
}
