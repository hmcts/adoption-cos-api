package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionDocument;

import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseworkerUploadDocument.MANAGE_DOCUMENT;

/**
 * This class is used to display the Manage Documents screen having all mandatory fields.
 */
public class ManageDocuments implements CcdPageConfiguration {
    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder.page("uploadDocument")
            .pageLabel(MANAGE_DOCUMENT)
            .complex(CaseData::getAdoptionDocument)
            .mandatory(AdoptionDocument::getDocumentLink)
            .mandatory(AdoptionDocument::getDocumentComment)
            .mandatory(AdoptionDocument::getDocumentCategory)
            .done();
        pageBuilder.page("documentSubmitter")
            .pageLabel("Who submitted the document?")
            .complex(CaseData::getAdoptionDocument)
            .mandatory(AdoptionDocument::getDocumentSubmitter)
            .done();
        /*.complex(AdoptionDocument::getOtherParty)
            .mandatory(OtherParty::getOtherPartyRole)
            .mandatory(OtherParty::getOtherPartyName)
            .done();*/
    }
}
