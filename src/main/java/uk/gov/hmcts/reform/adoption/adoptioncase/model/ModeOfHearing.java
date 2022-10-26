package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum ModeOfHearing implements HasLabel {

    @JsonProperty("setModeOfHearing")
    SET_MODE_OF_HEARING("Set mode of hearing");

    private final String label;
}
