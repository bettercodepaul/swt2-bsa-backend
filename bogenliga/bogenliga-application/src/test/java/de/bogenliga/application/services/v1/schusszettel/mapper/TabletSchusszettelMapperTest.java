package de.bogenliga.application.services.v1.schusszettel.mapper;

import de.bogenliga.application.business.schusszettel.api.types.*;
import de.bogenliga.application.business.schusszettel.api.types.inside.SatzErgebnisDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.SchuetzeMatchPunkteDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.SchuetzeStammdatenDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.TeamInfoDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.TeamMatchInfoDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.VerfuegbarerSchuetzeDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.WettkampfInfoDO;
import de.bogenliga.application.services.v1.schusszettel.model.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;

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

        // Match Numbers
        doObj.setEigenesTeamMatchNr(1);
        doObj.setEigenesTeamMatchId(200L);
        doObj.setGegnerischesTeamMatchId(201L);

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

        // Wettkampf-Info (optional, falls benötigt)
        WettkampfInfoDO wettkampfInfo = new WettkampfInfoDO(1L, 1L, null, "10:00", "Stadion", "Info", "Straße", "12345",
                2L, "Veranstaltung", 2023L, "Liga", "Wettkampftyp");
        doObj.setWettkampfInfo(wettkampfInfo);

        doObj.setEigenesTeamMatchId(111L);
        doObj.setGegnerischesTeamMatchId(222L);
        doObj.setEigenesTeamScheibennummer(7L);
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

        // Match Numbers
        assertThat(dto.getEigenesTeamMatchNr()).isEqualTo(1);
        assertThat(dto.getEigenesTeamMatchId()).isEqualTo(111L);
        assertThat(dto.getGegnerischesTeamMatchId()).isEqualTo(222L);

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

        assertThat(dto.getEigenesTeamMatchId()).isEqualTo(111L);
        assertThat(dto.getGegnerischesTeamMatchId()).isEqualTo(222L);
        assertThat(dto.getEigenesTeamScheibennummer()).isEqualTo(7L);
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

    @Test
    public void testToDTO_matchNumberIsNull() {
        // Arrange: DO mit null Match-Nummer
        doObj.setEigenesTeamMatchNr(null);
        doObj.setEigenesTeamMatchId(null);
        doObj.setGegnerischesTeamMatchId(null);

        // Act
        TabletSchusszettelDTO dto = TabletSchusszettelMapper.toDTO(doObj);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.getEigenesTeamMatchNr()).isNull();
        assertThat(dto.getEigenesTeamMatchId()).isNull();
        assertThat(dto.getGegnerischesTeamMatchId()).isNull();
    }

    @Test
    public void testToDTO_matchNumberIsPresent() {
        // Arrange: DO mit verschiedenen Match-Nummern
        doObj.setEigenesTeamMatchNr(5);
        doObj.setEigenesTeamMatchId(500L);
        doObj.setGegnerischesTeamMatchId(501L);

        // Act
        TabletSchusszettelDTO dto = TabletSchusszettelMapper.toDTO(doObj);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.getEigenesTeamMatchNr()).isEqualTo(5);
        assertThat(dto.getEigenesTeamMatchId()).isEqualTo(500L);
        assertThat(dto.getGegnerischesTeamMatchId()).isEqualTo(501L);
    }

    @Test
    public void testFromDTO_mapsScheibennummerAndIds() {
        TabletSchusszettelDTO dto = new TabletSchusszettelDTO();
        dto.setStatus(TabletSchusszettelDTO.TabletSchusszettelStatus.WARTE);
        dto.setEigenesTeamMatchId(123L);
        dto.setGegnerischesTeamMatchId(456L);
        dto.setEigenesTeamScheibennummer(9L);

        TabletSchusszettelDO mapped = TabletSchusszettelMapper.fromDTO(dto);

        assertThat(mapped).isNotNull();
        assertThat(mapped.getStatus()).isEqualTo(TabletSchusszettelDO.TabletSchusszettelStatus.WARTE);
        assertThat(mapped.getEigenesTeamMatchId()).isEqualTo(123L);
        assertThat(mapped.getGegnerischesTeamMatchId()).isEqualTo(456L);
        assertThat(mapped.getEigenesTeamScheibennummer()).isEqualTo(9L);
    }

    @Test
    public void testFromDTO_nullStatus_defaultsToNotAllowed() {
        TabletSchusszettelDTO dto = new TabletSchusszettelDTO();
        dto.setStatus(null);

        TabletSchusszettelDO mapped = TabletSchusszettelMapper.fromDTO(dto);

        assertThat(mapped).isNotNull();
        assertThat(mapped.getStatus()).isEqualTo(TabletSchusszettelDO.TabletSchusszettelStatus.NOT_ALLOWED);
    }

    @Test
    public void testFromDTO_nullInput_returnsNull() {
        TabletSchusszettelDO mapped = TabletSchusszettelMapper.fromDTO(null);

        assertThat(mapped).isNull();
    }

    @Test
    public void testRoundTrip_preservesScheibennummer() {
        TabletSchusszettelDTO dto = TabletSchusszettelMapper.toDTO(doObj);
        TabletSchusszettelDO roundTrip = TabletSchusszettelMapper.fromDTO(dto);

        assertThat(roundTrip).isNotNull();
        assertThat(roundTrip.getEigenesTeamScheibennummer()).isEqualTo(7L);
        assertThat(roundTrip.getEigenesTeamMatchId()).isEqualTo(111L);
        assertThat(roundTrip.getGegnerischesTeamMatchId()).isEqualTo(222L);
    }

    @Test
    public void testFromDTO_nullScheibennummer_isPreserved() {
        TabletSchusszettelDTO dto = new TabletSchusszettelDTO();
        dto.setStatus(TabletSchusszettelDTO.TabletSchusszettelStatus.WARTE);
        dto.setEigenesTeamScheibennummer(null);

        TabletSchusszettelDO mapped = TabletSchusszettelMapper.fromDTO(dto);

        assertThat(mapped).isNotNull();
        assertThat(mapped.getEigenesTeamScheibennummer()).isNull();
    }
}
