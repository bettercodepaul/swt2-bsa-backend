package online.bogenliga.application.services.v1.configuration.model;

import online.bogenliga.application.common.service.types.DataTransferObject;

/**
 * I´m the data transfer object of the configuration.
 *
 * I define the payload for the external REST interface of the configuration business entity.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see DataTransferObject
 */
public class ConfigurationDTO implements DataTransferObject {
    private String key;
    private String value;


    /**
     * Constructor
     */
    public ConfigurationDTO() {
        // empty constructor
    }


    /**
     * Constructor with required fields
     *
     * @param key   of the key-value-pair
     * @param value of the key-value-pair
     */
    public ConfigurationDTO(final String key, final String value) {
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
