package uk.gov.hmcts.reform.adoption.document;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionDocument;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference;
import uk.gov.hmcts.reform.adoption.idam.IdamService;

import java.util.Map;

import static uk.gov.hmcts.reform.adoption.document.DocumentUtil.adoptionDocumentFrom;
import static uk.gov.hmcts.reform.adoption.document.DocumentUtil.documentFrom;

@Service
@Slf4j
public class CaseDataDocumentService {

    @Autowired
    private DocAssemblyService docAssemblyService;

    @Autowired
    private DocumentIdProvider documentIdProvider;

    @Autowired
    private IdamService idamService;

    public void renderDocumentAndUpdateCaseData(final CaseData caseData,
                                                final DocumentType documentType,
                                                final Map<String, Object> templateContent,
                                                final Long caseId,
                                                final String templateId,
                                                final LanguagePreference languagePreference,
                                                final String filename) {

        log.info("Rendering document request for templateId : {} case id: {}", templateId, caseId);

        final String authorisation = idamService.retrieveSystemUpdateUserDetails().getAuthToken();

        final var documentInfo = docAssemblyService.renderDocument(
            templateContent,
            caseId,
            authorisation,
            templateId,
            languagePreference,
            filename
        );

        log.info("Adding document to case data for templateId : {} case id: {}", templateId, caseId);


        log.info("Document url: {}", documentInfo);

        ListValue<AdoptionDocument> adoptionDocument = ListValue.<AdoptionDocument>builder()
            .id(documentIdProvider.documentId())
            .value(adoptionDocumentFrom(documentInfo, documentType))
            .build();
        log.info("AdoptionDocument: {}", adoptionDocument);

        log.info("Calling method for adding documents to casedata for case ref: {}", caseId);
        log.info("Current size of available documents in casedata is: {}", caseData.getDocumentsGenerated().size());
        caseData.addToDocumentsGenerated(adoptionDocument);
    }

    public Document renderDocument(final Map<String, Object> templateContent,
                                   final Long caseId,
                                   final String templateId,
                                   final LanguagePreference languagePreference,
                                   final String filename) {

        log.info("Rendering document request for templateId : {} ", templateId);

        final String authorisation = idamService.retrieveSystemUpdateUserDetails().getAuthToken();

        final var documentInfo = docAssemblyService.renderDocument(
            templateContent,
            caseId,
            authorisation,
            templateId,
            languagePreference,
            filename
        );

        return documentFrom(documentInfo);
    }
}
