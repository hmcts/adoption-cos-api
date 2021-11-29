package uk.gov.hmcts.reform.adoption.bulkaction.ccd.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.bulkaction.ccd.BulkActionPageBuilder;
import uk.gov.hmcts.reform.adoption.bulkaction.ccd.BulkActionState;
import uk.gov.hmcts.reform.adoption.bulkaction.data.BulkActionCaseData;
import uk.gov.hmcts.reform.adoption.divorcecase.model.UserRole;

import static uk.gov.hmcts.reform.adoption.bulkaction.ccd.BulkActionState.Dropped;
import static uk.gov.hmcts.reform.adoption.bulkaction.ccd.BulkActionState.Listed;
import static uk.gov.hmcts.reform.adoption.bulkaction.ccd.BulkActionState.Pronounced;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.UserRole.CASE_WORKER;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.UserRole.SYSTEMUPDATE;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.access.Permissions.CREATE_READ_UPDATE;

@Component
@Slf4j
public class SystemUpdateCase implements CCDConfig<BulkActionCaseData, BulkActionState, UserRole> {

    public static final String SYSTEM_UPDATE_BULK_CASE = "system-update-case";

    @Override
    public void configure(final ConfigBuilder<BulkActionCaseData, BulkActionState, UserRole> configBuilder) {
        new BulkActionPageBuilder(configBuilder
            .event(SYSTEM_UPDATE_BULK_CASE)
            .forStates(Listed, Pronounced, Dropped)
            .name("System update case")
            .description("System update case")
            .explicitGrants()
            .grant(CREATE_READ_UPDATE, CASE_WORKER, SYSTEMUPDATE));
    }
}
