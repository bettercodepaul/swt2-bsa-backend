package de.bogenliga.application.business.user.impl.business;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class CustomWebAuthenticationDetailsSourceTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    private CustomWebAuthenticationDetailsSource underTest;

    @Test
    public void buildDetails(){
        WebAuthenticationDetails actual = underTest.buildDetails(null);

        assertThat(actual).isNull();
    }
}
