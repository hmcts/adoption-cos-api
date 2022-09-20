package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseworkerScannedDocuments.SCANNED_DOCUMENT;

/**
 * This class is used to display the Manage Documents screen having all mandatory fields.
 */
public class ScannedDocuments implements CcdPageConfiguration {
    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder.page("uploadDocumentPage1")
            .pageLabel(SCANNED_DOCUMENT)
            .mandatory(CaseData::getApplicant1DocumentsUploaded)
            /*.mandatory(AdoptionUploadDocument::getDocumentLink)
            .mandatory(AdoptionUploadDocument::getDocumentComment)
            .mandatory(AdoptionUploadDocument::getDocumentCategory)*/
            .done();

        /*pageBuilder.page("uploadDocumentPage2")
            .pageLabel("Who submitted the document?")
            .label("uploadDocumentPage2Label","Who submitted the document?")
            .mandatory(CaseData::getName)
            .mandatory(CaseData::getRole)
            .done();*/

    }
}
