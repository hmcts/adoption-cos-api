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
