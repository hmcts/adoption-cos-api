package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasRole;

@AllArgsConstructor
@Getter
public enum UserRole implements HasRole {
    ADOPTION_GENERIC("caseworker-adoption", "RU"),
    CASE_WORKER("caseworker-adoption-caseworker", "RU"),
    COURT_ADMIN("caseworker-adoption-courtadmin", "R"),
    LEGAL_ADVISOR("caseworker-adoption-la", "R"),
    DISTRICT_JUDGE("caseworker-adoption-judge", "RU"),
    SUPER_USER("caseworker-adoption-superuser", "CRUD"),
    SOLICITOR("caseworker-adoption-solicitor", "R"),
    CITIZEN("citizen", "CRUD"),
    CREATOR("[CREATOR]", "CRU"),
    SYSTEM_UPDATE("caseworker-adoption-systemupdate", "CRUD");//TODO REMOVE DELETE ACCESS

    @JsonValue
    private final String role;
    private final String caseTypePermissions;

}
