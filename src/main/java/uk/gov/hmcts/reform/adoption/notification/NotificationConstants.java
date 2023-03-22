package uk.gov.hmcts.reform.adoption.notification;

public final class NotificationConstants {

    public static final String APPLICANT_1_FULL_NAME = "applicant1FullName";
    public static final String HAS_SECOND_APPLICANT = "hasSecondApplicant";
    public static final String HAS_MULTIPLE_APPLICANT = "hasMultipleApplicants";
    public static final String APPLICANT_2_FULL_NAME = "applicant2FullName";
    public static final String LOCAL_COURT_NAME = "localCourtName";

    public static final String CHILD_FULL_NAME = "childFullName";

    public static final String LA_PORTAL_URL = "laPortalURL";

    public static final String ADOPTION_CUI_URL = "signInAdoptionUrl";

    public static final String ADOPTION_CUI_MULTI_CHILDREN_URL = "signInAdoptionUrlForMultipleChildren";

    public static final String DRAFT_LOCAL_COURT_EMAIL_SENDGRID_SUBJECT_LINE1 = "Draft adoption application Court case ref: ";

    public static final String LOCAL_COURT_EMAIL_SENDGRID_SUBJECT_LINE1 = "Adoption application Court case ref: ";

    public static final String LOCAL_COURT_EMAIL_SENDGRID_SUBJECT_LINE2 = " Child's name: ";

    public static final String LOCAL_COURT_EMAIL_SENDGRID_CONTENT_TYPE = "text/html";

    public static final String LOCAL_COURT_EMAIL_SENDGRID_CONTENT_BODY = "From,<br>"
        + "<br>"
        + "The Adoption Service<br>"
        + "HM Courts and Tribunals Service<br>"
        + "Telephone: 01634 887900<br>"
        + "Monday to Friday 9am to 5pm<br>"
        + "Email: <a href = \"mailto: adoptionproject@justice.gov.uk\">adoptionproject@justice.gov.uk</a>  <br>"
        + "This is an automated message. Please don’t reply to this email.";

    public static final String LOCAL_COURT_EMAIL_SENDGRID_ENDPOINT = "mail/send";

    public static final String LOCAL_COURT_EMAIL_SENDGRID_DISPOSITION_ATTACHMENT = "attachment";

    public static final String LOCAL_COURT_EMAIL_SENDGRID_ATTACHMENT_MIME_TYPE = "application/pdf";


    private NotificationConstants() {
    }
}
