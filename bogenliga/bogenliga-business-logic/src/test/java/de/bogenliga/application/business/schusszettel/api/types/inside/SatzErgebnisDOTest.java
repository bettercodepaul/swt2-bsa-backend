package de.bogenliga.application.business.schusszettel.api.types.inside;

import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * Test class for SatzErgebnisDO
 * Tests all constructors, getters, and setters to achieve full coverage
 */
public class SatzErgebnisDOTest {

    @Test
    public void testDefaultConstructor() {
        // Act
        SatzErgebnisDO result = new SatzErgebnisDO();

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
        SatzErgebnisDO result = new SatzErgebnisDO(satzNr, team1Punkte, team2Punkte);

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
        SatzErgebnisDO result = new SatzErgebnisDO(satzNr, team1Punkte, team2Punkte, 
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
        SatzErgebnisDO satzErgebnis = new SatzErgebnisDO();
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
        SatzErgebnisDO satzErgebnis = new SatzErgebnisDO(1, 50, 40);

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
        SatzErgebnisDO satzErgebnis = new SatzErgebnisDO(0, 0, 0, 0L, "", 0L, "");

        // Assert
        Assertions.assertThat(satzErgebnis.getSatzNr()).isEqualTo(0);
        Assertions.assertThat(satzErgebnis.getTeam1Punkte()).isEqualTo(0);
        Assertions.assertThat(satzErgebnis.getTeam2Punkte()).isEqualTo(0);
        Assertions.assertThat(satzErgebnis.getTeam1Id()).isEqualTo(0L);
        Assertions.assertThat(satzErgebnis.getTeam1Name()).isEqualTo("");
        Assertions.assertThat(satzErgebnis.getTeam2Id()).isEqualTo(0L);
        Assertions.assertThat(satzErgebnis.getTeam2Name()).isEqualTo("");
    }

    @Test
    public void testNegativeValues() {
        // Arrange & Act
        SatzErgebnisDO satzErgebnis = new SatzErgebnisDO(-1, -10, -5);
        satzErgebnis.setTeam1Id(-100L);
        satzErgebnis.setTeam2Id(-200L);

        // Assert
        Assertions.assertThat(satzErgebnis.getSatzNr()).isEqualTo(-1);
        Assertions.assertThat(satzErgebnis.getTeam1Punkte()).isEqualTo(-10);
        Assertions.assertThat(satzErgebnis.getTeam2Punkte()).isEqualTo(-5);
        Assertions.assertThat(satzErgebnis.getTeam1Id()).isEqualTo(-100L);
        Assertions.assertThat(satzErgebnis.getTeam2Id()).isEqualTo(-200L);
    }
}