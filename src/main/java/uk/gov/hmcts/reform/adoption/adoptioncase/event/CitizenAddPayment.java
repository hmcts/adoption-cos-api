package uk.gov.hmcts.reform.adoption.adoptioncase.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.reform.adoption.common.service.SendNotificationService;
import uk.gov.hmcts.reform.adoption.common.service.SubmissionService;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.payment.model.PaymentStatus;

import java.util.List;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.AwaitingPayment;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Draft;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.CASE_WORKER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.CITIZEN;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.SUPER_USER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions.CREATE_READ_UPDATE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions.READ;
import static uk.gov.hmcts.reform.adoption.adoptioncase.validation.ApplicationValidation.validateSubmission;
import static uk.gov.hmcts.reform.adoption.payment.model.PaymentStatus.IN_PROGRESS;
import static uk.gov.hmcts.reform.adoption.payment.model.PaymentStatus.SUCCESS;

@Component
@Slf4j
public class CitizenAddPayment implements CCDConfig<CaseData, State, UserRole> {

    public static final String CITIZEN_ADD_PAYMENT = "citizen-add-payment";

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private SendNotificationService sendNotificationService;

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder
            .event(CITIZEN_ADD_PAYMENT)
            .forState(AwaitingPayment)
            .name("Payment made")
            .description("Payment made")
            .retries(120, 120)
            .grant(CREATE_READ_UPDATE, CITIZEN)
            .grant(READ, SUPER_USER, CASE_WORKER)
            .aboutToSubmitCallback(this::aboutToSubmit);
    }

    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(final CaseDetails<CaseData, State> details,
                                                                       final CaseDetails<CaseData, State> beforeDetails) {
        final CaseData caseData = details.getData();
        final Long caseId = details.getId();

        log.info("Add payment about to submit callback invoked CaseID: {}", caseId);
        final PaymentStatus lastPaymentStatus = caseData.getApplication().getLastPaymentStatus();

        if (IN_PROGRESS.equals(lastPaymentStatus)) {
            log.info("Case {} payment in progress", caseId);

            return AboutToStartOrSubmitResponse.<CaseData, State>builder()
                .data(details.getData())
                .state(AwaitingPayment)
                .build();
        }

        if (!SUCCESS.equals(lastPaymentStatus)) {
            log.info("Case {} payment canceled", caseId);

            return AboutToStartOrSubmitResponse.<CaseData, State>builder()
                .data(details.getData())
                .state(Draft)
                .build();
        }

        log.info("Validating case caseData CaseID: {}", caseId);
        final List<String> submittedErrors = validateSubmission(caseData.getApplication());

        if (!submittedErrors.isEmpty()) {
            return AboutToStartOrSubmitResponse.<CaseData, State>builder()
                .data(details.getData())
                .state(details.getState())
                .errors(submittedErrors)
                .build();
        }

        final CaseDetails<CaseData, State> updatedCaseDetails = submissionService.submitApplication(details);
        final CaseDetails<CaseData, State> notificationSentUpdatedDetails = sendNotificationService.sendNotifications(
            updatedCaseDetails);
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(notificationSentUpdatedDetails.getData())
            .state(notificationSentUpdatedDetails.getState())
            .build();
    }
}

