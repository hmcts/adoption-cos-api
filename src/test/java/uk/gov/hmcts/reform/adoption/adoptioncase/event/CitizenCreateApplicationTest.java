package uk.gov.hmcts.reform.adoption.adoptioncase.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@ExtendWith(MockitoExtension.class)
class CitizenCreateApplicationTest extends EventTest {

    @InjectMocks
    private CitizenCreateApplication citizenCreateApplication;

    @Test
    @DisplayName("Testing submitted event for citizen case creation with dss meta data")
    void testingCitizenSubmissionWith_dssDataAboutToSubmit() {
        var caseDetails = getCaseDetails();
        var callbackResponse = citizenCreateApplication.aboutToSubmit(caseDetails, caseDetails);
        var callbackData = callbackResponse.getData();

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

    @Test
    @DisplayName("Testing draft status is set in about to submit method")
    void testingCitizenSubmission_stateSetToDraft() {
        var caseDetails = getCaseDetails();
        assertThat(caseDetails).isNotNull();
        assertThat(caseDetails.getData()).isNotNull();
        assertThat(caseDetails.getState()).isEqualTo(State.Draft);
    }

    @Test
    @DisplayName("Testing getCaseDetails method")
    void testingCitizenSubmission_caseFieldsConstants() {
        var caseDetails = getCaseDetails();
        var caseData = caseDetails.getData();
        assertThat(caseDetails).isNotNull();
        assertThat(caseDetails.getData()).isNotNull();
        assertThat(caseData.getTypeOfAdoption()).isEqualTo(CaseFieldsConstants.TYPE_OF_ADOPTION);
    }


    @ParameterizedTest
    @CsvSource({
        "1234567890123456, 1234-5678-9012-3456",
        "1234,             0000-0000-0000-1234",
        "1,                0000-0000-0000-0001",
        "0,                0000-0000-0000-0000"
    })
    @DisplayName("Testing hyphenated case reference formatting")
    void shouldFormatHyphenatedCaseRef(long caseId, String expectedRef) {
        var caseDetails = CaseDetails.<CaseData, State>builder()
            .data(caseData())
            .id(caseId)
            .build();

        var response = citizenCreateApplication.aboutToSubmit(caseDetails, caseDetails);

        assertThat(response.getData().getHyphenatedCaseRef()).isEqualTo(expectedRef);
    }


    private CaseDetails<CaseData, State> getCaseDetails() {
        return CaseDetails.<CaseData, State>builder()
            .data(caseData())
            .id(1234567890123456L)
            .build();
    }
}
