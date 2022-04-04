package uk.gov.hmcts.reform.adoption;

import net.serenitybdd.junit5.SerenityJUnit5Extension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SerenityJUnit5Extension.class)
class DemoUnitTest {

    @Test
    void exampleOfTest() {
        assertTrue(System.currentTimeMillis() > 0, "Example of Unit Test");
    }
}
