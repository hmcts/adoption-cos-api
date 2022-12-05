package uk.gov.hmcts.reform.adoption.adoptioncase.search;

public final class CaseFieldsConstants {

    public static final String APPLICANT_FIRST_NAME = "applicant1FirstName";
    public static final String FIRST_NAME = "FirstName";

    public static final String CCD_REFERENCE = "[CASE_REFERENCE]";
    public static final String CASE_STATE = "[STATE]";
    public static final String CHILD_FIRST_NAME = "childrenFirstName";
    public static final String CHILD_LAST_NAME = "childrenLastName";

    public static final String DATE_SUBMITTED = "dateSubmitted";

    //Default value as of now to Post-placement as per ADOP-1399
    public static final String TYPE_OF_ADOPTION = "Post-placement";

    public static final String STRING_COLON = ":";
    public static final String BLANK_SPACE = " ";

    public static final String NOT_APPLICABLE_ERROR = " is not applicable";

    public static final String CHECK_N_SEND_ORDER_DATE_FORMAT = "dd/MM/yyyy',' HH:mm:ss";
    public static final String SEND_N_REPLY_DATE_FORMAT = "dd/MM/yyyy',' HH:mm:ss";

    public static final String COMMA = ",";

    public static final String ADOPTION_AGENCY_STR = "ADOPTION_AGENCY";

    public static final String OTHER_ADOPTION_AGENCY_STR = "OTHER_ADOPTION_AGENCY";

    public static final String CHILD_SOCIAL_WORKER_STR = "CHILD_SOCIAL_WORKER";

    public static final String APPLICANT_SOCIAL_WORKER_STR = "APPLICANT_SOCIAL_WORKER";

    // required for Checkstyle
    private CaseFieldsConstants() {
    }
}
