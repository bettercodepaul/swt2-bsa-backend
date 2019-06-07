package de.bogenliga.application.business.Setzliste.impl.entity;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author Michael Hesse, michael_maximilian.hesse@student.reutlingen-university.de
 */
public class SetzlisteBETest {

    private static final int LIGATABELLENPLATZ = 1;
    private static final long MANNSCHAFTSID = 5;
    private static final long WETTKAMPFID = 30;

    private SetzlisteBE getSetzlisteBE(){
        final SetzlisteBE setzlisteBE = new SetzlisteBE();
        setzlisteBE.setLigatabelleTabellenplatz(LIGATABELLENPLATZ);
        setzlisteBE.setMannschaftid(MANNSCHAFTSID);
        setzlisteBE.setWettkampfid(WETTKAMPFID);
        return setzlisteBE;
    }

    @Test
    public void assertToString() {
        final SetzlisteBE underTest = getSetzlisteBE();

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(MANNSCHAFTSID))
                .contains(Long.toString(WETTKAMPFID));

    }

}