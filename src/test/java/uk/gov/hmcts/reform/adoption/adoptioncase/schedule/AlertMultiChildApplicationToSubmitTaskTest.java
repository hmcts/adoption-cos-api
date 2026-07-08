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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Draft;
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
    void shouldSendReminderForDraftCasesWithSameApplicantEmail() {
        final CaseDetails caseDetails1 = mock(CaseDetails.class);
        final CaseDetails caseDetails2 = mock(CaseDetails.class);
        final CaseDetails caseDetails3 = mock(CaseDetails.class);

        when(caseDetails1.getState()).thenReturn(String.valueOf(State.Submitted));
        when(caseDetails2.getState()).thenReturn(String.valueOf(Draft));
        when(caseDetails3.getState()).thenReturn(String.valueOf(State.Submitted));
        final List<CaseDetails> caseDetailsList = List.of(caseDetails1, caseDetails2, caseDetails3);
        when(ccdSearchService.searchForAllCasesWithQuery(any(), any(), any(), anyString()))
            .thenReturn(caseDetailsList);
        final uk.gov.hmcts.ccd.sdk.api.CaseDetails<CaseData, State> caseDetails4 = new uk.gov.hmcts.ccd.sdk.api.CaseDetails<>();
        caseDetails4.setData(caseData());
        when(caseDetailsConverter.convertToCaseDetailsFromReformModel(any(CaseDetails.class))).thenReturn(caseDetails4);

        alertMultiChildApplicationToSubmitTask.run();
        verify(multiChildSubmitAlertEmailNotification, times(1)).sendToApplicants(
            any(CaseData.class),
            any(Long.class));
    }
}
