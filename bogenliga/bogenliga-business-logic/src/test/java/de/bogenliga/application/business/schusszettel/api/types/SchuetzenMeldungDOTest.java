package de.bogenliga.application.business.schusszettel.api.types;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SchuetzenMeldungDOTest {

    @Test
    public void coverAllLines() {
        // Cover constructor (implicit, no line to cover specifically)
        SchuetzenMeldungDO meldung = new SchuetzenMeldungDO();
        
        // Create test data
        List<Long> testList = Arrays.asList(1L, 2L, 3L);
        
        // Cover setter (lines 18-20)
        meldung.setGemeldeteSchuetzen(testList);
        
        // Cover getter (lines 14-16)
        List<Long> result = meldung.getGemeldeteSchuetzen();
        
        // Basic assertions
        assertThat(meldung).isNotNull();
        assertThat(result).isEqualTo(testList);
        assertThat(result).hasSize(3);
    }
}