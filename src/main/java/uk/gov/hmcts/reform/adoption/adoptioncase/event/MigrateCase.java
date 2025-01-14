package uk.gov.hmcts.reform.adoption.adoptioncase.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

import java.util.Map;
import java.util.function.Consumer;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.AwaitingPayment;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Draft;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.LaSubmitted;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Submitted;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.SYSTEM_UPDATE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions.CREATE_READ_UPDATE_DELETE;

@Slf4j
@Component
public class MigrateCase implements CCDConfig<CaseData, State, UserRole> {

    public static final String MIGRATE_CASE = "migrate-case";

    // Note - keep "ADOP-log", it is useful for triggering an "event" without updating data
    private final Map<String, Consumer<CaseDetails<CaseData, State>>> migrations = Map.of(
        "ADOP-log", this::runLog,
        "ADOP-2555", this::run2555,
        "ADOP-2555-suspend", this::run2555
    );

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {

        configBuilder
            .event(MIGRATE_CASE)
            .forStates(Draft, AwaitingPayment, Submitted, LaSubmitted)
            .name("Migrate case")
            .description("Migrate case")
            .retries(120, 120)
            .grant(CREATE_READ_UPDATE_DELETE, SYSTEM_UPDATE)
            .aboutToSubmitCallback(this::aboutToSubmit);
    }

    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(CaseDetails<CaseData, State> details,
            CaseDetails<CaseData, State> beforeDetails) {
        CaseData data = details.getData();
        String migrationId = data.getMigrationId();
        Long id = details.getId();

        log.info("Migration {id = {}, case reference = {}} started", migrationId, id);

        if (!migrations.containsKey(migrationId)) {
            throw new RuntimeException("No migration mapped to " + migrationId);
        }

        migrations.get(migrationId).accept(details);

        log.info("Migration {id = {}, case reference = {}} finished", migrationId, id);

        State state = details.getState();

        data.setMigrationId(null);
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
                .data(data)
                .state(state)
                .build();
    }

    private void runLog(CaseDetails<CaseData, State> caseDetails) {
        log.info("Logging migration on case {}", caseDetails.getId());
    }

    private void run2555(CaseDetails<CaseData, State> caseDetails) {
        //Empty function due to all migration logic being in the migration tool
    }
}
