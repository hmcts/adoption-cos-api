package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasRole;

@AllArgsConstructor
@Getter
public enum UserRole implements HasRole {
    ADOPTION_GENERIC("caseworker-adoption", "D"),
    CASE_WORKER("caseworker-adoption-caseworker", "D"),
    COURT_ADMIN("caseworker-adoption-courtadmin", "D"),
    LEGAL_ADVISOR("caseworker-adoption-la", "D"),
    DISTRICT_JUDGE("caseworker-adoption-judge", "D"),
    SUPER_USER("caseworker-adoption-superuser", "D"),
    SOLICITOR("caseworker-adoption-solicitor", "D"),
    CITIZEN("citizen", "D"),
    CREATOR("[CREATOR]", "D"),
    SYSTEM_UPDATE("caseworker-adoption-systemupdate", "D");//TODO REMOVE DELETE ACCESS

    @JsonValue
    private final String role;
    private final String caseTypePermissions;

}
