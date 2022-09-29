package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.SystemUpdateAccess;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.SystemUpdateCollectionAccess;

import java.time.LocalDate;
import java.util.List;
import java.util.SortedSet;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.Collection;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedRadioList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Builder
public class Children {

    @CCD(label = "First names",
        access = {DefaultAccess.class})
    private String firstName;

    @CCD(label = "Last names",
        access = {DefaultAccess.class})
    private String lastName;

    @CCD(
        label = "Date of Birth",
        access = {SystemUpdateAccess.class,
            DefaultAccess.class}
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @CCD(
        label = "Sex at birth",
        hint = "Child Gender",
        typeOverride = FixedRadioList,
        typeParameterOverride = "Gender",
        access = {DefaultAccess.class}
    )
    private Gender sexAtBirth;

    @CCD(label = "Nationality",
        access = {DefaultAccess.class})
    private SortedSet<Nationality> nationality;

    @CCD(
        label = "Nationality (other)",
        typeOverride = Collection,
        typeParameterOverride = "OtherNationality",
        access = {SystemUpdateCollectionAccess.class}
    )
    private List<ListValue<OtherNationality>> additionalNationalities;

    @CCD(label = "First name after adoption",
        access = {DefaultAccess.class})
    private String firstNameAfterAdoption;

    @CCD(label = "Last name after adoption",
        access = {DefaultAccess.class})
    private String lastNameAfterAdoption;


    @CCD(label = "Sex at birth (other)")
    private String otherSexAtBirth;
}
