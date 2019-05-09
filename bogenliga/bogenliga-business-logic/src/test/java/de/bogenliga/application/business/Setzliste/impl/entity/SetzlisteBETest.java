package de.bogenliga.application.business.Setzliste.impl.entity;

import java.sql.Date;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author Michael Hesse, michael_maximilian.hesse@student.reutlingen-university.de
 */
public class SetzlisteBETest {

    private static final int LIGATABELLENPLATZ = 1;
    private static final int MANSCHAFTSNUMMER = 1;
    private static final String VEREINNAME = "TestVerein";
    private static final String VERANSTALTUNGSNAME = "TestVeranstaltung";
    private static final Integer WETTKAMPFTAG = 1;
    private static final Date WETTKAMPFDATUM = new Date(987654321098L);
    private static final String WETTKAMPFBEGINN = "13.00";
    private static final String WETTKAMPFORT = "Reutlingen";


    private SetzlisteBE getSetzlisteBE(){
        final SetzlisteBE setzlisteBE = new SetzlisteBE();
        setzlisteBE.setLigatabelleTabellenplatz(LIGATABELLENPLATZ);
        setzlisteBE.setMannschaftNummer(MANSCHAFTSNUMMER);
        setzlisteBE.setVereinName(VEREINNAME);
        setzlisteBE.setVeranstaltungName(VERANSTALTUNGSNAME);
        setzlisteBE.setWettkampfTag(WETTKAMPFTAG);
        setzlisteBE.setWettkampfDatum(WETTKAMPFDATUM);
        setzlisteBE.setWettkampfBeginn(WETTKAMPFBEGINN);
        setzlisteBE.setWettkampfOrt(WETTKAMPFORT);
        return setzlisteBE;
    }

    @Test
    public void assertToString() {
        final SetzlisteBE underTest = getSetzlisteBE();

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(MANSCHAFTSNUMMER))
                .contains(VEREINNAME)
                .contains(VERANSTALTUNGSNAME)
                .contains(Integer.toString(WETTKAMPFTAG))
                .contains(WETTKAMPFBEGINN)
                .contains(WETTKAMPFORT);

    }

}