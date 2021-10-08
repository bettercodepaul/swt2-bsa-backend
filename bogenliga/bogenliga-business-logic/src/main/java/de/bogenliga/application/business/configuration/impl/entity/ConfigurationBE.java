package de.bogenliga.application.business.configuration.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * I represent a configuration table row as business entity
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */

public class ConfigurationBE extends CommonBusinessEntity implements BusinessEntity {
    private static final long serialVersionUID = -76389969048178948L;
    private Long configurationId;
    private String configurationKey;
    private String configurationValue;
    private String configurationRegex;
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
                ", id=" + configurationId + '\'' +
                ", key=" + configurationKey + '\'' +
                ", value=" + configurationValue + '\'' +
                ", regex=" + configurationRegex + '\'' +
                "}";
    }

    public Long getConfigurationId() { return configurationId; }


    public void setConfigurationId(final Long configurationId) {
        this.configurationId = configurationId;
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


    public String getConfigurationRegex() {
        return configurationRegex;
    }


    public void setConfigurationRegex(String configurationRegex) {
        this.configurationRegex = configurationRegex;
    }
}
