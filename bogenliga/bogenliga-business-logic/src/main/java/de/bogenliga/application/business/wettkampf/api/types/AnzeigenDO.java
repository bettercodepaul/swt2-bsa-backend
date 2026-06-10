package de.bogenliga.application.business.wettkampf.api.types;

import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

import java.time.OffsetDateTime;

public class AnzeigenDO extends CommonDataObject implements DataObject {

    //TODO serialVersionUID
    private Long id;
    private String physischeBildschirmId;
    private String tableTyp;
    private Long wettkampfId;
    private int aktuellesMatch;

    public AnzeigenDO() {
        this.physischeBildschirmId = null;
        this.tableTyp = "Tabelle";
        this.wettkampfId = null;
        this.aktuellesMatch = 1;
    }

    public AnzeigenDO(Long id, String physischeBildschirmId, String tableTyp, Long wettkampfId, int aktuellesMatch) {
        this.id = id;
        this.physischeBildschirmId = physischeBildschirmId;
        this.tableTyp = tableTyp;
        this.wettkampfId = wettkampfId;
        this.aktuellesMatch = aktuellesMatch;
    }

    public AnzeigenDO(Long id, String physischeBildschirmId, String tableTyp,
                      Long wettkampfId, int aktuellesMatch,
                      final OffsetDateTime createdAtUtc,
                      final Long createdByUserId, final OffsetDateTime lastModifiedUtc,
                      final Long lastModifiedByUserId, final Long version)
    {
        this.id = id;
        this.physischeBildschirmId = physischeBildschirmId;
        this.tableTyp = tableTyp;
        this.wettkampfId = wettkampfId;
        this.aktuellesMatch = aktuellesMatch;

        this.setCreatedAtUtc(createdAtUtc);
        this.setCreatedByUserId(createdByUserId);
        this.setLastModifiedAtUtc(lastModifiedUtc);
        this.setLastModifiedByUserId(lastModifiedByUserId);
        this.setVersion(version);
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

    public int getAktuellesMatch() {
        return aktuellesMatch;
    }

    public void setAktuellesMatch(int aktuellesMatch) {
        this.aktuellesMatch = aktuellesMatch;
    }

}
