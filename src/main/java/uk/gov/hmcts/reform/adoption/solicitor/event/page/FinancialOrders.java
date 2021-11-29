package uk.gov.hmcts.reform.adoption.solicitor.event.page;

import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.divorcecase.model.Applicant;
import uk.gov.hmcts.reform.adoption.divorcecase.model.CaseData;

public class FinancialOrders implements CcdPageConfiguration {

    @Override
    public void addTo(final PageBuilder pageBuilder) {

        pageBuilder
            .page("FinancialOrders")
            .pageLabel("Financial orders")
            .complex(CaseData::getApplicant1)
                .mandatory(Applicant::getFinancialOrder)
                .done();
    }
}
