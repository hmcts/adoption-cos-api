package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.SystemUpdateAccess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.SortedSet;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedRadioList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManageHearingTabDetails {

    @CCD(
        label = "Type of hearing"
    )
    private String typeOfHearing;

    @CCD(
        label = "Hearing date & time"
    )
    private LocalDateTime hearingDateAndTime;

    @CCD(label = " Length of hearing")
    private String lengthOfHearing;

    @CCD(
        label = "Judge"
    )
    private String judge;

    @CCD(
        label = "Court"
    )
    private String court;

    @CCD(
        label = "Is an interpreter needed?"
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

    @CCD(
        label = "Recipients",
        access = {DefaultAccess.class}
    )
    private SortedSet<RecipientsInTheCase> recipientsInTheCase;

    @CCD(
        label = "Reason for vacating hearing",
        access = { SystemUpdateAccess.class,DefaultAccess.class}
    )
    private ReasonForVacatingHearing reasonForVacatingHearing;

    @CCD(
        label = "Reason for adjournment",
        access = { SystemUpdateAccess.class,DefaultAccess.class}
    )
    private ReasonForAdjournHearing reasonForAdjournHearing;

    @CCD
    private LocalDate hearingCreationDate;

    @CCD(
        access = {DefaultAccess.class}
    )
    private Document hearingA90Document;

    @CCD(
        access = {DefaultAccess.class}
    )
    private Document hearingA91DocumentMother;

    @CCD(
        access = {DefaultAccess.class}
    )
    private Document hearingA91DocumentFather;
}
