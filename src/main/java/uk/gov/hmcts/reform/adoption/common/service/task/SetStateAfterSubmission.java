package uk.gov.hmcts.reform.adoption.common.service.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Application;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Payment;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.task.CaseTask;
import uk.gov.hmcts.reform.adoption.payment.model.PaymentStatus;

//TODO
// import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.AwaitingDocuments;
// import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.AwaitingHWFDecision;
import java.util.Objects;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Submitted;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.AwaitingPayment;


@Component
@Slf4j
public class SetStateAfterSubmission implements CaseTask {

    @Override
    public CaseDetails<CaseData, State> apply(final CaseDetails<CaseData, State> caseDetails) {

        final CaseData caseData = caseDetails.getData();
        final Application application = caseData.getApplication();

        // if (application.isHelpWithFeesApplication()) {
        // caseDetails.setState(AwaitingHWFDecision);
        // } else
        if (!application.hasBeenPaidFor()) {
            caseDetails.setState(AwaitingPayment);
        // } else if (application.hasAwaitingApplicant1Documents()) {
        //   caseDetails.setState(AwaitingDocuments);
        } else {
            caseDetails.setState(Submitted);
            //As agreed these changes for showing successful payment in service request tab. This needs to be removed once
            //Fee and pay  & ccd team delivers ways to pay component
            ListValue<Payment> paymentInfo = application.getApplicationPayments()
                .stream()
                .filter(p -> p.getValue().getStatus().equals(PaymentStatus.SUCCESS))
                .findAny()
                .orElse(null);
            if (Objects.nonNull(paymentInfo)) {
                caseData.setSuccessfulPayment(paymentInfo.getValue());
            }
        }

        caseDetails.getData().setStatus(Submitted);

        log.info("State set to {}, CaseID {}", caseDetails.getState(), caseDetails.getId());

        return caseDetails;
    }
}
