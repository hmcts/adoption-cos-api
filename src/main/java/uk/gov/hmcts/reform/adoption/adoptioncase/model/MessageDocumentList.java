package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import lombok.*;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MessageDocumentList {
    @CCD(
        label = "Message Id"
    )
    private String messageId;

    @CCD(
        label = "Document",
        hint = "The selected file must be smaller than 1GB"
    )
    private Document documentLink;

}
