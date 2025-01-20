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
    private String wettkampftypname;

    public VeranstaltungBEext(){

    }

    public VeranstaltungBEext(Long veranstaltungId, Long veranstaltungLigaId, Long veranstaltungWettkampftypId, String veranstaltungName, Long veranstaltungSportjahr, Date veranstaltungMeldedeadline, Long veranstaltungLigaleiterId, Integer veranstaltungPhase, Integer veranstaltungGroesse, String ligaLeiterEmail, String ligaName, String wettkampftypname) {
        super(veranstaltungId, veranstaltungLigaId, veranstaltungWettkampftypId, veranstaltungName, veranstaltungSportjahr, veranstaltungMeldedeadline,  veranstaltungLigaleiterId, veranstaltungPhase, veranstaltungGroesse);

        this.wettkampftypname = wettkampftypname;
        this.ligaLeiterEmail = ligaLeiterEmail;
        this.ligaName = ligaName;
    }

    public String getwettkampftypname() {
        return wettkampftypname;
    }


    public void setwettkampftypName(final String wettkampftypname) {
        this.wettkampftypname = wettkampftypname;
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
                ", wettkampftypName='" + wettkampftypname +
                '}';
    }

}
