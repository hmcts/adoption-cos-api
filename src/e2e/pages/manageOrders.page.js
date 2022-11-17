const config = require('../config');
const { I } = inject();
const manageOrderDetails = require('../fixtures/manageOrderDetails.js');
module.exports = {
  fields: {
    caseId: 'h3:nth-child(1)',
    ChildName: 'h3:nth-child(2)',
    continueButton: 'button[type="submit"]',
    manageOrderTypeErrorMessage: '#manageOrderType .error-message',
    manageOrderActivityErrorMessage: '#manageOrderActivity .error-message',
    createOrder: '#manageOrderActivity-createOrder',
    caseManagementOrder: '#manageOrderType-caseManagementOrder',
    generalDirectionsOrder: '#manageOrderType-generalDirectionsOrder',
    finalAdoptionOrder: '#manageOrderType-finalAdoptionOrder',
    manageOrdersUpdate: '//div[contains(text(), "has been updated with event: Manage orders")]',
    preambleDetails: '#preambleDetails',
    allocatedJudge: '#allocatedJudge',
    reallocateJudge: '#allocationJudge-reallocateJudge',
    nameOfJudge: '#nameOfJudge',
    allocatePreviousProceedingsJudge: '#allocationJudge-allocatePreviousProceedingsJudge',
    listForFirstHearing: '#hearingNotices-listForFirstHearing',
    additionalParagraph: '#additionalPara_0_additionalParagraphTA',
    orderBy: '#orderedBy',
    hearingDetails: {
      dateAndTimeFirstHearingErrorMessage: '#dateAndTimeFirstHearing .error-message',
      dateAndTimeFirstHearingDay: '#dateAndTimeFirstHearing-day',
      dateAndTimeFirstHearingMonth: '#dateAndTimeFirstHearing-month',
      dateAndTimeFirstHearingYear: '#dateAndTimeFirstHearing-year',
    },
    lengthOfHearingFirstHearing: '#lengthOfHearingFirstHearing',
    lengthOfHearingFirstHearingErrorMessage: 'ccd-field-write[field_id=\'lengthOfHearingFirstHearing\'] .error-message',
    localAuthority: {
      selectLocalAuthority: '#selectedLocalAuthority-fileAdoptionAgencyReport',
      dateAndTimeForOption1Day: '#dateAndTimeForOption1-day',
      dateAndTimeForOption1Month: '#dateAndTimeForOption1-month',
      dateAndTimeForOption1Year: '#dateAndTimeForOption1-year',
      dateAndTimeForOption2Day: '#timeForOption2-day',
      dateAndTimeForOption2Month: '#timeForOption2-month',
      dateAndTimeForOption2Year: '#timeForOption2-year',
      dateAndTimeForOption1Error: '#dateAndTimeForOption1 .error-message',
      dateAndTimeForOption2Error: '#timeForOption2 .error-message',

    },
    attendance: {
      applicantsAttendance: '#attendance-applicantsAttendance',
      childAttendance: '#attendance-childAttendance',
      localAuthorityAttendance: '#attendance-localAuthorityAttendance',
      birthParentsAttendance: '#attendance-birthParentsAttendance',
      applicantAttendanceNotAttend: '#applicantAttendance-applicantAttendanceNotAttend',
      childAttendanceNotBeBrought: '#childAttendance-childAttendanceNotBeBrought',
      laAttendanceAttend: '#laAttendance-laAttendanceAttend',
      birthParentAttendanceCourtArrange: '#birthParentAttendance-birthParentAttendanceCourtArrange',
      applicantAttendanceError: '#applicantAttendance .error-message',
      childAttendanceError: '#childAttendance .error-message',
      laAttendanceError: '#laAttendance .error-message',
      birthParentAttendanceError: '#birthParentAttendance .error-message',
    },
    leaveToOppose: {
      leaveToOppose: '#leaveToOppose-leaveToOppose',
      additionalParagraphTA0: '#additionalPara_0_additionalParagraphTA',
      additionalParagraphTA1: '#additionalPara_1_additionalParagraphTA',
    },
    costOrders: {
      noOrderForCosts: '#costOrders-noOrderForCosts',
      orderedBy: '#orderedBy',

    },
    cafcass: {
      reportingOfficer: '#cafcass-reportingOfficer',
      childrensGuardian: '#cafcass-childrensGuardian',
      reportingOfficerCafcass: '#reportingOfficer-cafcass',
      childrensGuardianCafcass: '#childrensGuardian-cafcass',
      caseNumberCG: '#caseNumberCG',
      dateAndTimeRODay: '#dateAndTimeRO-day',
      dateAndTimeROMonth: '#dateAndTimeRO-month',
      dateAndTimeROYear: '#dateAndTimeRO-year',
      dateAndTimeROHour: '#dateAndTimeRO-hour',

    },
    serveParties: {
      birthMother: '#recipientsList-birthMother',
      birthFather: '#recipientsList-birthFather',
      applicants: '#recipientsList-applicants',
      childLocalAuthority: '#recipientsList-childsLocalAuthority',
      applicantLocalAuthority: '#recipientsList-applicantsLocalAuthority',
      otherAdoptionAgency: '#recipientsList-otherAdoptionAgency',
      otherPersonWithParentalResponsibility: '#recipientsList-otherPersonWithParentalResponsibility',
      cafCass: '#recipientsList-cafcass'

    },

  },

  async verifyCaseDetails() {
    await I.wait(3);
    await I.retry(3).waitForSelector(this.fields.caseId);
    await I.retry(3).seeElement(this.fields.caseId);
    await I.retry(3).seeElement(this.fields.ChildName);
  },

  async selectCreateNewOrder(){
    await I.wait(3);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).see('What do you want to do? is required',this.fields.manageOrderActivityErrorMessage);
    await I.retry(3).click(this.fields.createOrder);
    await I.retry(3).click(this.fields.continueButton);
  },

  async verifyTypeOfOrdersListed(){
    await I.wait(3);
    await I.retry(3).seeElement(this.fields.caseManagementOrder);
    await I.retry(3).seeElement(this.fields.generalDirectionsOrder);
    await I.retry(3).seeElement(this.fields.finalAdoptionOrder);
  },

  async selectCaseManagementOrderAndVerify(){
    await I.wait(3);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).see('Select type of order is required',this.fields.manageOrderTypeErrorMessage);
    await I.retry(3).click(this.fields.caseManagementOrder);
    await I.wait(3);
    await I.retry(3).click(this.fields.continueButton);
  },
  async addPreambleAndReallocateJudgeInCaseManagementOrder(){
    await I.wait(3);
    await I.retry(3).fillField(this.fields.preambleDetails, manageOrderDetails.caseManagementOrderDetails.preamble);
    await I.wait(3);
    await I.retry(3).click(this.fields.reallocateJudge);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).see('Case management order first directions');
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.preamble);
    await I.retry(3).fillField(this.fields.nameOfJudge, manageOrderDetails.caseManagementOrderDetails.nameOfReallocatedJudge);
    await I.retry(3).click(this.fields.continueButton);
    await this.addLeaveToOpposeAndCostOrder();
    await I.retry(3).click(this.fields.continueButton);
  },

  async addPreambleAndPreviousAllocateJudgeInCaseManagementOrder(){
    await I.wait(3);
    await I.retry(3).fillField(this.fields.preambleDetails, manageOrderDetails.caseManagementOrderDetails.preamble);
    await I.wait(3);
    await I.retry(3).click(this.fields.allocatePreviousProceedingsJudge);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).see('Case management order first directions');
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.preamble);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).see('Name of the judge is required');
    await I.retry(3).fillField(this.fields.allocatedJudge, manageOrderDetails.caseManagementOrderDetails.nameOfAllocatedJudge);
    await I.retry(3).click(this.fields.continueButton);
    await this.addLeaveToOpposeAndCostOrder();
    await I.retry(3).click(this.fields.continueButton);
  },

  async caseManagementOrderPreambleReAllocatedCYAPage(){
    await I.wait(3);
    await I.retry(3).waitForText('Check your answers',30);
    await I.retry(3).see('Check the information below carefully.');
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.preamble);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.nameOfReallocatedJudge);
    await I.wait(3);
    await I.retry(3).click(this.fields.continueButton);
  },

  async caseManagementOrderPreambleAllocatedCYAPage(){
    await I.wait(3);
    await I.retry(3).waitForText('Check your answers',30);
    await I.retry(3).see('Check the information below carefully.');
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.preamble);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.nameOfAllocatedJudge);
    await I.wait(3);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
  },
  async caseManagementOrderListForFirstHearing(){
      await I.wait(3);
      await I.retry(3).click(this.fields.listForFirstHearing);
      await I.retry(3).click(this.fields.continueButton);
      await I.wait(3);
      await I.retry(3).click(this.fields.continueButton);
      await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.errorMessage,this.fields.hearingDetails.dateAndTimeFirstHearingErrorMessage);
      await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.errorMessage,this.fields.lengthOfHearingFirstHearingErrorMessage);
      await I.retry(3).fillField(this.fields.hearingDetails.dateAndTimeFirstHearingDay, manageOrderDetails.caseManagementOrderDetails.hearingDate.day);
      await I.retry(3).fillField(this.fields.hearingDetails.dateAndTimeFirstHearingMonth, manageOrderDetails.caseManagementOrderDetails.hearingDate.month);
      await I.retry(3).fillField(this.fields.hearingDetails.dateAndTimeFirstHearingYear, manageOrderDetails.caseManagementOrderDetails.hearingDate.year);
      await I.retry(3).fillField(this.fields.lengthOfHearingFirstHearing, manageOrderDetails.caseManagementOrderDetails.lengthOfHearingFirstHearing);
      await I.retry(3).click(this.fields.continueButton);
      await this.addLeaveToOpposeAndCostOrder();
      await I.retry(3).click(this.fields.continueButton);
    },

      async caseManagementOrderListForFirstHearingCYAPage(){
          await I.wait(3);
          await I.retry(3).see('1 Jan 2050, 12:00:00 AM');
          await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.lengthOfHearingFirstHearing);
          await I.retry(3).click(this.fields.continueButton);
        },

 async caseManagementOrderLocalAuthority(){
     await I.wait(3);
     await I.retry(3).click(this.fields.localAuthority.selectLocalAuthority);
     await I.retry(3).click(this.fields.continueButton);
     await I.wait(3);
     await I.retry(3).click(this.fields.continueButton);
     await I.wait(3);
     await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.errorMessage,this.fields.localAuthority.dateAndTimeForOption1Error);
     await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.errorMessage,this.fields.localAuthority.dateAndTimeForOption2Error);
     await I.wait(3);
     await I.retry(3).fillField(this.fields.localAuthority.dateAndTimeForOption1Day, manageOrderDetails.caseManagementOrderDetails.localAuthorityOption1.day);
     await I.retry(3).fillField(this.fields.localAuthority.dateAndTimeForOption1Month, manageOrderDetails.caseManagementOrderDetails.localAuthorityOption1.month);
     await I.retry(3).fillField(this.fields.localAuthority.dateAndTimeForOption1Year, manageOrderDetails.caseManagementOrderDetails.localAuthorityOption1.year);
     await I.retry(3).fillField(this.fields.localAuthority.dateAndTimeForOption2Day, manageOrderDetails.caseManagementOrderDetails.localAuthorityOption2.day);
     await I.retry(3).fillField(this.fields.localAuthority.dateAndTimeForOption2Month, manageOrderDetails.caseManagementOrderDetails.localAuthorityOption2.month);
     await I.retry(3).fillField(this.fields.localAuthority.dateAndTimeForOption2Year, manageOrderDetails.caseManagementOrderDetails.localAuthorityOption2.year);
     await I.retry(3).click(this.fields.continueButton);
     await this.addLeaveToOpposeAndCostOrder();
     await I.retry(3).click(this.fields.continueButton);
   },

  async caseManagementOrderAttendance(){
    await I.retry(3).click(this.fields.attendance.applicantsAttendance);
    await I.retry(3).click(this.fields.attendance.childAttendance);
    await I.retry(3).click(this.fields.attendance.localAuthorityAttendance);
    await I.retry(3).click(this.fields.attendance.birthParentsAttendance);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).waitForText('Choose the direction for the attendees',30);
    await I.retry(3).see('Choose the direction for the attendees');
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.errorMessage,this.fields.attendance.applicantAttendanceError);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.errorMessage,this.fields.attendance.childAttendanceError);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.errorMessage,this.fields.attendance.laAttendanceError);
    await I.retry(3).click(this.fields.attendance.applicantAttendanceNotAttend);
    await I.retry(3).click(this.fields.attendance.childAttendanceNotBeBrought);
    await I.retry(3).click(this.fields.attendance.laAttendanceAttend);
    await I.retry(3).click(this.fields.attendance.birthParentAttendanceCourtArrange);
    await I.retry(3).click(this.fields.continueButton);
    await this.addLeaveToOpposeAndCostOrder();
    await I.retry(3).click(this.fields.continueButton);

  },
  async caseManagementOrderAttendanceCYAPage(){
    await I.wait(3);
    await I.retry(3).waitForText('Check your answers',30);
    await I.retry(3).see('Check your answers');
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.applicantAttendanceNotAttendOption);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.childAttendanceNotBeBroughtOption);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.localAuthorityAttendanceOption);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.birthParentAttendanceCourtOption);
    await I.retry(3).click(this.fields.continueButton);
  },
  async caseManagementOrderLocalAuthorityCYAPage(){
    await I.wait(3);
    await I.retry(3).see('1 Jan 2029, 12:00:00 AM');
    await I.retry(3).see('1 Jan 2030, 12:00:00 AM');
    await I.retry(3).click(this.fields.continueButton);
  },

  async caseManagementOrderLeaveToOpposeAndCostOrder(){
    await I.wait(3);
    await I.retry(3).click(this.fields.leaveToOppose.leaveToOppose);
    await I.retry(3).click(this.fields.costOrders.noOrderForCosts);
    await I.wait(3);
    await I.retry(3).click(this.fields.continueButton);
    await this.addLeaveToOpposeAndCostOrder();
    await I.retry(3).click(this.fields.continueButton);
  },

  async addLeaveToOpposeAndCostOrder(){
    await I.wait(3);
    await I.retry(3).click('Add new');
    await I.wait(3);
    await I.retry(3).fillField(this.fields.leaveToOppose.additionalParagraphTA0, manageOrderDetails.caseManagementOrderDetails.additionalParagraphsOne);
    await I.retry(3).click('Add new');
    await I.wait(3);
    await I.retry(3).fillField(this.fields.leaveToOppose.additionalParagraphTA1, manageOrderDetails.caseManagementOrderDetails.additionalParagraphsTwo);
    await I.retry(3).fillField(this.fields.costOrders.orderedBy, manageOrderDetails.caseManagementOrderDetails.orderedBy);
    await I.wait(3);
  },

  async caseManagementOrderCafcass(){
    await I.wait(3);
    await I.retry(3).click(this.fields.cafcass.reportingOfficer);
    await I.retry(3).click(this.fields.cafcass.childrensGuardian);
    await I.wait(3);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).see('Case management order first directions');
    await I.retry(3).click(this.fields.cafcass.reportingOfficerCafcass);
    await I.retry(3).fillField(this.fields.cafcass.dateAndTimeRODay, manageOrderDetails.caseManagementOrderDetails.cafcassReportingOfficer.day);
    await I.retry(3).fillField(this.fields.cafcass.dateAndTimeROMonth, manageOrderDetails.caseManagementOrderDetails.cafcassReportingOfficer.month);
    await I.retry(3).fillField(this.fields.cafcass.dateAndTimeROYear, manageOrderDetails.caseManagementOrderDetails.cafcassReportingOfficer.year);
    await I.retry(3).fillField(this.fields.cafcass.dateAndTimeROHour, manageOrderDetails.caseManagementOrderDetails.cafcassReportingOfficer.hour);
    await I.retry(3).click(this.fields.cafcass.childrensGuardianCafcass);
    await I.retry(3).fillField(this.fields.cafcass.caseNumberCG, manageOrderDetails.caseManagementOrderDetails.cafcassCaseNumber);
    await I.retry(3).click(this.fields.continueButton);
    await this.addLeaveToOpposeAndCostOrder();
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
  },
  async caseManagementOrderAddAdditionalParagraph() {
    await I.wait(3);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(10);
    await I.retry(3).see('Additional paragraphs');
    await I.retry(3).click('Add new');
    await I.retry(3).fillField(this.fields.additionalParagraph, "additional text");
    await I.retry(3).fillField(this.fields.orderBy, "James Bond");
    await I.retry(3).click(this.fields.continueButton);

  },


  async caseManagementOrderServeParties() {
    await I.wait(3);
    await I.retry(3).waitForText('Case management order recipients', 30);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.partiesToServerOrder);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.errorMessage);
    await I.retry(3).click(this.fields.serveParties.birthMother);
    await I.retry(3).click(this.fields.serveParties.birthFather);
    await I.retry(3).click(this.fields.serveParties.applicants);
    await I.retry(3).click(this.fields.serveParties.childLocalAuthority);
    await I.retry(3).click(this.fields.serveParties.applicantLocalAuthority);
    await I.retry(3).click(this.fields.serveParties.otherAdoptionAgency);
    await I.retry(3).click(this.fields.serveParties.otherPersonWithParentalResponsibility);
    await I.retry(3).click(this.fields.serveParties.cafCass);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
  },
  async caseManagementOrderServePartiesCYAPage() {
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.partiesToServerOrder);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.applicantLocalAuthority);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.birthMother);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.birthFather);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.applicants);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.childLocalAuthority);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.otherAdoptionAgency);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.otherPersonWithParentalResponsibility);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.cafCass);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
  },
};
