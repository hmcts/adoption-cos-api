package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.reform.adoption.adoptioncase.model.Applicant;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
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
        pageBuilder.page("amendCaseDetails")
            .pageLabel(AMEND_APPLICANT_DETAILS)
            .label("applicant1Label","## First applicant","applyingWith=\"alone\"")
            .complex(CaseData::getApplicant1)
            .mandatory(Applicant::getFirstName)
            .mandatory(Applicant::getLastName)
            .mandatory(Applicant::getAdditionalNames)
            .mandatory(Applicant::getDateOfBirth)
            .mandatory(Applicant::getOccupation)
            .mandatory(Applicant::getAddress1)
            .mandatory(Applicant::getAddress2)
            .mandatory(Applicant::getAddressTown)
            .mandatory(Applicant::getAddressCountry)
            .mandatory(Applicant::getAddressPostCode)
            .mandatory(Applicant::getEmailAddress)
            .mandatory(Applicant::getPhoneNumber)
            .done()
            .label("applicant2Label","## Second applicant","applyingWith=!\"alone\"")
            .complex(CaseData::getApplicant2)
            .mandatory(Applicant::getFirstName)
            .mandatory(Applicant::getLastName)
            .mandatory(Applicant::getAdditionalNames)
            .mandatory(Applicant::getDateOfBirth)
            .mandatory(Applicant::getOccupation)
            .mandatory(Applicant::getAddress1)
            .mandatory(Applicant::getAddress2)
            .mandatory(Applicant::getAddressTown)
            .mandatory(Applicant::getAddressCountry)
            .mandatory(Applicant::getAddressPostCode)
            .mandatory(Applicant::getEmailAddress)
            .mandatory(Applicant::getPhoneNumber)
            .done();
    }
}
