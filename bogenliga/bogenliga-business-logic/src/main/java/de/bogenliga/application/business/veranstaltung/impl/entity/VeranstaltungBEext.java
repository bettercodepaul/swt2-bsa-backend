package de.bogenliga.application.business.veranstaltung.impl.entity;

import java.sql.Date;
import de.bogenliga.application.common.component.entity.BusinessEntity;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class VeranstaltungBEext extends VeranstaltungBE implements BusinessEntity {

    private static final long serialVersionUID = -7987623598712368L;
    private String ligaName;
    private String ligaLeiterEmail;
    private String wettkampftypName;

    public VeranstaltungBEext(){

    }

    public VeranstaltungBEext(Long veranstaltungId, Long veranstaltungLigaId, Long veranstaltungWettkampftypId, String veranstaltungName, Long veranstaltungSportjahr, Date veranstaltungMeldedeadline, Long veranstaltungLigaleiterId, Integer veranstaltungPhase, Integer veranstaltungGroesse, String ligaLeiterEmail, String ligaName, String wettkampftypName) {
        super(veranstaltungId, veranstaltungLigaId, veranstaltungWettkampftypId, veranstaltungName, veranstaltungSportjahr, veranstaltungMeldedeadline,  veranstaltungLigaleiterId, veranstaltungPhase, veranstaltungGroesse);

        this.wettkampftypName = wettkampftypName;
        this.ligaLeiterEmail = ligaLeiterEmail;
        this.ligaName = ligaName;
    }

    public String getWettkampftypName() {
        return wettkampftypName;
    }


    public void setWettkampftypName(final String wettkampftypName) {
        this.wettkampftypName = wettkampftypName;
    }


    public String getLigaName() {
        return ligaName;
    }


    public void setLigaName(final String ligaName) {
        this.ligaName = ligaName;
    }


    public String getLigaLeiterEmail() {
        return ligaLeiterEmail;
    }


    public void setLigaLeiterEmail(final String benutzer_email) {
        this.ligaLeiterEmail = benutzer_email;
    }


    @Override
    public String toString() {
        return "VeranstaltungBEext{" +
                "ligaName=" + ligaName +
                ", ligaLeiterEmail='" + ligaLeiterEmail +
                ", wettkampftypName='" + wettkampftypName +
                '}';
    }

}
