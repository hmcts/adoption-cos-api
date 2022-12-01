package uk.gov.hmcts.reform.adoption.adoptioncase.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderData {

    private String orderId;

    private ManageOrdersData.ManageOrderType manageOrderType;

    private LocalDateTime submittedDateAndTimeOfOrder;

}
