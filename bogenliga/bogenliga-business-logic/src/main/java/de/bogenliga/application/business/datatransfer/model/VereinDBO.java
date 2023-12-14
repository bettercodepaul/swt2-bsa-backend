package de.bogenliga.application.business.datatransfer.model;

import java.sql.Timestamp;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class VereinDBO {

    private int verein_id;
    private String verein_name;
    private int verein_region_id;
    private String verein_dsb_identifier;
    private Timestamp created_at_utc;
    private String created_by;
    private Timestamp last_modified_at_utc;
    private int last_modified_by;
    private int version;
    private String verein_website;
    private String verein_describtion;
    private String verein_icon;


    public VereinDBO() {
    }


    public int getVerein_id() {
        return verein_id;
    }


    public void setVerein_id(int verein_id) {
        this.verein_id = verein_id;
    }


    public String getVerein_name() {
        return verein_name;
    }


    public void setVerein_name(String verein_name) {
        this.verein_name = verein_name;
    }


    public int getVerein_region_id() {
        return verein_region_id;
    }


    public void setVerein_region_id(int verein_region_id) {
        this.verein_region_id = verein_region_id;
    }


    public String getVerein_dsb_identifier() {
        return verein_dsb_identifier;
    }


    public void setVerein_dsb_identifier(String verein_dsb_identifier) {
        this.verein_dsb_identifier = verein_dsb_identifier;
    }


    public Timestamp getCreated_at_utc() {
        return created_at_utc;
    }


    public void setCreated_at_utc(Timestamp created_at_utc) {
        this.created_at_utc = created_at_utc;
    }


    public String getCreated_by() {
        return created_by;
    }


    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }


    public Timestamp getLast_modified_at_utc() {
        return last_modified_at_utc;
    }


    public void setLast_modified_at_utc(Timestamp last_modified_at_utc) {
        this.last_modified_at_utc = last_modified_at_utc;
    }


    public int getLast_modified_by() {
        return last_modified_by;
    }


    public void setLast_modified_by(int last_modified_by) {
        this.last_modified_by = last_modified_by;
    }


    public int getVersion() {
        return version;
    }


    public void setVersion(int version) {
        this.version = version;
    }


    public String getVerein_website() {
        return verein_website;
    }


    public void setVerein_website(String verein_website) {
        this.verein_website = verein_website;
    }


    public String getVerein_describtion() {
        return verein_describtion;
    }


    public void setVerein_describtion(String verein_describtion) {
        this.verein_describtion = verein_describtion;
    }


    public String getVerein_icon() {
        return verein_icon;
    }


    public void setVerein_icon(String verein_icon) {
        this.verein_icon = verein_icon;
    }

}
