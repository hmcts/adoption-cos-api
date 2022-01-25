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
import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedRadioList;

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

    @CCD(label = "Selected Placement Order Id",
        access = {DefaultAccess.class})
    String selectedPlacementOrderId;

    @JsonUnwrapped
    @Builder.Default
    @CCD(access = {DefaultAccess.class})
    private SocialWorker socialWorker = new SocialWorker();

    @JsonUnwrapped
    @Builder.Default
    @CCD(access = {DefaultAccess.class})
    private Solicitor solicitor = new Solicitor();

    @CCD(
        label = "Adoption Agency Or LA list",
        typeOverride = Collection,
        typeParameterOverride = "AdoptionAgencyOrLocalAuthority",
        access = {CollectionAccess.class}
    )
    private List<ListValue<AdoptionAgencyOrLocalAuthority>> adopAgencyOrLAs;

    @CCD(
        label = "Siblings",
        typeOverride = Collection,
        typeParameterOverride = "Sibling",
        access = {CollectionAccess.class}
    )
    private List<ListValue<Sibling>> siblings;

    @CCD(
        label = "Payments",
        typeOverride = Collection,
        typeParameterOverride = "Payment",
        access = {CollectionAccess.class}
    )
    private List<ListValue<Payment>> payments;

    @CCD(
        label = "Has another Adoption Agency Or LA",
        access = {DefaultAccess.class}
    )
    private YesOrNo hasAnotherAdopAgencyOrLA;

    @CCD(label = "Selected Adoption Agency ID",
        access = {DefaultAccess.class}
    )
    private String selectedAdoptionAgencyId;

    @CCD(label = "Has Siblings",
        access = {DefaultAccess.class}
    )
    private String hasSiblings;

    @CCD(label = "Add Another Siblings",
        access = {DefaultAccess.class}
    )
    private String hasPoForSiblings;

    @CCD(label = "Has Placement order For Siblings Not Sure Reason",
        access = {DefaultAccess.class}
    )
    private String hasPoForSiblingsNotSureReason;

    @CCD(
        label = "Add Another Sibling Placement Order",
        access = {DefaultAccess.class}
    )
    private YesOrNo addAnotherSiblingPlacementOrder;

    @CCD(label = "Selected Sibling ID",
        access = {DefaultAccess.class}
    )
    private String selectedSiblingId;

    @CCD(label = "Selected Sibling PO ID",
        access = {DefaultAccess.class}
    )
    private String selectedSiblingPoId;

    @CCD(
        label = "hyphenatedCaseReference",
        access = {CaseworkerAccess.class}
    )
    private String hyphenatedCaseRef;

    @CCD(
        label = "Primary applicant statement of truth",
        access = {DefaultAccess.class}
    )
    private YesOrNo primaryApplicantSot;

    @CCD(
        label = "Secondary applicant statement ot truth",
        access = {DefaultAccess.class}
    )
    private YesOrNo secondaryApplicantSot;

    @CCD(label = "Primary applicant statement of truth full name",
        access = {DefaultAccess.class}
    )
    private String primaryApplicantSotFullName;

    @CCD(label = "Secondary applicant statement of truth full name",
        access = {DefaultAccess.class}
    )
    private String secondaryApplicantSotFullName;

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
