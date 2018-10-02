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
    protected long createdByUserId;

    protected OffsetDateTime lastModifiedAtUtc;
    protected long lastModifiedByUserId;

    protected long version;


    public OffsetDateTime getCreatedAtUtc() {
        return createdAtUtc;
    }


    public void setCreatedAtUtc(final OffsetDateTime createdAtUtc) {
        this.createdAtUtc = createdAtUtc;
    }


    public long getCreatedByUserId() {
        return createdByUserId;
    }


    public void setCreatedByUserId(final long createdByUserId) {
        this.createdByUserId = createdByUserId;
    }


    public OffsetDateTime getLastModifiedAtUtc() {
        return lastModifiedAtUtc;
    }


    public void setLastModifiedAtUtc(final OffsetDateTime lastModifiedAtUtc) {
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
