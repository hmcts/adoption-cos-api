package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.SystemUpdateAccess;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdoptionSeekFurtherInformation {

    @CCD(
        label = "Date"
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate seekFurtherInfoDateAdded;

    @CCD(label = "Information needed",
        access = {SystemUpdateAccess.class,
            DefaultAccess.class}
    )
    private LocalDateTime date;

    @CCD(
        label = "Uploaded by\n"
    )
    private String uploadedBy;

    @CCD(
        label = "List the documents you need\n"
    )
    private String documentRequired;

    @CCD(
        label = "List the questions you want to ask\n"
    )
    private String questions;
}
