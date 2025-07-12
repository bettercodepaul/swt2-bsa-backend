package de.bogenliga.application.business.schusszettel.impl.entity;

import org.junit.Test;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for TabletSchusszettelEntity database entity.
 * Tests entity getters, setters, and timestamp functionality.
 */
public class TabletSchusszettelEntityTest {

    @Test
    public void coverAllLines() {
        // Cover default constructor 
        TabletSchusszettelEntity entity = new TabletSchusszettelEntity();
        
        // Create test data
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        
        // Cover all setters
        entity.setId(1L);
        entity.setToken("test-token");
        entity.setTeamId(100L);
        entity.setWettkampfId(200L);
        entity.setCurrentMatchId(300L);
        entity.setCurrentMatchNr(1);
        entity.setCurrentPasseNumber(2);
        entity.setStatus("SCHUETZENMELDUNG");
        entity.setGegnerTeamId(400L);
        entity.setLastUpdatedNow(); // Set timestamp to current time
        
        // Cover all getters
        Long id = entity.getId();
        String token = entity.getToken();
        Long teamId = entity.getTeamId();
        Long wettkampfId = entity.getWettkampfId();
        Long currentMatchId = entity.getCurrentMatchId();
        Integer currentMatchNr = entity.getCurrentMatchNr();
        Integer currentPasseNumber = entity.getCurrentPasseNumber();
        String status = entity.getStatus();
        Long gegnerTeamId = entity.getGegnerTeamId();
        Timestamp lastUpdated = entity.getLastUpdated();
        
        // Basic assertions
        assertThat(entity).isNotNull();
        assertThat(id).isEqualTo(1L);
        assertThat(token).isEqualTo("test-token");
        assertThat(teamId).isEqualTo(100L);
        assertThat(status).isEqualTo("SCHUETZENMELDUNG");
        assertThat(currentPasseNumber).isEqualTo(2);
        assertThat(lastUpdated).isNotNull(); // Timestamp is set by setLastUpdatedNow()
    }
}