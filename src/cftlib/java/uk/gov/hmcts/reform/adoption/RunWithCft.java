package uk.gov.hmcts.reform.adoption;

import uk.gov.hmcts.rse.ccd.lib.api.LibRunner;

@SuppressWarnings("HideUtilityClassConstructor") // Spring needs a constructor, its not a utility class
public class RunWithCft {

    public static void main(final String[] args) {
        LibRunner.run(Application.class);
    }
}
