package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.ccd.sdk.ResolvedCCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.Event;
import uk.gov.hmcts.ccd.sdk.api.HasRole;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.DynamicListElement;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.SendOrReply;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.SelectedMessage;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MessageSendDetails;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.idam.client.models.User;
import uk.gov.hmcts.reform.idam.client.models.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.commons.util.ReflectionUtils.findMethod;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseworkerSendOrReply.CASEWORKER_SEND_OR_REPLY;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.COMMA;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.SEND_N_REPLY_DATE_FORMAT;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_AUTHORIZATION_TOKEN;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@ExtendWith(MockitoExtension.class)
public class CaseworkerSendOrReplyTest {

    @Mock
    private Clock clock;

    @InjectMocks
    private SendOrReply sendOrReply;
    @InjectMocks
    private CaseworkerSendOrReply caseworkerSendOrReply;

    @Mock
    private IdamService idamService;

    @Mock
    private HttpServletRequest request;



    public static ConfigBuilderImpl<CaseData, State, UserRole> createCaseDataConfigBuilder() {
        return new ConfigBuilderImpl<>(new ResolvedCCDConfig<>(
            CaseData.class,
            State.class,
            UserRole.class,
            new HashMap<>(),
            ImmutableSet.copyOf(State.class.getEnumConstants())
        ));
    }


    @SuppressWarnings({"unchecked"})
    public static <T, S, R extends HasRole> Map<String, Event<T, R, S>> getEventsFrom(
        final ConfigBuilderImpl<T, S, R> configBuilder) {

        return (Map<String, Event<T, R, S>>) findMethod(ConfigBuilderImpl.class, "getEvents")
            .map(method -> {
                try {
                    method.setAccessible(true);
                    return method.invoke(configBuilder);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new AssertionError("Unable to invoke ConfigBuilderImpl.class method getEvents", e);
                }
            })
            .orElseThrow(() -> new AssertionError("Unable to find ConfigBuilderImpl.class method getEvents"));
    }

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
        when(request.getHeader(AUTHORIZATION)).thenReturn(TEST_AUTHORIZATION_TOKEN);
        when(idamService.retrieveUser(TEST_AUTHORIZATION_TOKEN)).thenReturn(getCaseworkerUser());
        var result = caseworkerSendOrReply.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getListOfOpenMessages()).hasSize(1);
    }

    @Test
    void caseworkerSendOrReplyAboutToStartEventTest_Ok() {
        var caseDetails = getCaseDetails();
        List<ListValue<MessageSendDetails>> listOfOpenMessage = new ArrayList<>();
        caseDetails.getData().archiveManageOrdersHelper(listOfOpenMessage, getOpenMessageObject());
        caseDetails.getData().setListOfOpenMessages(listOfOpenMessage);
        var result = caseworkerSendOrReply.beforeStartEvent(caseDetails);
        assertThat(result.getData().getReplyMsgDynamicList()).isNotNull();
    }

    @Test
    @DisplayName("Testing a scenario of replying to existing a  message")
    void caseworkerSendOrReplyAboutToSubmitEventTest_ReplyMessage() {
        var caseDetails = getCaseDetails();
        List<ListValue<MessageSendDetails>> listOfOpenMessage = new ArrayList<>();
        caseDetails.getData().setListOfOpenMessages(caseDetails.getData().archiveManageOrdersHelper(listOfOpenMessage,
                                                                                                    getOpenMessageObject()
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
        selectedMessage.setReasonForMessage(MessageSendDetails.MessageReason.LIST_A_HEARING.toString());
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
        when(request.getHeader(AUTHORIZATION)).thenReturn(TEST_AUTHORIZATION_TOKEN);
        when(idamService.retrieveUser(TEST_AUTHORIZATION_TOKEN)).thenReturn(getCaseworkerUser());
        var result = caseworkerSendOrReply.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getListOfOpenMessages()).hasSize(1);
        assertThat(result.getData().getListOfOpenMessages().get(0).getValue().getMessageId().equals(selectedMessage.getMessageId()));
    }

    @Test
    @DisplayName("Testing a scenario of closing a message without replying")
    void caseworkerSendOrReplyAboutToSubmitEventTest_Ok_ReplyMessage_closed() {
        var caseDetails = getCaseDetails();
        List<ListValue<MessageSendDetails>> listOfOpenMessage = new ArrayList<>();
        MessageSendDetails messageSendDetails = getOpenMessageObject();
        caseDetails.getData().setListOfOpenMessages(caseDetails.getData().archiveManageOrdersHelper(listOfOpenMessage,
                                                                                                    getOpenMessageObject()
        ));
        caseDetails.getData().setMessageAction(MessageSendDetails.MessagesAction.REPLY_A_MESSAGE);
        SelectedMessage selectedMessage = new SelectedMessage();
        selectedMessage.setMessageId(messageSendDetails.getMessageId());
        selectedMessage.setMessageContent(messageSendDetails.getMessageText());
        selectedMessage.setReplyMessage(YesOrNo.NO);
        selectedMessage.setReasonForMessage(MessageSendDetails.MessageReason.LIST_A_HEARING.toString());
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
        when(request.getHeader(AUTHORIZATION)).thenReturn(TEST_AUTHORIZATION_TOKEN);
        when(idamService.retrieveUser(TEST_AUTHORIZATION_TOKEN)).thenReturn(getCaseworkerUser());
        var result = caseworkerSendOrReply.aboutToSubmit(caseDetails, caseDetails);
        assertThat(caseDetails.getData().getListOfOpenMessages()).hasSize(0);
        assertThat(caseDetails.getData().getClosedMessages()).hasSize(1);

    }


    private User getCaseworkerUser() {
        UserDetails userDetails = UserDetails
            .builder()
            .forename("testFname")
            .surname("testSname")
            .build();

        return new User(TEST_AUTHORIZATION_TOKEN, userDetails);
    }



    @NotNull
    private MessageSendDetails getOpenMessageObject() {
        MessageSendDetails message = new MessageSendDetails();
        message.setMessageId(UUID.randomUUID().toString());
        message.setMessageStatus(MessageSendDetails.MessageStatus.OPEN);
        message.setMessageSendDateNTime(LocalDateTime.now());
        message.setMessageText("message1");
        message.setMessageReasonList(MessageSendDetails.MessageReason.LIST_A_HEARING);
        return message;
    }

    private CaseDetails<CaseData, State> getCaseDetails() {
        return CaseDetails.<CaseData, State>builder()
            .data(caseData())
            .id(1L)
            .build();
    }
}
