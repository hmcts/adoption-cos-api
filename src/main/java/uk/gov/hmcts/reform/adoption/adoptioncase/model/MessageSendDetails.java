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
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
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
    )
    private String messageFrom;


    @CCD(
        displayOrder = 1
    )
    private String messageId;

    @CCD(
        label = "Status",
        displayOrder = 2
    )
    private MessageStatus messageStatus;

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

    @CCD(label = "Message",
        typeOverride = TextArea,
        access = {DefaultAccess.class,SystemUpdateAccess.class}
    )
    private String message;

    @CCD(
        label = "Do you want to attach documents from this case?",
        access = {SystemUpdateAccess.class, DefaultAccess.class}
    )
    private YesNo sendMessageAttachDocument;

    @CCD(
        label = "Select a document",
        access = {DefaultAccess.class,SystemUpdateAccess.class}
    )
    private DynamicList attachDocumentList;

    @CCD(
        access = { SystemUpdateAccess.class,DefaultAccess.class}
    )
    private Document selectedDocument;

    @CCD(
        access = { SystemUpdateAccess.class,DefaultAccess.class}
    )
    private List<Document> documentHistory;

    @CCD(
        access = {DefaultAccess.class})
    private LocalDateTime messageSendDateNTime;

    @CCD(access = {DefaultAccess.class})
    private String messageHistory;



    @Getter
    @AllArgsConstructor
    public enum MessageReceiverRoles implements HasLabel {

        @JsonProperty("judge")
        JUDGE("Judge"),

        @JsonProperty("gatekeeper")
        GATEKEEPER("Gatekeeper (Legal advisor)"),

        @JsonProperty("localCourtAdmin")
        LOCAL_COURT_ADMIN("Local court admin"),

        @JsonProperty("ctsTeamLeader")
        CTS_TEAM_LEADER("CTS team leader"),

        @JsonProperty("ctsTeamWorker")
        CTS_TEAM_WORKER("CTS team worker");

        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum MessageReason implements HasLabel {

        @JsonProperty("referToGatekeeper")
        REFER_TO_GATEKEEPER("Refer to gatekeeper"),

        @JsonProperty("referToJudge")
        REFER_TO_JUDGE("Refer to judge"),

        @JsonProperty("requestReviewDocuments")
        REQUEST_REVIEW_DOCUMENTS("Request review of documents");

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

    @Getter
    @AllArgsConstructor
    public enum YesNo implements HasLabel {

        @JsonProperty("Yes")
        YES("Yes"),
        @JsonProperty("No")
        NO("No");

        private final String label;
    }



}


