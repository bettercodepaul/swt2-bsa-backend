package de.bogenliga.application.services.v1.schusszettel.mapper;

import org.junit.Test;

import de.bogenliga.application.business.schusszettel.api.types.inside.SatzErgebnisDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.SchuetzeMatchPunkteDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.SchuetzeStammdatenDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.TeamMatchInfoDO;
import de.bogenliga.application.services.v1.schusszettel.model.inside.SatzErgebnisDTO;
import de.bogenliga.application.services.v1.schusszettel.model.inside.SchuetzeMatchPunkteDTO;
import de.bogenliga.application.services.v1.schusszettel.model.inside.SchuetzeStammdatenDTO;
import de.bogenliga.application.services.v1.schusszettel.model.inside.TeamMatchInfoDTO;
import de.bogenliga.application.services.v1.schusszettel.model.inside.VerfuegbarerSchuetzeDTO;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testet die Mapper-Methoden von TabletSchusszettelListenMapper.
 * Fokus liegt auf Konvertierung von DO-Listen zu DTO-Listen.
 *
 * @author Marty Lauterbach
 */
public class TabletSchusszettelListenMapperTest {

    @Test
    public void test_toSchuetzeStammdatenDTOList_ok() {
        SchuetzeStammdatenDO input = new SchuetzeStammdatenDO(1L, 12, "Max", "Mustermann");
        List<SchuetzeStammdatenDTO> result = TabletSchusszettelListenMapper.toSchuetzenInfoDTOList(List.of(input));

        assertThat(result).hasSize(1);
        SchuetzeStammdatenDTO dto = result.get(0);
        assertThat(dto.getSchuetzenId()).isEqualTo(1L);
        assertThat(dto.getRueckennummer()).isEqualTo(12);
        assertThat(dto.getVorname()).isEqualTo("Max");
        assertThat(dto.getNachname()).isEqualTo("Mustermann");
    }

    @Test
    public void test_toSatzErgebnisDTOList_ok() {
        SatzErgebnisDO input = new SatzErgebnisDO();
        input.setSatzNr(1);
        input.setTeam1Punkte(55);
        input.setTeam2Punkte(53);

        List<SatzErgebnisDTO> result = TabletSchusszettelListenMapper.toSatzErgebnisDTOList(List.of(input));

        assertThat(result).hasSize(1);
        SatzErgebnisDTO dto = result.get(0);
        assertThat(dto.getSatzNr()).isEqualTo(1);
        assertThat(dto.getTeam1Punkte()).isEqualTo(55);
        assertThat(dto.getTeam2Punkte()).isEqualTo(53);
    }

    @Test
    public void test_toTeamMatchInfoDTOList_ok() {
        TeamMatchInfoDO input = new TeamMatchInfoDO(99L, "Team Blau", 4);
        List<TeamMatchInfoDTO> result = TabletSchusszettelListenMapper.toTeamMatchInfoDTOList(List.of(input));

        assertThat(result).hasSize(1);
        TeamMatchInfoDTO dto = result.get(0);
        assertThat(dto.getTeamId()).isEqualTo(99L);
        assertThat(dto.getTeamName()).isEqualTo("Team Blau");
        assertThat(dto.getMatchpunkte()).isEqualTo(4);
    }

    @Test
    public void test_toVerfuegbarerSchuetzeDTOList_ok() {
        SchuetzeStammdatenDO input = new SchuetzeStammdatenDO(42L, 7, "Anna", "Schmidt");
        List<VerfuegbarerSchuetzeDTO> result = TabletSchusszettelListenMapper.toVerfuegbarerSchuetzeDTOList(List.of(input));

        assertThat(result).hasSize(1);
        VerfuegbarerSchuetzeDTO dto = result.get(0);
        assertThat(dto.getSchuetzenId()).isEqualTo(42L);
        assertThat(dto.getName()).isEqualTo("Anna Schmidt"); // Concatenated
    }

    @Test
    public void test_toSchuetzeMatchPunkteDTOList_ok() {
        SchuetzeMatchPunkteDO input = new SchuetzeMatchPunkteDO(100L, 65);
        List<SchuetzeMatchPunkteDTO> result = TabletSchusszettelListenMapper.toSchuetzeMatchPunkteDTOList(List.of(input));

        assertThat(result).hasSize(1);
        SchuetzeMatchPunkteDTO dto = result.get(0);
        assertThat(dto.getSchuetzenId()).isEqualTo(100L);
        assertThat(dto.getPunkteBisher()).isEqualTo(65);
    }

    @Test
    public void test_nullAndEmptyInputs() {
        // All should return null or empty without throwing
        assertThat(TabletSchusszettelListenMapper.toSchuetzenInfoDTOList(null)).isNull();
        assertThat(TabletSchusszettelListenMapper.toSatzErgebnisDTOList(null)).isNull();
        assertThat(TabletSchusszettelListenMapper.toTeamMatchInfoDTOList(Collections.emptyList())).isEmpty();
        assertThat(TabletSchusszettelListenMapper.toVerfuegbarerSchuetzeDTOList(Collections.emptyList())).isEmpty();
        assertThat(TabletSchusszettelListenMapper.toSchuetzeMatchPunkteDTOList(Collections.emptyList())).isEmpty();
    }
}
