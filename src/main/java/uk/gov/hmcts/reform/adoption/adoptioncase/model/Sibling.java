package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sibling {
    @CCD(label = "Sibling Id")
    private String siblingId;

    @CCD(label = "Sibling Relation")
    private String siblingRelation;

    @CCD(label = "Sibling Placement Order Type")
    private String siblingPoType;

    @CCD(label = "Sibling Placement Order Number")
    private String siblingPoNumber;
}
