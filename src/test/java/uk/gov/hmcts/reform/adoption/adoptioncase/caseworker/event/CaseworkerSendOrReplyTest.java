package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;
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
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.SendOrReply;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MessageSendDetails;

import java.lang.reflect.InvocationTargetException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.commons.util.ReflectionUtils.findMethod;
import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseworkerSendOrReply.CASEWORKER_SEND_OR_REPLY;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseDataUtil.archiveManageOrdersHelper;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@ExtendWith(MockitoExtension.class)
public class CaseworkerSendOrReplyTest {

    @Mock
    private Clock clock;

    @InjectMocks
    private SendOrReply sendOrReply;
    @InjectMocks
    private CaseworkerSendOrReply caseworkerSendOrReply;



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
        var result = caseworkerSendOrReply.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getListOfOpenMessages()).hasSize(1);
    }

    @Test
    void caseworkerSendOrReplyAboutToStartEventTest_Ok() {
        var caseDetails = getCaseDetails();
        List<ListValue<MessageSendDetails>> listOfOpenMessage = new ArrayList<>();
        archiveManageOrdersHelper(listOfOpenMessage, getOpenMessageObject());
        caseDetails.getData().setListOfOpenMessages(listOfOpenMessage);
        var result = caseworkerSendOrReply.beforeStartEvent(caseDetails);
        assertThat(result.getData().getReplyMsgDynamicList()).isNotNull();
    }





    @NotNull
    private MessageSendDetails getOpenMessageObject() {
        MessageSendDetails adoptionOrderData = new MessageSendDetails();
        adoptionOrderData.setMessageId(UUID.randomUUID().toString());
        adoptionOrderData.setMessageStatus(MessageSendDetails.MessageStatus.OPEN);
        adoptionOrderData.setMessageSendDateNTime(LocalDateTime.now());
        return adoptionOrderData;
    }

    private CaseDetails<CaseData, State> getCaseDetails() {
        return CaseDetails.<CaseData, State>builder()
            .data(caseData())
            .id(1L)
            .build();
    }
}
