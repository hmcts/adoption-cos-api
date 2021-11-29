package uk.gov.hmcts.reform.adoption.bulkaction.ccd;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.bulkaction.data.BulkActionCaseData;
import uk.gov.hmcts.reform.adoption.bulkaction.data.BulkCaseRetiredFields;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

import static uk.gov.hmcts.reform.adoption.bulkaction.ccd.BulkActionState.Created;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.CASE_WORKER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.SUPER_USER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.SYSTEMUPDATE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions.CREATE_READ_UPDATE;

@Component
public class BulkActionCaseTypeConfig implements CCDConfig<BulkActionCaseData, BulkActionState, UserRole> {

    public static final String CASE_TYPE = "A58_BulkAction";
    public static final String JURISDICTION = "ADOPTION";

    @Override
    public void configure(final ConfigBuilder<BulkActionCaseData, BulkActionState, UserRole> configBuilder) {
        configBuilder.addPreEventHook(BulkCaseRetiredFields::migrate);
        configBuilder.setCallbackHost(System.getenv().getOrDefault("CASE_API_URL", "http://adoption-cos-api:4550"));

        configBuilder.caseType(CASE_TYPE, CASE_TYPE, "Handling of child adoption case");
        configBuilder.jurisdiction(JURISDICTION, "Family jurisdiction adoption", "Child adoption");

        configBuilder.grant(Created, CREATE_READ_UPDATE, CASE_WORKER, SUPER_USER, SYSTEMUPDATE);
    }
}
