package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.SystemUpdateAccess;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SelectedMessage {

    @CCD(
        displayOrder = 1
    )
    private String messageId;

    @CCD(
        label = "Reason for message",
        displayOrder = 2
    )
    private String reasonForMessage;

    @CCD(
        label = "Urgency",
        displayOrder = 3
    )
    private String urgency;

    @CCD(
        label = "Message",
        displayOrder = 4
    )
    private String message;


    @CCD(
        label = "Document",
        access = { SystemUpdateAccess.class,DefaultAccess.class}
    )
    private Document documentLink;

    @CCD(
        label = "Do you want to reply to this message?",
        access = {SystemUpdateAccess.class}
    )
    private YesOrNo replyMessage;

}
