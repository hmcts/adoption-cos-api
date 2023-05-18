package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;


@Getter
@AllArgsConstructor
public enum GeneraDirectionsIncluded implements HasLabel {

    @JsonProperty("generalDirectionHearingDelay")
    HEARING_DELAY("Hearing delay"),

    @JsonProperty("generalDirectionFillingBundles")
    FILLING_BUNDLES("Filing bundles"),

    @JsonProperty("generalDirectionFillingPDDocuments")
    FILLING_PD_DOCUMENTS("Filing PD documents"),

    @JsonProperty("generalDirectionBackUpNotice")
    BACKUP_NOTICE("Back up notice"),

    @JsonProperty("generalDirectionWarningNotice")
    WARNING_NOTICE("Warning notice"),

    @JsonProperty("generalDirectionPenalNotice")
    PENAL_NOTICE("Penal notice");

    private final String label;
}
