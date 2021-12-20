package uk.gov.hmcts.reform.adoption.adoptioncase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.common.AddSystemUpdateRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.DRAFT;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.CASE_WORKER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.COURT_ADMIN;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.DISTRICT_JUDGE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.LEGAL_ADVISOR;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.SOLICITOR;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.SUPER_USER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.SYSTEM_UPDATE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions.CREATE_READ_UPDATE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions.READ;

@Component
public class Adoption implements CCDConfig<CaseData, State, UserRole> {
    public static final String CASE_TYPE = "A58";
    public static final String JURISDICTION = "ADOPTION";

    @Autowired
    private AddSystemUpdateRole addSystemUpdateRole;

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.setCallbackHost(System.getenv().getOrDefault("CASE_API_URL", "http://adoption-cos-api:4550"));
        configBuilder.caseType(CASE_TYPE, CASE_TYPE, "Handling of child adoption case");
        configBuilder.jurisdiction(JURISDICTION, "Family jurisdiction adoption", "Child adoption");

        configBuilder.grant(DRAFT, CREATE_READ_UPDATE, SOLICITOR);
        configBuilder.grant(DRAFT, CREATE_READ_UPDATE, SUPER_USER);
        configBuilder.grant(DRAFT, CREATE_READ_UPDATE, CASE_WORKER);
        configBuilder.grant(DRAFT, CREATE_READ_UPDATE, COURT_ADMIN);
        configBuilder.grant(DRAFT, CREATE_READ_UPDATE, SUPER_USER);

        configBuilder.grant(DRAFT, READ, LEGAL_ADVISOR);
        configBuilder.grant(DRAFT, READ, DISTRICT_JUDGE);

        if (addSystemUpdateRole.isEnvironmentAat()) {
            configBuilder.grant(DRAFT, CREATE_READ_UPDATE, SYSTEM_UPDATE);
        }
    }
}
