package uk.gov.hmcts.reform.adoption.adoptioncase.schedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.service.CcdSearchService;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.adoption.notification.MultiChildSubmitAlertEmailNotification;
import uk.gov.hmcts.reform.adoption.systemupdate.CaseDetailsConverter;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.idam.client.models.User;
import uk.gov.hmcts.reform.idam.client.models.UserDetails;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Draft;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.LaSubmitted;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Submitted;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_USER_EMAIL;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_USER_EMAIL_2;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@ExtendWith(MockitoExtension.class)
class AlertMultiChildApplicationToSubmitTaskTest {

    private static final String SYSTEM_UPDATE_AUTH_TOKEN = "Bearer SystemUpdateAuthToken";
    private static final String SERVICE_AUTHORIZATION = "ServiceAuthorization";
    private static final String APPLICANT_1_EMAIL = "applicant1Email";

    @InjectMocks
    private AlertMultiChildApplicationToSubmitTask alertMultiChildApplicationToSubmitTask;

    @Mock
    private CcdSearchService ccdSearchService;

    @Mock
    private IdamService idamService;

    @Mock
    private AuthTokenGenerator authTokenGenerator;

    @Mock
    private MultiChildSubmitAlertEmailNotification multiChildSubmitAlertEmailNotification;

    @Mock
    private CaseDetailsConverter caseDetailsConverter;

    @BeforeEach
    void setUp() {
        User user = new User(SYSTEM_UPDATE_AUTH_TOKEN, UserDetails.builder().build());
        when(idamService.retrieveSystemUpdateUserDetails()).thenReturn(user);
        when(authTokenGenerator.generate()).thenReturn(SERVICE_AUTHORIZATION);
    }

    @Test
    void shouldSendOneReminderWhenApplicantHasDraftAndSubmittedCase() {
        final CaseDetails draftCase = caseDetails(1L, TEST_USER_EMAIL, Draft);
        final CaseDetails submittedCase = caseDetails(2L, TEST_USER_EMAIL, Submitted);

        mockCasesByState(
            List.of(draftCase),
            List.of(submittedCase),
            List.of()
        );
        mockConvertedCaseDetails(draftCase);

        alertMultiChildApplicationToSubmitTask.run();

        verify(multiChildSubmitAlertEmailNotification, times(1)).sendToApplicants(
            any(CaseData.class),
            eq(1L)
        );
        verify(caseDetailsConverter).convertToCaseDetailsFromReformModel(draftCase);
    }

    @Test
    void shouldSendOneReminderWhenApplicantHasDraftAndLaSubmittedCase() {
        final CaseDetails draftCase = caseDetails(1L, TEST_USER_EMAIL, Draft);
        final CaseDetails laSubmittedCase = caseDetails(2L, TEST_USER_EMAIL, LaSubmitted);

        mockCasesByState(
            List.of(draftCase),
            List.of(),
            List.of(laSubmittedCase)
        );
        mockConvertedCaseDetails(draftCase);

        alertMultiChildApplicationToSubmitTask.run();

        verify(multiChildSubmitAlertEmailNotification, times(1)).sendToApplicants(
            any(CaseData.class),
            eq(1L)
        );
        verify(caseDetailsConverter).convertToCaseDetailsFromReformModel(draftCase);
    }

    @Test
    void shouldNotSendReminderWhenApplicantOnlyHasOneDraftCase() {
        final CaseDetails draftCase = caseDetails(1L, TEST_USER_EMAIL, Draft);

        mockCasesByState(
            List.of(draftCase),
            List.of(),
            List.of()
        );

        alertMultiChildApplicationToSubmitTask.run();

        verify(multiChildSubmitAlertEmailNotification, never()).sendToApplicants(
            any(CaseData.class),
            any(Long.class)
        );
        verify(caseDetailsConverter, never()).convertToCaseDetailsFromReformModel(any());
    }

    @Test
    void shouldNotSendReminderWhenApplicantHasMultipleDraftCasesButNoSubmittedOrLaSubmittedCase() {
        final CaseDetails draftCase1 = caseDetails(1L, TEST_USER_EMAIL, Draft);
        final CaseDetails draftCase2 = caseDetails(2L, TEST_USER_EMAIL, Draft);

        mockCasesByState(
            List.of(draftCase1, draftCase2),
            List.of(),
            List.of()
        );

        alertMultiChildApplicationToSubmitTask.run();

        verify(multiChildSubmitAlertEmailNotification, never()).sendToApplicants(
            any(CaseData.class),
            any(Long.class)
        );
        verify(caseDetailsConverter, never()).convertToCaseDetailsFromReformModel(any());
    }

    @Test
    void shouldNotSendReminderWhenDraftAndSubmittedCasesHaveDifferentApplicantEmails() {
        final CaseDetails draftCase = caseDetails(1L, TEST_USER_EMAIL, Draft);
        final CaseDetails submittedCase = caseDetails(2L, TEST_USER_EMAIL_2, Submitted);

        mockCasesByState(
            List.of(draftCase),
            List.of(submittedCase),
            List.of()
        );

        alertMultiChildApplicationToSubmitTask.run();

        verify(multiChildSubmitAlertEmailNotification, never()).sendToApplicants(
            any(CaseData.class),
            any(Long.class)
        );
        verify(caseDetailsConverter, never()).convertToCaseDetailsFromReformModel(any());
    }

    @Test
    void shouldOnlySendOneReminderWhenApplicantHasMultipleQualifyingCases() {
        final CaseDetails draftCase1 = caseDetails(1L, TEST_USER_EMAIL, Draft);
        final CaseDetails draftCase2 = caseDetails(2L, TEST_USER_EMAIL, Draft);
        final CaseDetails submittedCase = caseDetails(3L, TEST_USER_EMAIL, Submitted);
        final CaseDetails laSubmittedCase = caseDetails(4L, TEST_USER_EMAIL, LaSubmitted);

        mockCasesByState(
            List.of(draftCase1, draftCase2),
            List.of(submittedCase),
            List.of(laSubmittedCase)
        );
        mockConvertedCaseDetails(draftCase1);

        alertMultiChildApplicationToSubmitTask.run();

        verify(multiChildSubmitAlertEmailNotification, times(1)).sendToApplicants(
            any(CaseData.class),
            eq(1L)
        );
        verify(caseDetailsConverter).convertToCaseDetailsFromReformModel(draftCase1);
    }

    private void mockCasesByState(
        final List<CaseDetails> draftCases,
        final List<CaseDetails> submittedCases,
        final List<CaseDetails> laSubmittedCases
    ) {
        when(ccdSearchService.searchForAllCasesWithQuery(eq(Draft), any(), any(), anyString()))
            .thenReturn(draftCases);
        when(ccdSearchService.searchForAllCasesWithQuery(eq(Submitted), any(), any(), anyString()))
            .thenReturn(submittedCases);
        when(ccdSearchService.searchForAllCasesWithQuery(eq(LaSubmitted), any(), any(), anyString()))
            .thenReturn(laSubmittedCases);
    }

    private CaseDetails caseDetails(final Long id, final String applicantEmail, final State state) {
        return CaseDetails.builder()
            .id(id)
            .state(state.name())
            .data(Map.of(APPLICANT_1_EMAIL, applicantEmail))
            .build();
    }

    private void mockConvertedCaseDetails(final CaseDetails caseDetails) {
        final uk.gov.hmcts.ccd.sdk.api.CaseDetails<CaseData, State> convertedCaseDetails =
            new uk.gov.hmcts.ccd.sdk.api.CaseDetails<>();
        convertedCaseDetails.setData(caseData());

        when(caseDetailsConverter.convertToCaseDetailsFromReformModel(caseDetails))
            .thenReturn(convertedCaseDetails);
    }
}
