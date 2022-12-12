package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.SortedSet;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedRadioList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManageHearingDetails {

    @CCD(access = {DefaultAccess.class})
    private String hearingId;

    @CCD(
        access = {DefaultAccess.class},
        label = "Type of hearing",
        hint = "For example, a first hearing"
    )
    private String typeOfHearing;

    @CCD(
        access = {DefaultAccess.class},
        label = "Hearing date & time"
    )
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime hearingDateAndTime;

    @CCD(
        access = {DefaultAccess.class},
        label = " Length of hearing",
        hint = "Insert the length of the hearing in hours and minutes, for example 2 hours 30 minutes")
    private String lengthOfHearing;

    @CCD(
        access = {DefaultAccess.class},
        label = "Judge",
        hint = "This should be the allocated judge, if possible."
    )
    private String manageHearingsJudge;

    @CCD(
        access = {DefaultAccess.class},
        label = "Court",
        hint = "Enter the full name of the court where the hearing will be held "
    )
    private String manageHearingsCourt;


    @CCD(
        access = {DefaultAccess.class},
        label = "Is an interpreter needed?",
        hint = "Given details of the interpretation needed for example, sign language or Hindu translator"
    )
    private String isInterpreterNeeded;

    @CCD(
        access = {DefaultAccess.class},
        label = "Method of hearing",
        typeOverride = FixedRadioList,
        typeParameterOverride = "MethodOfHearing"
    )
    private MethodOfHearing methodOfHearing;


    @CCD(
        access = {DefaultAccess.class},
        label = "Accessibility requirements",
        typeOverride = FieldType.TextArea,
        hint = "List any accessibility requirements needed such as disabled access"
    )
    private String accessibilityRequirements;

    @CCD(
        access = {DefaultAccess.class},
        label = "Hearing directions",
        typeOverride = FieldType.MultiSelectList,
        typeParameterOverride = "HearingDirections"
    )
    private SortedSet<HearingDirections> hearingDirections;


    @CCD(
        access = {DefaultAccess.class},
        label = "Recipients"
    )
    private SortedSet<RecipientsInTheCase> recipientsInTheCase;

    @CCD(
        access = {DefaultAccess.class},
        label = "Reason for vacating hearing"
    )
    private ReasonForVacatingHearing reasonForVacatingHearing;

    @CCD(
        access = {DefaultAccess.class},
        label = "Reason for adjournment"
    )
    private ReasonForAdjournHearing reasonForAdjournHearing;

    @CCD(access = {DefaultAccess.class})
    private LocalDate hearingCreationDate;

    @CCD(access = {DefaultAccess.class})
    private Document hearingA90Document;

    @CCD(access = {DefaultAccess.class})
    private Document hearingA91DocumentMother;

    @CCD(access = {DefaultAccess.class})
    private Document hearingA91DocumentFather;

}
