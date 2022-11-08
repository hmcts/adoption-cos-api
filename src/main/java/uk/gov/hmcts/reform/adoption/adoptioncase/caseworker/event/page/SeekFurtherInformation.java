package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

public class SeekFurtherInformation implements CcdPageConfiguration {
    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder.page("pageSeekFurtherInformation")
            .pageLabel("Who do you need to contact?")
            .label("seekFurtherInformationLabel","Who do you need to contact?")
            .mandatory(CaseData::getName)
            .mandatory(CaseData::getRole)
            .done();
    }
}
