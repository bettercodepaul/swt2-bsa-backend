package de.bogenliga.application.services.v1.configuration.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * I´m the data transfer object of the configuration.
 * <p>
 * I define the payload for the external REST interface of the configuration business entity.
 *
 * @author Andre Lehnert, BettercallPaul gmbh
 * @see DataTransferObject
 */
public class ConfigurationDTO implements DataTransferObject {
    private Long id;
    private String key;
    private String value;
    private String regex;
    private Boolean hidden;


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

    public ConfigurationDTO(final Long id, final String key, final String value, final String regex,
                            final Boolean hidden) {
        this.id = id;
        this.key = key;
        this.value = value;
        this.regex = regex;
        this.hidden = hidden;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
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


    public String getRegex() {
        return regex;
    }


    public void setRegex(String regex) {
        this.regex = regex;
    }


    public Boolean isHidden() {
        return hidden;
    }


    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }
}
