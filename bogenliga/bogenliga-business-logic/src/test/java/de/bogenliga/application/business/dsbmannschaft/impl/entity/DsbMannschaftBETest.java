package de.bogenliga.application.business.dsbmannschaft.impl.entity;

import org.junit.Test;

import static de.bogenliga.application.business.dsbmannschaft.impl.business.DsbMannschaftComponentImplTest.getDsbMannschaftBE;
import static org.assertj.core.api.Assertions.assertThat;

public class DsbMannschaftBETest {

    private static final Long USER = 0L;
    private static final Long VERSION = 0L;

    private static final long id = 2222L;
    private static final long vereinId=101010;
    private static final long nummer=111;
    private static final long benutzerId=12;
    private static final long veranstaltungId=1;

    @Test
    public void assertToString() {
        final DsbMannschaftBE underTest = getDsbMannschaftBE();
        underTest.setId(id);
        underTest.setVereinId(vereinId);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(id))
                .contains(Long.toString(vereinId));
    }


   /* @Test
    public void assertToString_withoutVorname() {
        final DsbMannschaftBE underTest = new DsbMannschaftBE();
        underTest.setId(id);
        underTest.setDsbMitgliedVorname(null);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(ID))
                .contains("null");
    }*/
}
