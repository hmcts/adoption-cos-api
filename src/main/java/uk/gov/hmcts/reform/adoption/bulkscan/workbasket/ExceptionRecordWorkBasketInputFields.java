package uk.gov.hmcts.reform.adoption.bulkscan.workbasket;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.WorkBasketField;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.bulkscan.ccd.ExceptionRecordState;
import uk.gov.hmcts.reform.adoption.bulkscan.data.ExceptionRecord;

import java.util.List;

import static java.util.List.of;

@Component
public class ExceptionRecordWorkBasketInputFields implements CCDConfig<ExceptionRecord, ExceptionRecordState, UserRole> {

    @Override
    public void configure(final ConfigBuilder<ExceptionRecord, ExceptionRecordState, UserRole> configBuilder) {
        final List<WorkBasketField> workBasketFieldList = of(
            WorkBasketField.builder().label("Form type").id("formType").build(),
            WorkBasketField.builder().label("Contains payments").id("containsPayments").build()
        );

        configBuilder.workBasketInputFields().fields(workBasketFieldList);
    }
}
