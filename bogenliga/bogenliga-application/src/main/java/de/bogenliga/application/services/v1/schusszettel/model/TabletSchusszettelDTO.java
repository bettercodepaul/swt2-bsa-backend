package de.bogenliga.application.services.v1.schusszettel.model;

import java.util.List;

/**
 * Enthält die vollständige Antwortstruktur für den digitalen Schusszettel auf dem Tablet.
 * Diese DTO wird über die REST-API zurückgegeben und umfasst Status, Teams, Schützen und Ergebnisse.
 *
 * @author Marty Lauterbach
 */
public class TabletSchusszettelDTO {

    private TabletSchusszettelStatus status;
    private TeamInfoDTO eigenesTeam;
    private TeamInfoDTO gegnerischesTeam;
    private List<SchuetzeInfoDTO> schuetzen;
    private List<SatzErgebnisDTO> satzErgebnisse;
    private List<TeamMatchInfoDTO> matchErgebnis;
    private List<VerfuegbarerSchuetzeDTO> verfuegbareSchuetzen;

    // Getter/Setter
    public TabletSchusszettelStatus getStatus() { return status; }
    public void setStatus(TabletSchusszettelStatus status) { this.status = status; }

    public TeamInfoDTO getEigenesTeam() { return eigenesTeam; }
    public void setEigenesTeam(TeamInfoDTO eigenesTeam) { this.eigenesTeam = eigenesTeam; }

    public TeamInfoDTO getGegnerischesTeam() { return gegnerischesTeam; }
    public void setGegnerischesTeam(TeamInfoDTO gegnerischesTeam) { this.gegnerischesTeam = gegnerischesTeam; }

    public List<SchuetzeInfoDTO> getSchuetzen() { return schuetzen; }
    public void setSchuetzen(List<SchuetzeInfoDTO> schuetzen) { this.schuetzen = schuetzen; }

    public List<SatzErgebnisDTO> getSatzErgebnisse() { return satzErgebnisse; }
    public void setSatzErgebnisse(List<SatzErgebnisDTO> satzErgebnisse) { this.satzErgebnisse = satzErgebnisse; }

    public List<TeamMatchInfoDTO> getMatchErgebnis() { return matchErgebnis; }
    public void setMatchErgebnis(List<TeamMatchInfoDTO> matchErgebnis) { this.matchErgebnis = matchErgebnis; }

    public List<VerfuegbarerSchuetzeDTO> getVerfuegbareSchuetzen() { return verfuegbareSchuetzen; }
    public void setVerfuegbareSchuetzen(List<VerfuegbarerSchuetzeDTO> verfuegbareSchuetzen) {
        this.verfuegbareSchuetzen = verfuegbareSchuetzen;
    }

    /**
     * Statuswerte, die den aktuellen Zustand der Tablet-Eingabemaske darstellen.
     */
    public enum TabletSchusszettelStatus {
        SATZEINGABE,          // Aktive Eingabe einer Passe
        SCHUETZENMELDUNG,     // Eingabe der Rückennummern
        WARTE,                // Team wartet auf gegnerisches Team
        NOT_ALLOWED,          // Token ungültig oder Team-Zuordnung falsch
        WETTKAMPF_ENDE        // Alle Durchgänge abgeschlossen
    }
}
