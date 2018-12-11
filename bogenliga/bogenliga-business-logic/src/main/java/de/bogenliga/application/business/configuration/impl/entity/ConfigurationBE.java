package de.bogenliga.application.business.configuration.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;

/**
 * I represent a configuration table row as business entity
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class ConfigurationBE implements BusinessEntity {

    private static final long serialVersionUID = 815898607713382219L;
    private String configurationKey;
    private String configurationValue;


    /**
     * Constructor
     */
    public ConfigurationBE() {
        // empty constructor
    }


    /*
     * generated
     */
    @Override
    public String toString() {
        return "ConfigurationBE{" +
                "configurationKey='" + configurationKey + '\'' +
                ", configurationValue='" + configurationValue + '\'' +
                '}';
    }


    public String getConfigurationKey() {
        return configurationKey;
    }


    public void setConfigurationKey(final String configurationKey) {
        this.configurationKey = configurationKey;
    }


    public String getConfigurationValue() {
        return configurationValue;
    }


    public void setConfigurationValue(final String configurationValue) {
        this.configurationValue = configurationValue;
    }
}
