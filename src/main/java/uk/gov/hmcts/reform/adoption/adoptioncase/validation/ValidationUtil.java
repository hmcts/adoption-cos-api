package uk.gov.hmcts.reform.adoption.adoptioncase.validation;

import uk.gov.hmcts.reform.adoption.adoptioncase.model.Application;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public final class ValidationUtil {

    public static final String EMPTY = " cannot be empty or null";
    public static final String MUST_BE_YES = " must be YES";
    public static final String SOT_REQUIRED = "Statement of truth must be accepted by the person making the application";

    private ValidationUtil() {
    }

    public static List<String> validateBasicCase(CaseData caseData) {
        return flattenLists(
            notNull(caseData.getApplicant1().getFirstName(), "Applicant1FirstName"),
            notNull(caseData.getApplicant1().getLastName(), "Applicant1LastName"),
            notNull(caseData.getApplicant1().getGender(), "Applicant1Gender"),
            notNull(caseData.getApplicant1().getKeepContactDetailsConfidential(), "Applicant1KeepContactDetailsConfidential"),
            hasStatementOfTruth(caseData.getApplication()),
            validatePrayer(caseData.getApplication().getApplicant1PrayerHasBeenGivenCheckbox())
        );
    }

    private static List<String> hasStatementOfTruth(Application application) {
        return application.hasStatementOfTruth() ? emptyList() : List.of(SOT_REQUIRED);
    }

    private static List<String> validatePrayer(Set<Application.ThePrayer> thePrayer) {
        final String field = "applicant1PrayerHasBeenGivenCheckbox";

        if (Objects.isNull(thePrayer)) {
            return List.of(field + EMPTY);
        } else if (thePrayer.isEmpty()) {
            return List.of(field + MUST_BE_YES);
        }
        return emptyList();
    }

    public static List<String> validateApplicant1BasicCase(CaseData caseData) {
        return flattenLists(
            notNull(caseData.getApplicant1().getFirstName(), "Applicant1FirstName"),
            notNull(caseData.getApplicant1().getLastName(), "Applicant1LastName"),
            notNull(caseData.getApplicant1().getGender(), "Applicant1Gender")
        );
    }

    public static List<String> notNull(Object value, String field) {
        return value == null ? List.of(field + EMPTY) : emptyList();
    }

    @SafeVarargs
    public static <E> List<E> flattenLists(List<E>... lists) {
        return Arrays.stream(lists).flatMap(Collection::stream).collect(toList());
    }
}
