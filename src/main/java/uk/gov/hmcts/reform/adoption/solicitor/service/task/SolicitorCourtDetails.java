package uk.gov.hmcts.reform.adoption.solicitor.service.task;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.adoption.divorcecase.model.CaseData;
import uk.gov.hmcts.reform.adoption.divorcecase.model.State;
import uk.gov.hmcts.reform.adoption.divorcecase.task.CaseTask;

import static uk.gov.hmcts.reform.adoption.divorcecase.model.Court.SERVICE_CENTRE;

@Component
public class SolicitorCourtDetails implements CaseTask {

    @Override
    public CaseDetails<CaseData, State> apply(final CaseDetails<CaseData, State> caseDetails) {

        final CaseData caseData = caseDetails.getData();

        caseData.setDivorceUnit(SERVICE_CENTRE);

        return caseDetails;
    }
}
