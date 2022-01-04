package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Builder
public class PlacementOrder {
    @CCD(label = "Placement Order Number")
    private String number;

    @CCD(label = "Placement Order Court")
    private String court;

    @CCD(
        label = "Placement Order Date",
        access = {DefaultAccess.class}
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
