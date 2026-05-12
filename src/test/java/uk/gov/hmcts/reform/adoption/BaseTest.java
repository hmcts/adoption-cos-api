package uk.gov.hmcts.reform.adoption;

import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import uk.gov.hmcts.reform.adoption.adoptioncase.Adoption;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;

@SpringJUnitConfig(classes = Adoption.class)
@TestPropertySource(locations = "/application-contract.properties")
public abstract class BaseTest {
    @MockitoBean
    protected AuthTokenGenerator authTokenGenerator;
}
