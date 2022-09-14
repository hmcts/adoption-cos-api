package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.SystemUpdateAccess;

import java.time.LocalDate;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@Builder
public class PlacementOrder {
    @CCD(label = "Order ID", showCondition = "placementOrderNumber=\"\"")
    private String placementOrderId;

    @CCD(label = "Type of order",
        access = {SystemUpdateAccess.class},
        typeOverride = FixedList,
        typeParameterOverride = "PlacementOrderType")
    private PlacementOrderType placementOrderType;

    @CCD(label = "Type of order (Other)")
    private String otherPlacementOrderType;

    @CCD(label = "Order case or serial number")
    private String placementOrderNumber;

    @CCD(label = "Court")
    private String placementOrderCourt;

    @CCD(
        label = "Date",
        access = {SystemUpdateAccess.class}
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate placementOrderDate;
}
