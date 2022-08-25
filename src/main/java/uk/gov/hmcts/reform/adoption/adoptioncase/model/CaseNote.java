package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.TextArea;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseNote {

    @CCD(label = "Subject",
        displayOrder = 1)
    private String subject;

    @CCD(label = "Note",
        hint = "Include relevant dates and the people involved.",
        typeOverride = TextArea,
        displayOrder = 0)
    private String note;
}
