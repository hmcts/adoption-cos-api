package uk.gov.hmcts.reform.adoption.common.event.page;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.AcknowledgementOfService;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Applicant;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;

@Component
public class Applicant2SolStatementOfTruth implements CcdPageConfiguration {

    @Override
    public void addTo(final PageBuilder pageBuilder) {

        pageBuilder
            .page("Applicant2SolStatementOfTruth")
            .pageLabel("Statement of truth and reconciliation")
            .label("LabelApplicant2SolStatementOfTruth-AoSReview", "### Review the answers in your Acknowledgement "
                + "of Service below. If you wish to change any of your answers, please go back and use the 'Update AoS' action")
            .complex(CaseData::getAcknowledgementOfService)
                .readonlyNoSummary(AcknowledgementOfService::getConfirmReadPetition)
                .readonlyNoSummary(AcknowledgementOfService::getJurisdictionAgree)
                .readonlyNoSummary(AcknowledgementOfService::getJurisdictionDisagreeReason)
            .done()
            .complex(CaseData::getApplicant2)
                .readonlyNoSummary(Applicant::getLegalProceedings)
                .readonlyNoSummary(Applicant::getLegalProceedingsDetails)
            .done();
    }
}
