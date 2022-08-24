package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;

@RequiredArgsConstructor
@Getter
public enum State {
    @CCD(
        name = "Draft",
        label = "### #${[CASE_REFERENCE]}\n ### Child's Name: ${childrenFirstName} ${applicant1LastName}\n"
    )
    Draft("Draft"),

    @CCD(
        name = "Submitted",
        label = "### #${[CASE_REFERENCE]}\n ### Child's Name: ${childrenFirstName} ${applicant1LastName}\n"
    )
    Submitted("Submitted"),

    @CCD(
        name = "Application awaiting payment",
        label = "### #${[CASE_REFERENCE]}\n ### Child's Name: ${childrenFirstName} ${applicant1LastName}\n"
    )
    AwaitingPayment("AwaitingPayment"),


    @CCD(
        name = "Awaiting Admin checks",
        label = "### #${[CASE_REFERENCE]}\n ### Child's Name: ${childrenFirstName} ${applicant1LastName}\n"
    )
    AwaitingAdminChecks("AwaitingAdminChecks");

    private final String name;

}

