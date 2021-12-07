package uk.gov.hmcts.reform.adoption.solicitor.service.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.task.CaseTask;
import uk.gov.hmcts.reform.adoption.document.CaseDataDocumentService;
import uk.gov.hmcts.reform.adoption.document.content.DraftApplicationTemplateContent;

import java.time.LocalDate;

import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.DIVORCE_DRAFT_APPLICATION;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.DIVORCE_DRAFT_APPLICATION_DOCUMENT_NAME;
import static uk.gov.hmcts.reform.adoption.document.model.DocumentType.APPLICATION;

@Component
@Slf4j
public class AdoptionApplicationDraft implements CaseTask {

    @Autowired
    private CaseDataDocumentService caseDataDocumentService;

    @Autowired
    private DraftApplicationTemplateContent draftApplicationTemplateContent;

    @Override
    public CaseDetails<CaseData, State> apply(final CaseDetails<CaseData, State> caseDetails) {

        final CaseData caseData = caseDetails.getData();
        final Long caseId = caseDetails.getId();
        final LocalDate createdDate = caseDetails.getCreatedDate().toLocalDate();

        log.info("Executing handler for generating draft adoption application for case id {} ", caseId);

        caseDataDocumentService.renderDocumentAndUpdateCaseData(
            caseData,
            APPLICATION,
            draftApplicationTemplateContent.apply(caseData, caseId, createdDate),
            caseId,
            DIVORCE_DRAFT_APPLICATION,
            caseData.getApplicant1().getLanguagePreference(),
            DIVORCE_DRAFT_APPLICATION_DOCUMENT_NAME + caseId
        );

        return caseDetails;
    }
}
