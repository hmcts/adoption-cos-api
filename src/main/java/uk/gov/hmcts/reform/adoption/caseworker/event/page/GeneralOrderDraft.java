package uk.gov.hmcts.reform.adoption.caseworker.event.page;

import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.divorcecase.model.CaseData;
import uk.gov.hmcts.reform.adoption.divorcecase.model.GeneralOrder;

public class GeneralOrderDraft implements CcdPageConfiguration {

    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder.page("generalOrderDraft")
            .complex(CaseData::getGeneralOrder)
                .readonly(GeneralOrder::getGeneralOrderDraft)
                .done();
    }
}
