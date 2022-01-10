package uk.gov.hmcts.reform.adoption.adoptioncase.workbasket;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

@Component
public class WorkBasketResultFields implements CCDConfig<CaseData, State, UserRole> {

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {

        configBuilder
            .workBasketResultFields()
            .caseReferenceField()
            //.field("applicantHomeAddress", "Applicant's Post Code", "PostCode")
            .field("applicant1LastName", "Applicant's Last Name");
    }
}
