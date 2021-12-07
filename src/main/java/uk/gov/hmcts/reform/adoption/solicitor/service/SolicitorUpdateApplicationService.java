package uk.gov.hmcts.reform.adoption.solicitor.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.document.task.DivorceApplicationRemover;
import uk.gov.hmcts.reform.adoption.solicitor.service.task.AdoptionApplicationDraft;
import uk.gov.hmcts.reform.adoption.solicitor.service.task.SetApplicant1SolicitorAddress;

import static uk.gov.hmcts.reform.adoption.adoptioncase.task.CaseTaskRunner.caseTasks;

@Service
@Slf4j
public class SolicitorUpdateApplicationService {

    @Autowired
    private DivorceApplicationRemover divorceApplicationRemover;

    @Autowired
    private AdoptionApplicationDraft adoptionApplicationDraft;

    @Autowired
    private SetApplicant1SolicitorAddress setApplicant1SolicitorAddress;

    public CaseDetails<CaseData, State> aboutToSubmit(final CaseDetails<CaseData, State> caseDetails) {

        return caseTasks(
            setApplicant1SolicitorAddress,
            divorceApplicationRemover,
            adoptionApplicationDraft
        ).run(caseDetails);
    }
}
