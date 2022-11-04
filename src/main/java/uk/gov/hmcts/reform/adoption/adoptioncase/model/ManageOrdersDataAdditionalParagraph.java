package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.TextArea;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManageOrdersDataAdditionalParagraph {
    @CCD(label = "Copy and paste or type any additional orders directly into the text box. "
        + "You can include any orders for step parents, for example, or inclusion of other parties to attend.",
        typeOverride = TextArea)
    private String additionalParagraphTA;
}
