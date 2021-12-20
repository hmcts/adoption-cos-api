package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum ApplicationType implements HasLabel {

    @JsonProperty("nonMolestationOrder")
    NON_MOLESTATION_ORDER("Non-molestation Order"),

    @JsonProperty("occupationalOrder")
    OCCUPATIONAL_ORDER("Occupational Order");

    private final String label;

    public boolean isNonMolestationOrder() {
        return NON_MOLESTATION_ORDER.name().equalsIgnoreCase(this.name());
    }

    public boolean isOccupationalOrder() {
        return OCCUPATIONAL_ORDER.name().equalsIgnoreCase(this.name());
    }
}
