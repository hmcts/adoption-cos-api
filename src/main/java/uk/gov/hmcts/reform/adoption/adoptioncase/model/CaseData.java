package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.groovy.parser.antlr4.util.StringUtils;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.CaseLink;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.caseworker.model.CaseNote;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Applicant2Access;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.CaseworkerAccess;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.CaseworkerAndSuperUserAccess;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;

import java.time.LocalDate;
import java.util.List;

import static java.util.Objects.nonNull;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.Collection;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedList;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedRadioList;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.TextArea;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CaseData {

    @CCD(
        label = "Application type",
        access = {DefaultAccess.class},
        typeOverride = FixedList,
        typeParameterOverride = "ApplicationType"
    )
    private ApplicationType applicationType;

    @CCD(
        label = "Divorce or dissolution?",
        access = {DefaultAccess.class},
        typeOverride = FixedRadioList,
        typeParameterOverride = "DivorceOrDissolution"
    )
    private DivorceOrDissolution divorceOrDissolution;

    @JsonUnwrapped(prefix = "labelContent")
    @Builder.Default
    @CCD(access = {DefaultAccess.class})
    private LabelContent labelContent = new LabelContent();

    @JsonUnwrapped(prefix = "applicant1")
    @Builder.Default
    @CCD(access = {DefaultAccess.class})
    private Applicant applicant1 = new Applicant();

    @JsonUnwrapped(prefix = "applicant2")
    @Builder.Default
    @CCD(access = {DefaultAccess.class, Applicant2Access.class})
    private Applicant applicant2 = new Applicant();

    @JsonUnwrapped()
    @Builder.Default
    private Application application = new Application();

    @JsonUnwrapped()
    private CaseInvite caseInvite;

    @JsonUnwrapped()
    @Builder.Default
    private AcknowledgementOfService acknowledgementOfService = new AcknowledgementOfService();

    @JsonUnwrapped()
    @Builder.Default
    private FinalOrder finalOrder = new FinalOrder();

    @JsonUnwrapped
    @Builder.Default
    private GeneralOrder generalOrder = new GeneralOrder();

    @JsonUnwrapped
    @Builder.Default
    private GeneralEmail generalEmail = new GeneralEmail();

    @CCD(
        label = "RDC",
        hint = "Regional divorce unit",
        access = {DefaultAccess.class}
    )
    private Court divorceUnit;

    @CCD(
        label = "Case ID for previously Amended Case, which was challenged by the respondent",
        access = {DefaultAccess.class}
    )
    private CaseLink previousCaseId;

    @CCD(
        label = "Due Date",
        access = {DefaultAccess.class}
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    @CCD(
        label = "Notes",
        typeOverride = Collection,
        typeParameterOverride = "CaseNote",
        access = {CaseworkerAndSuperUserAccess.class}
    )
    private List<ListValue<CaseNote>> notes;

    @CCD(
        label = "Add a case note",
        hint = "Enter note",
        typeOverride = TextArea,
        access = {CaseworkerAndSuperUserAccess.class}
    )
    private String note;

    @CCD(
        label = "Bulk list case reference",
        access = {CaseworkerAccess.class}
    )
    private String bulkListCaseReference;

    @JsonIgnore
    public boolean isAmendedCase() {
        return null != previousCaseId;
    }

    @JsonIgnore
    public boolean isSoleApplicationOrApplicant2HasAgreedHwf() {
        return null != applicationType
            && applicationType.isSole()
            || null != application.getApplicant2HelpWithFees()
            && null != application.getApplicant2HelpWithFees().getNeedHelp()
            && application.getApplicant2HelpWithFees().getNeedHelp().toBoolean();
    }

    @JsonIgnore
    public String getApplicant2EmailAddress() {
        final String applicant2Email = applicant2.getEmail();

        if (StringUtils.isEmpty(applicant2Email)) {
            if (nonNull(caseInvite)) {
                return caseInvite.getApplicant2InviteEmailAddress();
            } else {
                return null;
            }
        }

        return applicant2Email;
    }
}
