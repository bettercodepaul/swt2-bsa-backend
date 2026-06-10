package de.bogenliga.application.business.wettkampf.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

public class AnzeigenBE extends CommonBusinessEntity implements BusinessEntity {
    //TODO serialVersionUID

    private Long id;
    private String physischeBildschirmId;
    private String tableTyp;
    private Long wettkampfId;
    private int aktuellesMatch;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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

    public int getAktuellesMatch() {
        return aktuellesMatch;
    }
    public void setAktuellesMatch(int aktuellesMatch) {
        this.aktuellesMatch = aktuellesMatch;
    }

}

