package uk.gov.hmcts.reform.adoption.adoptioncase.tab;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.CASE_WORKER;

@Component
public class CaseTypeTab implements CCDConfig<CaseData, State, UserRole> {

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        buildSummaryTab(configBuilder);
        buildChildDetailsTab(configBuilder);
        buildConfidentialTab(configBuilder);
        buildDocumentsTab(configBuilder);
    }

    public void buildChildDetailsTab(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.tab("otherParties", "Other Parties")
            .forRoles(CASE_WORKER)
            .label("labelSummary-otherParties", null, "#### Other parties")
            .label("labelSummary-childDetails", null, "### Child details")
            .field("childrenFirstName")
            .field("childrenLastName")
            .field("childrenDateOfBirth")
            .field("childrenSexAtBirt")
            .field("childrenOtherSexAtBirth")
            .field("childrenNationality")
            .field("additionalNationalities")
            .field("firstNameAfterAdoption")
            .field("lastNameAfterAdoption")
            .label("labelSummary-adoptionAgency", null, "#### Agencies/Local Authorities Details")
            .label("labelSummary-adoptionAgencySub", null, "### Adoption Agency")
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
            .label("labelSummary-respondentDetails", null, "#### Respondent Details")
            .label("labelSummary-birthMother", null, "### Birth Mother")
            .field("birthMotherFirstName")
            .field("birthMotherLastName")
            .field("birthMotherStillAlive")
            .field("birthMotherAddress1")
            .field("birthMotherAddress2")
            .field("birthMotherAddress3")
            .field("birthMotherAddressTown")
            .field("birthMotherAddressPostCode")
            .field("birthMotherLastAddressDate")
            .label("labelSummary-respondentDetails", null, "#### Respondent Details")
            .label("labelSummary-birthFather", null, "### Birth Father")
            .field("birthFatherFirstName")
            .field("birthFatherLastName")
            .field("birthFatherStillAlive")
            .field("birthFatherAddress1")
            .field("birthFatherAddress2")
            .field("birthFatherAddress3")
            .field("birthFatherAddressTown")
            .field("birthFatherAddressPostCode")
            .field("birthFatherLastAddressDate");
    }

    public void buildSummaryTab(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.tab("summary", "Summary")
            .forRoles(CASE_WORKER)
            .label("labelSummary-CaseStatus", null, "### Case status")
            .field("status")
            .field("messages")
            .label("labelSummary-CaseDetails", null, "### Case details")
            .field("typeOfAdoption")
            .field("dateSubmitted")
            .field("timetable20Week")
            .field("dateChildMovedIn")
            .field("applicationPayments")
            .field("familyCourtName")
            .field("placementOrderCourt")
            .field("placementOrders")
            .field("siblings");
    }

    private void buildConfidentialTab(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.tab("Confidential", "Confidential Details")
            .forRoles(CASE_WORKER)
            .field("applicant1PhoneNumber")
            .field("applicant1EmailAddress")
            .field("childrenFirstName")
            .field("childrenLastName")
            .field("applyingWith");
    }

    private void buildDocumentsTab(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.tab("documents", "Documents")
            .forRoles(CASE_WORKER)
            .field(CaseData::getDocumentsGenerated)
            .field(CaseData::getApplicant1DocumentsUploaded)
            .field(CaseData::getDocumentsUploaded);
    }
}
