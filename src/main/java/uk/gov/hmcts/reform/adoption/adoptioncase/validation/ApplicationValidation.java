package uk.gov.hmcts.reform.adoption.adoptioncase.validation;

import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;

import java.util.List;

import static uk.gov.hmcts.reform.adoption.adoptioncase.validation.ValidationUtil.flattenLists;
import static uk.gov.hmcts.reform.adoption.adoptioncase.validation.ValidationUtil.validateBasicCase;

public final class ApplicationValidation {

    private ApplicationValidation() {

    }

    public static List<String> validateIssue(CaseData caseData) {
        return flattenLists(
            validateBasicCase(caseData)
        );
    }
}
