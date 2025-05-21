package de.bogenliga.application.business.schusszettel.api.types.inside;

/**
 * Repräsentiert ein Vereinsmitglied, das im aktuellen Match als Schütze auswählbar ist.
 * Wird vom Business-Layer verwendet.
 *
 * @author Marty Lauterbach
 */
public class VerfuegbarerSchuetzeDO {
    private Long schuetzenId;
    private String name;

    public VerfuegbarerSchuetzeDO() {
        // no-args constructor
    }

    public VerfuegbarerSchuetzeDO(Long schuetzenId, String name) {
        this.schuetzenId = schuetzenId;
        this.name = name;
    }

    public Long getSchuetzenId() { return schuetzenId; }
    public void setSchuetzenId(Long schuetzenId) { this.schuetzenId = schuetzenId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
