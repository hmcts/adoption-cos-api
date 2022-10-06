package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

public class TransferCourt implements CcdPageConfiguration {

    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder.page("pageTransferCourt")
            .pageLabel("Which court are you transferring the case to ?")
            //.label("headingLabel","## Which court are you transferring the case to ?")
            .mandatory(CaseData::getTransferCourt, null,"Enter court name")
            .done();
    }
}
