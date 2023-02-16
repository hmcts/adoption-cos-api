package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;

@RequiredArgsConstructor
@Getter
public enum State {
    @CCD(
        label = "Draft",
        hint = "### Case number: ${[CASE_REFERENCE]}\n ### ${applicant1FirstName} ${applicant1LastName}\n"
    )
    Draft("Draft"),

    @CCD(
        label = "Submitted",
        hint = "### Case number: ${[CASE_REFERENCE]}\n ### ${applicant1FirstName} ${applicant1LastName}\n"
    )
    Submitted("Submitted"),

    @CCD(
        label = "Application awaiting payment",
        hint = "### Case number: ${[CASE_REFERENCE]}\n ### ${applicant1FirstName} ${applicant1LastName}\n"
    )
    AwaitingPayment("AwaitingPayment");

    private final String name;

}

