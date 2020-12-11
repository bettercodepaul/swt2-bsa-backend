package de.bogenliga.application.business.einstellungen.api.types;

import java.time.OffsetDateTime;
import java.util.Objects;
import de.bogenliga.application.business.role.api.types.RoleDO;
import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

/**
 * TODO [AL] class documentation
 *
 * @author Fabio Care, fabio_silas.care@student.reutlingen-university.de
 */
public class EinstellungenDO extends CommonDataObject implements DataObject {

    /**
     * business parameter
     */
    private String id;
    private String key;
    private String value;


    /**
     * Constructor with optional parameters
     */
    public EinstellungenDO() {
        // empty constructor
    }

    /**
     * Constructor with mandatory parameters
     */
    public EinstellungenDO(final String id,final String key, final String value, final OffsetDateTime createdAtUtc,
                           final Long createdByUserId, final OffsetDateTime lastModifiedAtUtc,
                           final Long lastModifiedByUserId, final Long version) {
        this.id=id;
        this.key = key;
        this.value = value;

        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.lastModifiedAtUtc = lastModifiedAtUtc;
        this.lastModifiedByUserId = lastModifiedByUserId;
        this.version = version;
    }





    public EinstellungenDO(final String id, final String key, final String value) {
        this.id=id;
        this.key = key;
        this.value = value;

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), getValue());
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoleDO)) {
            return false;
        }
        final EinstellungenDO einstellungenDO = (EinstellungenDO) o;
        return  Objects.equals(getKey(), einstellungenDO.getKey()) &&
                Objects.equals(getValue(), einstellungenDO.getValue()) &&
                Objects.equals(getId(), einstellungenDO.getId());
    }


}
