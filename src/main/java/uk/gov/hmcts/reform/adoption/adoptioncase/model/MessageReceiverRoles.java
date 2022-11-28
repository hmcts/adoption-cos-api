package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum MessageReceiverRoles implements HasLabel {

    @JsonProperty("judge")
    JUDGE("Judge"),

    @JsonProperty("gatekeeper")
    GATEKEEPER("Gatekeeper (legal advisor)"),

    @JsonProperty("localCourtAdmin")
    LOCAL_COURT_ADMIN("Local court admin");

    private final String label;
}
