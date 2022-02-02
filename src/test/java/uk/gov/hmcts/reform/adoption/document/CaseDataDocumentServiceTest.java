package uk.gov.hmcts.reform.adoption.document;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionDocument;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.idam.client.models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.ADOPTION_DRAFT_APPLICATION;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.ADOPTION_DRAFT_APPLICATION_DOCUMENT_NAME;
import static uk.gov.hmcts.reform.adoption.document.DocumentType.EMAIL;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference.ENGLISH;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_AUTHORIZATION_TOKEN;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_CASE_ID;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.SAMPLE_DOCUMENT;

@ExtendWith(MockitoExtension.class)
class CaseDataDocumentServiceTest {

    private static final String DOC_URL = "http://localhost:4200/assets/59a54ccc-979f-11eb-a8b3-0242ac130003";
    private static final String DOC_BINARY_URL = "http://localhost:4200/assets/59a54ccc-979f-11eb-a8b3-0242ac130003/binary";
    private static final String PDF_FILENAME = "draft-adoption-application-1616591401473378.pdf";
    private static final String GENERAL_ORDER_PDF_FILENAME = "draft-adoption-application-1616591401473378.pdf";
    private static final String URL = "url";
    private static final String FILENAME = "filename";
    private static final String BINARY_URL = "binaryUrl";

    @Mock
    private DocAssemblyService docAssemblyService;

    @Mock
    private DocumentIdProvider documentIdProvider;

    @Mock
    private IdamService idamService;

    @InjectMocks
    private CaseDataDocumentService caseDataDocumentService;

    @Test
    void shouldAddRenderedDocumentToCaseData() {

        final var documentId = "123456";
        final CaseData caseData = CaseData.builder().build();
        final Map<String, Object> templateContent = new HashMap<>();
        final User systemUser = mock(User.class);
        final String filename = ADOPTION_DRAFT_APPLICATION_DOCUMENT_NAME + TEST_CASE_ID;

        when(idamService.retrieveSystemUpdateUserDetails()).thenReturn(systemUser);
        when(systemUser.getAuthToken()).thenReturn(TEST_AUTHORIZATION_TOKEN);
        when(docAssemblyService
            .renderDocument(
                templateContent,
                TEST_CASE_ID,
                TEST_AUTHORIZATION_TOKEN,
                ADOPTION_DRAFT_APPLICATION,
                ENGLISH,
                filename))
            .thenReturn(new DocumentInfo(DOC_URL, PDF_FILENAME, DOC_BINARY_URL));

        when(documentIdProvider.documentId()).thenReturn(documentId);

        caseDataDocumentService.renderDocumentAndUpdateCaseData(
            caseData,
            EMAIL,
            templateContent,
            TEST_CASE_ID,
            ADOPTION_DRAFT_APPLICATION,
            ENGLISH,
            filename);

        final List<ListValue<AdoptionDocument>> documentsGenerated = caseData.getDocumentsGenerated();

        assertThat(documentsGenerated).hasSize(1);

        final ListValue<AdoptionDocument> documentListValue = documentsGenerated.get(0);
        final var adoptionDocument = documentListValue.getValue();

        assertThat(documentListValue.getId()).isEqualTo(documentId);
        assertThat(adoptionDocument.getDocumentType()).isEqualTo(EMAIL);
        assertThat(adoptionDocument
            .getDocumentLink())
            .extracting(URL, FILENAME, BINARY_URL)
            .contains(
                DOC_URL,
                PDF_FILENAME,
                DOC_BINARY_URL);
    }

    @Test
    void shouldGenerateAndReturnGeneralOrderDocument() {

        final Map<String, Object> templateContent = new HashMap<>();
        final User systemUser = mock(User.class);
        final String filename = GENERAL_ORDER_PDF_FILENAME + TEST_CASE_ID;

        when(idamService.retrieveSystemUpdateUserDetails()).thenReturn(systemUser);
        when(systemUser.getAuthToken()).thenReturn(TEST_AUTHORIZATION_TOKEN);
        when(docAssemblyService
            .renderDocument(
                templateContent,
                TEST_CASE_ID,
                TEST_AUTHORIZATION_TOKEN,
                SAMPLE_DOCUMENT,
                ENGLISH,
                filename))
            .thenReturn(new DocumentInfo(DOC_URL, PDF_FILENAME, DOC_BINARY_URL));

        final Document result = caseDataDocumentService.renderDocument(
            templateContent,
            TEST_CASE_ID,
            SAMPLE_DOCUMENT,
            ENGLISH,
            filename);

        assertThat(result.getBinaryUrl()).isEqualTo(DOC_BINARY_URL);
        assertThat(result.getUrl()).isEqualTo(DOC_URL);
        assertThat(result.getFilename()).isEqualTo(GENERAL_ORDER_PDF_FILENAME);
    }
}
