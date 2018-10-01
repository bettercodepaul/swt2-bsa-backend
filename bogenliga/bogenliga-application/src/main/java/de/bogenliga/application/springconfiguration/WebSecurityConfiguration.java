package de.bogenliga.application.springconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    private final CustomAuthenticationProvider authProvider;


    @Autowired
    public WebSecurityConfiguration(final JwtTokenProvider jwtTokenProvider,
                                    final CustomAuthenticationProvider authProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authProvider = authProvider;
    }


    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }


    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/v2/hello-world")
                .and()
                .ignoring()
                .antMatchers("/v1/hello-world");
        ;
    }


    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        // Disable CSRF (cross site request forgery)
        http.csrf().disable();

        // No session will be created or used by spring security
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Entry points
        http.authorizeRequests()
                .antMatchers("/v1/user/signin").permitAll()
                // Disallow everything else..
                .anyRequest().authenticated();


        // Apply JWT
        http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));

        // Optional, if you want to test the API from a browser
        // http.httpBasic();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

}
