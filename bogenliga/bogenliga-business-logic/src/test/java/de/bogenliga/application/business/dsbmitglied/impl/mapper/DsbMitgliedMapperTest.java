package de.bogenliga.application.business.dsbmitglied.impl.mapper;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import org.junit.Test;
import de.bogenliga.application.business.datatransfer.mapper.BL_DBOMapper;
import de.bogenliga.application.business.datatransfer.model.SchuetzeDBO;
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
    private static final long USERID = 4242;


    @Test
    public void toDO() throws Exception {
        final DsbMitgliedBE dsbMitgliedBE = getDsbMitgliedBE();

        final DsbMitgliedDO actual = DsbMitgliedMapper.toDsbMitgliedDO.apply(dsbMitgliedBE);

        assertThat(actual.getId()).isEqualTo(ID);
        assertThat(actual.getVorname()).isEqualTo(VORNAME);

        final DsbMitgliedDO dsbMitgliedDO = new DsbMitgliedDO(ID, VORNAME, NACHNAME, GEBURTSDATUM, NATIONALITAET,
                MITGLIEDSNUMMER, VEREINSID, USERID, false);
        assertThat(actual.getNachname()).isEqualTo(NACHNAME);
    }


    @Test
    public void toBE() throws Exception {
        final DsbMitgliedDO dsbMitgliedDO = getDsbMitgliedDO();

        final DsbMitgliedBE actual = DsbMitgliedMapper.toDsbMitgliedBE.apply(dsbMitgliedDO);

        assertThat(actual.getDsbMitgliedId()).isEqualTo(ID);
        assertThat(actual.getDsbMitgliedVorname()).isEqualTo(VORNAME);
    }

    @Test
    public void testMapperWithCommaFormat() throws Exception {

        // Creating an instance of the Mapper class
        BL_DBOMapper blDboMapper = new BL_DBOMapper();

        List<SchuetzeDBO> schuetzeList = null;

        try {
            schuetzeList = blDboMapper.mapSchuetze();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //name of schuetze at index 0 is separated by comma: Allmendinger, Michael
        SchuetzeDBO schuetzeDBO = schuetzeList.get(0);
        assertThat(schuetzeDBO.getDsb_mitglied_nachname()).isEqualTo("Allmendinger");
        assertThat(schuetzeDBO.getDsb_mitglied_vorname()).isEqualTo("Michael");
    }

    @Test
    public void testMapperWithSpaceFormat() throws Exception {

        // Creating an instance of the Mapper class
        BL_DBOMapper blDboMapper = new BL_DBOMapper();

        List<SchuetzeDBO> schuetzeList = null;

        try {
            schuetzeList = blDboMapper.mapSchuetze();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //name of schuetze at index 129 is separated by two spaces before and after comma: Alexander , Gröner
        SchuetzeDBO schuetzeDBO = schuetzeList.get(129);
        assertThat(schuetzeDBO.getDsb_mitglied_nachname()).isEqualTo("Gröner");
        assertThat(schuetzeDBO.getDsb_mitglied_vorname()).isEqualTo("Alexander");
    }

    @Test
    public void testMapperWithoutCommaFormat() throws Exception {

        // Creating an instance of the Mapper class
        BL_DBOMapper blDboMapper = new BL_DBOMapper();

        List<SchuetzeDBO> schuetzeList = null;

        try {
            schuetzeList = blDboMapper.mapSchuetze();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //name of schuetze at index 173 is separated without comma: Axel Haag
        SchuetzeDBO schuetzeDBO = schuetzeList.get(173);
        assertThat(schuetzeDBO.getDsb_mitglied_nachname()).isEqualTo("Haag");
        assertThat(schuetzeDBO.getDsb_mitglied_vorname()).isEqualTo("Axel");
    }
}