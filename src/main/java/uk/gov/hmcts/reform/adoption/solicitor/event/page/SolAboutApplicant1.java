package uk.gov.hmcts.reform.adoption.solicitor.event.page;

import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Applicant;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;

public class SolAboutApplicant1 implements CcdPageConfiguration {

    private static final String DARK_HORIZONTAL_RULE =
        "![Dark Rule](https://raw.githubusercontent.com/hmcts/adoption-cos-api/master/resources/image/LabelDarkHorizontalRule.png)";

    @Override
    public void addTo(final PageBuilder pageBuilder) {

        pageBuilder
            .page("SolAboutApplicant1")
            .pageLabel("About the applicant")
            .complex(CaseData::getApplicant1)
                .mandatoryWithLabel(Applicant::getFirstName,
                    "The applicant's first name")
                .optionalWithLabel(Applicant::getMiddleName,
                    "The applicant's middle name")
                .mandatoryWithLabel(Applicant::getLastName,
                    "The applicant's last name")
                .mandatoryWithLabel(Applicant::getNameDifferentToMarriageCertificate,
                    "Is the applicant's name different to that on their marriage certificate?")
                .mandatoryWithoutDefaultValue(Applicant::getNameChangedHowOtherDetails,
                "applicant1NameChangedHow=\"other\"",
                "If not through marriage or deed poll, please provide details of how they legally changed they name")
                .mandatoryWithLabel(Applicant::getGender,
                "What is the applicant's gender?")
                .done()
            .complex(CaseData::getApplicant1)
                .mandatoryWithLabel(Applicant::getEmail,
                    "The applicant's email address")
                .optionalWithLabel(Applicant::getPhoneNumber,
                    "The applicant's phone number")
                .mandatoryWithLabel(Applicant::getHomeAddress,
                    "The applicant's home address")
                .label("LabelHorizontalLine1-SolAboutApplicant1", DARK_HORIZONTAL_RULE)
                .mandatoryWithLabel(Applicant::getKeepContactDetailsConfidential,
                    "Keep the applicant's contact details private from ${labelContentTheApplicant2}? (yes/ no)")
                .done();
    }
}
