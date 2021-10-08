package de.bogenliga.application.business.dsbmitglied.api.types;

import java.sql.Date;
import java.time.OffsetDateTime;
import java.util.Objects;
import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

/**
 * Contains the values of the dsbmitglied business entity.
 *
 * @author Yann Philippczyk, eXXcellent solutions consulting & software gmbh
 */
public class DsbMitgliedDO extends CommonDataObject implements DataObject {
    private static final long serialVersionUID = 298357103627898987L;

    /**
     * business parameter
     */
    private Long id;
    private String vorname;
    private String nachname;
    private Date geburtsdatum;
    private String nationalitaet;
    private String mitgliedsnummer;
    private Long vereinsId;
    private Long userId;
    private Boolean kampfrichter;

    /**
     * NoArgs constructor
     */
    public DsbMitgliedDO(){

    }

    /**
     * Constructor with optional parameters
     * @param id
     * @param vorname
     * @param nachname
     * @param geburtsdatum
     * @param nationalitaet
     * @param mitgliedsnummer
     * @param vereinsId
     * @param userId
     * @param createdAtUtc
     * @param createdByUserId
     * @param lastModifiedAtUtc
     * @param lastModifiedByUserId
     * @param version
     */
    public DsbMitgliedDO(final Long id, final String vorname, final String nachname, final Date geburtsdatum,
                         final String nationalitaet, final String mitgliedsnummer, final Long vereinsId,
                         final Long userId, final OffsetDateTime createdAtUtc,
                         final Long createdByUserId, final OffsetDateTime lastModifiedAtUtc,
                         final Long lastModifiedByUserId, final Long version, boolean lizenz) {
        this.id = id;
        this.vorname = vorname;
        this.nachname = nachname;
        this.geburtsdatum = geburtsdatum;
        this.nationalitaet = nationalitaet;
        this.mitgliedsnummer = mitgliedsnummer;
        this.vereinsId = vereinsId;
        this.userId = userId;
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.lastModifiedAtUtc = lastModifiedAtUtc;
        this.lastModifiedByUserId = lastModifiedByUserId;
        this.version = version;
        this.kampfrichter =lizenz;
    }

    /**
     * Constructor with mandatory parameters
     * @param id
     * @param vorname
     * @param nachname
     * @param geburtsdatum
     * @param nationalitaet
     * @param mitgliedsnummer
     * @param vereinsId
     * @param createdAtUtc
     * @param createdByUserId
     * @param version
     */
    public DsbMitgliedDO(final Long id, final String vorname, final String nachname, final Date geburtsdatum,
                         final String nationalitaet, final String mitgliedsnummer, final Long vereinsId,
                         final OffsetDateTime createdAtUtc,
                         final Long createdByUserId, final Long version) {
        this.id = id;
        this.vorname = vorname;
        this.nachname = nachname;
        this.geburtsdatum = geburtsdatum;
        this.nationalitaet = nationalitaet;
        this.mitgliedsnummer = mitgliedsnummer;
        this.vereinsId = vereinsId;
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.version = version;
    }

    /**
     * Constructor without technical parameters
     * @param id
     * @param vorname
     * @param nachname
     * @param geburtsdatum
     * @param nationalitaet
     * @param mitgliedsnummer
     * @param vereinsId
     * @param userId
     */
    public DsbMitgliedDO(final Long id, final String vorname, final String nachname, final Date geburtsdatum,
                         final String nationalitaet, final String mitgliedsnummer,
                         final Long vereinsId, final Long userId, final Boolean kampfrichter) {
        this.id = id;
        this.vorname = vorname;
        this.nachname = nachname;
        this.geburtsdatum = geburtsdatum;
        this.nationalitaet = nationalitaet;
        this.mitgliedsnummer = mitgliedsnummer;
        this.vereinsId = vereinsId;
        this.userId = userId;
        this.kampfrichter = kampfrichter;
    }

    /**
     * Constructor with id for deleting existing entries
     * @param id
     */
    public DsbMitgliedDO(final Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }


    public void setId(final long id) {
        this.id = id;
    }

    public String getVorname() {
        return vorname;
    }


    public void setVorname(final String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }


    public void setNachname(final String nachname) {
        this.nachname = nachname;
    }


    public Date getGeburtsdatum() {
        return geburtsdatum;
    }


    public void setGeburtsdatum(final Date geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    public String getNationalitaet() {
        return nationalitaet;
    }


    public void setNationalitaet(final String nationalitaet) {
        this.nationalitaet = nationalitaet;
    }

    public String getMitgliedsnummer() {
        return mitgliedsnummer;
    }


    public void setMitgliedsnummer(final String mitgliedsnummer) {
        this.mitgliedsnummer = mitgliedsnummer;
    }


    public Long getVereinsId() {
        return vereinsId;
    }


    public void setVereinsId(final Long vereinsId) {
        this.vereinsId = vereinsId;
    }


    public Long getUserId() {
        return userId;
    }


    public void setUserId(final Long userId) {
        this.userId = userId;
    }


    public Boolean isKampfrichter() {
        return kampfrichter;
    }


    public void setKampfrichter(Boolean kampfrichter) {
        this.kampfrichter = kampfrichter;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, vorname, nachname, geburtsdatum, nationalitaet, mitgliedsnummer, vereinsId, userId,
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
        final DsbMitgliedDO that = (DsbMitgliedDO) o;
        return id == that.id &&
                vereinsId == that.vereinsId &&
                userId == that.userId &&
                createdByUserId == that.createdByUserId &&
                lastModifiedByUserId == that.lastModifiedByUserId &&
                version == that.version &&
                Objects.equals(vorname, that.vorname) &&
                Objects.equals(nachname, that.nachname) &&
                Objects.equals(geburtsdatum, that.geburtsdatum) &&
                Objects.equals(nationalitaet, that.nationalitaet) &&
                Objects.equals(mitgliedsnummer, that.mitgliedsnummer) &&
                Objects.equals(lastModifiedAtUtc, that.lastModifiedAtUtc);
    }
}
