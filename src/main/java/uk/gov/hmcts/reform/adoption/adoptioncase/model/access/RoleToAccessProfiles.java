package uk.gov.hmcts.reform.adoption.adoptioncase.model.access;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRolesForAccessProfiles;

/**
 * Contains configure overridden method to implement the mapping of Roles to that of Access Profiles.
 */
@Component
public class RoleToAccessProfiles implements CCDConfig<CaseData, State, UserRolesForAccessProfiles> {

    /**
     * Overridden method to define caseRoleToAccessProfile configuration.
     *
     * @param configBuilder - Application ConfigBuilder with customised UserRolesForAccessProfiles
     */
    @Override
    public void configure(ConfigBuilder<CaseData, State, UserRolesForAccessProfiles> configBuilder) {
        configBuilder.caseRoleToAccessProfile(UserRolesForAccessProfiles.ADOPTION_GENERIC)
            .accessProfiles("caseworker-adoption").build();
        configBuilder.caseRoleToAccessProfile(UserRolesForAccessProfiles.CASE_WORKER)
            .accessProfiles("caseworker-adoption-caseworker", "TTL_profile").readonly().build();
        configBuilder.caseRoleToAccessProfile(UserRolesForAccessProfiles.COURT_ADMIN)
            .accessProfiles("caseworker-adoption-courtadmin").build();
        configBuilder.caseRoleToAccessProfile(UserRolesForAccessProfiles.LEGAL_ADVISOR)
            .accessProfiles("caseworker-adoption-la").build();
        configBuilder.caseRoleToAccessProfile(UserRolesForAccessProfiles.DISTRICT_JUDGE)
            .accessProfiles("caseworker-adoption-judge").build();
        configBuilder.caseRoleToAccessProfile(UserRolesForAccessProfiles.SUPER_USER)
            .accessProfiles("caseworker-adoption-superuser").build();
        configBuilder.caseRoleToAccessProfile(UserRolesForAccessProfiles.SOLICITOR)
            .accessProfiles("caseworker-adoption-solicitor").build();
        configBuilder.caseRoleToAccessProfile(UserRolesForAccessProfiles.CITIZEN)
            .accessProfiles("citizen").build();
        configBuilder.caseRoleToAccessProfile(UserRolesForAccessProfiles.CREATOR)
            .accessProfiles("[CREATOR]").build();
        configBuilder.caseRoleToAccessProfile(UserRolesForAccessProfiles.SYSTEM_UPDATE)
            .accessProfiles("caseworker-adoption-systemupdate", "TTL_profile").build();
        configBuilder.caseRoleToAccessProfile(UserRolesForAccessProfiles.TTL_MANAGER)
            .accessProfiles("TTL_profile").build();
    }
}
