package uk.gov.hmcts.reform.adoption.adoptioncase.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.task.AdoptionApplicationDraft;
import uk.gov.hmcts.reform.adoption.adoptioncase.task.InitialiseSolicitorCreatedApplication;

import static uk.gov.hmcts.reform.adoption.adoptioncase.task.CaseTaskRunner.caseTasks;

@Service
@Slf4j
public class AdoptionCreateApplicationService {

    @Autowired
    private InitialiseSolicitorCreatedApplication initialiseSolicitorCreatedApplication;

    @Autowired
    private AdoptionApplicationDraft applicationDraft;

    public CaseDetails<CaseData, State> aboutToSubmit(final CaseDetails<CaseData, State> caseDetails) {

        return caseTasks(
            initialiseSolicitorCreatedApplication,
            applicationDraft
        ).run(caseDetails);
    }
}
