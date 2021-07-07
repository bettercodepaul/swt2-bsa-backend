package de.bogenliga.application.business.schuetzenstatistik.impl.entity;

import org.junit.Test;
import static de.bogenliga.application.business.schuetzenstatistik.impl.business.SchuetzenstatistikComponentImplTest.getSchuetzenstatistikBE;
import static org.assertj.core.api.Assertions.assertThat;

public class SchuetzenstatistikBETest {

    private static final long ID = 1337;
    private static final String VEREINNAME = "Test Verein";

    @Test
    public void assertToString() {
        final SchuetzenstatistikBE underTest = getSchuetzenstatistikBE();
        underTest.setVeranstaltungId(ID);
        underTest.setVereinName(VEREINNAME);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(ID))
                .contains(VEREINNAME);
    }


    @Test
    public void assertToString_withoutName() {
        final SchuetzenstatistikBE underTest = getSchuetzenstatistikBE();
        underTest.setVeranstaltungId(ID);
        underTest.setVereinName(null);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(ID))
                .contains("null");
    }

}
