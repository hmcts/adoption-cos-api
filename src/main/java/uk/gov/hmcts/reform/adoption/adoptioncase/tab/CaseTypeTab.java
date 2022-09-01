package uk.gov.hmcts.reform.adoption.adoptioncase.tab;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.CASE_WORKER;

@Component
public class CaseTypeTab implements CCDConfig<CaseData, State, UserRole> {

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        buildStateTab(configBuilder);
        buildConfidentialTab(configBuilder);
        buildDocumentsTab(configBuilder);
    }

    public void buildStateTab(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.tab("state", "State")
            .forRoles(CASE_WORKER)
            .label("LabelState", null, "#### Case State:  ${[STATE]}");
    }

    private void buildConfidentialTab(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.tab("Confidential", "Confidential Details")
            .forRoles(CASE_WORKER)
            .field("applicant1PhoneNumber")
            .field("applicant1EmailAddress")
            .field("applyingWith")
            .field("childrenFirstName")
            .field("childrenLastName");
    }

    private void buildDocumentsTab(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.tab("documents", "Documents")
            .forRoles(CASE_WORKER)
            .field(CaseData::getDocumentsGenerated)
            .field(CaseData::getApplicant1DocumentsUploaded)
            .field(CaseData::getDocumentsUploaded);
    }
}
