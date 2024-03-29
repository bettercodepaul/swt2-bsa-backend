package de.bogenliga.application.business.configuration.api.types;

import java.time.OffsetDateTime;
import java.util.Objects;
import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

/**
 * I contain the data of the configuration business entity.
 *
 * @author Andre Lehnert, BettercallPaul gmbh
 * @see DataObject
 */
public class ConfigurationDO extends CommonDataObject implements DataObject {
    private Long id;
    private String key;
    private String value;
    private String regex;
    private Boolean hidden;


    /**
     * Constructor
     */
    public ConfigurationDO() {
        // empty constructor
    }


    public ConfigurationDO(final Long id, final String key, final String value, final String regex,
                           final OffsetDateTime createdAtUtc,
                           final Long createdByUserId, final OffsetDateTime lastModifiedAtUtc,
                           final Long lastModifiedByUserId, final Long version, final Boolean hidden) {
        this.id = id;
        this.key = key;
        this.value = value;
        this.regex = regex;
        this.hidden = hidden;

        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.lastModifiedAtUtc = lastModifiedAtUtc;
        this.lastModifiedByUserId = lastModifiedByUserId;
        this.version = version;
    }


    public ConfigurationDO(final Long id, final String key, final String value, final String regex,
                           final Boolean hidden) {
        this.id = id;
        this.key = key;
        this.value = value;
        this.regex = regex;
        this.hidden = hidden;
    }


    public ConfigurationDO(final String key, final String value, final Boolean hidden) {
        this.key = key;
        this.value = value;
        this.hidden = hidden;
    }


    public Long getId() {
        return this.id;
    }


    public void setId(final Long id) {
        this.id = id;
    }


    public String getKey() {
        return this.key;
    }


    public void setKey(final String key) {
        this.key = key;
    }


    public String getValue() {
        return this.value;
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


    @Override
    public int hashCode() {
        return Objects.hash(getKey(), getValue());
    }


    public Boolean isHidden() {
        return hidden;
    }


    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfigurationDO)) {
            return false;
        }
        final ConfigurationDO configurationDO = (ConfigurationDO) o;
        return Objects.equals(getId(), configurationDO.getId()) &&
                Objects.equals(getKey(), configurationDO.getKey()) &&
                Objects.equals(getValue(), configurationDO.getValue()) &&
                Objects.equals(getRegex(), configurationDO.getRegex()) &&
                Objects.equals(isHidden(), configurationDO.isHidden());
    }


}
