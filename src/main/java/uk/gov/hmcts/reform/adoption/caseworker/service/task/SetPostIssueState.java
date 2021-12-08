package uk.gov.hmcts.reform.adoption.caseworker.service.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Application;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.task.CaseTask;

@Component
@Slf4j
public class SetPostIssueState implements CaseTask {

    @Override
    public CaseDetails<CaseData, State> apply(final CaseDetails<CaseData, State> caseDetails) {

        final Application application = caseDetails.getData().getApplication();

        log.info("Setting state to {}.  Case ID: {}", caseDetails.getState(), caseDetails.getId(), application);
        return caseDetails;
    }
}
