package uk.gov.hmcts.reform.adoption.caseworker.event;

import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.ccd.sdk.ResolvedCCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.Event;
import uk.gov.hmcts.ccd.sdk.api.HasRole;
import uk.gov.hmcts.ccd.sdk.type.AddressUK;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseworkerSeekFurtherInformation;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.OtherAdoptionAgencyOrLocalAuthority;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionUploadDocument;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.idam.client.models.User;
import uk.gov.hmcts.reform.idam.client.models.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.commons.util.ReflectionUtils.findMethod;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseworkerSeekFurtherInformation.CASEWORKER_SEEK_FURTHER_INFORMATION;
import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseworkerSeekFurtherInformation.SEEK_FURTHER_INFORMATION_HEADING;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_AUTHORIZATION_TOKEN;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@ExtendWith(MockitoExtension.class)
public class CaseWorkerSeekFurtherInformationTest {

    @InjectMocks
    CaseworkerSeekFurtherInformation caseworkerSeekFurtherInformation;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private Clock clock;

    @Mock
    private IdamService idamService;


    @Test
    void caseworkerSeekFurtherInformationEventAutoconfigureBuilderTest_Ok() {
        final ConfigBuilderImpl<CaseData, State, UserRole> configBuilder = createCaseDataConfigBuilder();
        caseworkerSeekFurtherInformation.configure(configBuilder);
        assertThat(getEventsFrom(configBuilder).values())
            .extracting(Event::getId)
            .contains(CASEWORKER_SEEK_FURTHER_INFORMATION);
    }

    @Test
    void seekFurtherInformationInitialDataTest_Ok() {
        var caseDetails = getCaseDetails();
        var result = caseworkerSeekFurtherInformation
            .seekFurtherInformationData(caseDetails);
        assertThat(result.getData().getSeekFurtherInformationList()).isNotNull();
    }

    @Test
    void seekFurtherInformationOtherAdoptionAgencyDetails_Ok() {
        var caseDetails = getCaseDetails();
        var otherAdoptionAgencyData =
            new OtherAdoptionAgencyOrLocalAuthority("Other Adoption Agency",
            "Adoption Agency", new AddressUK(),
            "07978656212","test@gov.uk");
        caseDetails.getData().setOtherAdoptionAgencyOrLA(otherAdoptionAgencyData);
        var result = caseworkerSeekFurtherInformation.seekFurtherInformationData(caseDetails);
        assertThat(result.getData().getSeekFurtherInformationList().getListItems()).hasSize(5);
    }

    @Test
    void caseworkerSeekFurtherInformationAboutToSubmitTest() {
        final var instant = Instant.now();
        final var zoneId = ZoneId.systemDefault();
        final var expectedDate = LocalDate.ofInstant(instant, zoneId);

        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(zoneId);

        when(httpServletRequest.getHeader(AUTHORIZATION)).thenReturn(TEST_AUTHORIZATION_TOKEN);

        when(idamService.retrieveUser(TEST_AUTHORIZATION_TOKEN)).thenReturn(getCaseworkerUser());

        var adoptionUploadDocument = new AdoptionUploadDocument();
        adoptionUploadDocument.setDocumentComment(SEEK_FURTHER_INFORMATION_HEADING);
        adoptionUploadDocument.setDocumentLink(null);
        adoptionUploadDocument.setDocumentDateAdded(LocalDate.now(clock));
        adoptionUploadDocument.setUploadedBy(getCaseworkerUser().getUserDetails().getFullName());
        List<ListValue<AdoptionUploadDocument>> listValues = new ArrayList<>();

        var listValue = ListValue
            .<AdoptionUploadDocument>builder()
            .id("1")
            .value(adoptionUploadDocument)
            .build();

        listValues.add(listValue);
        var caseDetails = getCaseDetails();
        caseDetails.getData().setCorrespondenceDocumentCategory(listValues);
        var result = caseworkerSeekFurtherInformation.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getCorrespondenceDocumentCategory()).isNotNull();
    }




    private CaseDetails<CaseData, State> getCaseDetails() {
        final var details = new CaseDetails<CaseData, State>();
        final var data = caseData();
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
            ImmutableSet.copyOf(State.class.getEnumConstants())));
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
