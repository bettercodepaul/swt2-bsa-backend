package de.bogenliga.application.services.v1.schusszettel.mapper;

import de.bogenliga.application.business.schusszettel.api.types.*;
import de.bogenliga.application.business.schusszettel.api.types.inside.*;
import de.bogenliga.application.services.v1.schusszettel.mapper.inside.TabletSessionSingMapper;
import de.bogenliga.application.services.v1.schusszettel.mapper.inside.TeamMatchInfoMapper;
import de.bogenliga.application.services.v1.schusszettel.mapper.inside.VerfuegbarerSchuetzeMapper;
import de.bogenliga.application.services.v1.schusszettel.model.*;
import de.bogenliga.application.services.v1.schusszettel.model.inside.*;
import org.junit.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * Tests that checks the functionality of all methods of all smaller schusszettel DTO/DO Mappers
 *
 * @author Marty Lauterbach
 */
public class TabletSmallMapperTest {

    // TabletSessionInfoMapper Tests
    // =============================================================================================

    @Test
    public void testTabletSessionInfoMapper_toDTOWithData() {
        // Arrange
        TabletSessionInfoDO sessionInfoDO = new TabletSessionInfoDO();
        sessionInfoDO.setWettkampfId(42L);

        TabletSessionSingDO[] singDOs = new TabletSessionSingDO[2];
        singDOs[0] = new TabletSessionSingDO(1L, "Team 1", "ACTIVE", "token1", 3, "Team 2");
        singDOs[1] = new TabletSessionSingDO(2L, "Team 2", "WAITING", "token2", 1, "Team 1");
        sessionInfoDO.setTabletSessionSingDOs(singDOs);

        // Act
        TabletSessionInfoDTO dto = TabletSessionInfoMapper.toDTO(sessionInfoDO);

        // Assert
        assertNotNull(dto);
        assertEquals(Long.valueOf(42L), Long.valueOf(dto.getWettkampfId()));
        assertEquals(Integer.valueOf(2), Integer.valueOf(dto.getTabletSessionSingDTOs().length));
        assertEquals("Team 1", dto.getTabletSessionSingDTOs()[0].getTeamName());
        assertEquals("WAITING", dto.getTabletSessionSingDTOs()[1].getStatus());
        assertEquals("token2", dto.getTabletSessionSingDTOs()[1].getToken());
    }

    @Test
    public void testTabletSessionInfoMapper_toDOWithData() {
        // Arrange
        TabletSessionInfoDTO sessionInfoDTO = new TabletSessionInfoDTO();
        sessionInfoDTO.setWettkampfId(42L);

        TabletSessionSingDTO[] singDTOs = new TabletSessionSingDTO[2];
        singDTOs[0] = new TabletSessionSingDTO(1L, "Team 1", "ACTIVE", "token1", 3, "Team 2");
        singDTOs[1] = new TabletSessionSingDTO(2L, "Team 2", "WAITING", "token2", 1, "Team 1");
        sessionInfoDTO.setTabletSessionSingDTOs(singDTOs);

        // Act
        TabletSessionInfoDO doObj = TabletSessionInfoMapper.toDO(sessionInfoDTO);

        // Assert
        assertNotNull(doObj);
        assertEquals(Long.valueOf(42L), Long.valueOf(doObj.getWettkampfId()));
        assertEquals(Integer.valueOf(2), Integer.valueOf(doObj.getTabletSessionSingDOs().length));
        assertEquals("Team 1", doObj.getTabletSessionSingDOs()[0].getTeamName());
        assertEquals("WAITING", doObj.getTabletSessionSingDOs()[1].getStatus());
        assertEquals("token2", doObj.getTabletSessionSingDOs()[1].getToken());
    }

    // =============================================================================================
    // TabletSchusszettelMapper Tests
    // =============================================================================================

    @Test
    public void testTabletSchusszettelMapper_toDTOWithData() {
        // Arrange
        TabletSchusszettelDO doObj = createSampleTabletSchusszettelDO();

        // Act
        TabletSchusszettelDTO dto = TabletSchusszettelMapper.toDTO(doObj);

        // Assert
        assertNotNull(dto);
        assertEquals(TabletSchusszettelDTO.TabletSchusszettelStatus.WARTE, dto.getStatus());
        assertEquals("Team 1", dto.getEigenesTeam().getTeamName());
        assertEquals("Team 2", dto.getGegnerischesTeam().getTeamName());
        assertEquals(Integer.valueOf(2), Integer.valueOf(dto.getSatzErgebnisse().size()));
        assertEquals(Integer.valueOf(2), Integer.valueOf(dto.getSchuetzenMatchPunkte().size()));
        assertEquals(Integer.valueOf(3), Integer.valueOf(dto.getSchuetzeStammDaten().size()));
        assertEquals(Integer.valueOf(2), Integer.valueOf(dto.getMatchErgebnis().size()));
        assertEquals(Integer.valueOf(1), Integer.valueOf(dto.getVerfuegbareSchuetzen().size()));
        assertEquals("John Doe", dto.getVerfuegbareSchuetzen().get(0).getName());
    }

    @Test
    public void testTabletSchusszettelMapper_toDTOWithNull() {
        // Act
        TabletSchusszettelDTO dto = TabletSchusszettelMapper.toDTO(null);

        // Assert
        assertNull(dto);
    }

    @Test
    public void testTabletSchusszettelMapper_fromDTOWithData() {
        // Arrange
        TabletSchusszettelDTO dto = createSampleTabletSchusszettelDTO();

        // Act
        TabletSchusszettelDO doObj = TabletSchusszettelMapper.fromDTO(dto);

        // Assert
        assertNotNull(doObj);
        assertEquals(TabletSchusszettelDO.TabletSchusszettelStatus.WARTE, doObj.getStatus());
        assertEquals("Team 1", doObj.getEigenesTeam().getTeamName());
        assertEquals("Team 2", doObj.getGegnerischesTeam().getTeamName());
        assertEquals(Integer.valueOf(2), Integer.valueOf(doObj.getSatzErgebnisse().size()));
        assertEquals(Integer.valueOf(2), Integer.valueOf(doObj.getSchuetzenMatchPunkte().size()));
        assertEquals(Integer.valueOf(3), Integer.valueOf(doObj.getSchuetzeStammDaten().size()));
        assertEquals(Integer.valueOf(2), Integer.valueOf(doObj.getMatchErgebnis().size()));
        assertEquals(Integer.valueOf(1), Integer.valueOf(doObj.getVerfuegbareSchuetzen().size()));
    }

    @Test
    public void testTabletSchusszettelMapper_fromDTOWithNull() {
        // Act
        TabletSchusszettelDO doObj = TabletSchusszettelMapper.fromDTO(null);

        // Assert
        assertNull(doObj);
    }

    // =============================================================================================
    // TabletSchusszettelListenMapper Tests
    // =============================================================================================

    @Test
    public void testTabletSchusszettelListenMapper_toSchuetzenInfoDTOList() {
        // Arrange
        List<SchuetzeStammdatenDO> doList = new ArrayList<>();
        doList.add(new SchuetzeStammdatenDO(1L, 10, "John", "Doe"));
        doList.add(new SchuetzeStammdatenDO(2L, 11, "Jane", "Doe"));

        // Act
        List<SchuetzeStammdatenDTO> dtoList = TabletSchusszettelListenMapper.toSchuetzenInfoDTOList(doList);

        // Assert
        assertEquals(Integer.valueOf(2), Integer.valueOf(dtoList.size()));
        assertEquals(Long.valueOf(1L), dtoList.get(0).getSchuetzenId());
        assertEquals("Jane", dtoList.get(1).getVorname());
    }

    @Test
    public void testTabletSchusszettelListenMapper_toSatzErgebnisDTOList() {
        // Arrange
        List<SatzErgebnisDO> doList = new ArrayList<>();
        doList.add(new SatzErgebnisDO(1, 10, 8));
        doList.add(new SatzErgebnisDO(2, 9, 9));

        // Act
        List<SatzErgebnisDTO> dtoList = TabletSchusszettelListenMapper.toSatzErgebnisDTOList(doList);

        // Assert
        assertEquals(Integer.valueOf(2), Integer.valueOf(dtoList.size()));
        assertEquals(Integer.valueOf(1), dtoList.get(0).getSatzNr());
        assertEquals(Integer.valueOf(9), dtoList.get(1).getTeam1Punkte());
        assertEquals(Integer.valueOf(9), dtoList.get(1).getTeam2Punkte());
    }

    @Test
    public void testTabletSchusszettelListenMapper_toTeamMatchInfoDTOList() {
        // Arrange
        List<TeamMatchInfoDO> doList = new ArrayList<>();
        doList.add(new TeamMatchInfoDO(1L, "Team 1", 6));
        doList.add(new TeamMatchInfoDO(2L, "Team 2", 4));

        // Act
        List<TeamMatchInfoDTO> dtoList = TabletSchusszettelListenMapper.toTeamMatchInfoDTOList(doList);

        // Assert
        assertEquals(Integer.valueOf(2), Integer.valueOf(dtoList.size()));
        assertEquals(Long.valueOf(1L), dtoList.get(0).getTeamId());
        assertEquals("Team 2", dtoList.get(1).getTeamName());
        assertEquals(Integer.valueOf(4), dtoList.get(1).getMatchpunkte());
    }

    @Test
    public void testTabletSchusszettelListenMapper_toVerfuegbarerSchuetzeDTOList() {
        // Arrange
        List<SchuetzeStammdatenDO> doList = new ArrayList<>();
        doList.add(new SchuetzeStammdatenDO(1L, 10, "John", "Doe"));
        doList.add(new SchuetzeStammdatenDO(2L, 11, "Jane", "Doe"));

        // Act
        List<VerfuegbarerSchuetzeDTO> dtoList = TabletSchusszettelListenMapper.toVerfuegbarerSchuetzeDTOList(doList);

        // Assert
        assertEquals(Integer.valueOf(2), Integer.valueOf(dtoList.size()));
        assertEquals(Long.valueOf(1L), dtoList.get(0).getSchuetzenId());
        assertEquals("John Doe", dtoList.get(0).getName());
        assertEquals("Jane Doe", dtoList.get(1).getName());
    }

    @Test
    public void testTabletSchusszettelListenMapper_toSchuetzeMatchPunkteDTOList() {
        // Arrange
        List<SchuetzeMatchPunkteDO> doList = new ArrayList<>();
        doList.add(new SchuetzeMatchPunkteDO(1L, 10));
        doList.add(new SchuetzeMatchPunkteDO(2L, 8));

        // Act
        List<SchuetzeMatchPunkteDTO> dtoList = TabletSchusszettelListenMapper.toSchuetzeMatchPunkteDTOList(doList);

        // Assert
        assertEquals(Integer.valueOf(2), Integer.valueOf(dtoList.size()));
        assertEquals(Long.valueOf(1L), dtoList.get(0).getSchuetzenId());
        assertEquals(Integer.valueOf(10), dtoList.get(0).getPunkteBisher());
        assertEquals(Integer.valueOf(8), dtoList.get(1).getPunkteBisher());
    }

    @Test
    public void testTabletSchusszettelListenMapper_toSchuetzeStammdatenDTOList() {
        // Arrange
        List<SchuetzeStammdatenDO> doList = new ArrayList<>();
        doList.add(new SchuetzeStammdatenDO(1L, 10, "John", "Doe"));
        doList.add(new SchuetzeStammdatenDO(2L, 11, "Jane", "Doe"));
        doList.add(new SchuetzeStammdatenDO(3L, 12, "Max", "Mustermann"));

        // Act
        List<SchuetzeStammdatenDTO> dtoList = TabletSchusszettelListenMapper.toSchuetzeStammdatenDTOList(doList);

        // Assert
        assertEquals(Integer.valueOf(3), Integer.valueOf(dtoList.size()));
        assertEquals(Long.valueOf(1L), dtoList.get(0).getSchuetzenId());
        assertEquals(Integer.valueOf(10), dtoList.get(0).getRueckennummer());
        assertEquals("John", dtoList.get(0).getVorname());
        assertEquals("Doe", dtoList.get(0).getNachname());
        assertEquals("Jane", dtoList.get(1).getVorname());
        assertEquals("Mustermann", dtoList.get(2).getNachname());
    }

    @Test
    public void testTabletSchusszettelListenMapper_toSchuetzeStammdatenDTOList_withNull() {
        // Act
        List<SchuetzeStammdatenDTO> dtoList = TabletSchusszettelListenMapper.toSchuetzeStammdatenDTOList(null);

        // Assert
        assertNull(dtoList);
    }

    // =============================================================================================
    // TabletSchusszettelDTOMapper Tests
    // =============================================================================================

    @Test
    public void testTabletSchusszettelDTOMapper_buildDTOFromData() {
        // Arrange
        TeamInfoDTO eigenesTeam = new TeamInfoDTO(1L, "Team 1");
        TeamInfoDTO gegnerischesTeam = new TeamInfoDTO(2L, "Team 2");

        List<SchuetzeMatchPunkteDO> schuetzenMatchPunkte = Arrays.asList(
            new SchuetzeMatchPunkteDO(1L, 10),
            new SchuetzeMatchPunkteDO(2L, 8)
        );

        List<SchuetzeStammdatenDO> eingesetzteSchuetzen = Arrays.asList(
            new SchuetzeStammdatenDO(1L, 10, "John", "Doe"),
            new SchuetzeStammdatenDO(2L, 11, "Jane", "Doe")
        );

        List<SatzErgebnisDO> satzErgebnisse = Arrays.asList(
            new SatzErgebnisDO(1, 10, 8),
            new SatzErgebnisDO(2, 9, 9)
        );

        List<TeamMatchInfoDO> matchErgebnis = Arrays.asList(
            new TeamMatchInfoDO(1L, "Team 1", 6),
            new TeamMatchInfoDO(2L, "Team 2", 4)
        );

        List<SchuetzeStammdatenDO> verfuegbareSchuetzen = List.of(
                new SchuetzeStammdatenDO(3L, 12, "Max", "Mustermann")
        );

        // Act
        TabletSchusszettelDTO dto = TabletSchusszettelDTOMapper.buildDTOFromData(
            TabletSchusszettelDTO.TabletSchusszettelStatus.WARTE,
            eigenesTeam,
            gegnerischesTeam,
            schuetzenMatchPunkte,
            eingesetzteSchuetzen,
            satzErgebnisse,
            matchErgebnis,
            verfuegbareSchuetzen
        );

        // Assert
        assertNotNull(dto);
        assertEquals(TabletSchusszettelDTO.TabletSchusszettelStatus.WARTE, dto.getStatus());
        assertEquals("Team 1", dto.getEigenesTeam().getTeamName());
        assertEquals("Team 2", dto.getGegnerischesTeam().getTeamName());
        assertEquals(Integer.valueOf(2), Integer.valueOf(dto.getSatzErgebnisse().size()));
        assertEquals(Integer.valueOf(2), Integer.valueOf(dto.getSchuetzenMatchPunkte().size()));
        assertEquals(Integer.valueOf(2), Integer.valueOf(dto.getSchuetzeStammDaten().size()));
        assertEquals(Integer.valueOf(2), Integer.valueOf(dto.getMatchErgebnis().size()));
        assertEquals(Integer.valueOf(1), Integer.valueOf(dto.getVerfuegbareSchuetzen().size()));
        assertEquals("Max Mustermann", dto.getVerfuegbareSchuetzen().get(0).getName());
    }

    @Test
    public void testTabletSchusszettelDTOMapper_fromRawSchuetzen() {
        // Arrange
        List<Object[]> rawData = new ArrayList<>();
        rawData.add(new Object[] { 1L, 10, "John", "Doe" });
        rawData.add(new Object[] { 2L, 11, "Jane", "Doe" });

        // Act
        List<SchuetzeStammdatenDTO> dtoList = TabletSchusszettelDTOMapper.fromRawSchuetzen(rawData);

        // Assert
        assertEquals(Integer.valueOf(2), Integer.valueOf(dtoList.size()));
        assertEquals(Long.valueOf(1L), dtoList.get(0).getSchuetzenId());
        assertEquals(Integer.valueOf(10), dtoList.get(0).getRueckennummer());
        assertEquals("John", dtoList.get(0).getVorname());
        assertEquals("Jane", dtoList.get(1).getVorname());
        assertEquals("Doe", dtoList.get(1).getNachname());
    }

    @Test
    public void testTabletSchusszettelDTOMapper_fromRawSatzdaten() {
        // Arrange
        List<Object[]> rawData = new ArrayList<>();
        rawData.add(new Object[] { 1, 10, 8 });
        rawData.add(new Object[] { 2, 9, 9 });

        // Act
        List<SatzErgebnisDTO> dtoList = TabletSchusszettelDTOMapper.fromRawSatzdaten(rawData);

        // Assert
        assertEquals(Integer.valueOf(2), Integer.valueOf(dtoList.size()));
        assertEquals(Integer.valueOf(1), dtoList.get(0).getSatzNr());
        assertEquals(Integer.valueOf(10), dtoList.get(0).getTeam1Punkte());
        assertEquals(Integer.valueOf(8), dtoList.get(0).getTeam2Punkte());
        assertEquals(Integer.valueOf(9), dtoList.get(1).getTeam1Punkte());
    }

    // =============================================================================================
    // TabletSchuetzenMeldungMapper Tests
    // =============================================================================================

    @Test
    public void testTabletSchuetzenMeldungMapper_toDO() {
        // Arrange
        SchuetzenMeldungDTO dto = new SchuetzenMeldungDTO();
        dto.setGemeldeteSchuetzen(Arrays.asList(1L, 2L, 3L));

        // Act
        SchuetzenMeldungDO doObj = TabletSchuetzenMeldungMapper.toDO(dto);

        // Assert
        assertThat(doObj).isNotNull();
        assertThat(doObj.getGemeldeteSchuetzen()).containsExactly(1L, 2L, 3L);
    }

    @Test
    public void testTabletSchuetzenMeldungMapper_toDTO() {
        // Arrange
        SchuetzenMeldungDO doObj = new SchuetzenMeldungDO();
        doObj.setGemeldeteSchuetzen(Arrays.asList(1L, 2L, 3L));

        // Act
        SchuetzenMeldungDTO dto = TabletSchuetzenMeldungMapper.toDTO(doObj);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.getGemeldeteSchuetzen()).containsExactly(1L, 2L, 3L);
    }

    @Test
    public void testTabletSchuetzenMeldungMapper_fromMap() {
        // Arrange
        Map<String, Object> payload = new HashMap<>();
        payload.put("gemeldete_schuetzen", Arrays.asList(1L, 2L, 3L));

        // Act
        SchuetzenMeldungDTO dto = TabletSchuetzenMeldungMapper.fromMap(payload);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.getGemeldeteSchuetzen()).containsExactly(1L, 2L, 3L);
    }

    @Test
    public void testTabletSchuetzenMeldungMapper_fromMapWithInvalidData() {
        // Arrange
        Map<String, Object> payload = new HashMap<>();
        // Missing the gemeldete_schuetzen key

        // Act
        SchuetzenMeldungDTO dto = TabletSchuetzenMeldungMapper.fromMap(payload);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.getGemeldeteSchuetzen()).isNull();
    }

    @Test
    public void testTabletSchuetzenMeldungMapper_fromMapWithMixedTypes() {
        // Arrange
        Map<String, Object> payload = new HashMap<>();
        List<Object> mixedList = new ArrayList<>();
        mixedList.add(1L); // Long
        mixedList.add("2"); // String that should be converted to Long
        payload.put("gemeldete_schuetzen", mixedList);

        // Act
        SchuetzenMeldungDTO dto = TabletSchuetzenMeldungMapper.fromMap(payload);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.getGemeldeteSchuetzen()).containsExactly(1L, 2L);
    }

    // =============================================================================================
    // TabletSatzEingabeMapper Tests
    // =============================================================================================

    @Test
    public void testTabletSatzEingabeMapper_toDO_fromDTO() {
        // Arrange
        SatzEingabeDTO dto = new SatzEingabeDTO();
        List<SchuetzenSatzDTO> schutzenSatzDTOs = new ArrayList<>();

        SchuetzenSatzDTO satz1 = new SchuetzenSatzDTO();
        satz1.setSchuetzenId(1L);
        satz1.setSchuss1(10);
        satz1.setSchuss2(9);
        satz1.setSchuss3(8);
        schutzenSatzDTOs.add(satz1);

        SchuetzenSatzDTO satz2 = new SchuetzenSatzDTO();
        satz2.setSchuetzenId(2L);
        satz2.setSchuss1(9);
        satz2.setSchuss2(8);
        satz2.setSchuss3(7);
        schutzenSatzDTOs.add(satz2);

        dto.setSatzeingabe(schutzenSatzDTOs);

        // Act
        SatzEingabeDO doObj = TabletSatzEingabeMapper.toDO(dto);

        // Assert
        assertNotNull(doObj);
        assertEquals(Integer.valueOf(2), Integer.valueOf(doObj.getSatzeingabe().size()));
        assertEquals(Long.valueOf(1L), doObj.getSatzeingabe().get(0).getSchuetzenId());
        assertEquals(Integer.valueOf(10), doObj.getSatzeingabe().get(0).getSchuss1());
        assertEquals(Integer.valueOf(8), doObj.getSatzeingabe().get(0).getSchuss3());
        assertEquals(Long.valueOf(2L), doObj.getSatzeingabe().get(1).getSchuetzenId());
    }

    @Test
    public void testTabletSatzEingabeMapper_toDTO_fromDO() {
        // Arrange
        SatzEingabeDO doObj = new SatzEingabeDO();
        List<SchuetzenSatzDO> schutzenSatzDOs = new ArrayList<>();

        schutzenSatzDOs.add(new SchuetzenSatzDO(1L, 10, 9, 8));
        schutzenSatzDOs.add(new SchuetzenSatzDO(2L, 9, 8, 7));

        doObj.setSatzeingabe(schutzenSatzDOs);

        // Act
        SatzEingabeDTO dto = TabletSatzEingabeMapper.toDTO(doObj);

        // Assert
        assertNotNull(dto);
        assertEquals(Integer.valueOf(2), Integer.valueOf(dto.getSatzeingabe().size()));
        assertEquals(Long.valueOf(1L), dto.getSatzeingabe().get(0).getSchuetzenId());
        assertEquals(Integer.valueOf(10), dto.getSatzeingabe().get(0).getSchuss1());
        assertEquals(Integer.valueOf(8), dto.getSatzeingabe().get(0).getSchuss3());
        assertEquals(Long.valueOf(2L), dto.getSatzeingabe().get(1).getSchuetzenId());
    }

    @Test
    public void testTabletSatzEingabeMapper_fromMap() {
        // Arrange
        Map<String, Object> payload = new HashMap<>();
        List<Map<String, Object>> satzeingabe = new ArrayList<>();

        Map<String, Object> satz1 = new HashMap<>();
        satz1.put("schuetzen_id", 1L);
        satz1.put("schuss1", 10);
        satz1.put("schuss2", 9);
        satz1.put("schuss3", 8);
        satzeingabe.add(satz1);

        Map<String, Object> satz2 = new HashMap<>();
        satz2.put("schuetzen_id", 2L);
        satz2.put("schuss1", 9);
        satz2.put("schuss2", 8);
        satz2.put("schuss3", 7);
        satzeingabe.add(satz2);

        payload.put("satzeingabe", satzeingabe);

        // Act
        SatzEingabeDTO dto = TabletSatzEingabeMapper.fromMap(payload);

        // Assert
        assertNotNull(dto);
        assertEquals(Integer.valueOf(2), Integer.valueOf(dto.getSatzeingabe().size()));
        assertEquals(Long.valueOf(1L), dto.getSatzeingabe().get(0).getSchuetzenId());
        assertEquals(Integer.valueOf(10), dto.getSatzeingabe().get(0).getSchuss1());
        assertEquals(Long.valueOf(2L), dto.getSatzeingabe().get(1).getSchuetzenId());
    }

    @Test
    public void testTabletSatzEingabeMapper_fromMapWithInvalidData() {
        // Arrange
        Map<String, Object> payload = new HashMap<>();
        // Missing the satzeingabe key

        // Act
        SatzEingabeDTO dto = TabletSatzEingabeMapper.fromMap(payload);

        // Assert
        assertNotNull(dto);
        assertNull(dto.getSatzeingabe());
    }

    @Test
    public void testTabletSatzEingabeMapper_fromMapWithMixedTypes() {
        // Arrange
        Map<String, Object> payload = new HashMap<>();
        List<Map<String, Object>> satzeingabe = new ArrayList<>();

        Map<String, Object> satz1 = new HashMap<>();
        satz1.put("schuetzen_id", 1L);
        satz1.put("schuss1", "10"); // String that should be converted to Integer
        satz1.put("schuss2", 9);
        satz1.put("schuss3", 8);
        satzeingabe.add(satz1);

        payload.put("satzeingabe", satzeingabe);

        // Act
        SatzEingabeDTO dto = TabletSatzEingabeMapper.fromMap(payload);

        // Assert
        assertNotNull(dto);
        assertEquals(1, dto.getSatzeingabe().size());
        assertEquals(Long.valueOf(1L), dto.getSatzeingabe().get(0).getSchuetzenId());
        assertEquals(Integer.valueOf(10), dto.getSatzeingabe().get(0).getSchuss1());
    }

    // =============================================================================================
    // Inside mappers
    // =============================================================================================

    @Test
    public void testVerfuegbarerSchuetzeMapper() {
        // Arrange
        VerfuegbarerSchuetzeDO doObj = new VerfuegbarerSchuetzeDO(1L, "John Doe");

        // Act & Assert - toDTO
        VerfuegbarerSchuetzeDTO dto = VerfuegbarerSchuetzeMapper.toDTO(doObj);
        assertEquals(Long.valueOf(1L), dto.getSchuetzenId());
        assertEquals("John Doe", dto.getName());

        // Act & Assert - toDO
        VerfuegbarerSchuetzeDO mappedDO = VerfuegbarerSchuetzeMapper.toDO(dto);
        assertEquals(Long.valueOf(1L), mappedDO.getSchuetzenId());
        assertEquals("John Doe", mappedDO.getName());

        // Test list mapping
        List<VerfuegbarerSchuetzeDO> doList = Arrays.asList(
                new VerfuegbarerSchuetzeDO(1L, "John Doe"),
                new VerfuegbarerSchuetzeDO(2L, "Jane Doe")
        );

        // Act - toDTOList
        List<VerfuegbarerSchuetzeDTO> dtoList = VerfuegbarerSchuetzeMapper.toDTOList(doList);
        assertEquals(Integer.valueOf(2), Integer.valueOf(dtoList.size()));
        assertEquals("Jane Doe", dtoList.get(1).getName());

        // Act - toDOList
        List<VerfuegbarerSchuetzeDO> mappedDOList = VerfuegbarerSchuetzeMapper.toDOList(dtoList);
        assertEquals(Integer.valueOf(2), Integer.valueOf(mappedDOList.size()));
        assertEquals("Jane Doe", mappedDOList.get(1).getName());
    }

    @Test
    public void testTeamMatchInfoMapper() {
        // Arrange
        TeamMatchInfoDO doObj = new TeamMatchInfoDO(1L, "Team A", 10);

        // Act & Assert - toDTO
        TeamMatchInfoDTO dto = TeamMatchInfoMapper.toDTO(doObj);
        assertEquals(Long.valueOf(1L), dto.getTeamId());
        assertEquals("Team A", dto.getTeamName());
        assertEquals(Integer.valueOf(10), dto.getMatchpunkte());

        // Act & Assert - toDO
        TeamMatchInfoDO mappedDO = TeamMatchInfoMapper.toDO(dto);
        assertEquals(Long.valueOf(1L), mappedDO.getTeamId());
        assertEquals("Team A", mappedDO.getTeamName());
        assertEquals(Integer.valueOf(10), mappedDO.getMatchpunkte());
    }

    @Test
    public void testTabletSessionSingMapper() {
        // Arrange
        TabletSessionSingDO doObj = new TabletSessionSingDO(1L, "Team A", "ACTIVE", "token123", 2, "Team B");

        // Act & Assert - toDTO
        TabletSessionSingDTO dto = TabletSessionSingMapper.mapToTabletSessionSingDTO(doObj);
        assertEquals(Long.valueOf(1L), dto.getTeamId());
        assertEquals("Team A", dto.getTeamName());
        assertEquals("ACTIVE", dto.getStatus());
        assertEquals("token123", dto.getToken());
        assertEquals(Integer.valueOf(2), dto.getCurrentPasse());
        assertEquals("Team B", dto.getNaechsterGegner());

        // Act & Assert - toDO
        TabletSessionSingDO mappedDO = TabletSessionSingMapper.mapToTabletSessionSingDO(dto);
        assertEquals(Long.valueOf(1L), mappedDO.getTeamId());
        assertEquals("Team A", mappedDO.getTeamName());
        assertEquals("ACTIVE", mappedDO.getStatus());
        assertEquals("token123", mappedDO.getToken());
        assertEquals(Integer.valueOf(2), mappedDO.getCurrentPasse());
        assertEquals("Team B", mappedDO.getNaechsterGegnerName());
    }

    // =============================================================================================
    // Tests for null handling in various mappers
    // =============================================================================================

    @Test
    public void testTabletSchusszettelListenMapper_nullHandling() {
        // Test all methods with null input
        assertNull(TabletSchusszettelListenMapper.toSchuetzenInfoDTOList(null));
        assertNull(TabletSchusszettelListenMapper.toSatzErgebnisDTOList(null));
        assertNull(TabletSchusszettelListenMapper.toTeamMatchInfoDTOList(null));
        assertNull(TabletSchusszettelListenMapper.toVerfuegbarerSchuetzeDTOList(null));
        assertNull(TabletSchusszettelListenMapper.toSchuetzeMatchPunkteDTOList(null));
    }

    // =============================================================================================
    // Helper methods
    // =============================================================================================

    private TabletSchusszettelDO createSampleTabletSchusszettelDO() {
        TabletSchusszettelDO doObj = new TabletSchusszettelDO();
        doObj.setStatus(TabletSchusszettelDO.TabletSchusszettelStatus.WARTE);
        
        // Eigenes Team
        TeamInfoDO eigenesTeam = new TeamInfoDO(1L, "Team 1");
        doObj.setEigenesTeam(eigenesTeam);
        
        // Gegnerisches Team
        TeamInfoDO gegnerischesTeam = new TeamInfoDO(2L, "Team 2");
        doObj.setGegnerischesTeam(gegnerischesTeam);
        
        // Satz Ergebnisse
        List<SatzErgebnisDO> satzErgebnisse = new ArrayList<>();
        satzErgebnisse.add(new SatzErgebnisDO(1, 10, 8));
        satzErgebnisse.add(new SatzErgebnisDO(2, 9, 9));
        doObj.setSatzErgebnisse(satzErgebnisse);
        
        // Schützen Match Punkte
        List<SchuetzeMatchPunkteDO> schuetzenMatchPunkte = new ArrayList<>();
        schuetzenMatchPunkte.add(new SchuetzeMatchPunkteDO(1L, 10));
        schuetzenMatchPunkte.add(new SchuetzeMatchPunkteDO(2L, 8));
        doObj.setSchuetzenMatchPunkte(schuetzenMatchPunkte);
        
        // Schütze Stammdaten
        List<SchuetzeStammdatenDO> schuetzeStammDaten = new ArrayList<>();
        schuetzeStammDaten.add(new SchuetzeStammdatenDO(1L, 10, "John", "Doe"));
        schuetzeStammDaten.add(new SchuetzeStammdatenDO(2L, 11, "Jane", "Doe"));
        schuetzeStammDaten.add(new SchuetzeStammdatenDO(3L, 12, "Max", "Mustermann"));
        doObj.setSchuetzeStammDaten(schuetzeStammDaten);
        
        // Match Ergebnis
        List<TeamMatchInfoDO> matchErgebnis = new ArrayList<>();
        matchErgebnis.add(new TeamMatchInfoDO(1L, "Team 1", 6));
        matchErgebnis.add(new TeamMatchInfoDO(2L, "Team 2", 4));
        doObj.setMatchErgebnis(matchErgebnis);
        
        // Verfügbare Schützen
        List<VerfuegbarerSchuetzeDO> verfuegbareSchuetzen = new ArrayList<>();
        verfuegbareSchuetzen.add(new VerfuegbarerSchuetzeDO(1L, "John Doe"));
        doObj.setVerfuegbareSchuetzen(verfuegbareSchuetzen);
        
        return doObj;
    }

    private TabletSchusszettelDTO createSampleTabletSchusszettelDTO() {
        TabletSchusszettelDTO dto = new TabletSchusszettelDTO();
        dto.setStatus(TabletSchusszettelDTO.TabletSchusszettelStatus.WARTE);
        
        // Eigenes Team
        TeamInfoDTO eigenesTeam = new TeamInfoDTO(1L, "Team 1");
        dto.setEigenesTeam(eigenesTeam);
        
        // Gegnerisches Team
        TeamInfoDTO gegnerischesTeam = new TeamInfoDTO(2L, "Team 2");
        dto.setGegnerischesTeam(gegnerischesTeam);
        
        // Satz Ergebnisse
        List<SatzErgebnisDTO> satzErgebnisse = new ArrayList<>();
        satzErgebnisse.add(new SatzErgebnisDTO(1, 10, 8));
        satzErgebnisse.add(new SatzErgebnisDTO(2, 9, 9));
        dto.setSatzErgebnisse(satzErgebnisse);
        
        // Schützen Match Punkte
        List<SchuetzeMatchPunkteDTO> schuetzenMatchPunkte = new ArrayList<>();
        schuetzenMatchPunkte.add(new SchuetzeMatchPunkteDTO(1L, 10));
        schuetzenMatchPunkte.add(new SchuetzeMatchPunkteDTO(2L, 8));
        dto.setSchuetzenMatchPunkte(schuetzenMatchPunkte);
        
        // Schütze Stammdaten
        List<SchuetzeStammdatenDTO> schuetzeStammDaten = new ArrayList<>();
        schuetzeStammDaten.add(new SchuetzeStammdatenDTO(1L, 10, "John", "Doe"));
        schuetzeStammDaten.add(new SchuetzeStammdatenDTO(2L, 11, "Jane", "Doe"));
        schuetzeStammDaten.add(new SchuetzeStammdatenDTO(3L, 12, "Max", "Mustermann"));
        dto.setSchuetzeStammDaten(schuetzeStammDaten);
        
        // Match Ergebnis
        List<TeamMatchInfoDTO> matchErgebnis = new ArrayList<>();
        matchErgebnis.add(new TeamMatchInfoDTO(1L, "Team 1", 6));
        matchErgebnis.add(new TeamMatchInfoDTO(2L, "Team 2", 4));
        dto.setMatchErgebnis(matchErgebnis);
        
        // Verfügbare Schützen
        List<VerfuegbarerSchuetzeDTO> verfuegbareSchuetzen = new ArrayList<>();
        verfuegbareSchuetzen.add(new VerfuegbarerSchuetzeDTO(1L, "John Doe"));
        dto.setVerfuegbareSchuetzen(verfuegbareSchuetzen);
        
        return dto;
    }
}
