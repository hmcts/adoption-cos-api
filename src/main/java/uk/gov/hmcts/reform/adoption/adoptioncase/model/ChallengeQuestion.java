package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;

@Data
@Builder
@SuppressWarnings({"PMD.AvoidInstantiatingObjectsInLoops"})
public class ChallengeQuestion {

    @CCD
    @JsonProperty("case_type_id")
    private String caseTypeId;

    @CCD
    @JsonProperty("question_text")
    private String questionText;

    @CCD
    @JsonProperty("answer_field_type")
    private FieldType answerFieldType;

    @CCD
    @JsonProperty("challenge_question_id")
    private String challengeQuestionId;

    @CCD
    @JsonProperty("answer_field")
    private String answerField;

    @CCD
    @JsonProperty("question_id")
    private String questionId;
}
