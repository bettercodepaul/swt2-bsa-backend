//======================================================================================================================
// Module: BMW Remote Software Update (RSU) - Zentrales Fahrzeug Update System (ZFUS)
// Copyright (c) 2018 BMW Group. All rights reserved.
//======================================================================================================================
package app.bogenliga.application.business.configuration.impl.entity;

import app.bogenliga.application.common.component.entity.BusinessEntity;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class ConfigurationBE implements BusinessEntity {

    private String configurationKey;
    private String configurationValue;


    public ConfigurationBE() {

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
