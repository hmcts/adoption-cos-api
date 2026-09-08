package uk.gov.hmcts.reform.adoption.testutil;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.ResourceUtils;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Applicant;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import static org.springframework.util.ResourceUtils.getFile;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference.ENGLISH;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_FIRST_NAME;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_LAST_NAME;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_USER_EMAIL;

public class TestDataHelper {


    private static final MapTypeReference MAP_TYPE = new MapTypeReference();

    private TestDataHelper() {
    }

    public static CaseData caseData() {
        return CaseData.builder()
            .applicant1(getApplicant())
            .applicant2(getApplicant())
            .build();
    }

    public static Map<String, Object> caseData(final String resourcePath) throws IOException {
        return getObjectMapper().readValue(getFile(resourcePath), MAP_TYPE);
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

    private static class MapTypeReference extends TypeReference<Map<String, Object>> {
    }

    public static ObjectMapper getObjectMapper() {
        return new ObjectMapper().findAndRegisterModules();
    }

    public static String expectedResponse(final String resourcePath) throws IOException {
        return resourceAsString(resourcePath);
    }

    public static String resourceAsString(final String resourcePath) throws IOException {
        final File file = ResourceUtils.getFile(resourcePath);
        return new String(Files.readAllBytes(file.toPath()));
    }
}
