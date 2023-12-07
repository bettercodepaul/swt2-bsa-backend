package de.bogenliga.application.business.datatransfer.model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class SchuetzeDBO {
    private int dsb_mitglied_id;
    private String dsb_mitglied_vorname;
    private String dsb_mitglied_nachname;
    private Date dsb_mitglied_geburtsdatum;
    private String dsb_mitglied_nationalitaet;
    private String dsb_mitglied_mitgliedsnummer;
    private int dsb_mitglied_verein_id;
    private int dsb_mitglied_benutzer_id;
    private Timestamp created_at_utc;
    private int created_by;
    private Timestamp last_modified_at_utc;
    private int last_modified_by;
    private int version;



    // if mannschaft hat keine nummer -> Verein
    // if mannschaft contains verein -> mannschaft mannschaftsnr order by num desc



    public SchuetzeDBO(){

    }


    public void setDsb_mitglied_id(int dsb_mitglied_id) {
        this.dsb_mitglied_id = dsb_mitglied_id;
    }


    public void setDsb_mitglied_vorname(String dsb_mitglied_vorname) {
        this.dsb_mitglied_vorname = dsb_mitglied_vorname;
    }


    public void setDsb_mitglied_nachname(String dsb_mitglied_nachname) {
        this.dsb_mitglied_nachname = dsb_mitglied_nachname;
    }


    public void setDsb_mitglied_geburtsdatum(Date dsb_mitglied_geburtsdatum) {
        this.dsb_mitglied_geburtsdatum = dsb_mitglied_geburtsdatum;
    }


    public void setDsb_mitglied_nationalitaet(String dsb_mitglied_nationalitaet) {
        this.dsb_mitglied_nationalitaet = dsb_mitglied_nationalitaet;
    }


    public void setDsb_mitglied_mitgliedsnummer(String dsb_mitglied_mitgliedsnummer) {
        this.dsb_mitglied_mitgliedsnummer = dsb_mitglied_mitgliedsnummer;
    }


    public void setDsb_mitglied_verein_id(int dsb_mitglied_verein_id) {
        this.dsb_mitglied_verein_id = dsb_mitglied_verein_id;
    }


    public void setDsb_mitglied_benutzer_id(int dsb_mitglied_benutzer_id) {
        this.dsb_mitglied_benutzer_id = dsb_mitglied_benutzer_id;
    }


    public void setCreated_at_utc(Timestamp created_at_utc) {
        this.created_at_utc = created_at_utc;
    }


    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }


    public void setLast_modified_at_utc(Timestamp last_modified_at_utc) {
        this.last_modified_at_utc = last_modified_at_utc;
    }


    public void setLast_modified_by(int last_modified_by) {
        this.last_modified_by = last_modified_by;
    }


    public void setVersion(int version) {
        this.version = version;
    }


    public int getDsb_mitglied_id() {
        return dsb_mitglied_id;
    }


    public String getDsb_mitglied_vorname() {
        return dsb_mitglied_vorname;
    }


    public String getDsb_mitglied_nachname() {
        return dsb_mitglied_nachname;
    }


    public Date getDsb_mitglied_geburtsdatum() {
        return dsb_mitglied_geburtsdatum;
    }


    public String getDsb_mitglied_nationalitaet() {
        return dsb_mitglied_nationalitaet;
    }


    public String getDsb_mitglied_mitgliedsnummer() {
        return dsb_mitglied_mitgliedsnummer;
    }


    public int getDsb_mitglied_verein_id() {
        return dsb_mitglied_verein_id;
    }


    public int getDsb_mitglied_benutzer_id() {
        return dsb_mitglied_benutzer_id;
    }


    public Timestamp getCreated_at_utc() {
        return created_at_utc;
    }


    public int getCreated_by() {
        return created_by;
    }


    public Timestamp getLast_modified_at_utc() {
        return last_modified_at_utc;
    }


    public int getLast_modified_by() {
        return last_modified_by;
    }


    public int getVersion() {
        return version;
    }
}
