package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseNote;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

/**
 * Specify the Order, Mandatory/Optional properties of fields and build the Notes Screen/Page.
 */
public class AddCaseNote implements CcdPageConfiguration {
    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder.page("pageNote")
            .complex(CaseData::getNote)
            .mandatoryWithLabel(
                CaseNote::getSubject, "Subject")
            .mandatoryWithLabel(
                CaseNote::getNote, "Note")
            .done();
    }
}
