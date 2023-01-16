package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.event.CitizenCreateApplication;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.ccd.client.CoreCaseDataApi;
import uk.gov.hmcts.reform.idam.client.models.User;
import uk.gov.hmcts.reform.idam.client.models.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_AUTHORIZATION_TOKEN;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@ExtendWith(MockitoExtension.class)
class CitizenCreateApplicationTest {

    @InjectMocks
    private CitizenCreateApplication citizenCreateApplication;

    @Mock
    CoreCaseDataApi coreCaseDataApi;

    @Mock
    private IdamService idamService;

    @Mock
    private AuthTokenGenerator authTokenGenerator;

    private static final String AUTH_TOKEN = "Bearer someAuthToken";
    public static final String SOME_SERVICE_AUTHORIZATION_TOKEN = "ServiceToken";



    @Test
    @DisplayName("Testing submitted event for citizen case creation with supplementary data")
    void testing_citizen_submission_with_supplementaryData_submitted() {
        var caseDetails = getCaseDetails();
        when(idamService.retrieveSystemUpdateUserDetails()).thenReturn(getCaseworkerUser());
        when(authTokenGenerator.generate()).thenReturn(SOME_SERVICE_AUTHORIZATION_TOKEN);
        citizenCreateApplication.submitted(caseDetails,caseDetails);
        verify(coreCaseDataApi, times(1)).submitSupplementaryData(any(), any(),any(),any());
    }

    @Test
    @DisplayName("Testing submitted event for citizen case creation with dss meta data")
    void testing_citizen_submission_with_dssData_aboutToSubmit() {
        var caseDetails = getCaseDetails();
        citizenCreateApplication.aboutToSubmit(caseDetails,caseDetails);
        assertThat(caseDetails.getData().getDssQuestion1()).isEqualTo("Full Name");
        assertThat(caseDetails.getData().getDssAnswer2()).isEqualTo("case_data.childrenDateOfBirth");
    }

    private CaseDetails<CaseData, State> getCaseDetails() {
        return CaseDetails.<CaseData, State>builder()
            .data(caseData())
            .id(1L)
            .build();
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
