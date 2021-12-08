package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.CaseAccessAdministrator;

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
        label = "# **${[CASE_REFERENCE]}** ${applicant1LastName} **&** ${applicant2LastName}\n### **${[STATE]}**\n"
    )
    AwaitingPayment("AwaitingPayment"),

    @CCD(
        name = "Application rejected",
        label = "# **${[CASE_REFERENCE]}** ${applicant1LastName} **&** ${applicant1LastName}\n### **${[STATE]}**\n",
        access = {CaseAccessAdministrator.class}
    )
    Rejected("Rejected"),

    @CCD(
        name = "Application withdrawn",
        label = "# **${[CASE_REFERENCE]}** ${applicant1LastName} **&** ${applicant1LastName}\n### **${[STATE]}**\n",
        access = {CaseAccessAdministrator.class}
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
        access = {CaseAccessAdministrator.class}
    )
    AwaitingServiceConsideration("AwaitingServiceConsideration"),

    @CCD(
        name = "Awaiting service payment",
        label = "# **${[CASE_REFERENCE]}** ${applicant1LastName} **&** ${applicant1LastName}\n### **${[STATE]}**\n",
        access = {CaseAccessAdministrator.class}
    )
    AwaitingServicePayment("AwaitingServicePayment"),

    @CCD(
        name = "Clarification response submitted",
        label = "# **${[CASE_REFERENCE]}** ${applicant1LastName} **&** ${applicant1LastName}\n### **${[STATE]}**\n",
        access = {CaseAccessAdministrator.class}
    )
    ClarificationSubmitted("ClarificationSubmitted"),

    @CCD(
        name = "Conditional order pronounced",
        label = "# **${[CASE_REFERENCE]}** ${applicant1LastName} **&** ${applicant1LastName}\n### **${[STATE]}**\n",
        access = {CaseAccessAdministrator.class}
    )
    ConditionalOrderPronounced("ConditionalOrderPronounced"),

    @CCD(
        name = "Conditional order refused",
        label = "# **${[CASE_REFERENCE]}** ${applicant1LastName} **&** ${applicant1LastName}\n### **${[STATE]}**\n",
        access = {CaseAccessAdministrator.class}
    )
    ConditionalOrderRefused("ConditionalOrderRefused"),

    @CCD(
        name = "Final order complete",
        label = "# **${[CASE_REFERENCE]}** ${applicant1LastName} **&** ${applicant1LastName}\n### **${[STATE]}**\n",
        access = {CaseAccessAdministrator.class}
    )
    FinalOrderComplete("FinalOrderComplete"),

    @CCD(
        name = "Submitted",
        label = "# **${[CASE_REFERENCE]}** ${applicant1LastName} **&** ${applicant1LastName}\n### **${[STATE]}**\n"
    )
    Submitted("Submitted");

    private final String name;

}

