package uk.gov.hmcts.reform.adoption.adoptioncase.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Draft;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Submitted;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.CITIZEN;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.SUPER_USER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions.CREATE_READ_UPDATE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions.READ;

@Slf4j
@Component
public class CitizenSubmitApplication implements CCDConfig<CaseData, State, UserRole> {

    public static final String CITIZEN_SUBMIT = "citizen-submit-application";

    //@Autowired
    //private PaymentService paymentService;

    /*@Autowired
    private SubmissionService submissionService;*/

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {

        configBuilder
            .event(CITIZEN_SUBMIT)
            .forStates(Draft, Submitted)
            .name("Applicant Statement of Truth")
            .description("The applicant confirms SOT")
            .retries(120, 120)
            .grant(CREATE_READ_UPDATE, CITIZEN)
            .grant(READ, SUPER_USER)
            .aboutToSubmitCallback(this::aboutToSubmit);
    }

    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(CaseDetails<CaseData, State> details,
                                                                       CaseDetails<CaseData, State> beforeDetails) {
        log.info("Submit application about to submit callback invoked");

        CaseData data = details.getData();
        State state = details.getState();

        //log.info("Validating case data");
        //final List<String> validationErrors = validateReadyForPayment(data);

        /*if (!validationErrors.isEmpty()) {
            log.info("Validation errors: ");
            for (String error : validationErrors) {
                log.info(error);
            }

            return AboutToStartOrSubmitResponse.<CaseData, State>builder()
                .data(data)
                .errors(validationErrors)
                .state(state)
                .build();
        }*/

        /*Application application = data.getApplication();

        if (data.isSoleApplicationOrApplicant2HasAgreedHwf() && application.isHelpWithFeesApplication()) {
            var submittedDetails = submissionService.submitApplication(details);
            data = submittedDetails.getData();
            state = submittedDetails.getState();
        } else {
            OrderSummary orderSummary = paymentService.getOrderSummaryByServiceEvent(SERVICE_DIVORCE,
                                                                                     EVENT_ISSUE,KEYWORD_DIVORCE);
            application.setApplicationFeeOrderSummary(orderSummary);

            state = AwaitingPayment;
        }

        data.getLabelContent().setApplicationType(data.getApplicationType());
        data.getLabelContent().setUnionType(data.getDivorceOrDissolution());*/

        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(data)
            .state(state)
            .build();
    }

}

