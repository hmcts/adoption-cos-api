package uk.gov.hmcts.reform.adoption.citizen.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.reform.adoption.citizen.notification.Applicant2ApprovedNotification;
import uk.gov.hmcts.reform.adoption.divorcecase.model.CaseData;
import uk.gov.hmcts.reform.adoption.divorcecase.model.State;
import uk.gov.hmcts.reform.adoption.divorcecase.model.UserRole;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static uk.gov.hmcts.ccd.sdk.type.YesOrNo.YES;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.State.Applicant2Approved;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.State.AwaitingApplicant2Response;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.UserRole.APPLICANT_2;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.access.Permissions.CREATE_READ_UPDATE;
import static uk.gov.hmcts.reform.adoption.divorcecase.validation.ValidationUtil.validateApplicant2BasicCase;

@Slf4j
@Component
public class CitizenApplicant2Approve implements CCDConfig<CaseData, State, UserRole> {

    public static final String APPLICANT_2_APPROVE = "applicant2-approve";

    @Autowired
    private Applicant2ApprovedNotification applicant2ApprovedNotification;

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {

        configBuilder
            .event(APPLICANT_2_APPROVE)
            .forStateTransition(AwaitingApplicant2Response, Applicant2Approved)
            .name("Applicant 2 approve")
            .description("Applicant 2 has approved")
            .grant(CREATE_READ_UPDATE, APPLICANT_2)
            .retries(120, 120)
            .aboutToSubmitCallback(this::aboutToSubmit);
    }

    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(CaseDetails<CaseData, State> details,
                                                                       CaseDetails<CaseData, State> beforeDetails) {
        log.info("Applicant 2 approve about to submit callback invoked");

        CaseData data = details.getData();

        log.info("Validating case data");
        final List<String> validationErrors = validateApplicant2BasicCase(data);

        if (!validationErrors.isEmpty()) {
            log.info("Validation errors: {} ", validationErrors);

            return AboutToStartOrSubmitResponse.<CaseData, State>builder()
                .data(data)
                .errors(validationErrors)
                .state(details.getState())
                .build();
        }

        data.setDueDate(LocalDate.now().plus(2, ChronoUnit.WEEKS));

        if (data.getApplication().isHelpWithFeesApplication() && data.getApplication().getApplicant2HelpWithFees().getNeedHelp() != YES) {
            log.info("Triggering applicant 2 denied HWF notification for applicant 1");
            applicant2ApprovedNotification.sendToApplicant1WithDeniedHwf(data, details.getId());
        } else {
            log.info("Triggering applicant 2 approved notification for applicant 1");
            applicant2ApprovedNotification.sendToApplicant1(data, details.getId());
        }

        applicant2ApprovedNotification.sendToApplicant2(data, details.getId());

        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(data)
            .state(Applicant2Approved)
            .build();
    }

}
