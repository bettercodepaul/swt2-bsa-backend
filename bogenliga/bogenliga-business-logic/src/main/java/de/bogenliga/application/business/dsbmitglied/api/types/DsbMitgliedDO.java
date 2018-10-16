package de.bogenliga.application.business.dsbmitglied.api.types;

import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

import java.time.OffsetDateTime;
import java.util.Objects;

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
    private long id;
    private String vorname;
    private String nachname;
    private String geburtsdatum;
    private String nationalitaet;
    private String mitgliedsnummer;
    private long vereinsId;
    private long userId;


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
    public DsbMitgliedDO(long id, String vorname, String nachname, String geburtsdatum,
                         String nationalitaet, String mitgliedsnummer, long vereinsId, long userId, final OffsetDateTime createdAtUtc,
                         final long createdByUserId, final OffsetDateTime lastModifiedAtUtc,
                         final long lastModifiedByUserId, final long version) {
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
    public DsbMitgliedDO(long id, String vorname, String nachname, String geburtsdatum,
                         String nationalitaet, String mitgliedsnummer, long vereinsId, final OffsetDateTime createdAtUtc,
                         final long createdByUserId, final long version) {
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
    public DsbMitgliedDO(long id, String vorname, String nachname, String geburtsdatum, String nationalitaet,
                         String mitgliedsnummer, long vereinsId, long userId) {
        this.id = id;
        this.vorname = vorname;
        this.nachname = nachname;
        this.geburtsdatum = geburtsdatum;
        this.nationalitaet = nationalitaet;
        this.mitgliedsnummer = mitgliedsnummer;
        this.vereinsId = vereinsId;
        this.userId = userId;
    }

    /**
     * Constructor with id for deleting existing entries
     * @param id
     */
    public DsbMitgliedDO(long id) {
        this.id = id;
    }




    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getGeburtsdatum() {
        return geburtsdatum;
    }

    public void setGeburtsdatum(String geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    public String getNationalitaet() {
        return nationalitaet;
    }

    public void setNationalitaet(String nationalitaet) {
        this.nationalitaet = nationalitaet;
    }

    public String getMitgliedsnummer() {
        return mitgliedsnummer;
    }

    public void setMitgliedsnummer(String mitgliedsnummer) {
        this.mitgliedsnummer = mitgliedsnummer;
    }

    public long getVereinsId() {
        return vereinsId;
    }

    public void setVereinsId(long vereinsId) {
        this.vereinsId = vereinsId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DsbMitgliedDO that = (DsbMitgliedDO) o;
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

    @Override
    public int hashCode() {
        return Objects.hash(id, vorname, nachname, geburtsdatum, nationalitaet, mitgliedsnummer, vereinsId, userId, createdByUserId, lastModifiedAtUtc,
                lastModifiedByUserId, version);
    }
}
