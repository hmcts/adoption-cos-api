package uk.gov.hmcts.reform.adoption.adoptioncase.tab;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.CASE_WORKER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.SYSTEM_UPDATE;

@Component
public class CaseTypeTab implements CCDConfig<CaseData, State, UserRole> {

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        buildSummaryTab(configBuilder);
        buildApplicantsTab(configBuilder);
        buildOtherPartiesTab(configBuilder);
        buildDocumentsTab(configBuilder);
        buildConfidentialTab(configBuilder);
    }

    public void buildApplicantsTab(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.tab("applicationDetails", "Applicants")
            .displayOrder(1)
            .showCondition(TabShowCondition.showForState(State.Submitted, State.LaSubmitted))
            .label("LabelApplicant-Heading",
                   "applyingWith=\"alone\"",
                   "# Applicant")
            .label("LabelApplicants-Heading",
                   "applyingWith!=\"alone\"",
                   "# Applicants")
            .label("LabelApplicantAlone-Heading",
                   "applyingWith=\"alone\"",
                   "### Applicant")
            .label("LabelApplicant1WithSpouseOrPartner-Heading",
                   "applyingWith!=\"alone\"",
                   "### First applicant")
            .field("applicant1FirstName")
            .field("applicant1LastName")
            .field("applicant1AdditionalNames")
            .field("applicant1DateOfBirth")
            .field("applicant1Occupation")
            .field("applicant1Address1")
            .field("applicant1Address2")
            .field("applicant1AddressTown")
            .field("applicant1AddressCountry")
            .field("applicant1AddressPostCode")
            .field("applicant1EmailAddress")
            .field("applicant1PhoneNumber")
            .label("LabelApplicant2WithSpouseOrPartner-Heading",
                   "applyingWith!=\"alone\"",
                   "### Second applicant")
            .field("applicant2FirstName","applyingWith!=\"alone\"")
            .field("applicant2LastName","applyingWith!=\"alone\"")
            .field("applicant2AdditionalNames","applyingWith!=\"alone\"")
            .field("applicant2DateOfBirth","applyingWith!=\"alone\"")
            .field("applicant2Occupation","applyingWith!=\"alone\"")
            .field("applicant2Address1","applyingWith!=\"alone\"")
            .field("applicant2Address2", "applyingWith!=\"alone\"")
            .field("applicant2AddressTown","applyingWith!=\"alone\"")
            .field("applicant2AddressCountry", "applyingWith!=\"alone\"")
            .field("applicant2AddressPostCode","applyingWith!=\"alone\"")
            .field("applicant2EmailAddress","applyingWith!=\"alone\"")
            .field("applicant2PhoneNumber","applyingWith!=\"alone\"");
    }

    public void buildOtherPartiesTab(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.tab("otherParties", "Other Parties")
            .forRoles(CASE_WORKER)
            .displayOrder(2)
            .label("labelSummary-otherParties", null, "### Other parties")
            .label("labelSummary-childDetails", null, "#### Child details")
            .field("childrenFirstName")
            .field("childrenLastName")
            .field("childrenDateOfBirth")
            .field("childrenSexAtBirth")
            .field("childrenOtherSexAtBirth")
            .field("childrenNationality")
            .field("childrenAdditionalNationalities")
            .field("childrenFirstNameAfterAdoption")
            .field("childrenLastNameAfterAdoption")
            .label("labelSummary-adoptionAgency", null, "### Agencies/Local Authorities Details")
            .label("labelSummary-adoptionAgencySub", "hasAnotherAdopAgencyOrLA = \"Yes\"", "#### Adoption Agency")
            .field("hasAnotherAdopAgencyOrLA", "applyingWith=\"NEVER_SHOW\"")
            .field("adopAgencyOrLaName")
            .field("adopAgencyOrLaContactName")
            .field("adopAgencyAddressLine1")
            .field("adopAgencyTown")
            .field("adopAgencyPostcode")
            .field("adopAgencyOrLaPhoneNumber")
            .field("adopAgencyOrLaContactEmail")
            .label("labelSummary-childLA", null, "### Child’s Local Authority")
            .field("childLocalAuthority")
            .field("childSocialWorkerName")
            .field("childSocialWorkerAddressLine1")
            .field("childSocialWorkerTown")
            .field("childSocialWorkerPostcode")
            .field("childSocialWorkerPhoneNumber")
            .field("childLocalAuthorityEmail")
            .label("labelSummary-applicantLA", null, "### Applicant’s Local Authority")
            .field("applicantLocalAuthority")
            .field("applicantSocialWorkerName")
            .field("applicantSocialWorkerAddressLine1")
            .field("applicantSocialWorkerTown")
            .field("applicantSocialWorkerPostcode")
            .field("applicantSocialWorkerPhoneNumber")
            .field("applicantLocalAuthorityEmail")
            .label("labelSummary-respondentDetails",
                   null,
                   "### Respondent Details")
            .label("labelSummary-birthMother",
                   null,
                   "#### Birth Mother")
            .field("birthMotherFirstName")
            .field("birthMotherLastName")
            .field("birthMotherDeceased")
            .field("birthMotherAddress1", "birthMotherDeceased=\"No\"")
            .field("birthMotherAddress2", "birthMotherDeceased=\"No\"")
            .field("birthMotherAddress3", "birthMotherDeceased=\"No\"")
            .field("birthMotherAddressTown", "birthMotherDeceased=\"No\"")
            .field("birthMotherAddressCountry", "birthMotherDeceased=\"No\"")
            .field("birthMotherAddressPostCode", "birthMotherDeceased=\"No\"")
            .field("birthMotherLastAddressDate", "birthMotherDeceased=\"No\"")
            .label("labelSummary-birthFather", "birthFatherNameOnCertificate=\"Yes\"", "#### Birth Father")
            .field("birthFatherFirstName")
            .field("birthFatherNameOnCertificate", "applyingWith=\"NEVER_SHOW\"")
            .field("birthFatherLastName")
            .field("birthFatherDeceased")
            .field("birthFatherAddress1")
            .field("birthFatherAddress2")
            .field("birthFatherAddress3")
            .field("birthFatherAddressTown")
            .field("birthMotherAddressCountry")
            .field("birthFatherAddressPostCode")
            .field("birthFatherLastAddressDate");
    }

    public void buildSummaryTab(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.tab("summary", "Summary")
            .displayOrder(0)
            .forRoles(CASE_WORKER)
            .label("labelSummary-CaseStatus", null, "### Case status")
            .field("status")
            .field("messages")
            .label("labelSummary-CaseDetails", null, "### Case details")
            .field("typeOfAdoption")
            .field("dateSubmitted")
            .field("timetable20Week")
            .field("dateChildMovedIn")
            .field("familyCourtName")
            .field("placementOrder")
            .field("placementOrders")
            .field("siblings");
    }

    private void buildDocumentsTab(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.tab("documents", "Documents")
            .forRoles(CASE_WORKER)
            .displayOrder(3)
            .label("Documents-Heading", null, "# Documents")
            .label("Upload documents",
                   null,
                   "[Upload documents](/cases/case-details/${[CASE_REFERENCE]}"
                       + "/trigger/caseworker-manage-document/caseworker-manage-documentuploadDocument)"
            )
            .field(CaseData::getApplicationDocumentsCategory)
            .field(CaseData::getReportsDocumentCategory)
            .field(CaseData::getStatementsDocumentCategory)
            .field(CaseData::getAdditionalDocumentsCategory);
    }

    private void buildConfidentialTab(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.tab("Confidential", "Confidential Details")
            .forRoles(SYSTEM_UPDATE)
            .field("applicant1PhoneNumber")
            .field("applicant1EmailAddress")
            .field("childrenFirstName")
            .field("childrenLastName")
            .field("birthFatherNameOnCertificate")
            .field("hasAnotherAdopAgencyOrLA")
            .field("applyingWith");
    }
}
