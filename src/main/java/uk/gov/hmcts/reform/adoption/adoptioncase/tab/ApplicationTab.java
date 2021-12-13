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
        final Tab.TabBuilder<CaseData, UserRole> tabBuilder = configBuilder.tab("applicationDetails", "Application");
        addHeaderFields(tabBuilder);
        addApplicant1(tabBuilder);
    }

    private void addHeaderFields(final Tab.TabBuilder<CaseData, UserRole> tabBuilder) {
        tabBuilder
            .field("createdDate")
            .field("dateSubmitted")
            .field("issueDate")
            .field("dueDate")
            .field(CaseData::getApplicationType)
            .field(CaseData::getBulkListCaseReference);
    }

    private void addApplicant1(final Tab.TabBuilder<CaseData, UserRole> tabBuilder) {
        tabBuilder
            .label("LabelApplicant1-Heading", null, "### The applicant")
            .field("applicant1FirstName")
            .field("applicant1MiddleName")
            .field("applicant1LastName")
            .field("applicant1Gender")
            .field("applicant1NameDifferentToMarriageCertificate")
            .field("applicant1NameChangedHow", "applicant1NameDifferentToMarriageCertificate=\"Yes\"")
            .field("applicant1NameChangedHowOtherDetails", "applicant1NameChangedHow=\"other\"")
            .field("applicant1KeepContactDetailsConfidential", "applicationType=\"NEVER_SHOW\"")
            .field("applicant1PcqId")
            .label("LabelApplicant1DetailsAreConfidential-Heading",
                "applicant1KeepContactDetailsConfidential=\"Yes\"",
                "#### The applicant's contact details are confidential")
            .field("applicant1PhoneNumber", "applicant1KeepContactDetailsConfidential=\"No\"")
            .field("applicant1Email", "applicant1KeepContactDetailsConfidential=\"No\"")
            .field("applicant1HomeAddress", "applicant1KeepContactDetailsConfidential=\"No\"")
            .field("applicant1CorrespondenceAddress", "applicant1KeepContactDetailsConfidential=\"No\"");
    }
}
