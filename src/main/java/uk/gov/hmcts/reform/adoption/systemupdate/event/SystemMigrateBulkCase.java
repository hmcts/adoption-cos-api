package uk.gov.hmcts.reform.adoption.systemupdate.event;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.bulkaction.ccd.BulkActionState;
import uk.gov.hmcts.reform.adoption.bulkaction.data.BulkActionCaseData;
import uk.gov.hmcts.reform.adoption.divorcecase.model.UserRole;

import static uk.gov.hmcts.reform.adoption.divorcecase.model.UserRole.SYSTEMUPDATE;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.access.Permissions.CREATE_READ_UPDATE;

@Component
public class SystemMigrateBulkCase implements CCDConfig<BulkActionCaseData, BulkActionState, UserRole> {

    public static final String SYSTEM_MIGRATE_BULK_CASE = "system-migrate-bulk-case";

    @Override
    public void configure(final ConfigBuilder<BulkActionCaseData, BulkActionState, UserRole> configBuilder) {
        configBuilder
            .event(SYSTEM_MIGRATE_BULK_CASE)
            .forAllStates()
            .name("Migrate bulk case data")
            .description("Migrate bulk case data to the latest version")
            .grant(CREATE_READ_UPDATE, SYSTEMUPDATE);
    }
}
