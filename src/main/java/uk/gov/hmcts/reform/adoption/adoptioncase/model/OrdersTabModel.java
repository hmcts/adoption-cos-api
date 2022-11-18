package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdersTabModel {

    @CCD(label = "Order",
        access = {DefaultAccess.class})
    private String order;

    @CCD(label = "Ordered type",
        access = {DefaultAccess.class})
    private String orderType;

    @CCD(label = "Date issued",
        access = {DefaultAccess.class})
    private LocalDateTime dateIssued;

    @CCD(label = "Ordered by",
        access = {DefaultAccess.class})
    private String orderedBy;




}
