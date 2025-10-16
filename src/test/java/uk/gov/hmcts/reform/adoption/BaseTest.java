package uk.gov.hmcts.reform.adoption;

import org.junit.runner.RunWith;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.adoption.adoptioncase.Adoption;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Adoption.class)
@TestPropertySource(locations = "/application-contract.properties")
public abstract class BaseTest {
    @MockitoBean
    protected AuthTokenGenerator authTokenGenerator;
}
