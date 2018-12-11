package de.bogenliga.application.common.component.types;

import java.time.OffsetDateTime;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public abstract class CommonDataObject implements DataObject {

    private static final long serialVersionUID = 9084773078605162633L;
    protected OffsetDateTime createdAtUtc;
    protected Long createdByUserId;

    protected OffsetDateTime lastModifiedAtUtc;
    protected Long lastModifiedByUserId;

    protected Long version;


    public OffsetDateTime getCreatedAtUtc() {
        return createdAtUtc;
    }


    public void setCreatedAtUtc(final OffsetDateTime createdAtUtc) {
        this.createdAtUtc = createdAtUtc;
    }


    public Long getCreatedByUserId() {
        return createdByUserId;
    }


    public void setCreatedByUserId(final Long createdByUserId) {
        this.createdByUserId = createdByUserId;
    }


    public OffsetDateTime getLastModifiedAtUtc() {
        return lastModifiedAtUtc;
    }


    public void setLastModifiedAtUtc(final OffsetDateTime lastModifiedAtUtc) {
        this.lastModifiedAtUtc = lastModifiedAtUtc;
    }


    public Long getLastModifiedByUserId() {
        return lastModifiedByUserId;
    }


    public void setLastModifiedByUserId(final Long lastModifiedByUserId) {
        this.lastModifiedByUserId = lastModifiedByUserId;
    }


    public Long getVersion() {
        return version;
    }


    public void setVersion(final Long version) {
        this.version = version;
    }

}
