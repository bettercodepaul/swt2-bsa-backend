package de.bogenliga.application.business.dsbmitglied.impl.mapper;

import java.sql.Date;
import org.junit.Test;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.dsbmitglied.impl.entity.DsbMitgliedBE;
import static de.bogenliga.application.business.dsbmitglied.impl.business.DsbMitgliedComponentImplTest.getDsbMitgliedBE;
import static de.bogenliga.application.business.dsbmitglied.impl.business.DsbMitgliedComponentImplTest.getDsbMitgliedDO;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Yann Philippczyk, BettercallPaul gmbh
 * @see <a href="http://joel-costigliola.github.io/assertj/">
 * AssertJ: Fluent assertions for java</a>
 * @see <a href="https://junit.org/junit4/">
 * JUnit4</a>
 * @see <a href="https://site.mockito.org/">
 * Mockito</a>
 */
public class DsbMitgliedMapperTest {

    private static final long USER = 0;

    private static final long ID = 1337;
    private static final String VORNAME = "Sorscha";
    private static final String NACHNAME = "Kratikoff";
    private static final Date GEBURTSDATUM = Date.valueOf("1991-09-01");
    private static final String NATIONALITAET = "DE";
    private static final String MITGLIEDSNUMMER = "223344uu";
    private static final long VEREINSID = 2;
    private static final String VEREINNAME = "TEST VEREIN";
    private static final long USERID = 4242;

    private static final Boolean KAMPFRICHTER = true;

    private static final Date BEITRITTSDATUM = Date.valueOf("2001-01-01");

    private final int[] ZAHLENLISTE = {0,129, 173};
    private final String[] NAMENSLISTE = {"Allmendinger" , "Michael", "Gröner", "Alexander" , "Haag", "Axel"};

    @Test
    public void toDO() throws Exception {
        final DsbMitgliedBE dsbMitgliedBE = getDsbMitgliedBE();

        final DsbMitgliedDO actual = DsbMitgliedMapper.toDsbMitgliedDO.apply(dsbMitgliedBE);

        assertThat(actual.getId()).isEqualTo(ID);
        assertThat(actual.getVorname()).isEqualTo(VORNAME);

        final DsbMitgliedDO dsbMitgliedDO = new DsbMitgliedDO(ID, VORNAME, NACHNAME, GEBURTSDATUM, NATIONALITAET,
                MITGLIEDSNUMMER, VEREINSID, VEREINNAME, USERID, KAMPFRICHTER, BEITRITTSDATUM);
        assertThat(actual.getNachname()).isEqualTo(NACHNAME);
    }


    @Test
    public void toBE() throws Exception {
        final DsbMitgliedDO dsbMitgliedDO = getDsbMitgliedDO();

        final DsbMitgliedBE actual = DsbMitgliedMapper.toDsbMitgliedBE.apply(dsbMitgliedDO);

        assertThat(actual.getDsbMitgliedId()).isEqualTo(ID);
        assertThat(actual.getDsbMitgliedVorname()).isEqualTo(VORNAME);
    }

}