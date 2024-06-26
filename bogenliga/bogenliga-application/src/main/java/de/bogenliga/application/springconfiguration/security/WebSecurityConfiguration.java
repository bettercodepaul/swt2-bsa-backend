package de.bogenliga.application.springconfiguration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import de.bogenliga.application.business.user.impl.business.CustomWebAuthenticationDetailsSource;
import de.bogenliga.application.springconfiguration.security.authentication.UserAuthenticationProvider;
import de.bogenliga.application.springconfiguration.security.jsonwebtoken.JwtTokenFilterConfigurer;
import de.bogenliga.application.springconfiguration.security.jsonwebtoken.JwtTokenProvider;

/**
 * I configure the Spring Security to protect REST resources against unauthorized requests.
 *
 * @author Andre Lehnert, BettercallPaul gmbh
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserAuthenticationProvider authProvider;


    @Autowired
    private CustomWebAuthenticationDetailsSource authenticationDetailsSource;

    @Autowired
    public WebSecurityConfiguration(JwtTokenProvider jwtTokenProvider,
                                    UserAuthenticationProvider authProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authProvider = authProvider;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider);
    }


    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    public void configure(WebSecurity web) {
        // allow unauthorized access to...
        web.ignoring()
                //...example endpoints
                .antMatchers("/v2/hello-world")
                .antMatchers("/v1/hello-world")
                //... swagger endpoints
                .antMatchers("/swagger-ui.html")
                .antMatchers("/swagger-ui/**")
                .antMatchers("/swagger-resources/**")
                .antMatchers("/v2/api-docs")
                .antMatchers("/webjars/**")
                .antMatchers(HttpMethod.POST, "/v1/signin")
                .antMatchers(HttpMethod.POST, "/v1/user/signin");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Disable CSRF (cross site request forgery)
        http.csrf().disable();

        // No session will be created or used by spring security
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Apply JWT
        http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));

        // Entry points
        http.authorizeRequests()
                .antMatchers("/v1/user/signin").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                //.antMatchers("/v1/*").permitAll() // TODO allow all in pupose of the failing angular application
                //.antMatchers("/v1/**").permitAll() // TODO allow all in pupose of the failing angular application
                //.antMatchers("/v1/configuration/*").permitAll() // TODO allow all in pupose of the failing angular application
                // Disallow everything else...
                .anyRequest().authenticated();
    }
}
