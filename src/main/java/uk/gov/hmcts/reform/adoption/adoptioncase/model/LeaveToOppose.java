package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum LeaveToOppose implements HasLabel {

    @JsonProperty("leaveToOppose")
    LEAVE_TO_OPPOSE("Leave to oppose");

    private final String label;
}
