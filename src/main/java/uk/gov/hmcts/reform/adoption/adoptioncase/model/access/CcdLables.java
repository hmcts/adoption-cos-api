package uk.gov.hmcts.reform.adoption.adoptioncase.model.access;

import uk.gov.hmcts.ccd.sdk.api.CCD;


public class CcdLables {

    @CCD(
        access = {DefaultAccess.class},
        label = "### Add new hearing"
    )
    public String addNewHearing3 = "### Add new hearing";
}
