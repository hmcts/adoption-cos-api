package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.SystemUpdateAccess;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedRadioList;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedList;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.TextArea;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageSendDetails {

    @CCD(
        displayOrder = 1
    )
    private String messageId;

    @CCD(
        label = "Who do you want to send a message to?",
        access = { SystemUpdateAccess.class, DefaultAccess.class},
        typeOverride = FixedRadioList,
        typeParameterOverride = "MessageReceiverRoles")
    private MessageReceiverRoles messageReceiverRoles;

    @CCD(label = "Select reason for this message",
        typeOverride = FixedList,
        access = {DefaultAccess.class},
        typeParameterOverride = "MessageReason"
    )
    private MessageReason messageReasonList;

    @CCD(label = "Urgency",
        typeOverride = FixedList,
        access = {DefaultAccess.class},
        typeParameterOverride = "MessageUrgency"
    )
    private MessageUrgency messageUrgencyList;

    @CCD(
        label = "Do you want to attach documents from this case?",
        access = {SystemUpdateAccess.class, DefaultAccess.class}
    )
    private YesOrNo attachDocument;

    @CCD(label = "Message",
        typeOverride = TextArea,
        access = {SystemUpdateAccess.class, DefaultAccess.class}
    )
    private String message;


    @CCD(
        access = {DefaultAccess.class},
        label = "Select a document"
    )
    private DynamicList attachDocumentList;
}
