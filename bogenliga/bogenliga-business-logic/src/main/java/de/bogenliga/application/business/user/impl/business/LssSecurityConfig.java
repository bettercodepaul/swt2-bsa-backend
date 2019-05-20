package de.bogenliga.application.business.user.impl.business;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity
public class LssSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private CustomWebAuthenticationDetailsSource authenticationDetailsSource;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().authenticationDetailsSource(authenticationDetailsSource);
    }
}
