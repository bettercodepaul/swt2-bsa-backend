package de.bogenliga.application.business.schuetzenstatistikwettkampf.impl.entity;

import org.junit.Test;
import static de.bogenliga.application.business.schuetzenstatistikwettkampf.impl.business.SchuetzenstatistikWettkampfComponentImplTest.getSchuetzenstatistikWettkampfBE;
import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author Anna Baur
 */
public class SchuetzenstatistikWettkampfBETest {
    private static final long ID = 1337;
    @Test
    public void assertToString() {
        final SchuetzenstatistikWettkampfBE underTest = getSchuetzenstatistikWettkampfBE();
        underTest.setVeranstaltungId(ID);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(ID));
    }


    @Test
    public void assertToString_withoutName() {
        final SchuetzenstatistikWettkampfBE underTest = getSchuetzenstatistikWettkampfBE();
        underTest.setVeranstaltungId(ID);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(ID));
    }
}
