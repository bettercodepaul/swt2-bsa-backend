package de.bogenliga.application.services.v1.passe.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class PasseDTO implements DataTransferObject {


    private static final long serialVersionUID = -3582493648923927523L;
    private int mannschaft_id;
    private int wettkampf_id;
    private int match_nr;
    private int lfdnr;
    private int dsb_mitglied_nr;
    private int[] ringzahl;

    public PasseDTO(){}

    public PasseDTO(int mannschaft_id, int wettkampf_id, int match_nr, int lfdnr, int dsb_mitglied_nr, int[] ringzahl){
        this.mannschaft_id = mannschaft_id;
        this.wettkampf_id = wettkampf_id;
        this.match_nr = match_nr;
        this.lfdnr = lfdnr;
        this.dsb_mitglied_nr = dsb_mitglied_nr;
        this.ringzahl = ringzahl;
    }


    public int getMannschaft_id() {
        return mannschaft_id;
    }


    public void setMannschaft_id(int mannschaft_id) {
        this.mannschaft_id = mannschaft_id;
    }


    public int getWettkampf_id() {
        return wettkampf_id;
    }


    public void setWettkampf_id(int wettkampf_id) {
        this.wettkampf_id = wettkampf_id;
    }


    public int getMatch_nr() {
        return match_nr;
    }


    public void setMatch_nr(int match_nr) {
        this.match_nr = match_nr;
    }


    public int getLfdnr() {
        return lfdnr;
    }


    public void setLfdnr(int lfdnr) {
        this.lfdnr = lfdnr;
    }


    public int getDsb_mitglied_nr() {
        return dsb_mitglied_nr;
    }


    public void setDsb_mitglied_nr(int dsb_mitglied_nr) {
        this.dsb_mitglied_nr = dsb_mitglied_nr;
    }


    public int[] getRingzahl() {
        return ringzahl;
    }


    public void setRingzahl(int[] ringzahl) {
        this.ringzahl = ringzahl;
    }
}
