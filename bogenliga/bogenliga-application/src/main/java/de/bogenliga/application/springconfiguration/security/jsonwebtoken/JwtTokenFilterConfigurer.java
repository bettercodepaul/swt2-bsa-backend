package de.bogenliga.application.springconfiguration.security.jsonwebtoken;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * I configure the JSON Web Token Filter to be executed before the build-in Spring Security filter
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class JwtTokenFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JwtTokenProvider jwtTokenProvider;


    /**
     * Constructor
     *
     * @param jwtTokenProvider will be used by the Filter (without dependency injection)
     */
    public JwtTokenFilterConfigurer(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    public void configure(final HttpSecurity http) throws Exception {
        final JwtTokenFilter customFilter = new JwtTokenFilter(jwtTokenProvider);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
