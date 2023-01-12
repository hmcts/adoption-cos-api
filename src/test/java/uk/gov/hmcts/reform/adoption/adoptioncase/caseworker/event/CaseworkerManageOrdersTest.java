package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.ccd.sdk.ResolvedCCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.Event;
import uk.gov.hmcts.ccd.sdk.api.HasRole;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.AddressUK;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.ManageOrders;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.GeneralDirectionOrderTypes;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ManageOrdersData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.AdoptionOrderData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.SocialWorker;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Parent;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Applicant;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.AdoptionAgencyOrLocalAuthority;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.platform.commons.util.ReflectionUtils.findMethod;
import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseworkerManageOrders.CASEWORKER_MANAGE_ORDERS;
import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.ManageOrders.ERROR_CHECK_HEARINGS_SELECTION;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.AdoptionOrderData.RecipientsA206.RESPONDENT_BIRTH_MOTHER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.AdoptionOrderData.RecipientsA206.RESPONDENT_BIRTH_FATHER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.AdoptionOrderData.RecipientsA206.ADOPTION_AGENCY;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.AdoptionOrderData.RecipientsA206.OTHER_PERSON_WITH_PARENTAL_RESPONSIBILITY;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.AdoptionOrderData.RecipientsA206.LEGAL_GUARDIAN_CAFCASS;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.AdoptionOrderData.RecipientsA206.CHILDS_LOCAL_AUTHORITY;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.AdoptionOrderData.RecipientsA206.OTHER_ADOPTION_AGENCY;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.AdoptionOrderData.RecipientsA206.APPLICANTS_LOCAL_AUTHORITY;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.AdoptionOrderData.RecipientsA76.APPLICANT1;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.AdoptionOrderData.RecipientsA76.APPLICANT2;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.ManageOrdersData.ManageOrderType.CASE_MANAGEMENT_ORDER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.ManageOrdersData.ManageOrderType.FINAL_ADOPTION_ORDER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.ManageOrdersData.ManageOrderType.GENERAL_DIRECTIONS_ORDER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.ManageOrdersData.ModeOfHearing.SET_MODE_OF_HEARING;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.ManageOrdersData.HearingNotices.LIST_FOR_FIRST_HEARING;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.ManageOrdersData.HearingNotices.LIST_FOR_FURTHER_HEARINGS;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.ManageOrdersData.HearingNotices.HEARING_DATE_TO_BE_SPECIFIED_IN_THE_FUTURE;
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
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

/**
 * The type Caseworker manage orders test.
 */
@ExtendWith(MockitoExtension.class)
class CaseworkerManageOrdersTest {

    @InjectMocks
    private ManageOrders manageOrdersPage;

    @InjectMocks
    private CaseworkerManageOrders caseworkerManageOrders;

    /**
     * Caseworker manage orders configure test.
     */
    @Test
    void caseworkerManageOrdersConfigureTest() {
        final ConfigBuilderImpl<CaseData, State, UserRole> configBuilder = createCaseDataConfigBuilder();
        caseworkerManageOrders.configure(configBuilder);
        assertThat(getEventsFrom(configBuilder).values())
            .extracting(Event::getId)
            .contains(CASEWORKER_MANAGE_ORDERS);
    }

    @Test
    void caseworkerManageOrdersAboutToSubmitManageOrderList() {
        var caseDetails = getCaseDetails();
        caseDetails.getData().getManageOrdersData().setManageOrderType(CASE_MANAGEMENT_ORDER);
        var result = caseworkerManageOrders.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getManageOrderList().size()).isEqualTo(1);
        caseDetails.getData().getManageOrdersData().setManageOrderType(CASE_MANAGEMENT_ORDER);
        result = caseworkerManageOrders.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getManageOrderList().size()).isEqualTo(2);
        assertThat(result.getData().getDirectionsOrderList()).isNull();
        assertThat(result.getData().getAdoptionOrderList()).isNull();
        result.getData().getManageOrderList().forEach(listValueObj -> assertTrue(
            listValueObj.getValue().getSubmittedDateManageOrder().isBefore(LocalDateTime.now())));
    }

    @Test
    void caseworkerMidEventGeneralDirectionRecipients() {
        var caseDetails = getCaseDetails();
        caseDetails.getData().getDirectionsOrderData()
            .setGeneralDirectionOrderTypes(GeneralDirectionOrderTypes.GENERAL_DIRECTION_PRODUCTION_ORDER);
        caseDetails.getData().getDirectionsOrderData().setGeneralDirectionRecipientsList(null);
        var response = caseworkerManageOrders
            .midEventGeneralDirectionRecipients(caseDetails, caseDetails);
        assertThat(response.getErrors().size()).isEqualTo(1);
    }

    @Test
    void caseworkerManageOrdersAboutToSubmitDirectionsOrderList() {
        var caseDetails = getCaseDetails();
        caseDetails.getData().getManageOrdersData().setManageOrderType(GENERAL_DIRECTIONS_ORDER);
        var result = caseworkerManageOrders.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getDirectionsOrderList().size()).isEqualTo(1);
        caseDetails.getData().getManageOrdersData().setManageOrderType(GENERAL_DIRECTIONS_ORDER);
        result = caseworkerManageOrders.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getDirectionsOrderList().size()).isEqualTo(2);
        assertThat(result.getData().getManageOrderList()).isNull();
        assertThat(result.getData().getAdoptionOrderList()).isNull();
        result.getData().getDirectionsOrderList().forEach(listValueObj -> assertTrue(
            listValueObj.getValue().getSubmittedDateDirectionsOrder().isBefore(LocalDateTime.now())));
    }

    @Test
    void caseworkerManageOrdersAboutToSubmitAdoptionOrderList() {
        var caseDetails = getCaseDetails();
        caseDetails.getData().getManageOrdersData().setManageOrderType(FINAL_ADOPTION_ORDER);
        var result = caseworkerManageOrders.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getAdoptionOrderList().size()).isEqualTo(1);
        caseDetails.getData().getManageOrdersData().setManageOrderType(FINAL_ADOPTION_ORDER);
        result = caseworkerManageOrders.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getAdoptionOrderList().size()).isEqualTo(2);
        assertThat(result.getData().getDirectionsOrderList()).isNull();
        assertThat(result.getData().getManageOrderList()).isNull();
        result.getData().getAdoptionOrderList().forEach(listValueObj -> assertTrue(
            listValueObj.getValue().getSubmittedDateAdoptionOrder().isBefore(LocalDateTime.now())));
    }

    @Test
    public void shouldValidateAdoptionOrderDataRecipientsToBeAMandateSelection() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().getAdoptionOrderData().setRecipientsListA76(null);
        caseDetails.getData().getAdoptionOrderData().setRecipientsListA206(null);
        AboutToStartOrSubmitResponse<CaseData, State> response = caseworkerManageOrders.midEventFinalRecipients(caseDetails, caseDetails);
        assertThat(response.getErrors()).contains(ERROR_CHECK_RECIPIENTS_SELECTION);
    }

    @Test
    public void shouldValidateAdoptionOrderDataRecipientsSelectionToBeAnApplicableFirstApplicant() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setApplicant1(new Applicant());
        Set<AdoptionOrderData.RecipientsA76> recipients = new HashSet<>();
        recipients.add(APPLICANT1);
        caseDetails.getData().getAdoptionOrderData().setRecipientsListA76(recipients);
        AboutToStartOrSubmitResponse<CaseData, State> response = caseworkerManageOrders.midEventFinalRecipients(caseDetails, caseDetails);
        assertThat(response.getErrors()).doesNotContain(ERROR_CHECK_RECIPIENTS_SELECTION);
        assertThat(response.getErrors()).contains(FIRST_APPLICANT_NOT_APPLICABLE);
    }

    @Test
    public void shouldValidateAdoptionOrderDataRecipientsSelectionToBeAnApplicableSecondApplicant() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setApplicant2(new Applicant());
        Set<AdoptionOrderData.RecipientsA76> recipients = new HashSet<>();
        recipients.add(APPLICANT2);
        caseDetails.getData().getAdoptionOrderData().setRecipientsListA76(recipients);
        AboutToStartOrSubmitResponse<CaseData, State> response = caseworkerManageOrders.midEventFinalRecipients(caseDetails, caseDetails);
        assertThat(response.getErrors()).doesNotContain(ERROR_CHECK_RECIPIENTS_SELECTION);
        assertThat(response.getErrors()).contains(SECOND_APPLICANT_NOT_APPLICABLE);
    }

    @Test
    public void shouldValidateAdoptionOrderDataRecipientsSelectionToBeAnApplicableRespondentBirthMother() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setBirthMother(new Parent());
        Set<AdoptionOrderData.RecipientsA206> recipients = new HashSet<>();
        recipients.add(RESPONDENT_BIRTH_MOTHER);
        caseDetails.getData().getAdoptionOrderData().setRecipientsListA206(recipients);
        AboutToStartOrSubmitResponse<CaseData, State> response = caseworkerManageOrders.midEventFinalRecipients(caseDetails, caseDetails);
        assertThat(response.getErrors()).doesNotContain(ERROR_CHECK_RECIPIENTS_SELECTION);
        assertThat(response.getErrors()).contains(BIRTH_MOTHER_NOT_APPLICABLE);
    }

    @Test
    public void shouldValidateAdoptionOrderDataRecipientsSelectionToBeAnApplicableRespondentBirthFather() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setBirthFather(new Parent());
        Set<AdoptionOrderData.RecipientsA206> recipients = new HashSet<>();
        recipients.add(RESPONDENT_BIRTH_FATHER);
        caseDetails.getData().getAdoptionOrderData().setRecipientsListA206(recipients);
        AboutToStartOrSubmitResponse<CaseData, State> response = caseworkerManageOrders.midEventFinalRecipients(caseDetails, caseDetails);
        assertThat(response.getErrors()).doesNotContain(ERROR_CHECK_RECIPIENTS_SELECTION);
        assertThat(response.getErrors()).contains(BIRTH_FATHER_NOT_APPLICABLE);
    }

    @Test
    public void shouldValidateAdoptionOrderDataRecipientsSelectionToBeAnApplicableLegalGuardian() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setIsChildRepresentedByGuardian(YesOrNo.NO);
        Set<AdoptionOrderData.RecipientsA206> recipients = new HashSet<>();
        recipients.add(LEGAL_GUARDIAN_CAFCASS);
        caseDetails.getData().getAdoptionOrderData().setRecipientsListA206(recipients);
        AboutToStartOrSubmitResponse<CaseData, State> response = caseworkerManageOrders.midEventFinalRecipients(caseDetails, caseDetails);
        assertThat(response.getErrors()).doesNotContain(ERROR_CHECK_RECIPIENTS_SELECTION);
        assertThat(response.getErrors()).contains(LEGAL_GUARDIAN_NOT_APPLICABLE);
    }

    @Test
    public void shouldValidateAdoptionOrderDataRecipientsSelectionToBeAnApplicableChildsLA() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setChildSocialWorker(new SocialWorker());
        Set<AdoptionOrderData.RecipientsA206> recipients = new HashSet<>();
        recipients.add(CHILDS_LOCAL_AUTHORITY);
        caseDetails.getData().getAdoptionOrderData().setRecipientsListA206(recipients);
        AboutToStartOrSubmitResponse<CaseData, State> response = caseworkerManageOrders.midEventFinalRecipients(caseDetails, caseDetails);
        assertThat(response.getErrors()).doesNotContain(ERROR_CHECK_RECIPIENTS_SELECTION);
        assertThat(response.getErrors()).contains(CHILDS_LA_NOT_APPLICABLE);
    }

    @Test
    public void shouldValidateAdoptionOrderDataRecipientsSelectionToBeAnApplicableApplicantsLA() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setApplicantSocialWorker(new SocialWorker());
        Set<AdoptionOrderData.RecipientsA206> recipients = new HashSet<>();
        recipients.add(APPLICANTS_LOCAL_AUTHORITY);
        caseDetails.getData().getAdoptionOrderData().setRecipientsListA206(recipients);
        AboutToStartOrSubmitResponse<CaseData, State> response = caseworkerManageOrders.midEventFinalRecipients(caseDetails, caseDetails);
        assertThat(response.getErrors()).doesNotContain(ERROR_CHECK_RECIPIENTS_SELECTION);
        assertThat(response.getErrors()).contains(APPLICANTS_LA_NOT_APPLICABLE);
    }

    @Test
    public void shouldValidateAdoptionOrderDataRecipientsSelectionToBeAnApplicableAdopAgency() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setAdopAgencyOrLA(new AdoptionAgencyOrLocalAuthority());
        Set<AdoptionOrderData.RecipientsA206> recipients = new HashSet<>();
        recipients.add(ADOPTION_AGENCY);
        caseDetails.getData().getAdoptionOrderData().setRecipientsListA206(recipients);
        AboutToStartOrSubmitResponse<CaseData, State> response = caseworkerManageOrders.midEventFinalRecipients(caseDetails, caseDetails);
        assertThat(response.getErrors()).doesNotContain(ERROR_CHECK_RECIPIENTS_SELECTION);
        assertThat(response.getErrors()).contains(ADOP_AGENCY_NOT_APPLICABLE);
    }

    @Test
    public void shouldValidateAdoptionOrderDataRecipientsSelectionToBeAnApplicableOtherAdopAgency() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setHasAnotherAdopAgencyOrLAinXui(null);
        Set<AdoptionOrderData.RecipientsA206> recipients = new HashSet<>();
        recipients.add(OTHER_ADOPTION_AGENCY);
        caseDetails.getData().getAdoptionOrderData().setRecipientsListA206(recipients);
        AboutToStartOrSubmitResponse<CaseData, State> response = caseworkerManageOrders.midEventFinalRecipients(caseDetails, caseDetails);
        assertThat(response.getErrors()).doesNotContain(ERROR_CHECK_RECIPIENTS_SELECTION);
        assertThat(response.getErrors()).contains(OTHER_ADOP_AGENCY_NOT_APPLICABLE);
        caseDetails.getData().setHasAnotherAdopAgencyOrLAinXui(YesOrNo.NO);
        response = caseworkerManageOrders.midEventFinalRecipients(caseDetails, caseDetails);
        assertThat(response.getErrors()).doesNotContain(ERROR_CHECK_RECIPIENTS_SELECTION);
        assertThat(response.getErrors()).contains(OTHER_ADOP_AGENCY_NOT_APPLICABLE);
    }

    @Test
    public void shouldValidateManageOrdersOrderDataRecipientsSelectionToBeAnApplicableOtherLA() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setLocalAuthority(null);
        Set<ManageOrdersData.Recipients> recipients = new HashSet<>();
        recipients.add(ManageOrdersData.Recipients.OTHER_LOCAL_AUTHORITY);
        caseDetails.getData().getManageOrdersData().setRecipientsList(recipients);
        AboutToStartOrSubmitResponse<CaseData, State> response =
            caseworkerManageOrders.midEventManageOrderRecipients(caseDetails, caseDetails);
        assertThat(response.getErrors()).doesNotContain(ERROR_CHECK_RECIPIENTS_SELECTION);
        assertThat(response.getErrors()).contains(OTHER_LA_NOT_APPLICABLE);
    }

    @Test
    public void shouldValidateAdoptionOrderDataRecipientsSelectionToBeAnApplicableOtherParent() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setIsThereAnyOtherPersonWithParentalResponsibility(null);
        Set<AdoptionOrderData.RecipientsA206> recipients = new HashSet<>();
        recipients.add(OTHER_PERSON_WITH_PARENTAL_RESPONSIBILITY);
        caseDetails.getData().getAdoptionOrderData().setRecipientsListA206(recipients);
        AboutToStartOrSubmitResponse<CaseData, State> response = caseworkerManageOrders.midEventFinalRecipients(caseDetails, caseDetails);
        assertThat(response.getErrors()).doesNotContain(ERROR_CHECK_RECIPIENTS_SELECTION);
        assertThat(response.getErrors()).contains(OTHER_PARENT_AGENCY_NOT_APPLICABLE);
        caseDetails.getData().setIsThereAnyOtherPersonWithParentalResponsibility(YesOrNo.NO);
        response = caseworkerManageOrders.midEventFinalRecipients(caseDetails, caseDetails);
        assertThat(response.getErrors()).doesNotContain(ERROR_CHECK_RECIPIENTS_SELECTION);
        assertThat(response.getErrors()).contains(OTHER_PARENT_AGENCY_NOT_APPLICABLE);
    }

    @Test
    public void shouldValidateHearingDateToBeSpecifiedInTheFutureSelectedByUser() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        Set<ManageOrdersData.HearingNotices> hearingNotices = new HashSet<>();
        hearingNotices.add(HEARING_DATE_TO_BE_SPECIFIED_IN_THE_FUTURE);
        caseDetails.getData().getManageOrdersData().setHearingNotices(hearingNotices);
        AboutToStartOrSubmitResponse<CaseData, State> response = manageOrdersPage.midEvent(caseDetails, caseDetails);
        assertThat(response.getErrors()).isEmpty();
    }

    @Test
    public void shouldValidateValuesSelectedByUser() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        Set<ManageOrdersData.HearingNotices> hearingNotices = new HashSet<>();
        hearingNotices.add(LIST_FOR_FIRST_HEARING);
        hearingNotices.add(LIST_FOR_FURTHER_HEARINGS);
        caseDetails.getData().getManageOrdersData().setHearingNotices(hearingNotices);

        Set<ManageOrdersData.ModeOfHearing> modeOfHearings = new HashSet<>();
        modeOfHearings.add(SET_MODE_OF_HEARING);
        caseDetails.getData().getManageOrdersData().setModeOfHearing(modeOfHearings);

        AboutToStartOrSubmitResponse<CaseData, State> response = manageOrdersPage.midEvent(caseDetails, caseDetails);
        assertThat(response.getErrors()).isEmpty();
    }

    @Test
    public void shouldReturnErrorsIfHearingDateToBeSpecifiedInTheFutureAccompaniesWithAll() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        Set<ManageOrdersData.HearingNotices> hearingNotices = new HashSet<>();
        hearingNotices.add(LIST_FOR_FIRST_HEARING);
        hearingNotices.add(LIST_FOR_FURTHER_HEARINGS);
        hearingNotices.add(HEARING_DATE_TO_BE_SPECIFIED_IN_THE_FUTURE);
        caseDetails.getData().getManageOrdersData().setHearingNotices(hearingNotices);

        Set<ManageOrdersData.ModeOfHearing> modeOfHearings = new HashSet<>();
        modeOfHearings.add(SET_MODE_OF_HEARING);
        caseDetails.getData().getManageOrdersData().setModeOfHearing(modeOfHearings);

        AboutToStartOrSubmitResponse<CaseData, State> response = manageOrdersPage.midEvent(caseDetails, caseDetails);
        assertThat(response.getErrors())
            .contains(ERROR_CHECK_HEARINGS_SELECTION);
    }

    @Test
    public void shouldReturnErrorsIfHearingDateToBeSpecifiedInTheFutureAccompaniesWithModeOfHearing() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        Set<ManageOrdersData.HearingNotices> hearingNotices = new HashSet<>();
        hearingNotices.add(HEARING_DATE_TO_BE_SPECIFIED_IN_THE_FUTURE);
        caseDetails.getData().getManageOrdersData().setHearingNotices(hearingNotices);

        Set<ManageOrdersData.ModeOfHearing> modeOfHearings = new HashSet<>();
        modeOfHearings.add(SET_MODE_OF_HEARING);
        caseDetails.getData().getManageOrdersData().setModeOfHearing(modeOfHearings);

        AboutToStartOrSubmitResponse<CaseData, State> response = manageOrdersPage.midEvent(caseDetails, caseDetails);
        assertThat(response.getErrors())
            .contains(ERROR_CHECK_HEARINGS_SELECTION);
    }

    @Test
    public void shouldReturnErrorsIfHearingDateToBeSpecifiedInTheFutureAccompaniesWithHearingNoticesOptions() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        Set<ManageOrdersData.HearingNotices> hearingNotices = new HashSet<>();
        hearingNotices.add(LIST_FOR_FIRST_HEARING);
        hearingNotices.add(LIST_FOR_FURTHER_HEARINGS);
        hearingNotices.add(HEARING_DATE_TO_BE_SPECIFIED_IN_THE_FUTURE);
        caseDetails.getData().getManageOrdersData().setHearingNotices(hearingNotices);

        AboutToStartOrSubmitResponse<CaseData, State> response = manageOrdersPage.midEvent(caseDetails, caseDetails);
        assertThat(response.getErrors())
            .contains(ERROR_CHECK_HEARINGS_SELECTION);
    }

    @Test
    public void shouldCreateDynamicList() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        final CaseData caseData = caseDetails.getData();
        AdoptionAgencyOrLocalAuthority adoptionAgencyOrLocalAuthority = new AdoptionAgencyOrLocalAuthority();
        adoptionAgencyOrLocalAuthority.setAdopAgencyOrLaName("TEST_NAME");
        caseData.setAdopAgencyOrLA(adoptionAgencyOrLocalAuthority);
        SocialWorker socialWorker = new SocialWorker();
        socialWorker.setSocialWorkerName("TEST_NAME");
        caseData.setChildSocialWorker(socialWorker);
        caseData.setApplicantSocialWorker(socialWorker);
        caseData.setHasAnotherAdopAgencyOrLAinXui(YesOrNo.YES);
        caseData.getOtherAdoptionAgencyOrLA().setAgencyOrLaName("TEST_NAME");
        AddressUK addressUK = new AddressUK();
        caseData.getOtherAdoptionAgencyOrLA().setAgencyAddress(addressUK);
        caseData.getOtherAdoptionAgencyOrLA().getAgencyAddress().setPostTown("TEST_TOWN");
        caseData.getOtherAdoptionAgencyOrLA().getAgencyAddress().setPostCode("TEST_POST_CODE");
        caseData.getPlacementOfTheChildList();
        assertThat(caseData.getPlacementOfTheChildList().getListItems().size()).isEqualTo(4);
    }

    /**
     * Helper method to build CaseDetails .
     *
     * @return - ConfigBuilderImpl
     */
    private CaseDetails<CaseData, State> getCaseDetails() {
        return CaseDetails.<CaseData, State>builder()
            .data(caseData())
            .id(1L)
            .build();
    }

    /**
     * Gets events from.
     *
     * @param <T>           the type parameter
     * @param <S>           the type parameter
     * @param <R>           the type parameter
     * @param configBuilder the config builder
     * @return the events from
     */
    @SuppressWarnings({"unchecked"})
    public static <T, S, R extends HasRole> Map<String, Event<T, R, S>> getEventsFrom(
        final ConfigBuilderImpl<T, S, R> configBuilder) {

        return (Map<String, Event<T, R, S>>) findMethod(ConfigBuilderImpl.class, "getEvents")
            .map(method -> {
                try {
                    method.setAccessible(true);
                    return method.invoke(configBuilder);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new AssertionError("Unable to invoke ConfigBuilderImpl.class method getEvents", e);
                }
            })
            .orElseThrow(() -> new AssertionError("Unable to find ConfigBuilderImpl.class method getEvents"));
    }


    /**
     * Create case data config builder config builder.
     *
     * @return the config builder
     */
    public static ConfigBuilderImpl<CaseData, State, UserRole> createCaseDataConfigBuilder() {
        return new ConfigBuilderImpl<>(new ResolvedCCDConfig<>(
            CaseData.class,
            State.class,
            UserRole.class,
            new HashMap<>(),
            ImmutableSet.copyOf(State.class.getEnumConstants())));
    }
}
