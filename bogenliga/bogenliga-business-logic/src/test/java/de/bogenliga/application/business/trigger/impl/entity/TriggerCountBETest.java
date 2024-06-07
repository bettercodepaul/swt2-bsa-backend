package de.bogenliga.application.business.trigger.impl.entity;
import org.junit.Test;
import junit.framework.TestCase;
import static de.bogenliga.application.business.trigger.impl.business.TriggerComponentImplTest.getTriggerBE;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class TriggerCountBETest extends TestCase {
    private static final Long count = 40000L;
    private static final Long altCount = 50000L;
    public TriggerCountBE getExpectedCountBE() {
        return new TriggerCountBE();
    }

    @Test
    public void testGetCount(){
        TriggerCountBE actualBE = getExpectedCountBE();
        actualBE.setCount(count);
        Long actualCount = actualBE.getCount();

        assertEquals(count, actualCount);
    }

    @Test
    public void testSetCount(){
        TriggerCountBE actualBE = getExpectedCountBE();
        actualBE.setCount(altCount);
        Long actualCount = actualBE.getCount();

        assertEquals(altCount, actualCount);
    }

    @Test
    public void testToString() {
        final TriggerCountBE actualBE = getExpectedCountBE();
        actualBE.setCount(count);


        final String actual = actualBE.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(count));
    }

}
