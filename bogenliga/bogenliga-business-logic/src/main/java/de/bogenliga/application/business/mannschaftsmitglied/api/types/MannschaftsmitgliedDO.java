package de.bogenliga.application.business.mannschaftsmitglied.api.types;

import java.sql.Date;
import java.time.OffsetDateTime;
import java.util.Objects;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class MannschaftsmitgliedDO extends CommonDataObject implements DataObject {
    private static final long serialVersionUID =123;



    /**
     * business parameter
     */
    private Long mannschaftId;
    private Long mitgliedId;
    private boolean mitgliedEingesetzt;



    /**
     * Constructor with optional parameters
     * @param mannschaftId
     * @param mitgliedId
     * @param mitgliedEingesetzt
     * @param createdAtUtc
     * @param createdByUserId
     * @param lastModifiedAtUtc
     * @param lastModifiedByUserId
     * @param version
     */

    public MannschaftsmitgliedDO(final Long mannschaftId, final Long mitgliedId, final boolean mitgliedEingesetzt, final OffsetDateTime createdAtUtc,
                         final Long createdByUserId, final OffsetDateTime lastModifiedAtUtc,
                         final Long lastModifiedByUserId, final Long version) {
        this.mannschaftId = mannschaftId;
        this.mitgliedId = mitgliedId;
        this.mitgliedEingesetzt = mitgliedEingesetzt;
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.lastModifiedAtUtc = lastModifiedAtUtc;
        this.lastModifiedByUserId = lastModifiedByUserId;
        this.version = version;
    }


    /**
     * Constructor with mandatory parameters
     * @param mannschaftId
     * @param mitgliedId
     *
     * @param createdAtUtc
     * @param createdByUserId
     * @param version
     */


    public MannschaftsmitgliedDO(final Long mannschaftId, final Long mitgliedId, final OffsetDateTime createdAtUtc,
                                 final Long createdByUserId, final Long version) {
        this.mannschaftId = mannschaftId;
        this.mitgliedId = mitgliedId;
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.version = version;
    }

    /**
     * Constructor without technical parameters
     * @param mannschaftId
     * @param mitgliedId
     * @param mitgliedEingesetzt
     */

    public MannschaftsmitgliedDO(final Long mannschaftId, final Long mitgliedId, final boolean mitgliedEingesetzt) {
        this.mannschaftId = mannschaftId;
        this.mitgliedId = mitgliedId;
        this.mitgliedEingesetzt = mitgliedEingesetzt;

    }



    public Long getMannschaftId() {
        return mannschaftId;
    }

    public void setMannschaftId(Long mannschaftId) {
        this.mannschaftId = mannschaftId;
    }

    public Long getMitgliedId() {
        return mitgliedId;
    }

    public void setMitgliedId(Long mitgliedId) {
        this.mitgliedId = mitgliedId;
    }


    public boolean isMitgliedEingesetzt() {
        return mitgliedEingesetzt;
    }


    public void setMitgliedEingesetzt(boolean mitgliedEingesetzt) {
        this.mitgliedEingesetzt = mitgliedEingesetzt;
    }


    @Override
    public int hashCode() {
        return Objects.hash(mannschaftId, mitgliedId, mitgliedId,
                createdByUserId, lastModifiedAtUtc,
                lastModifiedByUserId, version);
    }



    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MannschaftsmitgliedDO that = (MannschaftsmitgliedDO) o;
        return mannschaftId == that.mannschaftId &&
                mitgliedId == that.mitgliedId &&
                mitgliedEingesetzt == that.mitgliedEingesetzt &&
                createdByUserId == that.createdByUserId &&
                lastModifiedByUserId == that.lastModifiedByUserId &&
                version == that.version &&
                Objects.equals(mannschaftId, that.mannschaftId) &&
                Objects.equals(mitgliedId, that.mitgliedId) &&
                Objects.equals(mitgliedEingesetzt, that.mitgliedEingesetzt) &&
                Objects.equals(lastModifiedAtUtc, that.lastModifiedAtUtc);
    }


}
