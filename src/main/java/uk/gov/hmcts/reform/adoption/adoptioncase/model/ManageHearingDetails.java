package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Builder
public class ManageHearingDetails {


    @CCD(
        label = "Type of hearing",
        access = {DefaultAccess.class},
        hint = "For example, a first hearing",
        displayOrder = 0

    )
    private String typeOfHearing;

    @CCD(
        label = "Hearing Date & Time",
        access = {DefaultAccess.class},
        displayOrder = 1
    )
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime hearingDate;

    @CCD(
        label = "Judge",
        access = {DefaultAccess.class},
        displayOrder = 3
    )
    private String judge;

    @CCD(
        label = "Court",
        access = {DefaultAccess.class},
        displayOrder = 4
    )
    private String court;


    @CCD(
        label = "is an interpreter needed?",
        access = {DefaultAccess.class},
        hint = "Given details of the interpretation needed for example, sign language or Hindu translator",
        displayOrder = 5
    )
    private String isInterpreterNeeded;

    @CCD(
        label = "Method of hearing",
        access = {DefaultAccess.class},
        typeOverride = FixedRadioList,
        typeParameterOverride = "MethodOfHearing",
        displayOrder = 6
    )
    private MethodOfHearing methodOfHearing;


    @CCD(
        label = "Accessibility requirements",
        access = {DefaultAccess.class},
        displayOrder = 7,
        typeOverride = FieldType.TextArea
    )
    private String accessibilityRequirements;

    @CCD(
        label = "Hearing directions",
        access = {DefaultAccess.class},
        typeOverride = FieldType.MultiSelectList,
        typeParameterOverride = "HearingDirections",
        displayOrder = 8
    )
    private SortedSet<HearingDirections> hearingDirections;


    @CCD(label = "Hearing duration",
        access = {DefaultAccess.class})
    private HearingDuration hearingDuration;


    @CCD(label = "Hearing length, in hours",
        access = {DefaultAccess.class},
        showCondition = "hearingDuration = \"hoursAndMinutes\"")
    private Integer hours;

    @CCD(label = "Hearing length, in minutes",
        access = {DefaultAccess.class},
        showCondition = "hearingDuration = \"hoursAndMinutes\"")
    private Integer minutes;

    @CCD(label = "Number of days",
        access = {DefaultAccess.class},
        showCondition = "hearingDuration = \"numberOfDays\"")
    private Integer numberOfDays;

    @CCD(label = "Hearing end date & time",
        access = {DefaultAccess.class},
        showCondition = "hearingDuration = \"hearingEndDateAndTime\"")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime hearingEndDateAndTime;


}
