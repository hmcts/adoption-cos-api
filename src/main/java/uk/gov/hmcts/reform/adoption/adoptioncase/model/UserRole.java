package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasRole;

@AllArgsConstructor
@Getter
public enum UserRole implements HasRole {
    DOMESTIC_ABUSE_GENERIC("caseworker-domesticabuse", "CRU"),
    CASE_WORKER("caseworker-domesticabuse-caseworker", "CRU"),
    COURT_ADMIN("caseworker-domesticabuse-courtadmin", "CRU"),
    LEGAL_ADVISOR("caseworker-domesticabuse-la", "CRU"),
    DISTRICT_JUDGE("caseworker-domesticabuse-judge", "CRU"),
    SUPER_USER("caseworker-domesticabuse-superuser", "CRU"),
    SYSTEM_UPDATE("caseworker-domesticabuse-systemupdate", "CRU"),
    SOLICITOR("caseworker-domesticabuse-solicitor", "CRU"),
    APPLICANT_SOLICITOR("[APPLICANTSOLICITOR]", "CRU");

    @JsonValue
    private final String role;
    private final String caseTypePermissions;

}
