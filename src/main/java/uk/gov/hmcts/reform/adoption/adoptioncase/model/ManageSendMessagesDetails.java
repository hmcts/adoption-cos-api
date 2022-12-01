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

import static uk.gov.hmcts.ccd.sdk.type.FieldType.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManageSendMessagesDetails {

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

    @CCD(label = "Select reason for message",
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

    @CCD(label = "Message",
        typeOverride = TextArea,
        access = {SystemUpdateAccess.class, DefaultAccess.class}
    )
    private String message;


    @CCD(
        label = "Attach document for this case to this message?",
        access = {SystemUpdateAccess.class, DefaultAccess.class}
    )
    private YesOrNo attachDocument;

    @CCD(
        typeOverride = DynamicRadioList,
        label = "Documents\n"
    )
    private DynamicList documentList;
}
