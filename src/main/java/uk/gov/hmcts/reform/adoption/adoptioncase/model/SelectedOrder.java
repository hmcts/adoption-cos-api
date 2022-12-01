package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.SystemUpdateAccess;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SelectedOrder {

    @CCD(
        label = "Document to review",
        hint = "This document will open in a new page when you select it.",
        access = { SystemUpdateAccess.class, DefaultAccess.class}
    )
    private Document documentReview;

    @CCD(
        access = { SystemUpdateAccess.class, DefaultAccess.class}
    )
    private Set<ManageOrdersData.Recipients> adoptionOrderRecipients;


    private ManageOrdersData.ManageOrderType orderType;

    @CCD(
        label = "Recipients of Final adoption order (A76)",
        access = { SystemUpdateAccess.class, DefaultAccess.class}
    )
    private Set<AdoptionOrderData.RecipientsA76> finalOrderRecipientsA76;

    @CCD(
        label = "Recipients of Final adoption order (A206)",
        access = { SystemUpdateAccess.class, DefaultAccess.class}
    )
    private Set<AdoptionOrderData.RecipientsA206> finalOrderRecipientsA206;

    @CCD(
        access = { SystemUpdateAccess.class, DefaultAccess.class}
    )
    private OrderStatus orderStatus;


}
