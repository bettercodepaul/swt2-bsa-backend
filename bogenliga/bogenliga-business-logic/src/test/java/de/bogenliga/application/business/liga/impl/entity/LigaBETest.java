package de.bogenliga.application.business.liga.impl.entity;

import org.junit.Test;
import static de.bogenliga.application.business.liga.impl.business.LigaComponentImplTest.getLigaBE;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * I'm testing the LigaBE Class
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
public class LigaBETest {
    private static final long USER = 0;

    private static final long ID = 1337;
    private static final String LIGANAME = "Test Liga";


    @Test
    public void assertToString() {
        final LigaBE underTest = getLigaBE();
        underTest.setLigaId(ID);
        underTest.setLigaName(LIGANAME);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(ID))
                .contains(LIGANAME);
    }


    @Test
    public void assertToString_withoutName() {
        final LigaBE underTest = getLigaBE();
        underTest.setLigaId(ID);
        underTest.setLigaName(null);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(ID))
                .contains("null");
    }
}