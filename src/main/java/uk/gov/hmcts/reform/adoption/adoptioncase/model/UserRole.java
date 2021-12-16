package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasRole;

@AllArgsConstructor
@Getter
public enum UserRole implements HasRole {

    ADOPTION("caseworker-adoption", "CRU"),
    COURT_ADMIN("caseworker-adoption-courtadmin", "CRU"),
    SOLICITOR("caseworker-adoption-solicitor", "CRU"),
    CASE_WORKER("caseworker-adoption-caseworker", "CRU"),
    JUDGE("caseworker-adoption-judge", "CRU"),
    ADOPTION_LA("caseworker-adoption-la", "CRU"),
    SUPER_USER("caseworker-adoption-superuser", "CRU");

    @JsonValue
    private final String role;
    private final String caseTypePermissions;

}
