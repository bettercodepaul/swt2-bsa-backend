package de.bogenliga.application.business.kampfrichter.impl.entity;

import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * Database entity for kampfrichter tablet session.
 * One session per Wettkampf, identified by a token used for QR-code access.
 */
public class KampfrichterSessionEntity extends CommonBusinessEntity {

    private Long id;
    private String token;
    private Long wettkampfId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Long getWettkampfId() { return wettkampfId; }
    public void setWettkampfId(Long wettkampfId) { this.wettkampfId = wettkampfId; }
}
