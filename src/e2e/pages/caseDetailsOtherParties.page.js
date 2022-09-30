const {I} = inject();
const config = require('../config');

module.exports = {

  fields: {
     placementHeader: '//div[contains(@class, "complex-panel") and contains(.//span, "Placement")]',
     birthMother: '#labelSummary-birthMother'
  },


 async verifyBirthMotherAndFatherDetails(){
  // ToDO - Generic method to be written
 };
