package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum OrderStatus implements HasLabel {

    @JsonProperty("openOrder")
    OPEN("Open"),

    @JsonProperty("servedOrder")
    SERVED("Served");

    private String label;

}
