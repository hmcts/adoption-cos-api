package uk.gov.hmcts.reform.adoption.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum DocumentCategory implements HasLabel {
    @JsonProperty("applicationDocuments")
    APPLICATION_DOCUMENTS("Application documents", "For example, birth or death certificates."),

    @JsonProperty("courtOrders")
    COURT_ORDERS("Court orders","For example placement and care orders."),

    @JsonProperty("reports")
    REPORTS("Reports","ABC"),

    @JsonProperty("statements")
    STATEMENTS("Statements","dfdsfds"),

    @JsonProperty("correspondence")
    CORRESPONDENCE("Correspondence","dfsdvcsd"),

    @JsonProperty("additionalDocuments")
    ADDITIONAL_DOCUMENTS("Additional documents","efdwef");

    private final String label;
    private final String hint;
}
