package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.AddressUK;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.SystemUpdateAccess;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.SystemUpdateCollectionAccess;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.Collection;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.TextArea;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Builder
public class Parent {

    @CCD(label = "First names")
    private String firstName;

    @CCD(label = "Last names")
    private String lastName;

    @CCD(label = "Deceased – Yes/No",
        access = {SystemUpdateAccess.class})
    private YesOrNo stillAlive;

    @CCD(label = "Deceased",
        access = {SystemUpdateAccess.class})
    private YesOrNo deceased;

    @CCD(label = "Not Alive Reason")
    private String notAliveReason;

    @CCD(label = "Nationality")
    private SortedSet<Nationality> nationality;

    @CCD(label = "Occupation")
    private String occupation;

    @CCD(
        label = "Address Known",
        access = {SystemUpdateAccess.class}
    )
    private YesOrNo addressKnown;

    @CCD(label = "Address line 1")
    private String address1;

    @CCD(label = "Address line 2")
    private String address2;

    @CCD(label = "Address line 3")
    private String address3;

    @CCD(label = "Town or city")
    private String addressTown;

    @CCD(label = "County, district, state or province")
    private String addressCounty;

    @CCD(label = "Post code")
    private String addressPostCode;

    @CCD(label = "Country")
    private String addressCountry;

    @CCD(access = {DefaultAccess.class},
        label = "Address")
    private AddressUK parentAddress;

    @CCD(
        label = "Additional Nationalities",
        typeOverride = Collection,
        typeParameterOverride = "OtherNationality",
        access = {SystemUpdateCollectionAccess.class}
    )
    private List<ListValue<OtherNationality>> otherNationalities;

    @CCD(label = "Name on Certificate")
    private String nameOnCertificate;

    @CCD(label = "Address Not Known Reason",
        typeOverride = TextArea
    )
    private String addressNotKnownReason;

    @CCD(
        label = "Date of address last confirmed",
        access = {SystemUpdateAccess.class}
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastAddressDate;



    @CCD(label = "Identity known")
    private String identityKnown;

    @CCD(label = "Relationship (e.g. grandparent, step-parent):")
    private String relationShipWithChild;

    @CCD(label = "To be served",
        access = {SystemUpdateAccess.class})
    private YesOrNo toBeServed;

    public YesOrNo getToBeServed() {
        if (Objects.isNull(toBeServed)) {
            return YesOrNo.YES;
        }
        return toBeServed;
    }


}
