package uk.gov.hmcts.reform.adoption.adoptioncase.bulkscan.ccd.workbasket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.bulkscan.ccd.ExceptionRecordState;
import uk.gov.hmcts.reform.adoption.bulkscan.data.ExceptionRecord;
import uk.gov.hmcts.reform.adoption.bulkscan.workbasket.ExceptionRecordWorkBasketInputFields;

import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.createExceptionRecordConfigBuilder;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.getWorkBasketInputFields;

public class ExceptionRecordWorkBasketInputFieldsTest {
    private ExceptionRecordWorkBasketInputFields workbasketInputFields;

    @BeforeEach
    void setUp() {
        workbasketInputFields = new ExceptionRecordWorkBasketInputFields();
    }

    @Test
    void shouldSetWorkBasketInputFields() throws Exception {
        final ConfigBuilderImpl<ExceptionRecord, ExceptionRecordState, UserRole> configBuilder = createExceptionRecordConfigBuilder();

        workbasketInputFields.configure(configBuilder);

        assertThat(getWorkBasketInputFields(configBuilder).getFields())
            .extracting("id", "label")
            .contains(
                tuple("formType", "Form type"),
                tuple("containsPayments", "Contains payments")
            );
    }
}
