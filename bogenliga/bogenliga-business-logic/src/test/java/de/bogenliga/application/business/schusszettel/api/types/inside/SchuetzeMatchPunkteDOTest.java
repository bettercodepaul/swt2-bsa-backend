package de.bogenliga.application.business.schusszettel.api.types.inside;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SchuetzeMatchPunkteDOTest {

    @Test
    public void coverAllLines() {
        // Cover default constructor (lines 13-15)
        SchuetzeMatchPunkteDO schuetze1 = new SchuetzeMatchPunkteDO();
        
        // Cover parameterized constructor (lines 17-20)
        SchuetzeMatchPunkteDO schuetze2 = new SchuetzeMatchPunkteDO(1L, 25);
        
        // Cover all setters (lines 26-28, 34-36)
        schuetze1.setSchuetzenId(2L);
        schuetze1.setPunkteBisher(30);
        
        // Cover all getters (lines 22-24, 30-32)
        Long id1 = schuetze1.getSchuetzenId();
        Integer punkte1 = schuetze1.getPunkteBisher();
        
        Long id2 = schuetze2.getSchuetzenId();
        Integer punkte2 = schuetze2.getPunkteBisher();
        
        // Basic assertions
        assertThat(schuetze1).isNotNull();
        assertThat(schuetze2).isNotNull();
        assertThat(id1).isEqualTo(2L);
        assertThat(id2).isEqualTo(1L);
        assertThat(punkte2).isEqualTo(25);
    }
}