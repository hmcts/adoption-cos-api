package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.ccd.sdk.type.OrderSummary;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.ReasonForVacatingHearing.AGREEMENT_OR_CONSENT_ORDER_MADE;

class CaseDataTest {

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void shouldSerializeAllJsonUnwrappedFieldsProperly() throws Exception {
        CaseData caseData = CaseData.builder()
            .applicant1(Applicant.builder().firstName("A1First").lastName("A1Last").build())
            .applicant2(Applicant.builder().firstName("A2First").lastName("A2Last").build())
            .children(Children.builder().firstName("CFirst").lastName("CLast").build())
            .localGuardian(Guardian.builder().name("GName").build())
            .childSolicitor(Solicitor.builder().solicitorFirm("CSsolicitorFirm").build())
            .birthMother(Parent.builder().firstName("BMFirst").lastName("BMLast").build())
            .birthMotherSolicitor(Solicitor.builder().solicitorFirm("BMSsolicitorFirm").build())
            .birthFather(Parent.builder().firstName("BFFirst").lastName("BFLast").build())
            .birthFatherSolicitor(Solicitor.builder().solicitorFirm("BFSsolicitorFirm").build())
            .otherParent(Parent.builder().firstName("OPFirst").lastName("OPLast").build())
            .otherParentSolicitor(Solicitor.builder().solicitorFirm("OPSsolicitorFirm").build())
            .childSocialWorker(SocialWorker.builder().socialWorkerName("ChildSW").build())
            .applicantSocialWorker(SocialWorker.builder().socialWorkerName("ApplicantSW").build())
            .solicitor(Solicitor.builder().solicitorFirm("SsolicitorFirm").build())
            .otherAdoptionAgencyOrLA(
                OtherAdoptionAgencyOrLocalAuthority.builder()
                    .agencyOrLaName("OtherAgency")
                    .build())
            .build();

        String json = objectMapper.writeValueAsString(caseData);

        // prefixed unwrapped fields
        assertThat(json).contains("\"applicant1FirstName\":\"A1First\"");
        assertThat(json).contains("\"applicant1LastName\":\"A1Last\"");
        assertThat(json).contains("\"applicant2FirstName\":\"A2First\"");
        assertThat(json).contains("\"applicant2LastName\":\"A2Last\"");
        assertThat(json).contains("\"childrenFirstName\":\"CFirst\"");
        assertThat(json).contains("\"childrenLastName\":\"CLast\"");

        assertThat(json).contains("\"localGuardianName\":\"GName\"");
        assertThat(json).contains("\"childSolicitorSolicitorFirm\":\"CSsolicitorFirm\"");

        assertThat(json).contains("\"birthMotherFirstName\":\"BMFirst\"");
        assertThat(json).contains("\"birthMotherLastName\":\"BMLast\"");
        assertThat(json).contains("\"motherSolicitorSolicitorFirm\":\"BMSsolicitorFirm\"");

        assertThat(json).contains("\"birthFatherFirstName\":\"BFFirst\"");
        assertThat(json).contains("\"birthFatherLastName\":\"BFLast\"");
        assertThat(json).contains("\"fatherSolicitorSolicitorFirm\":\"BFSsolicitorFirm\"");

        assertThat(json).contains("\"otherParentFirstName\":\"OPFirst\"");
        assertThat(json).contains("\"otherParentLastName\":\"OPLast\"");
        assertThat(json).contains("\"otherParentSolicitorSolicitorFirm\":\"OPSsolicitorFirm\"");

        assertThat(json).contains("\"childSocialWorkerName\":\"ChildSW\"");
        assertThat(json).contains("\"applicantSocialWorkerName\":\"ApplicantSW\"");
        assertThat(json).contains("\"solicitorSolicitorFirm\":\"SsolicitorFirm\"");

        assertThat(json).contains("\"otherAdoptionAgencyOrLaName\":\"OtherAgency\"");

        // guard: no nested object for wrapped fields
        assertThat(json).doesNotContain("\"applicant1\":{");
        assertThat(json).doesNotContain("\"applicant2\":{");
        assertThat(json).doesNotContain("\"children\":{");
        assertThat(json).doesNotContain("\"localGuardian\":{");
        assertThat(json).doesNotContain("\"childSolicitor\":{");
        assertThat(json).doesNotContain("\"birthMother\":{");
        assertThat(json).doesNotContain("\"motherSolicitor\":{");
        assertThat(json).doesNotContain("\"birthFather\":{");
        assertThat(json).doesNotContain("\"fatherSolicitor\":{");
        assertThat(json).doesNotContain("\"otherParent\":{");
        assertThat(json).doesNotContain("\"otherParentSolicitor\":{");
        assertThat(json).doesNotContain("\"childSocialWorker\":{");
        assertThat(json).doesNotContain("\"applicantSocialWorker\":{");
        assertThat(json).doesNotContain("\"solicitor\":{");
        assertThat(json).doesNotContain("\"otherAdoptionAgencyOrLA\":{");

        // unprefixed @JsonUnwrapped fields should also not appear nested
        assertThat(json).doesNotContain("\"localAuthority\":{");
        assertThat(json).doesNotContain("\"adopAgencyOrLA\":{");
        assertThat(json).doesNotContain("\"application\":{");
        assertThat(json).doesNotContain("\"manageHearingDetails\":{");
        assertThat(json).doesNotContain("\"manageOrdersData\":{");
        assertThat(json).doesNotContain("\"adoptionOrderData\":{");
        assertThat(json).doesNotContain("\"directionsOrderData\":{");
        assertThat(json).doesNotContain("\"messageSendDetails\":{");
        assertThat(json).doesNotContain("\"selectedMessage\":{");
        assertThat(json).doesNotContain("\"selectedOrder\":{");
    }

    @Test
    void shouldDeserializeJsonUnwrappedFieldsProperly() throws Exception {
        String json = """
        {
          "applicant1FirstName": "A1First",
          "applicant1LastName": "A1Last",
          "applicant2FirstName": "A2First",
          "applicant2LastName": "A2Last",
          "childrenFirstName": "CFirst",
          "childrenLastName": "CLast",

          "localGuardianName": "GName",
          "childSolicitorSolicitorFirm": "CSsolicitorFirm",

          "birthMotherFirstName": "BMFirst",
          "birthMotherLastName": "BMLast",
          "motherSolicitorSolicitorFirm": "BMSsolicitorFirm",

          "birthFatherFirstName": "BFFirst",
          "birthFatherLastName": "BFLast",
          "fatherSolicitorSolicitorFirm": "BFSsolicitorFirm",

          "otherParentFirstName": "OPFirst",
          "otherParentLastName": "OPLast",
          "otherParentSolicitorSolicitorFirm": "OPSsolicitorFirm",

          "childSocialWorkerName": "ChildSW",
          "applicantSocialWorkerName": "ApplicantSW",
          "solicitorSolicitorFirm": "SsolicitorFirm",

          "otherAdoptionAgencyOrLaName": "OtherAgency",

          "createdDate": "2026-11-10",
          "dateSubmitted": "2026-11-11",
          "applicationFeeOrderSummary": {
            "PaymentReference": "1234567890",
            "PaymentTotal": "100.00"
          },

          "nameOfApplicant2": "NestedApplicationField",
          "nameOfCourtFirstHearing": "NestedManageOrdersField",
          "reasonForVacatingHearing": "agreementConsentOrderMade"
        }
        """;

        CaseData caseData = objectMapper.readValue(json, CaseData.class);

        // prefixed unwrapped fields
        assertThat(caseData.getApplicant1()).isNotNull();
        assertThat(caseData.getApplicant1().getFirstName()).isEqualTo("A1First");
        assertThat(caseData.getApplicant1().getLastName()).isEqualTo("A1Last");

        assertThat(caseData.getApplicant2()).isNotNull();
        assertThat(caseData.getApplicant2().getFirstName()).isEqualTo("A2First");
        assertThat(caseData.getApplicant2().getLastName()).isEqualTo("A2Last");

        assertThat(caseData.getChildren()).isNotNull();
        assertThat(caseData.getChildren().getFirstName()).isEqualTo("CFirst");
        assertThat(caseData.getChildren().getLastName()).isEqualTo("CLast");

        assertThat(caseData.getLocalGuardian()).isNotNull();
        assertThat(caseData.getLocalGuardian().getName()).isEqualTo("GName");

        assertThat(caseData.getChildSolicitor()).isNotNull();
        assertThat(caseData.getChildSolicitor().getSolicitorFirm()).isEqualTo("CSsolicitorFirm");

        assertThat(caseData.getBirthMother()).isNotNull();
        assertThat(caseData.getBirthMother().getFirstName()).isEqualTo("BMFirst");
        assertThat(caseData.getBirthMother().getLastName()).isEqualTo("BMLast");

        assertThat(caseData.getBirthMotherSolicitor()).isNotNull();
        assertThat(caseData.getBirthMotherSolicitor().getSolicitorFirm()).isEqualTo("BMSsolicitorFirm");

        assertThat(caseData.getBirthFather()).isNotNull();
        assertThat(caseData.getBirthFather().getFirstName()).isEqualTo("BFFirst");
        assertThat(caseData.getBirthFather().getLastName()).isEqualTo("BFLast");

        assertThat(caseData.getBirthFatherSolicitor()).isNotNull();
        assertThat(caseData.getBirthFatherSolicitor().getSolicitorFirm()).isEqualTo("BFSsolicitorFirm");

        assertThat(caseData.getOtherParent()).isNotNull();
        assertThat(caseData.getOtherParent().getFirstName()).isEqualTo("OPFirst");
        assertThat(caseData.getOtherParent().getLastName()).isEqualTo("OPLast");

        assertThat(caseData.getOtherParentSolicitor()).isNotNull();
        assertThat(caseData.getOtherParentSolicitor().getSolicitorFirm()).isEqualTo("OPSsolicitorFirm");

        assertThat(caseData.getChildSocialWorker()).isNotNull();
        assertThat(caseData.getChildSocialWorker().getSocialWorkerName()).isEqualTo("ChildSW");

        assertThat(caseData.getApplicantSocialWorker()).isNotNull();
        assertThat(caseData.getApplicantSocialWorker().getSocialWorkerName()).isEqualTo("ApplicantSW");

        assertThat(caseData.getSolicitor()).isNotNull();
        assertThat(caseData.getSolicitor().getSolicitorFirm()).isEqualTo("SsolicitorFirm");

        assertThat(caseData.getOtherAdoptionAgencyOrLA()).isNotNull();
        assertThat(caseData.getOtherAdoptionAgencyOrLA().getAgencyOrLaName()).isEqualTo("OtherAgency");

        // unprefixed nested/unwrapped spot checks
        assertThat(caseData.getApplication()).isNotNull();
        assertThat(caseData.getApplication().getCreatedDate()).isEqualTo("2026-11-10");
        assertThat(caseData.getApplication().getDateSubmitted()).isEqualTo("2026-11-11");
        assertThat(caseData.getApplication().getApplicationFeeOrderSummary()).isEqualTo(
            OrderSummary.builder().paymentReference("1234567890").paymentTotal("100.00").build()
        );

        assertThat(caseData.getManageOrdersData()).isNotNull();
        assertThat(caseData.getManageOrdersData().getNameOfCourtFirstHearing())
            .isEqualTo("NestedManageOrdersField");

        assertThat(caseData.getManageHearingDetails()).isNotNull();
        assertThat(caseData.getManageHearingDetails().getReasonForVacatingHearing())
            .isEqualTo(AGREEMENT_OR_CONSENT_ORDER_MADE);
    }
}
