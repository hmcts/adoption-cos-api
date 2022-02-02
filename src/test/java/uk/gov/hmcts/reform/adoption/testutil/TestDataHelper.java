package uk.gov.hmcts.reform.adoption.testutil;

import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.document.DocumentType;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionDocument;

import java.time.LocalDateTime;
import java.util.UUID;

import static uk.gov.hmcts.reform.adoption.document.DocumentType.APPLICATION;

public class TestDataHelper {

    public static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2021, 4, 28, 1, 0);

    public static ListValue<AdoptionDocument> documentWithType(final DocumentType documentType) {
        return documentWithType(documentType, UUID.randomUUID().toString());
    }

    public static ListValue<AdoptionDocument> documentWithType(final DocumentType documentType,
                                                              final String documentId) {
        String documentUrl = "http://localhost:8080/" + documentId;

        Document ccdDocument = new Document(
            documentUrl,
            "test-draft-adoption-application.pdf",
            documentUrl + "/binary"
        );

        AdoptionDocument adoptionDocument = AdoptionDocument
            .builder()
            .documentLink(ccdDocument)
            .documentFileName("test-draft-adoption-application-12345.pdf")
            .documentType(documentType)
            .build();


        return ListValue
            .<AdoptionDocument>builder()
            .id(APPLICATION.getLabel())
            .value(adoptionDocument)
            .build();
    }

    private TestDataHelper() {
    }
}
