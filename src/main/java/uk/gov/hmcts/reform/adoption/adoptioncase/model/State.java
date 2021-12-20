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
    DRAFT("A58 application case drafted"),

    @CCD(
        name = "Submitted",
        label = "# **${[CASE_REFERENCE]}** ${applicantLastName} **&** ${respondentLastName}\n### **${[STATE]}**\n"
    )
    SUBMITTED("A58 application case submitted");

    private final String name;

}

