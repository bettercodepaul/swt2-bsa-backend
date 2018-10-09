package de.bogenliga.application.springconfiguration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import de.bogenliga.application.springconfiguration.security.authentication.UserAuthenticationProvider;
import de.bogenliga.application.springconfiguration.security.jsonwebtoken.JwtTokenFilterConfigurer;
import de.bogenliga.application.springconfiguration.security.jsonwebtoken.JwtTokenProvider;

/**
 * I configure the Spring Security to protect REST resources against unauthorized requests.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserAuthenticationProvider authProvider;


    @Autowired
    public WebSecurityConfiguration(final JwtTokenProvider jwtTokenProvider,
                                    final UserAuthenticationProvider authProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authProvider = authProvider;
    }


    /*
    // TODO fix CORS
     * I configure the Cross-Origin Resource Sharing (CORS) mechanism.
     *
     * @see <a href="https://de.wikipedia.org/wiki/Cross-Origin_Resource_Sharing">CORS</a>

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(
                Arrays.asList("x-auth-token", "Authorization", "Cache-Control", "Content-Type"));
        configuration.setExposedHeaders(Collections.singletonList("x-auth-token"));
        configuration.setAllowCredentials(true);
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
     */

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider);
    }


    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    public void configure(final WebSecurity web) {
        // allow unauthorized access to
        web.ignoring()
                .antMatchers("/v2/hello-world")
                .and()
                .ignoring()
                .antMatchers("/v1/hello-world");
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
                .antMatchers("/v1/*").permitAll() // TODO allow all in pupose of the failing angular application
                .antMatchers("/v1/configuration/*").permitAll() // TODO allow all in pupose of the failing angular application
                // Disallow everything else...
                .anyRequest().authenticated();

        // Apply JWT
        http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));
    }
}
