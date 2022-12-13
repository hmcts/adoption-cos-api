const config = require('../config');
const {I} = inject();
const generalDirectionOrderDetails = require('../fixtures/generalDirectionOrderDetails.js');

module.exports = {
  fields: {
    generalDirectionsOrder: '#manageOrderType-generalDirectionsOrder',
    manageOrderTypeErrorMessage: '#manageOrderType .error-message',
    orderByError: '[field_id=\'directionOrderBy\'] .error-message',
    orderBy: '#directionOrderBy',
    preambleDetails: '#generalDirectionOrderPreamble',
    bodyOfOrder: '#generalDirectionBodyOfTheOrder',
    costOrder: '#generalDirectionCostOfOrder',
    dateOrderMadeError: '#dateOfGeneralDirectionOrderMade .error-message',
    dateOrderMade: {
      day: '#dateOfGeneralDirectionOrderMade-day',
      month: '#dateOfGeneralDirectionOrderMade-month',
      year: '#dateOfGeneralDirectionOrderMade-year'
    },

    directionToOrder: {
      hearingDelay: '#generaDirectionsIncluded-generalDirectionHearingDelay',
      fillingBundles: '#generaDirectionsIncluded-generalDirectionFillingBundles',
      warningNotice: '#generaDirectionsIncluded-generalDirectionWarningNotice',
    },
    furtherOrderError: '#isThereAnyOrderTypeYouNeedToSend .error-message',
    areThereFurtherOrdersYes: '#isThereAnyOrderTypeYouNeedToSend_Yes',
    areThereFurtherOrdersNo: '#isThereAnyOrderTypeYouNeedToSend_No',
    productionOrder: '#generalDirectionOrderTypes-generalDirectionProductionOrder',
    nameOfPrisonError: '[field_id=\'generalDirectionNameOfPrison\'] .error-message',
    nameOfPrison: '#generalDirectionNameOfPrison',
    addressOfPrisonError: '[field_id=\'generalDirectionAddressOfPrison\'] .error-message',
    addressOfPrison: '#generalDirectionAddressOfPrison',
    nameOfPrisonerError: '[field_id=\'generalDirectionNameOfThePrisoner\'] .error-message',
    nameOfPrisoner: '#generalDirectionNameOfThePrisoner',
    prisonerNumberError: '[field_id=\'generalDirectionPrisonerNumber\'] .error-message',
    prisonerNumber: '#generalDirectionPrisonerNumber',
    hearingVenueError: '[field_id=\'generalDirectionHearingVenue\'] .error-message',
    hearingsVenue: '#generalDirectionHearingVenue',
    hearingDateAndTimeError: '[field_id=\'generalDirectionHearingDateTime\'] .error-message',
    hearingDateTime: {
      day: '#generalDirectionHearingDateTime-day',
      month: '#generalDirectionHearingDateTime-month',
      year: '#generalDirectionHearingDateTime-year',
      hour: '#generalDirectionHearingDateTime-hour'
    },
    recipientsList: {
      error: '#generalDirectionRecipientsList .error-message',
      generalDirectionRecipientsListApplicant1: '#generalDirectionRecipientsList-applicant1',
      generalDirectionRecipientsListApplicant2: '#generalDirectionRecipientsList-applicant2',
      generalDirectionRecipientsListBirthMother: '#generalDirectionRecipientsList-birthMother',
    },

    continueButton: 'button[type="submit"]',
  },

    async selectGeneralDirectionOrderAndVerify() {
      await I.wait(3);
      await I.retry(3).click(this.fields.continueButton);
      await I.wait(3);
      await I.retry(3).see('Select type of order is required', this.fields.manageOrderTypeErrorMessage);
      await I.retry(3).click(this.fields.generalDirectionsOrder);
      await I.retry(3).click(this.fields.continueButton);
      await I.wait(3);
    },

    async verifyGeneralDirectionOrderDetails() {
      await I.retry(3).waitForText('General directions order', 30);
      await I.retry(3).see("General directions order");
      await I.retry(3).see(generalDirectionOrderDetails.gdOrderDetails.hearingDetailsTitle);
      await I.retry(3).click(this.fields.continueButton);
      await I.wait(3);
      await I.retry(3).see(generalDirectionOrderDetails.gdOrderDetails.errorMessage, this.fields.orderByError);
      await I.retry(3).fillField(this.fields.orderBy, generalDirectionOrderDetails.gdOrderDetails.orderedBy)
      await I.retry(3).fillField(this.fields.preambleDetails, generalDirectionOrderDetails.gdOrderDetails.preamble);
      await I.retry(3).see(generalDirectionOrderDetails.gdOrderDetails.errorMessage, this.fields.dateOrderMadeError);
      await I.retry(3).fillField(this.fields.dateOrderMade.day, generalDirectionOrderDetails.gdOrderDetails.dom.day);
      await I.retry(3).fillField(this.fields.dateOrderMade.month, generalDirectionOrderDetails.gdOrderDetails.dom.month);
      await I.retry(3).fillField(this.fields.dateOrderMade.year, generalDirectionOrderDetails.gdOrderDetails.dom.year);
      await I.retry(3).see(generalDirectionOrderDetails.gdOrderDetails.directionsToOrder);
      await I.retry(3).click(this.fields.directionToOrder.hearingDelay);
      await I.retry(3).click(this.fields.directionToOrder.fillingBundles);
      await I.retry(3).click(this.fields.directionToOrder.warningNotice);
      await I.retry(3).see(generalDirectionOrderDetails.gdOrderDetails.anotherOrderTypeToSend);
      await I.retry(3).click(this.fields.continueButton);
      await I.wait(3);
      await I.retry(3).see(generalDirectionOrderDetails.gdOrderDetails.errorMessage, this.fields.furtherOrderError);
      await I.retry(3).click(this.fields.areThereFurtherOrdersYes);
      await I.wait(3);
      await I.retry(3).see(generalDirectionOrderDetails.gdOrderDetails.productionOrderHintText);
      await I.retry(3).click(this.fields.productionOrder);
      await I.retry(3).click(this.fields.continueButton);
      await I.wait(3);

    },

    async verifyProductionOrderDetails() {
      await I.retry(3).waitForText('Production order', 30);
      await I.retry(3).see("Production order");
      await I.retry(3).click(this.fields.continueButton);
      await I.wait(3);
      await I.retry(3).see(generalDirectionOrderDetails.gdOrderDetails.errorMessage, this.fields.nameOfPrisonError);
      await I.retry(3).fillField(this.fields.nameOfPrison, generalDirectionOrderDetails.gdOrderDetails.prisionName);
      await I.retry(3).see(generalDirectionOrderDetails.gdOrderDetails.errorMessage, this.fields.addressOfPrisonError);
      await I.retry(3).fillField(this.fields.addressOfPrison, generalDirectionOrderDetails.gdOrderDetails.prisonAddress);
      await I.retry(3).see(generalDirectionOrderDetails.gdOrderDetails.errorMessage, this.fields.nameOfPrisonerError);
      await I.retry(3).fillField(this.fields.nameOfPrisoner, generalDirectionOrderDetails.gdOrderDetails.prisonerName);
      await I.retry(3).see(generalDirectionOrderDetails.gdOrderDetails.errorMessage, this.fields.prisonerNumberError);
      await I.retry(3).fillField(this.fields.prisonerNumber, generalDirectionOrderDetails.gdOrderDetails.prisonerNumber);
      await I.retry(3).see(generalDirectionOrderDetails.gdOrderDetails.errorMessage, this.fields.hearingVenueError);
      await I.retry(3).fillField(this.fields.hearingsVenue, generalDirectionOrderDetails.gdOrderDetails.hearingVenue);
      await I.retry(3).see(generalDirectionOrderDetails.gdOrderDetails.errorMessage, this.fields.hearingDateAndTimeError);
      await I.retry(3).fillField(this.fields.hearingDateTime.day, generalDirectionOrderDetails.gdOrderDetails.hearingDateAndTime.day);
      await I.retry(3).fillField(this.fields.hearingDateTime.month, generalDirectionOrderDetails.gdOrderDetails.hearingDateAndTime.month);
      await I.retry(3).fillField(this.fields.hearingDateTime.year, generalDirectionOrderDetails.gdOrderDetails.hearingDateAndTime.year);
      await I.retry(3).fillField(this.fields.hearingDateTime.hour, generalDirectionOrderDetails.gdOrderDetails.hearingDateAndTime.hour);
      await I.retry(3).click(this.fields.continueButton);
      await I.wait(3);
      },

  async selectRecipientsOfThisOrderDetails() {
    await I.retry(3).waitForText('Recipients of this order', 30);
    await I.retry(3).see("Recipients of this order");
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).see(generalDirectionOrderDetails.gdOrderDetails.errorMessage, this.fields.recipientsList.error);
    await I.retry(3).click(this.fields.recipientsList.generalDirectionRecipientsListApplicant1);
    await I.retry(3).click(this.fields.recipientsList.generalDirectionRecipientsListApplicant2);
    await I.retry(3).click(this.fields.recipientsList.generalDirectionRecipientsListBirthMother);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);

  },

  async verifyPreviewTheOrderDetails() {
    await I.retry(3).waitForText('Preview the order', 30);
    await I.retry(3).see("Preview the order");
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
  },

}
