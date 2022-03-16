package uk.gov.hmcts.reform.adoption.document;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.adoption.document.content.DocmosisTemplateProvider;
import uk.gov.hmcts.reform.adoption.document.model.DocAssemblyRequest;
import uk.gov.hmcts.reform.adoption.document.model.DocAssemblyResponse;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;

import java.util.Map;

import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.PDF;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.PDF_EXT;

@Service
@Slf4j
public class DocAssemblyService {

    @Autowired
    private AuthTokenGenerator authTokenGenerator;

    @Autowired
    private DocAssemblyClient docAssemblyClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DocmosisTemplateProvider docmosisTemplateProvider;

    public DocumentInfo renderDocument(final Map<String, Object> templateContent,
                                       final Long caseId,
                                       final String authorisation,
                                       final String templateId,
                                       final LanguagePreference languagePreference,
                                       final String filename) {

        final String templateName = docmosisTemplateProvider.templateNameFor(templateId, languagePreference);

        final DocAssemblyRequest docAssemblyRequest =
            DocAssemblyRequest
                .builder()
                .templateId(templateName)
                .outputType(PDF)
                .formPayload(objectMapper.valueToTree(templateContent))
                .build();

        log.info("Sending document request for template : {} case id: {}", templateName, caseId);

        final DocAssemblyResponse docAssemblyResponse = docAssemblyClient.generateAndStoreDraftApplication(
            authorisation,
            authTokenGenerator.generate(),
            docAssemblyRequest
        );

        log.info("Document successfully generated and stored for case Id {} with document location {}",
            caseId,
            docAssemblyResponse.getRenditionOutputLocation()
        );

        return new DocumentInfo(
            docAssemblyResponse.getRenditionOutputLocation(),
            filename + PDF_EXT,
            docAssemblyResponse.getBinaryFilePath(),
            docAssemblyResponse.getFileId()
        );
    }
}
