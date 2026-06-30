package de.bogenliga.application.services.v1.wettkampf.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

public class AnzeigenDTO implements DataTransferObject {
    private static final long serialVersionUID = 5493029943811486806L;

    private Long id;
    private String physischeBildschirmId;
    private String tableTyp;
    private Long wettkampfId;
    private int aktuellesMatch;

    public AnzeigenDTO() {
        this.physischeBildschirmId = null;
        this.tableTyp = "Tabelle";
        this.wettkampfId = null;
        this.aktuellesMatch = 1;
    }

    public AnzeigenDTO(Long id, String physischeBildschirmId, String tableTyp, Long wettkampfId, int aktuellesMatch) {
        this.id = id;
        this.physischeBildschirmId = physischeBildschirmId;
        this.tableTyp = tableTyp;
        this.wettkampfId = wettkampfId;
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

    public Long getWettkampfId() {
        return wettkampfId;
    }

    public void setWettkampfId(Long wettkampfId) {
        this.wettkampfId = wettkampfId;
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
