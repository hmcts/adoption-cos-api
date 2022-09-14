package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.OtherParty;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.document.DocumentSubmitter;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionDocument;

import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseworkerUploadDocument.MANAGE_DOCUMENT;

/**
 * This class is used to display the Manage Documents screen having all mandatory fields.
 */
public class ManageDocuments implements CcdPageConfiguration {
    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder.page("uploadDocumentPage1")
            .pageLabel(MANAGE_DOCUMENT)
            .complex(CaseData::getAdoptionDocument)
            .mandatory(AdoptionDocument::getDocumentLink)
            .mandatory(AdoptionDocument::getDocumentComment)
            .mandatory(AdoptionDocument::getDocumentCategory)
            .done();

        pageBuilder.page("uploadDocumentPage2")
            .pageLabel("Who submitted the document?")
            .complex(CaseData::getAdoptionDocument)
            .complex(AdoptionDocument::getDocumentSubmitter)
            .mandatory(DocumentSubmitter::getDocumentSubmittedBy)
            .done();
        pageBuilder.page("uploadDocumentPage2")
            .complex(CaseData::getAdoptionDocument)
            .complex(AdoptionDocument::getDocumentSubmitter)
            .complex(DocumentSubmitter::getOtherParty)
            .mandatory(OtherParty::getOtherPartyName, "documentSubmittedBy=\"otherParty\"")
            .mandatory(OtherParty::getOtherPartyRole, "documentSubmittedBy=\"otherParty\"")
            .done();

    }
}
