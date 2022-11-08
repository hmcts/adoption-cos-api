package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.TextArea;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdoptionOrderData {

    @CCD(hint = "Copy and paste or type directly into the text box",
        typeOverride = TextArea,
        access = {DefaultAccess.class})
    private String preambleDetailsFinalAdoptionOrder;

    @CCD(hint = "Enter the name of the judge issuing this order",
        access = {DefaultAccess.class})
    private String orderedByFinalAdoptionOrder;

    @CCD(access = {DefaultAccess.class})
    private String costOrdersFinalAdoptionOrder;

}
