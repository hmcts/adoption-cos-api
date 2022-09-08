package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.SystemUpdateAccess;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sibling {
    @CCD(label = "Sibling Id")
    private String siblingId;

    @CCD(label = "Relationship",
        access = {SystemUpdateAccess.class},
        typeOverride = FixedList,
        typeParameterOverride = "SiblingRelation")
    private SiblingRelation siblingRelation;

    @CCD(label = "Type of order",
        access = {SystemUpdateAccess.class},
        typeOverride = FixedList,
        typeParameterOverride = "SiblingPoType")
    private SiblingPoType siblingPoType;

    @CCD(label = "Type of order")
    private String siblingPlacementOtherType;

    @CCD(label = "Order case or serial number")
    private String siblingPoNumber;
}
