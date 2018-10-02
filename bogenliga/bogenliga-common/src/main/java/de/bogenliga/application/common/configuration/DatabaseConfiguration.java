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


    public String getHost() {
        return host;
    }


    public void setHost(final String host) {
        this.host = host;
    }


    public int getPort() {
        return port;
    }


    public void setPort(final int port) {
        this.port = port;
    }


    public String getDatabaseName() {
        return databaseName;
    }


    public void setDatabaseName(final String databaseName) {
        this.databaseName = databaseName;
    }


    public String getUser() {
        return user;
    }


    public void setUser(final String user) {
        this.user = user;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(final String password) {
        this.password = password;
    }
}