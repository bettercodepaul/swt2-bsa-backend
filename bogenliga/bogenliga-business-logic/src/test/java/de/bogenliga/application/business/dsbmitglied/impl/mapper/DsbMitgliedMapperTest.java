package de.bogenliga.application.business.dsbmitglied.impl.mapper;

import java.sql.Date;
import org.junit.Test;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.dsbmitglied.impl.entity.MitgliedBE;
import static de.bogenliga.application.business.dsbmitglied.impl.business.MitgliedComponentImplTest.getDsbMitgliedBE;
import static de.bogenliga.application.business.dsbmitglied.impl.business.MitgliedComponentImplTest.getDsbMitgliedDO;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Yann Philippczyk, eXXcellent solutions consulting & software gmbh
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
    private static final long USERID = 4242;


    @Test
    public void toDO() throws Exception {
        final MitgliedBE mitgliedBE = getDsbMitgliedBE();

        final DsbMitgliedDO actual = DsbMitgliedMapper.toDsbMitgliedDO.apply(mitgliedBE);

        assertThat(actual.getId()).isEqualTo(ID);
        assertThat(actual.getVorname()).isEqualTo(VORNAME);

        final DsbMitgliedDO dsbMitgliedDO = new DsbMitgliedDO(ID, VORNAME, NACHNAME, GEBURTSDATUM, NATIONALITAET,
                MITGLIEDSNUMMER, VEREINSID, USERID, false);
        assertThat(actual.equals(dsbMitgliedDO));
    }


    @Test
    public void toBE() throws Exception {
        final DsbMitgliedDO dsbMitgliedDO = getDsbMitgliedDO();

        final MitgliedBE actual = DsbMitgliedMapper.toDsbMitgliedBE.apply(dsbMitgliedDO);

        assertThat(actual.getDsbMitgliedId()).isEqualTo(ID);
        assertThat(actual.getDsbMitgliedVorname()).isEqualTo(VORNAME);
    }
}