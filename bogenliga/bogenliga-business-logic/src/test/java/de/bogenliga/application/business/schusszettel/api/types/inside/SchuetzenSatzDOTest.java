package de.bogenliga.application.business.schusszettel.api.types.inside;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SchuetzenSatzDOTest {

    @Test
    public void coverAllLines() {
        // Cover default constructor (line 16-18)
        SchuetzenSatzDO satz1 = new SchuetzenSatzDO();
        
        // Cover parameterized constructor (lines 20-25)
        SchuetzenSatzDO satz2 = new SchuetzenSatzDO(1L, 10, 9, 8);
        
        // Cover all setters (lines 31-33, 39-41, 47-49, 55-57)
        satz1.setSchuetzenId(2L);
        satz1.setSchuss1(7);
        satz1.setSchuss2(6);
        satz1.setSchuss3(5);
        
        // Cover all getters (lines 27-29, 35-37, 43-45, 51-53)
        Long id1 = satz1.getSchuetzenId();
        Integer s1_1 = satz1.getSchuss1();
        Integer s1_2 = satz1.getSchuss2();
        Integer s1_3 = satz1.getSchuss3();
        
        Long id2 = satz2.getSchuetzenId();
        Integer s2_1 = satz2.getSchuss1();
        Integer s2_2 = satz2.getSchuss2();
        Integer s2_3 = satz2.getSchuss3();
        
        // Basic assertions
        assertThat(satz1).isNotNull();
        assertThat(satz2).isNotNull();
        assertThat(id1).isEqualTo(2L);
        assertThat(id2).isEqualTo(1L);
        assertThat(s2_1).isEqualTo(10);
    }
}