package de.bogenliga.application.services.v1.schusszettel.model.inside;

import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * Test class for SatzErgebnisDTO
 * Tests all constructors, getters, and setters to achieve full coverage
 */
public class SatzErgebnisDTOTest {

    @Test
    public void testDefaultConstructor() {
        // Act
        SatzErgebnisDTO result = new SatzErgebnisDTO();

        // Assert
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getSatzNr()).isNull();
        Assertions.assertThat(result.getTeam1Punkte()).isNull();
        Assertions.assertThat(result.getTeam2Punkte()).isNull();
        Assertions.assertThat(result.getTeam1Id()).isNull();
        Assertions.assertThat(result.getTeam1Name()).isNull();
        Assertions.assertThat(result.getTeam2Id()).isNull();
        Assertions.assertThat(result.getTeam2Name()).isNull();
    }

    @Test
    public void testBasicConstructor() {
        // Arrange
        Integer satzNr = 1;
        Integer team1Punkte = 54;
        Integer team2Punkte = 48;

        // Act
        SatzErgebnisDTO result = new SatzErgebnisDTO(satzNr, team1Punkte, team2Punkte);

        // Assert
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getSatzNr()).isEqualTo(satzNr);
        Assertions.assertThat(result.getTeam1Punkte()).isEqualTo(team1Punkte);
        Assertions.assertThat(result.getTeam2Punkte()).isEqualTo(team2Punkte);
        
        // Team info should be null with basic constructor
        Assertions.assertThat(result.getTeam1Id()).isNull();
        Assertions.assertThat(result.getTeam1Name()).isNull();
        Assertions.assertThat(result.getTeam2Id()).isNull();
        Assertions.assertThat(result.getTeam2Name()).isNull();
    }

    @Test
    public void testEnrichedConstructor() {
        // Arrange
        Integer satzNr = 2;
        Integer team1Punkte = 60;
        Integer team2Punkte = 45;
        Long team1Id = 10L;
        String team1Name = "Team Alpha";
        Long team2Id = 20L;
        String team2Name = "Team Beta";

        // Act
        SatzErgebnisDTO result = new SatzErgebnisDTO(satzNr, team1Punkte, team2Punkte, 
                                                    team1Id, team1Name, team2Id, team2Name);

        // Assert
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getSatzNr()).isEqualTo(satzNr);
        Assertions.assertThat(result.getTeam1Punkte()).isEqualTo(team1Punkte);
        Assertions.assertThat(result.getTeam2Punkte()).isEqualTo(team2Punkte);
        Assertions.assertThat(result.getTeam1Id()).isEqualTo(team1Id);
        Assertions.assertThat(result.getTeam1Name()).isEqualTo(team1Name);
        Assertions.assertThat(result.getTeam2Id()).isEqualTo(team2Id);
        Assertions.assertThat(result.getTeam2Name()).isEqualTo(team2Name);
    }

    @Test
    public void testSettersAndGetters() {
        // Arrange
        SatzErgebnisDTO satzErgebnis = new SatzErgebnisDTO();
        Integer satzNr = 3;
        Integer team1Punkte = 72;
        Integer team2Punkte = 51;
        Long team1Id = 15L;
        String team1Name = "Team Gamma";
        Long team2Id = 25L;
        String team2Name = "Team Delta";

        // Act
        satzErgebnis.setSatzNr(satzNr);
        satzErgebnis.setTeam1Punkte(team1Punkte);
        satzErgebnis.setTeam2Punkte(team2Punkte);
        satzErgebnis.setTeam1Id(team1Id);
        satzErgebnis.setTeam1Name(team1Name);
        satzErgebnis.setTeam2Id(team2Id);
        satzErgebnis.setTeam2Name(team2Name);

        // Assert
        Assertions.assertThat(satzErgebnis.getSatzNr()).isEqualTo(satzNr);
        Assertions.assertThat(satzErgebnis.getTeam1Punkte()).isEqualTo(team1Punkte);
        Assertions.assertThat(satzErgebnis.getTeam2Punkte()).isEqualTo(team2Punkte);
        Assertions.assertThat(satzErgebnis.getTeam1Id()).isEqualTo(team1Id);
        Assertions.assertThat(satzErgebnis.getTeam1Name()).isEqualTo(team1Name);
        Assertions.assertThat(satzErgebnis.getTeam2Id()).isEqualTo(team2Id);
        Assertions.assertThat(satzErgebnis.getTeam2Name()).isEqualTo(team2Name);
    }

    @Test
    public void testNullValues() {
        // Arrange
        SatzErgebnisDTO satzErgebnis = new SatzErgebnisDTO(1, 50, 40);

        // Act - Set all values to null
        satzErgebnis.setSatzNr(null);
        satzErgebnis.setTeam1Punkte(null);
        satzErgebnis.setTeam2Punkte(null);
        satzErgebnis.setTeam1Id(null);
        satzErgebnis.setTeam1Name(null);
        satzErgebnis.setTeam2Id(null);
        satzErgebnis.setTeam2Name(null);

        // Assert
        Assertions.assertThat(satzErgebnis.getSatzNr()).isNull();
        Assertions.assertThat(satzErgebnis.getTeam1Punkte()).isNull();
        Assertions.assertThat(satzErgebnis.getTeam2Punkte()).isNull();
        Assertions.assertThat(satzErgebnis.getTeam1Id()).isNull();
        Assertions.assertThat(satzErgebnis.getTeam1Name()).isNull();
        Assertions.assertThat(satzErgebnis.getTeam2Id()).isNull();
        Assertions.assertThat(satzErgebnis.getTeam2Name()).isNull();
    }

    @Test
    public void testZeroValues() {
        // Arrange & Act
        SatzErgebnisDTO satzErgebnis = new SatzErgebnisDTO(0, 0, 0, 0L, "", 0L, "");

        // Assert
        Assertions.assertThat(satzErgebnis.getSatzNr()).isZero();
        Assertions.assertThat(satzErgebnis.getTeam1Punkte()).isZero();
        Assertions.assertThat(satzErgebnis.getTeam2Punkte()).isZero();
        Assertions.assertThat(satzErgebnis.getTeam1Id()).isZero();
        Assertions.assertThat(satzErgebnis.getTeam1Name()).isEmpty();
        Assertions.assertThat(satzErgebnis.getTeam2Id()).isZero();
        Assertions.assertThat(satzErgebnis.getTeam2Name()).isEmpty();
    }

    @Test
    public void testHighValues() {
        // Arrange & Act
        SatzErgebnisDTO satzErgebnis = new SatzErgebnisDTO(999, 9999, 8888);
        satzErgebnis.setTeam1Id(Long.MAX_VALUE);
        satzErgebnis.setTeam2Id(Long.MAX_VALUE - 1);
        satzErgebnis.setTeam1Name("Very Long Team Name That Could Potentially Be Used In Real Applications");
        satzErgebnis.setTeam2Name("Another Very Long Team Name For Testing Purposes");

        // Assert
        Assertions.assertThat(satzErgebnis.getSatzNr()).isEqualTo(999);
        Assertions.assertThat(satzErgebnis.getTeam1Punkte()).isEqualTo(9999);
        Assertions.assertThat(satzErgebnis.getTeam2Punkte()).isEqualTo(8888);
        Assertions.assertThat(satzErgebnis.getTeam1Id()).isEqualTo(Long.MAX_VALUE);
        Assertions.assertThat(satzErgebnis.getTeam2Id()).isEqualTo(Long.MAX_VALUE - 1);
        Assertions.assertThat(satzErgebnis.getTeam1Name())
            .isEqualTo("Very Long Team Name That Could Potentially Be Used In Real Applications");
        Assertions.assertThat(satzErgebnis.getTeam2Name())
            .isEqualTo("Another Very Long Team Name For Testing Purposes");
    }

    @Test
    public void testSpecialCharactersInTeamNames() {
        // Arrange & Act
        SatzErgebnisDTO satzErgebnis = new SatzErgebnisDTO();
        satzErgebnis.setTeam1Name("Team Ä-Ö-Ü & ß");
        satzErgebnis.setTeam2Name("Team 中文 русский العربية");

        // Assert
        Assertions.assertThat(satzErgebnis.getTeam1Name()).isEqualTo("Team Ä-Ö-Ü & ß");
        Assertions.assertThat(satzErgebnis.getTeam2Name()).isEqualTo("Team 中文 русский العربية");
    }
}