package uk.gov.hmcts.reform.adoption.adoptioncase.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.Event;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static uk.gov.hmcts.reform.adoption.adoptioncase.event.MigrateCase.MIGRATE_CASE;

@ExtendWith(MockitoExtension.class)
public class MigrateCaseTest extends EventTest {

    @InjectMocks
    private MigrateCase migrateCase;

    @Test
    void shouldSetupMigrateCaseEvent() {
        final ConfigBuilderImpl<CaseData, State, UserRole> configBuilder = createCaseDataConfigBuilder();
        migrateCase.configure(configBuilder);

        assertThat(getEventsFrom(configBuilder).values())
            .extracting(Event::getId)
            .contains(MIGRATE_CASE);
    }

    @Test
    void shouldPerformMigrationWithGivenIdAndNullifyMigrationId() {
        CaseDetails<CaseData, State> caseDetails = CaseDetails.<CaseData, State>builder()
            .data(CaseData.builder()
                      .migrationId("ADOP-log")
                      .build())
            .id(1L)
            .build();

        AboutToStartOrSubmitResponse<CaseData, State> resp = migrateCase.aboutToSubmit(caseDetails, null);

        assertThat(resp.getData().getMigrationId()).isNullOrEmpty();
    }

    @Test
    void shouldThrowExceptionWhenMigrationRunWithInvalidId() {
        CaseDetails<CaseData, State> caseDetails = CaseDetails.<CaseData, State>builder()
            .data(CaseData.builder()
                      .migrationId("THIS-MIGRATION-ID-IS-NOT-VALID")
                      .build())
            .id(1L)
            .build();

        assertThrows("No migration mapped to THIS-MIGRATION-ID-IS-NOT-VALID", RuntimeException.class,
                     () -> migrateCase.aboutToSubmit(caseDetails, null)
        );
    }

}
