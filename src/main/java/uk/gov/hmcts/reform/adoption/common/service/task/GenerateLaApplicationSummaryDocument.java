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

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.LaSubmitted;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.ADOPTION_LA_APPLICATION_SUMMARY;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.ADOPTION_LA_APPLICATION_FILE_NAME;
import static uk.gov.hmcts.reform.adoption.document.DocumentType.APPLICATION_LA_SUMMARY_EN;
import static uk.gov.hmcts.reform.adoption.document.DocumentUtil.formatDocumentName;

/**
 * Helper class targeted for Generating the PDF document for LA Portal Application Submition summary.
 *
 */
@Component
@Slf4j
public class GenerateLaApplicationSummaryDocument implements CaseTask {

    @Autowired
    private CaseDataDocumentService caseDataDocumentService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * This overridden method use caseDataDocumentService to render document and update the details back in CaseData.
     *
     * @return - CaseDetails
     */
    @Override
    public CaseDetails<CaseData, State> apply(CaseDetails<CaseData, State> caseDetails) {
        final CaseData caseData = caseDetails.getData();
        final Long caseId = caseDetails.getId();
        final State state = caseDetails.getState();

        @SuppressWarnings("unchecked")
        Map<String, Object> templateContent = objectMapper.convertValue(caseData, Map.class);

        if (EnumSet.of(LaSubmitted).contains(state)) {
            log.info("Generating summary document for caseId: {}", caseId);
            log.info("templateContent: {}", templateContent);

            final CompletableFuture<Void> appSummaryEn = CompletableFuture
                .runAsync(() -> caseDataDocumentService.renderDocumentAndUpdateCaseData(
                    caseData,
                    APPLICATION_LA_SUMMARY_EN,
                    templateContent,
                    caseDetails.getId(),
                    ADOPTION_LA_APPLICATION_SUMMARY,
                    LanguagePreference.ENGLISH,
                    formatDocumentName(
                        caseDetails.getId(),
                        ADOPTION_LA_APPLICATION_FILE_NAME,
                        LocalDateTime.now()
                    )
                ));

            CompletableFuture.allOf(appSummaryEn).join();
        } else {
            log.error("Could not generate summary document for caseId: {}", caseId);
        }
        return caseDetails;
    }
}
