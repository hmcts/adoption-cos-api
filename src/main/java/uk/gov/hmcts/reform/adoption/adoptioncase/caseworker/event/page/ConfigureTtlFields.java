package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.ccd.sdk.type.TTL;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

public class ConfigureTtlFields implements CcdPageConfiguration {
    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder.page("manageCaseTTL")
            .complex(CaseData::getRetainAndDisposeTimeToLive)
            .readonlyWithLabel(
                TTL::getSystemTTL, "System TTL"
            )
            .optionalWithLabel(
                TTL::getOverrideTTL, "Override TTL"
            )
            .optionalWithLabel(
                TTL::getSuspended, "Suspend Deletion"
            )
            .done();
    }
}
