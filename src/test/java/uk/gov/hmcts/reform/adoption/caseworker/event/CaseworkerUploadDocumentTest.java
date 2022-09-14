package uk.gov.hmcts.reform.adoption.caseworker.event;

import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.ccd.sdk.ResolvedCCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.Event;
import uk.gov.hmcts.ccd.sdk.api.HasRole;
import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseworkerUploadDocument;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.OtherParty;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.document.DocumentCategory;
import uk.gov.hmcts.reform.adoption.document.DocumentSubmittedBy;
import uk.gov.hmcts.reform.adoption.document.DocumentSubmitter;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionDocument;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.commons.util.ReflectionUtils.findMethod;
import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseworkerUploadDocument.CASEWORKER_UPLOAD_DOCUMENT;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@ExtendWith(MockitoExtension.class)
public class CaseworkerUploadDocumentTest {

    @InjectMocks
    CaseworkerUploadDocument caseworkerUploadDocument;

    @Test
    void shouldAddConfigurationToConfigBuilder() throws Exception {
        final ConfigBuilderImpl<CaseData, State, UserRole> configBuilder = createCaseDataConfigBuilder();

        caseworkerUploadDocument.configure(configBuilder);

        assertThat(getEventsFrom(configBuilder).values())
            .extracting(Event::getId)
            .contains(CASEWORKER_UPLOAD_DOCUMENT);
    }


    @Test
    public void shouldSuccessfullyAddAdoptionDocumentWithApplicationDocumentCategory() {
        var caseDetails = getCaseDetails();
        OtherParty otherParty = new OtherParty("TEST_PARTY_ROLE","TEST_PARTY_NAME");
        caseDetails.getData().setAdoptionDocument(setAdoptionDocumentCategory(DocumentCategory.APPLICATION_DOCUMENTS));
        /*caseDetails.getData().getAdoptionDocument()
            .setDocumentSubmitter(DocumentSubmitter.builder()
                                      .documentSubmittedBy(DocumentSubmittedBy.ADOPTION_AGENCY_OR_LOCAL_AUTHORITY)
                                      .otherParty(otherParty)
                                      .build());*/
        caseDetails.getData().getAdoptionDocument().setName("TEST_NAME");
        caseDetails.getData().getAdoptionDocument().setRole("TEST_ROLE");
        var result = caseworkerUploadDocument.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getApplicationDocumentsCategory()).isNotNull();
    }

    @Test
    public void shouldSuccessfullyAddAdoptionDocumentWithApplicationDocumentCategoryWhenThereAreExistingEntries() {
        var caseDetails = getCaseDetails();
        caseDetails.getData().setAdoptionDocument(setAdoptionDocumentCategory(DocumentCategory.APPLICATION_DOCUMENTS));
        ListValue<AdoptionDocument> listValue = ListValue.<AdoptionDocument>builder()
            .id("1")
            .value(setAdoptionDocumentCategory(DocumentCategory.APPLICATION_DOCUMENTS))
            .build();
        List<ListValue<AdoptionDocument>> applicationDocumentsCategoryList = new ArrayList<>();
        applicationDocumentsCategoryList.add(listValue);
        caseDetails.getData().setApplicationDocumentsCategory(applicationDocumentsCategoryList);
        var result = caseworkerUploadDocument.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getApplicationDocumentsCategory()).isNotNull();
    }

    @Test
    public void shouldSuccessfullyAddAdoptionDocumentWithCourtOrdersDocumentCategory() {
        var caseDetails = getCaseDetails();
        caseDetails.getData().setAdoptionDocument(setAdoptionDocumentCategory(DocumentCategory.COURT_ORDERS));
        var result = caseworkerUploadDocument.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getCourtOrdersDocumentCategory()).isNotNull();
    }

    @Test
    public void shouldSuccessfullyAddAdoptionDocumentWithReportsDocumentCategory() {
        var caseDetails = getCaseDetails();
        caseDetails.getData().setAdoptionDocument(setAdoptionDocumentCategory(DocumentCategory.REPORTS));
        var result = caseworkerUploadDocument.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getReportsDocumentCategory()).isNotNull();
    }

    @Test
    public void shouldSuccessfullyAddAdoptionDocumentWithStatementsDocumentCategory() {
        var caseDetails = getCaseDetails();
        caseDetails.getData().setAdoptionDocument(setAdoptionDocumentCategory(DocumentCategory.STATEMENTS));
        var result = caseworkerUploadDocument.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getStatementsDocumentCategory()).isNotNull();
    }

    @Test
    public void shouldSuccessfullyAddAdoptionDocumentWithCorrespondenceDocumentCategory() {
        var caseDetails = getCaseDetails();
        caseDetails.getData().setAdoptionDocument(setAdoptionDocumentCategory(DocumentCategory.CORRESPONDENCE));
        var result = caseworkerUploadDocument.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getCorrespondenceDocumentCategory()).isNotNull();
    }

    @Test
    public void shouldSuccessfullyAddAdoptionDocumentWithAdditionalDocumentCategory() {
        var caseDetails = getCaseDetails();
        caseDetails.getData().setAdoptionDocument(setAdoptionDocumentCategory(DocumentCategory.ADDITIONAL_DOCUMENTS));
        var result = caseworkerUploadDocument.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getAdditionalDocumentsCategory()).isNotNull();
    }

    private CaseDetails<CaseData, State> getCaseDetails() {
        final var details = new CaseDetails<CaseData, State>();
        final var data = caseData();
        details.setData(data);
        details.setId(1L);
        return details;
    }

    private AdoptionDocument setAdoptionDocumentCategory(DocumentCategory category) {
        DocumentSubmitter documentSubmitter = DocumentSubmitter.builder()
                .documentSubmittedBy(DocumentSubmittedBy.BIRTH_FATHER)
                .otherParty(new OtherParty("TEST_PARTY_ROLE","TEST_PARTY_NAME"))
                .build();

        return AdoptionDocument.builder()
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

    public static ConfigBuilderImpl<CaseData, State, UserRole> createCaseDataConfigBuilder() {
        return new ConfigBuilderImpl<>(new ResolvedCCDConfig<>(
            CaseData.class,
            State.class,
            UserRole.class,
            new HashMap<>(),
            ImmutableSet.copyOf(State.class.getEnumConstants())));
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
