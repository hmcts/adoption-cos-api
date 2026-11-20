package uk.gov.hmcts.reform.adoption.adoptioncase.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.ccd.sdk.api.Event;
import uk.gov.hmcts.ccd.sdk.api.Webhook;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.adoption.adoptioncase.event.CitizenUpdateApplication.CITIZEN_UPDATE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.AwaitingPayment;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Draft;

@ExtendWith(MockitoExtension.class)
class CitizenUpdateApplicationTest extends EventTest {

    @InjectMocks
    private CitizenUpdateApplication citizenUpdateApplication;

    @Test
    void shouldConfigureCitizenUpdateEvent() {
        final ConfigBuilderImpl<CaseData, State, UserRole> configBuilder = createCaseDataConfigBuilder();

        citizenUpdateApplication.configure(configBuilder);

        assertThat(getEventsFrom(configBuilder).values())
            .extracting(Event::getId)
            .contains(CITIZEN_UPDATE);

        Event<CaseData, UserRole, State> event = getEventsFrom(configBuilder).get(CITIZEN_UPDATE);
        assertThat(event).isNotNull();
        assertThat(event.getName()).isEqualTo("Adoption case");
        assertThat(event.getDescription()).isEqualTo("Adoption application update");
        assertThat(event.getPreState()).containsExactlyInAnyOrder(Draft, AwaitingPayment);
        assertThat(event.getRetries())
            .containsEntry(Webhook.AboutToStart, "120,120")
            .containsEntry(Webhook.AboutToSubmit, "120,120");
    }
}
