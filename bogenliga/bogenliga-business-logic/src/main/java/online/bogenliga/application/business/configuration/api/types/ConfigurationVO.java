package online.bogenliga.application.business.configuration.api.types;

import online.bogenliga.application.common.component.types.ValueObject;

/**
 * I contain the values of the configuration business entity.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see ValueObject
 */
public class ConfigurationVO implements ValueObject {
    private String key;
    private String value;


    /**
     * Constructor
     */
    public ConfigurationVO() {
        // empty constructor
    }


    public ConfigurationVO(final String key, final String value) {
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
