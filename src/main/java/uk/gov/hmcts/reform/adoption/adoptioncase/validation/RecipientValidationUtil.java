package uk.gov.hmcts.reform.adoption.adoptioncase.validation;

import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ApplyingWith;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.RecipientsInTheCase;

import java.util.List;
import java.util.Optional;

import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.NOT_APPLICABLE_ERROR;

public final class RecipientValidationUtil {

    private RecipientValidationUtil() {
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
