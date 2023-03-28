package uk.gov.hmcts.reform.adoption.document.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.CaseworkerAccess;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
public class DssUploadedDocument {

    @CCD(
        label = "Documents generated",
        typeOverride = FieldType.Collection,
        typeParameterOverride = "DssDocumentInfo",
        access = {CaseworkerAccess.class}
    )
    private List<ListValue<DssDocumentInfo>> dssDocuments;

    @CCD(
        access = {CaseworkerAccess.class}
    )
    private String dssAdditionalCaseInformation;

    @CCD(
        access = {CaseworkerAccess.class}
    )
    private String dssCaseUpdatedBy;

}
