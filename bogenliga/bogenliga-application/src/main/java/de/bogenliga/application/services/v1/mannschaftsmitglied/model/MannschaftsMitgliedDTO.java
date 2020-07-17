package de.bogenliga.application.services.v1.mannschaftsmitglied.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

public class MannschaftsMitgliedDTO implements DataTransferObject {
    private static final long serialVersionUID = 3599079471852195486L;

    private Long id;
    private Long mannschaftsId;
    private Long dsbMitgliedId;
    private Integer dsbMitgliedEingesetzt;
    private Long rueckennummer;


    public MannschaftsMitgliedDTO() {
    }


    public MannschaftsMitgliedDTO(final Long id, final Long mannschaftsId, final Long dsbMitgliedId,
                                  final Integer dsbMitgliedEingesetzt,
                                  final Long rueckennummer) {
        this.id = id;
        this.mannschaftsId = mannschaftsId;
        this.dsbMitgliedId = dsbMitgliedId;
        this.dsbMitgliedEingesetzt = dsbMitgliedEingesetzt;
        this.rueckennummer = rueckennummer;
    }


    public Long getMannschaftsId() {
        return mannschaftsId;
    }


    public void setMannschaftsId(Long mannschaftsId) {
        this.mannschaftsId = mannschaftsId;
    }


    public Long getDsbMitgliedId() {
        return dsbMitgliedId;
    }


    public void setDsbMitgliedId(Long dsbMitgliedId) {
        this.dsbMitgliedId = dsbMitgliedId;
    }


    public Integer getDsbMitgliedEingesetzt() {
        return dsbMitgliedEingesetzt;
    }


    public void setDsbMitgliedEingesetzt(Integer dsbMitgliedEingesetzt) {
        this.dsbMitgliedEingesetzt = dsbMitgliedEingesetzt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRueckennummer() { return rueckennummer; }

    public void setRueckennummer(Long rueckennummer) { this.rueckennummer = rueckennummer; }
}
