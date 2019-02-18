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

    static final long DEFAULT_EXPIRATION_TIME = 3600000; // ms
    static final String DEFAULT_SECRET = "default-secret-key";
    static final int DEFAULT_REFRESH_COUNT = 3; // x times

    private String secret;
    private long expiration; // ms
    private int refresh;


    /**
     * Getter
     *
     * @return secret
     */
    public String getSecret() {
        return secret == null ? DEFAULT_SECRET : secret;
    }


    /**
     * Setter
     *
     * @param secret
     */
    public void setSecret(final String secret) {
        this.secret = secret;
    }


    /**
     * Getter
     *
     * @return expiration
     */
    public long getExpiration() {
        return expiration <= 0 ? DEFAULT_EXPIRATION_TIME : expiration;
    }


    /**
     * Setter
     *
     * @param expiration
     */
    public void setExpiration(final long expiration) {
        this.expiration = expiration;
    }


    /**
     * Getter
     *
     * @return refresh
     */
    public int getRefresh() {
        return refresh <= 0 ? DEFAULT_REFRESH_COUNT : refresh;
    }


    /**
     * Setter
     *
     * @param refresh
     */
    public void setRefresh(final int refresh) {
        this.refresh = refresh;
    }
}