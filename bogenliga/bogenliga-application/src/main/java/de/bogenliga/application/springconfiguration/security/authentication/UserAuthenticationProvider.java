package de.bogenliga.application.springconfiguration.security.authentication;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
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


    @Override
    public Authentication authenticate(final Authentication authentication)
            throws AuthenticationException {

        final String username = authentication.getName();
        final String password = authentication.getCredentials().toString();

        // TODO remove static code
        if (shouldAuthenticateAgainstDatabase(username, password)) {

            final List<UserPermission> permissions;
            if (username.equals("ADMIN")) {
                permissions = Arrays.asList(
                        UserPermission.CAN_MODIFY_STAMMDATEN,
                        UserPermission.CAN_MODIFY_WETTKAMPF,
                        UserPermission.CAN_MODIFY_WETTKAMPF,
                        UserPermission.CAN_MODIFY_SYSTEMDATEN,

                        UserPermission.CAN_READ_STAMMDATEN,
                        UserPermission.CAN_READ_WETTKAMPF,
                        UserPermission.CAN_READ_SPORTJAHR,
                        UserPermission.CAN_READ_SYSTEMDATEN
                );
            } else {
                permissions = Arrays.asList(
                        UserPermission.CAN_READ_STAMMDATEN,
                        UserPermission.CAN_READ_WETTKAMPF,
                        UserPermission.CAN_READ_SPORTJAHR,
                        UserPermission.CAN_READ_SYSTEMDATEN
                );
            }

            LOG.info("Authenticate user {} with password {} and permissions [{}]", username, password, permissions);


            // TODO get UserDO as principal

            return new UsernamePasswordAuthenticationToken(
                    username, password, permissions);
        } else {
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


    private boolean shouldAuthenticateAgainstDatabase(final String email, final String password) {
        // TODO implement check


        // TODO return UserDO
        return true;
    }
}
