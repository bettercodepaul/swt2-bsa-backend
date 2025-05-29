package de.bogenliga.application.services.v1.trigger.model;
import org.junit.Test;

import junit.framework.TestCase;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class TriggerCountDTOTest extends TestCase {
    private static final Long COUNT = 40000L;
    private static final Long ALTCOUT = 50000L;
    public TriggerCountDTO getExpectedCountDTO() {
        return new TriggerCountDTO(COUNT);
    }

    @Test
    public void testGetCount(){
       TriggerCountDTO actualDTO = getExpectedCountDTO();
       Long actualCount = actualDTO.getCount();

       assertEquals(COUNT, actualCount);
    }

    @Test
    public void testSetCount(){
        TriggerCountDTO actualDTO = getExpectedCountDTO();
        actualDTO.setCount(ALTCOUT);
        Long actualCount = actualDTO.getCount();

        assertEquals(ALTCOUT, actualCount);
    }
}
