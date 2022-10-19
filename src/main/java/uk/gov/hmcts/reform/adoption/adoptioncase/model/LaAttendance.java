package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum LaAttendance implements HasLabel {

    @JsonProperty("laAttendanceAttend")
    LA_ATTENDANCE_ATTEND("A representative of the Local Authority shall attend the next hearing.");

    private final String label;
}
