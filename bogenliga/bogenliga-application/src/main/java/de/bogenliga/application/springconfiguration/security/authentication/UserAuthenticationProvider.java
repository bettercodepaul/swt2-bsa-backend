package de.bogenliga.application.springconfiguration.security.authentication;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.user.api.UserComponent;
import de.bogenliga.application.business.user.api.types.UserWithPermissionsDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.services.common.errorhandling.ErrorDTO;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * I´m a custom authentication provider for Spring Security.
 *
 * I authenticate the user credentials with the persisted user information from the database.
 *
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
    public Authentication authenticate(final Authentication authentication) {

        final String username = authentication.getName();
        final String password = authentication.getCredentials().toString();
        final List<UserPermission> permissions;

        ErrorDTO errorDTO = null;
        try {
            final UserWithPermissionsDO userDO = userComponent.signIn(username, password);

            if (userDO != null) {

                permissions = userDO.getPermissions().stream()
                        .map(UserPermission::fromValue)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                return new UsernamePasswordAuthenticationToken(
                        userDO, "", permissions);
            }
        } catch (final BusinessException e) {
            errorDTO = new ErrorDTO(e.getErrorCode(), e.getParameters(), e.getMessage());
        } catch (final RuntimeException e) { // NOSONAR
            LOG.warn("An unexpected error occured", e);
            // null will be returned
            errorDTO = new ErrorDTO(ErrorCode.UNEXPECTED_ERROR, e.getMessage());
        }

        LOG.debug("No auth. Return not authenticated");
        final UsernamePasswordAuthenticationToken invalidAuth = new UsernamePasswordAuthenticationToken(null, null);
        invalidAuth.setAuthenticated(false);
        invalidAuth.setDetails(errorDTO);
        return invalidAuth;
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

        return new UsernamePasswordAuthenticationToken(0, "", userPermissions);
    }
}
