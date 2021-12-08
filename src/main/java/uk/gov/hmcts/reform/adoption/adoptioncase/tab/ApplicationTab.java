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
        addLegalConnections(tabBuilder);
        addOtherProceedings(tabBuilder);
        addService(tabBuilder);
    }

    private void addHeaderFields(final Tab.TabBuilder<CaseData, UserRole> tabBuilder) {
        tabBuilder
            .field("createdDate")
            .field("dateSubmitted")
            .field("issueDate")
            .field("dueDate")
            .field(CaseData::getApplicationType)
            .field(CaseData::getAdoptionUnit)
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
            .field("applicant1CorrespondenceAddress", "applicant1KeepContactDetailsConfidential=\"No\"")

            //Applicant 1 Solicitor
            .field("applicant1SolicitorRepresented", "applicationType=\"NEVER_SHOW\"")
            .label("LabelApplicant1sSolicitor-Heading",
                "applicant1SolicitorRepresented=\"Yes\"",
                "#### The applicant's solicitor")
            .field("applicant1SolicitorReference", "applicant1SolicitorRepresented=\"Yes\"")
            .field("applicant1SolicitorName", "applicant1SolicitorRepresented=\"Yes\"")
            .field("applicant1SolicitorAddress", "applicant1SolicitorRepresented=\"Yes\"")
            .field("applicant1SolicitorPhone", "applicant1SolicitorRepresented=\"Yes\"")
            .field("applicant1SolicitorEmail", "applicant1SolicitorRepresented=\"Yes\"")
            .field("applicant1SolicitorOrganisationPolicy", "applicant1SolicitorRepresented=\"Yes\"")
            .field("applicant1SolicitorAgreeToReceiveEmails", "applicant1SolicitorRepresented=\"Yes\"");
    }

    private void addLegalConnections(final Tab.TabBuilder<CaseData, UserRole> tabBuilder) {
        tabBuilder
            .label("LabelJurisdiction-Heading", null, "### Jurisdiction")
            .field("jurisdictionConnections");
    }

    private void addOtherProceedings(final Tab.TabBuilder<CaseData, UserRole> tabBuilder) {
        tabBuilder
            .label("LabelOtherProceedings-Heading", null, "### Applicant's other proceedings:")
            .field("applicant1LegalProceedings")
            .field("applicant1LegalProceedingsDetails", "applicant1LegalProceedings=\"Yes\"")
            .field("applicant1FinancialOrder")
            .field("solUrgentCase")
            .field("solUrgentCaseSupportingInformation", "solUrgentCase=\"Yes\"")
            .field("solStatementOfReconciliationCertify")
            .field("solStatementOfReconciliationDiscussed")
            .field("applicant1StatementOfTruth")
            .field("solSignStatementOfTruth")
            .field("solStatementOfReconciliationName")
            .field("solStatementOfReconciliationFirm")
            .field("statementOfReconciliationComments");
    }

    private void addService(final Tab.TabBuilder<CaseData, UserRole> tabBuilder) {
        tabBuilder
            .label("Label-SolicitorService", "solServiceMethod=\"solicitorService\"", "### Solicitor Service")
            .field("solServiceMethod", "solServiceMethod=\"*\"")
            .field("solServiceDateOfService", "solServiceMethod=\"solicitorService\"")
            .field("solServiceDocumentsServed", "solServiceMethod=\"solicitorService\"")
            .field("solServiceOnWhomServed", "solServiceMethod=\"solicitorService\"")
            .field("solServiceHowServed", "solServiceMethod=\"solicitorService\"")
            .field("solServiceServiceDetails",
                "solServiceHowServed=\"deliveredTo\" OR solServiceHowServed=\"postedTo\"")
            .field("solServiceAddressServed", "solServiceMethod=\"solicitorService\"")
            .field("solServiceBeingThe", "solServiceMethod=\"solicitorService\"")
            .field("solServiceLocationServed", "solServiceMethod=\"solicitorService\"")
            .field("solServiceSpecifyLocationServed", "solServiceMethod=\"solicitorService\" AND solServiceLocationServed=\"otherSpecify\"")
            .field("solServiceServiceSotName", "solServiceMethod=\"solicitorService\"")
            .field("solServiceTruthStatement", "solServiceMethod=\"solicitorService\" AND solServiceHowServed=\"*\"")
            .field("solServiceServiceSotFirm", "solServiceMethod=\"solicitorService\"");
    }
}
