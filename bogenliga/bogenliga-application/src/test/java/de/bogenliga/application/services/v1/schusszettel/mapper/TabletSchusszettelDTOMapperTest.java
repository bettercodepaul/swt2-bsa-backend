package de.bogenliga.application.services.v1.schusszettel.mapper;

import de.bogenliga.application.business.schusszettel.api.types.inside.SatzErgebnisDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.SchuetzeMatchPunkteDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.SchuetzeStammdatenDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.TeamMatchInfoDO;
import de.bogenliga.application.services.v1.schusszettel.model.*;
import org.junit.Test;

import de.bogenliga.application.services.v1.schusszettel.model.inside.TeamInfoDTO;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for the {@link TabletSchusszettelMapper} class.
 *
 * Verifies the correct mapping from the business-layer object {@code TabletSchusszettelDO}
 * to the API-layer object {@code TabletSchusszettelDTO}, including:
 *
 *
 * Status conversion
 * Team metadata mapping
 * Schützenpunkte und Stammdaten
 * Satz- und Match-Ergebnisse
 * Verfügbare Schützen
 *
 *
 * Ensures compliance with the expected JSON structure of the REST API.
 *
 * @author Marty Lauterbach
 */
public class TabletSchusszettelDTOMapperTest {

    @Test
    public void buildDTOFromData_shouldMapCorrectly() {
        // Prepare test data
        TabletSchusszettelDTO.TabletSchusszettelStatus status = TabletSchusszettelDTO.TabletSchusszettelStatus.SATZEINGABE;

        TeamInfoDTO eigenesTeam = new TeamInfoDTO(1L, "SV Reutlingen");
        TeamInfoDTO gegnerischesTeam = new TeamInfoDTO(2L, "BSG Tübingen");

        List<SchuetzeStammdatenDO> eingesetzteSchuetzen = List.of(
                new SchuetzeStammdatenDO(101L, 12, "Max", "Mustermann"),
                new SchuetzeStammdatenDO(102L, 14, "Erika", "Mustermann")
        );

        List<SatzErgebnisDO> satzErgebnisse = List.of(
                new SatzErgebnisDO(1, 55, 53),
                new SatzErgebnisDO(2, 57, 57)
        );

        List<TeamMatchInfoDO> matchErgebnis = List.of(
                new TeamMatchInfoDO(1L, "SV Reutlingen", 3),
                new TeamMatchInfoDO(2L, "BSG Tübingen", 1)
        );

        List<SchuetzeStammdatenDO> verfuegbareSchuetzen = List.of(
                new SchuetzeStammdatenDO(201L, 21, "Lena", "Maier"),
                new SchuetzeStammdatenDO(202L, 22, "Timo", "Schulz")
        );

        List<SchuetzeMatchPunkteDO> schuetzenMatchPunkte = List.of(
                new SchuetzeMatchPunkteDO(101L, 56),
                new SchuetzeMatchPunkteDO(102L, 49)
        );

        TabletSchusszettelDTO dto = TabletSchusszettelDTOMapper.buildDTOFromData(
                status,
                eigenesTeam,
                gegnerischesTeam,
                schuetzenMatchPunkte,
                eingesetzteSchuetzen,
                satzErgebnisse,
                matchErgebnis,
                verfuegbareSchuetzen
        );

        // Assertions
        assertThat(dto).isNotNull();
        assertThat(dto.getStatus()).isEqualTo(status);
        assertThat(dto.getEigenesTeam().getTeamName()).isEqualTo("SV Reutlingen");
        assertThat(dto.getGegnerischesTeam().getTeamName()).isEqualTo("BSG Tübingen");

        assertThat(dto.getSchuetzeStammDaten()).hasSize(2);
        assertThat(dto.getSchuetzeStammDaten().get(0).getVorname()).isEqualTo("Max");

        assertThat(dto.getSatzErgebnisse()).hasSize(2);
        assertThat(dto.getSatzErgebnisse().get(1).getTeam2Punkte()).isEqualTo(57);

        assertThat(dto.getMatchErgebnis()).hasSize(2);
        assertThat(dto.getMatchErgebnis().get(0).getMatchpunkte()).isEqualTo(3);

        assertThat(dto.getVerfuegbareSchuetzen()).hasSize(2);
        assertThat(dto.getVerfuegbareSchuetzen().get(0).getName()).contains("Lena");
    }
}
