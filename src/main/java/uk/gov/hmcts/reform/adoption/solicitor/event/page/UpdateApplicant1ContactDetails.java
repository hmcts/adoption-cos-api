package uk.gov.hmcts.reform.adoption.solicitor.event.page;

import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Applicant;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;

public class UpdateApplicant1ContactDetails implements CcdPageConfiguration {

    @Override
    public void addTo(final PageBuilder pageBuilder) {
        pageBuilder
            .page("ApplicantContactDetails")
            .pageLabel("Update applicants contact details")
            .complex(CaseData::getApplicant1)
                .mandatory(Applicant::getEmail)
                .mandatory(Applicant::getPhoneNumber)
                .mandatory(Applicant::getHomeAddress)
                .label("LabelHorizontalLine1", "<hr>")
                .mandatory(Applicant::getKeepContactDetailsConfidential)
                .done();
    }
}
