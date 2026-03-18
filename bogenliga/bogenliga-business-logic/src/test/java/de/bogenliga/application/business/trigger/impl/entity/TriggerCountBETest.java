package de.bogenliga.application.business.trigger.impl.entity;
import org.junit.Test;
import junit.framework.TestCase;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class TriggerCountBETest extends TestCase {
    private static final Long COUNT = 40000L;
    private static final Long ALT_COUNT = 50000L;
    public TriggerCountBE getExpectedCountBE() {
        return new TriggerCountBE();
    }

    @Test
    public void testGetCount(){
        TriggerCountBE actualBE = getExpectedCountBE();
        actualBE.setCount(COUNT);
        Long actualCount = actualBE.getCount();

        assertEquals(COUNT, actualCount);
    }

    @Test
    public void testSetCount(){
        TriggerCountBE actualBE = getExpectedCountBE();
        actualBE.setCount(ALT_COUNT);
        Long actualCount = actualBE.getCount();

        assertEquals(ALT_COUNT, actualCount);
    }

    @Test
    public void testToString() {
        final TriggerCountBE actualBE = getExpectedCountBE();
        actualBE.setCount(COUNT);


        final String actual = actualBE.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(COUNT));
    }

}
