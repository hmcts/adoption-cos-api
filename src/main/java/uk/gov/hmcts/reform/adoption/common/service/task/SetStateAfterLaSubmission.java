package uk.gov.hmcts.reform.adoption.common.service.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.task.CaseTask;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.LaSubmitted;


@Component
@Slf4j
public class SetStateAfterLaSubmission implements CaseTask {

    @Override
    public CaseDetails<CaseData, State> apply(final CaseDetails<CaseData, State> caseDetails) {
        caseDetails.setState(LaSubmitted);
        caseDetails.getData().setStatus(LaSubmitted);
        caseDetails.getData().getPlacementOrders()
            .stream()
            .filter(item -> item.getValue().getPlacementOrderType() == null)
            .findFirst()
            .ifPresent(item -> item.setValue(PlacementOrder
                                                 .builder()
                                                 .placementOrderId(item.getValue().getPlacementOrderId())
                                                 .placementOrderNumber(item.getValue().getPlacementOrderNumber())
                                                 .placementOrderType(PlacementOrderType.PLACEMENT_ORDER)
                                                 .placementOrderCourt(item.getValue().getPlacementOrderCourt())
                                                 .placementOrderDate(item.getValue().getPlacementOrderDate())
                                                 .otherPlacementOrderType(item.getValue().getOtherPlacementOrderType())
                                                 .build()));
        log.info("State set to {}, CaseID {}", caseDetails.getState(), caseDetails.getId());

        return caseDetails;
    }
}
