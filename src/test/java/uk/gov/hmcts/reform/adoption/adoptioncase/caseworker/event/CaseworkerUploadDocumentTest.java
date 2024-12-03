package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.Event;
import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.event.EventTest;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.document.DocumentCategory;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionUploadDocument;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseworkerUploadDocument.CASEWORKER_UPLOAD_DOCUMENT;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@ExtendWith(MockitoExtension.class)
public class CaseworkerUploadDocumentTest extends EventTest {

    @InjectMocks
    CaseworkerUploadDocument caseworkerUploadDocument;

    @Mock
    private Clock clock;

    @Test
    void shouldAddConfigurationToConfigBuilder() throws Exception {
        final ConfigBuilderImpl<CaseData, State, UserRole> configBuilder = createCaseDataConfigBuilder();

        caseworkerUploadDocument.configure(configBuilder);

        assertThat(getEventsFrom(configBuilder).values())
            .extracting(Event::getId)
            .contains(CASEWORKER_UPLOAD_DOCUMENT);
    }


    @Test
    void shouldSuccessfullyAddAdoptionDocumentWithApplicationDocumentCategory() {
        final var instant = Instant.now();
        final var zoneId = ZoneId.systemDefault();
        final var expectedDate = LocalDate.ofInstant(instant, zoneId);

        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(zoneId);

        var caseDetails = getCaseDetails();
        caseDetails.getData().setAdoptionUploadDocument(setAdoptionDocumentCategory(DocumentCategory.APPLICATION_DOCUMENTS));
        caseDetails.getData().getAdoptionUploadDocument().setName("TEST_NAME");
        caseDetails.getData().getAdoptionUploadDocument().setRole("TEST_ROLE");
        var result = caseworkerUploadDocument.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getApplicationDocumentsCategory()).isNotNull();
        assertThat(result.getData().getApplicationDocumentsCategory())
            .allMatch(item -> expectedDate.equals(item.getValue().getDocumentDateAdded()));
        assertThat(result.getData().getApplicationDocumentsCategory())
            .allMatch(item -> item.getValue().getDocumentCategory() == null);
    }

    @Test
    void shouldSuccessfullyAddAdoptionDocumentWithApplicationDocumentCategoryWhenThereAreExistingEntries() {
        final var instant = Instant.now();
        final var zoneId = ZoneId.systemDefault();
        final var expectedDate = LocalDate.ofInstant(instant, zoneId);

        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(zoneId);

        var caseDetails = getCaseDetails();
        caseDetails.getData().setAdoptionUploadDocument(setAdoptionDocumentCategory(DocumentCategory.APPLICATION_DOCUMENTS));
        AdoptionUploadDocument adoptionDocument = setAdoptionDocumentCategory(DocumentCategory.APPLICATION_DOCUMENTS);
        adoptionDocument.setDocumentDateAdded(LocalDate.now(clock));
        adoptionDocument.setDocumentCategory(null);
        ListValue<AdoptionUploadDocument> listValue = ListValue.<AdoptionUploadDocument>builder()
            .id("1")
            .value(adoptionDocument)
            .build();
        List<ListValue<AdoptionUploadDocument>> applicationDocumentsCategoryList = new ArrayList<>();
        applicationDocumentsCategoryList.add(listValue);
        caseDetails.getData().setApplicationDocumentsCategory(applicationDocumentsCategoryList);
        var result = caseworkerUploadDocument.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getApplicationDocumentsCategory()).isNotNull();
        assertThat(result.getData().getApplicationDocumentsCategory())
            .allMatch(item -> expectedDate.equals(item.getValue().getDocumentDateAdded()));
        assertThat(result.getData().getApplicationDocumentsCategory())
            .allMatch(item -> item.getValue().getDocumentCategory() == null);
    }

    @Test
    void shouldSuccessfullyAddAdoptionDocumentWithCourtOrdersDocumentCategory() {
        final var instant = Instant.now();
        final var zoneId = ZoneId.systemDefault();
        final var expectedDate = LocalDate.ofInstant(instant, zoneId);

        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(zoneId);

        var caseDetails = getCaseDetails();
        caseDetails.getData().setAdoptionUploadDocument(setAdoptionDocumentCategory(DocumentCategory.COURT_ORDERS));
        var result = caseworkerUploadDocument.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getCourtOrdersDocumentCategory()).isNotNull();
        assertThat(result.getData().getCourtOrdersDocumentCategory())
            .allMatch(item -> expectedDate.equals(item.getValue().getDocumentDateAdded()));
        assertThat(result.getData().getCourtOrdersDocumentCategory())
            .allMatch(item -> item.getValue().getDocumentCategory() == null);
    }

    @Test
    void shouldSuccessfullyAddAdoptionDocumentWithReportsDocumentCategory() {
        final var instant = Instant.now();
        final var zoneId = ZoneId.systemDefault();
        final var expectedDate = LocalDate.ofInstant(instant, zoneId);

        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(zoneId);

        var caseDetails = getCaseDetails();
        caseDetails.getData().setAdoptionUploadDocument(setAdoptionDocumentCategory(DocumentCategory.REPORTS));
        var result = caseworkerUploadDocument.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getReportsDocumentCategory()).isNotNull();
        assertThat(result.getData().getReportsDocumentCategory())
            .allMatch(item -> expectedDate.equals(item.getValue().getDocumentDateAdded()));
        assertThat(result.getData().getReportsDocumentCategory())
            .allMatch(item -> item.getValue().getDocumentCategory() == null);
    }

    @Test
    void shouldSuccessfullyAddAdoptionDocumentWithStatementsDocumentCategory() {
        final var instant = Instant.now();
        final var zoneId = ZoneId.systemDefault();
        final var expectedDate = LocalDate.ofInstant(instant, zoneId);

        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(zoneId);

        var caseDetails = getCaseDetails();
        caseDetails.getData().setAdoptionUploadDocument(setAdoptionDocumentCategory(DocumentCategory.STATEMENTS));
        var result = caseworkerUploadDocument.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getStatementsDocumentCategory()).isNotNull();
        assertThat(result.getData().getStatementsDocumentCategory())
            .allMatch(item -> expectedDate.equals(item.getValue().getDocumentDateAdded()));
        assertThat(result.getData().getStatementsDocumentCategory())
            .allMatch(item -> item.getValue().getDocumentCategory() == null);
    }

    @Test
    void shouldSuccessfullyAddAdoptionDocumentWithCorrespondenceDocumentCategory() {
        final var instant = Instant.now();
        final var zoneId = ZoneId.systemDefault();
        final var expectedDate = LocalDate.ofInstant(instant, zoneId);

        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(zoneId);

        var caseDetails = getCaseDetails();
        caseDetails.getData().setAdoptionUploadDocument(setAdoptionDocumentCategory(DocumentCategory.CORRESPONDENCE));
        var result = caseworkerUploadDocument.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getCorrespondenceDocumentCategory()).isNotNull();
        assertThat(result.getData().getCorrespondenceDocumentCategory())
            .allMatch(item -> expectedDate.equals(item.getValue().getDocumentDateAdded()));
        assertThat(result.getData().getCorrespondenceDocumentCategory())
            .allMatch(item -> item.getValue().getDocumentCategory() == null);
    }

    @Test
    void shouldSuccessfullyAddAdoptionDocumentWithAdditionalDocumentCategory() {
        final var instant = Instant.now();
        final var zoneId = ZoneId.systemDefault();
        final var expectedDate = LocalDate.ofInstant(instant, zoneId);

        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(zoneId);

        var caseDetails = getCaseDetails();
        caseDetails.getData().setAdoptionUploadDocument(setAdoptionDocumentCategory(DocumentCategory.ADDITIONAL_DOCUMENTS));
        var result = caseworkerUploadDocument.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getAdditionalDocumentsCategory()).isNotNull();
        assertThat(result.getData().getAdditionalDocumentsCategory())
            .allMatch(item -> expectedDate.equals(item.getValue().getDocumentDateAdded()));
        assertThat(result.getData().getAdditionalDocumentsCategory())
            .allMatch(item -> item.getValue().getDocumentCategory() == null);
    }

    private CaseDetails<CaseData, State> getCaseDetails() {
        final var details = new CaseDetails<CaseData, State>();
        final var data = caseData();
        details.setData(data);
        details.setId(1L);
        return details;
    }

    private AdoptionUploadDocument setAdoptionDocumentCategory(DocumentCategory category) {
        return AdoptionUploadDocument.builder()
                .documentLink(Document
                                  .builder()
                                  .url("TEST URL")
                                  .build())
                .documentComment("TEST_COMMENT")
                .documentCategory(category)
                .name("TEST_NAME")
                .role("TEST_ROLE")
                .build();
    }
}
