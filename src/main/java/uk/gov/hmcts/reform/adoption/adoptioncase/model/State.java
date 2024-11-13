package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.state.AwaitingPaymentAccess;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.state.DraftAccess;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.state.LocalAuthoritySubmittedAccess;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.state.SubmittedAccess;

@RequiredArgsConstructor
@Getter
public enum State {
    @CCD(
        label = "Draft",
        hint = "### #${[CASE_REFERENCE]}\n ### Child's name: ${childrenFirstName} ${childrenLastName}\n",
        access = {DraftAccess.class}
    )
    Draft("Draft"),

    @CCD(
        label = "Application awaiting payment",
        hint = "### #${[CASE_REFERENCE]}\n ### Child's name: ${childrenFirstName} ${childrenLastName}\n",
        access = {AwaitingPaymentAccess.class}
    )
    AwaitingPayment("AwaitingPayment"),

    @CCD(
        label = "Submitted",
        hint = "### #${[CASE_REFERENCE]}\n ### Child's name: ${childrenFirstName} ${childrenLastName}\n",
        access = {SubmittedAccess.class}
    )
    Submitted("Awaiting LA input"),

    @CCD(
        label = "LA Submitted",
        hint = "### #${[CASE_REFERENCE]}\n ### Child's name: ${childrenFirstName} ${childrenLastName}\n",
        access = {LocalAuthoritySubmittedAccess.class}
    )
    LaSubmitted("LaSubmitted");

    private final String name;

}
