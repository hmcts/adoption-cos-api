const {I} = inject();
const config = require('../config');

module.exports = {

  fields: {
     placementHeader: '//div[contains(@class, "complex-panel") and contains(.//span, "Placement")]'
  },
   async verifyPlacementTableOptions(){
      console.log('verifyPlacementTableOptions verifyPlacementTableOptions verifyPlacementTableOptions');
      I.seeInTab(['Placement', 'Court'], 'xyz');
      I.seeInTab(['Placement', 'Case or serial number'], '123456');
      I.seeInTab(['Placement', 'Type'], 'Placement order');
    },

    async verifyLinkedCasesTableOptions(){
    console.log('verifyLinkedCasesTableOptions verifyLinkedCasesTableOptions verifyLinkedCasesTableOptions');
      I.seeInTab(['Linked cases 1', 'Court'], 'london court');
      I.seeTextInTab(['Linked cases 1', 'Case or serial number'], '12345');
      I.seeTextInTab(['Linked cases 1', 'Type'], 'Adoption order');
      I.seeInTab(['Linked cases 2', 'Court'], 'london court');
      I.seeTextInTab(['Linked cases 2', 'Case or serial number'], '12345');
      I.seeTextInTab(['Linked cases 2', 'Type'], 'Care order');
    },

    async verifySiblingCourtCaseTableOptions(){
    console.log('verifySiblingCourtCaseTableOptions verifySiblingCourtCaseTableOptions verifySiblingCourtCaseTableOptions');
      I.seeInTab(['Sibling court cases 1', 'Case or serial number'], '12345');
      I.seeTextInTab(['Sibling court cases 1', 'Type'], 'Adoption order');
      I.seeTextInTab(['Sibling court cases 1', 'Relationship'], 'Sister');
      I.seeInTab(['Sibling court cases 2', 'Case or serial number'], '12345');
      I.seeTextInTab(['Sibling court cases 2', 'Type'], 'Care order');
      I.seeTextInTab(['Sibling court cases 2', 'Relationship'], 'Half-sister');
        },
  };
