package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.reform.adoption.adoptioncase.model.Applicant;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Solicitor;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseworkerAmendApplicant.AMEND_APPLICANT_DETAILS;

/**
 * Contains method to add Page Configuration for ExUI.
 * Display the Amend Case Details screen with all required fields.
 */
public class AmendApplicantDetails implements CcdPageConfiguration {
    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder.page("amendCaseDetailsPage1")
            .pageLabel(AMEND_APPLICANT_DETAILS)
            .optional(CaseData::getApplyingWith,"applicant1EmailAddress=\"NEVER_SHOW\"")
            .label("AmendApplicantDetailsApplicant1Label","## First applicant")
            .complex(CaseData::getApplicant1)
            .mandatory(Applicant::getFirstName)
            .mandatory(Applicant::getLastName)
            .optional(Applicant::getAdditionalNames)
            .mandatory(Applicant::getDateOfBirth)
            .mandatory(Applicant::getOccupation)
            .mandatory(Applicant::getAddress1)
            .optional(Applicant::getAddress2)
            .mandatory(Applicant::getAddressTown)
            .optional(Applicant::getAddressCountry)
            .mandatory(Applicant::getAddressPostCode)
            .mandatory(Applicant::getEmailAddress)
            .mandatory(Applicant::getPhoneNumber)
            .done()
            .label("AmendApplicantDetailsApplicant2Label","## Second applicant","applyingWith!=\"alone\"")
            .optional(CaseData::getApplyingWith,"applicant1EmailAddress=\"NEVER_SHOW\"")
            .complex(CaseData::getApplicant2)
            .mandatory(Applicant::getFirstName,"applyingWith!=\"alone\"")
            .mandatory(Applicant::getLastName,"applyingWith!=\"alone\"")
            .optional(Applicant::getAdditionalNames,"applyingWith!=\"alone\"")
            .mandatory(Applicant::getDateOfBirth,"applyingWith!=\"alone\"")
            .mandatory(Applicant::getOccupation,"applyingWith!=\"alone\"")
            .mandatory(Applicant::getAddress1,"applyingWith!=\"alone\"")
            .optional(Applicant::getAddress2,"applyingWith!=\"alone\"")
            .mandatory(Applicant::getAddressTown,"applyingWith!=\"alone\"")
            .optional(Applicant::getAddressCountry,"applyingWith!=\"alone\"")
            .mandatory(Applicant::getAddressPostCode,"applyingWith!=\"alone\"")
            .mandatory(Applicant::getEmailAddress,"applyingWith!=\"alone\"")
            .mandatory(Applicant::getPhoneNumber,"applyingWith!=\"alone\"")
            .done()
            .label("Applicants-solicitor-Heading","## Applicant's solicitor",null)
            .mandatory(CaseData::getIsApplicantRepresentedBySolicitor)
            .complex(CaseData::getSolicitor,"isApplicantRepresentedBySolicitor=\"Yes\"")
            .mandatory(Solicitor::getSolicitorFirm, "isApplicantRepresentedBySolicitor=\"Yes\"")
            .mandatory(Solicitor::getSolicitorRef, "isApplicantRepresentedBySolicitor=\"Yes\"")
            .mandatory(Solicitor::getSolicitorAddress,"isApplicantRepresentedBySolicitor=\"Yes\"")
            .mandatory(Solicitor::getEmail, "isApplicantRepresentedBySolicitor=\"Yes\"")
            .mandatory(Solicitor::getPhoneNumber, "isApplicantRepresentedBySolicitor=\"Yes\"")
            .done();
    }
}
