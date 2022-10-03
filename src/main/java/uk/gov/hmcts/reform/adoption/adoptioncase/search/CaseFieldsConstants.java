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

    // required for Checkstyle
    private CaseFieldsConstants() {
    }
}
