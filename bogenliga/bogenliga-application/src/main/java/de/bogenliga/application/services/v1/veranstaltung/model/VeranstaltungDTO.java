package de.bogenliga.application.services.v1.veranstaltung.model;

import de.bogenliga.application.common.service.types.DataTransferObject;
import java.sql.Date;


/**
 * I'm the data transfer Object of Veranstaltung.
 * <p>
 * I define the payload for the external REST interface of the Veranstaltung business entity
 *
 * @author Marvin Holm
 */
public class VeranstaltungDTO implements DataTransferObject {
    private Long id;
    private Long wettkampfTypId;
    private String name;
    private Long sportjahr;
    private Date meldeDeadline;
    private Long ligaleiterID;
    private Long version;


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Long getWettkampfTypId() {
        return wettkampfTypId;
    }


    public void setWettkampfTypId(Long wettkampfTypId) {
        this.wettkampfTypId = wettkampfTypId;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Long getSportjahr() {
        return sportjahr;
    }


    public void setSportjahr(Long sportjahr) {
        this.sportjahr = sportjahr;
    }


    public Date getMeldeDeadline() {
        return meldeDeadline;
    }


    public void setMeldeDeadline(Date meldeDeadline) {
        this.meldeDeadline = meldeDeadline;
    }


    public Long getLigaleiterID() {
        return ligaleiterID;
    }


    public void setLigaleiterID(Long ligaleiterID) {
        this.ligaleiterID = ligaleiterID;
    }


    public Long getVersion() {
        return version;
    }


    public void setVersion(Long version) {
        this.version = version;
    }


    public VeranstaltungDTO() {
    }


    /**
     *

     */
    public VeranstaltungDTO(Long id, Long wettkampfTypId, String name, Long sportjahr,
                            Date meldeDeadline, Long ligaleiterID) {
        this.id = id;
        this.wettkampfTypId = wettkampfTypId;
        this.name = name;
        this.sportjahr = sportjahr;
        this.meldeDeadline = meldeDeadline;
        this.ligaleiterID = ligaleiterID;
        this.version = 1L;
    }


    @Override
    public String toString() {
        return "VeranstaltungDTO{" +
                "veranstaltung_id='" + this.id + '\'' +
                ", wettkampf_id='" + wettkampfTypId + '\'' +
                ", name='" + name + '\'' +
                ", sportjahr='" + this.sportjahr + '\'' +
                ", meldedeadline='" + this.meldeDeadline + '\'' +
                ", ligaleiter_id='" + this.ligaleiterID + '\'' +
                "}";
    }
}
