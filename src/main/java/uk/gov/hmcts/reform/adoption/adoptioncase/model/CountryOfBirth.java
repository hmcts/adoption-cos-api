
package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum CountryOfBirth implements HasLabel {

    @JsonProperty("unitedKingdom")
    UNITED_KINGDOM("United Kingdom, Channel Islands or the Isle of Man"),

    @JsonProperty("outsideTheUK")
    OUTSIDE_THE_UK("Outside the UK");

    private final String label;

}
