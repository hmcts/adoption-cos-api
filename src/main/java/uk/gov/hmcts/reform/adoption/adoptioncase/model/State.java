package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;

@RequiredArgsConstructor
@Getter
public enum State {
    @CCD(
        label = "Draft",
        hint = "### #${[CASE_REFERENCE]}\n ### Child's name: ${childrenFirstName} ${childrenLastName}\n"
    )
    Draft("Draft"),

    @CCD(
        label = "Submitted",
        hint = "### #${[CASE_REFERENCE]}\n ### Child's name: ${childrenFirstName} ${childrenLastName}\n"
    )
    Submitted("Awaiting LA input"),

    @CCD(
        label = "Application awaiting payment",
        hint = "### #${[CASE_REFERENCE]}\n ### Child's name: ${childrenFirstName} ${childrenLastName}\n"
    )
    AwaitingPayment("AwaitingPayment"),

    @CCD(
        label = "Awaiting Admin checks",
        hint = "### #${[CASE_REFERENCE]}\n ### Child's name: ${childrenFirstName} ${childrenLastName}\n"
    )
    AwaitingAdminChecks("AwaitingAdminChecks"),

    @CCD(
        label = "Awaiting admin check",
        hint = "### #${[CASE_REFERENCE]}\n ### Child's name: ${childrenFirstName} ${childrenLastName}\n"
    )
    LaSubmitted("LaSubmitted"),

    @CCD(
        label = "Awaiting admin check",
        hint = "### #${[CASE_REFERENCE]}\n ### Child's name: ${childrenFirstName} ${childrenLastName}\n"
    )
    SUBMITTED_AWAITING_ADMIN_CHECK("Awaiting admin check"),

    @CCD(
        label = "Gatekeeping",
        hint = "### #${[CASE_REFERENCE]}\n ### Child's name: ${childrenFirstName} ${childrenLastName}\n"
    )
    JUDICIAL_REVIEW("Gatekeeping"),

    @CCD(
        label = "Withdrawn",
        hint = "### #${[CASE_REFERENCE]}\n ### Child's name: ${childrenFirstName} ${childrenLastName}\n"
    )
    CASE_WITHDRAWN("Withdrawn"),

    @CCD(
        label = "Hearing",
        hint = "### #${[CASE_REFERENCE]}\n ### Child's name: ${childrenFirstName} ${childrenLastName}\n"
    )
    PREPARE_FOR_HEARING_CONDUCT_HEARING("Hearing"),

    @CCD(
        label = "Closed",
        hint = "### #${[CASE_REFERENCE]}\n ### Child's name: ${childrenFirstName} ${childrenLastName}\n"
    )
    ALL_FINAL_ORDERS_ISSUED("Closed"),

    @CCD(
        label = "Awaiting Applicant Input",
        hint = "### #${[CASE_REFERENCE]}\n ### Child's name: ${childrenFirstName} ${childrenLastName}\n"
    )
    AWAITING_RESUBMISSION_TO_HMCTS("Awaiting Applicant Input"),
    ;

    private final String name;

}
