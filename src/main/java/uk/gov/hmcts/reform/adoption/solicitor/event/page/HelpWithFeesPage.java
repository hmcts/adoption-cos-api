package uk.gov.hmcts.reform.adoption.solicitor.event.page;

import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.divorcecase.model.Application;
import uk.gov.hmcts.reform.adoption.divorcecase.model.CaseData;
import uk.gov.hmcts.reform.adoption.divorcecase.model.HelpWithFees;

public class HelpWithFeesPage implements CcdPageConfiguration {

    @Override
    public void addTo(final PageBuilder pageBuilder) {

        pageBuilder
            .page("HelpWithFees")
            .pageLabel("Help with fees")
            .showCondition("solPaymentHowToPay=\"feesHelpWith\"")
            .complex(CaseData::getApplication)
                .complex(Application::getApplicant1HelpWithFees)
                    .mandatory(HelpWithFees::getReferenceNumber)
                    .done()
                .done();
    }
}
