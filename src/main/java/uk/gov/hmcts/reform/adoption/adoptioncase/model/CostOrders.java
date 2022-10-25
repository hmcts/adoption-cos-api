package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum CostOrders implements HasLabel {

    @JsonProperty("noOrderForCosts")
    NO_ORDERS_FOR_COST("No order for costs");

    private final String label;
}
