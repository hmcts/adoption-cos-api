package uk.gov.hmcts.reform.adoption.adoptioncase;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

@Component
public class Adoption implements CCDConfig<CaseData, State, UserRole> {

    public static final String CASE_TYPE = "A58";
    public static final String JURISDICTION = "ADOPTION";

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.setCallbackHost(System.getenv().getOrDefault("CASE_API_URL", "http://adoption-cos-api:4550"));
        configBuilder.caseType(CASE_TYPE, CASE_TYPE, "Handling of child adoption case");
        configBuilder.jurisdiction(JURISDICTION, "Family jurisdiction adoption", "Child adoption");
    }
}
