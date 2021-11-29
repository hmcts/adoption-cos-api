package uk.gov.hmcts.reform.adoption.document.print.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.reform.adoption.document.model.DivorceDocument;

@AllArgsConstructor
@Getter
public class Letter {
    private final DivorceDocument divorceDocument;
    private final int count;
}
