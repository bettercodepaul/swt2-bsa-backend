package de.bogenliga.application.business.schusszettel.api.types;

import java.util.List;

/**
 * Enthält die Gesamtdaten für den Tablet-Schusszettel-Zustand.
 * Wird über die REST-API als Antwort geliefert.
 *
 * @author Marty Lauterbach, mklemmingen
 */
public class TabletSchusszettelDTO {
    private TabletSchusszettelStatus status;
    private TeamInfoDTO eigenesTeam;
    private TeamInfoDTO gegnerischesTeam;
    private List<SchuetzeInfoDTO> schuetzen;
    private List<SatzErgebnisDTO> satzErgebnisse;
    private List<TeamMatchInfoDTO> matchErgebnis;
    private List<VerfuegbarerSchuetzeDTO> verfuegbareSchuetzen;

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
    public void setVerfuegbareSchuetzen(List<VerfuegbarerSchuetzeDTO> verfuegbareSchuetzen) { this.verfuegbareSchuetzen = verfuegbareSchuetzen; }

    /**
     * Statuswerte für den aktuellen Zustand des Tablets im Wettkampf.
     */
    public enum TabletSchusszettelStatus {
        SATZEINGABE,          // Aktive Eingabe einer Passe
        SCHUETZENMELDUNG,     // Eingabe der Rückennummern
        WARTE,                // Team wartet auf gegnerisches Team
        NOT_ALLOWED,          // Token ungültig oder Team nicht erlaubt
        WETTKAMPF_ENDE        // Alle Matches des Tages sind beendet
    }
}