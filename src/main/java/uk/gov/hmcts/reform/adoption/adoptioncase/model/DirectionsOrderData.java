package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;
import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.MultiSelectList;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.TextArea;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DirectionsOrderData {

    @CCD(showCondition = "submittedDateDirectionsOrder=\"never\"")
    private String orderId;

    @CCD(access = {DefaultAccess.class})
    private LocalDateTime submittedDateDirectionsOrder;

    @CCD(hint = "Copy and paste or type directly into the text box",
        typeOverride = TextArea,
        access = {DefaultAccess.class})
    private String preambleDetailsDO;

    @CCD(showCondition = "submittedDateDirectionsOrder=\"never\"")
    private OrderStatus orderStatus;

    @CCD(hint = "Enter the name of the judge issuing this order",
        access = {DefaultAccess.class})
    private String orderBy;

    @CCD(access = {DefaultAccess.class})
    private LocalDate dateOfGeneralDirectionOrderMade;

    @CCD(hint = "Copy and paste or type directly into the text box",
        typeOverride = TextArea,
        access = {DefaultAccess.class})
    private String generalDirectionOrderPreamble;

    @CCD(hint = "Make sure you number the paragraphs in the body of the order starting with 1",
        typeOverride = TextArea,
        access = {DefaultAccess.class})
    private String generalDirectionBodyOfTheOrder;

    @CCD(access = {DefaultAccess.class})
    private String generalDirectionCostOfOrder;

    @CCD(access = {DefaultAccess.class})
    private YesOrNo isThereAnyOrderTypeYouNeedToSend;


    @CCD(access = {DefaultAccess.class},
        hint = "If you select one of these orders, any text you have entered above will not be saved")
    private GeneralDirectionOrderTypes generalDirectionOrderTypes;

    @CCD(access = {DefaultAccess.class},
        typeOverride = MultiSelectList,
        typeParameterOverride = "GeneraDirectionsIncluded")
    private Set<GeneraDirectionsIncluded> generaDirectionsIncluded;

    @CCD(access = {DefaultAccess.class})
    private String generalDirectionNameOfPrison;

    @CCD(access = {DefaultAccess.class},
        typeOverride = TextArea)
    private String generalDirectionAddressOfPrison;

    @CCD(access = {DefaultAccess.class})
    private String generalDirectionNameOfThePrisoner;

    @CCD(access = {DefaultAccess.class})
    private String generalDirectionPrisonerNumber;

    @CCD(access = {DefaultAccess.class})
    private String generalDirectionHearingVenue;

    @CCD(access = {DefaultAccess.class})
    private LocalDateTime generalDirectionHearingDateTime;

    @CCD(access = {DefaultAccess.class})
    private GeneralDirectionOrderState generalDirectionOrderState;

    @CCD(access = {DefaultAccess.class},
        typeOverride = MultiSelectList,
        typeParameterOverride = "GeneralDirectionRecipients")
    private Set<GeneralDirectionRecipients> generalDirectionRecipientsList;

    @CCD(access = {DefaultAccess.class})
    private Document draftDocument;


    @Getter
    @AllArgsConstructor
    public enum GeneralDirectionRecipients implements HasLabel {

        @JsonProperty("applicant1")
        APPLICANT1("Applicant 1"),

        @JsonProperty("applicant2")
        APPLICANT2("Applicant 2"),

        @JsonProperty("birthMother")
        RESPONDENT_BIRTH_MOTHER("Birth mother"),

        @JsonProperty("birthFather")
        RESPONDENT_BIRTH_FATHER("Birth father"),

        @JsonProperty("adoptionAgency")
        ADOPTION_AGENCY("Adoption agency"),

        @JsonProperty("childsLocalAuthority")
        CHILDS_LOCAL_AUTHORITY("Child's local authority"),

        @JsonProperty("applicantsLocalAuthority")
        APPLICANTS_LOCAL_AUTHORITY("Applicant's local authority"),

        @JsonProperty("otherAdoptionAgency")
        OTHER_ADOPTION_AGENCY("Other adoption agency"),

        @JsonProperty("otherPersonWithParentalResponsibility")
        OTHER_PERSON_WITH_PARENTAL_RESPONSIBILITY("Other person with parental responsibility"),

        @JsonProperty("cafcass")
        LEGAL_GUARDIAN_CAFCASS("Cafcass");

        private final String label;
    }

}
