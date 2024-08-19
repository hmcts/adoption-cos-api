import AxeBuilder from '@axe-core/playwright';
import { TestInfo } from '@playwright/test';

import { expect } from '../fixtures/fixtures.ts';

type MakeAxeBuilder = () => AxeBuilder;
export async function attachTestInfo(testInfo: TestInfo, data: object): Promise<void> {
  await testInfo.attach('accessibility-scan-results', {
    body: JSON.stringify(data, null, 2),
    contentType: 'application/json',
  });
}
export async function runAccessibilityScan(makeAxeBuilder: MakeAxeBuilder, testInfo: TestInfo): Promise<void> {
  const accessibilityScanResults = await makeAxeBuilder()
    // .disableRules(['aria-allowed-attr', 'target-size'])
    .withTags(['wcag2a', 'wcag2aa', 'wcag21a', 'wcag21aa', 'wcag22a', 'wcag22aa'])
    .analyze();
  await attachTestInfo(testInfo, accessibilityScanResults);
  expect(accessibilityScanResults.violations).toEqual([]);
}
export { expect } from '@playwright/test';
