package de.bogenliga.application.springconfiguration.security.jsonwebtoken;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.security.core.Authentication;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class JwtTokenFilterTest {
    private static final String JWT = "jwt";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private HttpServletRequest servletRequest;
    @Mock
    private HttpServletResponse servletResponse;
    @Mock
    private FilterChain filterChain;
    @Mock
    private Authentication authentication;
    @InjectMocks
    private JwtTokenFilter underTest;


    @Test
    public void doFilter_OPTIONS() throws IOException, ServletException {
        // prepare test data

        // configure mocks
        when(servletRequest.getMethod()).thenReturn("OPTIONS");
        when(jwtTokenProvider.getOptionsAuthentication()).thenReturn(authentication);
        when(jwtTokenProvider.refreshToken(any())).thenReturn("refreshed_token");
        when(servletRequest.getHeader(AUTHORIZATION_HEADER)).thenReturn("Bearer " + JWT);

        // call test method
        underTest.doFilter(servletRequest, servletResponse, filterChain);

        // assert result

        // verify invocations
    }


    @Test
    public void doFilter() throws IOException, ServletException {
        // prepare test data

        // configure mocks
        when(servletRequest.getMethod()).thenReturn("GET");
        when(jwtTokenProvider.refreshToken(any())).thenReturn("refreshed_token");
        when(servletRequest.getHeader(AUTHORIZATION_HEADER)).thenReturn("Bearer " + JWT);
        when(jwtTokenProvider.validateToken(any())).thenReturn(true);
        when(jwtTokenProvider.getAuthentication(any())).thenReturn(authentication);

        // call test method
        underTest.doFilter(servletRequest, servletResponse, filterChain);

        // assert result

        // verify invocations
    }
}