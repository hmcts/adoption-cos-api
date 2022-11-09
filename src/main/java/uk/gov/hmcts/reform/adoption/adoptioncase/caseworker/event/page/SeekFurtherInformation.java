package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import lombok.extern.slf4j.Slf4j;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

@Slf4j
public class SeekFurtherInformation implements CcdPageConfiguration {
    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder.page("pageSeekFurtherInformation")
            .label("seekFurtherInformationLabel","## Who do you need to contact?")
            .mandatory(CaseData::getSeekFurtherInformationList)
            .done();
    }

}
