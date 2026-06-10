package de.bogenliga.application.business.schusszettel.api.types;

import de.bogenliga.application.business.schusszettel.api.types.inside.*;
import de.bogenliga.application.business.schusszettel.api.types.TabletSchusszettelDO.TabletSchusszettelStatus;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TabletSchusszettelDOTest {

    @Test
    public void coverAllLines() {
        // Cover default constructor
        TabletSchusszettelDO tablet = new TabletSchusszettelDO();
        
        // Cover all setters and getters by setting values and then getting them
        tablet.setStatus(TabletSchusszettelStatus.SCHUETZENMELDUNG);
        tablet.setEigenesTeam(new TeamInfoDO());
        tablet.setGegnerischesTeam(new TeamInfoDO());
        tablet.setSchuetzenMatchPunkte(Arrays.asList(new SchuetzeMatchPunkteDO()));
        tablet.setSchuetzeStammDaten(Arrays.asList(new SchuetzeStammdatenDO()));
        tablet.setSatzErgebnisse(Arrays.asList(new SatzErgebnisDO()));
        tablet.setMatchErgebnis(Arrays.asList(new TeamMatchInfoDO()));
        tablet.setVerfuegbareSchuetzen(Arrays.asList(new VerfuegbarerSchuetzeDO()));
        tablet.setWettkampfInfo(new WettkampfInfoDO());
        tablet.setCurrentPasseNumber(1);
        tablet.setEigenesTeamMatchId(100L);
        tablet.setGegnerischesTeamMatchId(200L);
        tablet.setEigenesTeamScheibennummer(12L);

        // Cover all getters
        TabletSchusszettelStatus status = tablet.getStatus();
        TeamInfoDO eigenesTeam = tablet.getEigenesTeam();
        TeamInfoDO gegnerischesTeam = tablet.getGegnerischesTeam();
        List<SchuetzeMatchPunkteDO> schuetzenMatchPunkte = tablet.getSchuetzenMatchPunkte();
        List<SchuetzeStammdatenDO> schuetzeStammDaten = tablet.getSchuetzeStammDaten();
        List<SatzErgebnisDO> satzErgebnisse = tablet.getSatzErgebnisse();
        List<TeamMatchInfoDO> matchErgebnis = tablet.getMatchErgebnis();
        List<VerfuegbarerSchuetzeDO> verfuegbareSchuetzen = tablet.getVerfuegbareSchuetzen();
        WettkampfInfoDO wettkampfInfo = tablet.getWettkampfInfo();
        Integer currentPasseNumber = tablet.getCurrentPasseNumber();
        Long eigenesTeamMatchId = tablet.getEigenesTeamMatchId();
        Long gegnerischesTeamMatchId = tablet.getGegnerischesTeamMatchId();
        Long eigenesTeamScheibennummer = tablet.getEigenesTeamScheibennummer();

        // Basic assertions
        assertThat(tablet).isNotNull();
        assertThat(status).isEqualTo(TabletSchusszettelStatus.SCHUETZENMELDUNG);
        assertThat(currentPasseNumber).isEqualTo(1);
        assertThat(eigenesTeamMatchId).isEqualTo(100L);
        assertThat(gegnerischesTeamMatchId).isEqualTo(200L);
        assertThat(eigenesTeamScheibennummer).isEqualTo(12L);
    }
}