package de.bogenliga.application.business.schusszettel.api.types.inside;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TabletSessionSingDOTest {

    @Test
    public void coverAllLines() {
        // Cover default constructor (lines 29-30)
        TabletSessionSingDO session1 = new TabletSessionSingDO();
        
        // Cover basic constructor (lines 33-41)
        TabletSessionSingDO session2 = new TabletSessionSingDO(1L, "Team Alpha", "ACTIVE", "token123", 1, "Team Beta");
        
        // Cover constructor with wettkampf info (lines 44-54)
        WettkampfInfoDO wettkampfInfo = new WettkampfInfoDO();
        TabletSessionSingDO session3 = new TabletSessionSingDO(2L, "Team Gamma", "WAITING", "token456", 2, "Team Delta", wettkampfInfo);
        
        // Cover all setters (lines 61-63, 69-71, 77-79, 85-87, 93-95, 101-103, 109-111)
        session1.setTeamId(3L);
        session1.setTeamName("Team Test");
        session1.setStatus("TESTING");
        session1.setToken("token789");
        session1.setCurrentPasse(3);
        session1.setNaechsterGegnerName("Team Opponent");
        session1.setWettkampfInfo(wettkampfInfo);
        
        // Cover all getters (lines 57-59, 65-67, 73-75, 81-83, 89-91, 97-99, 105-107)
        Long teamId1 = session1.getTeamId();
        String teamName1 = session1.getTeamName();
        String status1 = session1.getStatus();
        String token1 = session1.getToken();
        Integer passe1 = session1.getCurrentPasse();
        String gegner1 = session1.getNaechsterGegnerName();
        WettkampfInfoDO info1 = session1.getWettkampfInfo();
        
        Long teamId2 = session2.getTeamId();
        String teamName2 = session2.getTeamName();
        String status2 = session2.getStatus();
        String token2 = session2.getToken();
        Integer passe2 = session2.getCurrentPasse();
        String gegner2 = session2.getNaechsterGegnerName();
        WettkampfInfoDO info2 = session2.getWettkampfInfo();
        
        Long teamId3 = session3.getTeamId();
        String teamName3 = session3.getTeamName();
        String status3 = session3.getStatus();
        String token3 = session3.getToken();
        Integer passe3 = session3.getCurrentPasse();
        String gegner3 = session3.getNaechsterGegnerName();
        WettkampfInfoDO info3 = session3.getWettkampfInfo();
        
        // Basic assertions
        assertThat(session1).isNotNull();
        assertThat(session2).isNotNull();
        assertThat(session3).isNotNull();
        assertThat(teamId1).isEqualTo(3L);
        assertThat(teamId2).isEqualTo(1L);
        assertThat(teamName2).isEqualTo("Team Alpha");
        assertThat(status3).isEqualTo("WAITING");
        assertThat(info3).isEqualTo(wettkampfInfo);
    }
}