package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum SiblingPoType implements HasLabel {

    @JsonProperty("Adoption order")
    ADOPTION_ORDER("Adoption order"),

    @JsonProperty("Care order")
    CARE_ORDER("Care order"),

    @JsonProperty("Contact order")
    CONTACT_ORDER("Contact order"),

    @JsonProperty("Freeing order")
    FREEING_ORDER("Freeing order"),

    @JsonProperty("Placement order")
    PLACEMENT_ORDER("Placement order"),

    @JsonProperty("Supervision order")
    SUPERVIS_ORDER("Supervision order"),

    @JsonProperty("Other")
    OTHER("Other");

    private final String label;

}
