package de.bogenliga.application.business.vereine.impl.entity;

import org.junit.Test;

import static de.bogenliga.application.business.vereine.impl.business.VereinComponentImplTest.getVereinBE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class VereinBETest {

    private static long VEREIN_ID = 0;
    private static String VEREIN_NAME="";

    @Test
    public void assertToString() {
        final VereinBE underTest = getVereinBE(); // Methode aus anderer Testklasse?
        underTest.setVereinId(VEREIN_ID);
        underTest.setVereinName(VEREIN_NAME);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(VEREIN_ID))
                .contains(VEREIN_NAME);
    }


    @Test
    public void assertToString_withoutVorname() {
        final VereinBE underTest = new VereinBE();
        underTest.setVereinId(VEREIN_ID);
        underTest.setVereinName(null);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(VEREIN_ID))
                .contains("null");
    }

}