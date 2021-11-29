package uk.gov.hmcts.reform.adoption.caseworker.event.page;

import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.AlternativeService;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;

public class AlternativeServicePaymentConfirmation implements CcdPageConfiguration  {

    @Override
    public void addTo(PageBuilder pageBuilder) {

        pageBuilder.page("alternativeServicePayment")
            .pageLabel("Payment - service application payment")
            .complex(CaseData::getAlternativeService)
                .mandatory(AlternativeService::getPaymentMethod)
                .mandatory(AlternativeService::getFeeAccountNumber, "paymentMethod = \"feePayByAccount\"")
                .optional(AlternativeService::getFeeAccountReferenceNumber, "paymentMethod = \"feePayByAccount\"")
                .mandatory(AlternativeService::getHelpWithFeesReferenceNumber, "paymentMethod = \"feePayByHelp\"")
            .done();
    }
}
