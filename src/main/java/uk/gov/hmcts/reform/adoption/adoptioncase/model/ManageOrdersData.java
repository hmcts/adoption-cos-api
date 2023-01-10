package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedRadioList;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.MultiSelectList;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.TextArea;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManageOrdersData {

    @CCD(showCondition = "submittedDateManageOrder=\"never\"")
    private String orderId;

    @CCD(access = {DefaultAccess.class})
    private LocalDateTime submittedDateManageOrder;

    @CCD(showCondition = "submittedDateManageOrder=\"never\"")
    private OrderStatus orderStatus;

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

    @CCD(hint = "Copy and paste or type directly into the text box",
        typeOverride = TextArea,
        access = {DefaultAccess.class})
    private String preambleDetails;

    @CCD(hint = "Choose which judge to allocate to",
        access = {DefaultAccess.class},
        typeOverride = FixedRadioList,
        typeParameterOverride = "AllocationJudge")
    private AllocationJudge allocationJudge;

    @CCD(
        access = {DefaultAccess.class}
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOrderMade;

    @CCD(hint = "You can choose one or 2 options here",
        access = {DefaultAccess.class},
        typeOverride = MultiSelectList,
        typeParameterOverride = "HearingNotices")
    private Set<HearingNotices> hearingNotices;

    @CCD(hint = "Select mode of hearing to choose between in-person or remote hearings.",
        access = {DefaultAccess.class},
        typeOverride = MultiSelectList,
        typeParameterOverride = "ModeOfHearing")
    private Set<ModeOfHearing> modeOfHearing;

    @CCD(hint = "If relevant",
        access = {DefaultAccess.class},
        typeOverride = MultiSelectList,
        typeParameterOverride = "LocalAuthority")
    private Set<LocalAuthority> selectedLocalAuthority;

    @CCD(access = {DefaultAccess.class},
        typeOverride = MultiSelectList,
        typeParameterOverride = "Cafcass")
    private Set<Cafcass> cafcass;

    @CCD(hint = "You are choosing which parties are issued with a direction on whether or not they can attend the"
        + "\nhearing. You can choose more than one option.",
        access = {DefaultAccess.class},
        typeOverride = MultiSelectList,
        typeParameterOverride = "Attendance")
    private Set<Attendance> attendance;

    @CCD(hint = "If relevant",
        access = {DefaultAccess.class},
        typeOverride = MultiSelectList,
        typeParameterOverride = "LeaveToOppose")
    private Set<LeaveToOppose> leaveToOppose;

    @CCD(access = {DefaultAccess.class},
        typeOverride = MultiSelectList,
        typeParameterOverride = "CostOrders")
    private Set<CostOrders> costOrders;

    @CCD(hint = "Enter the full name and title of the judge as it would appear on the order",
        access = {DefaultAccess.class}
    )
    private String nameOfJudge;

    @CCD(access = {DefaultAccess.class})
    private LocalDateTime dateAndTimeFirstHearing;

    @CCD(access = {DefaultAccess.class})
    private LocalDateTime dateAndTimeFurtherHearing;

    @CCD(hint = "Insert the length of the hearing in hours and minutes, for example 2 hours 30 minutes")
    private String lengthOfHearingFirstHearing;

    @CCD(hint = "Insert the length of the hearing in hours and minutes, for example 2 hours 30 minutes")
    private String lengthOfHearingFurtherHearing;

    @CCD(access = {DefaultAccess.class})
    private String listingTypeFurtherHearing;

    @CCD(access = {DefaultAccess.class})
    private String listingTypeHearingInFutureDate;

    @CCD(access = {DefaultAccess.class})
    private String nameOfCourtFirstHearing;

    @CCD(access = {DefaultAccess.class})
    private String nameOfCourtFurtherHearing;

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
        typeParameterOverride = "ReportingOfficer")
    private ReportingOfficer reportingOfficer;

    @CCD(access = {DefaultAccess.class},
        typeOverride = FixedRadioList,
        typeParameterOverride = "ReportingOfficer")
    private ReportingOfficer childrensGuardian;

    @CCD(access = {DefaultAccess.class})
    private LocalDateTime dateAndTimeRO;

    @CCD(access = {DefaultAccess.class})
    private String caseNumberCG;

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

    @CCD(hint = "Choose all that are relevant.",
        access = {DefaultAccess.class},
        typeOverride = MultiSelectList,
        typeParameterOverride = "BirthParentAttendance")
    private Set<BirthParentAttendance> birthParentAttendance;

    @CCD(access = {DefaultAccess.class})
    private List<ListValue<ManageOrdersDataAdditionalParagraph>> additionalPara;

    @CCD(hint = "Enter the name of the person issuing this order",
        access = {DefaultAccess.class})
    private String orderedBy;

    @CCD(
        typeOverride = MultiSelectList,
        hint = "You can select more than one person. It is important that recipients are "
            + "checked carefully to make sure this order is not served to the wrong person.",
        typeParameterOverride = "Recipients"
    )
    private Set<Recipients> recipientsList;

    @Getter
    @AllArgsConstructor
    public enum ManageOrderActivity implements HasLabel {

        @JsonProperty("createOrder")
        CREATE_ORDER("Create new order"),

        @JsonProperty("editOrder")
        EDIT_ORDER("Edit draft order");

        private final String label;

    }

    @Getter
    @AllArgsConstructor
    public enum ManageOrderType implements HasLabel {
        @JsonProperty("caseManagementOrder")
        CASE_MANAGEMENT_ORDER("Case management (gatekeeping) order"),

        @JsonProperty("generalDirectionsOrder")
        GENERAL_DIRECTIONS_ORDER("General directions order"),

        @JsonProperty("finalAdoptionOrder")
        FINAL_ADOPTION_ORDER("Final adoption order");

        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum AllocationJudge implements HasLabel {

        @JsonProperty("allocatePreviousProceedingsJudge")
        ALLOCATE_PREV_PROCEEDINGS_JUDGE("Allocate previous proceedings judge"),

        @JsonProperty("reallocateJudge")
        REALLOCATE_JUDGE("Reallocate judge");

        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum HearingNotices implements HasLabel {

        @JsonProperty("listForFirstHearing")
        LIST_FOR_FIRST_HEARING("List for first hearing"),

        @JsonProperty("listForFurtherHearings")
        LIST_FOR_FURTHER_HEARINGS("List for further hearings"),

        @JsonProperty("hearingDateToBeSpecifiedInTheFuture")
        HEARING_DATE_TO_BE_SPECIFIED_IN_THE_FUTURE("Hearing date to be specified in the future");

        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum ModeOfHearing implements HasLabel {

        @JsonProperty("setModeOfHearing")
        SET_MODE_OF_HEARING("Set mode of hearing");

        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum LocalAuthority implements HasLabel {

        @JsonProperty("fileAdoptionAgencyReport")
        FILE_ADOPTION_AGENCY_REPORT("File adoption agency report");

        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum Cafcass implements HasLabel {
        @JsonProperty("reportingOfficer")
        REPORTING_OFFICER("Reporting officer"),

        @JsonProperty("childrensGuardian")
        CHILDRENS_GUARDIAN("Children's guardian");
        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum Attendance implements HasLabel {

        @JsonProperty("applicantsAttendance")
        APPLICANTS_ATTENDANCE("Applicants attendance"),

        @JsonProperty("childAttendance")
        CHILD_ATTENDANCE("Child attendance"),

        @JsonProperty("localAuthorityAttendance")
        LOCAL_AUTHORITY_ATTENDANCE("Local authority attendance"),

        @JsonProperty("birthParentsAttendance")
        BIRTH_PARENTS_ATTENDANCE("Birth parents attendance");

        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum LeaveToOppose implements HasLabel {

        @JsonProperty("leaveToOppose")
        LEAVE_TO_OPPOSE("Leave to oppose");

        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum CostOrders implements HasLabel {

        @JsonProperty("noOrderForCosts")
        NO_ORDERS_FOR_COST("No order for costs");

        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum ModeOfHearings implements HasLabel {

        @JsonProperty("remotely")
        REMOTELY("Remotely"),

        @JsonProperty("inPerson")
        IN_PERSON("In person");

        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum ReportingOfficer implements HasLabel {

        @JsonProperty("cafcass")
        REMOTELY("Cafcass"),

        @JsonProperty("cafcassCymru")
        IN_PERSON("Cafcass Cymru");

        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum ApplicantAttendance implements HasLabel {

        @JsonProperty("applicantAttendanceAttend")
        APPLICANT_ATTENDANCE_ATTEND("The Applicants are not required to attend the hearing "
                                        + "but if they wish to do so, they are requested to advise "
                                        + "the Court giving not less than 7 days’ notice so that arrangements "
                                        + "can be made to protect the confidentiality of the placement"),

        @JsonProperty("applicantAttendanceNotAttend")
        APPLICANT_ATTENDANCE_NOT_ATTEND("The applicants are NOT to attend the next hearing");

        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum ChildAttendance implements HasLabel {

        @JsonProperty("childAttendanceExcused")
        CHILD_ATTENDANCE_EXCUSED("The attendance of the child is excused for all hearings."),

        @JsonProperty("childAttendanceNotBeBrought")
        CHILD_ATTENDANCE_NOT_BE_BROUGHT("The child will not be brought to this hearing.");

        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum LaAttendance implements HasLabel {

        @JsonProperty("laAttendanceAttend")
        LA_ATTENDANCE_ATTEND("A representative of the Local Authority shall attend the next hearing.");

        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum BirthParentAttendance implements HasLabel {

        @JsonProperty("birthParentAttendanceAttend")
        BIRTH_PARENT_ATTENDANCE_ATTEND("1. The birth parents are required to attend the next hearing "
                                           + "IF they wish to make any representations as to why the court should not "
                                           + "make an adoption order in respect of the child."),

        @JsonProperty("birthParentAttendanceCourtNotice")
        BIRTH_PARENT_ATTENDANCE_COURT_NOTICE("2. The court will serve notice of the hearing listed above "
                                                 + "on the birth mother and birth father at their last known address(es) "
                                                 + "specified in the application/covering letter."),

        @JsonProperty("birthParentAttendanceCourtArrange")
        BIRTH_PARENT_ATTENDANCE_COURT_ARRANGE("3. the court shall arrange personal service of notice of the "
                                                  + "hearing/this order on the respondent parents."),

        @JsonProperty("birthParentAttendanceLaArrange")
        BIRTH_PARENT_ATTENDANCE_LA_ARRANGE("4. the local authority shall arrange personal service of notice of the "
                                               + "hearing/this order on the respondent parents."),

        @JsonProperty("birthParentAttendanceReceiptOfOrder")
        BIRTH_PARENT_ATTENDANCE_RECEIPT_OF_ORDER("5. Upon receipt of this order the birth mother and father must "
                                                     + "complete the acknowledgement quoting the case number to (PO BOX ADDRESS) "
                                                     + "or speak to the social worker to confirm receipt of this order. "
                                                     + "Failing that the court may order that they are personally served with "
                                                     + "this order."),

        @JsonProperty("birthParentAttendanceOnBehalf")
        BIRTH_PARENT_ATTENDANCE_ON_BEHALF("6. The purpose of the first hearing listed above is to consider any "
                                              + "representations made by or on behalf of the birth parents and to give "
                                              + "any consequential directions or orders."),

        @JsonProperty("birthParentAttendanceNotAttend")
        BIRTH_PARENT_ATTENDANCE_NOT_ATTEND("7.  In the event that the birth parents do not attend or otherwise make "
                                               + "representations as to why an order should not be made the court shall: "
                                               + "                                        "
                                               + "a. determine whether it is satisfied that the birth parents were each served with a "
                                               + "copy of this order; and, if so"
                                               + "                                        "
                                               + "b. consider whether a final order "
                                               + "should be made at that hearing."),

        @JsonProperty("birthParentAttendanceOrderMade")
        BIRTH_PARENT_ATTENDANCE_ORDER_MADE("8. If  the court considers that a Final Order should be made the  First Hearing "
                                               + "shall be treated as a Final Hearing and an Adoption Order shall be made.");
        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum Recipients implements HasLabel {

        @JsonProperty("birthMother")
        BIRTH_MOTHER("Birth mother"),

        @JsonProperty("birthFather")
        BIRTH_FATHER("Birth father"),

        @JsonProperty("applicants")
        APPLICANTS("Applicants"),

        @JsonProperty("childsLocalAuthority")
        CHILDS_LOCAL_AUTHORITY("Child's local authority"),

        @JsonProperty("applicantsLocalAuthority")
        APPLICANTS_LOCAL_AUTHORITY("Applicant's local authority"),

        @JsonProperty("otherAdoptionAgency")
        OTHER_ADOPTION_AGENCY("Other adoption agency"),

        @JsonProperty("otherPersonWithParentalResponsibility")
        OTHER_PERSON_WITH_PARENTAL_RESPONSIBILITY("Other person with parental responsibility"),

        @JsonProperty("cafcass")
        CAFCASS("Cafcass");

        private final String label;
    }
}
