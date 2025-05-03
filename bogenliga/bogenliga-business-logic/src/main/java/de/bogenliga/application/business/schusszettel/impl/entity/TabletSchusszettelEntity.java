package de.bogenliga.application.business.schusszettel.impl.entity;

/**
 * Datenbank-Entity für einen Schuss eines Schützen im Wettkampfkontext.
 * Wird für Insert/Read/Update genutzt – typischerweise über JDBC.
 *
 * @author Marty Lauterbach, mklemmingen
 */
public class TabletSchusszettelEntity {
    private Long id;
    private Long schuetzenId;
    private Integer schuss1;
    private Integer schuss2;
    private Integer schuss3;
    private Long teamId;
    private Integer satzNr;
    private Long wettkampfId;

    // Getter/Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSchuetzenId() { return schuetzenId; }
    public void setSchuetzenId(Long schuetzenId) { this.schuetzenId = schuetzenId; }

    public Integer getSchuss1() { return schuss1; }
    public void setSchuss1(Integer schuss1) { this.schuss1 = schuss1; }

    public Integer getSchuss2() { return schuss2; }
    public void setSchuss2(Integer schuss2) { this.schuss2 = schuss2; }

    public Integer getSchuss3() { return schuss3; }
    public void setSchuss3(Integer schuss3) { this.schuss3 = schuss3; }

    public Long getTeamId() { return teamId; }
    public void setTeamId(Long teamId) { this.teamId = teamId; }

    public Integer getSatzNr() { return satzNr; }
    public void setSatzNr(Integer satzNr) { this.satzNr = satzNr; }

    public Long getWettkampfId() { return wettkampfId; }
    public void setWettkampfId(Long wettkampfId) { this.wettkampfId = wettkampfId; }
}