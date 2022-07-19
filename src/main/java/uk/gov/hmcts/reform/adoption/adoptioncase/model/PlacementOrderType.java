package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum PlacementOrderType implements HasLabel {

    @JsonProperty("Adoption Order")
    ADOPTION_ORDER("Adoption Order"),

    @JsonProperty("Care Order")
    CARE_ORDER("Care Order"),

    @JsonProperty("Contact Order")
    CONTACT_ORDER("Contact Order"),

    @JsonProperty("Freeing Order")
    FREEING_ORDER("Freeing Order"),

    @JsonProperty("Placement Order")
    PLACEMENT_ORDER("Placement Order"),

    @JsonProperty("Supervision Order")
    SUPERVIS_ORDER("Supervision Order"),

    @JsonProperty("Other")
    OTHER("Other");

    private final String label;

}
