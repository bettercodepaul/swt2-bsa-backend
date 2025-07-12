package de.bogenliga.application.business.schusszettel.api.types.inside;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class VerfuegbarerSchuetzeDOTest {

    @Test
    public void coverAllLines() {
        // Cover default constructor (lines 13-15)
        VerfuegbarerSchuetzeDO schuetze1 = new VerfuegbarerSchuetzeDO();
        
        // Cover parameterized constructor (lines 17-20)
        VerfuegbarerSchuetzeDO schuetze2 = new VerfuegbarerSchuetzeDO(1L, "Max Mustermann");
        
        // Cover all setters (lines 23, 26)
        schuetze1.setSchuetzenId(2L);
        schuetze1.setName("Anna Schmidt");
        
        // Cover all getters (lines 22, 25)
        Long id1 = schuetze1.getSchuetzenId();
        String name1 = schuetze1.getName();
        
        Long id2 = schuetze2.getSchuetzenId();
        String name2 = schuetze2.getName();
        
        // Basic assertions
        assertThat(schuetze1).isNotNull();
        assertThat(schuetze2).isNotNull();
        assertThat(id1).isEqualTo(2L);
        assertThat(id2).isEqualTo(1L);
        assertThat(name2).isEqualTo("Max Mustermann");
        assertThat(name1).isEqualTo("Anna Schmidt");
    }
}