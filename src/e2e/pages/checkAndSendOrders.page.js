const { I } = inject();

module.exports = {

  fields: {
    pageTitle: '//h1[text()="Check and send orders"]',
    childName: '//h3[text()="Child\'s Name: child child"]',
    ordersToReviewTitle: '//h2[text()="Orders for review"]',
    ordersToReviewSubTitle: '//span[text()="Select the order you want to review"]',
    continueButton: '//button[@type="submit"]',
    errorMessage: '//span[@class="error-message ng-star-inserted"]',
    cancelButton: '//a[text()="Cancel"]',
    ordersDropDown: '#checkAndSendOrderDropdownList',
    orderToSelect: '//option[starts-with(@value, "1")]',
    continueButton: 'button[type="submit"]',
    alertMessage: '//div[@class="alert-message"]',
  },

  async verifyCheckAndSendOrdersPageDetails() {
    await I.wait(3);
    await I.retry(3).seeElement(this.fields.pageTitle);
    await I.retry(3).seeElement(this.fields.childName);
    await I.retry(3).seeElement(this.fields.ordersToReviewTitle);
    await I.retry(3).seeElement(this.fields.ordersToReviewSubTitle);
    await I.click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).seeElement(this.fields.errorMessage);
    await I.click(this.fields.cancelButton);
    await I.wait(5);
  },

 async selectOrderToReview(){
 //Implementation
    const currentUrl = await I.grabCurrentUrl();
    await I.retryUntilExists(async () => {
      if(await I.waitForSelector(this.fields.ordersDropDown, 30) != null) {
        await I.scrollToElement(this.fields.ordersDropDown);
        I.selectOption(this.fields.ordersDropDown, '//select[@id="checkAndSendOrderDropdownList"]/option[2]');
        I.click(this.fields.continueButton);
      } else {
        const newUrl = await I.grabCurrentUrl();
        if(newUrl === currentUrl || !newUrl.includes('http')){
          console.log('Page refresh');
          I.refreshPage();
        }
      }
    }, 'ccd-case-event-trigger', false);
  },

 async verifyCheckYourAnswersPage(){
  // To be implemented after developement
  await I.wait(5);
  I.click(this.fields.continueButton);
  I.wait(3);
  I.retry(5).seeElement(this.fields.alertMessage);
  },
  };
