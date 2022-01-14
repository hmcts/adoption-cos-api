package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.CollectionAccess;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;
import uk.gov.hmcts.reform.adoptions.dacase.model.access.CaseworkerAccess;

import java.util.List;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.Collection;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.Email;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedRadioList;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.PhoneUK;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CaseData {
    @CCD(
        label = "Applying with",
        access = {DefaultAccess.class},
        typeOverride = FixedRadioList,
        typeParameterOverride = "ApplyingWith"
    )
    private ApplyingWith applyingWith;

    @JsonUnwrapped(prefix = "applicant1")
    @Builder.Default
    @CCD(access = {DefaultAccess.class})
    private Applicant applicant1 = new Applicant();

    @JsonUnwrapped(prefix = "applicant2")
    @Builder.Default
    @CCD(access = {DefaultAccess.class})
    private Applicant applicant2 = new Applicant();

    @JsonUnwrapped(prefix = "children")
    @Builder.Default
    @CCD(access = {DefaultAccess.class})
    private Children children = new Children();

    @JsonUnwrapped(prefix = "birthMother")
    @Builder.Default
    @CCD(access = {DefaultAccess.class})
    private Parent birthMother = new Parent();

    @JsonUnwrapped(prefix = "birthFather")
    @Builder.Default
    @CCD(access = {DefaultAccess.class})
    private Parent birthFather = new Parent();

    @JsonUnwrapped(prefix = "otherParent")
    @Builder.Default
    @CCD(access = {DefaultAccess.class})
    private Parent otherParent = new Parent();

    @CCD(
        label = "Placement orders",
        typeOverride = Collection,
        typeParameterOverride = "PlacementOrder",
        access = {CollectionAccess.class}
    )
    private List<ListValue<PlacementOrder>> placementOrders;

    @CCD(
        label = "Add Another Placement Order",
        access = {DefaultAccess.class}
    )
    private YesOrNo addAnotherPlacementOrder;

    @CCD(label = "Selected Placement Order Id")
    String selectedPlacementOrderId;

    @CCD(label = "Social Worker Name")
    private String socialWorkerName;

    @CCD(label = "Social Worker PhoneNumber",
        typeOverride = PhoneUK
    )
    private String socialWorkerPhoneNumber;

    @CCD(label = "Social Worker Email")
    private String socialWorkerEmail;

    @CCD(label = "Social Worker Team Email",
        typeOverride = Email
    )
    private String socialWorkerTeamEmail;

    @CCD(label = "Solicitor’s Firm")
    private String solicitorFirm;

    @CCD(label = "Solicitor’s Name")
    private String solicitorName;

    @CCD(
        label = "Solicitor’s Phone number",
        typeOverride = PhoneUK
    )
    private String solicitorPhoneNumber;

    @CCD(
        label = "Solicitor’s Email",
        typeOverride = Email
    )
    private String solicitorEmail;

    @CCD(
        label = "Solicitor Helping With Application",
        access = {DefaultAccess.class}
    )
    private YesOrNo solicitorHelpingWithApplication;

    @CCD(
        typeOverride = Collection,
        typeParameterOverride = "AdoptionAgencyOrLocalAuthority",
        access = {CollectionAccess.class}
    )
    private List<ListValue<AdoptionAgencyOrLocalAuthority>> adopAgencyOrLAs;

    @CCD(access = {DefaultAccess.class})
    private YesOrNo hasAnotherAdopAgencyOrLA;

    @CCD(label = "Selected Adoption Agency ID")
    String selectedAdoptionAgencyId;

    @CCD
    String hasSiblings;

    @CCD
    String hasSiblingNotSureReason;

    @CCD(
        label = "Add Another Siblings"
    )
    private String hasPoForSiblings;

    @CCD
    String hasPoForSiblingsNotSureReason;

    @CCD(
        label = "Siblings",
        typeOverride = Collection,
        typeParameterOverride = "Sibling",
        access = {CollectionAccess.class}
    )
    private List<ListValue<Sibling>> siblings;

    @CCD(
        label = "Add Another Sibling Placement Order",
        access = {DefaultAccess.class}
    )
    private YesOrNo addAnotherSiblingPlacementOrder;

    @CCD(label = "Selected Sibling ID")
    String selectedSiblingId;

    @CCD(label = "Selected Sibling PO ID")
    String selectedSiblingPoId;

    @CCD(
        label = "Payments",
        typeOverride = Collection,
        typeParameterOverride = "Payment",
        access = {CollectionAccess.class}
    )
    private List<ListValue<Payment>> payments;

    @CCD(
        label = "hyphenatedCaseReference",
        access = {CaseworkerAccess.class}
    )
    private String hyphenatedCaseRef;

    @JsonIgnore
    public String formatCaseRef(long caseId) {
        String temp = String.format("%016d", caseId);
        return String.format(
            "%4s-%4s-%4s-%4s",
            temp.substring(0, 4),
            temp.substring(4, 8),
            temp.substring(8, 12),
            temp.substring(12, 16)
        );
    }

}
