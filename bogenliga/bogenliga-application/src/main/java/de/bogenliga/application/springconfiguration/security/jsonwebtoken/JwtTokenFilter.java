package de.bogenliga.application.springconfiguration.security.jsonwebtoken;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

/**
 * I wrap the http requests and extracts the JSON Web Token (JWT) from the request header.
 * <p>
 * The JWT will be converted into an authentication object to authenticate the request against Spring Security.
 * <p>
 * No database query is necessary to authorize the user.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;


    JwtTokenFilter(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain filterChain)
            throws IOException, ServletException {

        final String token = JwtTokenProvider.resolveToken((HttpServletRequest) req);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            final Authentication auth = jwtTokenProvider.getAuthentication(token);
            // authenticate against Spring Security
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(req, res);
    }

}
