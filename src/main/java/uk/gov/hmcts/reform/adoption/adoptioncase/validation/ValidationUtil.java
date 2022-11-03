package uk.gov.hmcts.reform.adoption.adoptioncase.validation;

import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ApplyingWith;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.LocalAuthority;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.AdoptionAgencyOrLocalAuthority;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.RecipientsInTheCase;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.SocialWorker;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Parent;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.WEEKS;
import static java.util.Collections.emptyList;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.ApplyingWith.ALONE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.NOT_APPLICABLE_ERROR;

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
        if (YesOrNo.YES.equals(otherParent.getStillAlive())) {
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
        return Stream.of(lists).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public static void checkForInvalidCheckboxSelection(CaseData caseData, List<String> error, RecipientsInTheCase recipientsInTheCase) {
        Optional<RecipientsInTheCase> optionalRecipient = caseData.getRecipientsInTheCase().stream()
            .filter(e -> e.equals(recipientsInTheCase))
            .findAny();
        if (optionalRecipient.isPresent()) {
            error.add(recipientsInTheCase.getLabel() + NOT_APPLICABLE_ERROR);
        }
    }

    public static void settingApplicantRelatedRecipients(CaseData caseData) {
        if (ApplyingWith.ALONE.equals(caseData.getApplyingWith())) {
            caseData.getRecipientsInTheCase().add(RecipientsInTheCase.APPLICANT1);
        } else {
            caseData.getRecipientsInTheCase().add(RecipientsInTheCase.APPLICANT1);
            caseData.getRecipientsInTheCase().add(RecipientsInTheCase.APPLICANT2);
        }

        /* Applicant Local Authority is by default selected */
        caseData.getRecipientsInTheCase().add(RecipientsInTheCase.APPLICANT_LOCAL_AUTHORITY);
    }

    public static void settingOtherPersonRelatedRecipients(CaseData caseData) {
        /* Check if Other Parent has selected To Be Served as Yes */
        if (YesOrNo.YES.equals(caseData.getOtherParent().getToBeServed())) {
            caseData.getRecipientsInTheCase().add(RecipientsInTheCase.OTHER_PERSON_WITH_PARENTAL_RESPONSIBILITIES);
        }
    }

    public static void settingAdoptionAgencyRelatedRecipients(CaseData caseData) {
        /* Adoption Agency is by default selected */
        caseData.getRecipientsInTheCase().add(RecipientsInTheCase.ADOPTION_AGENCY);

        /* Check if application has another adoption agency Or Local Authority */
        if (YesOrNo.YES.equals(caseData.getHasAnotherAdopAgencyOrLAinXui())) {
            caseData.getRecipientsInTheCase().add(RecipientsInTheCase.OTHER_ADOPTION_AGENCY);
        }
    }

    public static void settingParentRelatedRecipients(CaseData caseData) {
        /* Check if Birth Mother has selected To Be Served as Yes */
        if (YesOrNo.YES.equals(caseData.getBirthMother().getToBeServed())) {
            caseData.getRecipientsInTheCase().add(RecipientsInTheCase.RESPONDENT_MOTHER);
        }

        /* Check if Birth Father has selected To Be Served as Yes */
        if (YesOrNo.YES.equals(caseData.getBirthFather().getToBeServed())) {
            caseData.getRecipientsInTheCase().add(RecipientsInTheCase.RESPONDENT_FATHER);
        }
    }

    public static void settingChildRelatedRecipients(CaseData caseData) {
        /* Check if Child is represented by Guardian */
        if (YesOrNo.YES.equals(caseData.getIsChildRepresentedByGuardian())) {
            caseData.getRecipientsInTheCase().add(RecipientsInTheCase.LEGAL_GUARDIAN);
        }

        /* Child Local Authority is by default selected */
        caseData.getRecipientsInTheCase().add(RecipientsInTheCase.CHILD_LOCAL_AUTHORITY);
    }

    public static void checkingAdoptionAgencyRelatedSelectedRecipients(CaseData caseData, List<String> error) {
        if (YesOrNo.NO.equals(caseData.getHasAnotherAdopAgencyOrLAinXui()) || caseData.getHasAnotherAdopAgencyOrLAinXui() == null) {
            checkForInvalidCheckboxSelection(caseData, error, RecipientsInTheCase.OTHER_ADOPTION_AGENCY);
        }
    }

    public static void checkingOtherPersonRelatedSelectedRecipients(CaseData caseData, List<String> error) {
        if (YesOrNo.NO.equals(caseData.getOtherParent().getToBeServed())
            || caseData.getOtherParent().getToBeServed() == null) {
            checkForInvalidCheckboxSelection(caseData, error, RecipientsInTheCase.OTHER_PERSON_WITH_PARENTAL_RESPONSIBILITIES);
        }
    }

    public static void checkingParentRelatedSelectedRecipients(CaseData caseData, List<String> error) {
        if (YesOrNo.NO.equals(caseData.getBirthMother().getToBeServed()) || caseData.getBirthMother().getToBeServed() == null) {
            checkForInvalidCheckboxSelection(caseData, error, RecipientsInTheCase.RESPONDENT_MOTHER);
        }

        if (YesOrNo.NO.equals(caseData.getBirthFather().getToBeServed()) || caseData.getBirthFather().getToBeServed() == null) {
            checkForInvalidCheckboxSelection(caseData, error, RecipientsInTheCase.RESPONDENT_FATHER);
        }
    }

    public static void checkingChildRelatedSelectedRecipient(CaseData caseData, List<String> error) {
        if (YesOrNo.NO.equals(caseData.getIsChildRepresentedByGuardian()) || caseData.getIsChildRepresentedByGuardian() == null) {
            checkForInvalidCheckboxSelection(caseData, error, RecipientsInTheCase.LEGAL_GUARDIAN);
        }
    }

    public static void checkingApplicantRelatedSelectedRecipients(CaseData caseData, List<String> error) {
        if (ApplyingWith.ALONE.equals(caseData.getApplyingWith())) {
            checkForInvalidCheckboxSelection(caseData, error, RecipientsInTheCase.APPLICANT2);
        }
    }
}
