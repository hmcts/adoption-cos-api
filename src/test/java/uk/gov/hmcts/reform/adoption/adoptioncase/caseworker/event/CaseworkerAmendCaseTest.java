package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.ccd.sdk.api.Event;
import uk.gov.hmcts.reform.adoption.adoptioncase.event.EventTest;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseworkerAmendCase.CASEWORKER_AMEND_CASE;

/**
 * Unit Test class targeted for Amend Case Details functionality.
 * CaseworkerAmendCase configuration class will be used as base in all Tests
 */
@ExtendWith(MockitoExtension.class)
public class CaseworkerAmendCaseTest extends EventTest {

    @InjectMocks
    CaseworkerAmendCase caseworkerAmendCase;

    /**
     * Test Scenario: Should be able to ADD Configuration to the CCD Config Builder.
     */
    @Test
    void shouldAddConfigurationToConfigBuilder() {
        final ConfigBuilderImpl<CaseData, State, UserRole> configBuilder = createCaseDataConfigBuilder();
        caseworkerAmendCase.configure(configBuilder);
        assertThat(getEventsFrom(configBuilder).values())
            .extracting(Event::getId)
            .contains(CASEWORKER_AMEND_CASE);
    }
}