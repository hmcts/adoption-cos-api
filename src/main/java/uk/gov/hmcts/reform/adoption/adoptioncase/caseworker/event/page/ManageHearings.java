package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import lombok.extern.slf4j.Slf4j;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ApplyingWith;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ManageHearingDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.RecipientsInTheCase;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.MANDATORY_ERROR;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.NOT_APPLICABLE_ERROR;

@Slf4j
public class ManageHearings implements CcdPageConfiguration {

    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder.page("manageOrders1")
            .mandatory(CaseData::getManageHearingOptions)
            .page("manageOrders2", this::midEvent)
            .label("addNewHearing1","## Add new hearing")
            .complex(CaseData::getManageHearingDetails)
            .mandatory(ManageHearingDetails::getTypeOfHearing)
            .mandatory(ManageHearingDetails::getHearingDateAndTime)
            .mandatory(ManageHearingDetails::getLengthOfHearing)
            .mandatory(ManageHearingDetails::getJudge)
            .mandatory(ManageHearingDetails::getCourt)
            .mandatory(ManageHearingDetails::getIsInterpreterNeeded)
            .mandatory(ManageHearingDetails::getMethodOfHearing)
            .mandatory(ManageHearingDetails::getAccessibilityRequirements)
            .mandatory(ManageHearingDetails::getHearingDirections)
            .done()
            .page("manageOrders3", this::midEventAfterRecipientSelection)
            .mandatory(CaseData::getRecipientsInTheCase)
            .done();
    }

    @SuppressWarnings({"unchecked"})
    public AboutToStartOrSubmitResponse<CaseData, State> midEvent(
        CaseDetails<CaseData, State> details,
        CaseDetails<CaseData, State> detailsBefore
    ) {
        var caseData = details.getData();

        /* Check if Application is applied alone or with someone
        and based on that display proper Recipient */
        caseData.setRecipientsInTheCase(new TreeSet<>());
        settingApplicantRelatedRecipients(caseData);
        settingChildRelatedRecipients(caseData);
        settingParentRelatedRecipients(caseData);
        settingAdoptionAgencyRelatedRecipients(caseData);
        settingOtherPersonRelatedRecipients(caseData);

        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .build();
    }

    @SuppressWarnings({"unchecked"})
    public AboutToStartOrSubmitResponse<CaseData, State> midEventAfterRecipientSelection(
        CaseDetails<CaseData, State> details,
        CaseDetails<CaseData, State> detailsBefore
    ) {
        var caseData = details.getData();
        List<String> error = new ArrayList<>();

        checkingApplicantRelatedSelectedRecipients(caseData, error);

        checkingChildRelatedSelectedRecipient(caseData, error);

        checkingParentRelatedSelectedRecipients(caseData, error);

        checkingOtherPersonRelatedSelectedRecipients(caseData, error);

        checkingAdoptionAgencyRelatedSelectedRecipients(caseData, error);

        checkForMandatoryCheckbox(caseData, error, RecipientsInTheCase.APPLICANT1);
        checkForMandatoryCheckbox(caseData, error, RecipientsInTheCase.ADOPTION_AGENCY);
        checkForMandatoryCheckbox(caseData, error, RecipientsInTheCase.CHILD_LOCAL_AUTHORITY);
        checkForMandatoryCheckbox(caseData, error, RecipientsInTheCase.APPLICANT_LOCAL_AUTHORITY);

        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .errors(error)
            .build();
    }

    private void checkingAdoptionAgencyRelatedSelectedRecipients(CaseData caseData, List<String> error) {
        if (YesOrNo.NO.equals(caseData.getHasAnotherAdopAgencyOrLAinXui()) || caseData.getHasAnotherAdopAgencyOrLAinXui() == null) {
            checkForInvalidCheckboxSelection(caseData, error, RecipientsInTheCase.OTHER_ADOPTION_AGENCY);
        }
    }

    private void checkingOtherPersonRelatedSelectedRecipients(CaseData caseData, List<String> error) {
        if (YesOrNo.NO.equals(caseData.getOtherParent().getToBeServed())
            || caseData.getOtherParent().getToBeServed() == null) {
            checkForInvalidCheckboxSelection(caseData, error, RecipientsInTheCase.OTHER_PERSON_WITH_PARENTAL_RESPONSIBILITIES);
        }

        /* Check if Other Parent is represented by Solicitor */
        if (YesOrNo.NO.equals(caseData.getIsOtherParentRepresentedBySolicitor())
            || caseData.getIsOtherParentRepresentedBySolicitor() == null) {
            checkForInvalidCheckboxSelection(caseData, error, RecipientsInTheCase.OTHER_PERSON_WITH_PARENTAL_RESPONSIBILITIES_SOLICITOR);
        }
    }

    private void checkingParentRelatedSelectedRecipients(CaseData caseData, List<String> error) {
        if (YesOrNo.NO.equals(caseData.getBirthMother().getToBeServed()) || caseData.getBirthMother().getToBeServed() == null) {
            checkForInvalidCheckboxSelection(caseData, error, RecipientsInTheCase.RESPONDENT_MOTHER);
        }

        if (YesOrNo.NO.equals(caseData.getIsBirthMotherRepresentedBySolicitor())
            || caseData.getIsBirthMotherRepresentedBySolicitor() == null) {
            checkForInvalidCheckboxSelection(caseData, error, RecipientsInTheCase.BIRTH_MOTHER_SOLICITOR);
        }

        if (YesOrNo.NO.equals(caseData.getBirthFather().getToBeServed()) || caseData.getBirthFather().getToBeServed() == null) {
            checkForInvalidCheckboxSelection(caseData, error, RecipientsInTheCase.RESPONDENT_FATHER);
        }

        if (YesOrNo.NO.equals(caseData.getIsBirthFatherRepresentedBySolicitor())
            || caseData.getIsBirthFatherRepresentedBySolicitor() == null) {
            checkForInvalidCheckboxSelection(caseData, error, RecipientsInTheCase.BIRTH_FATHER_SOLICITOR);
        }
    }

    private void checkingChildRelatedSelectedRecipient(CaseData caseData, List<String> error) {
        if (YesOrNo.NO.equals(caseData.getIsChildRepresentedByGuardian()) || caseData.getIsChildRepresentedByGuardian() == null) {
            checkForInvalidCheckboxSelection(caseData, error, RecipientsInTheCase.LEGAL_GUARDIAN);
        }

        if (YesOrNo.NO.equals(caseData.getIsChildRepresentedBySolicitor()) || caseData.getIsChildRepresentedBySolicitor() == null) {
            checkForInvalidCheckboxSelection(caseData, error, RecipientsInTheCase.CHILD_SOLICITOR);
        }
    }

    private void checkingApplicantRelatedSelectedRecipients(CaseData caseData, List<String> error) {
        if (ApplyingWith.ALONE.equals(caseData.getApplyingWith())) {
            checkForInvalidCheckboxSelection(caseData, error, RecipientsInTheCase.APPLICANT2);
        }

        if (YesOrNo.NO.equals(caseData.getIsApplicantRepresentedBySolicitor())) {
            checkForInvalidCheckboxSelection(caseData, error, RecipientsInTheCase.APPLICANT_SOLICITOR);
        }
    }

    private void settingOtherPersonRelatedRecipients(CaseData caseData) {
        /* Check if Other Parent has selected To Be Served as Yes */
        if (YesOrNo.YES.equals(caseData.getOtherParent().getToBeServed())) {
            caseData.getRecipientsInTheCase().add(RecipientsInTheCase.OTHER_PERSON_WITH_PARENTAL_RESPONSIBILITIES);
        }

        /* Check if Other Parent is represented by Solicitor */
        if (YesOrNo.YES.equals(caseData.getIsOtherParentRepresentedBySolicitor())) {
            caseData.getRecipientsInTheCase().add(RecipientsInTheCase.OTHER_PERSON_WITH_PARENTAL_RESPONSIBILITIES_SOLICITOR);
        }
    }

    private void settingAdoptionAgencyRelatedRecipients(CaseData caseData) {
        /* Adoption Agency is by default selected */
        caseData.getRecipientsInTheCase().add(RecipientsInTheCase.ADOPTION_AGENCY);

        /* Check if application has another adoption agency Or Local Authority */
        if (YesOrNo.YES.equals(caseData.getHasAnotherAdopAgencyOrLAinXui())) {
            caseData.getRecipientsInTheCase().add(RecipientsInTheCase.OTHER_ADOPTION_AGENCY);
        }
    }

    private void settingParentRelatedRecipients(CaseData caseData) {
        /* Check if Birth Mother has selected To Be Served as Yes */
        if (YesOrNo.YES.equals(caseData.getBirthMother().getToBeServed())) {
            caseData.getRecipientsInTheCase().add(RecipientsInTheCase.RESPONDENT_MOTHER);
        }

        /* Check if Birth Mother is represented by Solicitor */
        if (YesOrNo.YES.equals(caseData.getIsBirthMotherRepresentedBySolicitor())) {
            caseData.getRecipientsInTheCase().add(RecipientsInTheCase.BIRTH_MOTHER_SOLICITOR);
        }

        /* Check if Birth Father has selected To Be Served as Yes */
        if (YesOrNo.YES.equals(caseData.getBirthFather().getToBeServed())) {
            caseData.getRecipientsInTheCase().add(RecipientsInTheCase.RESPONDENT_FATHER);
        }

        /* Check if Birth Father is represented by Solicitor */
        if (YesOrNo.YES.equals(caseData.getIsBirthFatherRepresentedBySolicitor())) {
            caseData.getRecipientsInTheCase().add(RecipientsInTheCase.BIRTH_FATHER_SOLICITOR);
        }
    }

    private void settingChildRelatedRecipients(CaseData caseData) {
        /* Check if Child is represented by Guardian */
        if (YesOrNo.YES.equals(caseData.getIsChildRepresentedByGuardian())) {
            caseData.getRecipientsInTheCase().add(RecipientsInTheCase.LEGAL_GUARDIAN);
        }

        /* Check if Child is represented by Solicitor */
        if (YesOrNo.YES.equals(caseData.getIsChildRepresentedBySolicitor())) {
            caseData.getRecipientsInTheCase().add(RecipientsInTheCase.CHILD_SOLICITOR);
        }

        /* Child Local Authority is by default selected */
        caseData.getRecipientsInTheCase().add(RecipientsInTheCase.CHILD_LOCAL_AUTHORITY);
    }

    private void settingApplicantRelatedRecipients(CaseData caseData) {
        if (ApplyingWith.ALONE.equals(caseData.getApplyingWith())) {
            caseData.getRecipientsInTheCase().add(RecipientsInTheCase.APPLICANT1);
        } else {
            caseData.getRecipientsInTheCase().add(RecipientsInTheCase.APPLICANT1);
            caseData.getRecipientsInTheCase().add(RecipientsInTheCase.APPLICANT2);
        }

        /* Check if Applicant is represented by Solicitor */
        if (YesOrNo.YES.equals(caseData.getIsApplicantRepresentedBySolicitor())) {
            caseData.getRecipientsInTheCase().add(RecipientsInTheCase.APPLICANT_SOLICITOR);
        }

        /* Applicant Local Authority is by default selected */
        caseData.getRecipientsInTheCase().add(RecipientsInTheCase.APPLICANT_LOCAL_AUTHORITY);
    }

    private void checkForMandatoryCheckbox(CaseData caseData, List<String> error, RecipientsInTheCase recipientsInTheCase) {
        if (!caseData.getRecipientsInTheCase().contains(recipientsInTheCase)) {
            error.add(recipientsInTheCase.getLabel() + MANDATORY_ERROR);
        }
    }

    private void checkForInvalidCheckboxSelection(CaseData caseData, List<String> error, RecipientsInTheCase recipientsInTheCase) {
        Optional<RecipientsInTheCase> optionalRecipient = caseData.getRecipientsInTheCase().stream()
            .filter(e -> e.equals(recipientsInTheCase))
            .findAny();
        if (optionalRecipient.isPresent()) {
            error.add(recipientsInTheCase.getLabel() + NOT_APPLICABLE_ERROR);
        }
    }
}
