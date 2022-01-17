package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdditionalName {
    @CCD(
        label = "First name"
    )
    private String firstNames;

    @CCD(
        label = "First name"
    )
    private String lastNames;
}
