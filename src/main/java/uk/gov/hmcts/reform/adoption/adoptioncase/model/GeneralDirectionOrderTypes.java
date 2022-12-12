package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;


@AllArgsConstructor
@Getter
public enum GeneralDirectionOrderTypes implements HasLabel {

    @JsonProperty("generalDirectionProductionOrder")
    GENERAL_DIRECTION_PRODUCTION_ORDER("Production order"),

    @JsonProperty("generalDirectionDisclosureOfAddress")
    GENERAL_DIRECTION_DISCLOSURE_OF_ADDRESS("Disclosure of address");


    private final String label;
}
