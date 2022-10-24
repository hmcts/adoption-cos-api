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
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ManageHearingDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ManageHearingOptions;
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
        assertThat(result.getData().getManageHearingDetails()).isNull();
        assertThat(result.getData().getNewHearings()).isNotNull();
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
        data.setManageHearingOptions(ManageHearingOptions.ADD_NEW_HEARING);
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
