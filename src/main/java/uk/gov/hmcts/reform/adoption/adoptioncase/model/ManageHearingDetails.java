package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;

import java.time.LocalDateTime;
import java.util.SortedSet;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedRadioList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManageHearingDetails {


    @CCD(
        label = "Type of hearing",
        hint = "For example, a first hearing"

    )
    private String typeOfHearing;

    @CCD(
        label = "Hearing date & time"
    )
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime hearingDateAndTime;

    @CCD(
        label = "Judge"

    )
    private String judge;

    @CCD(
        label = "Court"
    )
    private String court;


    @CCD(
        label = "is an interpreter needed?",
        hint = "Given details of the interpretation needed for example, sign language or Hindu translator"
    )
    private String isInterpreterNeeded;

    @CCD(
        label = "Method of hearing",
        typeOverride = FixedRadioList,
        typeParameterOverride = "MethodOfHearing"
    )
    private MethodOfHearing methodOfHearing;


    @CCD(
        label = "Accessibility requirements",
        typeOverride = FieldType.TextArea
    )
    private String accessibilityRequirements;

    @CCD(
        label = "Hearing directions",
        access = {DefaultAccess.class},
        typeOverride = FieldType.MultiSelectList,
        typeParameterOverride = "HearingDirections"
    )
    private SortedSet<HearingDirections> hearingDirections;

    @CCD(label = " Length of hearing",
         hint = "Insert the length of the hearing in hours and minutes, for example 2 hours 30 minutes")
    private String lengthOfHearing;

}
