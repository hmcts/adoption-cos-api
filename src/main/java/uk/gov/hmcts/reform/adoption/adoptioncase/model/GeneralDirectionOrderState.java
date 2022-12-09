package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@AllArgsConstructor
@Getter
public enum GeneralDirectionOrderState implements HasLabel {

    @JsonProperty("generalDirectionCHeckAndSend")
    GENERAL_DIRECTION_CHECK_AND_SEND("Send for check and send"),

    @JsonProperty("generalDirectionSaveDraft")
    GENERAL_DIRECTION_SAVE_DRAFT("Save as draft");

    private final String label;
}
