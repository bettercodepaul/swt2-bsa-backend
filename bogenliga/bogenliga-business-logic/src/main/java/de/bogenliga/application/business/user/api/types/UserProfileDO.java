package de.bogenliga.application.business.user.api.types;

import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

import java.sql.Date;
import java.util.Objects;

public class UserProfileDO extends CommonDataObject implements DataObject {
    private static final long serialVersionUID = 298357103627898987L;

    /**
     * business parameter
     */
    private Long id;
    private String email;

    private String vorname;
    private String nachname;
    private Date geburtsdatum;
    private String nationalitaet;
    private String mitgliedsnummer;
    private Long vereinsId;


    /**
     * Constructor with optional parameters
     */
    public UserProfileDO() {
        // empty constructor
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfileDO that = (UserProfileDO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(email, that.email) &&
                Objects.equals(vorname, that.vorname) &&
                Objects.equals(nachname, that.nachname) &&
                Objects.equals(geburtsdatum, that.geburtsdatum) &&
                Objects.equals(nationalitaet, that.nationalitaet) &&
                Objects.equals(mitgliedsnummer, that.mitgliedsnummer) &&
                Objects.equals(vereinsId, that.vereinsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, vorname, nachname, geburtsdatum, nationalitaet, mitgliedsnummer, vereinsId);
    }
}
