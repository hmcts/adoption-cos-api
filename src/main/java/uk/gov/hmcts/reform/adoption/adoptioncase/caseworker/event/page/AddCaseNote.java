package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Children;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

/**
 * Specify the Order, Mandatory/Optional properties of fields and build the Notes Screen/Page.
 */
public class AddCaseNote implements CcdPageConfiguration {
    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder.page("pageNote")
            .mandatory(CaseData::getCaseNote)
            .done();
        pageBuilder.page("testPage")
            .pageLabel("Test Page")
            .complex(CaseData::getChildren)
            .optional(Children::getFirstName)
            .optional(Children::getLastName)
            .done();
    }
}
