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
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.SystemUpdateAccess;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.SystemUpdateCollectionAccess;
import uk.gov.hmcts.reform.adoption.document.DocumentType;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionDocument;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.Collection;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedRadioList;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.TextArea;

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

    @CCD(label = "Applying with someone else reason",
        typeOverride = TextArea,
        access = {DefaultAccess.class}
    )
    private String otherApplicantRelation;

    @CCD(
        label = "Date child moved in",
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
    @CCD(access = {DefaultAccess.class, SystemUpdateAccess.class})
    private Children children = new Children();

    @JsonUnwrapped(prefix = "birthMother")
    @Builder.Default
    @CCD(access = {SystemUpdateAccess.class})
    private Parent birthMother = new Parent();

    @JsonUnwrapped(prefix = "birthFather")
    @Builder.Default
    @CCD(access = {SystemUpdateAccess.class})
    private Parent birthFather = new Parent();

    @JsonUnwrapped(prefix = "otherParent")
    @Builder.Default
    @CCD(access = {SystemUpdateAccess.class})
    private Parent otherParent = new Parent();

    @CCD(
        label = "Linked cases",
        typeOverride = Collection,
        typeParameterOverride = "PlacementOrder",
        access = {SystemUpdateCollectionAccess.class}
    )
    private List<ListValue<PlacementOrder>> placementOrders;

    @CCD(
        label = "Add Another Placement Order",
        access = {SystemUpdateAccess.class}
    )
    private YesOrNo addAnotherPlacementOrder;

    @CCD(label = "Selected Placement Order Id",
        access = {SystemUpdateAccess.class})
    String selectedPlacementOrderId;


    @JsonUnwrapped(prefix = "child")
    @Builder.Default
    @CCD(access = {DefaultAccess.class})
    private SocialWorker childSocialWorker = new SocialWorker();

    @JsonUnwrapped(prefix = "applicant")
    @Builder.Default
    @CCD(access = {DefaultAccess.class})
    private SocialWorker applicantSocialWorker = new SocialWorker();

    @JsonUnwrapped
    @Builder.Default
    @CCD(access = {DefaultAccess.class})
    private Solicitor solicitor = new Solicitor();

    @JsonUnwrapped
    @Builder.Default
    @CCD(access = {DefaultAccess.class})
    private LocalAuthority localAuthority = new LocalAuthority();

    @JsonUnwrapped
    @Builder.Default
    @CCD(access = {DefaultAccess.class})
    private AdoptionAgencyOrLocalAuthority adopAgencyOrLA = new AdoptionAgencyOrLocalAuthority();

    @CCD(
        label = "Sibling cases",
        typeOverride = Collection,
        typeParameterOverride = "Sibling",
        access = {SystemUpdateCollectionAccess.class}
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
        access = {SystemUpdateAccess.class}
    )
    private String hasSiblings;

    @CCD(
        label = "Add Another Sibling Placement Order",
        access = {SystemUpdateAccess.class}
    )
    private YesOrNo addAnotherSiblingPlacementOrder;

    @CCD(label = "Selected Sibling ID",
        access = {SystemUpdateAccess.class}
    )
    private String selectedSiblingId;

    @CCD(label = "Selected Sibling Relation",
        access = {SystemUpdateAccess.class}
    )
    private String selectedSiblingRelation;

    @CCD(label = "Selected Sibling Po Type",
        access = {SystemUpdateAccess.class}
    )
    private String selectedSiblingPoType;

    @CCD(
        label = "hyphenatedCaseReference",
        access = {DefaultAccess.class}
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

    @CCD(label = "Messages",
        access = {DefaultAccess.class}
    )
    private String messages;

    @CCD(label = "Applicant2 statement of truth full name",
        access = {DefaultAccess.class}
    )
    private String applicant2SotFullName;


    @CCD(label = "Local authority worker statement of truth full name",
        access = {SystemUpdateAccess.class}
    )
    private String laSotFullName;

    @CCD(label = "Local authority worker job title",
        access = {SystemUpdateAccess.class}
    )
    private String laSotJobtitle;


    @CCD(label = "Local authority worker job title",
        access = {SystemUpdateAccess.class}
    )
    private String laNameSot;

    @CCD(
        label = "LA worker believes that the facts stated in this application are true.",
        access = {SystemUpdateAccess.class}
    )
    private YesOrNo laStatementOfTruth;

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

    @CCD(label = "Prospective parents social worker",
        access = {DefaultAccess.class}
    )
    private String socialWorkerDetails;

    @CCD(label = "Messages",
        access = {DefaultAccess.class}
    )
    private String message;

    @CCD(label = "Adoption Type",
        access = {DefaultAccess.class}
    )
    private String typeOfAdoption;

    @CCD(label = "Status",
        access = {DefaultAccess.class}
    )
    private State status;

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
        label = "Documents uploaded",
        typeOverride = Collection,
        typeParameterOverride = "AdoptionDocument",
        access = {DefaultAccess.class}
    )
    private List<ListValue<AdoptionDocument>> documentsUploaded;

    @CCD(
        access = {DefaultAccess.class}
    )
    private AdoptionDocument adoptionDocument;

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


    @CCD(
        label = "LA can not upload",
        access = { SystemUpdateAccess.class }
    )
    private String laCannotUpload;

    @CCD(
        label = "LA cannot upload supporting documents",
        access = { SystemUpdateAccess.class }
    )
    private Set<DocumentType> laCannotUploadSupportingDocument;


    @CCD(
        label = "Documents uploaded",
        typeOverride = Collection,
        typeParameterOverride = "AdoptionDocument",
        access = { SystemUpdateAccess.class }
    )
    private List<ListValue<AdoptionDocument>> laDocumentsUploaded;

    @CCD(
        label = "Find Family Court",
        access = {DefaultAccess.class}
    )
    private YesOrNo findFamilyCourt;

    @CCD(
        label = "Allocated court",
        access = {DefaultAccess.class}
    )
    private String placementOrderCourt;

    @CCD(
        label = "Allocated court",
        access = {DefaultAccess.class}
    )
    private String familyCourtName;

    @CCD(
        label = "Family court email",
        access = {DefaultAccess.class}
    )
    private String familyCourtEmailId;

    @CCD(
        label = "Application documents",
        access = {DefaultAccess.class}
    )
    private List<ListValue<AdoptionDocument>> applicationDocumentsCategory;

    @CCD(
        label = "Court orders",
        access = {DefaultAccess.class}
    )
    private List<ListValue<AdoptionDocument>> courtOrdersDocumentCategory;

    @CCD(
        label = "Reports",
        access = {DefaultAccess.class}
    )
    private List<ListValue<AdoptionDocument>> reportsDocumentCategory;

    @CCD(
        label = "Statements",
        access = {DefaultAccess.class}
    )
    private List<ListValue<AdoptionDocument>> statementsDocumentCategory;

    @CCD(
        label = "Correspondences",
        access = {DefaultAccess.class}
    )
    private List<ListValue<AdoptionDocument>> correspondenceDocumentCategory;

    @CCD(
        label = "Additional documents",
        access = {DefaultAccess.class}
    )
    private List<ListValue<AdoptionDocument>> additionalDocumentsCategory;

    @CCD(
        label = "Notes",
        typeOverride = Collection,
        typeParameterOverride = "CaseNote",
        access = {DefaultAccess.class}
    )
    private List<ListValue<CaseNote>> caseNote;

    @CCD(
        label = "Add a Case Note",
        access = {DefaultAccess.class}
    )
    private CaseNote note;

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

    public void sortUploadedDocuments(List<ListValue<AdoptionDocument>> previousDocuments) {
        if (isEmpty(previousDocuments)) {
            return;
        }

        Set<String> previousListValueIds = previousDocuments
            .stream()
            .map(ListValue::getId)
            .collect(Collectors.toCollection(HashSet::new));

        //Split the collection into two lists one without id's(newly added documents) and other with id's(existing documents)
        Map<Boolean, List<ListValue<AdoptionDocument>>> documentsWithoutIds =
            this.getDocumentsUploaded()
                .stream()
                .collect(Collectors.groupingBy(listValue -> !previousListValueIds.contains(listValue.getId())));

        this.setDocumentsUploaded(sortDocuments(documentsWithoutIds));
    }

    private List<ListValue<AdoptionDocument>> sortDocuments(final Map<Boolean, List<ListValue<AdoptionDocument>>> documentsWithoutIds) {

        final List<ListValue<AdoptionDocument>> sortedDocuments = new ArrayList<>();

        final var newDocuments = documentsWithoutIds.get(true);
        final var previousDocuments = documentsWithoutIds.get(false);

        if (null != newDocuments) {
            sortedDocuments.addAll(0, newDocuments); // add new documents to start of the list
            sortedDocuments.addAll(1, previousDocuments);
            sortedDocuments.forEach(
                uploadedDocumentListValue -> uploadedDocumentListValue.setId(String.valueOf(UUID.randomUUID()))
            );
            return sortedDocuments;
        }

        return previousDocuments;
    }

    @JsonIgnore
    public void addToDocumentsUploaded(final ListValue<AdoptionDocument> listValue) {

        final List<ListValue<AdoptionDocument>> documents = getDocumentsUploaded();

        if (isEmpty(documents)) {
            final List<ListValue<AdoptionDocument>> documentList = new ArrayList<>();
            documentList.add(listValue);
            setDocumentsUploaded(documentList);
        } else {
            documents.add(0, listValue); // always add to start top of list
        }
    }

}
