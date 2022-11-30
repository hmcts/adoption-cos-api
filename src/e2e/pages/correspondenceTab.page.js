const config = require('../config');
const { I } = inject();
const seekFurtherInfoDetails = require('../fixtures/seekFurtherInfoDetails.js');
module.exports = {

fields: {
    uploadCorrespondenceLink: '//*[text()="Upload correspondence"]',
},


  async verifyCorrespondenceTabOptions(){
  await I.wait(3);
  const date = new Date();
  let day = date.getDate();
  console.log(day);
  var month = date.toLocaleString('default', { month: 'short' });
  let year = date.getFullYear();
  let currentDate = `${day} ${month} ${year}`;
  await I.retry(6).seeElement(this.fields.uploadCorrespondenceLink);
  I.seeTextInTab(['Correspondence 1', 'Date'], currentDate);
  I.seeTextInTab(['Correspondence 1', 'Document description'], 'Seek further information');
  I.seeTextInTab(['Correspondence 1', 'Uploaded by'], 'User Test');
  },
};
