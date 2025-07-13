package de.bogenliga.application.business.schusszettel.api.types;

import de.bogenliga.application.business.schusszettel.api.types.inside.SchuetzenSatzDO;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SatzEingabeDOTest {

    @Test
    public void coverAllLines() {
        // Cover default constructor (line 17)
        SatzEingabeDO eingabe1 = new SatzEingabeDO();
        
        // Create test data
        List<SchuetzenSatzDO> testList = Arrays.asList(new SchuetzenSatzDO());
        
        // Cover parameterized constructor (lines 20-22)
        SatzEingabeDO eingabe2 = new SatzEingabeDO(testList);
        
        // Cover setter (lines 28-30)
        eingabe1.setSatzeingabe(testList);
        
        // Cover getter (lines 24-26)
        List<SchuetzenSatzDO> result1 = eingabe1.getSatzeingabe();
        List<SchuetzenSatzDO> result2 = eingabe2.getSatzeingabe();
        
        // Basic assertions to satisfy test frameworks
        assertThat(eingabe1).isNotNull();
        assertThat(eingabe2).isNotNull();
        assertThat(result1).isEqualTo(testList);
        assertThat(result2).isEqualTo(testList);
    }
}