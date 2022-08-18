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
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.CCD_REFERENCE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.CHILD_FIRST_NAME;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.CHILD_LAST_NAME;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.CASE_STATE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.DATE_SUBMITTED;

@Component
public class SearchResultFields implements CCDConfig<CaseData, State, UserRole> {


    public static final List<SearchField<UserRole>> SEARCH_RESULT_FIELD_LIST = of(
        SearchField.<UserRole>builder().id(CCD_REFERENCE).label("Case Reference Number").build(),
        SearchField.<UserRole>builder().id(CHILD_FIRST_NAME).label("Child's First Name").build(),
        SearchField.<UserRole>builder().id(CHILD_LAST_NAME).label("Child's Last Name").build(),
        SearchField.<UserRole>builder().id(CASE_STATE).label("State").build(),
        SearchField.<UserRole>builder().id(DATE_SUBMITTED).label("Date Submitted").build()
    );


    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {

        configBuilder
            .searchResultFields()
            .fields(SEARCH_RESULT_FIELD_LIST);
    }
}
