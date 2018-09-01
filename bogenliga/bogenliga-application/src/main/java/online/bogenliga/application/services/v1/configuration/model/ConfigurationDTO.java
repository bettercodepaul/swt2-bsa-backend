package online.bogenliga.application.services.v1.configuration.model;

import online.bogenliga.application.common.service.types.DataTransferObject;

/**
 * I´m the data transfer object of the configuration.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class ConfigurationDTO implements DataTransferObject {
    private String key;
    private String value;


    /**
     * Constructor
     */
    public ConfigurationDTO() {

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
