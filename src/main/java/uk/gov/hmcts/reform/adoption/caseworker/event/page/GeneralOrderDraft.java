package uk.gov.hmcts.reform.adoption.caseworker.event.page;

import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

public class GeneralOrderDraft implements CcdPageConfiguration {

    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder.page("generalOrderDraft");
        //.complex(CaseData::getGeneralOrder)
        //.readonly(GeneralOrder::getGeneralOrderDraft)
        // .done();//TODO
    }
}
