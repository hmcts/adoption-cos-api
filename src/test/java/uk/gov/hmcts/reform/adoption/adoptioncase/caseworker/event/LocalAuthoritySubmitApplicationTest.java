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
import uk.gov.hmcts.reform.adoption.adoptioncase.event.LocalAuthoritySubmitApplication;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.common.service.SendNotificationService;
import uk.gov.hmcts.reform.adoption.common.service.SubmissionService;
import uk.gov.hmcts.reform.ccd.client.model.SubmittedCallbackResponse;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.commons.util.ReflectionUtils.findMethod;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.adoption.adoptioncase.event.LocalAuthoritySubmitApplication.LOCAL_AUTHORITY_SUBMIT;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Submitted;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@ExtendWith(MockitoExtension.class)
public class LocalAuthoritySubmitApplicationTest {

    @InjectMocks
    private LocalAuthoritySubmitApplication localAuthoritySubmitApplication;

    @Mock
    private SendNotificationService sendNotificationService;

    @Mock
    private SubmissionService submissionService;

    @Test
    void localAuthoritySubmitApplicationConfigure() {
        final ConfigBuilderImpl<CaseData, State, UserRole> configBuilder = createCaseDataConfigBuilder();
        localAuthoritySubmitApplication.configure(configBuilder);
        assertThat(getEventsFrom(configBuilder).values())
            .extracting(Event::getId)
            .contains(LOCAL_AUTHORITY_SUBMIT);
    }

    @Test
    void localAuthoritySubmitApplicationSubmitted() {
        var caseDetails = getCaseDetails();
        caseDetails.setState(Submitted);
        SubmittedCallbackResponse submittedCallbackResponse = localAuthoritySubmitApplication.submitted(caseDetails, caseDetails);
        System.out.println(submittedCallbackResponse.toString());
        assertThat(submittedCallbackResponse).isNotNull();
    }

    @Test
    void localAuthoritySubmitApplicationAboutToSubmit() {
        var caseDetails = getCaseDetails();
        /*List<ListValue<Payment>> payments = new ArrayList<>();
        Payment payment = new Payment();
        payment.setStatus(IN_PROGRESS);
        ListValue<Payment> paymentListValue = ListValue.<Payment>builder().value(payment).build();
        payments.add(paymentListValue);
        caseDetails.getData().getApplication().setApplicationPayments(payments);*/
        when(submissionService.laSubmitApplication(caseDetails)).thenReturn(caseDetails);
        var result = localAuthoritySubmitApplication.aboutToSubmit(caseDetails,caseDetails);
        //assertThat(result.getData().getStatus()).isEqualTo(AwaitingPayment);
    }

    private CaseDetails<CaseData, State> getCaseDetails() {
        return CaseDetails.<CaseData, State>builder()
            .data(caseData())
            .id(1L)
            .build();
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
