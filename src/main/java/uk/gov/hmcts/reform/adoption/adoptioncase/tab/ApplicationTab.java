package uk.gov.hmcts.reform.adoption.adoptioncase.tab;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.Tab;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

@Component
public class ApplicationTab implements CCDConfig<CaseData, State, UserRole> {


    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        final Tab.TabBuilder<CaseData, UserRole> tabBuilder = configBuilder.tab("applicationDetails", "Applicants");
        //tabBuilder.showCondition(TabShowCondition.showForState(State.Submitted));
        addHeaderFields(tabBuilder);
        addApplicant(tabBuilder);
    }

    public void addHeaderFields(final Tab.TabBuilder<CaseData, UserRole> tabBuilder) {
        tabBuilder
            .field("createdDate")
            .field("dateSubmitted");
    }

    private void addApplicant(final Tab.TabBuilder<CaseData, UserRole> tabBuilder) {
        tabBuilder
            .label("LabelApplicant-Heading",
                   "applyingWith=\"alone\"",
                   "#### Applicant")
            .label("LabelApplicants-Heading",
                   "applyingWith!=\"alone\"",
                   "### Applicants")
            .label("LabelApplicantAlone-Heading",
                   "applyingWith=\"alone\"",
                   "# Applicant")
            .label("LabelApplicant1WithSpouseOrPartner-Heading",
                   "applyingWith!=\"alone\"",
                   "## First Applicant")
            .field("applicant1FirstName")
            .field("applicant1LastName")
            .field("applicant1AdditionalNames")
            .field("applicant1DateOfBirth")
            .field("applicant1Occupation")
            .field("applicant1Address1")
            .field("applicant1AddressTown")
            .field("applicant1AddressPostCode")
            .field("applicant1EmailAddress")
            .field("applicant1PhoneNumber")
            .label("LabelApplicant2WithSpouseOrPartner-Heading",
                   "applyingWith!=\"alone\"",
                   "### Second Applicant")
            .field("applicant2FirstName","applyingWith!=\"alone\"")
            .field("applicant2LastName","applyingWith!=\"alone\"")
            .field("applicant2AdditionalNames","applyingWith!=\"alone\"")
            .field("applicant2DateOfBirth","applyingWith!=\"alone\"")
            .field("applicant2Occupation","applyingWith!=\"alone\"")
            .field("applicant2Address1","applyingWith!=\"alone\"")
            .field("applicant2AddressTown","applyingWith!=\"alone\"")
            .field("applicant2AddressPostCode","applyingWith!=\"alone\"")
            .field("applicant2EmailAddress","applyingWith!=\"alone\"")
            .field("applicant2PhoneNumber","applyingWith!=\"alone\"");
    }

}
