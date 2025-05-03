package de.bogenliga.application.business.schusszettel.api.types;

import java.util.List;

/**
 * Business-Objekt für die vollständige Struktur des digitalen Schusszettels.
 * Wird vom Service- bzw. Component-Layer befüllt und an die REST-API weitergereicht.
 *
 * @author Marty Lauterbach
 */
public class TabletSchusszettelDO {

    private TabletSchusszettelStatus status;
    private TeamInfoDO eigenesTeam;
    private TeamInfoDO gegnerischesTeam;
    private List<SchuetzeMatchPunkteDO> schuetzenMatchPunkte;
    private List<SchuetzeStammdatenDO> schuetzeStammDaten;
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

    public List<SchuetzeMatchPunkteDO> getSchuetzenMatchPunkte() { return schuetzenMatchPunkte; }
    public void setSchuetzenMatchPunkte(List<SchuetzeMatchPunkteDO> schuetzenMatchPunkte) { this.schuetzenMatchPunkte = schuetzenMatchPunkte; }

    public List<SchuetzeStammdatenDO> getSchuetzeStammDaten() { return schuetzeStammDaten; }
    public void setSchuetzeStammDaten(List<SchuetzeStammdatenDO> schuetzeStammDaten) { this.schuetzeStammDaten = schuetzeStammDaten; }

    public List<SatzErgebnisDO> getSatzErgebnisse() { return satzErgebnisse; }
    public void setSatzErgebnisse(List<SatzErgebnisDO> satzErgebnisse) { this.satzErgebnisse = satzErgebnisse; }

    public List<TeamMatchInfoDO> getMatchErgebnis() { return matchErgebnis; }
    public void setMatchErgebnis(List<TeamMatchInfoDO> matchErgebnis) { this.matchErgebnis = matchErgebnis; }

    public List<VerfuegbarerSchuetzeDO> getVerfuegbareSchuetzen() { return verfuegbareSchuetzen; }
    public void setVerfuegbareSchuetzen(List<VerfuegbarerSchuetzeDO> verfuegbareSchuetzen) {
        this.verfuegbareSchuetzen = verfuegbareSchuetzen;
    }

    /**
     * Statuswerte, die den aktuellen Zustand der Tablet-Eingabemaske repräsentieren.
     */
    public enum TabletSchusszettelStatus {
        SATZEINGABE,
        SCHUETZENMELDUNG,
        WARTE,
        NOT_ALLOWED,
        WETTKAMPF_ENDE
    }
}
