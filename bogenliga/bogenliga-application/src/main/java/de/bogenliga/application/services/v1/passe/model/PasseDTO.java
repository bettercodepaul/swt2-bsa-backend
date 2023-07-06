package de.bogenliga.application.services.v1.passe.model;

import de.bogenliga.application.common.service.types.DataTransferObject;


public class PasseDTO implements DataTransferObject {
    private static final long serialVersionUID = -3582493648923927523L;

    private Long id;
    private Long mannschaftId;
    private Long wettkampfId;
    private Long matchNr;
    private Long matchId;
    private Long lfdNr;
    private Long dsbMitgliedId;
    private Integer[] ringzahl;

    // used for mapping to the mannschaftsmitglied_id to set the dsbMitgliedId later...
    private Integer rueckennummer;


    public PasseDTO() {
    }


    public PasseDTO(Long id, Long mannschaftId, Long wettkampfId, Long matchNr, Long matchid,
                    Long lfdNr, Long dsbMitgliedId, Integer[] ringzahl) {
        this.id = id;
        this.mannschaftId = mannschaftId;
        this.wettkampfId = wettkampfId;
        this.matchNr = matchNr;
        this.matchId = matchid;
        this.lfdNr = lfdNr;
        this.dsbMitgliedId = dsbMitgliedId;
        this.ringzahl = ringzahl;
    }


    public PasseDTO(Long id, Long mannschaftId, Long wettkampfId, Long matchNr, Long matchid,
                    Long lfdNr, Integer rueckennummer, Long dsbMitgliedId, Integer[] ringzahl) {
        this.id = id;
        this.mannschaftId = mannschaftId;
        this.wettkampfId = wettkampfId;
        this.matchNr = matchNr;
        this.matchId = matchid;
        this.lfdNr = lfdNr;
        this.dsbMitgliedId = dsbMitgliedId;
        this.ringzahl = ringzahl;
        this.rueckennummer = rueckennummer;
    }


    public Long getMatchId() {
        return matchId;
    }


    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Long getMannschaftId() {
        return mannschaftId;
    }


    public void setMannschaftId(Long mannschaftId) {
        this.mannschaftId = mannschaftId;
    }


    public Long getWettkampfId() {
        return wettkampfId;
    }


    public void setWettkampfId(Long wettkampfId) {
        this.wettkampfId = wettkampfId;
    }


    public Long getMatchNr() {
        return matchNr;
    }


    public void setMatchNr(Long matchNr) {
        this.matchNr = matchNr;
    }


    public Long getLfdNr() {
        return lfdNr;
    }


    public void setLfdNr(Long lfdNr) {
        this.lfdNr = lfdNr;
    }


    public Long getDsbMitgliedId() {
        return dsbMitgliedId;
    }


    public void setDsbMitgliedId(Long dsbMitgliedId) {
        this.dsbMitgliedId = dsbMitgliedId;
    }


    public Integer[] getRingzahl() {
        return ringzahl;
    }


    public void setRingzahl(Integer[] ringzahl) {
        this.ringzahl = ringzahl;
    }


    public Integer getRueckennummer() {
        return rueckennummer;
    }


    public void setRueckennummer(Integer rueckennummer) {
        this.rueckennummer = rueckennummer;
    }
}
