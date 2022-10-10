package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseworkerReviewDocuments.SCANNED_DOCUMENT;

/**
 * This class is used to display the Manage Documents screen having all mandatory fields.
 */
public class ReviewDocuments implements CcdPageConfiguration {
    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder.page("scannedDocumentPage1")
            .pageLabel(SCANNED_DOCUMENT)
            .mandatory(CaseData::getLaDocumentsUploaded)
            .done();

    }
}
