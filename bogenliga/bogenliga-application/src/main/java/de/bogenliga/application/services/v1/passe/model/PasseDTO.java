package de.bogenliga.application.services.v1.passe.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class PasseDTO implements DataTransferObject {


    private static final long serialVersionUID = -3582493648923927523L;

    private long id;
    private long mannschaftId;
    private long wettkampfId;
    private long matchNr;
    private long lfdNr;
    private long dsbMitgliedNr;
    private int[] ringzahl;

    public PasseDTO(){}

    public PasseDTO(long id, long mannschaftId, long wettkampfId, long matchNr,
                    long lfdNr, long dsbMitgliedNr, int[] ringzahl){
        this.id = id;
        this.mannschaftId = mannschaftId;
        this.wettkampfId = wettkampfId;
        this.matchNr = matchNr;
        this.lfdNr = lfdNr;
        this.dsbMitgliedNr = dsbMitgliedNr;
        this.ringzahl = ringzahl;
    }

    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public long getMannschaftId() {
        return mannschaftId;
    }


    public void setMannschaftId(long mannschaftId) {
        this.mannschaftId = mannschaftId;
    }


    public long getWettkampfId() {
        return wettkampfId;
    }


    public void setWettkampfId(long wettkampfId) {
        this.wettkampfId = wettkampfId;
    }


    public long getMatchNr() {
        return matchNr;
    }


    public void setMatchNr(long matchNr) {
        this.matchNr = matchNr;
    }


    public long getLfdNr() {
        return lfdNr;
    }


    public void setLfdNr(long lfdNr) {
        this.lfdNr = lfdNr;
    }


    public long getDsbMitgliedNr() {
        return dsbMitgliedNr;
    }


    public void setDsbMitgliedNr(long dsbMitgliedNr) {
        this.dsbMitgliedNr = dsbMitgliedNr;
    }


    public int[] getRingzahl() {
        return ringzahl;
    }


    public void setRingzahl(int[] ringzahl) {
        this.ringzahl = ringzahl;
    }
}
