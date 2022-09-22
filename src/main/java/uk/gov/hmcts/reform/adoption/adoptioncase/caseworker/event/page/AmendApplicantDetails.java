package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

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
            .label("Applicants-solicitor-Heading","## Applicant's solicitor",null)
            .mandatory(CaseData::getYesNo)
            .mandatory(CaseData::getSolicitor, "yesNo=\"Yes\"")
            .done();
    }
}
