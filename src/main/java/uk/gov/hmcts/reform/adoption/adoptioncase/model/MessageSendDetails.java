package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;
import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.SystemUpdateAccess;

import java.time.LocalDateTime;
import java.util.List;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedRadioList;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedList;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.TextArea;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageSendDetails {

    @CCD(
        label = "From",
        displayOrder = 1
    )
    private String messageFrom;


    @CCD(
        displayOrder = 1,
        showCondition = "messageStatus=\"NEVER_SHOW\""
    )
    private String messageId;

    @CCD(
        label = "Status"

    )
    private MessageStatus messageStatus;

    @CCD(
        label = "Who do you want to send a message to ?",
        access = {DefaultAccess.class},
        typeOverride = FixedRadioList,
        typeParameterOverride = "MessageReceiverRoles")
    private MessageReceiverRoles messageReceiverRoles;

    @CCD(
        typeOverride = FixedList,
        label = "Select a reason for this message",
        typeParameterOverride = "MessageReason",
        inheritAccessFromParent = false,
        access = {DefaultAccess.class}
    )
    private MessageReason messageReasonList;


    @CCD(
        typeOverride = FixedList,
        label = "Select a reason for this message",
        access = {DefaultAccess.class},
        inheritAccessFromParent = false,
        typeParameterOverride = "MessageReasonJudge"
    )
    private MessageReasonJudge messageReasonJudge;

    @CCD(label = "Urgency",
        typeOverride = FixedList,
        access = {DefaultAccess.class},
        displayOrder = 4,
        typeParameterOverride = "MessageUrgency"
    )
    private MessageUrgency messageUrgencyList;

    @CCD(label = "Message",
        hint = "Enter your message",
        typeOverride = TextArea,
        displayOrder = 6,
        access = {DefaultAccess.class,SystemUpdateAccess.class}
    )
    private String messageText;

    @CCD(
        access = { SystemUpdateAccess.class,DefaultAccess.class},
        displayOrder = 7,
        label = "Attached documents from the case"
    )
    private Document selectedDocument;

    @CCD(access = {DefaultAccess.class},
        label = "Message history",
        displayOrder = 8,
        typeOverride = TextArea)
    private String messageHistory;

    @CCD(
        access = { SystemUpdateAccess.class,DefaultAccess.class},
        displayOrder = 9,
        label = "Document history"
    )
    private List<ListValue<Document>> documentHistory;

    @CCD(
        access = {DefaultAccess.class},
        label = "Date sent",
        displayOrder = 3)
    private LocalDateTime messageSendDateNTime;


    @Getter
    @AllArgsConstructor
    public enum MessageReceiverRoles implements HasLabel {

        @JsonProperty("judge")
        JUDGE("Judge"),

        @JsonProperty("gatekeeper")
        GATEKEEPER("Gatekeeper (Legal advisor)"),

        @JsonProperty("localCourtAdmin")
        LOCAL_COURT_ADMIN("Local court admin"),

        @JsonProperty("ctscTeamLeader")
        CTSC_TEAM_LEADER("CTSC team leader"),

        @JsonProperty("ctscTeamWorker")
        CTSC_CASE_WORKER("CTSC case worker");

        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum MessageReason implements HasLabel {

        @JsonProperty("referForGateKeeping")
        REFER_FOR_GATEKEEPING("Refer for gatekeeping"),

        @JsonProperty("laApplicationNotReceived")
        LOCAL_AUTHORITY_APPLICATION("Local authority application response not received"),

        @JsonProperty("annexAReview")
        ANNEX_A("Annex A for review"),

        @JsonProperty("correspondanceForReview")
        CORRESPONDANCE_FOR_REVIEW("Correspondence for review"),

        @JsonProperty("reviewDocument")
        DOCUMENT_FOR_REVIEW("Document for review"),

        @JsonProperty("approvalOrder")
        ORDER_FOR_APPROVAL("Order for approval"),

        @JsonProperty("approvalOrder")
        LEAVE_TO_OPPOSE("Leave to oppose received"),

        @JsonProperty("approvalOrder")
        GENERAL_QUERY("General query"),

        @JsonProperty("approvalOrder")
        REQUEST_HEARING_DATE("Request hearing date (for CTSC caseworkers to send to LA Admin)");

        private final String label;



    }

    @Getter
    @AllArgsConstructor
    public enum MessageReasonJudge implements HasLabel {

        @JsonProperty("listForAHearing")
        LIST_A_HEARING("List for a hearing"),

        @JsonProperty("requestDocument")
        REQUEST_DOCUMENT("Request document"),

        @JsonProperty("requestInfo")
        REQUEST_IFNORMATION("Request information"),

        @JsonProperty("createOrder")
        CREATE_ORDER("Create order"),

        @JsonProperty("returnOrder")
        RETURN_ORDER("Return order for amendments"),

        @JsonProperty("serveDocument")
        SERVE_DOCUMENT("Serve document"),

        @JsonProperty("sendALetter")
        SEND_A_LETTER("Send a letter"),

        @JsonProperty("generalQuery")
        GENERAL_QUERY("General query");


        private final String label;

    }

    @Getter
    @AllArgsConstructor
    public enum MessagesAction implements HasLabel {

        @JsonProperty("sendMessage")
        SEND_A_MESSAGE("Send a message"),

        @JsonProperty("replyMessage")
        REPLY_A_MESSAGE("Reply to a message");

        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum MessageUrgency implements HasLabel {

        @JsonProperty("high")
        HIGH("High"),

        @JsonProperty("medium")
        MEDIUM("Medium"),

        @JsonProperty("low")
        LOW("Low");

        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum MessageStatus implements HasLabel {

        @JsonProperty("open")
        OPEN("Open"),

        @JsonProperty("closed")
        CLOSED("Closed");

        private final String label;
    }


}


