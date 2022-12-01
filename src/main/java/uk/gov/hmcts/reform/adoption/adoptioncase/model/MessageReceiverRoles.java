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
    GATEKEEPER("Gatekeeper (Legal advisor)"),

    @JsonProperty("localCourtAdmin")
    LOCAL_COURT_ADMIN("Local court admin"),

    @JsonProperty("ctsTeamLeader")
    CTS_TEAM_LEADER("CTS team leader"),

    @JsonProperty("ctsTeamWorker")
    CTS_TEAM_WORKER("CTS team worker");

    private final String label;
}
