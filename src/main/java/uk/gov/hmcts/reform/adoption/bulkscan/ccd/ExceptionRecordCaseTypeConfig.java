package uk.gov.hmcts.reform.adoption.bulkscan.ccd;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.bulkscan.data.ExceptionRecord;

@Component
public class ExceptionRecordCaseTypeConfig implements CCDConfig<ExceptionRecord, ExceptionRecordState, UserRole> {

    public static final String CASE_TYPE = "ADOPTION_ExceptionRecord";
    public static final String JURISDICTION = "ADOPTION";

    @Override
    public void configure(final ConfigBuilder<ExceptionRecord, ExceptionRecordState, UserRole> configBuilder) {
        configBuilder.caseType(CASE_TYPE, "Exception record", "Exception record for new law case");
        configBuilder.jurisdiction(JURISDICTION, "Family Adoption", "Manage new law case exception records");
    }
}
