package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.CollectionAccess;

import java.util.List;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sibling {
    @CCD(label = "Sibling Id")
    private String siblingId;

    @CCD(label = "Sibling First Name")
    private String siblingFirstName;

    @CCD(label = "Sibling Last Name")
    private String siblingLastNames;

    @CCD(
        label = "Sibling Placement orders",
        typeOverride = Collection,
        typeParameterOverride = "PlacementOrder",
        access = {CollectionAccess.class}
    )
    private List<ListValue<PlacementOrder>> siblingPlacementOrders;
}
