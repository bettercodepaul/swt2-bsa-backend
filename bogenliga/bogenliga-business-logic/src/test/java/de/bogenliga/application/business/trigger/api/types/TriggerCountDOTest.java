package de.bogenliga.application.business.trigger.api.types;
import org.junit.Test;
import junit.framework.TestCase;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class TriggerCountDOTest extends TestCase {
    private static final Long COUNT = 40000L;
    private static final Long altCount =50000L;
    public TriggerCountDO getExpectedCountDO() {
        return new TriggerCountDO(COUNT);
    }

    @Test
    public void testGetCount(){
        TriggerCountDO actualDO = getExpectedCountDO();
        Long actualCount = actualDO.getCount();

        assertEquals(COUNT, actualCount);
    }

    @Test
    public void testSetCount(){
        TriggerCountDO actualDO = getExpectedCountDO();
        actualDO.setCount(altCount);
        Long actualCount = actualDO.getCount();

        assertEquals(altCount, actualCount);
    }

}
