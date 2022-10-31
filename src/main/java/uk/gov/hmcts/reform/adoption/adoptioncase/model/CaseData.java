package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.CollectionAccess;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.SystemUpdateAccess;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.SystemUpdateCollectionAccess;
import uk.gov.hmcts.reform.adoption.document.DocumentType;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionDocument;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionUploadDocument;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedSet;
import java.util.UUID;

import static org.springframework.util.CollectionUtils.isEmpty;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.Collection;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.DynamicRadioList;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedRadioList;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.TextArea;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.MultiSelectList;
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

    @JsonUnwrapped(prefix = "childSolicitor")
    @Builder.Default
    @CCD(access = {DefaultAccess.class})
    private Solicitor childSolicitor = new Solicitor();

    @CCD(
        label = "Is the birth mother represented by a solicitor?",
        access = {SystemUpdateAccess.class}
    )
    private YesOrNo isBirthMotherRepresentedBySolicitor;

    @JsonUnwrapped(prefix = "birthMother")
    @Builder.Default
    @CCD(access = {SystemUpdateAccess.class})
    private Parent birthMother = new Parent();

    @JsonUnwrapped(prefix = "motherSolicitor")
    @Builder.Default
    @CCD(access = {DefaultAccess.class})
    private Solicitor birthMotherSolicitor = new Solicitor();

    @JsonUnwrapped(prefix = "birthFather")
    @Builder.Default
    @CCD(access = {SystemUpdateAccess.class})
    private Parent birthFather = new Parent();

    @CCD(
        label = "Is the birth father represented by a solicitor?",
        access = {SystemUpdateAccess.class}
    )
    private YesOrNo isBirthFatherRepresentedBySolicitor;

    @JsonUnwrapped(prefix = "fatherSolicitor")
    @Builder.Default
    @CCD(access = {DefaultAccess.class})
    private Solicitor birthFatherSolicitor = new Solicitor();

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

    @JsonUnwrapped(prefix = "otherParentSolicitor")
    @Builder.Default
    @CCD(access = {DefaultAccess.class,SystemUpdateAccess.class})
    private Solicitor otherParentSolicitor = new Solicitor();

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

    @JsonUnwrapped(prefix = "solicitor")
    @Builder.Default
    @CCD(access = {DefaultAccess.class})
    private Solicitor solicitor = new Solicitor();

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

    @JsonUnwrapped(prefix = "otheradopAgency")
    @Builder.Default
    @CCD(access = {DefaultAccess.class,SystemUpdateAccess.class})
    private OtherAdoptionAgencyOrLocalAuthority otherAdopAgencyOrLA = new OtherAdoptionAgencyOrLocalAuthority();

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
        label = "What do you want to do?",
        access = {DefaultAccess.class},
        typeOverride = FixedRadioList,
        typeParameterOverride = "ManageOrderActivity"
    )
    private ManageOrderActivity manageOrderActivity;

    @CCD(
        label = "Select type of order",
        access = {DefaultAccess.class},
        typeOverride = FixedRadioList,
        typeParameterOverride = "ManageOrderType"
    )
    private ManageOrderType manageOrderType;

    @CCD(label = "Copy and paste or type directly into the text box",
        typeOverride = TextArea,
        access = {DefaultAccess.class})
    private String preambleDetails;

    @CCD(
        label = "Choose which judge to allocate to",
        access = {DefaultAccess.class},
        typeOverride = FixedRadioList,
        typeParameterOverride = "AllocationJudge")
    private AllocationJudge allocationJudge;

    @CCD(label = "You can choose one or 2 options here",
        access = {DefaultAccess.class},
        typeOverride = MultiSelectList,
        typeParameterOverride = "HearingNotices")
    private Set<HearingNotices> hearingNotices;

    @CCD(label = "Select mode of hearing to choose between in-person or remote hearings.",
        access = {DefaultAccess.class},
        typeOverride = MultiSelectList,
        typeParameterOverride = "ModeOfHearing")
    private Set<ModeOfHearing> modeOfHearing;

    @CCD(label = "If relevant",
        access = {DefaultAccess.class},
        typeOverride = MultiSelectList,
        typeParameterOverride = "SelectedLocalAuthority")
    private Set<SelectedLocalAuthority> selectedLocalAuthority;

    @CCD(label = "You are choosing which parties are issued with a direction on whether or not they can attend the"
        + "\nhearing. You can choose more than one option.",
        access = {DefaultAccess.class},
        typeOverride = MultiSelectList,
        typeParameterOverride = "Attendance")
    private Set<Attendance> attendance;

    @CCD(label = "If relevant",
        access = {DefaultAccess.class},
        typeOverride = MultiSelectList,
        typeParameterOverride = "LeaveToOppose")
    private Set<LeaveToOppose> leaveToOppose;

    @CCD(access = {DefaultAccess.class},
        typeOverride = MultiSelectList,
        typeParameterOverride = "CostOrders")
    private Set<CostOrders> costOrders;

    @CCD(label = "Enter the full name and title of the judge as it would appear on the order",
        access = {DefaultAccess.class}
    )
    private String nameOfJudge;

    @CCD(
        label = "Recipients",
        access = {DefaultAccess.class}
    )
    private SortedSet<RecipientsInTheCase> recipientsInTheCase;


    @CCD(
        access = {DefaultAccess.class},
        typeOverride = FixedRadioList,
        typeParameterOverride = "ManageHearingOptions"
    )
    private ManageHearingOptions manageHearingOptions;



    @CCD(
        typeOverride = DynamicRadioList,
        label = "Select a hearing you want to vacate\n"
    )
    private DynamicList hearingsList;

    @CCD(
        label = "Enter hearing details",
        access = { SystemUpdateAccess.class,DefaultAccess.class}
    )
    private ManageHearingDetails manageHearingDetails;

    @CCD(
        label = "Reason for vacating a hearing",
        access = { SystemUpdateAccess.class,DefaultAccess.class}
    )
    private ReasonForVacatingHearing reasonForVacatingHearing;

    @CCD(
        label = "Does the hearing need to be relisted",
        access = { SystemUpdateAccess.class,DefaultAccess.class}
    )
    private YesOrNo isTheHearingNeedsRelisting;

    @CCD(
        label = "Vacated hearing",
        typeOverride = Collection,
        typeParameterOverride = "ManageHearingDetails",
        access = {DefaultAccess.class}
    )
    private List<ListValue<ManageHearingDetails>> vacatedHearings;


    @CCD(
        label = "New hearing",
        typeOverride = Collection,
        typeParameterOverride = "ManageHearingDetails",
        access = {DefaultAccess.class}
    )
    private List<ListValue<ManageHearingDetails>> newHearings;

    @CCD(access = {DefaultAccess.class})
    private LocalDateTime dateAndTimeFirstHearing;

    @CCD(access = {DefaultAccess.class})
    private LocalDateTime dateAndTimeFurtherHearing;

    @CCD(access = {DefaultAccess.class})
    private String nameOfCourtFirstHearing;

    @CCD(access = {DefaultAccess.class})
    private String nameOfCourtFurtherHearing;

    @CCD(hint = "Insert the length of the hearing in hours and minutes, for example 2 hours 30 minutes")
    private String lengthOfHearingFirstHearing;

    @CCD(hint = "Insert the length of the hearing in hours and minutes, for example 2 hours 30 minutes")
    private String lengthOfHearingFurtherHearing;

    @CCD(access = {DefaultAccess.class})
    private String listingTypeFurtherHearing;

    @CCD(access = {DefaultAccess.class})
    private String listingTypeHearingInFutureDate;

    @CCD(access = {DefaultAccess.class},
        typeOverride = FixedRadioList,
        typeParameterOverride = "ModeOfHearings")
    private ModeOfHearings modeOfHearings;

    @CCD(access = {DefaultAccess.class})
    private LocalDateTime dateAndTimeForOption1;

    @CCD(access = {DefaultAccess.class})
    private LocalDateTime timeForOption2;

    @CCD(access = {DefaultAccess.class},
        typeOverride = FixedRadioList,
        typeParameterOverride = "ApplicantAttendance")
    private ApplicantAttendance applicantAttendance;
    @CCD(access = {DefaultAccess.class},
        typeOverride = FixedRadioList,
        typeParameterOverride = "ChildAttendance")
    private ChildAttendance childAttendance;
    @CCD(access = {DefaultAccess.class},
        typeOverride = MultiSelectList,
        typeParameterOverride = "LaAttendance")
    private Set<LaAttendance> laAttendance;
    @CCD(label = "Choose all that are relevant.",
        access = {DefaultAccess.class},
        typeOverride = MultiSelectList,
        typeParameterOverride = "BirthParentAttendance")
    private Set<BirthParentAttendance> birthParentAttendance;


    public String getNameOfCourtFirstHearing() {
        if (Objects.nonNull(familyCourtName)) {
            return familyCourtName;
        }
        return nameOfCourtFirstHearing;
    }

    public String getNameOfCourtFurtherHearing() {
        if (Objects.nonNull(familyCourtName)) {
            return familyCourtName;
        }
        return nameOfCourtFurtherHearing;
    }

    public YesOrNo getIsApplicantRepresentedBySolicitor() {
        if (Objects.isNull(isApplicantRepresentedBySolicitor)) {
            return YesOrNo.NO;
        }
        return isApplicantRepresentedBySolicitor;
    }

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
    public void addToCombinedDocumentsGenerated() {
        setCombinedDocumentsGenerated(
            this.getDocumentsGenerated()
                .stream()
                .filter(listValue -> listValue.getValue().getDocumentType()
                    .equals(APPLICATION_LA_SUMMARY_EN)).collect(Collectors.toList()));
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

    @JsonIgnore
    public void archiveHearingInformation() {
        ManageHearingDetails manageHearingDetails = this.manageHearingDetails;
        manageHearingDetails.setRecipientsInTheCase(this.getRecipientsInTheCase());
        manageHearingDetails.setHearingId(UUID.randomUUID().toString());

        if (null != manageHearingDetails) {
            if (isEmpty(this.getNewHearings())) {
                List<ListValue<ManageHearingDetails>> listValues = new ArrayList<>();
                var listValue = ListValue
                    .<ManageHearingDetails>builder()
                    .id("1")
                    .value(manageHearingDetails)
                    .build();
                listValues.add(listValue);
                this.setNewHearings(listValues);

            } else {
                var listValue = ListValue
                    .<ManageHearingDetails>builder()
                    .value(manageHearingDetails)
                    .build();
                int listValueIndex = 0;
                this.getNewHearings().add(0, listValue);
                for (ListValue<ManageHearingDetails> asListValue : this.getNewHearings()) {
                    asListValue.setId(String.valueOf(listValueIndex++));
                }
            }

            this.setManageHearingDetails(null);
            this.setManageHearingOptions(null);
            this.setRecipientsInTheCase(null);
        }
    }

    @JsonIgnore
    public void updateVacatedHearings() {

        Optional<ListValue<ManageHearingDetails>> vacatedHearingDetails = newHearings.stream().filter(hearing -> StringUtils.equals(
            hearing.getValue().getHearingId(),
            hearingsList.getValue().getCode().toString()
        )).findFirst();

        if (!vacatedHearings.contains(vacatedHearingDetails.get())) {
            vacatedHearingDetails.get().getValue().setReasonForVacatingHearing(reasonForVacatingHearing);

            if (isEmpty(this.getVacatedHearings())) {
                List<ListValue<ManageHearingDetails>> listValues = new ArrayList<>();
                var listValue = ListValue
                    .<ManageHearingDetails>builder()
                    .id("1")
                    .value(vacatedHearingDetails.get().getValue())
                    .build();
                listValues.add(listValue);
                this.setVacatedHearings(listValues);
            } else {
                var listValue = ListValue
                    .<ManageHearingDetails>builder()
                    .value(vacatedHearingDetails.get().getValue())
                    .build();
                int listValueIndex = 0;
                this.getVacatedHearings().add(0, listValue);
                for (ListValue<ManageHearingDetails> asListValue : this.getNewHearings()) {
                    asListValue.setId(String.valueOf(listValueIndex++));
                }
            }
            newHearings.remove(vacatedHearingDetails.get());
        }
        this.setManageHearingOptions(null);
    }

}
