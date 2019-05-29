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
    private Long ligaID;
    private String ligaleiterEmail;
    private String wettkampftypName;
    private String ligaName;


    /**
     *
     */
    public VeranstaltungDTO(Long id, Long wettkampfTypId, String name, Long sportjahr,
                            Date meldeDeadline, Long ligaleiterID, Long ligaID, String ligaleiterEmail,
                            String wettkampftypName, String ligaName) {
        this.id = id;
        this.wettkampfTypId = wettkampfTypId;
        this.name = name;
        this.sportjahr = sportjahr;
        this.meldeDeadline = meldeDeadline;
        this.ligaleiterID = ligaleiterID;
        this.ligaID = ligaID;
        this.ligaleiterEmail = ligaleiterEmail;
        this.wettkampftypName = wettkampftypName;
        this.ligaName = ligaName;
    }


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


    public String getLigaleiterEmail() {
        return ligaleiterEmail;
    }


    public void setLigaleiterEmail(String ligaleiterEmail) {
        this.ligaleiterEmail = ligaleiterEmail;
    }


    public String getWettkampftypName() {
        return wettkampftypName;
    }


    public void setWettkampftypName(String wettkampftypName) {
        this.wettkampftypName = wettkampftypName;
    }


    public Long getLigaID() {
        return ligaID;
    }


    public void setLigaID(Long ligaID) {
        this.ligaID = ligaID;
    }


    public String getLigaName() {
        return ligaName;
    }


    public void setLigaName(String ligaName) {
        this.ligaName = ligaName;
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
                ", ligaID ='" + this.ligaID + '\'' +
                ", ligaleiterEmail ='" + this.ligaleiterEmail + '\'' +
                ", wettkampftypName ='" + this.wettkampftypName + '\'' +
                ", ligaName ='" + this.ligaName + '\'' +
                "}";

    }
}
