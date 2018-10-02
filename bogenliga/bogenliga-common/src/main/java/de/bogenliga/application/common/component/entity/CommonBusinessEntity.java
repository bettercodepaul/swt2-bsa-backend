package de.bogenliga.application.common.component.entity;

import java.sql.Timestamp;

/**
 * Helper class for versioned database entity representations.
 *
 * Versioned business entities are used to detect concurrent modifications.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public abstract class CommonBusinessEntity implements BusinessEntity {

    private static final long serialVersionUID = -3370626232686254535L;

    /*
     * technical parameter
     */
    private Timestamp createdAtUtc;
    private long createdByUserId;

    private Timestamp lastModifiedAtUtc;
    private long lastModifiedByUserId;

    private long version;


    public Timestamp getCreatedAtUtc() {
        return createdAtUtc;
    }


    public void setCreatedAtUtc(final Timestamp createdAtUtc) {
        this.createdAtUtc = createdAtUtc;
    }


    public long getCreatedByUserId() {
        return createdByUserId;
    }


    public void setCreatedByUserId(final long createdByUserId) {
        this.createdByUserId = createdByUserId;
    }


    public Timestamp getLastModifiedAtUtc() {
        return lastModifiedAtUtc;
    }


    public void setLastModifiedAtUtc(final Timestamp lastModifiedAtUtc) {
        this.lastModifiedAtUtc = lastModifiedAtUtc;
    }


    public long getLastModifiedByUserId() {
        return lastModifiedByUserId;
    }


    public void setLastModifiedByUserId(final long lastModifiedByUserId) {
        this.lastModifiedByUserId = lastModifiedByUserId;
    }


    public long getVersion() {
        return version;
    }


    public void setVersion(final long version) {
        this.version = version;
    }
}
