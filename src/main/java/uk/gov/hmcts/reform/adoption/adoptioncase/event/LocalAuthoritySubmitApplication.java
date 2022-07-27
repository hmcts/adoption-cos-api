package uk.gov.hmcts.reform.adoption.adoptioncase.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.common.service.SendNotificationService;
import uk.gov.hmcts.reform.adoption.common.service.SubmissionService;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.AwaitingAdminChecks;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.SUPER_USER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.CASE_WORKER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions.READ;

@Component
@Slf4j
public class LocalAuthoritySubmitApplication implements CCDConfig<CaseData, State, UserRole> {

    public static final String LOCAL_AUTHORITY_SUBMIT = "local-authority-application-submit";

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private SendNotificationService sendNotificationService;

    @Override
    public void configure(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder
            .event(LOCAL_AUTHORITY_SUBMIT)
            .forState(AwaitingAdminChecks)
            .name("Local Authority Submit")
            .description("Local Authority Application Submit- Awaiting Admin Checks")
            .retries(120, 120)
            //.grant(CREATE_READ_UPDATE, CITIZEN)
            .grant(READ, SUPER_USER, CASE_WORKER)
            .aboutToSubmitCallback(this::aboutToSubmit);
    }

    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(final CaseDetails<CaseData, State> details,
                                                                       final CaseDetails<CaseData, State> beforeDetails) {
        final Long caseId = details.getId();

        log.info("Citizen Submit Application about to submit callback invoked CaseID: {}", caseId);

        final CaseDetails<CaseData, State> updatedCaseDetails = submissionService.submitApplication(details);
        final CaseDetails<CaseData, State> notificationSentUpdatedDetails = sendNotificationService.sendNotifications(
            updatedCaseDetails);
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(notificationSentUpdatedDetails.getData())
            .state(notificationSentUpdatedDetails.getState())
            .build();

    }
}
