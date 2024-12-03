package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.Event;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.AddressUK;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.DynamicListElement;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.event.EventTest;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.OtherAdoptionAgencyOrLocalAuthority;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.document.CaseDataDocumentService;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionUploadDocument;
import uk.gov.hmcts.reform.adoption.idam.IdamService;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseworkerSeekFurtherInformation.CASEWORKER_SEEK_FURTHER_INFORMATION;
import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseworkerSeekFurtherInformation.SEEK_FURTHER_INFORMATION_HEADING;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_AUTHORIZATION_TOKEN;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@ExtendWith(MockitoExtension.class)
public class CaseWorkerSeekFurtherInformationTest extends EventTest {

    @InjectMocks
    CaseworkerSeekFurtherInformation caseworkerSeekFurtherInformation;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private Clock clock;

    @Mock
    private IdamService idamService;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    CaseDataDocumentService caseDataDocumentService;


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

    @Test
    void midEventAfterDateSelectionTest() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        DynamicList seekFurtherInformationList = new DynamicList();
        DynamicListElement seekFurtherInformation = new DynamicListElement(UUID.randomUUID(), "TEST:LABEL");
        seekFurtherInformationList.setValue(seekFurtherInformation);
        caseDetails.getData().setSeekFurtherInformationList(seekFurtherInformationList);
        AboutToStartOrSubmitResponse<CaseData, State> response = caseworkerSeekFurtherInformation
            .midEventAfterDateSelection(caseDetails, caseDetails);
        assertThat(response.getErrors()).isEmpty();
    }


    private CaseDetails<CaseData, State> getCaseDetails() {
        final var details = new CaseDetails<CaseData, State>();
        final var data = caseData();
        details.setData(data);
        details.setId(1L);
        return details;
    }
}
