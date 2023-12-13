package de.bogenliga.application.business.datatransfer.model;

import java.sql.Timestamp;

public class MannschaftDBO {
    private int mannschaft_id;
    private int mannschaft_verein_id;
    private int mannschaft_nummer;
    private int mannschaft_benutzer_id;
    private int mannschaft_veranstaltung_id;
    private Timestamp created_at_utc;
    private int created_by;
    private Timestamp last_modified_at_utc;
    private int last_modified_by;
    private int version;
    private int mannschaft_sortierung;


    public MannschaftDBO(){

    }

    // Getter and Setter methods for mannschaft_id
    public int getMannschaftId() {
        return mannschaft_id;
    }

    public void setMannschaftId(int mannschaftId) {
        this.mannschaft_id = mannschaftId;
    }

    // Getter and Setter methods for mannschaft_verein_id
    public int getMannschaftVereinId() {
        return mannschaft_verein_id;
    }

    public void setMannschaftVereinId(int mannschaftVereinId) {
        this.mannschaft_verein_id = mannschaftVereinId;
    }

    // Getter and Setter methods for mannschaft_nummer
    public int getMannschaftNummer() {
        return mannschaft_nummer;
    }

    public void setMannschaftNummer(int mannschaftNummer) {
        this.mannschaft_nummer = mannschaftNummer;
    }

    // Getter and Setter methods for mannschaft_benutzer_id
    public int getMannschaftBenutzerId() {
        return mannschaft_benutzer_id;
    }

    public void setMannschaftBenutzerId(int mannschaftBenutzerId) {
        this.mannschaft_benutzer_id = mannschaftBenutzerId;
    }

    // Getter and Setter methods for mannschaft_veranstaltung_id
    public int getMannschaftVeranstaltungId() {
        return mannschaft_veranstaltung_id;
    }

    public void setMannschaftVeranstaltungId(int mannschaftVeranstaltungId) {
        this.mannschaft_veranstaltung_id = mannschaftVeranstaltungId;
    }

    // Getter and Setter methods for created_at_utc
    public Timestamp getCreatedAtUtc() {
        return created_at_utc;
    }

    public void setCreatedAtUtc(Timestamp createdAtUtc) {
        this.created_at_utc = createdAtUtc;
    }

    // Getter and Setter methods for created_by
    public int getCreatedBy() {
        return created_by;
    }

    public void setCreatedBy(int createdBy) {
        this.created_by = createdBy;
    }

    // Getter and Setter methods for last_modified_at_utc
    public Timestamp getLastModifiedAtUtc() {
        return last_modified_at_utc;
    }

    public void setLastModifiedAtUtc(Timestamp lastModifiedAtUtc) {
        this.last_modified_at_utc = lastModifiedAtUtc;
    }

    // Getter and Setter methods for last_modified_by
    public int getLastModifiedBy() {
        return last_modified_by;
    }

    public void setLastModifiedBy(int lastModifiedBy) {
        this.last_modified_by = lastModifiedBy;
    }

    // Getter and Setter methods for version
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    // Getter and Setter methods for mannschaft_sortierung
    public int getMannschaftSortierung() {
        return mannschaft_sortierung;
    }

    public void setMannschaftSortierung(int mannschaftSortierung) {
        this.mannschaft_sortierung = mannschaftSortierung;
    }
}



