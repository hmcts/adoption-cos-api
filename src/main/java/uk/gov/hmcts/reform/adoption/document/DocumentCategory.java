package uk.gov.hmcts.reform.adoption.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum DocumentCategory implements HasLabel {
    @JsonProperty("applicationDocuments")
    APPLICATION_DOCUMENTS("Application Documents"),

    @JsonProperty("courtOrders")
    COURT_ORDERS("Court orders"),

    @JsonProperty("reports")
    REPORTS("Reports"),

    @JsonProperty("statements")
    STATEMENTS("Statements"),

    @JsonProperty("Correspondence")
    CORRESPONDENCE("Correspondence"),

    @JsonProperty("additionalDocuments")
    ADDITIONAL_DOCUMENTS("Additional documents");

    private final String label;
}
