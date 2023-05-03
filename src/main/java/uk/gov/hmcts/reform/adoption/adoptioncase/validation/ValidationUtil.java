package uk.gov.hmcts.reform.adoption.adoptioncase.validation;

import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.LocalAuthority;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.AdoptionAgencyOrLocalAuthority;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.SocialWorker;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Parent;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.YesNoNotSure;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.WEEKS;
import static java.util.Collections.emptyList;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.ApplyingWith.ALONE;

public final class ValidationUtil {

    public static final String EMPTY = " cannot be empty or null";
    public static final String YES = "YES";
    public static final String SOT_REQUIRED_APPLICANT1 = "Statement of truth must be accepted by the applicant1";
    public static final String SOT_REQUIRED_APPLICANT2 = "Statement of truth must be accepted by the applicant2";
    public static final String LESS_THAN_TEN_WEEKS_AGO = " can not be less than ten weeks ago.";

    private ValidationUtil() {
    }

    public static List<String> validateBasicCase(CaseData caseData) {
        boolean applyingAlone = ALONE.equals(caseData.getApplyingWith());

        return flattenLists(
            notNull(caseData.getApplyingWith(), "ApplyingWith"),
            notNull(caseData.getDateChildMovedIn(), "DateChildMovedIn"),
            validateDateChildMovedIn(caseData.getDateChildMovedIn(), "DateChildMovedIn"),
            validateApplicant1(caseData),
            validateApplicant2(applyingAlone, caseData)
        );
    }

    public static List<String> validateLocalAuthorityAndAdoptionAgency(LocalAuthority localAuthority,
                                                                       AdoptionAgencyOrLocalAuthority adopAgencyOrLA,
                                                                       YesOrNo hasAnotherAdopAgencyOrLA) {
        List<String> list = flattenLists(
            notNull(localAuthority.getLocalAuthorityContactEmail(), "LocalAuthorityEmail"),
            notNull(localAuthority.getLocalAuthorityPhoneNumber(), "LocalAuthorityPhoneNumber")
        );
        if (hasAnotherAdopAgencyOrLA.equals(YesOrNo.YES)) {
            list.addAll(flattenLists(
                notNull(adopAgencyOrLA.getAdopAgencyOrLaContactEmail(), "AdoptionAgencyOrLocalAuthorityEmail"),
                notNull(adopAgencyOrLA.getAdopAgencyOrLaPhoneNumber(), "AdoptionAgencyOrLocalAuthorityPhoneNumber")
            ));
        }
        return list;
    }

    public static List<String> validateSocialWorker(SocialWorker socialWorker) {
        return flattenLists(
            notNull(socialWorker.getSocialWorkerEmail(), "SocialWorkerEmail"),
            notNull(socialWorker.getSocialWorkerPhoneNumber(), "SocialWorkerPhoneNumber")
        );
    }


    public static List<String> validateBirthFather(Parent birthFather) {
        if (YES.equalsIgnoreCase(birthFather.getNameOnCertificate())) {
            return flattenLists(
                notNull(birthFather.getFirstName(), "BirthFatherFirstName"),
                notNull(birthFather.getLastName(), "BirthFatherLastName")
            );
        }
        return emptyList();
    }

    public static List<String> validateOtherParent(Parent otherParent) {
        if (YesNoNotSure.YES.equals(otherParent.getStillAlive())) {
            return flattenLists(
                notNull(otherParent.getFirstName(), "BirthFatherFirstName"),
                notNull(otherParent.getLastName(), "BirthFatherLastName")
            );
        }
        return emptyList();
    }

    private static List<String> validateApplicant1(CaseData caseData) {
        return flattenLists(
            notNull(caseData.getApplicant1().getFirstName(), "Applicant1FirstName"),
            notNull(caseData.getApplicant1().getLastName(), "Applicant1LastName"),
            notNull(caseData.getApplicant1().getEmailAddress(), "Applicant1EmailAddress"),
            notNull(caseData.getApplicant1().getPhoneNumber(), "Applicant1PhoneNumber"),
            hasStatementOfTruth(caseData.getApplicant1StatementOfTruth(), SOT_REQUIRED_APPLICANT1),
            notNull(caseData.getApplicant1SotFullName(), "Applicant1SotFullName")
        );
    }

    private static List<String> validateApplicant2(boolean applyingAlone, CaseData caseData) {
        if (!applyingAlone) {
            return flattenLists(
                notNull(caseData.getApplicant2().getFirstName(), "Applicant2FirstName"),
                notNull(caseData.getApplicant2().getLastName(), "Applicant2LastName"),
                notNull(caseData.getApplicant2().getEmailAddress(), "Applicant2EmailAddress"),
                notNull(caseData.getApplicant2().getPhoneNumber(), "Applicant2PhoneNumber"),
                hasStatementOfTruth(caseData.getApplicant2StatementOfTruth(), SOT_REQUIRED_APPLICANT2),
                notNull(caseData.getApplicant2SotFullName(), "Applicant2SotFullName")
            );
        }
        return emptyList();
    }

    public static List<String> validateDateChildMovedIn(LocalDate dateChildMovedIn, String field) {
        if (nonNull(dateChildMovedIn) && dateChildMovedIn.isAfter(LocalDate.now().minus(10, WEEKS))) {
            return List.of(field + LESS_THAN_TEN_WEEKS_AGO);
        }
        return emptyList();
    }

    private static List<String> hasStatementOfTruth(YesOrNo statementOfTruth, String message) {
        return YesOrNo.YES.equals(statementOfTruth) ? emptyList() : List.of(message);
    }

    public static List<String> notNull(Object value, String field) {
        return isEmpty(value) ? List.of(field + EMPTY) : emptyList();
    }

    @SafeVarargs
    public static <E> List<E> flattenLists(List<E>... lists) {
        return Stream.of(lists).flatMap(Collection::stream).collect(Collectors.toCollection(ArrayList::new));
    }
}
