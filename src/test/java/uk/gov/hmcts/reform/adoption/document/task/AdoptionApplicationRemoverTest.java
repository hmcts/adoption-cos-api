package uk.gov.hmcts.reform.adoption.document.task;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.document.DraftApplicationRemovalService;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.adoption.document.DocumentTypes.APPLICATION;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_CASE_ID;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.LOCAL_DATE_TIME;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.documentWithType;

@ExtendWith(MockitoExtension.class)
class AdoptionApplicationRemoverTest {

    @Mock
    private DraftApplicationRemovalService draftApplicationRemovalService;

    @InjectMocks
    private AdoptionApplicationRemover adoptionApplicationRemover;

    @Test
    void shouldRemoveDraftApplication() {

        final var generatedDocuments = singletonList(documentWithType(APPLICATION));
        final var caseData = CaseData.builder().build();
        caseData.setDocumentsGenerated(generatedDocuments);

        final CaseDetails<CaseData, State> caseDetails = new CaseDetails<>();
        caseDetails.setData(caseData);
        caseDetails.setId(TEST_CASE_ID);
        caseDetails.setCreatedDate(LOCAL_DATE_TIME);

        when(draftApplicationRemovalService.removeDraftApplicationDocument(generatedDocuments, TEST_CASE_ID))
            .thenReturn(emptyList());

        final var result = adoptionApplicationRemover.apply(caseDetails);
        assertThat(result.getData().getDocumentsGenerated(), empty());
        verify(draftApplicationRemovalService)
            .removeDraftApplicationDocument(
                generatedDocuments,
                TEST_CASE_ID);

        verifyNoMoreInteractions(draftApplicationRemovalService);
    }
}
