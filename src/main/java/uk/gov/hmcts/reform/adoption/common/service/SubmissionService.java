package uk.gov.hmcts.reform.adoption.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.task.CaseTaskRunner;
import uk.gov.hmcts.reform.adoption.common.service.task.GenerateApplicationSummaryDocument;
import uk.gov.hmcts.reform.adoption.common.service.task.SetDateSubmitted;
import uk.gov.hmcts.reform.adoption.common.service.task.SetStateAfterLaSubmission;
import uk.gov.hmcts.reform.adoption.common.service.task.SetStateAfterSubmission;

@Service
public class SubmissionService {

    @Autowired
    private SetStateAfterSubmission setStateAfterSubmission;

    @Autowired
    private SetStateAfterLaSubmission setStateAfterLaSubmission;

    @Autowired
    private SetDateSubmitted setDateSubmitted;

    // @Autowired
    // private SetApplicant2Email setApplicant2Email;

    @Autowired
    private GenerateApplicationSummaryDocument generateApplicationSummaryDocument;

    public CaseDetails<CaseData, State> submitApplication(final CaseDetails<CaseData, State> caseDetails) {

        return CaseTaskRunner.caseTasks(
            setStateAfterSubmission,
            setDateSubmitted,
            generateApplicationSummaryDocument
        ).run(caseDetails);
    }

    public CaseDetails<CaseData, State> laSubmitApplication(final CaseDetails<CaseData, State> caseDetails) {

        return CaseTaskRunner.caseTasks(
            setStateAfterLaSubmission,
            setDateSubmitted// TODO, generateApplicationSummaryDocument
        ).run(caseDetails);
    }
}
