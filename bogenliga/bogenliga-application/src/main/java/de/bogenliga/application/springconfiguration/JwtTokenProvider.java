package de.bogenliga.application.springconfiguration;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.services.v1.user.model.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

    private static final String AUTH = "auth";


    @Value("${security.jwt.token.expire-length:259200000}")
    private final long validityInMilliseconds = 259200000; // 3 days
    /**
     * THIS IS NOT A SECURE PRACTICE! For simplicity, we are storing a static key here. Ideally, in a
     * microservices environment, this key would be kept on a config-server.
     */
    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;


    private static UserDetails loadUserByUsername(final String username) {

        return org.springframework.security.core.userdetails.User//
                .withUsername(username)//
                .password("password")//
                .authorities(Collections.singletonList(UserRole.ROLE_ADMIN))//
                .accountExpired(false)//
                .accountLocked(false)//
                .credentialsExpired(false)//
                .disabled(false)//
                .build();
    }


    public static String resolveToken(final HttpServletRequest req) {
        final String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }


    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }


    public String createToken(final Authentication authentication) {
        final String username = (String) authentication.getPrincipal();
        final List<UserRole> roles = authentication.getAuthorities().stream()
                .map(authority -> (UserRole) authority).collect(
                        Collectors.toList());

        return createToken(username, roles);
    }


    public String createToken(final String username, final List<UserRole> roles) {

        final Claims claims = Jwts.claims().setSubject(username);
        claims.put(AUTH, roles.stream()
                .map(UserRole::name)
                .collect(Collectors.toList()));

        final Date now = new Date();
        final Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)//
                .signWith(SignatureAlgorithm.HS256, secretKey)//
                .compact();
    }


    public Authentication getAuthentication(final String token) {
        final UserDetails userDetails = loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


    public String getUsername(final String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }


    public List<UserRole> getRoles(final String token) {
        final Object roles = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get(AUTH);

        List<UserRole> userRoles = new ArrayList<>();

        if (roles instanceof ArrayList) {
            final List<String> roleStrings = (ArrayList<String>) roles;
            userRoles = roleStrings.stream().map(UserRole::fromValue).collect(Collectors.toList());
        }

        return userRoles;
    }


    public boolean validateToken(final String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (final JwtException | IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.NO_SESSION_ERROR, "Expired or invalid JWT token");
        }
    }

}
