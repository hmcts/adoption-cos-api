package uk.gov.hmcts.reform.adoption.adoptioncase.validation;

import uk.gov.hmcts.reform.adoption.adoptioncase.model.Application;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;

import java.util.ArrayList;
import java.util.List;

import static uk.gov.hmcts.reform.adoption.adoptioncase.validation.ValidationUtil.flattenLists;
// import static uk.gov.hmcts.reform.adoption.adoptioncase.validation.ValidationUtil.validateApplicant2BasicCase;
import static uk.gov.hmcts.reform.adoption.adoptioncase.validation.ValidationUtil.validateBasicCase;

public final class ApplicationValidation {

    private ApplicationValidation() {

    }

    public static List<String> validateReadyForPayment(CaseData caseData) {
        return validateBasicCase(caseData);
        //TODO
        // List<String> errors = validateBasicCase(caseData);
        // if (caseData.getApplicationType() != null && !caseData.getApplicationType().isSole()) {
        //     errors.addAll(validateApplicant2BasicCase(caseData));
        // }
        // return errors;
    }

    public static List<String> validateSubmission(Application application) {
        //List<String> errors = new ArrayList<>();

        // if (!application.hasBeenPaidFor()) {
        // TODO to be enabled after submission has been enabled
        //      errors.add("Payment incomplete");
        // }

        //TODO
        // if (!application.applicant1HasStatementOfTruth() && !application.hasSolSignStatementOfTruth()) {
        //     errors.add("Statement of truth must be accepted by the person making the application");
        // }

        return new ArrayList<>();
    }

    public static List<String> validateIssue(CaseData caseData) {
        return flattenLists(
            validateBasicCase(caseData)
        );
    }

}
