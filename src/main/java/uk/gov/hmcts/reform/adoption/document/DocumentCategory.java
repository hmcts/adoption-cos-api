package uk.gov.hmcts.reform.adoption.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum DocumentCategory implements HasLabel {
    @JsonProperty("applicationDocuments")
    APPLICATION_DOCUMENTS("Application documents ${applicant1FirstName}"),

    @JsonProperty("courtOrders")
    COURT_ORDERS("Court orders ${applicant1FirstName} ${applicant1LastName}"),

    @JsonProperty("reports")
    REPORTS("Reports ${applicant2FirstName} ${applicant2LastName}"),

    @JsonProperty("statements")
    STATEMENTS("Statements ${birthMotherFirstName} ${birthMotherLastName}"),

    @JsonProperty("correspondence")
    CORRESPONDENCE("Correspondence ${birthFatherFirstName} ${birthFatherLastName}"),

    @JsonProperty("additionalDocuments")
    ADDITIONAL_DOCUMENTS("Additional documents");

    private final String label;
}
