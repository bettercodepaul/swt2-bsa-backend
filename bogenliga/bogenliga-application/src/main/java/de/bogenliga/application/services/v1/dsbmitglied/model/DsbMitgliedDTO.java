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
    private static final long serialVersionUID = 8559563978424033907L;
    private Long id;
    private String vorname;
    private String nachname;
    private String geburtsdatum;
    private String nationalitaet;
    private String mitgliedsnummer;
    private Long vereinsId;
    private Long userId;


    /**
     * Constructors
     */
    public DsbMitgliedDTO() {
        // empty constructor
    }


    public DsbMitgliedDTO(final Long id, final String vorname, final String nachname, final String geburtsdatum,
                          final String nationalitaet, final String mitgliedsnummer, final Long vereinsId,
                          final Long userId) {
        this.id = id;
        this.vorname = vorname;
        this.nachname = nachname;
        this.geburtsdatum = geburtsdatum;
        this.nationalitaet = nationalitaet;
        this.mitgliedsnummer = mitgliedsnummer;
        this.vereinsId = vereinsId;
        this.userId = userId;
    }


    public Long getId() {
        return id;
    }


    public void setId(final Long id) {
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

    public String getGeburtsdatum() {
        return geburtsdatum;
    }


    public void setGeburtsdatum(final String geburtsdatum) {
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
}
