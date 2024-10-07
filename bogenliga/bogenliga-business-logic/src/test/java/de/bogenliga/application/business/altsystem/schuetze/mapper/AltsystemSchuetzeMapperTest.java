package de.bogenliga.application.business.altsystem.schuetze.mapper;

import java.sql.Date;
import java.sql.SQLException;

import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
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


    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    AltsystemUebersetzung altsystemUebersetzung;
    @Mock
    DsbMitgliedComponent dsbMitgliedComponent;

    @InjectMocks
    AltsystemSchuetzeMapper altsystemSchuetzeMapper;



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


    @Test
    public void testAddDefaultFields() {
        //actual
        DsbMitgliedDO mitglied = new DsbMitgliedDO();
        mitglied.setId(1L);
        mitglied.setVorname("Marco");
        mitglied.setNachname("Bammert");
        mitglied.setVereinsId(1L);

        //expected
        DsbMitgliedDO expected = new DsbMitgliedDO();
        expected.setVorname("Marco");
        expected.setNachname("Bammert");
        expected.setId(1L);
        expected.setGeburtsdatum(new Date(111,11,11));
        expected.setNationalitaet("D");
        expected.setUserId(null);
        expected.setKampfrichter(false);

        //when(altsystemSchuetzeMapper.addDefaultFields(mitglied, 1L)).thenReturn(mitglied);

        altsystemSchuetzeMapper.addDefaultFields(mitglied, CURRENTUSERID);


        assertEquals(expected.getId(), mitglied.getId());
        assertEquals(expected.getGeburtsdatum(), mitglied.getGeburtsdatum());
        assertEquals(expected.getNationalitaet(), mitglied.getNationalitaet());
        assertEquals(expected.getUserId(), mitglied.getUserId());
        assertEquals(expected.getKampfrichter(), mitglied.getKampfrichter());
    }

    @Test
    public void testBuildMannschaftsMitglied() {

        //actual
        Long altsystemMannschaftId = 123L;
        Long rueckennummer = 456L;

        DsbMitgliedDO mitglied = new DsbMitgliedDO();
        mitglied.setId(789L);
        mitglied.setVorname("Marco");
        mitglied.setNachname("Bammert");
        mitglied.setVereinsId(101112L);

        AltsystemUebersetzungDO altsystemUebersetzungDO = new AltsystemUebersetzungDO();
        altsystemUebersetzungDO.setAltsystemId(123L);
        altsystemUebersetzungDO.setBogenligaId(321L);

        //expected
        MannschaftsmitgliedDO expected = new MannschaftsmitgliedDO(
                null, 321L, 789L, 1,
                "Marco", "Bammert", 456L
        );

        when(altsystemUebersetzung.findByAltsystemID(any(), any())).thenReturn(altsystemUebersetzungDO);

        MannschaftsmitgliedDO result = altsystemSchuetzeMapper.buildMannschaftsMitglied(altsystemMannschaftId, rueckennummer, mitglied);


        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getMannschaftId(), result.getMannschaftId());
        assertEquals(expected.getDsbMitgliedId(), result.getDsbMitgliedId());
        assertEquals(expected.getDsbMitgliedNachname(), result.getDsbMitgliedNachname());
        assertEquals(expected.getDsbMitgliedVorname(), result.getDsbMitgliedVorname());
        assertEquals(expected.getRueckennummer(), result.getRueckennummer());

    }
}
