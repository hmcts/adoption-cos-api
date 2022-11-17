package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum ReasonForAdjournHearing implements HasLabel {

    @JsonProperty("courtOrJudgeUnavailable")
    COURT_OR_JUDGE_UNAVAILABLE("Court or judge unavailable"),

    @JsonProperty("partiesUnavailable")
    PARTIES_UNAVAILABLE("Parties unavailable"),

    @JsonProperty("lateFillingOfDocuments")
    LATE_FILLING_OF_DOCUMENTS("Late filing of documents"),

    @JsonProperty("caseListedOnDatesToAvoid")
    CASE_LISTED_ON_DATES_TO_AVOID("Date listed on 'Dates to avoid'");


    private final String label;
}
