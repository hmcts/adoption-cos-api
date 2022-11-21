package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdersTabModel {

    @CCD(
        label = "Date of the order",
        access = {DefaultAccess.class}
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOrderMate;

    @CCD(
        label = "Date served",
        access = {DefaultAccess.class}
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateServed;

    @CCD(label = "Order",
        access = {DefaultAccess.class})
    private Document documentLink;

    @CCD(label = "Ordered by",
        access = {DefaultAccess.class})
    private String orderedBy;

    @CCD(label = "Ordered type",
        access = {DefaultAccess.class})
    private String orderType;

    @CCD(label = "Recipients",
        access = {DefaultAccess.class})
    private String recipients;

    @CCD(label = "Status",
        access = {DefaultAccess.class})
    private String status;
}
