package uk.gov.hmcts.reform.adoption.common.service.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Nationality;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.task.CaseTask;
import uk.gov.hmcts.reform.adoption.document.CaseDataDocumentService;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.LaSubmitted;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.ADOPTION_LAAPPLICATION_FILE_NAME;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.ADOPTION_APPLICATION_LASUMMARY;
import static uk.gov.hmcts.reform.adoption.document.DocumentType.APPLICATION_LASUMMARY_EN;
import static uk.gov.hmcts.reform.adoption.document.DocumentUtil.formatDocumentName;

@Component
@Slf4j
public class GenerateApplicationLaSummaryDocument implements CaseTask {

    @Autowired
    private CaseDataDocumentService caseDataDocumentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public CaseDetails<CaseData, State> apply(CaseDetails<CaseData, State> caseDetails) {
        final CaseData caseData = caseDetails.getData();
        final Long caseId = caseDetails.getId();
        final State state = caseDetails.getState();

        @SuppressWarnings("unchecked")
        Map<String, Object> templateContent = objectMapper.convertValue(caseData, Map.class);
        if (caseData.getBirthMother() != null && caseData.getBirthMother().getNationality() != null) {
            templateContent.put("birthMotherNationality", caseData.getBirthMother().getNationality().stream()
                .filter(item -> item != Nationality.OTHER).collect(
                    Collectors.toCollection(TreeSet<Nationality>::new)));
        }
        if (caseData.getBirthFather() != null && caseData.getBirthFather().getNationality() != null) {
            templateContent.put("birthFatherNationality", caseData.getBirthFather().getNationality().stream()
                .filter(item -> item != Nationality.OTHER).collect(
                    Collectors.toCollection(TreeSet<Nationality>::new)));
        }
        if (caseData.getChildren() != null && caseData.getChildren().getNationality() != null) {
            templateContent.put("childrenNationality", caseData.getChildren().getNationality().stream()
                .filter(item -> item != Nationality.OTHER).collect(
                    Collectors.toCollection(TreeSet<Nationality>::new)));
        }

        log.info("LaSubmitted: {}", LaSubmitted);
        log.info("caseData: {}", caseData);
        log.info("APPLICATION_LASUMMARY_EN: {}", APPLICATION_LASUMMARY_EN);
        log.info("ADOPTION_APPLICATION_LASUMMARY: {}", ADOPTION_APPLICATION_LASUMMARY);
        log.info("templateContent: {}", templateContent);
        log.info("caseDetails.getId(): {}", caseDetails.getId());
        log.info("formatDocumentName: {}", formatDocumentName(
            caseDetails.getId(),
            ADOPTION_LAAPPLICATION_FILE_NAME,
            LocalDateTime.now()
        ));

        if (EnumSet.of(LaSubmitted).contains(state)) {
            log.info("Generating summary document for caseId: {}", caseId);
            caseDataDocumentService.renderDocumentAndUpdateCaseData(
                caseData,
                APPLICATION_LASUMMARY_EN,
                templateContent,
                caseDetails.getId(),
                ADOPTION_APPLICATION_LASUMMARY,
                LanguagePreference.ENGLISH,
                formatDocumentName(
                    caseDetails.getId(),
                    ADOPTION_LAAPPLICATION_FILE_NAME,
                    LocalDateTime.now()
                )
            );
        } else {
            log.error("Could not generate summary document for caseId: {}", caseId);
        }
        return caseDetails;
    }
}
