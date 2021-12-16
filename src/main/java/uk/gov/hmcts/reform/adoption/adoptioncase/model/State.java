package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;

@RequiredArgsConstructor
@Getter
public enum State {

    @CCD(
        name = "Applicant approved",
        label = "# **${[CASE_REFERENCE]}** ${applicant1LastName} **&** ${applicant1LastName}\n### **${[STATE]}**\n"
    )
    Applicant1Approved("Applicant1Approved"),

    @CCD(
        name = "Application awaiting payment",
        label = "# **${[CASE_REFERENCE]}** ${applicant1LastName}"
    )
    AwaitingPayment("AwaitingPayment"),

    @CCD(
        name = "Application rejected",
        label = "# **${[CASE_REFERENCE]}** ${applicant1LastName} **&** ${applicant1LastName}\n### **${[STATE]}**\n",
        access = {DefaultAccess.class}
    )
    Rejected("Rejected"),

    @CCD(
        name = "Application withdrawn",
        label = "# **${[CASE_REFERENCE]}** ${applicant1LastName} **&** ${applicant1LastName}\n### **${[STATE]}**\n",
        access = {DefaultAccess.class}
    )
    Withdrawn("Withdrawn"),

    @CCD(
        name = "Awaiting applicant",
        label = "# **${[CASE_REFERENCE]}** ${applicant1LastName} **&** ${applicant1LastName}\n### **${[STATE]}**\n"
    )
    AwaitingDocuments("AwaitingDocuments"),

    @CCD(
        name = "Awaiting service",
        label = "# **${[CASE_REFERENCE]}** ${applicant1LastName} **&** ${applicant1LastName}\n### **${[STATE]}**\n"
    )
    AwaitingService("AwaitingService"),

    @CCD(
        name = "Awaiting Service Consideration",
        label = "# **${[CASE_REFERENCE]}** ${applicant1LastName} **&** ${applicant1LastName}\n### **${[STATE]}**\n",
        access = {DefaultAccess.class}
    )
    AwaitingServiceConsideration("AwaitingServiceConsideration"),

    @CCD(
        name = "Awaiting service payment",
        label = "# **${[CASE_REFERENCE]}** ${applicant1LastName} **&** ${applicant1LastName}\n### **${[STATE]}**\n",
        access = {DefaultAccess.class}
    )
    AwaitingServicePayment("AwaitingServicePayment"),

    @CCD(
        name = "Clarification response submitted",
        label = "# **${[CASE_REFERENCE]}** ${applicant1LastName} **&** ${applicant1LastName}\n### **${[STATE]}**\n",
        access = {DefaultAccess.class}
    )
    ClarificationSubmitted("ClarificationSubmitted"),

    @CCD(
        name = "Conditional order pronounced",
        label = "# **${[CASE_REFERENCE]}** ${applicant1LastName} **&** ${applicant1LastName}\n### **${[STATE]}**\n",
        access = {DefaultAccess.class}
    )
    ConditionalOrderPronounced("ConditionalOrderPronounced"),

    @CCD(
        name = "Conditional order refused",
        label = "# **${[CASE_REFERENCE]}** ${applicant1LastName} **&** ${applicant1LastName}\n### **${[STATE]}**\n",
        access = {DefaultAccess.class}
    )
    ConditionalOrderRefused("ConditionalOrderRefused"),

    @CCD(
        name = "Final order complete",
        label = "# **${[CASE_REFERENCE]}** ${applicant1LastName} **&** ${applicant1LastName}\n### **${[STATE]}**\n",
        access = {DefaultAccess.class}
    )
    FinalOrderComplete("FinalOrderComplete"),

    @CCD(
        name = "Submitted",
        label = "# **${[CASE_REFERENCE]}** ${applicant1LastName} **&** ${applicant1LastName}\n### **${[STATE]}**\n"
    )
    Submitted("Submitted");

    private final String name;

}

