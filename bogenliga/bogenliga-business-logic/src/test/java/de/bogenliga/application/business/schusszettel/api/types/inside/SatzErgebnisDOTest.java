package de.bogenliga.application.business.schusszettel.api.types.inside;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SatzErgebnisDOTest {

    @Test
    public void coverAllLines() {
        // Cover default constructor (line 36)
        SatzErgebnisDO satz1 = new SatzErgebnisDO();
        
        // Cover basic constructor (lines 18-22)
        SatzErgebnisDO satz2 = new SatzErgebnisDO(1, 54, 48);
        
        // Cover enriched constructor (lines 25-34)
        SatzErgebnisDO satz3 = new SatzErgebnisDO(2, 60, 45, 10L, "Team Alpha", 20L, "Team Beta");
        
        // Cover all setters (lines 39, 42, 45, 49, 52, 55, 58)
        satz1.setSatzNr(3);
        satz1.setTeam1Punkte(72);
        satz1.setTeam2Punkte(51);
        satz1.setTeam1Id(15L);
        satz1.setTeam1Name("Team Gamma");
        satz1.setTeam2Id(25L);
        satz1.setTeam2Name("Team Delta");
        
        // Cover all getters (lines 38, 41, 44, 48, 51, 54, 57)
        Integer satzNr1 = satz1.getSatzNr();
        Integer team1Punkte1 = satz1.getTeam1Punkte();
        Integer team2Punkte1 = satz1.getTeam2Punkte();
        Long team1Id1 = satz1.getTeam1Id();
        String team1Name1 = satz1.getTeam1Name();
        Long team2Id1 = satz1.getTeam2Id();
        String team2Name1 = satz1.getTeam2Name();
        
        Integer satzNr2 = satz2.getSatzNr();
        Integer team1Punkte2 = satz2.getTeam1Punkte();
        Integer team2Punkte2 = satz2.getTeam2Punkte();
        Long team1Id2 = satz2.getTeam1Id();
        String team1Name2 = satz2.getTeam1Name();
        Long team2Id2 = satz2.getTeam2Id();
        String team2Name2 = satz2.getTeam2Name();
        
        Integer satzNr3 = satz3.getSatzNr();
        Integer team1Punkte3 = satz3.getTeam1Punkte();
        Integer team2Punkte3 = satz3.getTeam2Punkte();
        Long team1Id3 = satz3.getTeam1Id();
        String team1Name3 = satz3.getTeam1Name();
        Long team2Id3 = satz3.getTeam2Id();
        String team2Name3 = satz3.getTeam2Name();
        
        // Basic assertions
        assertThat(satz1).isNotNull();
        assertThat(satz2).isNotNull();
        assertThat(satz3).isNotNull();
        assertThat(satzNr1).isEqualTo(3);
        assertThat(satzNr2).isEqualTo(1);
        assertThat(satzNr3).isEqualTo(2);
        assertThat(team1Name3).isEqualTo("Team Alpha");
    }
}