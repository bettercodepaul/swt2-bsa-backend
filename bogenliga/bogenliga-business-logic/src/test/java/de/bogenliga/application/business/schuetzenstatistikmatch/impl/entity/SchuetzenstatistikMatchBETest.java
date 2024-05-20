package de.bogenliga.application.business.schuetzenstatistikmatch.impl.entity;

import org.junit.Test;
import de.bogenliga.application.business.schuetzenstatistik.impl.entity.SchuetzenstatistikBE;
import static de.bogenliga.application.business.schuetzenstatistik.impl.business.SchuetzenstatistikComponentImplTest.getSchuetzenstatistikBE;
import static de.bogenliga.application.business.schuetzenstatistikmatch.impl.business.SchuetzenstatistikMatchComponentImplTest.getSchuetzenstatistikMatchBE;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class to test the functionality on SchuetzenstatistikMatchBE
 *
 * @author Lennart Raach
 */
public class SchuetzenstatistikMatchBETest {

    private static final String dsbMitgliedName = "Max Mustermann";
    private static final int rueckennummer = 5;
    private static final float pfeilpunkteSchnitt = (float) 3.7;
    private static final float match1 = 56f;
    private static final float match2 = 543f;


    @Test
    public void assertToString() {
        final SchuetzenstatistikMatchBE underTest = getSchuetzenstatistikMatchBE();

        underTest.setRueckennummer(rueckennummer);
        underTest.setMatch1(match1);
        underTest.setMatch2(match2);
        underTest.setDsbMitgliedName(dsbMitgliedName);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Float.toString(pfeilpunkteSchnitt))
                .contains(Integer.toString(rueckennummer))
                .contains(Float.toString(match1))
                .contains(Float.toString(match2))
                .contains(dsbMitgliedName);
    }


    @Test
    public void assertToString_withoutName() {
        final SchuetzenstatistikMatchBE underTest = getSchuetzenstatistikMatchBE();
        underTest.setRueckennummer(rueckennummer);
        underTest.setDsbMitgliedName(null);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Integer.toString(rueckennummer))
                .contains("null");
    }

}
