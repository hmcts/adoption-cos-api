package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.Event;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.DynamicListElement;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.event.EventTest;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ApplyingWith;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ManageHearingDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ManageHearingOptions;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MethodOfHearing;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Parent;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.RecipientsInTheCase;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.document.CaseDataDocumentService;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseWorkerManageHearing.CASEWORKER_MANAGE_HEARING;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.BLANK_SPACE;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@ExtendWith(MockitoExtension.class)
class CaseWorkerManageHearingTest extends EventTest {


    @InjectMocks
    private CaseWorkerManageHearing caseWorkerManageHearing;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private CaseDataDocumentService caseDataDocumentService;

    @Mock
    private Clock clock;

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
        assertThat(result.getData().getNewHearings()).isNotNull();
    }

    @Test
    void caseworkerManageHearingAboutToSubmitVacateHearingTest() {
        var caseDetails = getCaseDetailsForHearing();
        final var instant = Instant.now();
        final var zoneId = ZoneId.systemDefault();
        var result = caseWorkerManageHearing.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getNewHearings()).isEmpty();
        assertThat(result.getData().getVacatedHearings()).hasSize(1);
    }

    @Test
    void caseworkerManageHearingAboutToSubmitAdjournTest() {
        var caseDetails = getCaseDetailsForHearing();
        CaseData data = caseDetails.getData();
        data.getManageHearingDetails().setManageHearingOptions(ManageHearingOptions.ADJOURN_HEARING);
        var result = caseWorkerManageHearing.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getNewHearings()).isEmpty();
        assertThat(result.getData().getAdjournHearings()).hasSize(1);
    }

    @Test
    void caseworkerManageHearingAboutToSubmitAdjournTestWithRelisting() {
        var caseDetails = getCaseDetailsForHearing();
        ManageHearingDetails manageHearingDetails = new ManageHearingDetails();
        manageHearingDetails.setHearingId(UUID.randomUUID().toString());
        manageHearingDetails.setLengthOfHearing("2hrs 30min");
        manageHearingDetails.setMethodOfHearing(MethodOfHearing.REMOTE);
        manageHearingDetails.setCourt("test court");
        manageHearingDetails.setJudge("test judge");
        manageHearingDetails.setHearingDateAndTime(LocalDateTime.now());
        CaseData data = caseDetails.getData();
        data.setManageHearingDetails(manageHearingDetails);
        data.getManageHearingDetails().setManageHearingOptions(ManageHearingOptions.ADJOURN_HEARING);
        data.getManageHearingDetails().setIsTheHearingNeedsRelisting(YesOrNo.YES);
        var result = caseWorkerManageHearing.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getNewHearings()).hasSize(1);
        assertThat(result.getData().getAdjournHearings()).hasSize(1);
    }

    @Test
    void caseworkerManageHearingAboutToSubmitVacateTestWithRelisting() {
        var caseDetails = getCaseDetailsForHearing();
        ManageHearingDetails manageHearingDetails = new ManageHearingDetails();
        manageHearingDetails.setHearingId(UUID.randomUUID().toString());
        manageHearingDetails.setLengthOfHearing("2hrs 30min");
        manageHearingDetails.setMethodOfHearing(MethodOfHearing.REMOTE);
        manageHearingDetails.setCourt("test court");
        manageHearingDetails.setJudge("test judge");
        manageHearingDetails.setHearingDateAndTime(LocalDateTime.now());
        CaseData data = caseDetails.getData();
        data.setManageHearingDetails(manageHearingDetails);
        data.getManageHearingDetails().setManageHearingOptions(ManageHearingOptions.VACATE_HEARING);
        data.getManageHearingDetails().setIsTheHearingNeedsRelisting(YesOrNo.YES);
        var result = caseWorkerManageHearing.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getNewHearings()).hasSize(1);
        assertThat(result.getData().getVacatedHearings()).hasSize(1);
    }

    @Test
    void checkForInvalidCheckboxSelectionForAloneSuccess() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setApplyingWith(ApplyingWith.ALONE);
        SortedSet<RecipientsInTheCase> recipientsInTheCases = new TreeSet<>();
        recipientsInTheCases.add(RecipientsInTheCase.APPLICANT1);
        caseDetails.getData().getManageHearingDetails().setRecipientsInTheCase(recipientsInTheCases);
        final var instant = Instant.now();
        final var zoneId = ZoneId.systemDefault();
        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(zoneId);
        AboutToStartOrSubmitResponse<CaseData, State> response = caseWorkerManageHearing
            .midEventAfterRecipientSelection(caseDetails, caseDetails);
        assertThat(response.getErrors()).isEmpty();
    }

    @Test
    void checkForInvalidCheckboxSelectionForNotAloneSuccess() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setApplyingWith(ApplyingWith.WITH_SPOUSE_OR_CIVIL_PARTNER);
        SortedSet<RecipientsInTheCase> recipientsInTheCases = new TreeSet<>();
        recipientsInTheCases.add(RecipientsInTheCase.APPLICANT1);
        caseDetails.getData().getManageHearingDetails().setRecipientsInTheCase(recipientsInTheCases);
        final var instant = Instant.now();
        final var zoneId = ZoneId.systemDefault();
        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(zoneId);
        AboutToStartOrSubmitResponse<CaseData, State> response = caseWorkerManageHearing
            .midEventAfterRecipientSelection(caseDetails, caseDetails);
        assertThat(response.getErrors()).isEmpty();
    }

    @Test
    void checkForInvalidCheckboxSelectionForAnotherAdoptionAgencySuccess() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setHasAnotherAdopAgencyOrLAinXui(YesOrNo.YES);
        SortedSet<RecipientsInTheCase> recipientsInTheCases = new TreeSet<>();
        recipientsInTheCases.add(RecipientsInTheCase.APPLICANT1);
        caseDetails.getData().getManageHearingDetails().setRecipientsInTheCase(recipientsInTheCases);
        final var instant = Instant.now();
        final var zoneId = ZoneId.systemDefault();
        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(zoneId);
        AboutToStartOrSubmitResponse<CaseData, State> response = caseWorkerManageHearing
            .midEventAfterRecipientSelection(caseDetails, caseDetails);
        assertThat(response.getErrors()).isEmpty();
    }

    @Test
    void checkForInvalidCheckboxSelectionForChildRepresentedByGuardianSuccess() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setIsChildRepresentedByGuardian(YesOrNo.YES);
        SortedSet<RecipientsInTheCase> recipientsInTheCases = new TreeSet<>();
        recipientsInTheCases.add(RecipientsInTheCase.APPLICANT1);
        caseDetails.getData().getManageHearingDetails().setRecipientsInTheCase(recipientsInTheCases);
        final var instant = Instant.now();
        final var zoneId = ZoneId.systemDefault();
        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(zoneId);
        AboutToStartOrSubmitResponse<CaseData, State> response = caseWorkerManageHearing
            .midEventAfterRecipientSelection(caseDetails, caseDetails);
        assertThat(response.getErrors()).isEmpty();
    }

    @Test
    void checkForInvalidCheckboxSelectionForParentServingSuccess() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().getOtherParent().setToBeServed(YesOrNo.NO);
        caseDetails.getData().getBirthMother().setToBeServed(YesOrNo.NO);
        caseDetails.getData().getBirthFather().setToBeServed(YesOrNo.NO);
        SortedSet<RecipientsInTheCase> recipientsInTheCases = new TreeSet<>();
        recipientsInTheCases.add(RecipientsInTheCase.APPLICANT1);
        caseDetails.getData().getManageHearingDetails().setRecipientsInTheCase(recipientsInTheCases);
        Map<String, Object> templateVars = new HashMap<>();
        when(objectMapper.convertValue(caseDetails.getData(), Map.class)).thenReturn(templateVars);
        final var instant = Instant.now();
        final var zoneId = ZoneId.systemDefault();
        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(zoneId);
        AboutToStartOrSubmitResponse<CaseData, State> response = caseWorkerManageHearing
            .midEventAfterRecipientSelection(caseDetails, caseDetails);
        assertThat(response.getErrors()).isEmpty();
    }

    @Test
    void checkForInvalidCheckboxSelectionForAloneFailure() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setApplyingWith(ApplyingWith.ALONE);
        SortedSet<RecipientsInTheCase> recipientsInTheCases = new TreeSet<>();
        recipientsInTheCases.add(RecipientsInTheCase.APPLICANT2);
        caseDetails.getData().getManageHearingDetails().setRecipientsInTheCase(recipientsInTheCases);
        final var instant = Instant.now();
        final var zoneId = ZoneId.systemDefault();
        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(zoneId);
        AboutToStartOrSubmitResponse<CaseData, State> response = caseWorkerManageHearing
            .midEventAfterRecipientSelection(caseDetails, caseDetails);
        assertThat(response.getErrors()).isNotNull();
    }

    @Test
    void checkForInvalidCheckboxSelectionForBirthMotherSuccess() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setApplyingWith(ApplyingWith.ALONE);
        SortedSet<RecipientsInTheCase> recipientsInTheCases = new TreeSet<>();
        recipientsInTheCases.add(RecipientsInTheCase.RESPONDENT_BIRTH_MOTHER);
        caseDetails.getData().getManageHearingDetails().setRecipientsInTheCase(recipientsInTheCases);
        Parent birthMother = new Parent();
        birthMother.setFirstName("TEST_NAME");
        birthMother.setDeceased(YesOrNo.NO);
        caseDetails.getData().setBirthMother(birthMother);
        final var instant = Instant.now();
        final var zoneId = ZoneId.systemDefault();
        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(zoneId);
        AboutToStartOrSubmitResponse<CaseData, State> response = caseWorkerManageHearing
            .midEventAfterRecipientSelection(caseDetails, caseDetails);
        assertThat(response.getErrors()).isEmpty();
    }

    @Test
    void checkForInvalidCheckboxSelectionForBirthFatherSuccess() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        caseDetails.getData().setApplyingWith(ApplyingWith.ALONE);
        SortedSet<RecipientsInTheCase> recipientsInTheCases = new TreeSet<>();
        recipientsInTheCases.add(RecipientsInTheCase.RESPONDENT_BIRTH_FATHER);
        caseDetails.getData().getManageHearingDetails().setRecipientsInTheCase(recipientsInTheCases);
        Parent birthFather = new Parent();
        birthFather.setFirstName("TEST_NAME");
        birthFather.setDeceased(YesOrNo.NO);
        caseDetails.getData().setBirthFather(birthFather);
        final var instant = Instant.now();
        final var zoneId = ZoneId.systemDefault();
        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(zoneId);
        AboutToStartOrSubmitResponse<CaseData, State> response = caseWorkerManageHearing
            .midEventAfterRecipientSelection(caseDetails, caseDetails);
        assertThat(response.getErrors()).isEmpty();
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
        data.getManageHearingDetails().setManageHearingOptions(ManageHearingOptions.ADD_NEW_HEARING);
        details.setData(data);
        details.setId(1L);
        return details;
    }

    private CaseDetails<CaseData, State> getCaseDetailsForHearing() {
        final var details = new CaseDetails<CaseData, State>();
        final var data = caseData();
        ManageHearingDetails manageHearingDetails = new ManageHearingDetails();
        manageHearingDetails.setHearingId(UUID.randomUUID().toString());
        manageHearingDetails.setLengthOfHearing("2hrs 30min");
        manageHearingDetails.setMethodOfHearing(MethodOfHearing.REMOTE);
        manageHearingDetails.setCourt("test court");
        manageHearingDetails.setJudge("test judge");
        manageHearingDetails.setHearingDateAndTime(LocalDateTime.now());
        data.getManageHearingDetails().setManageHearingOptions(ManageHearingOptions.VACATE_HEARING);
        List<ListValue<ManageHearingDetails>> listValues = new ArrayList<>();
        var listValue = ListValue
            .<ManageHearingDetails>builder()
            .id("1")
            .value(manageHearingDetails)
            .build();
        listValues.add(listValue);
        data.setNewHearings(listValues);
        List<DynamicListElement> dynamicListElements = new ArrayList<>();
        data.getNewHearings().forEach(hearing -> {
            DynamicListElement listElement1 = DynamicListElement.builder()
                .label(String.join(BLANK_SPACE, hearing.getValue().getTypeOfHearing(),
                                   "-",
                                   hearing.getValue().getHearingDateAndTime().format(DateTimeFormatter.ofPattern(
                                       "dd MMM yyyy',' hh:mm:ss a")).replace("pm", "PM").replace("am", "PM")
                )).code(UUID.fromString(hearing.getValue().getHearingId()))
                .build();
            dynamicListElements.add(listElement1);
        });
        data.setHearingListThatCanBeVacated(DynamicList.builder().listItems(dynamicListElements).value(dynamicListElements.get(0)).build());
        data.setHearingListThatCanBeAdjourned(DynamicList.builder().listItems(dynamicListElements).value(
            dynamicListElements.get(0)).build());
        details.setData(data);
        details.setId(1L);
        return details;
    }
}
