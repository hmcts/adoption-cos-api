package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.SystemUpdateAccess;

import java.time.LocalDateTime;
import java.util.SortedSet;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedRadioList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManageHearingDetails {

    @CCD(
        showCondition = "methodOfHearing=\"never\"",
        displayOrder = 1
    )
    private String hearingId;

    @CCD(
        label = "Type of hearing",
        hint = "For example, a first hearing",
        displayOrder = 2

    )
    private String typeOfHearing;

    @CCD(
        label = "Hearing date & time",
        displayOrder = 3
    )
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime hearingDateAndTime;

    @CCD(
        label = "Judge",
        hint = "This should be the allocated judge, if possible.",
        displayOrder = 4

    )
    private String judge;

    @CCD(
        label = "Court",
        hint = "Enter the full name of the court where the hearing will be held ",
        displayOrder = 5
    )
    private String court;


    @CCD(
        label = "Is an interpreter needed?",
        hint = "Given details of the interpretation needed for example, sign language or Hindu translator",
        displayOrder = 6
    )
    private String isInterpreterNeeded;

    @CCD(
        label = "Method of hearing",
        typeOverride = FixedRadioList,
        typeParameterOverride = "MethodOfHearing",
        displayOrder = 7
    )
    private MethodOfHearing methodOfHearing;


    @CCD(
        label = "Accessibility requirements",
        typeOverride = FieldType.TextArea,
        hint = "List any accessibility requirements needed such as disabled access",
        displayOrder = 8
    )
    private String accessibilityRequirements;

    @CCD(
        label = "Hearing directions",
        access = {DefaultAccess.class},
        typeOverride = FieldType.MultiSelectList,
        typeParameterOverride = "HearingDirections",
        displayOrder = 9
    )
    private SortedSet<HearingDirections> hearingDirections;

    @CCD(label = " Length of hearing",
         hint = "Insert the length of the hearing in hours and minutes, for example 2 hours 30 minutes",
         displayOrder = 10)
    private String lengthOfHearing;

    @CCD(
        label = "Recipients",
        access = {DefaultAccess.class},
        displayOrder = 11
    )
    private SortedSet<RecipientsInTheCase> recipientsInTheCase;

    @CCD(
        label = "Reason for vacating hearing",
        access = { SystemUpdateAccess.class,DefaultAccess.class},
        displayOrder = 12
    )
    private ReasonForVacatingHearing reasonForVacatingHearing;


}
