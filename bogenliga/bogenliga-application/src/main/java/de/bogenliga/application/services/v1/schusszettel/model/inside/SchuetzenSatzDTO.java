package de.bogenliga.application.services.v1.schusszettel.model.inside;

/**
 * Einzelne Satzdaten eines Schützen
 * @author Marty Lauterbach, mklemmingen
 */
public class SchuetzenSatzDTO {
    private Long schuetzenId;
    private Integer schuss1;
    private Integer schuss2;
    private Integer schuss3;

    public Long getSchuetzenId() { return schuetzenId; }
    public void setSchuetzenId(Long schuetzenId) { this.schuetzenId = schuetzenId; }

    public Integer getSchuss1() { return schuss1; }
    public void setSchuss1(Integer schuss1) { this.schuss1 = schuss1; }

    public Integer getSchuss2() { return schuss2; }
    public void setSchuss2(Integer schuss2) { this.schuss2 = schuss2; }

    public Integer getSchuss3() { return schuss3; }
    public void setSchuss3(Integer schuss3) { this.schuss3 = schuss3; }
}