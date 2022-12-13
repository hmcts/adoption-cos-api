package uk.gov.hmcts.reform.adoption.adoptioncase.validation;

import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Applicant;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ApplyingWith;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.DirectionsOrderData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Parent;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.RecipientsInTheCase;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.ADOP_AGENCY_NOT_APPLICABLE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.APPLICANTS_LA_NOT_APPLICABLE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.BIRTH_FATHER_NOT_APPLICABLE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.BIRTH_MOTHER_NOT_APPLICABLE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.CHILDS_LA_NOT_APPLICABLE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.FIRST_APPLICANT_NOT_APPLICABLE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.LEGAL_GUARDIAN_NOT_APPLICABLE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.NOT_APPLICABLE_ERROR;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.OTHER_ADOP_AGENCY_NOT_APPLICABLE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.OTHER_PARENT_AGENCY_NOT_APPLICABLE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.SECOND_APPLICANT_NOT_APPLICABLE;


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

    public static String isValidRecipientForGeneralOrder(DirectionsOrderData.GeneralDirectionRecipients choice, CaseData caseData) {
        switch (choice) {
            case APPLICANT2:
                return isApplicantValidForCase(caseData.getApplicant2()) ? SECOND_APPLICANT_NOT_APPLICABLE : null;

            case APPLICANT1:
                return isApplicantValidForCase(caseData.getApplicant1()) ? FIRST_APPLICANT_NOT_APPLICABLE : null;

            case RESPONDENT_BIRTH_MOTHER:
                return isValidParentForTheCase(caseData.getBirthMother()) ? BIRTH_MOTHER_NOT_APPLICABLE : null;

            case RESPONDENT_BIRTH_FATHER:
                return isValidParentForTheCase(caseData.getBirthFather()) ? BIRTH_FATHER_NOT_APPLICABLE : null;

            case LEGAL_GUARDIAN_CAFCASS:
                return getChildRepresentedByLegalGuardianErrorMessage(caseData);

            case CHILDS_LOCAL_AUTHORITY:
                if (isEmpty(caseData.getChildSocialWorker().getLocalAuthority())) {
                    return CHILDS_LA_NOT_APPLICABLE;
                }
                break;
            case APPLICANTS_LOCAL_AUTHORITY:
                if (isEmpty(caseData.getApplicantSocialWorker().getLocalAuthority())) {
                    return APPLICANTS_LA_NOT_APPLICABLE;
                }
                break;
            case ADOPTION_AGENCY:
                if (isEmpty(caseData.getAdopAgencyOrLA().getAdopAgencyOrLaName())) {
                    return ADOP_AGENCY_NOT_APPLICABLE;
                }
                break;
            case OTHER_ADOPTION_AGENCY:
                if (isValidAdoptionAgency(caseData)) {
                    return OTHER_ADOP_AGENCY_NOT_APPLICABLE;
                }
                break;
            default:
                if (isThereAnyOtherPersonWithParentalResponsibility(caseData)) {
                    return OTHER_PARENT_AGENCY_NOT_APPLICABLE;
                }
                break;

        }
        return null;
    }

    private static String getChildRepresentedByLegalGuardianErrorMessage(CaseData caseData) {
        return isEmpty(caseData.getIsChildRepresentedByGuardian())
            || caseData.getIsChildRepresentedByGuardian().equals(YesOrNo.NO) ? LEGAL_GUARDIAN_NOT_APPLICABLE : null;
    }

    private static boolean isThereAnyOtherPersonWithParentalResponsibility(CaseData caseData) {
        return isEmpty(caseData.getIsThereAnyOtherPersonWithParentalResponsibility())
            || caseData.getIsThereAnyOtherPersonWithParentalResponsibility().equals(YesOrNo.NO);
    }

    private static boolean isValidAdoptionAgency(CaseData caseData) {
        return isEmpty(caseData.getHasAnotherAdopAgencyOrLAinXui())
            || caseData.getHasAnotherAdopAgencyOrLAinXui().equals(YesOrNo.NO);
    }

    private static boolean isValidParentForTheCase(Parent parent) {
        return isEmpty(parent.getFirstName());
    }

    private static boolean isApplicantValidForCase(Applicant caseData) {
        return isNotEmpty(caseData) && isEmpty(caseData.getFirstName());
    }
}
