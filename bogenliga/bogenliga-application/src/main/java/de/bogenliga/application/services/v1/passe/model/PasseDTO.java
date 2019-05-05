package de.bogenliga.application.services.v1.passe.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * TODO [AL] class documentation
 *
 * @author Yannik Prigl
 */
public class PasseDTO implements DataTransferObject {


    private static final long serialVersionUID = -3582493648923927523L;

    private long id;
    private long mannschaft_id;
    private long wettkampf_id;
    private long match_nr;
    private long lfdnr;
    private long dsb_mitglied_nr;
    private int[] ringzahl;


    public PasseDTO() {
    }


    public PasseDTO(long id, long mannschaft_id, long wettkampf_id, long match_nr,
                    long lfdnr, long dsb_mitglied_nr, int[] ringzahl) {
        this.id = id;
        this.mannschaft_id = mannschaft_id;
        this.wettkampf_id = wettkampf_id;
        this.match_nr = match_nr;
        this.lfdnr = lfdnr;
        this.dsb_mitglied_nr = dsb_mitglied_nr;
        this.ringzahl = ringzahl;
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public long getMannschaft_id() {
        return mannschaft_id;
    }


    public void setMannschaft_id(long mannschaft_id) {
        this.mannschaft_id = mannschaft_id;
    }


    public long getWettkampf_id() {
        return wettkampf_id;
    }


    public void setWettkampf_id(long wettkampf_id) {
        this.wettkampf_id = wettkampf_id;
    }


    public long getMatch_nr() {
        return match_nr;
    }


    public void setMatch_nr(long match_nr) {
        this.match_nr = match_nr;
    }


    public long getLfdnr() {
        return lfdnr;
    }


    public void setLfdnr(long lfdnr) {
        this.lfdnr = lfdnr;
    }


    public long getDsb_mitglied_nr() {
        return dsb_mitglied_nr;
    }


    public void setDsb_mitglied_nr(long dsb_mitglied_nr) {
        this.dsb_mitglied_nr = dsb_mitglied_nr;
    }


    public int[] getRingzahl() {
        return ringzahl;
    }


    public void setRingzahl(int[] ringzahl) {
        this.ringzahl = ringzahl;
    }
}
