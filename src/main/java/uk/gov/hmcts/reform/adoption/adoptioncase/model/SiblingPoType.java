package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum SiblingPoType implements HasLabel {

    @JsonProperty("adoptionOrder")
    ADOPTION_ORDER("adoptionOrder"),

    @JsonProperty("careOrder")
    CARE_ORDER("careOrder"),

    @JsonProperty("contactOrder")
    CONTACT_ORDER("contactOrder"),

    @JsonProperty("freeingOrder")
    FREEING_ORDER("freeingOrder"),

    @JsonProperty("placementOrder")
    PLACEMENT_ORDER("placementOrder"),

    @JsonProperty("superVisOrder")
    SUPERVIS_ORDER("superVisOrder"),

    @JsonProperty("other")
    OTHER("other");

    private final String label;

}
