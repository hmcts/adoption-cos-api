package uk.gov.hmcts.reform.adoption.adoptioncase.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.Event;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.ccd.sdk.type.OrderSummary;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Payment;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.common.service.SendNotificationService;
import uk.gov.hmcts.reform.adoption.common.service.SubmissionService;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.adoption.payment.model.PaymentStatus;
import uk.gov.hmcts.reform.adoption.service.event.ApplicationSubmitNotificationEvent;
import uk.gov.hmcts.reform.adoption.service.task.EventService;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.ccd.client.model.SubmittedCallbackResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.adoption.adoptioncase.event.CitizenAddPayment.CITIZEN_ADD_PAYMENT;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.AwaitingPayment;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Draft;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Submitted;
import static uk.gov.hmcts.reform.adoption.payment.model.PaymentStatus.CANCELLED;
import static uk.gov.hmcts.reform.adoption.payment.model.PaymentStatus.IN_PROGRESS;
import static uk.gov.hmcts.reform.adoption.payment.model.PaymentStatus.SUCCESS;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@ExtendWith(MockitoExtension.class)
class CitizenAddPaymentTest extends EventTest {

    @InjectMocks
    private CitizenAddPayment citizenAddPayment;

    @Mock
    private IdamService idamService;

    @Mock
    private AuthTokenGenerator authTokenGenerator;

    @Mock
    private SendNotificationService sendNotificationService;

    @Mock
    private SubmissionService submissionService;

    @Mock
    private EventService eventPublisher;

    @Test
    void aboutToSubmitWhenValidationFailsReturnsErrorsWithoutSubmitting() {
        var caseDetails = getCaseDetails(AwaitingPayment);
        var payment = Payment.builder().status(SUCCESS).build();
        caseDetails.getData().getApplication().setApplicationPayments(
            List.of(ListValue.<Payment>builder().value(payment).build())
        );

        var result = citizenAddPayment.aboutToSubmit(caseDetails, caseDetails);

        assertThat(result.getErrors()).isNotEmpty();
        verify(submissionService, never()).submitApplication(any());
    }

    @Test
    void citizenAddPaymentSubmittedPublishesNotificationEvent() {
        var caseDetails = getCaseDetails(Submitted);

        SubmittedCallbackResponse response = citizenAddPayment.submitted(caseDetails, caseDetails);

        assertThat(response).isNotNull();
        verify(eventPublisher, times(1)).publishEvent(
            ApplicationSubmitNotificationEvent.builder()
                .caseData(caseDetails)
                .build()
        );

        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    void citizenAddPaymentSubmittedWhenStateIsNotSubmittedDoesNotPublishEvent() {
        var caseDetails = getCaseDetails(AwaitingPayment);

        SubmittedCallbackResponse response = citizenAddPayment.submitted(caseDetails, caseDetails);

        assertThat(response).isNotNull();

        verifyNoInteractions(eventPublisher);
    }

    @Test
    void citizenAddPaymentSubmittedWhenStateIsDraftDoesNotPublishEvent() {
        var caseDetails = getCaseDetails(Draft);

        SubmittedCallbackResponse response = citizenAddPayment.submitted(caseDetails, caseDetails);

        assertThat(response).isNotNull();

        verifyNoInteractions(eventPublisher);
    }

    @Test
    void citizenAddPaymentSubmittedWhenStateIsNullDoesNotPublishEvent() {
        var caseDetails = getCaseDetails(null);

        SubmittedCallbackResponse response = citizenAddPayment.submitted(caseDetails, caseDetails);

        assertThat(response).isNotNull();

        verifyNoInteractions(eventPublisher);
    }

    @Test
    void citizenAddPaymentSubmittedWhenPublishEventThrowsPropagatesException() {
        var caseDetails = getCaseDetails(Submitted);

        RuntimeException exception = new RuntimeException("publish failed");

        doThrow(exception).when(eventPublisher).publishEvent(any(ApplicationSubmitNotificationEvent.class));

        assertThatThrownBy(() -> citizenAddPayment.submitted(caseDetails, caseDetails))
            .isSameAs(exception);

        verify(eventPublisher, times(1)).publishEvent(any(ApplicationSubmitNotificationEvent.class));
    }

    @Test
    void aboutToSubmitWhenLastPaymentIsInProgressKeepsCaseInAwaitingPaymentAndDoesNotSubmitApplication() {
        var caseDetails = getCaseDetails(AwaitingPayment);

        caseDetails.getData().getApplication().setApplicationPayments(
            List.of(ListValue.<Payment>builder().value(getPayment(IN_PROGRESS)).build())
        );

        var result = citizenAddPayment.aboutToSubmit(caseDetails, caseDetails);

        assertThat(result.getState()).isEqualTo(AwaitingPayment);
        assertThat(result.getData().getStatus()).isEqualTo(AwaitingPayment);
        verify(submissionService, never()).submitApplication(any());
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void aboutToSubmitWhenLastPaymentIsCancelledMovesCaseToDraftAndDoesNotSubmitApplication() {
        var caseDetails = getCaseDetails(AwaitingPayment);

        caseDetails.getData().getApplication().setApplicationPayments(
            List.of(ListValue.<Payment>builder().value(getPayment(CANCELLED)).build())
        );

        var result = citizenAddPayment.aboutToSubmit(caseDetails, caseDetails);

        assertThat(result).isNotNull();
        assertThat(result.getState()).isEqualTo(Draft);
        verify(submissionService, never()).submitApplication(any());
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void aboutToSubmitWhenLastPaymentIsSuccessSubmitsApplicationAndReturnsSubmittedState() {
        var caseDetails = getCaseDetails(AwaitingPayment);

        var payment = Payment.builder()
            .status(SUCCESS)
            .amount(183)
            .build();

        caseDetails.getData().getApplication().setApplicationPayments(
            List.of(ListValue.<Payment>builder().value(payment).build())
        );

        var orderSummary = new OrderSummary();
        orderSummary.setPaymentTotal("183");
        caseDetails.getData().getApplication().setApplicationFeeOrderSummary(orderSummary);

        var submittedDetails = CaseDetails.<CaseData, State>builder()
            .id(caseDetails.getId())
            .data(caseDetails.getData())
            .state(Submitted)
            .build();

        when(submissionService.submitApplication(caseDetails)).thenReturn(submittedDetails);

        var result = citizenAddPayment.aboutToSubmit(caseDetails, caseDetails);

        assertThat(result).isNotNull();
        assertThat(result.getState()).isEqualTo(Submitted);
        verify(submissionService, times(1)).submitApplication(caseDetails);
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void aboutToSubmitWhenSuccessPaymentAmountDoesNotMatchOrderSummaryValidateSubmissionReturnsErrorsAndDoesNotSubmit() {
        var caseDetails = getCaseDetails(AwaitingPayment);

        var payment = Payment.builder()
            .status(SUCCESS)
            .amount(100)
            .build();

        caseDetails.getData().getApplication().setApplicationPayments(
            List.of(ListValue.<Payment>builder().value(payment).build())
        );

        var orderSummary = new OrderSummary();
        orderSummary.setPaymentTotal("183");
        caseDetails.getData().getApplication().setApplicationFeeOrderSummary(orderSummary);

        var result = citizenAddPayment.aboutToSubmit(caseDetails, caseDetails);

        assertThat(result.getErrors()).isNotEmpty();
        verify(submissionService, never()).submitApplication(any());
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void aboutToSubmitWhenSubmissionServiceThrowsPropagatesException() {
        var caseDetails = getCaseDetails(AwaitingPayment);

        var payment = Payment.builder()
            .status(SUCCESS)
            .amount(183)
            .build();

        caseDetails.getData().getApplication().setApplicationPayments(
            List.of(ListValue.<Payment>builder().value(payment).build())
        );

        var orderSummary = new OrderSummary();
        orderSummary.setPaymentTotal("183");
        caseDetails.getData().getApplication().setApplicationFeeOrderSummary(orderSummary);

        RuntimeException exception = new RuntimeException("submission failed");
        when(submissionService.submitApplication(caseDetails)).thenThrow(exception);

        assertThatThrownBy(() -> citizenAddPayment.aboutToSubmit(caseDetails, caseDetails))
            .isSameAs(exception);

        verify(submissionService, times(1)).submitApplication(caseDetails);
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void citizenAddPaymentConfigure() {
        final ConfigBuilderImpl<CaseData, State, UserRole> configBuilder = createCaseDataConfigBuilder();
        citizenAddPayment.configure(configBuilder);

        var events = getEventsFrom(configBuilder).values();
        assertThat(events)
            .extracting(Event::getId)
            .contains(CITIZEN_ADD_PAYMENT);

        var event = getEventsFrom(configBuilder).get(CITIZEN_ADD_PAYMENT);
        assertThat(event)
            .isNotNull()
            .extracting(Event::getName)
            .isEqualTo("Payment made");
    }

    private CaseDetails<CaseData, State> getCaseDetails(State state) {
        return CaseDetails.<CaseData, State>builder()
            .data(caseData())
            .id(1L)
            .state(state)
            .build();
    }

    private Payment getPayment(PaymentStatus paymentStatus) {
        return Payment.builder()
            .status(paymentStatus)
            .build();
    }
}
