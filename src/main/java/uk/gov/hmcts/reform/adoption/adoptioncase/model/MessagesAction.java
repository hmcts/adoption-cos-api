package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum MessagesAction implements HasLabel {

    @JsonProperty("sendMessage")
    SEND_A_MESSAGE("Send a message"),

    @JsonProperty("replyMessage")
    REPLY_A_MESSAGE("Reply to a message");

    private final String label;
}
