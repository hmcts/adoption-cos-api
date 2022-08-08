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

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.AwaitingPayment;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Draft;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.SYSTEM_UPDATE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.SUPER_USER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions.CREATE_READ_UPDATE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions.READ;

@Slf4j
@Component
public class LocalAuthorityCheckYourAnswersValidation implements CCDConfig<CaseData, State, UserRole> {

    public static final String LOCAL_AUTHORITY_CHECK_YOUR_ANSWERS_SUBMIT= "local-authority-check-your-answer-submit";

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        log.info("LocalAuthorityCheckYourAnswersValidation configure invoked");
        configBuilder
            .event(LOCAL_AUTHORITY_CHECK_YOUR_ANSWERS_SUBMIT)
            .forStates(Draft)
            .name("Local Authority Check Your Answers Submit")
            .description("Local Authority Check Your Answers Submit Validation of Case Data")
            .retries(120, 120)
            .grant(CREATE_READ_UPDATE, SYSTEM_UPDATE)
            .grant(READ, SUPER_USER)
            .aboutToSubmitCallback(this::aboutToSubmit);
    }

    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(CaseDetails<CaseData, State> details,
                                                                       CaseDetails<CaseData, State> beforeDetails) {
        log.info("LocalAuthorityCheckYourAnswersValidation aboutToSubmit invoked");
        return null;
    }
}
