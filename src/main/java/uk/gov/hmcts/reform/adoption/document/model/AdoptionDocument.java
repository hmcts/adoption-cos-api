package uk.gov.hmcts.reform.adoption.document.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.reform.adoption.document.DocumentCategory;
import uk.gov.hmcts.reform.adoption.document.DocumentType;

import java.time.LocalDate;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedList;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedRadioList;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.TextArea;

@Data
@NoArgsConstructor
@Builder
@ToString
public class AdoptionDocument {

    @CCD(
        label = "Add content to be emailed",
        typeOverride = TextArea,
        ignore = true
    )
    private String documentEmailContent;

    @CCD(
        label = "Document",
        hint = "The selected file must be smaller than 1GB"
    )
    private Document documentLink;

    @CCD(
        label = "Date",
        showCondition = "documentLink=\"\""
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate documentDateAdded;

    @CCD(
        label = "Document description",
        hint = "Describe what the document is, such as death certificate for the birth father."
    )
    private String documentComment;

    @CCD(
        label = "File name",
        hint = "For your own reference, to make the document easier to find",
        showCondition = "documentLink=\"\""
    )
    private String documentFileName;

    @CCD(
        label = "Select document type",
        typeOverride = FixedList,
        typeParameterOverride = "DocumentType",
        showCondition = "documentLink=\"\""
    )
    private DocumentType documentType;

    @CCD(
        label = "File Id",
        hint = "DM Store file Id",
        showCondition = "documentLink=\"\""
    )
    private String documentFileId;

    @CCD(
        label = "What document are you uploading?",
        hint = "If you want to upload more than one, you need to go through the steps again from the documents tab.",
        typeOverride = FixedRadioList,
        typeParameterOverride = "DocumentCategory"
    )
    private DocumentCategory documentCategory;

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
        label = "User",
        showCondition = "documentLink=\"\""
    )
    private String user;

    @CCD(
        label = "Date added",
        showCondition = "documentLink=\"\""
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    //Add handwritten constructor as a workaround for @JsonUnwrapped prefix issue
    @JsonCreator
    public AdoptionDocument(@JsonProperty("documentEmailContent") String documentEmailContent,
                            @JsonProperty("documentLink") Document documentLink,
                            @JsonProperty("documentDateAdded") LocalDate documentDateAdded,
                            @JsonProperty("documentComment") String documentComment,
                            @JsonProperty("documentFileName") String documentFileName,
                            @JsonProperty("documentType") DocumentType documentType,
                            @JsonProperty("documentFileId") String documentFileId,
                            @JsonProperty("documentCategory") DocumentCategory documentCategory,
                            @JsonProperty("role") String role,
                            @JsonProperty("name") String name,
                            @JsonProperty("user") String user,
                            @JsonProperty("date") LocalDate date) {
        this.documentEmailContent = documentEmailContent;
        this.documentLink = documentLink;
        this.documentDateAdded = documentDateAdded;
        this.documentComment = documentComment;
        this.documentFileName = documentFileName;
        this.documentType = documentType;
        this.documentFileId = documentFileId;
        this.documentCategory = documentCategory;
        this.role = role;
        this.name = name;
        this.user = user;
        this.date = date;
    }
}
