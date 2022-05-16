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
public class ExceptionRecordWorkBasketResultFields implements CCDConfig<ExceptionRecord, ExceptionRecordState, UserRole> {

    @Override
    public void configure(final ConfigBuilder<ExceptionRecord, ExceptionRecordState, UserRole> configBuilder) {
        final List<WorkBasketField> workBasketFieldList = of(
            WorkBasketField.builder().label("Exception Id").id("[CASE_REFERENCE]").build(),
            WorkBasketField.builder().label("Exception created date").id("[CREATED_DATE]").build(),
            WorkBasketField.builder().label("Delivery date").id("deliveryDate").build(),
            WorkBasketField.builder().label("Opening date").id("openingDate").build(),
            WorkBasketField.builder().label("New case reference").id("caseReference").build(),
            WorkBasketField.builder().label("Attach to case reference").id("attachToCaseReference").build(),
            WorkBasketField.builder().label("PO Box").id("poBox").build(),
            WorkBasketField.builder().label("Journey classification").id("journeyClassification").build(),
            WorkBasketField.builder().label("Form type").id("formType").build()
        );

        configBuilder.workBasketResultFields().fields(workBasketFieldList);
    }
}
