package de.bogenliga.application.springconfiguration.requestfilters;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import de.bogenliga.application.springconfiguration.requestfilters.filters.CorsFilter;
import de.bogenliga.application.springconfiguration.requestfilters.filters.RequestResponseLoggingFilter;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Configuration
public class FilterRegistrationConfiguration {

    // uncomment this and comment the @Component in the filter class definition to register only for a url pattern
    @Bean
    public FilterRegistrationBean<RequestResponseLoggingFilter> loggingFilter() {
        final FilterRegistrationBean<RequestResponseLoggingFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new RequestResponseLoggingFilter());

        registrationBean.addUrlPatterns("/*");

        return registrationBean;

    }


    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        final FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new CorsFilter());

        registrationBean.addUrlPatterns("/*");

        return registrationBean;

    }
}