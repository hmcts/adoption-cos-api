package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.Event;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.DynamicListElement;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.SendOrReply;
import uk.gov.hmcts.reform.adoption.adoptioncase.event.EventTest;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MessageSendDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.SelectedMessage;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.idam.IdamService;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseworkerSendOrReply.CASEWORKER_SEND_OR_REPLY;
import static uk.gov.hmcts.reform.adoption.adoptioncase.common.CaseDataUtils.archiveListHelper;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.COMMA;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.SEND_N_REPLY_DATE_FORMAT;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_AUTHORIZATION_TOKEN;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@ExtendWith(MockitoExtension.class)
public class CaseworkerSendOrReplyTest extends EventTest {

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private IdamService idamService;

    @Mock
    private Clock clock;

    @InjectMocks
    private SendOrReply sendOrReply;

    @InjectMocks
    private CaseworkerSendOrReply caseworkerSendOrReply;

    @Test
    void caseworkerSendOrReplyConfigure() {
        final ConfigBuilderImpl<CaseData, State, UserRole> configBuilder = createCaseDataConfigBuilder();
        caseworkerSendOrReply.configure(configBuilder);
        assertThat(getEventsFrom(configBuilder).values())
            .extracting(Event::getId)
            .contains(CASEWORKER_SEND_OR_REPLY);
    }

    @Test
    void caseworkerSendOrReplyAboutToSubmit_OK() {
        var caseDetails = getCaseDetails();
        caseDetails.getData().setMessageAction(MessageSendDetails.MessagesAction.SEND_A_MESSAGE);
        caseDetails.getData().setMessageSendDetails(getOpenMessageObject());
        final var instant = Instant.now();
        final var zoneId = ZoneId.systemDefault();
        final var expectedDate = LocalDate.ofInstant(instant, zoneId);
        when(httpServletRequest.getHeader(AUTHORIZATION)).thenReturn(TEST_AUTHORIZATION_TOKEN);
        when(idamService.retrieveUser(TEST_AUTHORIZATION_TOKEN)).thenReturn(getCaseworkerUser());
        var result = caseworkerSendOrReply.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getListOfOpenMessages()).hasSize(1);
    }

    @Test
    void caseworkerSendOrReplyAboutToStartEventTest_Ok() {
        var caseDetails = getCaseDetails();
        List<ListValue<MessageSendDetails>> listOfOpenMessage = new ArrayList<>();
        archiveListHelper(listOfOpenMessage, getOpenMessageObject());
        caseDetails.getData().setListOfOpenMessages(listOfOpenMessage);
        when(httpServletRequest.getHeader(AUTHORIZATION)).thenReturn(TEST_AUTHORIZATION_TOKEN);

        when(idamService.retrieveUser(TEST_AUTHORIZATION_TOKEN)).thenReturn(getCaseworkerUser());
        var result = caseworkerSendOrReply.beforeStartEvent(caseDetails);
        assertThat(result.getData().getReplyMsgDynamicList()).isNotNull();
    }

    @Test
    @DisplayName("Testing a scenario of replying to existing a  message")
    void caseworkerSendOrReplyAboutToSubmitEventTest_ReplyMessage() {
        var caseDetails = getCaseDetails();
        List<ListValue<MessageSendDetails>> listOfOpenMessage = new ArrayList<>();
        caseDetails.getData().setListOfOpenMessages(archiveListHelper(listOfOpenMessage, getOpenMessageObject()
        ));
        caseDetails.getData().setMessageAction(MessageSendDetails.MessagesAction.REPLY_A_MESSAGE);
        MessageSendDetails latestMessage = getOpenMessageObject();
        latestMessage.setMessageText("latest");
        caseDetails.getData().setMessageSendDetails(latestMessage);
        SelectedMessage selectedMessage = new SelectedMessage();
        MessageSendDetails messageSendDetails = getOpenMessageObject();
        selectedMessage.setMessageId(messageSendDetails.getMessageId());
        selectedMessage.setMessageContent(messageSendDetails.getMessageText());
        selectedMessage.setReplyMessage(YesOrNo.YES);
        selectedMessage.setReasonForMessage(MessageSendDetails.MessageReason.LEAVE_TO_OPPOSE.toString());
        caseDetails.getData().setSelectedMessage(selectedMessage);
        List<DynamicListElement> replyMessageList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(caseDetails.getData().getListOfOpenMessages())) {
            caseDetails.getData().getListOfOpenMessages().forEach(item -> {
                if (item.getValue().getMessageStatus().equals(MessageSendDetails.MessageStatus.OPEN)) {
                    DynamicListElement orderInfo = DynamicListElement.builder().label(item.getValue().getMessageSendDateNTime().format(
                        DateTimeFormatter.ofPattern(SEND_N_REPLY_DATE_FORMAT)).concat(COMMA)
                        .concat(item.getValue().getMessageReasonList().getLabel()))
                        .code(UUID.fromString(item.getValue().getMessageId())).build();
                    replyMessageList.add(orderInfo);
                }
            });

        }
        caseDetails.getData().setReplyMsgDynamicList(DynamicList.builder().listItems(replyMessageList).value(
            replyMessageList.get(0)).build());
        final var instant = Instant.now();
        final var zoneId = ZoneId.systemDefault();
        final var expectedDate = LocalDate.ofInstant(instant, zoneId);
        when(httpServletRequest.getHeader(AUTHORIZATION)).thenReturn(TEST_AUTHORIZATION_TOKEN);
        when(idamService.retrieveUser(TEST_AUTHORIZATION_TOKEN)).thenReturn(getCaseworkerUser());
        var result = caseworkerSendOrReply.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getListOfOpenMessages()).hasSize(1);
        assertThat(result.getData().getListOfOpenMessages().get(0).getValue().getMessageId()).isEqualTo(latestMessage.getMessageId());
    }

    @Test
    @DisplayName("Testing a scenario of closing a message without replying")
    void caseworkerSendOrReplyAboutToSubmitEventTest_Ok_ReplyMessage_closed() {
        var caseDetails = getCaseDetails();
        List<ListValue<MessageSendDetails>> listOfOpenMessage = new ArrayList<>();
        MessageSendDetails messageSendDetails = getOpenMessageObject();
        caseDetails.getData().setListOfOpenMessages(archiveListHelper(listOfOpenMessage,getOpenMessageObject()
        ));
        caseDetails.getData().setMessageAction(MessageSendDetails.MessagesAction.REPLY_A_MESSAGE);
        SelectedMessage selectedMessage = new SelectedMessage();
        selectedMessage.setMessageId(messageSendDetails.getMessageId());
        selectedMessage.setMessageContent(messageSendDetails.getMessageText());
        selectedMessage.setReplyMessage(YesOrNo.NO);
        selectedMessage.setReasonForMessage(MessageSendDetails.MessageReason.LEAVE_TO_OPPOSE.toString());
        caseDetails.getData().setSelectedMessage(selectedMessage);
        List<DynamicListElement> replyMessageList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(caseDetails.getData().getListOfOpenMessages())) {
            caseDetails.getData().getListOfOpenMessages().forEach(item -> {
                if (item.getValue().getMessageStatus().equals(MessageSendDetails.MessageStatus.OPEN)) {
                    DynamicListElement orderInfo = DynamicListElement.builder().label(item.getValue().getMessageSendDateNTime().format(
                            DateTimeFormatter.ofPattern(SEND_N_REPLY_DATE_FORMAT))
                            .concat(COMMA).concat(item.getValue().getMessageReasonList().getLabel()))
                            .code(UUID.fromString(item.getValue().getMessageId())).build();
                    replyMessageList.add(orderInfo);
                }
            });

        }
        caseDetails.getData().setReplyMsgDynamicList(DynamicList.builder().listItems(replyMessageList).value(
            replyMessageList.get(0)).build());
        final var instant = Instant.now();
        final var zoneId = ZoneId.systemDefault();
        final var expectedDate = LocalDate.ofInstant(instant, zoneId);
        when(httpServletRequest.getHeader(AUTHORIZATION)).thenReturn(TEST_AUTHORIZATION_TOKEN);
        when(idamService.retrieveUser(TEST_AUTHORIZATION_TOKEN)).thenReturn(getJudgeUser());
        var result = caseworkerSendOrReply.aboutToSubmit(caseDetails, caseDetails);
        assertThat(caseDetails.getData().getListOfOpenMessages()).isEmpty();
        assertThat(caseDetails.getData().getClosedMessages()).hasSize(1);

    }

    @NotNull
    private MessageSendDetails getOpenMessageObject() {
        MessageSendDetails message = new MessageSendDetails();
        message.setMessageId(UUID.randomUUID().toString());
        message.setMessageStatus(MessageSendDetails.MessageStatus.OPEN);
        message.setMessageSendDateNTime(LocalDateTime.now());
        message.setMessageText("message1");
        message.setMessageReasonList(MessageSendDetails.MessageReason.LEAVE_TO_OPPOSE);
        return message;
    }

    private CaseDetails<CaseData, State> getCaseDetails() {
        return CaseDetails.<CaseData, State>builder()
            .data(caseData())
            .id(1L)
            .build();
    }
}
