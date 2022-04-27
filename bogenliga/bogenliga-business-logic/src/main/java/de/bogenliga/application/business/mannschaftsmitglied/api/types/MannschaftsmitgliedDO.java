package de.bogenliga.application.business.mannschaftsmitglied.api.types;


import java.time.OffsetDateTime;
import java.util.Objects;
import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;


public class MannschaftsmitgliedDO extends CommonDataObject implements DataObject {
    private static final long serialVersionUID = 123;


    /**
     * business parameter
     */
    private Long id;
    private Long mannschaftId;
    private Long dsbMitgliedId;
    private Integer dsbMitgliedEingesetzt;
    private String dsbMitgliedVorname;
    private String dsbMitgliedNachname;
    private Long rueckennummer;


    /**
     * Constructor with optional parameters
     *
     * @param mannschaftId id der Mannschaft
     * @param dsbMitgliedId id des DSB Mitglieds
     * @param dsbMitgliedEingesetzt hat er geschossen
     * @param createdAtUtc created Time
     * @param createdByUserId create user
     * @param lastModifiedAtUtc modified time
     * @param lastModifiedByUserId modified user
     * @param version version
     * @param rueckennummer nummer des Schützen in der Mannschaft
     */

    public MannschaftsmitgliedDO(final Long id, final Long mannschaftId, final Long dsbMitgliedId,
                                 final Integer dsbMitgliedEingesetzt,
                                 final String dsbMitgliedVorname, final String dsbMitgliedNachname,
                                 final OffsetDateTime createdAtUtc,
                                 final Long createdByUserId, final OffsetDateTime lastModifiedAtUtc,
                                 final Long lastModifiedByUserId, final Long version,
                                 final Long rueckennummer) {
        this.mannschaftId = mannschaftId;
        this.id = id;
        this.dsbMitgliedId = dsbMitgliedId;
        this.dsbMitgliedEingesetzt = dsbMitgliedEingesetzt;
        this.dsbMitgliedVorname = dsbMitgliedVorname;
        this.dsbMitgliedNachname = dsbMitgliedNachname;
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.lastModifiedAtUtc = lastModifiedAtUtc;
        this.lastModifiedByUserId = lastModifiedByUserId;
        this.version = version;
        this.rueckennummer = rueckennummer;
    }


    /**
     * Constructor with mandatory parameters
     *
     * @param mannschaftId manschafts ID
     * @param dsbMitgliedId dsb mitlgieds ID
     * @param createdAtUtc create time
     * @param createdByUserId create user
     * @param version version des Datensatzes
     */


    public MannschaftsmitgliedDO(final Long id, final Long mannschaftId, final Long dsbMitgliedId,
                                 final OffsetDateTime createdAtUtc,
                                 final Long createdByUserId, final Long version) {
        this.mannschaftId = mannschaftId;
        this.id = id;
        this.dsbMitgliedId = dsbMitgliedId;
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.version = version;
    }


    /**
     * Constructor without technical parameters
     *
     * @param mannschaftId Manschafts ID
     * @param dsbMitgliedId DSB MitgliedsID
     * @param dsbMitgliedEingesetzt hat er geschossen
     */

    public MannschaftsmitgliedDO(final Long id, final Long mannschaftId, final Long dsbMitgliedId,
                                 final Integer dsbMitgliedEingesetzt, final String dsbMitgliedVorname,
                                 final String dsbMitgliedNachname, final Long rueckennummer) {
        this.mannschaftId = mannschaftId;
        this.id = id;
        this.dsbMitgliedId = dsbMitgliedId;
        this.dsbMitgliedEingesetzt = dsbMitgliedEingesetzt;
        this.dsbMitgliedVorname = dsbMitgliedVorname;
        this.dsbMitgliedNachname = dsbMitgliedNachname;
        this.rueckennummer = rueckennummer;

    }


    /**
     * Constructor with id for deleting existing entries
     *
     * @param mannschaftId mannschafts ID
     * @param dsbMitgliedId dsb mitglieds ID
     */
    public MannschaftsmitgliedDO(final Long mannschaftId, final Long dsbMitgliedId) {
        this.mannschaftId = mannschaftId;
        this.dsbMitgliedId = dsbMitgliedId;
    }

    public MannschaftsmitgliedDO(final Long id) {
        this.setId(id);
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


    public Integer getDsbMitgliedEingesetzt() {
        return dsbMitgliedEingesetzt;
    }

    public void setDsbMitgliedEingesetzt(Integer dsbMitgliedEingesetzt) {
        this.dsbMitgliedEingesetzt = dsbMitgliedEingesetzt;
    }


    public String getDsbMitgliedVorname() {
        return dsbMitgliedVorname;
    }


    public void setDsbMitgliedVorname(String dsbMitgliedVorname) {
        this.dsbMitgliedVorname = dsbMitgliedVorname;
    }


    public String getDsbMitgliedNachname() {
        return dsbMitgliedNachname;
    }


    public void setDsbMitgliedNachname(String dsbMitgliedNachname) {
        this.dsbMitgliedNachname = dsbMitgliedNachname;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getRueckennummer() { return rueckennummer; }

    public void setRueckennummer(Long rueckennummer) { this.rueckennummer = rueckennummer; }

    @Override
    public int hashCode() {
        return Objects.hash(mannschaftId, dsbMitgliedId, dsbMitgliedId, dsbMitgliedVorname, dsbMitgliedNachname,
                createdByUserId, lastModifiedAtUtc,
                lastModifiedByUserId, version, rueckennummer);
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
        return lastModifiedByUserId.equals(that.lastModifiedByUserId) &&
                version.equals(that.version) &&
                mannschaftId.equals(that.mannschaftId) &&
                dsbMitgliedId.equals(that.dsbMitgliedId) &&
                dsbMitgliedEingesetzt.equals(that.dsbMitgliedEingesetzt) &&
                dsbMitgliedVorname.equals(that.dsbMitgliedVorname) &&
                dsbMitgliedNachname.equals(that.dsbMitgliedNachname) &&
                lastModifiedAtUtc.equals(that.lastModifiedAtUtc) &&
                rueckennummer.equals(that.rueckennummer);
    }
}
