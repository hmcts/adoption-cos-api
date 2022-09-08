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
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.SystemUpdateAccess;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.SystemUpdateCollectionAccess;

import java.time.LocalDate;
import java.util.List;
import java.util.SortedSet;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.Collection;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Builder
public class Children {

    @CCD(label = "First names")
    private String firstName;

    @CCD(label = "Last names")
    private String lastName;

    @CCD(
        label = "Date of Birth",
        access = {SystemUpdateAccess.class}
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @CCD(
        label = "Sex at birth",
        hint = "Child Gender",
        typeOverride = FixedList,
        typeParameterOverride = "Gender"
    )
    private Gender sexAtBirth;

    @CCD(label = "Nationality")
    private SortedSet<Nationality> nationality;

    @CCD(
        label = "Nationality",
        typeOverride = Collection,
        typeParameterOverride = "OtherNationality",
        access = {SystemUpdateCollectionAccess.class}
    )
    private List<ListValue<OtherNationality>> additionalNationalities;

    @CCD(label = "First name after adoption")
    private String firstNameAfterAdoption;

    @CCD(label = "Last name after adoption")
    private String lastNameAfterAdoption;


    @CCD(label = "Sex at birth")
    private String otherSexAtBirth;
}
