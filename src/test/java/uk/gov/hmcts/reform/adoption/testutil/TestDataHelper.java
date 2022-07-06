package uk.gov.hmcts.reform.adoption.testutil;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;
import org.springframework.util.ResourceUtils;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.ccd.sdk.ResolvedCCDConfig;
import uk.gov.hmcts.ccd.sdk.api.Event;
import uk.gov.hmcts.ccd.sdk.api.HasRole;
import uk.gov.hmcts.ccd.sdk.api.Search;
import uk.gov.hmcts.ccd.sdk.api.Search.SearchBuilder;
import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Applicant;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.bulkscan.ccd.ExceptionRecordState;
import uk.gov.hmcts.reform.adoption.bulkscan.data.ExceptionRecord;
import uk.gov.hmcts.reform.adoption.document.DocumentType;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionDocument;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.codehaus.plexus.util.ReflectionUtils.getValueIncludingSuperclasses;
import static org.junit.platform.commons.util.ReflectionUtils.findMethod;
import static org.springframework.util.ResourceUtils.getFile;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference.ENGLISH;
import static uk.gov.hmcts.reform.adoption.document.DocumentType.APPLICATION;
import static uk.gov.hmcts.reform.adoption.notification.CommonContent.APPLICATION_REFERENCE;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_FIRST_NAME;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_LAST_NAME;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_USER_EMAIL;

public class TestDataHelper {

    public static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2021, 4, 28, 1, 0);
    private static final TestDataHelper.MapTypeReference MAP_TYPE = new TestDataHelper.MapTypeReference();

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

    public static Map<String, Object> getMainTemplateVars() {
        Map<String, Object> templateVars = new HashMap<>();
        Object appRef = new String("1234-5678-9012-3456");
        templateVars.put(APPLICATION_REFERENCE, appRef);
        return templateVars;
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

    public static byte[] resourceAsBytes(final String resourcePath) throws IOException {
        final File file = ResourceUtils.getFile(resourcePath);
        return Files.readAllBytes(file.toPath());
    }

    public static ConfigBuilderImpl<ExceptionRecord, ExceptionRecordState, UserRole> createExceptionRecordConfigBuilder() {
        return new ConfigBuilderImpl<>(new ResolvedCCDConfig<>(
            ExceptionRecord.class,
            ExceptionRecordState.class,
            UserRole.class,
            new HashMap<>(),
            ImmutableSet.copyOf(ExceptionRecordState.class.getEnumConstants())));
    }

    @SuppressWarnings({"unchecked"})
    public static <T, S, R extends HasRole> Map<String, Event<T, R, S>> getEventsFrom(
        final ConfigBuilderImpl<T, S, R> configBuilder) {
        return (Map<String, Event<T, R, S>>) findMethod(ConfigBuilderImpl.class, "getEvents")
            .map(method -> {
                try {
                    method.setAccessible(true);
                    return method.invoke(configBuilder);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new AssertionError("Unable to invoke ConfigBuilderImpl.class method getEvents", e);
                }
            })
            .orElseThrow(() -> new AssertionError("Unable to find ConfigBuilderImpl.class method getEvents"));
    }

    public static <T, S, R extends HasRole> Search getSearchInputFields(
        final ConfigBuilderImpl<T, S, R> configBuilder) throws IllegalAccessException {
        return getSearchFor("searchInputFields", configBuilder);
    }

    public static <T, S, R extends HasRole> Search getSearchResultFields(
        final ConfigBuilderImpl<T, S, R> configBuilder) throws IllegalAccessException {
        return getSearchFor("searchResultFields", configBuilder);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static <T, S, R extends HasRole> Search getSearchFor(
        final String fieldName,
        final ConfigBuilderImpl<T, S, R> configBuilder) throws IllegalAccessException {
        final List<SearchBuilder> searchInputFields =
            (List<SearchBuilder>) getValueIncludingSuperclasses(fieldName, configBuilder);
        final var searchInputBuilder = searchInputFields.get(0);
        return searchInputBuilder.build();
    }
}
