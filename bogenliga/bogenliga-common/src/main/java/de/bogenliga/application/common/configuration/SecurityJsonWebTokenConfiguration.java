package de.bogenliga.application.common.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * I contain the type-safe values of the application properties with the prefix "security.jwt."
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
@ConfigurationProperties("security.jwt")
public class SecurityJsonWebTokenConfiguration {

    private static final long DEFAULT_EXPIRATION_TIME = 3600000; // ms
    private static final String DEFAULT_SECRET = "default-secret-key";

    private String secret;
    private long expiration; // ms


    public String getSecret() {
        return secret == null ? DEFAULT_SECRET : secret;
    }


    public void setSecret(final String secret) {
        this.secret = secret;
    }


    public long getExpiration() {
        return expiration <= 0 ? DEFAULT_EXPIRATION_TIME : expiration;
    }


    public void setExpiration(final long expiration) {
        this.expiration = expiration;
    }
}