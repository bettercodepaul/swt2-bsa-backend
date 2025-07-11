package de.bogenliga.application.business.schusszettel.api.types;

import de.bogenliga.application.business.schusszettel.api.types.inside.SatzErgebnisDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.SchuetzeMatchPunkteDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.SchuetzeStammdatenDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.TeamInfoDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.TeamMatchInfoDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.VerfuegbarerSchuetzeDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.WettkampfInfoDO;
import java.util.List;

/**
 * Data transfer object for tablet schusszettel GET responses.
 * 
 * <h2>STRUCTURE</h2>
 * Contains complete tablet session context including current state, team information,
 * available shooters, current scores, and match results. Populated by component layer
 * and serialized to JSON for tablet frontend consumption.
 * 
 * <h2>CONTENT VARIES BY STATE</h2>
 * <ul>
 *   <li>SCHUETZENMELDUNG: verfuegbareSchuetzen populated</li>
 *   <li>SATZEINGABE: schuetzeStammDaten populated for registered shooters</li>
 *   <li>WARTE: satzErgebnisse populated with current match scores</li>
 *   <li>WETTKAMPF_ENDE: matchErgebnis populated with final results</li>
 * </ul>
 * 
 * <h2>NAVIGATION DATA</h2>
 * Includes match IDs for frontend navigation and opponent team information
 * for match context display.
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
    private WettkampfInfoDO wettkampfInfo;
    private Integer currentPasseNumber;
    
    // Match-IDs für Frontend-Navigation
    private Long eigenesTeamMatchId;
    private Long gegnerischesTeamMatchId;

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

    public WettkampfInfoDO getWettkampfInfo() { return wettkampfInfo; }
    public void setWettkampfInfo(WettkampfInfoDO wettkampfInfo) { this.wettkampfInfo = wettkampfInfo; }

    public Integer getCurrentPasseNumber() { return currentPasseNumber; }
    public void setCurrentPasseNumber(Integer currentPasseNumber) { this.currentPasseNumber = currentPasseNumber; }

    public Long getEigenesTeamMatchId() { return eigenesTeamMatchId; }
    public void setEigenesTeamMatchId(Long eigenesTeamMatchId) { this.eigenesTeamMatchId = eigenesTeamMatchId; }

    public Long getGegnerischesTeamMatchId() { return gegnerischesTeamMatchId; }
    public void setGegnerischesTeamMatchId(Long gegnerischesTeamMatchId) { this.gegnerischesTeamMatchId = gegnerischesTeamMatchId; }

    /**
     * Statuswerte, die den aktuellen Zustand der Tablet-Eingabemaske repräsentieren.
     */
    public enum TabletSchusszettelStatus {
        SATZEINGABE,
        SCHUETZENMELDUNG,
        WARTE,
        MATCH_ENDE,
        NOT_ALLOWED,
        WETTKAMPF_ENDE
    }
}