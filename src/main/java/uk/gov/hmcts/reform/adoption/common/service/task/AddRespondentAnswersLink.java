package uk.gov.hmcts.reform.adoption.common.service.task;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.divorcecase.model.CaseData;
import uk.gov.hmcts.reform.adoption.divorcecase.model.State;
import uk.gov.hmcts.reform.adoption.divorcecase.task.CaseTask;
import uk.gov.hmcts.reform.adoption.document.model.DivorceDocument;

import java.util.Collection;
import java.util.stream.Stream;

import static uk.gov.hmcts.reform.adoption.document.model.DocumentType.RESPONDENT_ANSWERS;

@Component
public class AddRespondentAnswersLink implements CaseTask {

    @Override
    public CaseDetails<CaseData, State> apply(CaseDetails<CaseData, State> caseDetails) {
        final CaseData caseData = caseDetails.getData();

        Stream.ofNullable(caseData.getDocumentsGenerated())
            .flatMap(Collection::stream)
            .map(ListValue::getValue)
            .filter(divorceDocument ->
                RESPONDENT_ANSWERS.equals(divorceDocument.getDocumentType()))
            .map(DivorceDocument::getDocumentLink)
            .findFirst()
            .ifPresent(file -> caseData.getConditionalOrder().setRespondentAnswersLink(file));
        return caseDetails;
    }
}

