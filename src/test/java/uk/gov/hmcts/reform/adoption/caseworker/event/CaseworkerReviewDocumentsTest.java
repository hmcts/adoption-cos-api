package uk.gov.hmcts.reform.adoption.caseworker.event;

import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.ccd.sdk.ResolvedCCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.Event;
import uk.gov.hmcts.ccd.sdk.api.HasRole;
import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseworkerReviewDocuments;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.document.DocumentCategory;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionDocument;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionUploadDocument;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.idam.client.models.User;
import uk.gov.hmcts.reform.idam.client.models.UserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.commons.util.ReflectionUtils.findMethod;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseworkerReviewDocuments.CASEWORKER_REVIEW_DOCUMENT;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_AUTHORIZATION_TOKEN;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@ExtendWith(MockitoExtension.class)
public class CaseworkerReviewDocumentsTest {

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private Clock clock;

    @Mock
    private IdamService idamService;

    @InjectMocks
    CaseworkerReviewDocuments caseworkerReviewDocuments;

    @Test
    void shouldSuccessfullyCategoriseDocumentsForApplicationDocuments() {
        var caseDetails = getCaseDetails(DocumentCategory.APPLICATION_DOCUMENTS);
        setTimeAndIdamUser();
        var result = caseworkerReviewDocuments.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getApplicationDocumentsCategory()).isNotNull();
    }

    @Test
    void shouldSuccessfullyCategoriseDocumentsForApplicationDocumentsExisting() {
        var caseDetails = getCaseDetails(DocumentCategory.APPLICATION_DOCUMENTS);
        createApplicationDocumentsCategoryList(caseDetails);
        setTimeAndIdamUser();
        var result = caseworkerReviewDocuments.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getApplicationDocumentsCategory()).isNotNull();
    }

    @Test
    void shouldSuccessfullyCategoriseDocumentsForCourtOrders() {
        var caseDetails = getCaseDetails(DocumentCategory.COURT_ORDERS);
        setTimeAndIdamUser();
        var result = caseworkerReviewDocuments.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getCourtOrdersDocumentCategory()).isNotNull();
    }

    @Test
    void shouldSuccessfullyCategoriseDocumentsForReports() {
        var caseDetails = getCaseDetails(DocumentCategory.REPORTS);
        setTimeAndIdamUser();
        var result = caseworkerReviewDocuments.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getReportsDocumentCategory()).isNotNull();
    }

    @Test
    void shouldSuccessfullyCategoriseDocumentsForStatements() {
        var caseDetails = getCaseDetails(DocumentCategory.STATEMENTS);
        setTimeAndIdamUser();
        var result = caseworkerReviewDocuments.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getStatementsDocumentCategory()).isNotNull();
    }

    @Test
    void shouldSuccessfullyCategoriseDocumentsForCorrespondence() {
        var caseDetails = getCaseDetails(DocumentCategory.CORRESPONDENCE);
        setTimeAndIdamUser();
        var result = caseworkerReviewDocuments.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getCorrespondenceDocumentCategory()).isNotNull();
    }

    @Test
    void shouldSuccessfullyCategoriseDocumentsForAdditionalDocuments() {
        var caseDetails = getCaseDetails(DocumentCategory.ADDITIONAL_DOCUMENTS);
        setTimeAndIdamUser();
        var result = caseworkerReviewDocuments.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getAdditionalDocumentsCategory()).isNotNull();
    }

    private void setTimeAndIdamUser() {
        final var instant = Instant.now();
        final var zoneId = ZoneId.systemDefault();
        final var expectedDate = LocalDate.ofInstant(instant, zoneId);

        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(zoneId);

        when(httpServletRequest.getHeader(AUTHORIZATION)).thenReturn(TEST_AUTHORIZATION_TOKEN);

        when(idamService.retrieveUser(TEST_AUTHORIZATION_TOKEN)).thenReturn(getCaseworkerUser());
    }

    private void createApplicationDocumentsCategoryList(CaseDetails<CaseData, State> caseDetails) {
        AdoptionUploadDocument adoptionUploadDocument = new AdoptionUploadDocument();
        adoptionUploadDocument.setDocumentCategory(DocumentCategory.APPLICATION_DOCUMENTS);
        ListValue<AdoptionUploadDocument> adoptionUploadDocumentListValue = new ListValue<>();
        adoptionUploadDocumentListValue.setValue(adoptionUploadDocument);
        List<ListValue<AdoptionUploadDocument>> listValues = new ArrayList<>();
        listValues.add(adoptionUploadDocumentListValue);
        caseDetails.getData().setApplicationDocumentsCategory(listValues);
    }

    private CaseDetails<CaseData, State> getCaseDetails(DocumentCategory documentCategory) {
        final var details = new CaseDetails<CaseData, State>();
        final var data = caseData();
        List<ListValue<AdoptionDocument>> adoptionDocumentList = new ArrayList<>();
        adoptionDocumentList.add(createDocumentWithCategory(documentCategory));
        data.setLaDocumentsUploaded(adoptionDocumentList);
        details.setData(data);
        details.setId(1L);
        return details;
    }

    @NotNull
    private ListValue<AdoptionDocument> createDocumentWithCategory(DocumentCategory documentCategory) {
        Document document = new Document();
        document.setUrl("TEST_URL");
        document.setFilename("TEST_FILE_NAME");
        document.setBinaryUrl("TEST_BINARY_URL");
        AdoptionDocument adoptionDocument = new AdoptionDocument();
        adoptionDocument.setDocumentCategory(documentCategory);
        adoptionDocument.setDocumentDateAdded(LocalDate.EPOCH);
        adoptionDocument.setDocumentComment("TEST_COMMENT");
        adoptionDocument.setDocumentLink(document);
        adoptionDocument.setName("TEST_NAME");
        adoptionDocument.setRole("TEST_ROLE");
        ListValue<AdoptionDocument> listValue = new ListValue<>();
        listValue.setValue(adoptionDocument);
        return listValue;
    }


    @Test
    void shouldAddConfigurationToConfigBuilder() {
        final ConfigBuilderImpl<CaseData, State, UserRole> configBuilder = createCaseDataConfigBuilder();
        caseworkerReviewDocuments.configure(configBuilder);
        assertThat(getEventsFrom(configBuilder).values())
            .extracting(Event::getId)
            .contains(CASEWORKER_REVIEW_DOCUMENT);
    }

    public static ConfigBuilderImpl<CaseData, State, UserRole> createCaseDataConfigBuilder() {
        return new ConfigBuilderImpl<>(new ResolvedCCDConfig<>(
            CaseData.class,
            State.class,
            UserRole.class,
            new HashMap<>(),
            ImmutableSet.copyOf(State.class.getEnumConstants())
        ));
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

    private User getCaseworkerUser() {
        UserDetails userDetails = UserDetails
            .builder()
            .forename("testFname")
            .surname("testSname")
            .build();

        return new User(TEST_AUTHORIZATION_TOKEN, userDetails);
    }
}
