package uk.gov.hmcts.reform.adoption.adoptioncase.validation;

import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Parent;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Applicant;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.APPLICANT1;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.APPLICANT2;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.RESPONDENT_BIRTH_MOTHER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.RESPONDENT_BIRTH_FATHER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.LEGAL_GUARDIAN_CAFCASS;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.CHILDS_LOCAL_AUTHORITY;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.APPLICANTS_LOCAL_AUTHORITY;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.OTHER_LOCAL_AUTHORITY;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.ADOPTION_AGENCY;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.OTHER_ADOPTION_AGENCY;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.OTHER_PERSON_WITH_PARENTAL_RESPONSIBILITY;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.ADOP_AGENCY_NOT_APPLICABLE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.APPLICANTS_LA_NOT_APPLICABLE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.BIRTH_FATHER_NOT_APPLICABLE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.BIRTH_MOTHER_NOT_APPLICABLE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.CHILDS_LA_NOT_APPLICABLE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.FIRST_APPLICANT_NOT_APPLICABLE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.LEGAL_GUARDIAN_NOT_APPLICABLE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.OTHER_ADOP_AGENCY_NOT_APPLICABLE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.OTHER_LA_NOT_APPLICABLE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.OTHER_PARENT_AGENCY_NOT_APPLICABLE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.SECOND_APPLICANT_NOT_APPLICABLE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.ERROR_CHECK_RECIPIENTS_SELECTION;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.ERROR_INVALID_RECIPIENTS_SELECTION;


public final class RecipientValidationUtil {

    private RecipientValidationUtil() {
    }

    public static AboutToStartOrSubmitResponse<CaseData, State> validateRecipients(
        Set<? extends Enum<?>> recipientsPrimary, Set<? extends Enum<?>> recipientsSecondary, CaseData caseData, List<String> errors) {
        if (isEmpty(recipientsPrimary) && isEmpty(recipientsSecondary)) {
            errors.add(ERROR_CHECK_RECIPIENTS_SELECTION);
            return AboutToStartOrSubmitResponse.<CaseData, State>builder()
                .data(caseData)
                .errors(errors)
                .build();
        }
        if (isNotEmpty(recipientsPrimary)) {
            recipientsPrimary.forEach(recipient -> Optional.ofNullable(isValidRecipientForGeneralOrder(
                recipient.name(),
                caseData
            )).ifPresent(errors::add));
        }
        if (isNotEmpty(recipientsSecondary)) {
            recipientsSecondary.forEach(recipient -> Optional.ofNullable(isValidRecipientForGeneralOrder(
                recipient.name(),
                caseData
            )).ifPresent(errors::add));
        }

        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .errors(errors)
            .build();
    }

    public static String isValidRecipientForGeneralOrder(String choice, CaseData caseData) {
        switch (choice) {
            case APPLICANT1:
                return isApplicantValidForCase(caseData.getApplicant1()) ? FIRST_APPLICANT_NOT_APPLICABLE : null;
            case APPLICANT2:
                return isApplicantValidForCase(caseData.getApplicant2()) ? SECOND_APPLICANT_NOT_APPLICABLE : null;
            case RESPONDENT_BIRTH_MOTHER:
                return isValidParentForTheCase(caseData.getBirthMother()) ? BIRTH_MOTHER_NOT_APPLICABLE : null;
            case RESPONDENT_BIRTH_FATHER:
                return isValidParentForTheCase(caseData.getBirthFather()) ? BIRTH_FATHER_NOT_APPLICABLE : null;
            case LEGAL_GUARDIAN_CAFCASS:
                return isEmpty(caseData.getIsChildRepresentedByGuardian())
                    || caseData.getIsChildRepresentedByGuardian().equals(YesOrNo.NO) ? LEGAL_GUARDIAN_NOT_APPLICABLE : null;
            case CHILDS_LOCAL_AUTHORITY:
                return isEmpty(caseData.getChildSocialWorker().getLocalAuthority()) ? CHILDS_LA_NOT_APPLICABLE : null;
            default:
                return isValidRecipientForGeneralOrderOthers(choice, caseData);
        }
    }

    public static String isValidRecipientForGeneralOrderOthers(String choice, CaseData caseData) {
        switch (choice) {
            case APPLICANTS_LOCAL_AUTHORITY:
                return isEmpty(caseData.getApplicantSocialWorker().getLocalAuthority()) ? APPLICANTS_LA_NOT_APPLICABLE : null;
            case ADOPTION_AGENCY:
                return isEmpty(caseData.getAdopAgencyOrLA().getAdopAgencyOrLaName()) ? ADOP_AGENCY_NOT_APPLICABLE : null;
            case OTHER_ADOPTION_AGENCY:
                return isEmpty(caseData.getHasAnotherAdopAgencyOrLAinXui())
                    || caseData.getHasAnotherAdopAgencyOrLAinXui().equals(YesOrNo.NO) ? OTHER_ADOP_AGENCY_NOT_APPLICABLE : null;
            case OTHER_PERSON_WITH_PARENTAL_RESPONSIBILITY:
                return isEmpty(caseData.getIsThereAnyOtherPersonWithParentalResponsibility())
                    || caseData.getIsThereAnyOtherPersonWithParentalResponsibility().equals(YesOrNo.NO)
                    ? OTHER_PARENT_AGENCY_NOT_APPLICABLE : null;
            case OTHER_LOCAL_AUTHORITY:
                return isEmpty(caseData.getLocalAuthority()) ? OTHER_LA_NOT_APPLICABLE : null;
            default:
                return ERROR_INVALID_RECIPIENTS_SELECTION;
        }
    }

    private static boolean isValidParentForTheCase(Parent parent) {
        return isEmpty(parent.getFirstName());
    }

    private static boolean isApplicantValidForCase(Applicant caseData) {
        return isNotEmpty(caseData) && isEmpty(caseData.getFirstName());
    }
}
