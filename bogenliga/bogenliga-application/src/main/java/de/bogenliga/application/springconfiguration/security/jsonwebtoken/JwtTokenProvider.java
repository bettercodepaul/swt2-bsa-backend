package de.bogenliga.application.springconfiguration.security.jsonwebtoken;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import de.bogenliga.application.common.configuration.SecurityJsonWebTokenConfiguration;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import de.bogenliga.application.springconfiguration.security.authentication.UserAuthenticationProvider;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * I handle all JSON Web Token processing steps.
 * <p>
 * I extract the JWT from the request header and decrypt the token. The token contains the user information and
 * permissions.
 * <p>
 * The permissions can be checked with the {@link RequiresPermission} annotation.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class JwtTokenProvider {

    private static final Logger LOG = LoggerFactory.getLogger(JwtTokenProvider.class);

    private static final String AUTH = "auth";
    private final SecurityJsonWebTokenConfiguration securityJsonWebTokenConfiguration;
    private final UserAuthenticationProvider userAuthenticationProvider;

    // defined in resources/application-<PROFILE>.properties
    private long validityInMilliseconds;
    private String secretKey;


    @Autowired
    public JwtTokenProvider(
            final SecurityJsonWebTokenConfiguration securityJsonWebTokenConfiguration,
            final UserAuthenticationProvider userAuthenticationProvider) {
        this.securityJsonWebTokenConfiguration = securityJsonWebTokenConfiguration;
        this.userAuthenticationProvider = userAuthenticationProvider;
    }


    public static String resolveToken(final HttpServletRequest req) {
        final String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }


    public String createToken(final Authentication authentication) {
        final String username = (String) authentication.getPrincipal();
        final List<UserPermission> permissions = authentication.getAuthorities().stream()
                .map(authority -> (UserPermission) authority)
                .collect(Collectors.toList());

        return createToken(username, permissions);
    }


    public Authentication getAuthentication(final String token) {
        return userAuthenticationProvider.createAuthenticationPlaceholder(getUsername(token), getPermissions(token));
    }


    public String getUsername(final String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(
                token).getBody().getSubject();
    }


    public Set<UserPermission> getPermissions(final String token) {
        final Object permissions = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get(AUTH);

        Set<UserPermission> userPermissions = new HashSet<>();

        try {
            if (permissions instanceof ArrayList) {
                final List<String> permissionStringList = (ArrayList<String>) permissions;
                userPermissions = permissionStringList.stream()
                        .map(UserPermission::fromValue)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());
            }
        } catch (final ClassCastException e) {
            throw new TechnicalException(ErrorCode.UNEXPECTED_ERROR, "Permissions could not parsed from JWT", e);
        }

        return userPermissions;
    }


    public boolean validateToken(final String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (final JwtException | IllegalArgumentException e) {
            LOG.warn("Received invalid JWT token with error message: {}", e.getMessage());
            // send 403 "Forbidden"
            return false;
        }
    }


    private String createToken(final String username, final List<UserPermission> roles) {

        final Claims claims = Jwts.claims().setSubject(username);
        claims.put(AUTH, roles.stream()
                .map(UserPermission::getAuthority)
                .collect(Collectors.toList()));

        final Date now = new Date();
        final Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }


    @PostConstruct
    protected void init() {
        validityInMilliseconds = securityJsonWebTokenConfiguration.getExpiration();
        secretKey = Base64.getEncoder().encodeToString(securityJsonWebTokenConfiguration.getSecret().getBytes());
    }


}
