package uk.gov.hmcts.reform.adoption.common.service.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.document.CaseDataDocumentService;
import uk.gov.hmcts.reform.adoption.document.DocumentType;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.ADOPTION_LA_APPLICATION_FILE_NAME;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.ADOPTION_LA_APPLICATION_SUMMARY;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.ADOPTION_APPLICATION_SUMMARY;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.ADOPTION_APPLICATION_FILE_NAME;
import static uk.gov.hmcts.reform.adoption.document.DocumentUtil.formatDocumentName;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_CASE_ID;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@ExtendWith(MockitoExtension.class)
class GenerateApplicationSummaryDocumentTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private CaseDataDocumentService caseDataDocumentService;
    @InjectMocks
    GenerateApplicationSummaryDocument generateApplicationSummaryDocument;
    @InjectMocks
    GenerateLaApplicationSummaryDocument generateLaApplicationSummaryDocument;

    @Test
    void shouldInitiateDocumentGeneration() {
        final CaseData caseData = caseData();
        final CaseDetails<CaseData, State> caseDetails = new CaseDetails<>();
        caseDetails.setId(TEST_CASE_ID);
        caseDetails.setData(caseData);
        caseDetails.setState(State.Submitted);
        Map<String, Object> templateVars = new HashMap<>();
        when(objectMapper.convertValue(caseData, Map.class)).thenReturn(templateVars);
        generateApplicationSummaryDocument.apply(caseDetails);
        verify(caseDataDocumentService).renderDocumentAndUpdateCaseData(
            caseData,
            DocumentType.APPLICATION_SUMMARY_EN,
            templateVars,
            TEST_CASE_ID,
            ADOPTION_APPLICATION_SUMMARY,
            LanguagePreference.ENGLISH,
            formatDocumentName(TEST_CASE_ID, ADOPTION_APPLICATION_FILE_NAME, LocalDateTime.now())
        );
    }

    /**
     * Test Scenario: Should be able to Generate the LA Portal Summary application details as a PDF file.
     * GenerateLaApplicationSummaryDocument is the targeted class
     */
    @Test
    void shouldInitiateLaPortalDocumentGeneration() {
        final CaseData caseData = caseData();
        final CaseDetails<CaseData, State> caseDetails = new CaseDetails<>();
        caseDetails.setId(TEST_CASE_ID);
        caseDetails.setData(caseData);
        caseDetails.setState(State.LaSubmitted);

        Map<String, Object> templateVars = new HashMap<>();
        when(objectMapper.convertValue(caseData, Map.class)).thenReturn(templateVars);
        generateLaApplicationSummaryDocument.apply(caseDetails);
        verify(caseDataDocumentService).renderDocumentAndUpdateCaseData(
            caseData,
            DocumentType.APPLICATION_LA_SUMMARY_EN,
            templateVars,
            TEST_CASE_ID,
            ADOPTION_LA_APPLICATION_SUMMARY,
            LanguagePreference.ENGLISH,
            formatDocumentName(TEST_CASE_ID, ADOPTION_LA_APPLICATION_FILE_NAME, LocalDateTime.now())
        );
    }
}
