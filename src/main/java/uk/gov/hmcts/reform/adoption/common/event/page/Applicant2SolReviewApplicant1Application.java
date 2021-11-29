package uk.gov.hmcts.reform.adoption.common.event.page;

import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.divorcecase.model.AcknowledgementOfService;
import uk.gov.hmcts.reform.adoption.divorcecase.model.Application;
import uk.gov.hmcts.reform.adoption.divorcecase.model.CaseData;

public class Applicant2SolReviewApplicant1Application implements CcdPageConfiguration {

    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder
            .page("Applicant2SolReviewApplicant1Application")
            .pageLabel("Review application")
            .complex(CaseData::getApplication)
                .readonly(Application::getMiniApplicationLink)
                .done()
            .label("LabelRespSol-AOSRespond",
                "### Respond to ${labelContentDivorceOrCivilPartnershipApplication} \n\n"
                    + "# Reference number\n"
                    + "${[CASE_REFERENCE]}\n\n"
                    + "# ${labelContentApplicant2UC}\n"
                    + "${applicant2FirstName} ${applicant2LastName}\n\n"
                    + "You must respond to this application within 14 days of ${labelContentTheApplicant2} receiving the letter "
                    + "from the courts."
                    + " If you don't, the applicant can ask the court to move the divorce forwards without ${labelContentTheApplicant2}. "
                    + "${labelContentTheApplicant2UC} may have to pay extra fees if this happens.\n\n")
            .complex(CaseData::getAcknowledgementOfService)
            .mandatory(AcknowledgementOfService::getConfirmReadPetition)
            .label("LabelRespSol-AOSConfirmRead",
                "### ${labelContentTheApplicant2} has not read the application\n\n"
                    + "${labelContentTheApplicant2} must have read the application in order to respond.",
              "confirmReadPetition=\"No\"")
            .done();
    }
}
