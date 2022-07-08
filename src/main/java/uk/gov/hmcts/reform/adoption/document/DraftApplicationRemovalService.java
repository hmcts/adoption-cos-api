package uk.gov.hmcts.reform.adoption.document;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionDocument;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.idam.client.models.User;

import java.util.List;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;
import static uk.gov.hmcts.reform.adoption.document.DocumentType.APPLICATION;

@Service
@Slf4j
public class DraftApplicationRemovalService {


    @Autowired
    private CaseDocumentClient caseDocumentClient;

    @Autowired
    private AuthTokenGenerator authTokenGenerator;

    @Autowired
    private IdamService idamService;

    public List<ListValue<AdoptionDocument>> removeDraftApplicationDocument(
        final List<ListValue<AdoptionDocument>> generatedDocuments,
        final Long caseId
    ) {

        if (isEmpty(generatedDocuments)) {
            log.info("Generated documents list is empty for case id {} ", caseId);
            return emptyList();
        }

        final User systemUser = idamService.retrieveSystemUpdateUserDetails();

        final List<ListValue<AdoptionDocument>> generatedDocumentsExcludingApplication = generatedDocuments
            .stream()
            .map(document ->
                deleteDocumentFromDocumentStore(
                    document,
                    systemUser,
                    String.valueOf(caseId)
                )
            )
            .filter(document -> !isApplicationDocument(document))
            .collect(toList());


        log.info("Successfully removed application document from case data generated document list for case id {} ", caseId);

        return generatedDocumentsExcludingApplication;
    }

    private ListValue<AdoptionDocument> deleteDocumentFromDocumentStore(
        final ListValue<AdoptionDocument> document,
        final User user,
        final String caseId
    ) {
        if (isApplicationDocument(document)) {

            caseDocumentClient.deleteDocument(user.getAuthToken(),
                                                authTokenGenerator.generate(),
                                                UUID.fromString(FilenameUtils.getName(document.getValue().getDocumentLink().getUrl())),
                                                true);

            log.info("Successfully deleted application document from document management for case id {} ", caseId);
        } else {
            log.info("No draft application document found for case id {} ", caseId);
        }
        return document;
    }

    private boolean isApplicationDocument(ListValue<AdoptionDocument> document) {
        return document.getValue().getDocumentType().equals(APPLICATION);
    }
}
