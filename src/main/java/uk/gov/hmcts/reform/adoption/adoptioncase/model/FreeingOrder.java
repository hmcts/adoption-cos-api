package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@Builder
public class FreeingOrder {

    @CCD(label = "Freeing Order Court")
    private String freeingOrderCourt;

    @CCD(label = "Freeing Order ID")
    private String freeingOrderId;

    @CCD(label = "Freeing Order Type")
    private String freeingOrderType;

    @CCD(label = "Freeing Order Date")
    private String freeingOrderDate;


}
