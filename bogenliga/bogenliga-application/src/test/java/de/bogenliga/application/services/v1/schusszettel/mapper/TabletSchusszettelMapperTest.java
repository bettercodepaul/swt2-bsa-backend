package de.bogenliga.application.services.v1.schusszettel.mapper;

import de.bogenliga.application.business.schusszettel.api.types.*;
import de.bogenliga.application.services.v1.schusszettel.model.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testet die Mapper-Funktionalität von TabletSchusszettelMapper.
 *
 * @author Marty Lauterbach, mklemmingen
 */
public class TabletSchusszettelMapperTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private TabletSchusszettelDO doObj;

    private static final long TEAM_ID = 42L;
    private static final String TEAM_NAME = "Reutlingen";

    @Before
    public void setUp() {
        doObj = new TabletSchusszettelDO();

        // Status
        doObj.setStatus(TabletSchusszettelDO.TabletSchusszettelStatus.SATZEINGABE);

        // Teams
        doObj.setEigenesTeam(new TeamInfoDO(TEAM_ID, TEAM_NAME));
        doObj.setGegnerischesTeam(new TeamInfoDO(99L, "Gegner"));

        // Satz-Ergebnisse
        SatzErgebnisDO satz = new SatzErgebnisDO();
        satz.setSatzNr(1);
        satz.setTeam1Punkte(55);
        satz.setTeam2Punkte(53);
        doObj.setSatzErgebnisse(Collections.singletonList(satz));

        // Schützenpunkte
        SchuetzeMatchPunkteDO matchPunkte = new SchuetzeMatchPunkteDO(10L, 60);
        doObj.setSchuetzenMatchPunkte(Collections.singletonList(matchPunkte));

        // Stammdaten
        SchuetzeStammdatenDO stamm = new SchuetzeStammdatenDO(10L, 5, "Max", "Mustermann");
        doObj.setSchuetzeStammDaten(Collections.singletonList(stamm));

        // Match-Ergebnis
        TeamMatchInfoDO matchInfo = new TeamMatchInfoDO(TEAM_ID, TEAM_NAME, 3);
        doObj.setMatchErgebnis(Collections.singletonList(matchInfo));

        // Verfügbare Schützen
        VerfuegbarerSchuetzeDO verfuegbarer = new VerfuegbarerSchuetzeDO(88L, "Anna Schmidt");
        doObj.setVerfuegbareSchuetzen(Collections.singletonList(verfuegbarer));
    }

    @Test
    public void testToDTO_ok() {
        // Act
        TabletSchusszettelDTO dto = TabletSchusszettelMapper.toDTO(doObj);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.getStatus()).isEqualTo(TabletSchusszettelDTO.TabletSchusszettelStatus.SATZEINGABE);
        assertThat(dto.getEigenesTeam().getTeamId()).isEqualTo(TEAM_ID);
        assertThat(dto.getEigenesTeam().getTeamName()).isEqualTo(TEAM_NAME);

        // Satz-Ergebnis
        assertThat(dto.getSatzErgebnisse()).hasSize(1);
        assertThat(dto.getSatzErgebnisse().get(0).getSatzNr()).isEqualTo(1);

        // Matchpunkte
        assertThat(dto.getSchuetzenMatchPunkte()).hasSize(1);
        assertThat(dto.getSchuetzenMatchPunkte().get(0).getPunkteBisher()).isEqualTo(60);

        // Stammdaten
        assertThat(dto.getSchuetzeStammDaten()).hasSize(1);
        assertThat(dto.getSchuetzeStammDaten().get(0).getVorname()).isEqualTo("Max");

        // Match-Ergebnis
        assertThat(dto.getMatchErgebnis()).hasSize(1);
        assertThat(dto.getMatchErgebnis().get(0).getMatchpunkte()).isEqualTo(3);

        // Verfügbare Schützen
        assertThat(dto.getVerfuegbareSchuetzen()).hasSize(1);
        assertThat(dto.getVerfuegbareSchuetzen().get(0).getName()).isEqualTo("Anna Schmidt");
    }

    @Test
    public void testToDTO_nullInput() {
        // Act
        TabletSchusszettelDTO dto = TabletSchusszettelMapper.toDTO(null);

        // Assert
        assertThat(dto).isNull();
    }

    @Test
    public void testToDTO_emptyLists() {
        TabletSchusszettelDO emptyDO = new TabletSchusszettelDO();
        emptyDO.setStatus(TabletSchusszettelDO.TabletSchusszettelStatus.WARTE);
        emptyDO.setSatzErgebnisse(Collections.emptyList());
        emptyDO.setSchuetzenMatchPunkte(Collections.emptyList());
        emptyDO.setSchuetzeStammDaten(Collections.emptyList());
        emptyDO.setMatchErgebnis(Collections.emptyList());
        emptyDO.setVerfuegbareSchuetzen(Collections.emptyList());

        TabletSchusszettelDTO dto = TabletSchusszettelMapper.toDTO(emptyDO);

        assertThat(dto).isNotNull();
        assertThat(dto.getStatus()).isEqualTo(TabletSchusszettelDTO.TabletSchusszettelStatus.WARTE);
        assertThat(dto.getSatzErgebnisse()).isEmpty();
        assertThat(dto.getSchuetzenMatchPunkte()).isEmpty();
        assertThat(dto.getSchuetzeStammDaten()).isEmpty();
        assertThat(dto.getMatchErgebnis()).isEmpty();
        assertThat(dto.getVerfuegbareSchuetzen()).isEmpty();
    }
}
