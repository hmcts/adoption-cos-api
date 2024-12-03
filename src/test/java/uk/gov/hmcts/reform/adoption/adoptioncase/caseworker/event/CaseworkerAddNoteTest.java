package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.Event;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.event.EventTest;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseNote;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.idam.IdamService;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseworkerCaseNote.CASEWORKER_ADD_CASE_NOTE;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_AUTHORIZATION_TOKEN;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@ExtendWith(MockitoExtension.class)
class CaseworkerAddNoteTest extends EventTest {

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private Clock clock;

    @Mock
    private IdamService idamService;

    @InjectMocks
    private CaseworkerCaseNote caseworkerAddNote;

    @Test
    void shouldAddConfigurationToConfigBuilder() throws Exception {
        final ConfigBuilderImpl<CaseData, State, UserRole> configBuilder = createCaseDataConfigBuilder();

        caseworkerAddNote.configure(configBuilder);

        assertThat(getEventsFrom(configBuilder).values())
            .extracting(Event::getId)
            .contains(CASEWORKER_ADD_CASE_NOTE);
    }


    @Test
    void shouldSuccessfullyAddCaseNoteToCaseDataWhenThereAreNoExistingCaseNotes() {
        var caseDetails = getCaseDetails();
        final var instant = Instant.now();
        final var zoneId = ZoneId.systemDefault();
        final var expectedDate = LocalDate.ofInstant(instant, zoneId);

        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(zoneId);

        when(httpServletRequest.getHeader(AUTHORIZATION)).thenReturn(TEST_AUTHORIZATION_TOKEN);

        when(idamService.retrieveUser(TEST_AUTHORIZATION_TOKEN)).thenReturn(getCaseworkerUser());
        var result = caseworkerAddNote.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getCaseNote()).isNotNull();
    }

    @Test
    void shouldSuccessfullyAddCaseNoteToCaseDataWhenThereAreExistingCaseNotes() {
        var caseDetails = getCaseDetails();

        final var instant = Instant.now();
        final var zoneId = ZoneId.systemDefault();
        final var expectedDate = LocalDate.ofInstant(instant, zoneId);

        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(zoneId);

        when(httpServletRequest.getHeader(AUTHORIZATION)).thenReturn(TEST_AUTHORIZATION_TOKEN);

        when(idamService.retrieveUser(TEST_AUTHORIZATION_TOKEN)).thenReturn(getCaseworkerUser());

        List<ListValue<CaseNote>> listValues = new ArrayList<>();

        var listValue = ListValue
            .<CaseNote>builder()
            .id("1")
            .value(caseDetails.getData().getNote())
            .build();

        listValues.add(listValue);
        caseDetails.getData().setCaseNote(listValues);
        var result = caseworkerAddNote.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getCaseNote()).isNotNull();
    }

    private CaseDetails<CaseData, State> getCaseDetails() {
        final var details = new CaseDetails<CaseData, State>();
        final var data = caseData();
        CaseNote caseNote = new CaseNote();
        caseNote.setSubject("TEST SUBJECT");
        caseNote.setNote("TEST NOTE");
        data.setNote(caseNote);
        details.setData(data);
        details.setId(1L);

        return details;
    }
}
