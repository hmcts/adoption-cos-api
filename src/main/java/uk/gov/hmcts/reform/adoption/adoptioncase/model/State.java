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
        label = "Application awaiting payment",
        hint = "### #${[CASE_REFERENCE]}\n ### Child's name: ${childrenFirstName} ${childrenLastName}\n"
    )
    AwaitingPayment("AwaitingPayment"),

    @CCD(
        label = "Submitted",
        hint = "### #${[CASE_REFERENCE]}\n ### Child's name: ${childrenFirstName} ${childrenLastName}\n"
    )
    Submitted("Awaiting LA input"), //TODO name???

    @CCD(
        label = "LA Submitted",
        hint = "### #${[CASE_REFERENCE]}\n ### Child's name: ${childrenFirstName} ${childrenLastName}\n"
    )
    LaSubmitted("LaSubmitted"),
    ;

    private final String name;

}
