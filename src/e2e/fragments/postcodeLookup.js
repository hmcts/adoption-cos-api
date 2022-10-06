const { I } = inject();

module.exports = {

  fields: {
    postcodeLookup: 'input[id$="solicitorSolicitorAddress_solicitorSolicitorAddress_postcodeInput"]',
    addressList: 'select[id$="solicitorSolicitorAddress_solicitorSolicitorAddress_addressList"]',
    buildingAndStreet: {
      lineOne: 'input[id$="solicitorSolicitorAddress__detailAddressLine1"]',
      lineTwo: 'input[id$="solicitorSolicitorAddress__detailAddressLine2"]',
      lineThree: 'input[id$="solicitorSolicitorAddress__detailAddressLine3"]',
    },
    town: 'input[id$="solicitorSolicitorAddress__detailPostTown"]',
    county: 'input[id$="County"]',
    postcode: 'input[id$="solicitorSolicitorAddress__detailPostCode"]',
    country: 'input[id$="solicitorSolicitorAddress__detailCountry"]',
  },
  findAddressButton: 'Find address',

  lookupPostcode(address) {
    I.fillField(this.fields.postcodeLookup, address.postcode);
    I.click(this.findAddressButton);
    I.waitForText('addresses found');
    I.waitForElement(locate(this.fields.addressList).find('option').withText(address.lookupOption));
    I.selectOption(this.fields.addressList, address.lookupOption);
    I.waitForValue(this.fields.buildingAndStreet.lineOne, address.buildingAndStreet.lineOne);
    I.waitForValue(this.fields.buildingAndStreet.lineTwo, address.buildingAndStreet.lineTwo);
    I.waitForValue(this.fields.buildingAndStreet.lineThree, address.buildingAndStreet.lineThree);
    I.waitForValue(this.fields.town, address.town);
    I.waitForValue(this.fields.postcode, address.postcode);
    I.waitForValue(this.fields.country, address.country);
  },
};
