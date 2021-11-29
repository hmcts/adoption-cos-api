package uk.gov.hmcts.reform.adoption.common.service.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.adoption.divorcecase.model.Application;
import uk.gov.hmcts.reform.adoption.divorcecase.model.CaseData;
import uk.gov.hmcts.reform.adoption.divorcecase.model.State;
import uk.gov.hmcts.reform.adoption.divorcecase.task.CaseTask;

import static uk.gov.hmcts.reform.adoption.divorcecase.model.State.AwaitingDocuments;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.State.AwaitingHWFDecision;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.State.AwaitingPayment;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.State.Submitted;

@Component
@Slf4j
public class SetStateAfterSubmission implements CaseTask {

    @Override
    public CaseDetails<CaseData, State> apply(final CaseDetails<CaseData, State> caseDetails) {

        final CaseData caseData = caseDetails.getData();
        final Application application = caseData.getApplication();

        if (application.isHelpWithFeesApplication()) {
            caseDetails.setState(AwaitingHWFDecision);
        } else if (!application.hasBeenPaidFor()) {
            caseDetails.setState(AwaitingPayment);
        } else if (application.hasAwaitingApplicant1Documents()) {
            caseDetails.setState(AwaitingDocuments);
        } else {
            caseDetails.setState(Submitted);
        }

        log.info("State set to {}, CaseID {}", caseDetails.getState(), caseDetails.getId());

        return caseDetails;
    }
}
