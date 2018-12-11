package de.bogenliga.application.common.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * I contain the type-safe values of the application properties with the prefix "database."
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
@ConfigurationProperties("database")
public class DatabaseConfiguration {
    private String host;
    private int port;
    private String databaseName;
    private String user;
    private String password;


    /**
     * Getter
     *
     * @return host
     */
    public String getHost() {
        return host;
    }


    /**
     * Setter
     *
     * @param host
     */
    public void setHost(final String host) {
        this.host = host;
    }


    /**
     * Getter
     *
     * @return port
     */
    public int getPort() {
        return port;
    }


    /**
     * Setter
     *
     * @param port
     */
    public void setPort(final int port) {
        this.port = port;
    }


    /**
     * Getter
     *
     * @return databaseName
     */
    public String getDatabaseName() {
        return databaseName;
    }


    /**
     * Setter
     *
     * @param databaseName
     */
    public void setDatabaseName(final String databaseName) {
        this.databaseName = databaseName;
    }


    /**
     * Getter
     *
     * @return user
     */
    public String getUser() {
        return user;
    }


    /**
     * Setter
     *
     * @param user
     */
    public void setUser(final String user) {
        this.user = user;
    }


    /**
     * Getter
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }


    /**
     * Setter
     *
     * @param password
     */
    public void setPassword(final String password) {
        this.password = password;
    }
}