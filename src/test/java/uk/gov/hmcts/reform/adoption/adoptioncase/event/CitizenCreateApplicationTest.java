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
        citizenCreateApplication.aboutToSubmit(caseDetails,caseDetails);
        assertThat(caseDetails.getData().getDssQuestion1()).isEqualTo("First Name");
        assertThat(caseDetails.getData().getDssAnswer3()).isEqualTo("case_data.childrenDateOfBirth");
    }

    private CaseDetails<CaseData, State> getCaseDetails() {
        return CaseDetails.<CaseData, State>builder()
            .data(caseData())
            .id(1L)
            .build();
    }
}
