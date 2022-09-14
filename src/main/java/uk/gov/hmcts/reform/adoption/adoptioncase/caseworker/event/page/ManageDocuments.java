package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionUploadDocument;

import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseworkerUploadDocument.MANAGE_DOCUMENT;

/**
 * This class is used to display the Manage Documents screen having all mandatory fields.
 */
public class ManageDocuments implements CcdPageConfiguration {
    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder.page("uploadDocumentPage1")
            .pageLabel(MANAGE_DOCUMENT)
            .complex(CaseData::getAdoptionUploadDocument)
            .mandatory(AdoptionUploadDocument::getDocumentLink)
            .mandatory(AdoptionUploadDocument::getDocumentComment)
            .mandatory(AdoptionUploadDocument::getDocumentCategory)
            .done();

        pageBuilder.page("uploadDocumentPage2")
            .pageLabel("Who submitted the document?")
            .complex(CaseData::getAdoptionUploadDocument)
            .mandatory(AdoptionUploadDocument::getRole)
            .mandatory(AdoptionUploadDocument::getName)
            /*.complex(CaseData::getOtherParty)
            .mandatory(OtherParty::getOtherPartyName, "documentSubmittedBy=\"otherParty\"")
            .mandatory(OtherParty::getOtherPartyRole, "documentSubmittedBy=\"otherParty\"")*/
            .done();

    }
}
