package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasRole;

/**
 * ENUM class for defining the IDAM roles for ADOPTION used for mapping with Access Profiles later.
 */
@AllArgsConstructor
@Getter
public enum UserRolesForAccessProfiles implements HasRole {
    ADOPTION_GENERIC("idam:caseworker-adoption", "RU"),
    CASE_WORKER("idam:caseworker-adoption-caseworker", "RU"),
    COURT_ADMIN("idam:caseworker-adoption-courtadmin", "R"),
    LEGAL_ADVISOR("idam:caseworker-adoption-la", "R"),
    DISTRICT_JUDGE("idam:caseworker-adoption-judge", "RU"),
    SUPER_USER("idam:caseworker-adoption-superuser", "CRUD"),
    SOLICITOR("idam:caseworker-adoption-solicitor", "R"),
    CITIZEN("idam:citizen", "CRUD"),
    CREATOR("[CREATOR]", "CRU"),
    CASE_WORKER_SYSTEM_UPDATE("idam:caseworker-adoption-systemupdate", "CRU");

    @JsonValue
    private final String role;
    private final String caseTypePermissions;

}