package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;

import java.time.LocalDate;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.TextArea;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseNote {

    @CCD(label = "Subject",
        displayOrder = 1)
    private String subject;

    @CCD(label = "Case note",
        hint = "Include relevant dates and the people involved.",
        typeOverride = TextArea,
        displayOrder = 2)
    private String note;

    @CCD(
        label = "User",
        displayOrder = 3
    )
    private String user;

    @CCD(
        label = "Date added",
        displayOrder = 4
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

}
