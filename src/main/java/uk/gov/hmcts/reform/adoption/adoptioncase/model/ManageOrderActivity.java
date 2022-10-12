package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum ManageOrderActivity implements HasLabel {

    @JsonProperty("createOrder")
    CREATE_ORDER("Create new order"),

    @JsonProperty("editOrder")
    EDIT_ORDER("Edit draft order");

    private final String label;

}
