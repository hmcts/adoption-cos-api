package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum HearingDirections implements HasLabel {

    @JsonProperty("hearingDelayWaring")
    HEARING_DELAY_WARNING("Hearing delay warning"),

    @JsonProperty("backupNotice")
    BACKUP_NOTICE("Backup notice");

    private String label;
}
