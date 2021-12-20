package uk.gov.hmcts.reform.adoption.adoptioncase.validation;

import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public final class ValidationUtil {

    public static final String EMPTY = " cannot be empty or null";

    private ValidationUtil() {
    }

    public static List<String> validateBasicCase(CaseData caseData) {
        return flattenLists(
            notNull(caseData.getApplicant().getFirstName(), "ApplicantFirstName"),
            notNull(caseData.getApplicant().getLastName(), "ApplicantLastName"),
            notNull(caseData.getApplicant().getGender(), "ApplicantGender"),
            notNull(
                caseData.getApplicant().getKeepContactDetailsConfidential(),
                "ApplicantKeepContactDetailsConfidential"
            )
        );
    }

    public static List<String> validateApplicantBasicCase(CaseData caseData) {
        return flattenLists(
            notNull(caseData.getApplicant().getFirstName(), "ApplicantFirstName"),
            notNull(caseData.getApplicant().getLastName(), "ApplicantLastName"),
            notNull(caseData.getApplicant().getGender(), "ApplicantGender")
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
