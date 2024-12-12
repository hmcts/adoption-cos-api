package uk.gov.hmcts.reform.adoption.adoptioncase.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.ccd.sdk.api.Event;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.adoption.adoptioncase.event.ManageCaseTtl.MANAGE_CASE_TTL;

@ExtendWith(MockitoExtension.class)
class ManageCaseTtlTest extends EventTest {

    @InjectMocks
    private ManageCaseTtl manageCaseTtl;

    @Test
    void shouldSetUpManageCaseTtlEvent() {
        final ConfigBuilderImpl<CaseData, State, UserRole> configBuilder = createCaseDataConfigBuilder();

        manageCaseTtl.configure(configBuilder);

        assertThat(getEventsFrom(configBuilder).values())
            .extracting(Event::getId)
            .contains(MANAGE_CASE_TTL);
    }
}
