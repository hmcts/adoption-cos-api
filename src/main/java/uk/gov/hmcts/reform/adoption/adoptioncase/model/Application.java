package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Application {
    @CCD(ignore = true)
    private static final int SUBMISSION_RESPONSE_DAYS = 14;

    @CCD(
        label = "Are there any ongoing family court proceedings involving both of you?",
        access = {DefaultAccess.class}
    )
    private YesOrNo applicantHasOngoingCourtProceedings;

    @CCD(
        label = "Date when the application was created",
        access = {DefaultAccess.class}
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdDate;
}
