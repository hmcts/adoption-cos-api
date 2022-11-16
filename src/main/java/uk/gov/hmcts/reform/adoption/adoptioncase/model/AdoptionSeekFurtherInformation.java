package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.SystemUpdateAccess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.*;

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

    @CCD(
        typeOverride = DynamicRadioList,
        label = "Who do you need to contact\n",
        typeParameterOverride = "DocumentSubmitter"
    )
    private DynamicList seekFurtherInformationList;

    @CCD(access = {DefaultAccess.class},
        label = "What information do you need?\n",
        typeOverride = MultiSelectList,
        typeParameterOverride = "FurtherInformation")
    private Set<FurtherInformation> furtherInformation;

    @CCD(access = {DefaultAccess.class},
        label = "List the documents you need",
        typeOverride = TextArea)
    private String askForAdditionalDocumentText;

    @CCD(access = {DefaultAccess.class},
        label = "List the questions you need",
        typeOverride = TextArea)
    private String askAQuestionText;

    @CCD(label = "When is the information needed by?",
        access = {SystemUpdateAccess.class,
            DefaultAccess.class}
    )
    private LocalDateTime date;
}
