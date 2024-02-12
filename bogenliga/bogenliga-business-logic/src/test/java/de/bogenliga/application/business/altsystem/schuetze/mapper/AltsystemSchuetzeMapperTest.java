package de.bogenliga.application.business.altsystem.schuetze.mapper;

import java.sql.SQLException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.altsystem.schuetze.dataobject.AltsystemSchuetzeDO;
import de.bogenliga.application.business.altsystem.schuetze.entity.AltsystemSchuetze;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class AltsystemSchuetzeMapperTest {

    private static final Long CURRENTUSERID = 1L;


    public AltsystemSchuetzeMapper altsystemSchuetzeMapper;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    AltsystemUebersetzung altsystemUebersetzung;
    @Mock
    DsbMitgliedComponent dsbMitgliedComponent;

    @InjectMocks
    AltsystemSchuetze altsystemSchuetze;


    @Before
    public void setUp() {
        altsystemSchuetzeMapper = Mockito.mock(AltsystemSchuetzeMapper.class);
        dsbMitgliedComponent = Mockito.mock(DsbMitgliedComponent.class);
        altsystemUebersetzung = Mockito.mock(AltsystemUebersetzung.class);
        altsystemSchuetze = new AltsystemSchuetze(altsystemSchuetzeMapper, dsbMitgliedComponent, altsystemUebersetzung);

    }


    @Test
    public void testToDO() throws SQLException {
        // prepare test data
        // altsystem DO
        AltsystemSchuetzeDO altsystemSchuetzeDO = new AltsystemSchuetzeDO();
        altsystemSchuetzeDO.setName("Bammert, Marco");
        // expectedResult
        DsbMitgliedDO expectedDO = new DsbMitgliedDO();
        expectedDO.setVorname("Marco");
        expectedDO.setNachname("Bammert");
        expectedDO.setVereinsId(1L);

        // configure mocks
        AltsystemUebersetzungDO altsystemUebersetzungDO = new AltsystemUebersetzungDO();
        altsystemUebersetzungDO.setBogenligaId(1L);

        when(altsystemUebersetzung.findByAltsystemID(any(), any())).thenReturn(altsystemUebersetzungDO);

        AltsystemSchuetzeMapper altsystemSchuetzeMapper = new AltsystemSchuetzeMapper(altsystemUebersetzung);
        // call test method
        DsbMitgliedDO actual = altsystemSchuetzeMapper.toDO(new DsbMitgliedDO(), altsystemSchuetzeDO);

        assertThat(actual.getVorname()).isEqualTo(expectedDO.getVorname());
        assertThat(actual.getNachname()).isEqualTo(expectedDO.getNachname());
    }

    @Test
    public void testGetIdentifier() {
        // prepare test data
        // altsystem DO
        AltsystemSchuetzeDO altsystemSchuetzeDO = new AltsystemSchuetzeDO();
        altsystemSchuetzeDO.setName("Bammert, Marco");

        // configure mocks
        AltsystemUebersetzungDO altsystemUebersetzungDO = new AltsystemUebersetzungDO();
        altsystemUebersetzungDO.setBogenligaId(1L);

        when(altsystemUebersetzung.findByAltsystemID(any(), any())).thenReturn(altsystemUebersetzungDO);

        AltsystemSchuetzeMapper altsystemSchuetzeMapper = new AltsystemSchuetzeMapper(altsystemUebersetzung);
        // call test method
        String identifier = altsystemSchuetzeMapper.getIdentifier(altsystemSchuetzeDO);

        String expectedIdentifier = "MarcoBammert1";

        assertThat(identifier).isEqualTo(expectedIdentifier);
    }

    @Test
    public void testGetDsbMitgliedDO() throws SQLException {
        // prepare test data
        // altsystem DO
        AltsystemSchuetzeDO altsystemSchuetzeDO = new AltsystemSchuetzeDO();
        altsystemSchuetzeDO.setName("Bammert, Marco");

        // configure mocks
        AltsystemUebersetzungDO expected = new AltsystemUebersetzungDO();
        expected.setBogenligaId(1L);
        expected.setWert("MarcoBammert1");
        expected.setKategorie(String.valueOf(AltsystemUebersetzungKategorie.Schuetze_DSBMitglied));

        String identifier = altsystemSchuetzeMapper.getIdentifier(altsystemSchuetzeDO);

        // Mock-Konfiguration
        when(altsystemSchuetzeMapper.getSchuetzeByIdentifier(any())).thenReturn(expected);

        AltsystemUebersetzungDO actual = altsystemSchuetzeMapper.getSchuetzeByIdentifier(identifier);

        assertEquals(expected.getUebersetzungId(), actual.getUebersetzungId());
    }


    @Test
    public void testparseNameWithComa() {
        // Mocking
        AltsystemSchuetzeDO schuetze = new AltsystemSchuetzeDO();
        schuetze.setName("Bammert, Marco");
        schuetze.setMannschaft_id(387);
        schuetze.setRuecknr(1);

        AltsystemSchuetzeMapper altsystemSchuetzeMapper = new AltsystemSchuetzeMapper(mock(altsystemUebersetzung.getClass()));

        String[] parsedName = altsystemSchuetzeMapper.parseName(schuetze);
        String schuetzeVorname = parsedName[1];
        String schuetzeNachname = parsedName[0];

        String expectedVorname = "Marco";
        String expectedNachname = "Bammert";

        assertEquals(expectedVorname, schuetzeVorname);
        assertEquals(expectedNachname, schuetzeNachname);
    }

    @Test
    public void testParseNameWithoutComma() {
        // Mocking
        AltsystemSchuetzeDO schuetze = new AltsystemSchuetzeDO();
        schuetze.setName("Bammert Marco");
        schuetze.setMannschaft_id(387);
        schuetze.setRuecknr(1);

        AltsystemSchuetzeMapper altsystemSchuetzeMapper = new AltsystemSchuetzeMapper(mock(altsystemUebersetzung.getClass()));

        String[] parsedName = altsystemSchuetzeMapper.parseName(schuetze);
        String schuetzeVorname = parsedName[1];
        String schuetzeNachname = parsedName[0];

        String expectedVorname = "Marco";
        String expectedNachname = "Bammert";

        assertEquals(expectedVorname, schuetzeVorname);
        assertEquals(expectedNachname, schuetzeNachname);
    }

    @Test
    public void testParseNameWithMultipleSpace() {
        // Mocking
        AltsystemSchuetzeDO schuetze = new AltsystemSchuetzeDO();
        schuetze.setName("Bammert   Marco");
        schuetze.setMannschaft_id(387);
        schuetze.setRuecknr(1);

        AltsystemSchuetzeMapper altsystemSchuetzeMapper = new AltsystemSchuetzeMapper(mock(altsystemUebersetzung.getClass()));

        String[] parsedName = altsystemSchuetzeMapper.parseName(schuetze);
        String schuetzeVorname = parsedName[1];
        String schuetzeNachname = parsedName[0];

        String expectedVorname = "Marco";
        String expectedNachname = "Bammert";

        assertEquals(expectedVorname, schuetzeVorname);
        assertEquals(expectedNachname, schuetzeNachname);
    }

}
