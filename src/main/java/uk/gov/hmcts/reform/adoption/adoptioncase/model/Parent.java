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

    @CCD(label = "First names",
        access = {DefaultAccess.class})
    private String firstName;

    @CCD(label = "Last names",
        access = {DefaultAccess.class})
    private String lastName;

    @CCD(label = "Deceased – Yes/No",
        access = {SystemUpdateAccess.class,DefaultAccess.class})
    private YesOrNo stillAlive;

    @CCD(label = "Deceased",
        access = {DefaultAccess.class})
    private YesOrNo deceased;

    @CCD(label = "Not Alive Reason",
        access = {DefaultAccess.class})
    private String notAliveReason;

    @CCD(label = "Nationality",
        access = {DefaultAccess.class})
    private SortedSet<Nationality> nationality;

    @CCD(label = "Occupation",
        access = {DefaultAccess.class})
    private String occupation;

    @CCD(
        label = "Address Known",
        access = {DefaultAccess.class}
    )
    private YesOrNo addressKnown;

    @CCD(label = "Address line 1",
        access = {DefaultAccess.class})
    private String address1;

    @CCD(label = "Address line 2",
        access = {DefaultAccess.class})
    private String address2;

    @CCD(label = "Address line 3",
        access = {DefaultAccess.class})
    private String address3;

    @CCD(label = "Town or city",
        access = {DefaultAccess.class})
    private String addressTown;

    @CCD(label = "County, district, state or province",
        access = {DefaultAccess.class})
    private String addressCounty;

    @CCD(label = "Post code",
        access = {DefaultAccess.class})
    private String addressPostCode;

    @CCD(label = "Country",
        access = {DefaultAccess.class})
    private String addressCountry;

    @CCD(
        label = "Additional Nationalities",
        typeOverride = Collection,
        typeParameterOverride = "OtherNationality",
        access = {SystemUpdateCollectionAccess.class}
    )
    private List<ListValue<OtherNationality>> otherNationalities;

    @CCD(label = "Name on Certificate",
        access = {DefaultAccess.class})
    private String nameOnCertificate;

    @CCD(label = "Address Not Known Reason",
        typeOverride = TextArea,
        access = {DefaultAccess.class}
    )
    private String addressNotKnownReason;

    @CCD(
        label = "Date of address last confirmed",
        access = {DefaultAccess.class}
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastAddressDate;

    @CCD(label = "Birth Father Responsibility",
        access = {DefaultAccess.class})
    private YesOrNo responsibility;

    @CCD(
        label = "Responsibility Reason",
        access = {DefaultAccess.class}
    )
    private SortedSet<ResponsibilityReasons> responsibilityReason;

    @CCD(label = "Other Responsibility Reason",
        access = {DefaultAccess.class})
    private String otherResponsibilityReason;

    @CCD(label = "Identity known",
        access = {DefaultAccess.class})
    private String identityKnown;

    @CCD(label = "Relationship (e.g. grandparent, step-parent):",
        access = {DefaultAccess.class})
    private String relationShipWithChild;

    @CCD(label = "To be served",
        access = {SystemUpdateAccess.class})
    private YesOrNo toBeServed;

    @CCD(label = "Served With",
        access = {DefaultAccess.class})
    private YesOrNo servedWith;

    @CCD(label = "Not served with reason",
        access = {SystemUpdateAccess.class})
    private String notServedWithReason;

    public YesOrNo getToBeServed() {
        if (Objects.isNull(toBeServed)) {
            return YesOrNo.YES;
        }
        return toBeServed;
    }


}
