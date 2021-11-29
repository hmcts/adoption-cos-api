package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasRole;

@AllArgsConstructor
@Getter
public enum UserRole implements HasRole {

    CASE_WORKER("caseworker-adoption-courtadmin_beta", "CRU"),
    LEGAL_ADVISOR("caseworker-adoption-courtadmin-la", "CRU"),
    SUPER_USER("caseworker-adoption-superuser", "CRU"),
    SYSTEMUPDATE("caseworker-adoption-systemupdate", "CRU"),

    SOLICITOR("caseworker-adoption-solicitor", "CRU"),
    APPLICANT_1_SOLICITOR("[APPONESOLICITOR]", "CRU"),
    APPLICANT_2_SOLICITOR("[APPTWOSOLICITOR]", "CRU"),
    ORGANISATION_CASE_ACCESS_ADMINISTRATOR("caseworker-caa", "CRU"),

    CITIZEN("citizen", "CRU"),
    CREATOR("[CREATOR]", "CRU"),
    APPLICANT_2("[APPLICANTTWO]", "CRU");

    @JsonValue
    private final String role;
    private final String caseTypePermissions;

}
