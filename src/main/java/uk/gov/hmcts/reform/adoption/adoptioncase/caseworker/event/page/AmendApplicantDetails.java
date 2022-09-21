package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

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
        pageBuilder.page("amendCaseDetails")
            .pageLabel(AMEND_APPLICANT_DETAILS)
            .label("solicitor", "Applicant's solicitor")
            .mandatory(CaseData::getApplicantSolicitor)
            .complex(CaseData::getSolicitor)
            .mandatory(Solicitor::getSolicitorFirm, "applicantSolicitor=\"Yes\"")
            .mandatory(Solicitor::getSolicitorReference, "applicantSolicitor=\"Yes\"")
            .mandatory(Solicitor::getAddress1, "applicantSolicitor=\"Yes\"")
            .mandatory(Solicitor::getAddress2, "applicantSolicitor=\"Yes\"")
            .mandatory(Solicitor::getAddressTown, "applicantSolicitor=\"Yes\"")
            .mandatory(Solicitor::getAddressCounty, "applicantSolicitor=\"Yes\"")
            .mandatory(Solicitor::getAddressPostCode, "applicantSolicitor=\"Yes\"")
            .mandatory(Solicitor::getSolicitorEmail, "applicantSolicitor=\"Yes\"")
            .mandatory(Solicitor::getSolicitorPhoneNumber, "applicantSolicitor=\"Yes\"")
            .done();
    }
}
