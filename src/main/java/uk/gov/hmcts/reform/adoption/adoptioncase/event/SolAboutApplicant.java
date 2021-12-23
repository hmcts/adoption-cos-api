package uk.gov.hmcts.reform.adoption.adoptioncase.event;

import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Applicant;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;

public class SolAboutApplicant implements CcdPageConfiguration {

    @Override
    public void addTo(final PageBuilder pageBuilder) {

        pageBuilder
            .page("SolAboutApplicant")
            .pageLabel("About the applicant")
            .complex(CaseData::getApplicant)
            .mandatoryWithLabel(
                Applicant::getFirstName,
                "The applicant's first name"
            )
            .optionalWithLabel(
                Applicant::getMiddleName,
                "The applicant's middle name"
            )
            .mandatoryWithLabel(
                Applicant::getLastName,
                "The applicant's last name"
            )
            .mandatoryWithLabel(
                Applicant::getGender,
                "What is the applicant's gender?"
            )
            .mandatoryWithLabel(
                Applicant::getEmail,
                "The applicant's email address"
            )
            .optionalWithLabel(
                Applicant::getPhoneNumber,
                "The applicant's phone number"
            )
            .mandatoryWithLabel(
                Applicant::getHomeAddress,
                "The applicant's home address"
            )
            .done();
    }
}
