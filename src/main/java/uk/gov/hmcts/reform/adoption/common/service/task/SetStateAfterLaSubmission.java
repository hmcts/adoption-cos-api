package uk.gov.hmcts.reform.adoption.common.service.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.PlacementOrder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.PlacementOrderType;
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
        caseDetails.getData().getBirthMother()
            .setDeceased(caseDetails.getData().getBirthMother().getStillAlive()
                             .equals(YesOrNo.YES) ? YesOrNo.NO.getValue() : YesOrNo.YES.getValue());
        if (caseDetails.getData().getBirthFather() != null && caseDetails.getData().getBirthFather().getStillAlive() != null) {
            caseDetails.getData().getBirthFather()
                .setDeceased(caseDetails.getData().getBirthFather().getStillAlive()
                                 .equals(YesOrNo.YES) ? YesOrNo.NO.getValue() : YesOrNo.YES.getValue());
        }
        caseDetails.getData().getPlacementOrders()
            .stream()
            .filter(item -> item.getValue().getPlacementOrderType() == null)
            .findFirst()
            .ifPresent(item -> {
                caseDetails.getData().setPlacementOrder(PlacementOrder
                    .builder()
                    .placementOrderId(item.getValue().getPlacementOrderId())
                    .placementOrderNumber(item.getValue().getPlacementOrderNumber())
                    .placementOrderType(PlacementOrderType.PLACEMENT_ORDER)
                    .placementOrderCourt(item.getValue().getPlacementOrderCourt())
                    .placementOrderDate(item.getValue().getPlacementOrderDate())
                    .otherPlacementOrderType(item.getValue().getOtherPlacementOrderType())
                    .build());
                caseDetails.getData().getPlacementOrders().remove(item);
            });
        log.info("State set to {}, CaseID {}", caseDetails.getState(), caseDetails.getId());

        return caseDetails;
    }
}
