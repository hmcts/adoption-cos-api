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
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.ManageOrders;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.HearingNotices;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ModeOfHearing;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.commons.util.ReflectionUtils.findMethod;
import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseworkerManageOrders.CASEWORKER_MANAGE_ORDERS;
import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.ManageOrders.ERROR_CHECK_HEARINGS_SELECTION;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.HearingNotices.LIST_FOR_FURTHER_HEARINGS;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.HearingNotices.LIST_FOR_FIRST_HEARING;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.HearingNotices.HEARING_DATE_TO_BE_SPECIFIED_IN_THE_FUTURE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.ModeOfHearing.SET_MODE_OF_HEARING;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

/**
 * The type Caseworker manage orders test.
 */
@ExtendWith(MockitoExtension.class)
class CaseworkerManageOrdersTest {

    private final ManageOrders page = new ManageOrders();

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
    public void shouldValidateHearingDateToBeSpecifiedInTheFutureSelectedByUser() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        Set<HearingNotices> hearingNotices = new HashSet<>();
        hearingNotices.add(HEARING_DATE_TO_BE_SPECIFIED_IN_THE_FUTURE);
        caseDetails.getData().setHearingNotices(hearingNotices);
        AboutToStartOrSubmitResponse<CaseData, State> response = page.midEvent(caseDetails, caseDetails);
        assertThat(response.getErrors()).isEmpty();
    }

    @Test
    public void shouldValidateValuesSelectedByUser() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        Set<HearingNotices> hearingNotices = new HashSet<>();
        hearingNotices.add(LIST_FOR_FIRST_HEARING);
        hearingNotices.add(LIST_FOR_FURTHER_HEARINGS);
        caseDetails.getData().setHearingNotices(hearingNotices);

        Set<ModeOfHearing> modeOfHearings = new HashSet<>();
        modeOfHearings.add(SET_MODE_OF_HEARING);
        caseDetails.getData().setModeOfHearing(modeOfHearings);

        AboutToStartOrSubmitResponse<CaseData, State> response = page.midEvent(caseDetails, caseDetails);
        assertThat(response.getErrors()).isEmpty();
    }

    @Test
    public void shouldReturnErrorsIfHearingDateToBeSpecifiedInTheFutureAccompaniesWithAll() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        Set<HearingNotices> hearingNotices = new HashSet<>();
        hearingNotices.add(LIST_FOR_FIRST_HEARING);
        hearingNotices.add(LIST_FOR_FURTHER_HEARINGS);
        hearingNotices.add(HEARING_DATE_TO_BE_SPECIFIED_IN_THE_FUTURE);
        caseDetails.getData().setHearingNotices(hearingNotices);

        Set<ModeOfHearing> modeOfHearings = new HashSet<>();
        modeOfHearings.add(SET_MODE_OF_HEARING);
        caseDetails.getData().setModeOfHearing(modeOfHearings);

        AboutToStartOrSubmitResponse<CaseData, State> response = page.midEvent(caseDetails, caseDetails);
        assertThat(response.getErrors())
            .contains(ERROR_CHECK_HEARINGS_SELECTION);
    }

    @Test
    public void shouldReturnErrorsIfHearingDateToBeSpecifiedInTheFutureAccompaniesWithModeOfHearing() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        Set<HearingNotices> hearingNotices = new HashSet<>();
        hearingNotices.add(HEARING_DATE_TO_BE_SPECIFIED_IN_THE_FUTURE);
        caseDetails.getData().setHearingNotices(hearingNotices);

        Set<ModeOfHearing> modeOfHearings = new HashSet<>();
        modeOfHearings.add(SET_MODE_OF_HEARING);
        caseDetails.getData().setModeOfHearing(modeOfHearings);

        AboutToStartOrSubmitResponse<CaseData, State> response = page.midEvent(caseDetails, caseDetails);
        assertThat(response.getErrors())
            .contains(ERROR_CHECK_HEARINGS_SELECTION);
    }

    @Test
    public void shouldReturnErrorsIfHearingDateToBeSpecifiedInTheFutureAccompaniesWithHearingNoticesOptions() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        Set<HearingNotices> hearingNotices = new HashSet<>();
        hearingNotices.add(LIST_FOR_FIRST_HEARING);
        hearingNotices.add(LIST_FOR_FURTHER_HEARINGS);
        hearingNotices.add(HEARING_DATE_TO_BE_SPECIFIED_IN_THE_FUTURE);
        caseDetails.getData().setHearingNotices(hearingNotices);

        AboutToStartOrSubmitResponse<CaseData, State> response = page.midEvent(caseDetails, caseDetails);
        assertThat(response.getErrors())
            .contains(ERROR_CHECK_HEARINGS_SELECTION);
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
