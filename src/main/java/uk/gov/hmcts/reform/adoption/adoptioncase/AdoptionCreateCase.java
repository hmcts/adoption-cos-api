package uk.gov.hmcts.reform.adoption.adoptioncase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.RetiredFields;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

import static uk.gov.hmcts.reform.adoption.adoptioncase.Adoption.CASE_TYPE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.Adoption.JURISDICTION;

@Component
@Slf4j
public class AdoptionCreateCase implements CCDConfig<CaseData, State, UserRole> {

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.setCallbackHost(System.getenv().getOrDefault("CASE_API_URL", "http://localhost:4550"));

        configBuilder.caseType(CASE_TYPE, "New Adoption Case", "Handling of the dissolution of marriage");
        configBuilder.jurisdiction(JURISDICTION, "Adoption", "Family Divorce: dissolution of marriage");
        configBuilder.addPreEventHook(RetiredFields::migrate);
        //configBuilder.omitHistoryForRoles(APPLICANT_1_SOLICITOR, APPLICANT_2_SOLICITOR);

        // to shutter the service within xui uncomment this line
        // configBuilder.shutterService();
        log.info("Building definition for " + System.getenv().getOrDefault("ENVIRONMENT", ""));
    }
}
