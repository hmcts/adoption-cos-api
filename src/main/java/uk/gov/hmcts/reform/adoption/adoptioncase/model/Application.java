package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;
import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.ccd.sdk.type.OrderSummary;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.CaseworkerAccess;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;
import uk.gov.hmcts.reform.adoption.payment.model.Payment;
import uk.gov.hmcts.reform.adoption.payment.model.PaymentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static java.lang.Integer.parseInt;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.Collection;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.TextArea;
import static uk.gov.hmcts.ccd.sdk.type.YesOrNo.YES;
import static uk.gov.hmcts.reform.adoption.payment.model.PaymentStatus.SUCCESS;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Application {
    @CCD(ignore = true)
    private static final int SUBMISSION_RESPONSE_DAYS = 14;

    @CCD(
        label = "Has the applicant's marriage broken down irretrievably?",
        access = {DefaultAccess.class}
    )
    private YesOrNo applicant1ScreenHasMarriageBroken;

    @CCD(
        label = "Any supporting information or instructions?",
        typeOverride = TextArea,
        access = {DefaultAccess.class}
    )
    private String solUrgentCaseSupportingInformation;

    @CCD(
        label = "The applicant wants/will to apply to have the papers served to the respondent another way.",
        hint = "For example by email, text message or social media. This is a separate application with "
            + "an additional fee, which will need to be reviewed by a judge.",
        access = {DefaultAccess.class}
    )
    private YesOrNo applicant1WantsToHavePapersServedAnotherWay;

    @CCD(
        label = "I have discussed the possibility of a reconciliation with the applicant.",
        access = {DefaultAccess.class}
    )
    private YesOrNo solStatementOfReconciliationCertify;

    @CCD(
        label = "I have given the applicant the names and addresses of persons qualified to help effect a reconciliation.",
        access = {DefaultAccess.class}
    )
    private YesOrNo solStatementOfReconciliationDiscussed;

    @CCD(
        label = "This confirms what you are asking the court to do on behalf of the applicant. It’s known as ‘the prayer’.",
        access = {DefaultAccess.class}
    )
    private Set<ThePrayer> applicant1PrayerHasBeenGivenCheckbox;

    @Getter
    @AllArgsConstructor
    public enum ThePrayer implements HasLabel {

        @JsonProperty("Yes")
        I_CONFIRM("I confirm the applicant is applying to the court to:");

        private final String label;
    }

    @CCD(
        label = "The applicant believes that the facts stated in this application are true.",
        access = {DefaultAccess.class}
    )
    private YesOrNo applicant1StatementOfTruth;

    @CCD(
        label = "I am duly authorised by the applicant to sign this statement.",
        access = {DefaultAccess.class}
    )
    private YesOrNo solSignStatementOfTruth;

    @CCD(
        label = "Your name",
        access = {DefaultAccess.class}
    )
    private String solStatementOfReconciliationName;

    @CCD(
        label = "Name of your firm",
        access = {DefaultAccess.class}
    )
    private String solStatementOfReconciliationFirm;

    @CCD(
        label = "Additional comments",
        hint = "For the attention of court staff. These comments will not form part of the application",
        typeOverride = TextArea,
        access = {DefaultAccess.class}
    )
    private String statementOfReconciliationComments;

    // TODO move to OrderSummary?
    @CCD(
        label = "Solicitor application fee (in pounds)",
        access = {DefaultAccess.class}
    )
    private String solApplicationFeeInPounds;

    @CCD(
        label = "Account number",
        access = {DefaultAccess.class}
    )
    private DynamicList pbaNumbers;

    @CCD(
        label = "Fee account reference",
        hint = "This will appear on your statement to help you identify this payment",
        access = {DefaultAccess.class}
    )
    private String feeAccountReference;

    @CCD(
        label = "Here are your order details",
        access = {DefaultAccess.class}
    )
    private OrderSummary applicationFeeOrderSummary;

    @CCD(
        label = "Is the respondent's email address known?",
        access = {DefaultAccess.class}
    )
    private YesOrNo applicant1KnowsApplicant2EmailAddress;

    @CCD(
        label = "Is the respondent's home address known?",
        access = {DefaultAccess.class}
    )
    private YesOrNo applicant1KnowsApplicant2Address;

    @CCD(
        label = "Link to online application",
        access = {DefaultAccess.class}
    )
    private Document miniApplicationLink;

    @CCD(
        label = "Date submitted to HMCTS",
        access = {DefaultAccess.class}
    )
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime dateSubmitted;

    @CCD(
        label = "Date when the application was issued",
        access = {CaseworkerAccess.class}
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate issueDate;

    @CCD(
        label = "Date when the application was reissued",
        access = {CaseworkerAccess.class}
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate reissueDate;

    @CCD(
        label = "Date when the application was created",
        access = {DefaultAccess.class}
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdDate;

    @CCD(
        label = "Previous state",
        access = {CaseworkerAccess.class}
    )
    private State previousState;

    @CCD(
        label = "Payments",
        typeOverride = Collection,
        typeParameterOverride = "Payment",
        access = {DefaultAccess.class}
    )
    private List<ListValue<Payment>> applicationPayments;

    @CCD(
        label = "Notification of overdue application sent?",
        access = {DefaultAccess.class}
    )
    private YesOrNo overdueNotificationSent;

    @CCD(
        label = "Notification sent to Applicant 1 indicating they can apply for a Conditional Order",
        access = {DefaultAccess.class}
    )
    private YesOrNo applicant1NotifiedCanApplyForConditionalOrder;

    @JsonIgnore
    public boolean hasBeenPaidFor() {
        return null != applicationFeeOrderSummary
            && parseInt(applicationFeeOrderSummary.getPaymentTotal()) == getPaymentTotal();
    }

    @JsonIgnore
    public Integer getPaymentTotal() {
        return applicationPayments == null
            ? 0
            : applicationPayments
            .stream()
            .filter(p -> p.getValue().getStatus().equals(SUCCESS))
            .map(p -> p.getValue().getAmount())
            .reduce(0, Integer::sum);
    }

    @JsonIgnore
    public PaymentStatus getLastPaymentStatus() {
        return applicationPayments == null || applicationPayments.isEmpty()
            ? null
            : applicationPayments.get(applicationPayments.size() - 1).getValue().getStatus();
    }

    @JsonIgnore
    public boolean applicant1HasStatementOfTruth() {
        return YES.equals(applicant1StatementOfTruth);
    }

    @JsonIgnore
    public boolean hasSolSignStatementOfTruth() {
        return YES.equals(solSignStatementOfTruth);
    }

    @JsonIgnore
    public boolean hasStatementOfTruth() {
        return applicant1HasStatementOfTruth() || hasSolSignStatementOfTruth();
    }

    @JsonIgnore
    public LocalDate getDateOfSubmissionResponse() {
        return dateSubmitted == null ? null : dateSubmitted.plusDays(SUBMISSION_RESPONSE_DAYS).toLocalDate();
    }

    @JsonIgnore
    public boolean hasApplicant1BeenNotifiedCanApplyForConditionalOrder() {
        return YES.equals(applicant1NotifiedCanApplyForConditionalOrder);
    }

    @JsonIgnore
    public boolean hasOverdueNotificationBeenSent() {
        return YES.equals(overdueNotificationSent);
    }

    @JsonIgnore
    public boolean isSolicitorApplication() {
        return hasSolSignStatementOfTruth();
    }
}
