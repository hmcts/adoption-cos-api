package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum HearingNotices implements HasLabel {

    @JsonProperty("listForFirstHearing")
    LIST_FOR_FIRST_HEARING("List for first hearing"),

    @JsonProperty("listForFurtherHearings")
    LIST_FOR_FURTHER_HEARINGS("List for further hearings"),

    @JsonProperty("hearingDateToBeSpecifiedInTheFuture")
    HEARING_DATE_TO_BE_SPECIFIED_IN_THE_FUTURE("Hearing date to be specified in the future");

    private final String label;
}
