package de.bogenliga.application.business.schusszettel.impl.entity;

import java.time.Instant;

/**
 * Datenbank-Entity für einen Shooter Assignment (passe) im Match-Kontext.
 * Wird für Insert/Read/Update genutzt – typischerweise über JDBC/BasicDAO.
 *
 * @author Your Name
 */
public class MatchShooterEntity {

    private Long passeId;
    private Long matchId;
    private Long dsbMitgliedId;
    private Integer lfdnr;

    // technische Spalten
    private Long createdBy;
    private Instant createdAt;
    private Long modifiedBy;
    private Instant modifiedAt;

    public MatchShooterEntity() {
        // default constructor
    }

    public Long getPasseId() {
        return passeId;
    }

    public void setPasseId(Long passeId) {
        this.passeId = passeId;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Long getDsbMitgliedId() {
        return dsbMitgliedId;
    }

    public void setDsbMitgliedId(Long dsbMitgliedId) {
        this.dsbMitgliedId = dsbMitgliedId;
    }

    public Integer getLfdnr() {
        return lfdnr;
    }

    public void setLfdnr(Integer lfdnr) {
        this.lfdnr = lfdnr;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Long getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Instant getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Instant modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}
