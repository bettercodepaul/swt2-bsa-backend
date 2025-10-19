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

    private static final String DSB_MITGLIED_NAME  = "Max Mustermann";
    private static final int RUECKENNUMMER  = 5;
    private static final float PFEILPUNKTE_SCHNITT  = 3.7f;
    private static final float MATCH_1  = 56f;
    private static final float MATCH_2 = 543f;


    @Test
    public void assertToString() {
        final SchuetzenstatistikMatchBE underTest = getSchuetzenstatistikMatchBE();

        underTest.setRueckennummer(RUECKENNUMMER);
        underTest.setMatch1(MATCH_1);
        underTest.setMatch2(MATCH_2);
        underTest.setDsbMitgliedName(DSB_MITGLIED_NAME);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Float.toString(PFEILPUNKTE_SCHNITT))
                .contains(Integer.toString(RUECKENNUMMER))
                .contains(Float.toString(MATCH_1))
                .contains(Float.toString(MATCH_2))
                .contains(DSB_MITGLIED_NAME);
    }


    @Test
    public void assertToString_withoutName() {
        final SchuetzenstatistikMatchBE underTest = getSchuetzenstatistikMatchBE();
        underTest.setRueckennummer(RUECKENNUMMER);
        underTest.setDsbMitgliedName(null);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Integer.toString(RUECKENNUMMER))
                .contains("null");
    }

}