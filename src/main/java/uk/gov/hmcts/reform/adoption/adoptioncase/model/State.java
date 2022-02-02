package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;

@RequiredArgsConstructor
@Getter
public enum State {
    @CCD(
        name = "Draft",
        label = "# **${[CASE_REFERENCE]}** ${applicantLastName} **&** ${respondentLastName}\n### **${[STATE]}**\n"
    )
    Draft("Draft"),

    @CCD(
        name = "Submitted",
        label = "# **${[CASE_REFERENCE]}** ${applicantLastName} **&** ${respondentLastName}\n### **${[STATE]}**\n"
    )
    Submitted("Submitted"),

    @CCD(
        name = "Application awaiting payment",
        label = "### Case number: ${hyphenatedCaseRef}\n ### ${applicant1LastName}\n"
    )
    AwaitingPayment("AwaitingPayment");

    private final String name;

}

