package de.bogenliga.application.springconfiguration.requestfilters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;


/**
 * I wrap the REST request and response processing.
 *
 * I log the called REST resource and the response code for the request.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
// @Component // activate the filter globally
// The filter is registered by the {@link FilterRegistrationConfiguration} for specific url pattern
@Order(1)
public class RequestResponseLoggingFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);


    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        LOG.info("Initializing filter: {}", this);
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        LOG.debug("Receive {}: {}", req.getMethod(), req.getRequestURI());

        chain.doFilter(request, response);

        LOG.debug("Send response for '{}: {}' with status code '{}'",
                req.getMethod(), req.getRequestURI(), res.getStatus());
    }


    @Override
    public void destroy() {
        LOG.warn("Destructing filter: {}", this);
    }
}
