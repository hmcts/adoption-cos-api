package uk.gov.hmcts.reform.adoption.adoptioncase.search;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.SearchField;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

import java.util.List;

import static java.util.List.of;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.APPLICANT_FIRST_NAME;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.FIRST_NAME;

@Component
public class SearchInputFields implements CCDConfig<CaseData, State, UserRole> {

    public static final List<SearchField<UserRole>> SEARCH_FIELD_LIST = of(
        SearchField.<UserRole>builder().label(FIRST_NAME).id(APPLICANT_FIRST_NAME).build());

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.searchInputFields().caseReferenceField();
        configBuilder.searchInputFields().fields(SEARCH_FIELD_LIST);
    }
}
