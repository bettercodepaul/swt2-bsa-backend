package de.bogenliga.application.business.schuetzenstatistikLetzeJahre.impl.entity;

import org.junit.Test;
import de.bogenliga.application.business.schuetzenstatistikletztejahre.impl.entity.SchuetzenstatistikLetzteJahreBE;
import static de.bogenliga.application.business.schuetzenstatistikLetzeJahre.impl.business.SchuetzenstatistikLetzteJahreImplTest.getSchuetzenstatistikLetzteJahreBE;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alessa Hackh
 */
public class SchuetzenstatistikLetzteJahreBETest {
    @Test
    public void assertToString() {
        final SchuetzenstatistikLetzteJahreBE underTest = getSchuetzenstatistikLetzteJahreBE();

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty();
    }

    @Test
    public void assertToString_withoutName() {
        final SchuetzenstatistikLetzteJahreBE underTest = getSchuetzenstatistikLetzteJahreBE();

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty();
    }
}
