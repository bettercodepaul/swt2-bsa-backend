package de.bogenliga.application.business.mannschaftsmitglied.api.types;


import java.time.OffsetDateTime;
import java.util.Objects;
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
    private Long dsbMitgliedId;
    private boolean dsbMitgliedEingesetzt;
    private String dsbMitgliedVorname;
    private String dsbMitgliedNachname;



    /**
     * Constructor with optional parameters
     * @param mannschaftId
     * @param dsbMitgliedId
     * @param dsbMitgliedEingesetzt
     * @param createdAtUtc
     * @param createdByUserId
     * @param lastModifiedAtUtc
     * @param lastModifiedByUserId
     * @param version
     */

    public MannschaftsmitgliedDO(final Long mannschaftId, final Long dsbMitgliedId, final boolean dsbMitgliedEingesetzt,final String dsbMitgliedVorname,final String dsbMitgliedNachname, final OffsetDateTime createdAtUtc,
                         final Long createdByUserId, final OffsetDateTime lastModifiedAtUtc,
                         final Long lastModifiedByUserId, final Long version) {
        this.mannschaftId = mannschaftId;
        this.dsbMitgliedId = dsbMitgliedId;
        this.dsbMitgliedEingesetzt = dsbMitgliedEingesetzt;
        this.dsbMitgliedVorname=dsbMitgliedVorname;
        this.dsbMitgliedNachname=dsbMitgliedNachname;
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.lastModifiedAtUtc = lastModifiedAtUtc;
        this.lastModifiedByUserId = lastModifiedByUserId;
        this.version = version;
    }


    /**
     * Constructor with mandatory parameters
     * @param mannschaftId
     * @param dsbMitgliedId
     *
     * @param createdAtUtc
     * @param createdByUserId
     * @param version
     */


    public MannschaftsmitgliedDO(final Long mannschaftId, final Long dsbMitgliedId, final OffsetDateTime createdAtUtc,
                                 final Long createdByUserId, final Long version) {
        this.mannschaftId = mannschaftId;
        this.dsbMitgliedId = dsbMitgliedId;
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.version = version;
    }

    /**
     * Constructor without technical parameters
     * @param mannschaftId
     * @param dsbMitgliedId
     * @param dsbMitgliedEingesetzt
     */

    public MannschaftsmitgliedDO(final Long mannschaftId, final Long dsbMitgliedId, final boolean dsbMitgliedEingesetzt, final String dsbMitgliedVorname, final String dsbMitgliedNachname) {
        this.mannschaftId = mannschaftId;
        this.dsbMitgliedId = dsbMitgliedId;
        this.dsbMitgliedEingesetzt = dsbMitgliedEingesetzt;
        this.dsbMitgliedVorname= dsbMitgliedVorname;
        this.dsbMitgliedNachname= dsbMitgliedNachname;

    }

    /**
     * Constructor with id for deleting existing entries
     * @param mannschaftId
     * @param dsbMitgliedId
     */
    public MannschaftsmitgliedDO(final Long mannschaftId, final Long dsbMitgliedId) {
        this.mannschaftId = mannschaftId;
        this.dsbMitgliedId = dsbMitgliedId;
    }



    public Long getMannschaftId() {
        return mannschaftId;
    }

    public void setMannschaftId(Long mannschaftId) {
        this.mannschaftId = mannschaftId;
    }

    public Long getDsbMitgliedId() {
        return dsbMitgliedId;
    }

    public void setDsbMitgliedId(Long mitgliedId) {
        this.dsbMitgliedId = mitgliedId;
    }


    public boolean isDsbMitgliedEingesetzt() {
        return dsbMitgliedEingesetzt;
    }


    public void setDsbMitgliedEingesetzt(boolean dsbMitgliedEingesetzt) {
        this.dsbMitgliedEingesetzt = dsbMitgliedEingesetzt;
    }

    public String getDsbMitgliedVorname(){
        return dsbMitgliedVorname;
    }

    public void setDsbMitgliedVorname(String dsbMitgliedVorname){
        this.dsbMitgliedVorname=dsbMitgliedVorname;
    }

    public String getDsbMitgliedNachname(){
        return dsbMitgliedNachname;
    }

    public void setDsbMitgliedNachname(){
        this.dsbMitgliedNachname=dsbMitgliedNachname;
    }


    @Override
    public int hashCode() {
        return Objects.hash(mannschaftId, dsbMitgliedId, dsbMitgliedId, dsbMitgliedVorname, dsbMitgliedNachname,
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
                dsbMitgliedId == that.dsbMitgliedId &&
                dsbMitgliedEingesetzt == that.dsbMitgliedEingesetzt &&
                dsbMitgliedVorname == that.dsbMitgliedVorname &&
                dsbMitgliedNachname == that.dsbMitgliedVorname &&
                createdByUserId == that.createdByUserId &&
                lastModifiedByUserId == that.lastModifiedByUserId &&
                version == that.version &&
                Objects.equals(mannschaftId, that.mannschaftId) &&
                Objects.equals(dsbMitgliedId, that.dsbMitgliedId) &&
                Objects.equals(dsbMitgliedEingesetzt, that.dsbMitgliedEingesetzt) &&
                Objects.equals(dsbMitgliedVorname, that.dsbMitgliedVorname)&&
                Objects.equals(dsbMitgliedNachname, that.dsbMitgliedNachname)&&
                Objects.equals(lastModifiedAtUtc, that.lastModifiedAtUtc);
    }


}
