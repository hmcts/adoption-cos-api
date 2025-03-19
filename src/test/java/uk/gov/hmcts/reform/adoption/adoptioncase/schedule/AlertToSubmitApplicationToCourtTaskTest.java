package uk.gov.hmcts.reform.adoption.adoptioncase.schedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.service.CcdSearchService;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.adoption.notification.LocalAuthorityAlertToSubmitToCourt;
import uk.gov.hmcts.reform.adoption.systemupdate.CaseDetailsConverter;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.idam.client.models.User;
import uk.gov.hmcts.reform.idam.client.models.UserDetails;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Draft;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@ExtendWith(SpringExtension.class)
class AlertToSubmitApplicationToCourtTaskTest {

    @InjectMocks
    private AlertToSubmitApplicationToCourtTask alertToSubmitApplicationToCourtTask;

    @Mock
    private CcdSearchService ccdSearchService;

    @Mock
    private IdamService idamService;

    @Mock
    private AuthTokenGenerator authTokenGenerator;

    @Mock
    private LocalAuthorityAlertToSubmitToCourt localAuthorityAlertToSubmitToCourt;

    @Mock
    private CaseDetailsConverter caseDetailsConverter;

    public static final String SYSTEM_UPDATE_AUTH_TOKEN = "Bearer SystemUpdateAuthToken";

    public static final String SERVICE_AUTHORIZATION = "ServiceAuthorization";


    @BeforeEach
    void setUp() {
        User user = new User(SYSTEM_UPDATE_AUTH_TOKEN, UserDetails.builder().build());
        when(idamService.retrieveSystemUpdateUserDetails()).thenReturn(user);
        when(authTokenGenerator.generate()).thenReturn(SERVICE_AUTHORIZATION);
    }

    @Test
    void run() {
        final CaseDetails caseDetails1 = mock(CaseDetails.class);
        final CaseDetails caseDetails2 = mock(CaseDetails.class);
        final CaseDetails caseDetails3 = mock(CaseDetails.class);
        when(caseDetails1.getCreatedDate()).thenReturn(LocalDateTime.now());
        when(caseDetails2.getCreatedDate()).thenReturn(LocalDateTime.now());
        when(caseDetails3.getCreatedDate()).thenReturn(LocalDateTime.now());
        when(caseDetails1.getState()).thenReturn(String.valueOf(State.Submitted));
        when(caseDetails2.getState()).thenReturn(String.valueOf(Draft));
        when(caseDetails3.getState()).thenReturn(String.valueOf(State.Submitted));

        //Return all 3 cases as casesNeedingReminder
        final List<CaseDetails> caseDetailsList = List.of(caseDetails1, caseDetails2, caseDetails3);
        when(ccdSearchService.searchForAllCasesWithQuery(any(), any(), any(), anyString()))
            .thenReturn(caseDetailsList);

        //Use the same CCD Case Details for each case needing reminder
        final uk.gov.hmcts.ccd.sdk.api.CaseDetails<CaseData, State> ccdCaseDetails = new uk.gov.hmcts.ccd.sdk.api.CaseDetails<>();
        ccdCaseDetails.setData(caseData());
        when(caseDetailsConverter.convertToCaseDetailsFromReformModel(any(CaseDetails.class))).thenReturn(ccdCaseDetails);

        alertToSubmitApplicationToCourtTask.run();
        verify(localAuthorityAlertToSubmitToCourt, times(3)).sendLocalAuthorityAlertToSubmitToCourt(
            any(CaseData.class),
            any(Long.class));

    }
}
