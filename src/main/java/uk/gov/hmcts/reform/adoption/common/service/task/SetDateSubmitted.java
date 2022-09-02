package uk.gov.hmcts.reform.adoption.common.service.task;

import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.task.CaseTask;

// import java.time.Clock;
import java.time.LocalDate;

// import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.AwaitingDocuments;
// import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.AwaitingHWFDecision;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Submitted;

@Component
@Slf4j
public class SetDateSubmitted implements CaseTask {

    // @Autowired
    // private Clock clock;

    @Override
    public CaseDetails<CaseData, State> apply(final CaseDetails<CaseData, State> caseDetails) {

        final CaseData caseData = caseDetails.getData();
        final State state = caseDetails.getState();

        if (Submitted.equals(state)
            /*|| AwaitingDocuments.equals(state)
            || AwaitingHWFDecision.equals(state)*/) {

            caseData.getApplication().setDateSubmitted(LocalDate.now());
            caseData.setDueDate(caseData.getApplication().getDateOfSubmissionResponse());
        }

        return caseDetails;
    }
}
