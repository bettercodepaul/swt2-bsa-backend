package de.bogenliga.application.business.veranstaltung.impl.entity;

import java.sql.Date;
import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * @author Daniel Schott, daniel.schott@student.reutlingen-university.de
 */
public class VeranstaltungBE extends CommonBusinessEntity implements BusinessEntity {

    private static final long serialVersionUID = -7987623598712368L;
    private Long veranstaltungId;
    private Long veranstaltungWettkampftypId;
    private String veranstaltungName;
    private Long veranstaltungSportjahr;
    private Long veranstaltungLigaId;
    private Date veranstaltungMeldedeadline;
    private Long veranstaltungLigaleiterId;

    private Integer veranstaltungPhase;
    private Integer veranstaltungGroesse;


    /**
     * Default Constructor of VeranstaltungBE
     */
    public VeranstaltungBE() {
        // empty constructor
    }



    @Override
    public String toString() {
        return "VeranstaltungBE{" +
                "veranstaltungID=" + veranstaltungId +
                ", veranstaltungWettkampftypID=" + veranstaltungWettkampftypId +
                ", veranstaltungName='" + veranstaltungName +
                ", veranstaltungSportJahr=" + veranstaltungSportjahr +
                ", veranstaltungLigaId=" + veranstaltungLigaId +
                ", veranstaltungMeldeDeadline=" + veranstaltungMeldedeadline +
                ", veranstaltungLigaleiterID=" + veranstaltungLigaleiterId +
                ", veranstaltungPhase=" + veranstaltungPhase +
                ", veranstaltungGroesse=" + veranstaltungGroesse +
                '}';
    }

    public Long getVeranstaltungId() {
        return veranstaltungId;
    }

    public void setVeranstaltungId(Long veranstaltungId) {
        this.veranstaltungId = veranstaltungId;
    }

    public Long getVeranstaltungWettkampftypId() {
        return veranstaltungWettkampftypId;
    }

    public void setVeranstaltungWettkampftypId(Long veranstaltungWettkampftypId) {
        this.veranstaltungWettkampftypId = veranstaltungWettkampftypId;
    }

    public String getVeranstaltungName() {
        return veranstaltungName;
    }

    public void setVeranstaltungName(String veranstaltungName) {
        this.veranstaltungName = veranstaltungName;
    }

    public Long getVeranstaltungSportjahr() {
        return veranstaltungSportjahr;
    }

    public void setVeranstaltungSportjahr(Long veranstaltungSportjahr) {
        this.veranstaltungSportjahr = veranstaltungSportjahr;
    }

    public Long getVeranstaltungLigaId() {
        return veranstaltungLigaId;
    }

    public void setVeranstaltungLigaId(Long veranstaltungLigaId) {
        this.veranstaltungLigaId = veranstaltungLigaId;
    }

    public Date getVeranstaltungMeldedeadline() {
        return veranstaltungMeldedeadline;
    }

    public void setVeranstaltungMeldedeadline(Date veranstaltungMeldedeadline) {
        this.veranstaltungMeldedeadline = veranstaltungMeldedeadline;
    }


    public Long getVeranstaltungLigaleiterId() {
        return veranstaltungLigaleiterId;
    }


    public void setVeranstaltungLigaleiterId(Long veranstaltungLigaleiterId) {
        this.veranstaltungLigaleiterId = veranstaltungLigaleiterId;
    }


    public Integer getVeranstaltungPhase() {
        return veranstaltungPhase;
    }


    public void setVeranstaltungPhase(Integer veranstaltungPhase) {
        this.veranstaltungPhase = veranstaltungPhase;
    }

    public Integer getVeranstaltungGroesse() {
        return veranstaltungGroesse;
    }

    public void setVeranstaltungGroesse(Integer veranstaltungGroesse) {
        this.veranstaltungGroesse = veranstaltungGroesse;
    }

}