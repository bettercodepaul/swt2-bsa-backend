package de.bogenliga.application.services.v1.user.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

import java.sql.Date;

/**
 * I´m the data transfer object of the userProfile.
 * <p>
 * I define the payload for the external REST interface of the userProfile business entity.
 *
 * @author Yann Philippczyk, eXXcellent solutions consulting & software gmbh
 * @see DataTransferObject
 */
public class UserProfileDTO {

    private static final long serialVersionUID = -8591546299429551992L;
    private Long id;
    private String email;

    private String vorname;
    private String nachname;
    private Date geburtsdatum;
    private String nationalitaet;
    private String mitgliedsnummer;
    private Long vereinsId;

    public UserProfileDTO() {
        // empty constructor
    }

    public UserProfileDTO(Long id, String email, String vorname, String nachname, Date geburtsdatum,
                          String nationalitaet, String mitgliedsnummer, Long vereinsId) {
        this.id = id;
        this.email = email;
        this.vorname = vorname;
        this.nachname = nachname;
        this.geburtsdatum = geburtsdatum;
        this.nationalitaet = nationalitaet;
        this.mitgliedsnummer = mitgliedsnummer;
        this.vereinsId = vereinsId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Date getGeburtsdatum() {
        return geburtsdatum;
    }

    public void setGeburtsdatum(Date geburtsdatum) {
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

    public Long getVereinsId() {
        return vereinsId;
    }

    public void setVereinsId(Long vereinsId) {
        this.vereinsId = vereinsId;
    }
}
