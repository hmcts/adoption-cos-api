package uk.gov.hmcts.reform.adoption.adoptioncase.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.ccd.client.CoreCaseDataApi;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@ExtendWith(MockitoExtension.class)
class CitizenCreateApplicationTest extends EventTest {

    @InjectMocks
    private CitizenCreateApplication citizenCreateApplication;

    @Mock
    CoreCaseDataApi coreCaseDataApi;

    @Mock
    private IdamService idamService;

    @Mock
    private AuthTokenGenerator authTokenGenerator;

    @Test
    @DisplayName("Testing submitted event for citizen case creation with dss meta data")
    void testing_citizen_submission_with_dssData_aboutToSubmit() {
        var caseDetails = getCaseDetails();
        var callbackResponse = citizenCreateApplication.aboutToSubmit(caseDetails, caseDetails);
        var callbackData = callbackResponse.getData();

        assertThat(callbackData.getStatus()).isEqualTo(State.Draft);
        assertThat(callbackData.getTypeOfAdoption()).isEqualTo("Post-placement");
        assertThat(callbackData.getHyphenatedCaseRef()).isEqualTo("1234-5678-9012-3456");
        assertThat(callbackData.getDssQuestion1()).isEqualTo("First Name");
        assertThat(callbackData.getDssQuestion2()).isEqualTo("Last Name");
        assertThat(callbackData.getDssQuestion3()).isEqualTo("Date of Birth");
        assertThat(callbackData.getDssAnswer1()).isEqualTo("case_data.childrenFirstName");
        assertThat(callbackData.getDssAnswer2()).isEqualTo("case_data.childrenLastName");
        assertThat(callbackData.getDssAnswer3()).isEqualTo("case_data.childrenDateOfBirth");
        assertThat(callbackData.getDssHeaderDetails()).isEqualTo("Child Details");
    }

    private CaseDetails<CaseData, State> getCaseDetails() {
        return CaseDetails.<CaseData, State>builder()
            .data(caseData())
            .id(1234567890123456L)
            .build();
    }
}
