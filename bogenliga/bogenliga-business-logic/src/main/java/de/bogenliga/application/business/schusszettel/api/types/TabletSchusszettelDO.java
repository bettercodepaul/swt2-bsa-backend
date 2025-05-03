package de.bogenliga.application.business.schusszettel.api.types;

import java.util.List;

/**
 * Business-Objekt für die Antwortstruktur des Tablet-Schusszettels.
 * Wird in der Geschäftslogik verwendet und vom ComponentImpl aufgebaut.
 *
 * @author Marty Lauterbach
 */
public class TabletSchusszettelDO {

    private TabletSchusszettelStatus status;
    private TeamInfoDO eigenesTeam;
    private TeamInfoDO gegnerischesTeam;
    private List<SchuetzeInfoDO> schuetzen;
    private List<SatzErgebnisDO> satzErgebnisse;
    private List<TeamMatchInfoDO> matchErgebnis;
    private List<VerfuegbarerSchuetzeDO> verfuegbareSchuetzen;

    // Getter/Setter
    public TabletSchusszettelStatus getStatus() { return status; }
    public void setStatus(TabletSchusszettelStatus status) { this.status = status; }

    public TeamInfoDO getEigenesTeam() { return eigenesTeam; }
    public void setEigenesTeam(TeamInfoDO eigenesTeam) { this.eigenesTeam = eigenesTeam; }

    public TeamInfoDO getGegnerischesTeam() { return gegnerischesTeam; }
    public void setGegnerischesTeam(TeamInfoDO gegnerischesTeam) { this.gegnerischesTeam = gegnerischesTeam; }

    public List<SchuetzeInfoDO> getSchuetzen() { return schuetzen; }
    public void setSchuetzen(List<SchuetzeInfoDO> schuetzen) { this.schuetzen = schuetzen; }

    public List<SatzErgebnisDO> getSatzErgebnisse() { return satzErgebnisse; }
    public void setSatzErgebnisse(List<SatzErgebnisDO> satzErgebnisse) { this.satzErgebnisse = satzErgebnisse; }

    public List<TeamMatchInfoDO> getMatchErgebnis() { return matchErgebnis; }
    public void setMatchErgebnis(List<TeamMatchInfoDO> matchErgebnis) { this.matchErgebnis = matchErgebnis; }

    public List<VerfuegbarerSchuetzeDO> getVerfuegbareSchuetzen() { return verfuegbareSchuetzen; }
    public void setVerfuegbareSchuetzen(List<VerfuegbarerSchuetzeDO> verfuegbareSchuetzen) {
        this.verfuegbareSchuetzen = verfuegbareSchuetzen;
    }

    /**
     * Statuswerte, die vom Business-Layer verwendet werden.
     * Diese werden durch den Mapper in die DTO-Enum konvertiert.
     */
    public enum TabletSchusszettelStatus {
        SATZEINGABE,
        SCHUETZENMELDUNG,
        WARTE,
        NOT_ALLOWED,
        WETTKAMPF_ENDE
    }
}
