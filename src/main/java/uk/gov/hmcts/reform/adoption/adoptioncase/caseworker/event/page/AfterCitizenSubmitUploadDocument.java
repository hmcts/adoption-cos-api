package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.document.DocumentType;

import java.util.Collections;

public class AfterCitizenSubmitUploadDocument implements CcdPageConfiguration {

    @Override
    public void addTo(final PageBuilder pageBuilder) {
        pageBuilder.page("answerReceivedUploadDocument", this::midEvent)
            .pageLabel("Upload document")
            .mandatory(CaseData::getAdoptionDocument)
            .done();
    }

    public AboutToStartOrSubmitResponse<CaseData, State> midEvent(
        CaseDetails<CaseData, State> details,
        CaseDetails<CaseData, State> detailsBefore
    ) {
        CaseData caseData = details.getData();
        DocumentType documentType = caseData.getAdoptionDocument().getDocumentType();

        if (!DocumentType.BIRTH_OR_ADOPTION_CERTIFICATE.equals(documentType)) {
            return AboutToStartOrSubmitResponse.<CaseData, State>builder()
                .errors(Collections.singletonList("Please upload a adoption document type"))
                .build();
        }

        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .build();
    }
}
