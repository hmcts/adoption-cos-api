package uk.gov.hmcts.reform.adoption.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.OtherParty;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedRadioList;

@Data
@AllArgsConstructor
@Builder
@ToString
public class DocumentSubmitter {

    @CCD(
        label = "Who submitted the document?",
        //hint = "If you want to upload more than one, you need to go through the steps again from the documents tab.",
        typeOverride = FixedRadioList,
        typeParameterOverride = "DocumentSubmittedBy"
    )
    private DocumentSubmittedBy documentSubmittedBy;

    @CCD(
        label = "Other Party"
    )
    private OtherParty otherParty;
}
