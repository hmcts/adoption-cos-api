package uk.gov.hmcts.reform.adoption.common.service.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.task.CaseTask;
import uk.gov.hmcts.reform.adoption.document.CaseDataDocumentService;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Submitted;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.ADOPTION_APPLICATION_FILE_NAME;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.ADOPTION_APPLICATION_SUMMARY;
import static uk.gov.hmcts.reform.adoption.document.DocumentType.APPLICATION_SUMMARY_CY;
import static uk.gov.hmcts.reform.adoption.document.DocumentType.APPLICATION_SUMMARY_EN;
import static uk.gov.hmcts.reform.adoption.document.DocumentUtil.formatDocumentName;

@Component
@Slf4j
public class GenerateApplicationSummaryDocument  implements CaseTask {

    @Autowired
    private CaseDataDocumentService caseDataDocumentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public CaseDetails<CaseData, State> apply(CaseDetails<CaseData, State> caseDetails) {
        final CaseData caseData = caseDetails.getData();
        final Long caseId = caseDetails.getId();
        final LanguagePreference lang = caseData.getApplicant1().getLanguagePreference();
        final State state = caseDetails.getState();

        if (EnumSet.of(Submitted).contains(state)) {
            log.info("Generating summary document for caseId: {}", caseId);

            @SuppressWarnings("unchecked")
            Map<String, Object> templateContent = objectMapper.convertValue(caseData, Map.class);
            final CompletableFuture<Void> appSummaryEn = CompletableFuture.runAsync(() -> caseDataDocumentService.renderDocumentAndUpdateCaseData(caseData,
                                                                                            APPLICATION_SUMMARY_EN,
                                                                                            templateContent,
                                                                                            caseDetails.getId(),
                                                                                            ADOPTION_APPLICATION_SUMMARY,
                                                                                            LanguagePreference.ENGLISH,
                                                                                            formatDocumentName(caseDetails.getId(), ADOPTION_APPLICATION_FILE_NAME,
                                                                                            LocalDateTime.now())));
            final CompletableFuture<Void> appSummaryCy = CompletableFuture.runAsync(() ->
                                                                                        caseDataDocumentService.renderDocumentAndUpdateCaseData(caseData,
                                                                                            APPLICATION_SUMMARY_CY,
                                                                                            templateContent,
                                                                                            caseDetails.getId(),
                                                                                            ADOPTION_APPLICATION_SUMMARY,
                                                                                            LanguagePreference.WELSH,
                                                                                            formatDocumentName(caseDetails.getId(),
                                                                                            ADOPTION_APPLICATION_FILE_NAME, LocalDateTime.now())));

            CompletableFuture.allOf(appSummaryEn, appSummaryCy).join();
        } else {
            log.error("Could not generate summary document for caseId: {}", caseId);
        }

        return caseDetails;
    }
}
