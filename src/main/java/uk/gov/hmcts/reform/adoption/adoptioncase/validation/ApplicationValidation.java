package uk.gov.hmcts.reform.adoption.adoptioncase.validation;

import uk.gov.hmcts.reform.adoption.adoptioncase.model.Application;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;

import java.util.ArrayList;
import java.util.List;

import static uk.gov.hmcts.reform.adoption.adoptioncase.validation.ValidationUtil.validateBasicCase;

public final class ApplicationValidation {

    private ApplicationValidation() {

    }

    public static List<String> validateReadyForPayment(CaseData caseData) {
        return validateBasicCase(caseData);
    }

    public static List<String> validateSubmission(Application application) {
        List<String> errors = new ArrayList<>();

        if (!application.hasBeenPaidFor()) {
            errors.add("Payment incomplete");
        }

        if (!application.applicant1HasStatementOfTruth() && !application.hasSolSignStatementOfTruth()) {
            errors.add("Statement of truth must be accepted by the person making the application");
        }

        return errors;
    }
}
