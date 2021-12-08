package uk.gov.hmcts.reform.adoption.citizen.event;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

@Component
public class CitizenApplicant1ConfirmReceipt implements CCDConfig<CaseData, State, UserRole> {

    public static final String APPLICANT_1_CONFIRM_RECEIPT = "applicant1-confirm-receipt";

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.event(APPLICANT_1_CONFIRM_RECEIPT);//TODO
    }
}

