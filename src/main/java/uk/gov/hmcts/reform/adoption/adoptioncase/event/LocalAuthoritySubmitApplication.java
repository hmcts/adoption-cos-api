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
import uk.gov.hmcts.reform.ccd.client.model.SubmittedCallbackResponse;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.LaSubmitted;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Submitted;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.SUPER_USER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.CASE_WORKER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.SYSTEM_UPDATE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions.CREATE_READ_UPDATE;
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
            .forStates(Submitted)
            .name("Local Authority Submit")
            .description("Local Authority Application Submit- Awaiting Admin Checks")
            .retries(120, 120)
            .grant(CREATE_READ_UPDATE, SYSTEM_UPDATE)
            .grant(READ, SUPER_USER, CASE_WORKER)
            .aboutToSubmitCallback(this::aboutToSubmit)
            .submittedCallback(this::submitted);
    }

    private SubmittedCallbackResponse submitted(CaseDetails<CaseData, State> details, CaseDetails<CaseData, State> beforeDetails) {
        log.info("Local Authority Submit Application submitted callback invoked CaseID: {}", details.getId());
        log.info("Invoking Notifications for CaseID: {}", details.getId());
        sendNotificationService.sendNotifications(details);
        return SubmittedCallbackResponse.builder().build();
    }

    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(final CaseDetails<CaseData, State> details,
                                                                       final CaseDetails<CaseData, State> beforeDetails) {
        final Long caseId = details.getId();

        log.info("Local Authority Submit Application about to submit callback invoked CaseID: {}", caseId);

        final CaseDetails<CaseData, State> updatedCaseDetails = submissionService.laSubmitApplication(details);
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(updatedCaseDetails.getData())
            .state(LaSubmitted)
            .build();

    }
}
