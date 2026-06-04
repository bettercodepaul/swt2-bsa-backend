package de.bogenliga.application.services.v1.wettkampf.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

public class AnzeigenDTO implements DataTransferObject {
    //TODO serialVersionUID

    private Long id;
    private String physischeBildschirmId;
    private String tableTyp;
    private Long veranstaltungsId;
    private int aktuellesMatch;

    public AnzeigenDTO() {
        this.physischeBildschirmId = null;
        this.tableTyp = "Tabelle";
        this.veranstaltungsId = null;
        this.aktuellesMatch = 1;
    }

    public AnzeigenDTO(Long id, String physischeBildschirmId, String tableTyp, Long veranstaltungsId, int aktuellesMatch) {
        this.id = id;
        this.physischeBildschirmId = physischeBildschirmId;
        this.tableTyp = tableTyp;
        this.veranstaltungsId = veranstaltungsId;
        this.aktuellesMatch = aktuellesMatch;
    }

    public String getPhysischeBildschirmId() {
        return physischeBildschirmId;
    }

    public void setPhysischeBildschirmId(String physischeBildschirmId) {
        this.physischeBildschirmId = physischeBildschirmId;
    }

    public String getTableTyp() {
        return tableTyp;
    }

    public void setTableTyp(String tableTyp) {
        this.tableTyp = tableTyp;
    }

    public Long getVeranstaltungsId() {
        return veranstaltungsId;
    }

    public void setVeranstaltungsId(Long veranstaltungsId) {
        this.veranstaltungsId = veranstaltungsId;
    }

    public Long getId() {
        return id;
    }

    public void setAktuellesMatch(int aktuellesMatch) {
        this.aktuellesMatch = aktuellesMatch;
    }
    public int getAktuellesMatch() {
        return aktuellesMatch;
    }
}
