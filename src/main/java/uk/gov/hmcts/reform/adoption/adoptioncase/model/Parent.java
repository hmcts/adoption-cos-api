package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.SystemCollectionAccess;

import java.util.List;
import java.util.SortedSet;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.Collection;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.TextArea;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Builder
public class Parent {

    @CCD(label = "First name")
    private String firstName;

    @CCD(label = "Last name")
    private String lastName;

    @CCD(label = "Still alive")
    private String stillAlive;

    @CCD(label = "Not Alive Reason")
    private String notAliveReason;

    @CCD(label = "Nationality")
    private SortedSet<Nationality> nationality;

    @CCD(label = "Occupation")
    private String occupation;

    @CCD(
        label = "Address Known"
    )
    private YesOrNo addressKnown;

    @CCD(label = "Address1")
    private String address1;

    @CCD(label = "Address2")
    private String address2;

    @CCD(label = "Address3")
    private String address3;

    @CCD(label = "Town")
    private String addressTown;

    @CCD(label = "County")
    private String addressCounty;

    @CCD(label = "Post code")
    private String addressPostCode;

    @CCD(label = "Country")
    private String addressCountry;

    @CCD(
        label = "Additional Nationalities",
        typeOverride = Collection,
        typeParameterOverride = "OtherNationality",
        access = {SystemCollectionAccess.class}
    )
    private List<ListValue<OtherNationality>> otherNationalities;

    @CCD(label = "Name on Certificate")
    private String nameOnCertificate;

    @CCD(label = "Address Not Known Reason",
        typeOverride = TextArea
    )
    private String addressNotKnownReason;
}
