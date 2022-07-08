package uk.gov.hmcts.reform.adoption.adoptioncase.bulkscan.ccd.search;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.bulkscan.ccd.ExceptionRecordState;
import uk.gov.hmcts.reform.adoption.bulkscan.data.ExceptionRecord;
import uk.gov.hmcts.reform.adoption.bulkscan.search.ExceptionRecordSearchResultFields;

import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.createExceptionRecordConfigBuilder;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.getSearchResultFields;

public class ExceptionRecordSearchResultFieldsTest {
    private ExceptionRecordSearchResultFields searchResultFields;

    @BeforeEach
    void setUp() {
        searchResultFields = new ExceptionRecordSearchResultFields();
    }

    @Test
    void shouldSetSearchInputFields() throws Exception {
        final ConfigBuilderImpl<ExceptionRecord, ExceptionRecordState, UserRole> configBuilder = createExceptionRecordConfigBuilder();

        searchResultFields.configure(configBuilder);

        assertThat(getSearchResultFields(configBuilder).getFields())
            .extracting("id", "label")
            .contains(
                tuple("[CREATED_DATE]", "Exception created date"),
                tuple("deliveryDate", "Delivery date"),
                tuple("caseReference", "New case reference"),
                tuple("attachToCaseReference", "Attach to case reference"),
                tuple("formType", "Form type")
            );
    }
}
