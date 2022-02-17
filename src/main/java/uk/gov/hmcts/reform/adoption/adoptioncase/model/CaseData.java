package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.CaseworkerAccess;
import uk.gov.hmcts.reform.adoption.document.DocumentType;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionDocument;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;
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

    @CCD(
        label = "Child moved in date",
        access = {DefaultAccess.class}
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateChildMovedIn;

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
        label = "The applicant1 believes that the facts stated in this application are true.",
        access = {DefaultAccess.class}
    )
    private YesOrNo applicant1StatementOfTruth;

    @CCD(
        label = "The applicant1 believes that the facts stated in this application are true on behalf of applicant2.",
        access = {DefaultAccess.class}
    )
    private YesOrNo applicant2StatementOfTruth;

    @CCD(label = "Applicant1 statement of truth full name",
        access = {DefaultAccess.class}
    )
    private String applicant1SotFullName;

    @CCD(label = "Applicant2 statement of truth full name",
        access = {DefaultAccess.class}
    )
    private String applicant2SotFullName;

    @CCD(
        label = "Due Date",
        access = {DefaultAccess.class}
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    @JsonUnwrapped()
    @Builder.Default
    private Application application = new Application();

    @CCD(
        label = "PCQ ID",
        access = {DefaultAccess.class}
    )
    private String pcqId;

    @CCD(
        label = "Documents generated",
        typeOverride = Collection,
        typeParameterOverride = "AdoptionDocument",
        access = {CollectionAccess.class}
    )
    private List<ListValue<AdoptionDocument>> documentsGenerated;

    @CCD(
        label = "Applicant uploaded documents",
        typeOverride = Collection,
        typeParameterOverride = "AdoptionDocument",
        access = {DefaultAccess.class}
    )
    private List<ListValue<AdoptionDocument>> applicant1DocumentsUploaded;

    @CCD(
        label = "Applicant cannot upload supporting documents",
        access = {DefaultAccess.class}
    )
    private Set<DocumentType> applicant1CannotUploadSupportingDocument;

    @CCD(
        label = "Applicant can not upload",
        access = {DefaultAccess.class}
    )
    private String applicant1CannotUpload;

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

    @JsonIgnore
    public void addToDocumentsGenerated(final ListValue<AdoptionDocument> listValue) {

        final List<ListValue<AdoptionDocument>> documents = getDocumentsGenerated();

        if (isEmpty(documents)) {
            final List<ListValue<AdoptionDocument>> documentList = new ArrayList<>();
            documentList.add(listValue);
            setDocumentsGenerated(documentList);
        } else {
            documents.add(0, listValue); // always add to start top of list
        }
    }

}
