package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum ChildAttendance implements HasLabel {

    @JsonProperty("childAttendanceExcused")
    CHILD_ATTENDANCE_EXCUSED("The attendance of the child is excused for all hearings."),

    @JsonProperty("childAttendanceNotBeBrought")
    CHILD_ATTENDANCE_NOT_BE_BROUGHT("The child will not be brought to this hearing.");

    private final String label;
}
