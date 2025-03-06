package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.Event;
import uk.gov.hmcts.reform.adoption.adoptioncase.event.CaseWorkerAllocateJudge;
import uk.gov.hmcts.reform.adoption.adoptioncase.event.EventTest;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.adoption.adoptioncase.event.CaseWorkerAllocateJudge.CASEWORKER_ALLOCATE_JUDGE;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@ExtendWith(MockitoExtension.class)
public class CaseWorkerAllocateJudgeTest extends EventTest {

    @InjectMocks
    CaseWorkerAllocateJudge caseWorkerAllocateJudge;


    /**
     * Test Scenario: Should be able to ADD Configuration to the CCD Config Builder.
     */
    @Test
    void shouldAddConfigurationToConfigBuilder() {
        final ConfigBuilderImpl<CaseData, State, UserRole> configBuilder = createCaseDataConfigBuilder();
        caseWorkerAllocateJudge.configure(configBuilder);
        assertThat(getEventsFrom(configBuilder).values())
            .extracting(Event::getId)
            .contains(CASEWORKER_ALLOCATE_JUDGE);
    }

    private CaseDetails<CaseData, State> getCaseDetails() {
        final var details = new CaseDetails<CaseData, State>();
        final var data = caseData();
        data.setAllocatedJudge("TEST");
        details.setData(data);
        details.setId(1L);
        return details;
    }

    @Test
    void shouldSuccessfullyAllocateJudgeToCase() {
        var caseDetails = getCaseDetails();
        var result = caseWorkerAllocateJudge.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getAllocatedJudge()).isNotNull();
    }
}
