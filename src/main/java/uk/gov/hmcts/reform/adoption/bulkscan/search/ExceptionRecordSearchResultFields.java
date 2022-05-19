package uk.gov.hmcts.reform.adoption.bulkscan.search;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.SearchField;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.bulkscan.ccd.ExceptionRecordState;
import uk.gov.hmcts.reform.adoption.bulkscan.data.ExceptionRecord;

import java.util.List;

import static java.util.List.of;

@Component
public class ExceptionRecordSearchResultFields implements CCDConfig<ExceptionRecord, ExceptionRecordState, UserRole> {

    @Override
    public void configure(final ConfigBuilder<ExceptionRecord, ExceptionRecordState, UserRole> configBuilder) {
        final List<SearchField> searchFieldList = of(
            SearchField.builder().label("Exception created date").id("[CREATED_DATE]").build(),
            SearchField.builder().label("Delivery date").id("deliveryDate").build(),
            SearchField.builder().label("New case reference").id("caseReference").build(),
            SearchField.builder().label("Attach to case reference").id("attachToCaseReference").build(),
            SearchField.builder().label("Form type").id("formType").build()
        );

        configBuilder.searchResultFields().fields(searchFieldList);
    }
}
