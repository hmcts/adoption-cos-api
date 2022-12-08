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
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderData {

    @CCD(
        showCondition = "orderId=\"never\"",
        displayOrder = 1
    )
    private String orderId;

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

    @CCD(
        label = "Order"
    )
    private Document documentReview;

    @CCD(label = "Ordered by",
        access = {DefaultAccess.class})
    private String orderedBy;

    @CCD(label = "Ordered type",
        access = {DefaultAccess.class})
    private ManageOrdersData.ManageOrderType manageOrderType;

    @CCD(label = "Status",
        access = {DefaultAccess.class})
    private OrderStatus status;

    @CCD(label = "Recipients",
        access = {DefaultAccess.class})
    private Set<ManageOrdersData.Recipients> adoptionOrderRecipients;

    @CCD(label = "Recipients",
        access = {DefaultAccess.class})
    private Set<DirectionsOrderData.GeneralDirectionRecipients> generalDirectionOrderRecipients;

    @CCD(label = "Recipients of Final adoption order (A76)",
        access = {DefaultAccess.class})
    private Set<AdoptionOrderData.RecipientsA76> finalOrderRecipientsA76;

    @CCD(label = "Recipients of Final adoption order (A206)",
        access = {DefaultAccess.class})
    private Set<AdoptionOrderData.RecipientsA206> finalOrderRecipientsA206;

    @CCD(label = "Recipients",
        access = {DefaultAccess.class})
    private Set<DirectionsOrderData> directionsOrderData;

    @CCD(showCondition = "orderId=\"never\"",
        access = {DefaultAccess.class})
    private LocalDateTime submittedDateAndTimeOfOrder;

}
