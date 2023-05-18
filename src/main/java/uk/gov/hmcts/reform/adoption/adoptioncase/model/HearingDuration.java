package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum HearingDuration implements HasLabel {

    @JsonProperty("numberOfDays")
    NUMBER_OF_DAYS("Set number of days"),

    @JsonProperty("hoursAndMinutes")
    HOURS_AND_MINUTES("Set number of hours and  minutes");

    private String label;
}
