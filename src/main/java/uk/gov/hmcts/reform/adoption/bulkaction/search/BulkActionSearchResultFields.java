package uk.gov.hmcts.reform.adoption.bulkaction.search;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.bulkaction.ccd.BulkActionState;
import uk.gov.hmcts.reform.adoption.bulkaction.data.BulkActionCaseData;
import uk.gov.hmcts.reform.adoption.divorcecase.model.UserRole;

@Component
public class BulkActionSearchResultFields implements CCDConfig<BulkActionCaseData, BulkActionState, UserRole> {

    @Override
    public void configure(final ConfigBuilder<BulkActionCaseData, BulkActionState, UserRole> configBuilder) {
        configBuilder
            .searchResultFields()
            .caseReferenceField();
    }
}
