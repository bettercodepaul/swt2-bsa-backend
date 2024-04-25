package de.bogenliga.application.services.v1.dsbmitglied.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * I´m the data transfer object of the dsbMitglied.
 *
 * I define the payload for the external REST interface of the dsbMitglied business entity.
 *
 * @author Andre Lehnert, BettercallPaul gmbh
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
    private String vereinsName;
    private Long userId;
    private Boolean kampfrichter;


    /**
     * Constructors
     */
    public DsbMitgliedDTO() {
        // empty constructor
    }


    public DsbMitgliedDTO(final Long id, final String vorname, final String nachname, final String geburtsdatum,
                          final String nationalitaet, final String mitgliedsnummer, final Long vereinsId,
                          final String vereinsName, final Long userId, final Boolean iskampfrichter) {
        this.id = id;
        this.vorname = vorname;
        this.nachname = nachname;
        this.geburtsdatum = geburtsdatum;
        this.nationalitaet = nationalitaet;
        this.mitgliedsnummer = mitgliedsnummer;
        this.vereinsId = vereinsId;
        this.vereinsName = vereinsName;
        this.userId = userId;
        this.kampfrichter = iskampfrichter;
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


    public String getVereinsName() {
        return vereinsName;
    }


    public void setVereinsName(final String vereinsName) {
        this.vereinsName = vereinsName;
    }


    public Long getUserId() {
        return userId;
    }


    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public boolean isKampfrichter(){return kampfrichter;}

   public void setKampfrichter(final boolean kampfrichter) {this.kampfrichter = kampfrichter;}
}
