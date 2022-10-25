package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum Attendance implements HasLabel {

    @JsonProperty("applicants")
    APPLICANTS_ATTENDANCE("Applicants attendance"),

    @JsonProperty("child")
    CHILD_ATTENDANCE("Child attendance"),

    @JsonProperty("localAuthorityAttendance")
    LOCAL_AUTHORITY_ATTENDANCE("Local authority attendance"),

    @JsonProperty("respondents")
    BIRTH_PARENTS_ATTENDANCE("Birth parents attendance)");

    private final String label;
}
