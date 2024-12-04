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
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.event.EventTest;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.AdoptionAgencyOrLocalAuthority;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Children;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Gender;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Nationality;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Parent;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.SocialWorker;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.YesNoNotSure;
import uk.gov.hmcts.reform.adoption.idam.IdamService;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseworkerAmendOtherPartiesDetails.CASEWORKER_AMEND_OTHER_PARTIES_DETAILS;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;


@ExtendWith(MockitoExtension.class)
class CaseworkerAmendOtherPartiesDetailsTest extends EventTest {


    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private Clock clock;

    @Mock
    private IdamService idamService;

    @InjectMocks
    private CaseworkerAmendOtherPartiesDetails caseworkerAmendOtherPartiesDetails;

    @Test
    void addCaseworkerAmendOtherPartiesDetailsEventAutoconfigureBuilderTest() {

        final ConfigBuilderImpl<CaseData, State, UserRole> configBuilder = createCaseDataConfigBuilder();
        caseworkerAmendOtherPartiesDetails.configure(configBuilder);
        assertThat(getEventsFrom(configBuilder).values())
            .extracting(Event::getId)
            .contains(CASEWORKER_AMEND_OTHER_PARTIES_DETAILS);
    }

    @Test
    void caseworkerAmendOtherPartiesEventTest() {
        var caseDetails = getCaseDetails();
        final var instant = Instant.now();
        final var zoneId = ZoneId.systemDefault();
        final var expectedDate = LocalDate.ofInstant(instant, zoneId);
        var result = caseworkerAmendOtherPartiesDetails.aboutToSubmit(caseDetails, caseDetails);
        assertThat(result.getData().getChildren()).isNotNull();
        assertThat(result.getData().getChildSocialWorker()).isNotNull();
        assertThat(result.getData().getApplicantSocialWorker()).isNotNull();
        boolean birthFatherRes = result.getData().getBirthFather().getStillAlive() == YesNoNotSure.YES;
        assertThat(birthFatherRes).isTrue();
        boolean birthMotherRes = result.getData().getBirthMother().getStillAlive() == YesNoNotSure.YES;
        assertThat(birthMotherRes).isTrue();
    }



    private CaseDetails<CaseData, State> getCaseDetails() {
        final var details = new CaseDetails<CaseData, State>();
        final var data = caseData();
        Children children = new Children();
        children.setFirstName("test");
        children.setLastName("lastName");
        children.setSexAtBirth(Gender.MALE);
        final var instant = Instant.now();
        final var zoneId = ZoneId.systemDefault();
        children.setDateOfBirth(LocalDate.ofInstant(instant, zoneId));
        children.setNationality(new TreeSet<Nationality>());
        data.setChildren(children);
        SocialWorker socialWorker = new SocialWorker();
        socialWorker.setSocialWorkerName("abcd");
        socialWorker.setSocialWorkerEmail("test@mailinator.com");
        data.setChildSocialWorker(socialWorker);
        Parent parent = new Parent();
        parent.setFirstName("test");
        parent.setLastName("lastName");
        parent.setDeceased(YesOrNo.NO);
        data.setBirthFather(parent);
        data.setBirthMother(parent);
        data.setApplicantSocialWorker(socialWorker);
        AdoptionAgencyOrLocalAuthority adoptionAgencyOrLocalAuthority = new AdoptionAgencyOrLocalAuthority();
        adoptionAgencyOrLocalAuthority.setAdopAgencyAddressLine1("test address line 1");
        adoptionAgencyOrLocalAuthority.setAdopAgencyTown("Test town");
        adoptionAgencyOrLocalAuthority.setAdopAgencyPostcode("ABC DEF");
        data.setAdopAgencyOrLA(adoptionAgencyOrLocalAuthority);
        details.setData(data);
        details.setId(1L);
        return details;
    }
}
