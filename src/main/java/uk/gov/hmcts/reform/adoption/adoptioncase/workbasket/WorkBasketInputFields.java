package uk.gov.hmcts.reform.adoption.adoptioncase.workbasket;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

@Component
public class WorkBasketInputFields implements CCDConfig<CaseData, State, UserRole> {

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {

        configBuilder
            .workBasketInputFields()
            //.caseReferenceField()
            //.field("applicantHomeAddress", "Postcode", "PostCode")
            //.field("applicant1LastName", "Applicant's Last Name")
            .field("hyphenatedCaseRef","Case reference number")
            .field("childrenFirstName", "Child's first name")
            .field("childrenLastName", "Child's last name")
            .field("dateSubmitted", "Date submitted");
    }
}
