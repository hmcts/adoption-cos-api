package uk.gov.hmcts.reform.adoption.common.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.common.service.task.GenerateApplicationSummaryDocument;
import uk.gov.hmcts.reform.adoption.common.service.task.SendSubmissionNotifications;
import uk.gov.hmcts.reform.adoption.common.service.task.SetDateSubmitted;
import uk.gov.hmcts.reform.adoption.common.service.task.SetStateAfterSubmission;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubmissionServiceTest {

    @Mock
    private SetStateAfterSubmission setStateAfterSubmission;

    @Mock
    private SetDateSubmitted setDateSubmitted;

    @Mock
    private SendSubmissionNotifications sendSubmissionNotifications;

    @Mock
    private GenerateApplicationSummaryDocument generateApplicationSummaryDocument;

    @InjectMocks
    private SubmissionService submissionService;


    @Test
    void shouldProcessSubmissionCaseTasks() {

        final CaseDetails<CaseData, State> caseDetails = new CaseDetails<>();
        final CaseDetails<CaseData, State> expectedCaseDetails = new CaseDetails<>();

        when(setStateAfterSubmission.apply(caseDetails)).thenReturn(caseDetails);

        when(setDateSubmitted.apply(caseDetails)).thenReturn(caseDetails);

        when(generateApplicationSummaryDocument.apply(caseDetails)).thenReturn(expectedCaseDetails);

        CaseDetails<CaseData, State> result = submissionService.submitApplication(caseDetails);

        assertThat(result, samePropertyValuesAs(expectedCaseDetails));

        verify(setStateAfterSubmission).apply(caseDetails);
        verify(setDateSubmitted).apply(caseDetails);
    }


}
