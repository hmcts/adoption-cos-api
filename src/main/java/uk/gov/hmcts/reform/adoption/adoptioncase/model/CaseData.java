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
import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.ccd.sdk.type.WaysToPay;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.CaseworkerAccess;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.CollectionAccess;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.SystemUpdateAccess;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.SystemUpdateCollectionAccess;
import uk.gov.hmcts.reform.adoption.document.DocumentType;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionDocument;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionUploadDocument;
import uk.gov.hmcts.reform.adoption.document.model.DssUploadedDocument;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

import static org.springframework.util.CollectionUtils.isEmpty;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.Collection;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.DynamicRadioList;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedRadioList;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.TextArea;
import static uk.gov.hmcts.reform.adoption.document.DocumentType.APPLICATION_LA_SUMMARY_EN;

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
        label = "Are the applicants represented by a solicitor?",
        access = {DefaultAccess.class}
    )
    private YesOrNo isApplicantRepresentedBySolicitor;

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

    @CCD(
        label = "is Child Represented By Guardian",
        access = {SystemUpdateAccess.class}
    )
    private YesOrNo isChildRepresentedByGuardian;

    @JsonUnwrapped(prefix = "localGuardian")
    @Builder.Default
    @CCD(access = {DefaultAccess.class, SystemUpdateAccess.class})
    private Guardian localGuardian = new Guardian();

    @CCD(
        label = "Is the child represented by a solicitor?",
        access = {SystemUpdateAccess.class}
    )
    private YesOrNo isChildRepresentedBySolicitor;

    @CCD(
        label = "Is the birth mother represented by a solicitor?",
        access = {SystemUpdateAccess.class}
    )
    private YesOrNo isBirthMotherRepresentedBySolicitor;

    @JsonUnwrapped(prefix = "birthMother")
    @Builder.Default
    @CCD(access = {SystemUpdateAccess.class})
    private Parent birthMother = new Parent();

    @JsonUnwrapped(prefix = "birthFather")
    @Builder.Default
    @CCD(access = {SystemUpdateAccess.class})
    private Parent birthFather = new Parent();

    @CCD(
        label = "Is the birth father represented by a solicitor?",
        access = {SystemUpdateAccess.class}
    )
    private YesOrNo isBirthFatherRepresentedBySolicitor;

    @CCD(
        label = "Is there another person who has parental responsibility for the child?",
        access = {SystemUpdateAccess.class}
    )
    private YesOrNo isThereAnyOtherPersonWithParentalResponsibility;

    @JsonUnwrapped(prefix = "otherParent")
    @Builder.Default
    @CCD(access = {SystemUpdateAccess.class})
    private Parent otherParent = new Parent();

    @CCD(
        label = "Is the other person with parental responsibility represented by a solicitor?",
        access = {SystemUpdateAccess.class}
    )
    private YesOrNo isOtherParentRepresentedBySolicitor;

    @CCD(
        label = "Linked cases",
        typeOverride = Collection,
        typeParameterOverride = "PlacementOrder",
        access = {SystemUpdateCollectionAccess.class}
    )
    private List<ListValue<PlacementOrder>> placementOrders;

    @CCD(
        label = "Placement",
        access = {SystemUpdateAccess.class}
    )
    private PlacementOrder placementOrder;

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
    @CCD(access = {DefaultAccess.class,SystemUpdateAccess.class})
    private SocialWorker childSocialWorker = new SocialWorker();

    @JsonUnwrapped(prefix = "applicant")
    @Builder.Default
    @CCD(access = {DefaultAccess.class,SystemUpdateAccess.class})
    private SocialWorker applicantSocialWorker = new SocialWorker();

    @JsonUnwrapped
    @Builder.Default
    @CCD(access = {DefaultAccess.class,SystemUpdateAccess.class})
    private LocalAuthority localAuthority = new LocalAuthority();

    @JsonUnwrapped
    @Builder.Default
    @CCD(access = {DefaultAccess.class,SystemUpdateAccess.class})
    private AdoptionAgencyOrLocalAuthority adopAgencyOrLA = new AdoptionAgencyOrLocalAuthority();

    @CCD(
        label = "Sibling court cases",
        typeOverride = Collection,
        typeParameterOverride = "Sibling",
        access = {SystemUpdateCollectionAccess.class}
    )
    private List<ListValue<Sibling>> siblings;

    @CCD(
        label = "Has another Adoption Agency Or LA",
        access = {DefaultAccess.class,SystemUpdateAccess.class}
    )
    private YesOrNo hasAnotherAdopAgencyOrLA;

    @CCD(
        label = "Has another Adoption Agency Or LA",
        access = {DefaultAccess.class,SystemUpdateAccess.class}
    )
    private YesOrNo hasAnotherAdopAgencyOrLAinXui;

    @JsonUnwrapped(prefix = "otherAdoption")
    @Builder.Default
    @CCD(access = {DefaultAccess.class,SystemUpdateAccess.class})
    private OtherAdoptionAgencyOrLocalAuthority otherAdoptionAgencyOrLA = new OtherAdoptionAgencyOrLocalAuthority();

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
        access = {DefaultAccess.class, SystemUpdateAccess.class}
    )
    private String laSotFullName;

    @CCD(label = "Local authority worker job title",
        access = {DefaultAccess.class, SystemUpdateAccess.class}
    )
    private String laSotJobtitle;


    @CCD(label = "Local authority worker job title",
        access = {DefaultAccess.class, SystemUpdateAccess.class}
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

    @Builder.Default
    @CCD(
        label = "Payment",
        access = {DefaultAccess.class}
    )
    private Payment successfulPayment = new Payment();

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
        label = "Combined documents generated",
        typeOverride = Collection,
        typeParameterOverride = "AdoptionDocument",
        access = {CollectionAccess.class}
    )
    private List<ListValue<AdoptionDocument>> combinedDocumentsGenerated;

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
        label = "Documents pending review",
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

    @CCD(label = "Name of the judge",
        access = {DefaultAccess.class})
     private String allocatedJudge;

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
        label = "Enter court name",
        access = {DefaultAccess.class}
    )
    private String transferCourt;

    @CCD(
        label = "Family court email",
        access = {DefaultAccess.class}
    )
    private String familyCourtEmailId;

    @CCD(
        label = "Application documents",
        access = {DefaultAccess.class}
    )
    private List<ListValue<AdoptionUploadDocument>> applicationDocumentsCategory;

    @CCD(
        label = "Court orders",
        access = {DefaultAccess.class}
    )
    private List<ListValue<AdoptionUploadDocument>> courtOrdersDocumentCategory;

    @CCD(
        label = "Reports",
        access = {DefaultAccess.class}
    )
    private List<ListValue<AdoptionUploadDocument>> reportsDocumentCategory;

    @CCD(
        label = "Statements",
        access = {DefaultAccess.class}
    )
    private List<ListValue<AdoptionUploadDocument>> statementsDocumentCategory;

    @CCD(
        label = "Correspondence",
        access = {DefaultAccess.class}
    )
    private List<ListValue<AdoptionUploadDocument>> correspondenceDocumentCategory;

    @CCD(
        label = "Additional documents",
        access = {DefaultAccess.class}
    )
    private List<ListValue<AdoptionUploadDocument>> additionalDocumentsCategory;

    @CCD(
        access = {DefaultAccess.class}
    )
    private AdoptionUploadDocument adoptionUploadDocument;


    @CCD(
        label = "Role",
        hint = "What is their role? For example, first applicant or child's social worker."
    )
    private String role;

    @CCD(
        label = "Name",
        hint = "Add the name of the person who submitted the document."
    )
    private String name;

    @CCD(
        access = {DefaultAccess.class, CaseworkerAccess.class},
        label = "Ways To Pay"
    )
    private WaysToPay waysToPay;

    @CCD(
        typeOverride = DynamicRadioList,
        label = "Select a hearing you want to vacate\n"
    )
    private DynamicList hearingListThatCanBeVacated;

    @CCD(
        typeOverride = DynamicRadioList,
        label = "Select a hearing you want to adjourn\n"
    )
    private DynamicList hearingListThatCanBeAdjourned;

    @CCD(
        typeOverride = DynamicRadioList,
        label = "Who do you need to contact\n",
        typeParameterOverride = "SeekFurtherInformationList"
    )
    private DynamicList seekFurtherInformationList;

    @CCD(access = {DefaultAccess.class},
        label = "List the documents you need",
        typeOverride = TextArea)
    private String askForAdditionalDocumentText;

    @CCD(access = {DefaultAccess.class},
        label = "List the questions you want to ask",
        typeOverride = TextArea)
    private String askAQuestionText;

    @CCD(label = "When is the information needed by?",
        access = {SystemUpdateAccess.class,
            DefaultAccess.class}
    )
    private LocalDateTime seekInformationNeededDate;

    @CCD
    private Document seekFurtherInformationDocument;

    @CCD(
        access = {DefaultAccess.class},
        label = "Select the order you want to review"
    )
    private DynamicList checkAndSendOrderDropdownList;

    // ------------------- Send And Reply Messages Objects Start ----------------- //

    @CCD(
        label = "Reply a message\n"
    )
    private DynamicList replyMsgDynamicList;


    @CCD(access = {DefaultAccess.class})
    private String loggedInUserRole;

    @CCD(
        label = "Do you want to attach documents from this case?",
        access = {SystemUpdateAccess.class, DefaultAccess.class}
    )
    private YesOrNo sendMessageAttachDocument;

    @CCD(
        access = {DefaultAccess.class},
        label = "Select a document"
    )
    private DynamicList attachDocumentList;

    // ------------------- Send And Reply Messages Objects End ----------------- //


    @CCD(
        access = { DefaultAccess.class}
    )
    private String dssQuestion1;

    @CCD(
        access = { DefaultAccess.class}
    )
    private String dssAnswer1;

    @CCD(
        access = { DefaultAccess.class}
    )
    private String dssQuestion2;

    @CCD(
        access = {DefaultAccess.class}
    )
    private String dssAnswer2;

    @CCD(
        access = {DefaultAccess.class}
    )
    private String dssQuestion3;

    @CCD(
        access = { DefaultAccess.class}
    )
    private String dssAnswer3;


    @CCD(
        label = "Uploaded DSS Documents",
        typeOverride = Collection,
        typeParameterOverride = "DssUploadedDocument",
        access = {CaseworkerAccess.class}
    )
    private List<ListValue<DssUploadedDocument>> uploadedDssDocuments;

    @CCD(
            access = { DefaultAccess.class}
    )
    private String dssHeaderDetails;

    private String seekFurtherInformationDocumentSubmitterName;

    private YesOrNo seekFurtherInformationAdopOrLaSelected;

    /*public DynamicList getPlacementOfTheChildList() {
        if (adopAgencyOrLA.getAdopAgencyOrLaName() != null
            && childSocialWorker.getSocialWorkerName() != null
            && applicantSocialWorker.getSocialWorkerName() != null) {
            return this.getAdoptionOrderData().getPlacementOfTheChildList(
                        this.getAdopAgencyOrLA(),
                        this.getHasAnotherAdopAgencyOrLAinXui(),
                        this.getOtherAdoptionAgencyOrLA(),
                        this.getChildSocialWorker(),
                        this.getApplicantSocialWorker());
        }

        return this.getAdoptionOrderData().getPlacementOfTheChildList();
    }*/

    public YesOrNo getIsApplicantRepresentedBySolicitor() {
        if (Objects.isNull(isApplicantRepresentedBySolicitor)) {
            return YesOrNo.NO;
        }
        return isApplicantRepresentedBySolicitor;
    }

    @JsonIgnore
    public void addToCombinedDocumentsGenerated() {
        setCombinedDocumentsGenerated(
            this.getDocumentsGenerated()
                .stream()
                .filter(listValue -> listValue.getValue().getDocumentType()
                    .equals(APPLICATION_LA_SUMMARY_EN)).collect(Collectors.toList()));
        /*this.getCombinedDocumentsGenerated().stream()
            .forEach(listValue -> {
                listValue.getValue().getDocumentType()
                    .equals(APPLICATION_LA_SUMMARY_EN);
                this.getLaDocumentsUploaded().add(listValue);
            });*/
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
        addToCombinedDocumentsGenerated();
    }

}
