package uk.gov.hmcts.reform.adoption.caseworker.event.page;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.GeneralOrder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;

import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class CreateGeneralOrder implements CcdPageConfiguration {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder.page("CreateGeneralOrder", this::midEvent)
            .complex(CaseData::getGeneralOrder)
                .mandatory(GeneralOrder::getGeneralOrderDate)
                .mandatory(GeneralOrder::getGeneralOrderDivorceParties)
                .optional(GeneralOrder::getGeneralOrderRecitals)
                .mandatory(GeneralOrder::getGeneralOrderJudgeType)
                .mandatory(GeneralOrder::getGeneralOrderJudgeName)
                .mandatory(GeneralOrder::getGeneralOrderLegalAdvisorName)
                .mandatory(GeneralOrder::getGeneralOrderDetails)
                .done();
    }

    public AboutToStartOrSubmitResponse<CaseData, State> midEvent(
        CaseDetails<CaseData, State> details,
        CaseDetails<CaseData, State> detailsBefore
    ) {
        var caseDataCopy = details.getData().toBuilder().build();
        var generalOrder = caseDataCopy.getGeneralOrder();

        final Long caseId = details.getId();

        log.info("Mid-event callback triggered for CreateGeneralOrder {}", caseId, formatter);

        caseDataCopy.setGeneralOrder(generalOrder);

        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseDataCopy)
            .build();
    }
}
