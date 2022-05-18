package uk.gov.hmcts.reform.adoption.adoptioncase.bulkscan.ccd.workbasket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.bulkscan.ccd.ExceptionRecordState;
import uk.gov.hmcts.reform.adoption.bulkscan.data.ExceptionRecord;
import uk.gov.hmcts.reform.adoption.bulkscan.workbasket.ExceptionRecordWorkBasketResultFields;

import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.createExceptionRecordConfigBuilder;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.getWorkBasketResultFields;

public class ExceptionRecordWorkBasketResultFieldsTest {
    private ExceptionRecordWorkBasketResultFields workbasketResultFields;

    @BeforeEach
    void setUp() {
        workbasketResultFields = new ExceptionRecordWorkBasketResultFields();
    }

    @Test
    void shouldSetWorkBasketInputFields() throws Exception {
        final ConfigBuilderImpl<ExceptionRecord, ExceptionRecordState, UserRole> configBuilder = createExceptionRecordConfigBuilder();

        workbasketResultFields.configure(configBuilder);

        assertThat(getWorkBasketResultFields(configBuilder).getFields())
            .extracting("id", "label")
            .contains(
                tuple("[CASE_REFERENCE]", "Exception Id"),
                tuple("[CREATED_DATE]", "Exception created date"),
                tuple("deliveryDate", "Delivery date"),
                tuple("openingDate", "Opening date"),
                tuple("poBox", "PO Box"),
                tuple("caseReference", "New case reference"),
                tuple("attachToCaseReference", "Attach to case reference"),
                tuple("journeyClassification", "Journey classification"),
                tuple("formType", "Form type")
            );
    }
}
