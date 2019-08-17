package de.bogenliga.application.services.v1.veranstaltung.model;

import java.sql.Date;
import de.bogenliga.application.common.service.types.DataTransferObject;


/**
 * I'm the data transfer Object of Veranstaltung.
 * <p>
 * I define the payload for the external REST interface of the Veranstaltung business entity
 *
 * @author Marvin Holm
 */
public class VeranstaltungDTO implements DataTransferObject {
    private static final long serialVersionUID = 1913174887814473273L;

    private Long id;
    private Long wettkampfTypId;
    private String name;
    private Long sportjahr;
    private Date meldeDeadline;
    private Long ligaleiterId;
    private Long ligaId;
    private Long version;
    private String ligaleiterEmail;
    private String wettkampftypName;
    private String ligaName;




    public VeranstaltungDTO() {
    }


    /**
     *
     */
    public VeranstaltungDTO(Long id, Long wettkampfTypId, String name, Long sportjahr,
                            Date meldeDeadline, Long ligaleiterId, Long ligaId, String ligaleiterEmail,
                            String wettkampftypName, String ligaName) {
        this.setId(id);
        this.setWettkampfTypId(wettkampfTypId);
        this.setName(name);
        this.setSportjahr(sportjahr);
        this.setMeldeDeadline(meldeDeadline);
        this.setLigaleiterId(ligaleiterId);
        this.setWettkampftypName(wettkampftypName);
        this.setLigaName(ligaName);
        this.setLigaId(ligaId);
        this.setLigaleiterEmail(ligaleiterEmail);
        this.setVersion(1L);
    }


    @Override
    public String toString() {
        return "VeranstaltungDTO{" +
                "id=" + id +
                ", wettkampfTypId=" + wettkampfTypId +
                ", name='" + name + '\'' +
                ", wettkampftypName='" + wettkampftypName + '\'' +
                ", sportjahr=" + sportjahr +
                ", meldeDeadline=" + meldeDeadline +
                ", version=" + version +
                ", ligaleiterId=" + ligaleiterId +
                ", ligaleiterEmail='" + ligaleiterEmail + '\'' +
                ", ligaName='" + ligaName + '\'' +
                ", ligaId=" + ligaId +
                '}';
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

    public Long getLigaleiterId() {
        return ligaleiterId;
    }

    public void setLigaleiterId(Long ligaleiterId) {
        this.ligaleiterId = ligaleiterId;
    }

    public Long getLigaId() {
        return ligaId;
    }

    public void setLigaId(Long ligaId) {
        this.ligaId = ligaId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
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

    public String getLigaName() {
        return ligaName;
    }

    public void setLigaName(String ligaName) {
        this.ligaName = ligaName;
    }
}
