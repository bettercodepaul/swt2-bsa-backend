package de.bogenliga.application.business.schusszettel.api.types.inside;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TeamMatchInfoDOTest {

    @Test
    public void coverAllLines() {
        // Cover default constructor (lines 12-14)
        TeamMatchInfoDO team1 = new TeamMatchInfoDO();
        
        // Cover parameterized constructor (lines 16-20)
        TeamMatchInfoDO team2 = new TeamMatchInfoDO(1L, "Team Alpha", 5);
        
        // Cover all setters (lines 23, 26, 29)
        team1.setTeamId(2L);
        team1.setTeamName("Team Beta");
        team1.setMatchpunkte(3);
        
        // Cover all getters (lines 22, 25, 28)
        Long id1 = team1.getTeamId();
        String name1 = team1.getTeamName();
        Integer punkte1 = team1.getMatchpunkte();
        
        Long id2 = team2.getTeamId();
        String name2 = team2.getTeamName();
        Integer punkte2 = team2.getMatchpunkte();
        
        // Basic assertions
        assertThat(team1).isNotNull();
        assertThat(team2).isNotNull();
        assertThat(id1).isEqualTo(2L);
        assertThat(id2).isEqualTo(1L);
        assertThat(name2).isEqualTo("Team Alpha");
        assertThat(punkte2).isEqualTo(5);
        assertThat(punkte1).isEqualTo(3);
    }
}