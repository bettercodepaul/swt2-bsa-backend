package de.bogenliga.application.services.v1.dsbmitglied.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * I´m the data transfer object of the dsbMitglied.
 *
 * I define the payload for the external REST interface of the dsbMitglied business entity.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see DataTransferObject
 */
public class DsbMitgliedDTO implements DataTransferObject {
    private long id;
    private String vorname;
    private String nachname;
    private String geburtsdatum;
    private String nationalitaet;
    private String mitgliedsnummer;
    private long vereinsId;
    private long userId;


    /**
     * Constructors
     */
    public DsbMitgliedDTO() {
        // empty constructor
    }

    public DsbMitgliedDTO(long id, String vorname, String nachname, String geburtsdatum,
                          String nationalitaet, String mitgliedsnummer, long vereinsId, long userId) {
        this.id = id;
        this.vorname = vorname;
        this.nachname = nachname;
        this.geburtsdatum = geburtsdatum;
        this.nationalitaet = nationalitaet;
        this.mitgliedsnummer = mitgliedsnummer;
        this.vereinsId = vereinsId;
        this.userId = userId;
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
}
