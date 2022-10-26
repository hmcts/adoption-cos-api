package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

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
