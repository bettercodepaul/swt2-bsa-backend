package de.bogenliga.application.business.configuration.api.types;

import de.bogenliga.application.common.component.types.DataObject;

/**
 * I contain the data of the configuration business entity.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see DataObject
 */
public class ConfigurationDO implements DataObject {
    private String key;
    private String value;


    /**
     * Constructor
     */
    public ConfigurationDO() {
        // empty constructor
    }


    public ConfigurationDO(final String key, final String value) {
        this.key = key;
        this.value = value;
    }


    public String getKey() {
        return key;
    }


    public void setKey(final String key) {
        this.key = key;
    }


    public String getValue() {
        return value;
    }


    public void setValue(final String value) {
        this.value = value;
    }
}
