package uk.gov.hmcts.reform.adoption.adoptioncase.tab;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.Tab;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.CASE_WORKER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.DISTRICT_JUDGE;
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
        buildServiceRequestTab(configBuilder);
    }

    private void buildServiceRequestTab(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.tab("serviceRequest", "Payment")
            .forRoles(CASE_WORKER, DISTRICT_JUDGE)
            .field("waysToPay");
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
            .field("applyingWith", "applyingWith=\"applyingWith\"")
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
            .field("applicant2PhoneNumber","applyingWith!=\"alone\"")
            .label("LabelSolicitor-Heading", "",
                   "### Solicitor")
            .field("isApplicantRepresentedBySolicitor")
            .field("solicitorSolicitorFirm", "isApplicantRepresentedBySolicitor!=\"No\"")
            .field("solicitorSolicitorRef","isApplicantRepresentedBySolicitor!=\"No\"")
            .field("solicitorSolicitorAddress","isApplicantRepresentedBySolicitor!=\"No\"")
            .field("solicitorEmail","isApplicantRepresentedBySolicitor!=\"No\"")
            .field("solicitorPhoneNumber","isApplicantRepresentedBySolicitor!=\"No\"");
    }

    public void buildOtherPartiesTab(ConfigBuilder<CaseData, State, UserRole> configBuilder) {

        final Tab.TabBuilder<CaseData, UserRole> tabBuilderForOtherParties = configBuilder.tab("otherParties", "Other Parties")
            .displayOrder(2).forRoles(CASE_WORKER, DISTRICT_JUDGE);

        buildTabWithChildDetails(tabBuilderForOtherParties);
        buildTabWithLocalGuardianAndSolicitorDetails(tabBuilderForOtherParties);
        buildTabWithAgencyAndLocalAuthorityDetails(tabBuilderForOtherParties);
        buildTabWithRespondentDetails(tabBuilderForOtherParties);
        buildHearingsTab(configBuilder);
    }

    private void buildHearingsTab(ConfigBuilder<CaseData, State, UserRole> configBuilder) {

        configBuilder.tab("hearings","Hearings")
            .forRoles(CASE_WORKER, DISTRICT_JUDGE)
            .label("labelSummary-managehearing", null, "[Manage hearings](/cases/case-details/${[CASE_REFERENCE]}"
                + "/trigger/caseworker-manage-hearing/caseworker-manage-hearingmanageOrders1)")
            .field(CaseData::getNewHearings)
            .field(CaseData::getVacatedHearings);
    }

    private void buildTabWithRespondentDetails(Tab.TabBuilder<CaseData, UserRole> tabBuilderForOtherParties) {
        tabBuilderForOtherParties
            .label("labelSummary-respondentDetails",
                   null,
                   "### Respondent Details")
            .label("labelSummary-birthMother",
                   null,
                   "#### Birth Mother")
            .field("birthMotherFirstName")
            .field("birthMotherLastName")
            .field("birthMotherDeceased")
            .field("birthMotherAddress1")
            .field("birthMotherAddress2")
            .field("birthMotherAddress3")
            .field("birthMotherAddressTown")
            .field("birthMotherAddressCounty")
            .field("birthMotherAddressPostCode")
            .field("birthMotherAddressCountry")
            .field("birthMotherLastAddressDate")
            .field("birthMotherToBeServed")
            .label("labelsummary-mother-solicitor",null,"### Solicitor")
            .field("isBirthMotherRepresentedBySolicitor")
            .field("motherSolicitorSolicitorFirm")
            .field("motherSolicitorSolicitorRef")
            .field("motherSolicitorSolicitorAddress")
            .field("motherSolicitorPhoneNumber")
            .field("motherSolicitorEmail")
            .label("labelSummary-birthFather",
                   null,
                   "#### Birth Father")
            .field("birthFatherNameOnCertificate", "applyingWith=\"NEVER_SHOW\"")
            .field("birthFatherIdentityKnown", "applyingWith=\"NEVER_SHOW\"")
            .field("birthFatherFirstName")
            .field("birthFatherLastName")
            .field("birthFatherDeceased")
            .field("birthFatherAddress1")
            .field("birthFatherAddress2")
            .field("birthFatherAddress3")
            .field("birthFatherAddressTown")
            .field("birthFatherAddressCounty")
            .field("birthFatherAddressPostCode")
            .field("birthFatherAddressCountry")
            .field("birthFatherLastAddressDate")
            .field("birthFatherToBeServed")
            .label("labelsummary-father-solicitor",null,"### Solicitor")
            .field("isBirthFatherRepresentedBySolicitor")
            .field("fatherSolicitorSolicitorFirm")
            .field("fatherSolicitorSolicitorRef")
            .field("fatherSolicitorSolicitorAddress")
            .field("fatherSolicitorPhoneNumber")
            .field("fatherSolicitorEmail")
            .label("labelSummary-otherParent", null,
                   "#### Other person with parental responsibility")
            .field("isThereAnyOtherPersonWithParentalResponsibility")
            .field("otherParentFirstName")
            .field("otherParentLastName")
            .field("otherParentAddress1")
            .field("otherParentAddress2")
            .field("otherParentAddress3")
            .field("otherParentAddressTown")
            .field("otherParentAddressCounty")
            .field("otherParentAddressPostCode")
            .field("otherParentAddressCountry")
            .field("otherParentLastAddressDate")
            .field("otherParentRelationShipWithChild")
            .field("otherParentToBeServed")
            .label("labelsummary-otherparent-solicitor",null,"### Solicitor")
            .field("isOtherParentRepresentedBySolicitor")
            .field("otherParentSolicitorSolicitorFirm")
            .field("otherParentSolicitorSolicitorRef")
            .field("otherParentSolicitorSolicitorAddress");
    }

    private void buildTabWithAgencyAndLocalAuthorityDetails(Tab.TabBuilder<CaseData, UserRole> tabBuilderForOtherParties) {

        tabBuilderForOtherParties
            .label("labelSummary-adoptionAgency", null, "### Agencies/Local Authorities Details")
            .label("labelSummary-adoptionAgencyTitle", null, "#### Adoption Agency")
            .field("adopAgencyOrLaName")
            .field("adopAgencyOrLaContactName")
            .field("adopAgencyAddressLine1")
            .field("adopAgencyAddressLine2")
            .field("adopAgencyAddressLine3")
            .field("adopAgencyTown")
            .field("adopAgencyAddressCounty")
            .field("adopAgencyPostcode")
            .field("adopAgencyCountry")
            .field("adopAgencyOrLaPhoneNumber")
            .field("adopAgencyOrLaContactEmail")
            .label("labelSummary-otherAdoptionAgency", null, "#### Other adoption agency")
            .field("hasAnotherAdopAgencyOrLAinXui")
            .field("otherAdoptionAgencyOrLaName")
            .field("otherAdoptionAgencyOrLaContactName")
            .field("otherAdoptionAgencyAddress")
            .field("otherAdoptionAgencyOrLaPhoneNumber")
            .field("otherAdoptionAgencyOrLaContactEmail")
            .label("labelSummary-childLA", null, "### Child’s Local Authority")
            .field("childSocialWorkerName")
            .field("childLocalAuthority")
            .field("childSocialWorkerAddressLine1")
            .field("childSocialWorkerAddressLine2")
            .field("childSocialWorkerAddressLine3")
            .field("childSocialWorkerTown")
            .field("childSocialWorkerAddressCounty")
            .field("childSocialWorkerPostcode")
            .field("childSocialWorkerCountry")
            .field("childSocialWorkerPhoneNumber")
            .field("childLocalAuthorityEmail")
            .label("labelSummary-applicantLA", null, "### Applicant’s Local Authority")
            .field("applicantSocialWorkerName")
            .field("applicantLocalAuthority")
            .field("applicantSocialWorkerAddressLine1")
            .field("applicantSocialWorkerAddressLine2")
            .field("applicantSocialWorkerAddressLine3")
            .field("applicantSocialWorkerTown")
            .field("applicantSocialWorkerAddressCounty")
            .field("applicantSocialWorkerPostcode")
            .field("applicantSocialWorkerCountry")
            .field("applicantSocialWorkerPhoneNumber")
            .field("applicantLocalAuthorityEmail");
    }

    private void buildTabWithLocalGuardianAndSolicitorDetails(Tab.TabBuilder<CaseData, UserRole> tabBuilderForOtherParties) {

        tabBuilderForOtherParties
        .label("labelsummary-legal-guridan",null,"### Legal guardian (CAFCASS)")
            .field("isChildRepresentedByGuardian")
            .field("localGuardianName")
            .field("localGuardianGuardianAddress")
            .field("localGuardianPhoneNumber")
            .field("localGuardianEmail")
            .label("labelsummary-child-solicitor",null,"### Solicitor")
            .field("isChildRepresentedBySolicitor")
            .field("childSolicitorSolicitorFirm")
            .field("childSolicitorSolicitorRef")
            .field("childSolicitorSolicitorAddress")
            .field("childSolicitorPhoneNumber")
            .field("childSolicitorEmail");
    }

    private Tab.TabBuilder<CaseData, UserRole> buildTabWithChildDetails(Tab.TabBuilder<CaseData, UserRole> tabBuilderForOtherParties) {
        return tabBuilderForOtherParties
            .label("labelSummary-otherParties", null, "[Amend other parties details](/cases/case-details/${[CASE_REFERENCE]}"
                + "/trigger/caseworker-amend-other-parties-details/caseworker-amend-other-parties-detailsamendOtherParties)")
            .label("labelSummary-childDetails", null, "#### Child details")
            .field("childrenFirstName")
            .field("childrenLastName")
            .field("childrenDateOfBirth")
            .field("childrenSexAtBirth")
            .field("childrenOtherSexAtBirth")
            .field("childrenNationality")
            .field("childrenAdditionalNationalities")
            .field("childrenFirstNameAfterAdoption")
            .field("childrenLastNameAfterAdoption");
    }

    public void buildSummaryTab(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.tab("summary", "Summary")
            .displayOrder(0)
            .forRoles(CASE_WORKER, DISTRICT_JUDGE)
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
            .field("siblings")
            .field("allocatedJudge");
    }

    private void buildDocumentsTab(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.tab("documents", "Documents")
            .forRoles(CASE_WORKER, DISTRICT_JUDGE)
            .displayOrder(3)
            .label("Documents-Heading", null, "# Documents")
            .label("Upload documents",
                   null,
                   "[Upload documents](/cases/case-details/${[CASE_REFERENCE]}"
                       + "/trigger/caseworker-manage-document/caseworker-manage-documentuploadDocument)"
            )
            .field(CaseData::getLaDocumentsUploaded)
            .label("Review all documents",
                   null,
                   "[Review all documents](/cases/case-details/${[CASE_REFERENCE]}"
                       + "/trigger/caseworker-review-document/caseworker-review-documentuploadDocument)"
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
