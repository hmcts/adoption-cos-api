package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import com.google.common.collect.ImmutableSet;
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
import uk.gov.hmcts.ccd.sdk.type.OrderSummary;
import uk.gov.hmcts.reform.adoption.adoptioncase.event.CitizenAddPayment;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Payment;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.common.service.SendNotificationService;
import uk.gov.hmcts.reform.adoption.common.service.SubmissionService;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.adoption.payment.model.PaymentStatus;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.ccd.client.model.SubmittedCallbackResponse;
import uk.gov.hmcts.reform.idam.client.models.User;
import uk.gov.hmcts.reform.idam.client.models.UserDetails;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.commons.util.ReflectionUtils.findMethod;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.adoption.adoptioncase.event.CitizenAddPayment.CITIZEN_ADD_PAYMENT;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.AwaitingPayment;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Draft;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Submitted;
import static uk.gov.hmcts.reform.adoption.payment.model.PaymentStatus.IN_PROGRESS;
import static uk.gov.hmcts.reform.adoption.payment.model.PaymentStatus.SUCCESS;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_AUTHORIZATION_TOKEN;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;


@ExtendWith(MockitoExtension.class)
class CitizenAddPaymentTest {

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

    public static final String SOME_SERVICE_AUTHORIZATION_TOKEN = "ServiceToken";

    @Test
    void citizenAddPaymentConfigure() {
        var caseDetails = getCaseDetails();
        caseDetails.setState(Submitted);
        SubmittedCallbackResponse submittedCallbackResponse = citizenAddPayment.submitted(caseDetails, caseDetails);
        System.out.println(submittedCallbackResponse.toString());
        assertThat(submittedCallbackResponse).isNotNull();
    }


    @Test
    void citizenAddPaymentInProgress() {
        var caseDetails = getCaseDetails();
        List<ListValue<Payment>> payments = new ArrayList<>();
        Payment payment = new Payment();
        payment.setStatus(IN_PROGRESS);
        ListValue<Payment> paymentListValue = ListValue.<Payment>builder().value(payment).build();
        payments.add(paymentListValue);
        caseDetails.getData().getApplication().setApplicationPayments(payments);
        var result = citizenAddPayment.aboutToSubmit(caseDetails,caseDetails);
        assertThat(result.getData().getStatus()).isEqualTo(AwaitingPayment);
    }

    @Test
    void citizenAddPaymentNotSuccessful() {
        var caseDetails = getCaseDetails();
        List<ListValue<Payment>> payments = new ArrayList<>();
        Payment payment = new Payment();
        payment.setStatus(PaymentStatus.CANCELLED);
        ListValue<Payment> paymentListValue = ListValue.<Payment>builder().value(payment).build();
        payments.add(paymentListValue);
        caseDetails.getData().getApplication().setApplicationPayments(payments);
        var result = citizenAddPayment.aboutToSubmit(caseDetails,caseDetails);
        assertThat(result.getState()).isEqualTo(Draft);
    }

    @Test
    void citizenAddPaymentSuccessful() {
        List<ListValue<Payment>> payments = new ArrayList<>();
        Payment payment = new Payment();
        payment.setStatus(SUCCESS);
        payment.setAmount(Integer.parseInt("183"));
        ListValue<Payment> paymentListValue = ListValue.<Payment>builder().value(payment).build();
        payments.add(paymentListValue);
        var caseDetails = getCaseDetails();
        caseDetails.getData().getApplication().setApplicationPayments(payments);
        OrderSummary orderSummary = new OrderSummary();
        orderSummary.setPaymentTotal("183");
        caseDetails.getData().getApplication().setApplicationFeeOrderSummary(orderSummary);
        caseDetails.setState(Submitted);
        when(submissionService.submitApplication(caseDetails)).thenReturn(caseDetails);
        var result = citizenAddPayment.aboutToSubmit(caseDetails,caseDetails);
        System.out.println("result.getData().getStatus():   " + result.getData().getStatus());
        assertThat(result.getState()).isNotNull();
    }

    @Test
    void caseworkerAddPaymentConfigure() {
        final ConfigBuilderImpl<CaseData, State, UserRole> configBuilder = createCaseDataConfigBuilder();
        citizenAddPayment.configure(configBuilder);
        assertThat(getEventsFrom(configBuilder).values())
            .extracting(Event::getId)
            .contains(CITIZEN_ADD_PAYMENT);
    }

    private CaseDetails<CaseData, State> getCaseDetails() {
        return CaseDetails.<CaseData, State>builder()
            .data(caseData())
            .id(1L)
            .build();
    }

    private User getCaseworkerUser() {
        UserDetails userDetails = UserDetails
            .builder()
            .forename("testFname")
            .surname("testSname")
            .build();

        return new User(TEST_AUTHORIZATION_TOKEN, userDetails);
    }

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
}
