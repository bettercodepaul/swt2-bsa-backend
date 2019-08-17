package de.bogenliga.application.business.veranstaltung.impl.entity;

import java.sql.Date;
import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * @author Daniel Schott, daniel.schott@student.reutlingen-university.de
 */
public class VeranstaltungBE extends CommonBusinessEntity implements BusinessEntity {

    private static final long serialVersionUID = -7987623598712368L;
    private Long veranstaltung_id;
    private Long veranstaltung_wettkampftyp_id;
    private String veranstaltung_name;
    private Long veranstaltung_sportjahr;
    private Long veranstaltung_liga_id;
    private Date veranstaltung_meldedeadline;
    private Long veranstaltung_ligaleiter_id;


    /**
     * Default Constructor of VeranstaltungBE
     */
    public VeranstaltungBE() {
        // empty constructor
    }



    @Override
    public String toString() {
        return "VeranstaltungBE{" +
                "veranstaltungID=" + veranstaltung_id +
                ", veranstaltungWettkampftypID=" + veranstaltung_wettkampftyp_id +
                ", veranstaltungName='" + veranstaltung_name + '\'' +
                ", veranstaltungSportJahr=" + veranstaltung_sportjahr +
                ", veranstaltungLigaId=" + veranstaltung_liga_id +
                ", veranstaltungMeldeDeadline=" + veranstaltung_meldedeadline +
                ", veranstaltungLigaleiterID=" + veranstaltung_ligaleiter_id +
                '}';
    }

    public Long getVeranstaltung_id() {
        return veranstaltung_id;
    }

    public void setVeranstaltung_id(Long veranstaltung_id) {
        this.veranstaltung_id = veranstaltung_id;
    }

    public Long getVeranstaltung_wettkampftyp_id() {
        return veranstaltung_wettkampftyp_id;
    }

    public void setVeranstaltung_wettkampftyp_id(Long veranstaltung_wettkampftyp_id) {
        this.veranstaltung_wettkampftyp_id = veranstaltung_wettkampftyp_id;
    }

    public String getVeranstaltung_name() {
        return veranstaltung_name;
    }

    public void setVeranstaltung_name(String veranstaltung_name) {
        this.veranstaltung_name = veranstaltung_name;
    }

    public Long getVeranstaltung_sportjahr() {
        return veranstaltung_sportjahr;
    }

    public void setVeranstaltung_sportjahr(Long veranstaltung_sportjahr) {
        this.veranstaltung_sportjahr = veranstaltung_sportjahr;
    }

    public Long getVeranstaltung_liga_id() {
        return veranstaltung_liga_id;
    }

    public void setVeranstaltung_liga_id(Long veranstaltung_liga_id) {
        this.veranstaltung_liga_id = veranstaltung_liga_id;
    }

    public Date getVeranstaltung_meldedeadline() {
        return veranstaltung_meldedeadline;
    }

    public void setVeranstaltung_meldedeadline(Date veranstaltung_meldedeadline) {
        this.veranstaltung_meldedeadline = veranstaltung_meldedeadline;
    }

    public Long getVeranstaltung_ligaleiter_id() {
        return veranstaltung_ligaleiter_id;
    }

    public void setVeranstaltung_ligaleiter_id(Long veranstaltung_ligaleiter_id) {
        this.veranstaltung_ligaleiter_id = veranstaltung_ligaleiter_id;
    }

}