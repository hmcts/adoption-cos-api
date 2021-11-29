package uk.gov.hmcts.reform.adoption.adoptioncase.tab;

import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;

import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public final class TabShowCondition {

    private TabShowCondition() {
    }

    public static String andNotShowForState(final State... state) {
        return Stream.of(state)
            .map(State::getName)
            .collect(joining("\" AND [STATE]!=\"", "[STATE]!=\"", "\""));
    }
}
