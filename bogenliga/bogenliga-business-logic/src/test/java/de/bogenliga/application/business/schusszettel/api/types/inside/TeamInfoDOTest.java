package de.bogenliga.application.business.schusszettel.api.types.inside;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TeamInfoDOTest {

    @Test
    public void coverAllLines() {
        // Cover default constructor (lines 13-15)
        TeamInfoDO team1 = new TeamInfoDO();
        
        // Cover parameterized constructor (lines 17-20)
        TeamInfoDO team2 = new TeamInfoDO(1L, "Team Alpha");
        
        // Cover all setters (lines 26-28, 34-36)
        team1.setTeamId(2L);
        team1.setTeamName("Team Beta");
        
        // Cover all getters (lines 22-24, 30-32)
        Long id1 = team1.getTeamId();
        String name1 = team1.getTeamName();
        
        Long id2 = team2.getTeamId();
        String name2 = team2.getTeamName();
        
        // Basic assertions
        assertThat(team1).isNotNull();
        assertThat(team2).isNotNull();
        assertThat(id1).isEqualTo(2L);
        assertThat(id2).isEqualTo(1L);
        assertThat(name2).isEqualTo("Team Alpha");
        assertThat(name1).isEqualTo("Team Beta");
    }
}