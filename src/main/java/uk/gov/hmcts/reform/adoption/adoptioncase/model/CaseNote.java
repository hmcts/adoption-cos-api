package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.TextArea;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseNote {

    @CCD(label = "Subject",
        displayOrder = 0)
    private String subject;

    @CCD(label = "Note",
        hint = "Include relevant dates and the people involved.",
        typeOverride = TextArea,
        displayOrder = 1)
    private String note;




}
