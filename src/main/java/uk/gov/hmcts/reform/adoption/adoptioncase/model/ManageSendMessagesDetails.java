package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.SystemUpdateAccess;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedRadioList;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedList;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.DynamicRadioList;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.TextArea;

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
        label = "Who needs to get this message?",
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
        typeOverride = TextArea
    )
    private String message;


    @CCD(
        label = "Attach document for this case to this message?",
        access = {SystemUpdateAccess.class}
    )
    private YesNo attachDocument;

    @CCD(
        typeOverride = DynamicRadioList,
        label = "Documents\n"
    )
    private DynamicList documentList;
}
