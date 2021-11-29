package uk.gov.hmcts.reform.adoption.common.event.page;

import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.AcknowledgementOfService;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;

public class Applicant2SolAosJurisdiction implements CcdPageConfiguration {

    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder
            .page("Applicant2SolAosjurisdiction")
            .pageLabel("Do you agree that the courts of England and Wales have jurisdiction?")
            .complex(CaseData::getAcknowledgementOfService)
            .mandatory(AcknowledgementOfService::getJurisdictionAgree)
            .mandatory(AcknowledgementOfService::getJurisdictionDisagreeReason, "jurisdictionAgree=\"No\"")
            .done();
    }
}
