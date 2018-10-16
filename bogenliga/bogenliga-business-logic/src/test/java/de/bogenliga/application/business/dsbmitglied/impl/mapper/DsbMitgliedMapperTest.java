package de.bogenliga.application.business.dsbmitglied.impl.mapper;

import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.dsbmitglied.impl.entity.DsbMitgliedBE;
import org.junit.Test;

import static de.bogenliga.application.business.dsbmitglied.impl.business.DsbMitgliedComponentImplTest.getDsbMitgliedBE;
import static de.bogenliga.application.business.dsbmitglied.impl.business.DsbMitgliedComponentImplTest.getDsbMitgliedDO;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
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
    private static final String GEBURTSDATUM = "1.9.1991";
    private static final String NATIONALITAET = "DE";
    private static final String MITGLIEDSNUMMER = "223344uu";
    private static final long VEREINSID = 2;
    private static final long USERID = 4242;


    @Test
    public void toVO() throws Exception {
        final DsbMitgliedBE dsbMitgliedBE = getDsbMitgliedBE();

        final DsbMitgliedDO actual = DsbMitgliedMapper.toDsbMitgliedDO.apply(dsbMitgliedBE);

        assertThat(actual.getId()).isEqualTo(ID);
        assertThat(actual.getVorname()).isEqualTo(VORNAME);
    }


    @Test
    public void toBE() throws Exception {
        final DsbMitgliedDO dsbMitgliedDO = getDsbMitgliedDO();

        final DsbMitgliedBE actual = DsbMitgliedMapper.toDsbMitgliedBE.apply(dsbMitgliedDO);

        assertThat(actual.getDsbMitgliedId()).isEqualTo(ID);
        assertThat(actual.getDsbMitgliedVorname()).isEqualTo(VORNAME);
    }
}