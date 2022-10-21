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
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.ManageHearings;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ApplyingWith;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ManageHearingDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MethodOfHearing;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.idam.client.models.User;
import uk.gov.hmcts.reform.idam.client.models.UserDetails;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.commons.util.ReflectionUtils.findMethod;
import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseWorkerManageHearing.CASEWORKER_MANAGE_HEARING;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_AUTHORIZATION_TOKEN;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@ExtendWith(MockitoExtension.class)
class CaseWorkerManageHearingTest {

    ManageHearings manageHearings = new ManageHearings();

    @InjectMocks
    private CaseWorkerManageHearing caseWorkerManageHearing;


    @Test
    void addCaseworkerManageHearingsEventAutoconfigureBuilderTest() {

        final ConfigBuilderImpl<CaseData, State, UserRole> configBuilder = createCaseDataConfigBuilder();
        caseWorkerManageHearing.configure(configBuilder);
        assertThat(getEventsFrom(configBuilder).values())
            .extracting(Event::getId)
            .contains(CASEWORKER_MANAGE_HEARING);
    }

    @Test
    void caseworkerManageHearingAboutToSubmitTest() {
        var caseDetails = getCaseDetails();
        final var instant = Instant.now();
        final var zoneId = ZoneId.systemDefault();
        final var expectedDate = LocalDate.ofInstant(instant, zoneId);
        var result = caseWorkerManageHearing.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getManageHearingDetails()).isNotNull();
    }

    @Test
    void checkForInvalidCheckboxSelectionForApplyingWithAlone(){
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setApplyingWith(ApplyingWith.ALONE);
        AboutToStartOrSubmitResponse<CaseData, State> response =  manageHearings.midEvent(caseDetails, caseDetails);
        assertThat(response.getErrors()).isNull();
        response = manageHearings.midEventAfterRecipientSelection(caseDetails, caseDetails);
        assertThat(response.getErrors()).isEmpty();
    }

    @Test
    void checkForInvalidCheckboxSelectionForApplyingWithNotAlone(){
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setApplyingWith(ApplyingWith.WITH_SPOUSE_OR_CIVIL_PARTNER);
        AboutToStartOrSubmitResponse<CaseData, State> response =  manageHearings.midEvent(caseDetails, caseDetails);
        assertThat(response.getErrors()).isNull();
    }

    @Test
    void checkForInvalidCheckboxSelectionForApplicantSolicitor(){
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setIsApplicantRepresentedBySolicitor(YesOrNo.YES);
        AboutToStartOrSubmitResponse<CaseData, State> response =  manageHearings.midEvent(caseDetails, caseDetails);
        assertThat(response.getErrors()).isNull();
    }

    @Test
    void checkForInvalidCheckboxSelectionForNoApplicantSolicitor(){
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setIsApplicantRepresentedBySolicitor(YesOrNo.NO);
        AboutToStartOrSubmitResponse<CaseData, State> response =  manageHearings.midEvent(caseDetails, caseDetails);
        assertThat(response.getErrors()).isNull();
    }

    @Test
    void checkForInvalidCheckboxSelectionForChildRepresentedByGuardian(){
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setIsChildRepresentedByGuardian(YesOrNo.YES);
        AboutToStartOrSubmitResponse<CaseData, State> response =  manageHearings.midEvent(caseDetails, caseDetails);
        assertThat(response.getErrors()).isNull();
    }

    @Test
    void checkForInvalidCheckboxSelectionForChildNotRepresentedByGuardian(){
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setIsChildRepresentedByGuardian(YesOrNo.NO);
        AboutToStartOrSubmitResponse<CaseData, State> response =  manageHearings.midEvent(caseDetails, caseDetails);
        assertThat(response.getErrors()).isNull();
    }

    @Test
    void checkForInvalidCheckboxSelectionForChildRepresentedBySolicitor(){
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setIsChildRepresentedBySolicitor(YesOrNo.YES);
        AboutToStartOrSubmitResponse<CaseData, State> response =  manageHearings.midEvent(caseDetails, caseDetails);
        assertThat(response.getErrors()).isNull();
    }

    @Test
    void checkForInvalidCheckboxSelectionForChildNotRepresentedBySolicitor(){
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setIsChildRepresentedBySolicitor(YesOrNo.NO);
        AboutToStartOrSubmitResponse<CaseData, State> response =  manageHearings.midEvent(caseDetails, caseDetails);
        assertThat(response.getErrors()).isNull();
    }

    @Test
    void checkForInvalidCheckboxSelectionForHasAnotherAdoptionAgencyYes(){
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setHasAnotherAdopAgencyOrLAinXui(YesOrNo.YES);
        AboutToStartOrSubmitResponse<CaseData, State> response =  manageHearings.midEvent(caseDetails, caseDetails);
        assertThat(response.getErrors()).isNull();
    }

    @Test
    void checkForInvalidCheckboxSelectionForHasAnotherAdoptionAgencyNo(){
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setHasAnotherAdopAgencyOrLAinXui(YesOrNo.NO);
        AboutToStartOrSubmitResponse<CaseData, State> response =  manageHearings.midEvent(caseDetails, caseDetails);
        assertThat(response.getErrors()).isNull();
    }

    @Test
    void checkForInvalidCheckboxSelectionForBirthMotherRespondantYes(){
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().getBirthMother().setToBeServed(YesOrNo.YES);
        AboutToStartOrSubmitResponse<CaseData, State> response =  manageHearings.midEvent(caseDetails, caseDetails);
        assertThat(response.getErrors()).isNull();
    }

    @Test
    void checkForInvalidCheckboxSelectionForBirthMotherRespondantNo(){
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().getBirthMother().setToBeServed(YesOrNo.NO);
        AboutToStartOrSubmitResponse<CaseData, State> response =  manageHearings.midEvent(caseDetails, caseDetails);
        assertThat(response.getErrors()).isNull();
        response =  manageHearings.midEventAfterRecipientSelection(caseDetails, caseDetails);
        assertThat(response.getErrors()).isEmpty();
    }

    @Test
    void checkForInvalidCheckboxSelectionForBirthMotherRepresentedBySolicitorYes(){
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setIsBirthMotherRepresentedBySolicitor(YesOrNo.YES);
        AboutToStartOrSubmitResponse<CaseData, State> response =  manageHearings.midEvent(caseDetails, caseDetails);
        assertThat(response.getErrors()).isNull();
    }

    @Test
    void checkForInvalidCheckboxSelectionForBirthMotherRepresentedBySolicitorNo(){
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setIsBirthMotherRepresentedBySolicitor(YesOrNo.NO);
        AboutToStartOrSubmitResponse<CaseData, State> response =  manageHearings.midEvent(caseDetails, caseDetails);
        assertThat(response.getErrors()).isNull();
    }

    @Test
    void checkForInvalidCheckboxSelectionForBirthFatherRespondantYes(){
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().getBirthFather().setToBeServed(YesOrNo.YES);
        AboutToStartOrSubmitResponse<CaseData, State> response =  manageHearings.midEvent(caseDetails, caseDetails);
        assertThat(response.getErrors()).isNull();
    }

    @Test
    void checkForInvalidCheckboxSelectionForBirthFatherRespondantNo(){
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().getBirthFather().setToBeServed(YesOrNo.NO);
        AboutToStartOrSubmitResponse<CaseData, State> response =  manageHearings.midEvent(caseDetails, caseDetails);
        assertThat(response.getErrors()).isNull();
        response =  manageHearings.midEventAfterRecipientSelection(caseDetails, caseDetails);
        assertThat(response.getErrors()).isEmpty();
    }

    @Test
    void checkForInvalidCheckboxSelectionForBirthFatherRepresentedBySolicitorYes(){
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setIsBirthFatherRepresentedBySolicitor(YesOrNo.YES);
        AboutToStartOrSubmitResponse<CaseData, State> response =  manageHearings.midEvent(caseDetails, caseDetails);
        assertThat(response.getErrors()).isNull();
    }

    @Test
    void checkForInvalidCheckboxSelectionForBirthFatherRepresentedBySolicitorNo(){
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setIsBirthFatherRepresentedBySolicitor(YesOrNo.NO);
        AboutToStartOrSubmitResponse<CaseData, State> response =  manageHearings.midEvent(caseDetails, caseDetails);
        assertThat(response.getErrors()).isNull();
    }

    @Test
    void checkForInvalidCheckboxSelectionForOtherParentRespondantYes(){
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().getOtherParent().setToBeServed(YesOrNo.YES);
        AboutToStartOrSubmitResponse<CaseData, State> response =  manageHearings.midEvent(caseDetails, caseDetails);
        assertThat(response.getErrors()).isNull();
    }

    @Test
    void checkForInvalidCheckboxSelectionForOtherParentRespondantNo(){
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().getOtherParent().setToBeServed(YesOrNo.NO);
        AboutToStartOrSubmitResponse<CaseData, State> response =  manageHearings.midEvent(caseDetails, caseDetails);
        assertThat(response.getErrors()).isNull();
        response =  manageHearings.midEventAfterRecipientSelection(caseDetails, caseDetails);
        assertThat(response.getErrors()).isEmpty();
    }

    @Test
    void checkForInvalidCheckboxSelectionForOtherParentRepresentedBySolicitorYes(){
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setIsOtherParentRepresentedBySolicitor(YesOrNo.YES);
        AboutToStartOrSubmitResponse<CaseData, State> response =  manageHearings.midEvent(caseDetails, caseDetails);
        assertThat(response.getErrors()).isNull();
    }

    @Test
    void checkForInvalidCheckboxSelectionForOtherParentRepresentedBySolicitorNo(){
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setIsOtherParentRepresentedBySolicitor(YesOrNo.NO);
        AboutToStartOrSubmitResponse<CaseData, State> response =  manageHearings.midEvent(caseDetails, caseDetails);
        assertThat(response.getErrors()).isNull();
    }


    private CaseDetails<CaseData, State> getCaseDetails() {
        final var details = new CaseDetails<CaseData, State>();
        final var data = caseData();
        ManageHearingDetails manageHearingDetails = new ManageHearingDetails();
        manageHearingDetails.setLengthOfHearing("2hrs 30min");
        manageHearingDetails.setMethodOfHearing(MethodOfHearing.REMOTE);
        manageHearingDetails.setCourt("test court");
        manageHearingDetails.setJudge("test judge");
        data.setManageHearingDetails(manageHearingDetails);
        details.setData(data);
        details.setId(1L);
        return details;
    }


    public static ConfigBuilderImpl<CaseData, State, UserRole> createCaseDataConfigBuilder() {
        return new ConfigBuilderImpl<>(new ResolvedCCDConfig<>(
            CaseData.class,
            State.class,
            UserRole.class,
            new HashMap<>(),
            ImmutableSet.copyOf(State.class.getEnumConstants())
        ));
    }

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

    private User getCaseworkerUser() {
        UserDetails userDetails = UserDetails
            .builder()
            .forename("testFname")
            .surname("testSname")
            .build();

        return new User(TEST_AUTHORIZATION_TOKEN, userDetails);
    }
}
