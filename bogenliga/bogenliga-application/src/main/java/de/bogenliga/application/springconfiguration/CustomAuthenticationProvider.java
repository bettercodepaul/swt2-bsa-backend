package de.bogenliga.application.springconfiguration;

import java.util.Arrays;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import de.bogenliga.application.services.v1.user.model.UserRole;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="https://www.baeldung.com/spring-security-authentication-provider">
 * Spring Security Authentication Provider</a>
 */
@Component
public class CustomAuthenticationProvider
        implements AuthenticationProvider {
    private static final Logger LOG = LoggerFactory.getLogger(CustomAuthenticationProvider.class);


    private static boolean shouldAuthenticateAgainstDatabase(final String name, final String password) {
        // TODO implement check
        return true;
    }


    @Override
    public Authentication authenticate(final Authentication authentication)
            throws AuthenticationException {

        final String name = authentication.getName();
        final String password = authentication.getCredentials().toString();

        LOG.info("Authenticate user {} with password {}", name, password);

        if (shouldAuthenticateAgainstDatabase(name, password)) {

            if (name.equals("ADMIN")) {
                LOG.info("AUTH AS ADMIN");

                // use the credentials
                // and authenticate against the third-party system
                return new UsernamePasswordAuthenticationToken(
                        name, password, Arrays.asList(UserRole.ROLE_ADMIN, UserRole.ROLE_USER));
            } else {
                LOG.info("AUTH AS USER");

                // use the credentials
                // and authenticate against the third-party system
                return new UsernamePasswordAuthenticationToken(
                        name, password, Collections.singleton(UserRole.ROLE_USER));
            }
        } else {
            return null;
        }
    }


    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(
                UsernamePasswordAuthenticationToken.class);
    }
}
