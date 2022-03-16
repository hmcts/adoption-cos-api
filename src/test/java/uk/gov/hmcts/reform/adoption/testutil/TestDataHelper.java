package uk.gov.hmcts.reform.adoption.testutil;

import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Applicant;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.document.DocumentType;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionDocument;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference.ENGLISH;
import static uk.gov.hmcts.reform.adoption.document.DocumentType.APPLICATION;
import static uk.gov.hmcts.reform.adoption.notification.CommonContent.APPLICATION_REFERENCE;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_FIRST_NAME;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_LAST_NAME;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_USER_EMAIL;

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

    public static CaseData caseData() {
        return CaseData.builder()
            .applicant1(getApplicant())
            .applicant2(getApplicant())
            .build();
    }

    public static Applicant getApplicant() {
        return Applicant.builder()
            .firstName(TEST_FIRST_NAME)
            .lastName(TEST_LAST_NAME)
            .email(TEST_USER_EMAIL)
            .emailAddress(TEST_USER_EMAIL)
            .languagePreference(ENGLISH)
            .build();
    }

    public static Map<String, Object> getMainTemplateVars() {
        Map<String, Object> templateVars = new HashMap<>();
        Object appRef = new String("1234-5678-9012-3456");
        templateVars.put(APPLICATION_REFERENCE, appRef);
        return templateVars;
    }
}
