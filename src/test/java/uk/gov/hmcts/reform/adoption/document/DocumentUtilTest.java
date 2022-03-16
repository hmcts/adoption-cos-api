package uk.gov.hmcts.reform.adoption.document;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionDocument;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.adoption.document.DocumentType.APPLICATION;
import static uk.gov.hmcts.reform.adoption.document.DocumentUtil.adoptionDocumentFrom;
import static uk.gov.hmcts.reform.adoption.document.DocumentUtil.documentFrom;
import static uk.gov.hmcts.reform.adoption.document.DocumentUtil.formatDocumentName;

@ExtendWith(MockitoExtension.class)
class DocumentUtilTest {

    private static final String DOC_URL = "http://localhost:4200/assets/59a54ccc-979f-11eb-a8b3-0242ac130003";
    private static final String DOC_BINARY_URL = "http://localhost:4200/assets/59a54ccc-979f-11eb-a8b3-0242ac130003/binary";
    private static final String PDF_FILENAME = "draft-adoption-application-1616591401473378.pdf";
    private static final String URL = "url";
    private static final String FILENAME = "filename";
    private static final String DOCUMENT_NAME = "filename-1234567890123456-2021-02-02:02:02";
    private static final String BINARY_URL = "binaryUrl";
    private static final String FILE_ID = "fileId";

    @Test
    void shouldConvertFromDocumentInfoToDocument() {

        final Document document = documentFrom(documentInfo());

        assertThat(document)
            .extracting(URL, FILENAME, BINARY_URL)
            .contains(
                DOC_URL,
                PDF_FILENAME,
                DOC_BINARY_URL);
    }

    private DocumentInfo documentInfo() {
        return new DocumentInfo(
            DOC_URL,
            PDF_FILENAME,
            DOC_BINARY_URL,
            FILE_ID
        );
    }

    @Test
    void shouldCreateDocumentFromDocumentInfoAndDocumentType() {

        final AdoptionDocument adoptionDocument = adoptionDocumentFrom(documentInfo(), APPLICATION);

        assertThat(adoptionDocument.getDocumentType()).isEqualTo(APPLICATION);
        assertThat(adoptionDocument.getDocumentFileName()).isEqualTo(PDF_FILENAME);
        assertThat(adoptionDocument
                       .getDocumentLink())
            .extracting(URL, FILENAME, BINARY_URL)
            .contains(
                DOC_URL,
                PDF_FILENAME,
                DOC_BINARY_URL);
    }
    
    @Test
    void shouldGenerateDocumentName() {

        final AdoptionDocument adoptionDocument = adoptionDocumentFrom(documentInfo(), APPLICATION);

        assertThat(
            formatDocumentName(1234567890123456L, FILENAME,
                               LocalDateTime.of(2021, 2, 2, 2, 2)))
            .isEqualTo(DOCUMENT_NAME);
    }
}
