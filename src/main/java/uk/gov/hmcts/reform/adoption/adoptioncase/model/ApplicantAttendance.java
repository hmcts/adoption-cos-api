package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum ApplicantAttendance implements HasLabel {

    @JsonProperty("applicantAttendanceAttend")
    APPLICANT_ATTENDANCE_ATTEND("The Applicants are not required to attend the hearing "
                           + "but if they wish to do so, they are requested to advise "
                           + "the Court giving not less than 7 days’ notice so that arrangements "
                           + "can be made to protect the confidentiality of the placement"),

    @JsonProperty("applicantAttendanceNotAttend")
    APPLICANT_ATTENDANCE_NOT_ATTEND("The applicants are NOT to attend the next hearing");

    private final String label;
}
