package de.bogenliga.application.services.v1.trigger.model;
import org.junit.Test;
import junit.framework.TestCase;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class TriggerCountDTOTest extends TestCase {
    private static final Long count = 40000L;
    private static final Long altCount = 50000L;
    public TriggerCountDTO getExpectedCountDTO() {
        return new TriggerCountDTO(count);
    }

    @Test
    public void testGetCount(){
       TriggerCountDTO actualDTO = getExpectedCountDTO();
       Long actualCount = actualDTO.getCount();

       assertEquals(count, actualCount);
    }

    @Test
    public void testSetCount(){
        TriggerCountDTO actualDTO = getExpectedCountDTO();
        actualDTO.setCount(altCount);
        Long actualCount = actualDTO.getCount();

        assertEquals(altCount, actualCount);
    }
}
