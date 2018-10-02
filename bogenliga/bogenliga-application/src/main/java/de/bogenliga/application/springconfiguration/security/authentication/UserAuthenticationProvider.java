package de.bogenliga.application.springconfiguration.security.authentication;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.user.api.UserComponent;
import de.bogenliga.application.business.user.api.types.UserWithPermissionsDO;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="https://www.baeldung.com/spring-security-authentication-provider">
 * Spring Security Authentication Provider</a>
 */
@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOG = LoggerFactory.getLogger(UserAuthenticationProvider.class);

    private final UserComponent userComponent;


    @Autowired
    public UserAuthenticationProvider(final UserComponent userComponent) {
        this.userComponent = userComponent;
    }


    @Override
    public Authentication authenticate(final Authentication authentication)
            throws AuthenticationException {

        final String username = authentication.getName();
        final String password = authentication.getCredentials().toString();
        final List<UserPermission> permissions;

        try {
            final UserWithPermissionsDO userDO = userComponent.signIn(username, password);

            LOG.info("Signin with user: {}", userDO.toString());

            permissions = userDO.getPermissions().stream().map(UserPermission::fromValue).collect(
                    Collectors.toList());

            return new UsernamePasswordAuthenticationToken(
                    userDO.getEmail(), "", permissions);

        } catch (final RuntimeException e) { // NOSONAR
            LOG.warn("An unexpected error occured", e);
            return null;
        }
    }


    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }


    /**
     * I create an authentication token for Spring Security based on the JWT token from the request.
     * <p>
     * No database query is necessary.
     *
     * @param username        from the JWT token
     * @param userPermissions from the JWT token
     *
     * @return authentication token for Spring Security
     */
    public UsernamePasswordAuthenticationToken createAuthenticationPlaceholder(final String username,
                                                                               final Set<UserPermission> userPermissions) {
        LOG.trace("Create placeholder for authentication. Username = {}", username);

        return new UsernamePasswordAuthenticationToken(username, "", userPermissions);
    }
}
