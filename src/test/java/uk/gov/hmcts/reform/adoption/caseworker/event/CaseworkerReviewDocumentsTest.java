package uk.gov.hmcts.reform.adoption.caseworker.event;

import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;
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

import java.lang.reflect.InvocationTargetException;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.commons.util.ReflectionUtils.findMethod;
import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseworkerReviewDocuments.CASEWORKER_REVIEW_DOCUMENT;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@ExtendWith(MockitoExtension.class)
public class CaseworkerReviewDocumentsTest {

    @Mock
    private Clock clock;

    @InjectMocks
    CaseworkerReviewDocuments caseworkerReviewDocuments;

    @Test
    public void shouldSuccessfullyCategoriseDocumentsForApplicationDocuments() {
        var caseDetails = getCaseDetails(DocumentCategory.APPLICATION_DOCUMENTS);
        var result = caseworkerReviewDocuments.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getApplicationDocumentsCategory()).isNotNull();
    }

    @Test
    public void shouldSuccessfullyCategoriseDocumentsForApplicationDocumentsExisting() {
        var caseDetails = getCaseDetails(DocumentCategory.APPLICATION_DOCUMENTS);
        createApplicationDocumentsCategoryList(caseDetails);
        var result = caseworkerReviewDocuments.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getApplicationDocumentsCategory()).isNotNull();
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

    @Test
    public void shouldSuccessfullyCategoriseDocumentsForCourtOrders() {
        var caseDetails = getCaseDetails(DocumentCategory.COURT_ORDERS);
        var result = caseworkerReviewDocuments.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getCourtOrdersDocumentCategory()).isNotNull();
    }

    @Test
    public void shouldSuccessfullyCategoriseDocumentsForReports() {
        var caseDetails = getCaseDetails(DocumentCategory.REPORTS);
        var result = caseworkerReviewDocuments.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getReportsDocumentCategory()).isNotNull();
    }

    @Test
    public void shouldSuccessfullyCategoriseDocumentsForStatements() {
        var caseDetails = getCaseDetails(DocumentCategory.STATEMENTS);
        var result = caseworkerReviewDocuments.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getStatementsDocumentCategory()).isNotNull();
    }

    @Test
    public void shouldSuccessfullyCategoriseDocumentsForCorrespondence() {
        var caseDetails = getCaseDetails(DocumentCategory.CORRESPONDENCE);
        var result = caseworkerReviewDocuments.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getCorrespondenceDocumentCategory()).isNotNull();
    }

    @Test
    public void shouldSuccessfullyCategoriseDocumentsForAdditionalDocuments() {
        var caseDetails = getCaseDetails(DocumentCategory.ADDITIONAL_DOCUMENTS);
        var result = caseworkerReviewDocuments.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getAdditionalDocumentsCategory()).isNotNull();
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
}
