package uk.gov.hmcts.reform.adoption.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.AdditionalName;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.AdoptionAgencyOrLocalAuthority;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Applicant;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Application;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ApplyingWith;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Children;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Gender;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Nationality;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Parent;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.PlacementOrder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Sibling;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.SocialWorker;
import uk.gov.hmcts.reform.adoption.document.CaseDataDocumentService;
import uk.gov.hmcts.reform.adoption.document.DocumentType;
import uk.gov.hmcts.reform.adoption.notification.ApplicationSubmittedNotification;
import uk.gov.service.notify.NotificationClientException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.ADOPTION_APPLICATION_SUMMARY;

/**
 * Default endpoints per application.
 */
@RestController
public class RootController {

    @Autowired
    ApplicationSubmittedNotification applicantNotification;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CaseDataDocumentService caseDataDocumentService;

    /**
     * Root GET endpoint.
     *
     * <p>Azure application service has a hidden feature of making requests to root endpoint when
     * "Always On" is turned on.
     * This is the endpoint to deal with that and therefore silence the unnecessary 404s as a response code.
     *
     * @return Welcome message from the service.
     */
    @GetMapping("/testEndpoint")
    public ResponseEntity<String> test() throws NotificationClientException, IOException {
        List<ListValue<AdoptionAgencyOrLocalAuthority>> adopAgencyAndLas = new ArrayList<>();
        ListValue<AdoptionAgencyOrLocalAuthority> adoptionAgencyOrLocalAuthorityListValue = new ListValue<>();
        AdoptionAgencyOrLocalAuthority a1 = AdoptionAgencyOrLocalAuthority.builder()
            .adopAgencyOrLaContactEmail("a@b.com").adopAgencyOrLaId("1234").adopAgencyOrLaName("LA")
            .adopAgencyOrLaPhoneNumber("1234567890").adopAgencyOrLaContactName("contact")
            .build();

        adoptionAgencyOrLocalAuthorityListValue.setValue(a1);
        adopAgencyAndLas.add(adoptionAgencyOrLocalAuthorityListValue);


        List<ListValue<AdditionalName>> additionalNames = new ArrayList<>();
        ListValue<AdditionalName> addName = new ListValue<>();
        AdditionalName additionalName = AdditionalName.builder().firstNames("dummy").lastNames("name").build();
        addName.setValue(additionalName);
        additionalNames.add(addName);

        PlacementOrder po = PlacementOrder.builder()
            .placementOrderCourt("London Court").placementOrderId("1234567").placementOrderNumber("SW2190")
            .placementOrderDate(LocalDate.now()).placementOrderType("PO").build();
        PlacementOrder po1 = PlacementOrder.builder()
            .placementOrderCourt("London Courts").placementOrderId("12345sa67").placementOrderNumber("SW2190")
            .placementOrderDate(LocalDate.now()).placementOrderType("POas").build();
        List<ListValue<PlacementOrder>> placementList = new ArrayList<>();
        ListValue<PlacementOrder> placementOrderListValue = new ListValue<>();
        placementOrderListValue.setValue(po);
        ListValue<PlacementOrder> placementOrderListValue1 = new ListValue<>();
        placementOrderListValue1.setValue(po1);
        placementList.add(placementOrderListValue);
        placementList.add(placementOrderListValue1);


        Sibling sibling = Sibling.builder().siblingFirstName("Sibling").siblingLastNames("Last")
            .siblingPlacementOrders(placementList).siblingId("SBLNG").build();

        List<ListValue<Sibling>> siblingList = new ArrayList<>();
        ListValue<Sibling> siblingListValue = new ListValue<>();
        siblingListValue.setValue(sibling);
        siblingList.add(siblingListValue);
        Applicant applicant = Applicant.builder().hasOtherNames(YesOrNo.YES).address1("23").address2("2311").addressCountry("UK")
            .addressPostCode("E12WR0").addressTown("London").contactDetailsConsent(YesOrNo.YES).email("gaurav.tomar@hmcts.net")
            .emailAddress("rajatkumar.gupta@hmcts.net").firstName("Applicant").lastName("lastname").dateOfBirth(LocalDate.of(1994,02,02))
            .additionalNames(additionalNames).nationality(Collections.singleton(Nationality.IRISH)).occupation("somework")
            .phoneNumber("1234567890").build();
        CaseData caseData = CaseData.builder().applyingWith(ApplyingWith.WITH_SPOUSE_OR_CIVIL_PARTNER).adopAgencyOrLAs(adopAgencyAndLas)
            .hyphenatedCaseRef("1234").dateChildMovedIn(LocalDate.now()).hasAnotherAdopAgencyOrLA(YesOrNo.NO)
            .applicant1(applicant).applicant2(applicant).placementOrders(placementList)
            .siblings(siblingList).familyCourtName("London Court of justice").familyCourtEmailId("rajatkumar.gupta@hmcts.net")
            .otherApplicantRelation("Somebody I used to know").build();

        Parent parent = Parent.builder().nationality(Collections.singleton(
                Nationality.IRISH)).address1("23").address2("2311")
            .addressCountry("UK").addressPostCode("E12WR0").addressTown("London")
            .addressKnown(YesOrNo.YES).nameOnCertificate("Mother").firstName("Mother").lastName("ofChild")
            .occupation("Mother").stillAlive("Yes").build();
        caseData.setBirthMother(parent);
        SocialWorker socialWorker = SocialWorker.builder().socialWorkerEmail("s@b.com").socialWorkerName("SW")
            .socialWorkerTeamEmail("SWTE@g.com").socialWorkerPhoneNumber("1234567890").build();
        caseData.setSocialWorker(socialWorker);
        Children children = Children.builder().dateOfBirth(LocalDate.now()).firstName("child").lastName("chill")
            .firstNameAfterAdoption("adopted").lastNameAfterAdoption("child").nationality(Collections.singleton(
                Nationality.IRISH)).otherSexAtBirth("none").sexAtBirth(Gender.MALE).build();
        caseData.setChildren(children);
        Application application = Application.builder().dateSubmitted(LocalDateTime.now()).build();
        caseData.setApplication(application);

        @SuppressWarnings("unchecked")
        Map<String, Object> map = objectMapper.convertValue(caseData, Map.class);

        caseDataDocumentService.renderDocumentAndUpdateCaseData(caseData,
                                                                DocumentType.APPLICATION_SUMMARY,
                                                                map, 1644932103784645L,
                                                                ADOPTION_APPLICATION_SUMMARY,
                                                                LanguagePreference.ENGLISH, "temp");

        applicantNotification.sendToLocalCourt(caseData, 1644932103784645L);
        return ok("Success");
    }

    @GetMapping("/")
    public ResponseEntity<String> welcome() {
        return ok("Welcome to adoption-cos-api RootController");
    }
}
