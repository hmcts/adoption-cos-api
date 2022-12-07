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

    public static final String COMMA = ",";

    public static final String ADOPTION_AGENCY_STR = "ADOPTION_AGENCY";

    public static final String OTHER_ADOPTION_AGENCY_STR = "OTHER_ADOPTION_AGENCY";

    public static final String CHILD_SOCIAL_WORKER_STR = "CHILD_SOCIAL_WORKER";

    public static final String APPLICANT_SOCIAL_WORKER_STR = "APPLICANT_SOCIAL_WORKER";

    public static final String ERROR_CHECK_RECIPIENTS_SELECTION = "Recipients of Final adoption order is required";
    public static final String FIRST_APPLICANT_NOT_APPLICABLE = "First Applicant not applicable for the case";
    public static final String SECOND_APPLICANT_NOT_APPLICABLE = "Second Applicant not applicable for the case";
    public static final String BIRTH_MOTHER_NOT_APPLICABLE = "Recipient birth mother not applicable for the case";
    public static final String BIRTH_FATHER_NOT_APPLICABLE = "Recipient birth father not applicable for the case";
    public static final String LEGAL_GUARDIAN_NOT_APPLICABLE = "Legal guardian (CAFCASS) not applicable for the case";
    public static final String CHILDS_LA_NOT_APPLICABLE = "Child local authority not applicable for the case";
    public static final String APPLICANTS_LA_NOT_APPLICABLE = "Applicants local authority not applicable for the case";
    public static final String ADOP_AGENCY_NOT_APPLICABLE = "Adoption agency not applicable for the case";
    public static final String OTHER_ADOP_AGENCY_NOT_APPLICABLE = "Other adoption agency not applicable for the case";
    public static final String OTHER_PARENT_AGENCY_NOT_APPLICABLE = "Other person with parental responsibility not applicable for the case";
    public static final String ERROR_CHECK_RECIPIENTS_GENERAL_DIRECTION_SELECTION = "Recipients of general direction  order is required";


    // required for Checkstyle
    private CaseFieldsConstants() {
    }
}
