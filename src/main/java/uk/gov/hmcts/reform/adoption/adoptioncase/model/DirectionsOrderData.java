package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.MultiSelectList;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.TextArea;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DirectionsOrderData {

    @CCD(showCondition = "submittedDateDirectionsOrder=\"never\"")
    private String orderId;

    @CCD(access = {DefaultAccess.class})
    private LocalDateTime submittedDateDirectionsOrder;

    @CCD(hint = "Copy and paste or type directly into the text box",
        typeOverride = TextArea,
        access = {DefaultAccess.class})
    private String preambleDetailsDO;

    @CCD(showCondition = "submittedDateDirectionsOrder=\"never\"")
    private OrderStatus orderStatus;

    @CCD(hint = "Enter the name of the judge issuing this order",
        access = {DefaultAccess.class})
    private String orderBy;

    @CCD(access = {DefaultAccess.class})
    private LocalDate dateOfGeneralDirectionOrderMade;

    @CCD(hint = "Copy and paste or type directly into the text box",
        typeOverride = TextArea,
        access = {DefaultAccess.class})
    private String generalDirectionOrderPreamble;

    @CCD(hint = "Make sure you number the paragraphs in the body of the order starting with 1",
        typeOverride = TextArea,
        access = {DefaultAccess.class})
    private String generalDirectionBodyOfTheOrder;

    @CCD(access = {DefaultAccess.class})
    private String generalDirectionCostOfOrder;

    @CCD(access = {DefaultAccess.class})
    private YesOrNo isThereAnyOrderTypeYouNeedToSend;


    @CCD(access = {DefaultAccess.class},
        hint = "If you select one of these orders, any text you have entered above will not be saved")
    private GeneralDirectionOrderTypes generalDirectionOrderTypes;

    @CCD(access = {DefaultAccess.class},
        typeOverride = MultiSelectList,
        typeParameterOverride = "GeneraDirectionsIncluded")
    private Set<GeneraDirectionsIncluded> generaDirectionsIncluded;

    @CCD(access = {DefaultAccess.class})
    private String generalDirectionNameOfPrison;

    @CCD(access = {DefaultAccess.class},
        typeOverride = TextArea)
    private String generalDirectionAddressOfPrison;

    @CCD(access = {DefaultAccess.class})
    private String generalDirectionNameOfThePrisoner;

    @CCD(access = {DefaultAccess.class})
    private String generalDirectionPrisonerNumber;

    @CCD(access = {DefaultAccess.class})
    private String generalDirectionHearingVenue;

    @CCD(access = {DefaultAccess.class})
    private LocalDateTime generalDirectionHearingDateTime;

    @CCD(access = {DefaultAccess.class})
    private GeneralDirectionOrderState generalDirectionOrderState;



}
