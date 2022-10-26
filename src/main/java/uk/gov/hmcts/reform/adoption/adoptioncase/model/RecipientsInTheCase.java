package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum RecipientsInTheCase implements HasLabel {

    @CCD(showCondition = "birthMother=\"*\"")
    @JsonProperty("respondentBirthMother")
    RESPONDENT_MOTHER("Respondent(birth mother)"),

    @CCD(showCondition = "birthFather=\"*\"")
    @JsonProperty("respondentBirthFather")
    RESPONDENT_FATHER("Respondent(birth father)");

    private String label;
}
