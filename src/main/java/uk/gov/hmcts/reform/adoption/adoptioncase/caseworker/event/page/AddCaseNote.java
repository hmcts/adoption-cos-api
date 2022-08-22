package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseNote;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;


public class AddCaseNote implements CcdPageConfiguration {
    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder.page("pageNote")
            .complex(CaseData::getCaseNote)
            .mandatoryWithLabel(
                CaseNote::getNote,
                "Note")

            .mandatoryWithLabel(
                CaseNote::getSubject,
                "Subject")
            .done();
    }
}
