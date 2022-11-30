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
   let delivery = locate('//select[@id="checkAndSendOrderDropdownList"]/option').at(2);
   let orderType = await I.grabTextFrom(delivery);
   await I.wait(3);
   await I.retry(3).selectOption(this.fields.ordersDropDown, orderType);
   await I.click(this.fields.continueButton);
   await I.wait(3);
  },

  };
