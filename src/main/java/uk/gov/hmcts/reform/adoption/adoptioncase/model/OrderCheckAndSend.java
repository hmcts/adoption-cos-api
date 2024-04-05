package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum OrderCheckAndSend implements HasLabel {

    @JsonProperty("serveTheOrder")
    SERVE_THE_ORDER("Serve the order"),

    @JsonProperty("returnForAmendments")
    RETURN_FOR_AMENDMENTS("Return for amendments");

    private final String label;
}
