package de.bogenliga.application.springconfiguration.requestfilters.filters;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright 2015 Valerio Vaudi Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
public class CorsFilter implements Filter {
    private static final Logger LOG = LoggerFactory.getLogger(CorsFilter.class);


    private static final String ACCESS_CONTROL_ALLOW_ORIGIN_NAME = "Access-Control-Allow-Origin";
    private static final String DEFAULT_ACCESS_CONTROL_ALLOW_ORIGIN_VALUE = "*";

    private static final String ACCESS_CONTROL_ALLOW_METHDOS_NAME = "Access-Control-Allow-Methods";
    private static final String DEFAULT_ACCESS_CONTROL_ALLOW_METHDOS_VALUE = "POST, PUT, GET, OPTIONS, DELETE";

    private static final String ACCESS_CONTROL_MAX_AGE_NAME = "Access-Control-Max-Age";
    private static final String DEFAULT_ACCESS_CONTROL_MAX_AGE_VALUE = "3600";

    private static final String ACCESS_CONTROL_ALLOW_HEADERS_NAME = "Access-Control-Allow-Headers";
    private static final String DEFAULT_ACCESS_CONTROL_ALLOW_HEADERS_VALUE = "Authorization, Content-Type";


    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        String initParameterValue;
        final Map<String, String> stringStringMap = initConfig();

        for (final Map.Entry<String, String> stringStringEntry : stringStringMap.entrySet()) {
            initParameterValue = filterConfig.getInitParameter(stringStringEntry.getKey());

            // if the init paramiter value isn't null then set the value in the correct http header
            if (initParameterValue != null) {
                try {
                    getClass().getDeclaredField(stringStringEntry.getValue()).set(this, initParameterValue);
                } catch (final IllegalAccessException | NoSuchFieldException e) {
                    LOG.error("CORS filtering failed", e);
                }
            }
        }
    }


    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
                         final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN_NAME, DEFAULT_ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
        response.setHeader(ACCESS_CONTROL_ALLOW_METHDOS_NAME, DEFAULT_ACCESS_CONTROL_ALLOW_METHDOS_VALUE);
        response.setHeader(ACCESS_CONTROL_MAX_AGE_NAME, DEFAULT_ACCESS_CONTROL_MAX_AGE_VALUE);
        response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS_NAME, DEFAULT_ACCESS_CONTROL_ALLOW_HEADERS_VALUE);

        filterChain.doFilter(servletRequest, servletResponse);
    }


    @Override
    public void destroy() {
        // empty
    }


    /**
     * @return the method return a map that associated the name of paramiters in the web.xml to the class variable name
     * for the header binding
     */
    private Map<String, String> initConfig() {
        final Map<String, String> result = new HashMap<>();

        result.put(ACCESS_CONTROL_ALLOW_ORIGIN_NAME, "accessControlAllowOrigin");
        result.put(ACCESS_CONTROL_ALLOW_METHDOS_NAME, "accessControlAllowMethods");
        result.put(ACCESS_CONTROL_MAX_AGE_NAME, "accessControlAllowMaxAge");
        result.put(ACCESS_CONTROL_ALLOW_HEADERS_NAME, "accessControlAllowHeaders");

        return result;
    }

}