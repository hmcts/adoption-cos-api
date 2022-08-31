package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;

@RequiredArgsConstructor
@Getter
public enum State {
    @CCD(
        name = "Draft",
        label = "### Case number: ${[CASE_REFERENCE]}\n ### ${applicant1FirstName} ${applicant1LastName}\n"
    )
    Draft("Draft"),

    @CCD(
        name = "Awaiting LA input",
        label = "### Case number: ${[CASE_REFERENCE]}\n ### ${applicant1FirstName} ${applicant1LastName}\n"
    )
    Submitted("Awaiting LA input"),

    @CCD(
        name = "Application awaiting payment",
        label = "### Case number: ${[CASE_REFERENCE]}\n ### ${applicant1FirstName} ${applicant1LastName}\n"
    )
    AwaitingPayment("AwaitingPayment"),

    @CCD(
        name = "Awaiting Admin checks",
        label = "### Case number: ${[CASE_REFERENCE]}\n ### ${applicant1FirstName} ${applicant1LastName}\n"
    )
    AwaitingAdminChecks("AwaitingAdminChecks"),

    @CCD(
        name = "LA Submitted",
        label = "### Case number: ${[CASE_REFERENCE]}\n ### ${applicant1FirstName} ${applicant1LastName}\n"
    )
    LaSubmitted("LaSubmitted"),

    @CCD(
        name = "Awaiting admin check",
        label = "### Case number: ${[CASE_REFERENCE]}\n ### ${applicant1FirstName} ${applicant1LastName}\n"
    )
    SUBMITTED_AWAITING_ADMIN_CHECK("Awaiting admin check"),

    @CCD(
        name = "Gatekeeping",
        label = "### Case number: ${[CASE_REFERENCE]}\n ### ${applicant1FirstName} ${applicant1LastName}\n"
    )
    JUDICIAL_REVIEW("Gatekeeping"),

    @CCD(
        name = "Withdrawn",
        label = "### Case number: ${[CASE_REFERENCE]}\n ### ${applicant1FirstName} ${applicant1LastName}\n"
    )
    CASE_WITHDRAWN("Withdrawn"),

    @CCD(
        name = "Hearing",
        label = "### Case number: ${[CASE_REFERENCE]}\n ### ${applicant1FirstName} ${applicant1LastName}\n"
    )
    PREPARE_FOR_HEARING_CONDUCT_HEARING("Hearing"),

    @CCD(
        name = "Closed",
        label = "### Case number: ${[CASE_REFERENCE]}\n ### ${applicant1FirstName} ${applicant1LastName}\n"
    )
    ALL_FINAL_ORDERS_ISSUED("Closed"),

    @CCD(
        name = "Awaiting Applicant Input",
        label = "### Case number: ${[CASE_REFERENCE]}\n ### ${applicant1FirstName} ${applicant1LastName}\n"
    )
    AWAITING_RESUBMISSION_TO_HMCTS("Awaiting Applicant Input"),
    ;

    private final String name;

}

