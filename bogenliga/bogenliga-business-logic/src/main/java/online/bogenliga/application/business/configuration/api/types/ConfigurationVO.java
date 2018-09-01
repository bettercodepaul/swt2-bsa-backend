package online.bogenliga.application.business.configuration.api.types;

import online.bogenliga.application.common.component.types.ValueObject;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class ConfigurationVO implements ValueObject {

    private String key;
    private String value;


    public ConfigurationVO() {

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
