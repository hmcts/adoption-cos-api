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

@Data
@NoArgsConstructor
@Builder
@ToString
public class AdoptionUploadDocument {


    @CCD(
        label = "Document",
        hint = "The selected file must be smaller than 1GB"
    )
    private Document documentLink;

    @CCD(
        label = "Date added"
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate documentDateAdded;

    @CCD(
        label = "Document description",
        hint = "Describe what the document is, such as death certificate for the birth father."
    )
    private String documentComment;

    @CCD(
        label = "Select document type",
        typeOverride = FixedList,
        typeParameterOverride = "DocumentType"
    )
    private DocumentType documentType;

    @CCD(
        label = "What document are you uploading?",
        hint = "If you want to upload more than one, you need to go through the steps again from the documents tab.",
        typeOverride = FixedRadioList,
        typeParameterOverride = "DocumentCategory"
    )
    private DocumentCategory documentCategory;

    /*@CCD(
        label = "Who submitted the document?",
        //hint = "If you want to upload more than one, you need to go through the steps again from the documents tab.",
        typeOverride = FixedRadioList,
        typeParameterOverride = "DocumentSubmittedBy"
    )
    private DocumentSubmitter documentSubmitter;*/
    @CCD(
        label = "Role",
        hint = "Add a role. For example, \"Child social worker\" or \"First applicant\""
    )
    private String role;

    @CCD(
        label = "Name",
        hint = "Add corresponding name of the role mentioned above. "
            + "For example, \"Agnes James, London Borough of Tower Hamlets\" or \"Agatha Mary Clarissa Christie\""
    )
    private String name;

    //Add handwritten constructor as a workaround for @JsonUnwrapped prefix issue
    @JsonCreator
    public AdoptionUploadDocument(@JsonProperty("documentLink") Document documentLink,
                                  @JsonProperty("documentDateAdded") LocalDate documentDateAdded,
                                  @JsonProperty("documentComment") String documentComment,
                                  @JsonProperty("documentType") DocumentType documentType,
                                  @JsonProperty("documentCategory") DocumentCategory documentCategory,
                                  @JsonProperty("role") String role,
                                  @JsonProperty("name") String name) {
        this.documentLink = documentLink;
        this.documentDateAdded = documentDateAdded;
        this.documentComment = documentComment;
        this.documentType = documentType;
        this.documentCategory = documentCategory;
        this.role = role;
        this.name = name;
    }
}
