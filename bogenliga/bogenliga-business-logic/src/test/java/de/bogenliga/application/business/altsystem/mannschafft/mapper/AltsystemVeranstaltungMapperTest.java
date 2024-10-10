package de.bogenliga.application.business.altsystem.mannschafft.mapper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.bogenliga.application.business.configuration.api.ConfigurationComponent;
import de.bogenliga.application.business.configuration.api.types.ConfigurationDO;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.altsystem.mannschaft.dataobject.AltsystemMannschaftDO;
import de.bogenliga.application.business.altsystem.mannschaft.mapper.AltsystemVeranstaltungMapper;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.liga.api.LigaComponent;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.wettkampftyp.api.WettkampfTypComponent;
import de.bogenliga.application.business.wettkampftyp.api.types.WettkampfTypDO;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test for AltsystemVeranstaltungMapper
 *
 * @author Paul Roscher      paul.roscher@student.reutlingen-university.de
 */
public class AltsystemVeranstaltungMapperTest {

    // Constants for Mock Data
    private static final long CURRENTUSERID = 1L;
    private static final long LIGAID = 2L;
    private static final long SPORTJAHR = 2022L;
    private static final int SPORTJAHRID = 4;
    private static final long VERANSTALTUNGID = 2L;
    private static final long WETTKAMPFTYP_ID = 1L;
    private static final String WETTKAMPFTYP_NAME = "Liga Satzsystem";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    private LigaComponent ligaComponent;
    @Mock
    private VeranstaltungComponent veranstaltungComponent;
    @Mock
    private AltsystemUebersetzung altsystemUebersetzung;
    @Mock
    private WettkampfTypComponent wettkampfTypComponent;
    @Mock
    private DsbMannschaftComponent dsbMannschaftComponent;
    @Mock
    private ConfigurationComponent configurationComponent;

    @InjectMocks
    private AltsystemVeranstaltungMapper altsystemVeranstaltungMapper;

    // tests getOrCreateVeranstaltung when in try/catch no exception is thrown
    @Test
    public void testGetOrCreateVeranstaltung() {
        // prepare test data
        // altsystem MannschaftDO
        AltsystemMannschaftDO altsystemMannschaftDO = new AltsystemMannschaftDO();
        altsystemMannschaftDO.setLiga_id((int) LIGAID);
        altsystemMannschaftDO.setName("");
        altsystemMannschaftDO.setMannr("");
        altsystemMannschaftDO.setSaison_id(SPORTJAHRID);
        // liga uebersetzung
        AltsystemUebersetzungDO ligaUebersetzung = new AltsystemUebersetzungDO();
        ligaUebersetzung.setBogenligaId(LIGAID);
        // Sportjahr uebersetzung
        AltsystemUebersetzungDO sportjahrUebersetzung = new AltsystemUebersetzungDO();
        sportjahrUebersetzung.setWert(String.valueOf(SPORTJAHR));
        // aktives Sportjahr aus Configuration
        ConfigurationDO configurationDO = new ConfigurationDO();
        configurationDO.setKey("aktives-Sportjahr");
        configurationDO.setValue("2022");

        // expected Result
        VeranstaltungDO expectedVeranstaltungDO = new VeranstaltungDO();
        expectedVeranstaltungDO.setVeranstaltungID(1L);
        expectedVeranstaltungDO.setVeranstaltungLigaID(LIGAID);
        expectedVeranstaltungDO.setVeranstaltungSportJahr(SPORTJAHR);
        expectedVeranstaltungDO.setVeranstaltungGroesse(4);

        // Mock behavior of AltsystemUebersetzung.findByAltsystemID
        when(altsystemUebersetzung.findByAltsystemID(eq(AltsystemUebersetzungKategorie.Liga_Liga), anyLong()))
                .thenReturn(ligaUebersetzung);

        when(altsystemUebersetzung.findByAltsystemID(eq(AltsystemUebersetzungKategorie.Saison_Sportjahr), anyLong()))
                .thenReturn(sportjahrUebersetzung);

        // Mock behavior of veranstaltungComponent.findByLigaIDAndSportjahr
        when(veranstaltungComponent.findByLigaIDAndSportjahr(LIGAID, SPORTJAHR))
                .thenReturn(expectedVeranstaltungDO);

        // Mock behavior of groesse
        when(dsbMannschaftComponent.findAllByVeranstaltungsId(anyLong())).thenReturn(getMockedDsbMannschaften());

        when(configurationComponent.findByKey("aktives-Sportjahr")).thenReturn(configurationDO);
        // Mock behavior of AltsystemUebersetzung.updateOrInsertUebersetzung
        doNothing().when(altsystemUebersetzung).updateOrInsertUebersetzung(any(), anyLong(), anyLong(), any());


        // call test method
        VeranstaltungDO actual = altsystemVeranstaltungMapper.getOrCreateVeranstaltung(altsystemMannschaftDO, CURRENTUSERID);

        // assert result
        assertThat(actual.getVeranstaltungLigaID()).isEqualTo(expectedVeranstaltungDO.getVeranstaltungLigaID());
        assertThat(actual.getVeranstaltungSportJahr()).isEqualTo(expectedVeranstaltungDO.getVeranstaltungSportJahr());
        assertThat(actual.getVeranstaltungGroesse()).isEqualTo(4);


    }

     // tests getOrCreateVeranstaltung when try/catch throws exception
    @Test
    public void testTryCatch() {
        AltsystemVeranstaltungMapper altsystemVeranstaltungMapper1 = Mockito.spy(altsystemVeranstaltungMapper);
        // prepare test data
        // altsystem MannschaftDO
        AltsystemMannschaftDO altsystemMannschaftDO = new AltsystemMannschaftDO();
        altsystemMannschaftDO.setLiga_id((int) LIGAID);
        altsystemMannschaftDO.setName("");
        altsystemMannschaftDO.setMannr("");
        altsystemMannschaftDO.setSaison_id(SPORTJAHRID);
        // liga uebersetzung
        AltsystemUebersetzungDO ligaUebersetzung = new AltsystemUebersetzungDO();
        ligaUebersetzung.setBogenligaId(LIGAID);
        // Sportjahr uebersetzung
        AltsystemUebersetzungDO sportjahrUebersetzung = new AltsystemUebersetzungDO();
        sportjahrUebersetzung.setWert(String.valueOf(SPORTJAHR));
        // aktives Sportjahr aus Configuration
        ConfigurationDO configurationDO = new ConfigurationDO();
        configurationDO.setKey("aktives-Sportjahr");
        configurationDO.setValue("2022");

        // expected Result
        VeranstaltungDO expectedVeranstaltungDO = new VeranstaltungDO();
        expectedVeranstaltungDO.setVeranstaltungID(1L);
        expectedVeranstaltungDO.setVeranstaltungLigaID(LIGAID);
        expectedVeranstaltungDO.setVeranstaltungSportJahr(SPORTJAHR);

        // Mock behavior of AltsystemUebersetzung.findByAltsystemID
        when(altsystemUebersetzung.findByAltsystemID(eq(AltsystemUebersetzungKategorie.Liga_Liga), anyLong()))
                .thenReturn(ligaUebersetzung);

        when(altsystemUebersetzung.findByAltsystemID(eq(AltsystemUebersetzungKategorie.Saison_Sportjahr), anyLong()))
                .thenReturn(sportjahrUebersetzung);

        // Mock behavior of veranstaltungComponent.findByLigaIDAndSportjahr() throws Exception
        when(veranstaltungComponent.findByLigaIDAndSportjahr(anyLong(), anyLong())).thenThrow(new BusinessException(
                ErrorCode.ENTITY_NOT_FOUND_ERROR, "Test"));

        when(configurationComponent.findByKey("aktives-Sportjahr")).thenReturn(configurationDO);
        // Mock behavior of altsystemVeranstaltungMapper.createVeranstaltung
        doReturn(expectedVeranstaltungDO).when(altsystemVeranstaltungMapper1).createVeranstaltung(ligaUebersetzung.getBogenligaId(), Long.parseLong(sportjahrUebersetzung.getWert()), CURRENTUSERID);

        // Mock behavior of AltsystemUebersetzung.updateOrInsertUebersetzung
        doNothing().when(altsystemUebersetzung).updateOrInsertUebersetzung(any(), anyLong(), anyLong(), anyString());

        VeranstaltungDO actualVeranstaltungDO = altsystemVeranstaltungMapper1.getOrCreateVeranstaltung(altsystemMannschaftDO, CURRENTUSERID);
        assertThat(actualVeranstaltungDO.getVeranstaltungLigaID()).isEqualTo(expectedVeranstaltungDO.getVeranstaltungLigaID());
    }

    // tests createVeranstaltung
    @Test
    public void testCreateVeranstaltung() {
        // prepare test data
        // Mock Liga
        LigaDO mockedLigaDO = new LigaDO();
        mockedLigaDO.setId(LIGAID);
        mockedLigaDO.setName("TestLiga");
        // aktives Sportjahr aus Configuration
        ConfigurationDO configurationDO = new ConfigurationDO();
        configurationDO.setKey("aktives-Sportjahr");
        configurationDO.setValue("2022");

        // Mock behavior of ligaComponent.findById
        when(ligaComponent.findById(LIGAID)).thenReturn(mockedLigaDO);

        // Mock behavior of getSatzSystemDO()
        when(wettkampfTypComponent.findAll()).thenReturn(getMockedWettkampfTypen());

        // Mock behavior of veranstaltungComponent.create()
        when(veranstaltungComponent.create(any(VeranstaltungDO.class), eq(CURRENTUSERID)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(configurationComponent.findByKey("aktives-Sportjahr")).thenReturn(configurationDO);

        // Call test method
        VeranstaltungDO result = altsystemVeranstaltungMapper.createVeranstaltung(LIGAID, SPORTJAHR, CURRENTUSERID);

        // assert result
        assertEquals("TestLiga " + SPORTJAHR, result.getVeranstaltungName());
        assertEquals((Long)LIGAID, result.getVeranstaltungLigaID());
        assertEquals((Long)SPORTJAHR, result.getVeranstaltungSportJahr());
        assertEquals(Date.valueOf("2021-10-01"), result.getVeranstaltungMeldeDeadline());
        assertEquals((Integer) 4, result.getVeranstaltungGroesse());
        assertEquals("Laufend", result.getVeranstaltungPhase());
        assertEquals((Long)CURRENTUSERID, result.getVeranstaltungLigaleiterID());
        assertEquals((Long) WETTKAMPFTYP_ID, result.getVeranstaltungWettkampftypID());
        assertEquals(WETTKAMPFTYP_NAME, result.getVeranstaltungWettkampftypName());


    }
    // tests createVeranstaltung
    @Test
    public void testCreateVeranstaltung_geplant() {
        // prepare test data
        // Mock Liga
        LigaDO mockedLigaDO = new LigaDO();
        mockedLigaDO.setId(LIGAID);
        mockedLigaDO.setName("TestLiga");
        // aktives Sportjahr aus Configuration
        ConfigurationDO configurationDO = new ConfigurationDO();
        configurationDO.setKey("aktives-Sportjahr");
        configurationDO.setValue("2020");

        // Mock behavior of ligaComponent.findById
        when(ligaComponent.findById(LIGAID)).thenReturn(mockedLigaDO);

        // Mock behavior of getSatzSystemDO()
        when(wettkampfTypComponent.findAll()).thenReturn(getMockedWettkampfTypen());

        // Mock behavior of veranstaltungComponent.create()
        when(veranstaltungComponent.create(any(VeranstaltungDO.class), eq(CURRENTUSERID)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(configurationComponent.findByKey("aktives-Sportjahr")).thenReturn(configurationDO);

        // Call test method
        VeranstaltungDO result = altsystemVeranstaltungMapper.createVeranstaltung(LIGAID, SPORTJAHR, CURRENTUSERID);

        // assert result
        assertEquals("TestLiga " + SPORTJAHR, result.getVeranstaltungName());
        assertEquals((Long)LIGAID, result.getVeranstaltungLigaID());
        assertEquals((Long)SPORTJAHR, result.getVeranstaltungSportJahr());
        assertEquals(Date.valueOf("2021-10-01"), result.getVeranstaltungMeldeDeadline());
        assertEquals((Integer) 4, result.getVeranstaltungGroesse());
        assertEquals("Geplant", result.getVeranstaltungPhase());
        assertEquals((Long)CURRENTUSERID, result.getVeranstaltungLigaleiterID());
        assertEquals((Long) WETTKAMPFTYP_ID, result.getVeranstaltungWettkampftypID());
        assertEquals(WETTKAMPFTYP_NAME, result.getVeranstaltungWettkampftypName());


    }
    @Test
    public void testCreateVeranstaltung_abgeschlossen() {
        // prepare test data
        // Mock Liga
        LigaDO mockedLigaDO = new LigaDO();
        mockedLigaDO.setId(LIGAID);
        mockedLigaDO.setName("TestLiga");
        // aktives Sportjahr aus Configuration
        ConfigurationDO configurationDO = new ConfigurationDO();
        configurationDO.setKey("aktives-Sportjahr");
        configurationDO.setValue("2024");

        // Mock behavior of ligaComponent.findById
        when(ligaComponent.findById(LIGAID)).thenReturn(mockedLigaDO);

        // Mock behavior of getSatzSystemDO()
        when(wettkampfTypComponent.findAll()).thenReturn(getMockedWettkampfTypen());

        // Mock behavior of veranstaltungComponent.create()
        when(veranstaltungComponent.create(any(VeranstaltungDO.class), eq(CURRENTUSERID)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(configurationComponent.findByKey("aktives-Sportjahr")).thenReturn(configurationDO);

        // Call test method
        VeranstaltungDO result = altsystemVeranstaltungMapper.createVeranstaltung(LIGAID, SPORTJAHR, CURRENTUSERID);

        // assert result
        assertEquals("TestLiga " + SPORTJAHR, result.getVeranstaltungName());
        assertEquals((Long)LIGAID, result.getVeranstaltungLigaID());
        assertEquals((Long)SPORTJAHR, result.getVeranstaltungSportJahr());
        assertEquals(Date.valueOf("2021-10-01"), result.getVeranstaltungMeldeDeadline());
        assertEquals((Integer) 4, result.getVeranstaltungGroesse());
        assertEquals("Abgeschlossen", result.getVeranstaltungPhase());
        assertEquals((Long)CURRENTUSERID, result.getVeranstaltungLigaleiterID());
        assertEquals((Long) WETTKAMPFTYP_ID, result.getVeranstaltungWettkampftypID());
        assertEquals(WETTKAMPFTYP_NAME, result.getVeranstaltungWettkampftypName());


    }

    // tests getOrCreateVeranstaltung when 4 DsbMannschaften exist
    @Test
    public void testSetVeranstaltungGroesse6() {
        // prepare test data
        // altsystem MannschaftDO
        AltsystemMannschaftDO altsystemMannschaftDO = new AltsystemMannschaftDO();
        altsystemMannschaftDO.setLiga_id((int) LIGAID);
        altsystemMannschaftDO.setName("");
        altsystemMannschaftDO.setMannr("");
        altsystemMannschaftDO.setSaison_id(SPORTJAHRID);
        // liga uebersetzung
        AltsystemUebersetzungDO ligaUebersetzung = new AltsystemUebersetzungDO();
        ligaUebersetzung.setBogenligaId(LIGAID);
        // Sportjahr uebersetzung
        AltsystemUebersetzungDO sportjahrUebersetzung = new AltsystemUebersetzungDO();
        sportjahrUebersetzung.setWert(String.valueOf(SPORTJAHR));
        // aktives Sportjahr aus Configuration
        ConfigurationDO configurationDO = new ConfigurationDO();
        configurationDO.setKey("aktives-Sportjahr");
        configurationDO.setValue("2022");

        // expected Result
        VeranstaltungDO expectedVeranstaltungDO = new VeranstaltungDO();
        expectedVeranstaltungDO.setVeranstaltungID(1L);
        expectedVeranstaltungDO.setVeranstaltungLigaID(LIGAID);
        expectedVeranstaltungDO.setVeranstaltungSportJahr(SPORTJAHR);
        expectedVeranstaltungDO.setVeranstaltungGroesse(6);

        // Mock behavior of AltsystemUebersetzung.findByAltsystemID
        when(altsystemUebersetzung.findByAltsystemID(eq(AltsystemUebersetzungKategorie.Liga_Liga), anyLong()))
                .thenReturn(ligaUebersetzung);

        when(altsystemUebersetzung.findByAltsystemID(eq(AltsystemUebersetzungKategorie.Saison_Sportjahr), anyLong()))
                .thenReturn(sportjahrUebersetzung);

        // Mock behavior of veranstaltungComponent.findByLigaIDAndSportjahr
        when(veranstaltungComponent.findByLigaIDAndSportjahr(LIGAID, SPORTJAHR))
                .thenReturn(expectedVeranstaltungDO);

        // Mock behavior of groesse
        when(dsbMannschaftComponent.findAllByVeranstaltungsId(anyLong())).thenReturn(get4MockedDsbMannschaften());

        // Mock behavior of veranstaltungComponent.update()
        when(veranstaltungComponent.update(expectedVeranstaltungDO, CURRENTUSERID)).thenReturn(expectedVeranstaltungDO);
        when(configurationComponent.findByKey("aktives-Sportjahr")).thenReturn(configurationDO);

        // Mock behavior of AltsystemUebersetzung.updateOrInsertUebersetzung
        doNothing().when(altsystemUebersetzung).updateOrInsertUebersetzung(any(), anyLong(), anyLong(), any());


        // call test method
        VeranstaltungDO actual = altsystemVeranstaltungMapper.getOrCreateVeranstaltung(altsystemMannschaftDO, CURRENTUSERID);

        // assert result
        assertThat(actual.getVeranstaltungLigaID()).isEqualTo(expectedVeranstaltungDO.getVeranstaltungLigaID());
        assertThat(actual.getVeranstaltungSportJahr()).isEqualTo(expectedVeranstaltungDO.getVeranstaltungSportJahr());
        assertThat(actual.getVeranstaltungGroesse()).isEqualTo(6);


    }

    // tests getOrCreateVeranstaltung when 6 DsbMannschaften exist
    @Test
    public void testSetVeranstaltungGroesse8() {
        // prepare test data
        // altsystem MannschaftDO
        AltsystemMannschaftDO altsystemMannschaftDO = new AltsystemMannschaftDO();
        altsystemMannschaftDO.setLiga_id((int) LIGAID);
        altsystemMannschaftDO.setName("");
        altsystemMannschaftDO.setMannr("");
        altsystemMannschaftDO.setSaison_id(SPORTJAHRID);
        // liga uebersetzung
        AltsystemUebersetzungDO ligaUebersetzung = new AltsystemUebersetzungDO();
        ligaUebersetzung.setBogenligaId(LIGAID);
        // Sportjahr uebersetzung
        AltsystemUebersetzungDO sportjahrUebersetzung = new AltsystemUebersetzungDO();
        sportjahrUebersetzung.setWert(String.valueOf(SPORTJAHR));
        // aktives Sportjahr aus Configuration
        ConfigurationDO configurationDO = new ConfigurationDO();
        configurationDO.setKey("aktives-Sportjahr");
        configurationDO.setValue("2022");

        // expected Result
        VeranstaltungDO expectedVeranstaltungDO = new VeranstaltungDO();
        expectedVeranstaltungDO.setVeranstaltungID(1L);
        expectedVeranstaltungDO.setVeranstaltungLigaID(LIGAID);
        expectedVeranstaltungDO.setVeranstaltungSportJahr(SPORTJAHR);
        expectedVeranstaltungDO.setVeranstaltungGroesse(8);

        // Mock behavior of AltsystemUebersetzung.findByAltsystemID
        when(altsystemUebersetzung.findByAltsystemID(eq(AltsystemUebersetzungKategorie.Liga_Liga), anyLong()))
                .thenReturn(ligaUebersetzung);

        when(altsystemUebersetzung.findByAltsystemID(eq(AltsystemUebersetzungKategorie.Saison_Sportjahr), anyLong()))
                .thenReturn(sportjahrUebersetzung);

        // Mock behavior of veranstaltungComponent.findByLigaIDAndSportjahr
        when(veranstaltungComponent.findByLigaIDAndSportjahr(LIGAID, SPORTJAHR))
                .thenReturn(expectedVeranstaltungDO);
        when(configurationComponent.findByKey("aktives-Sportjahr")).thenReturn(configurationDO);

        // Mock behavior of groesse
        when(dsbMannschaftComponent.findAllByVeranstaltungsId(anyLong())).thenReturn(get6MockedDsbMannschaften());

        // Mock behavior of veranstaltungComponent.update()
        when(veranstaltungComponent.update(expectedVeranstaltungDO, CURRENTUSERID)).thenReturn(expectedVeranstaltungDO);

        // Mock behavior of AltsystemUebersetzung.updateOrInsertUebersetzung
        doNothing().when(altsystemUebersetzung).updateOrInsertUebersetzung(any(), anyLong(), anyLong(), any());


        // call test method
        VeranstaltungDO actual = altsystemVeranstaltungMapper.getOrCreateVeranstaltung(altsystemMannschaftDO, CURRENTUSERID);

        // assert result
        assertThat(actual.getVeranstaltungLigaID()).isEqualTo(expectedVeranstaltungDO.getVeranstaltungLigaID());
        assertThat(actual.getVeranstaltungSportJahr()).isEqualTo(expectedVeranstaltungDO.getVeranstaltungSportJahr());
        assertThat(actual.getVeranstaltungGroesse()).isEqualTo(8);


    }

    public static List<DsbMannschaftDO> getMockedDsbMannschaften(){
        List<DsbMannschaftDO> dsbMannschaften = new LinkedList<>();
        DsbMannschaftDO mannschaftDO = new DsbMannschaftDO();
        mannschaftDO.setVeranstaltungId(VERANSTALTUNGID);
        dsbMannschaften.add(mannschaftDO);
        return dsbMannschaften;
    }

    public static List<DsbMannschaftDO> get4MockedDsbMannschaften(){
        List<DsbMannschaftDO> dsbMannschaften = new LinkedList<>();
        for (Long i = 1L; i <= 4L; i++) {
            DsbMannschaftDO dsbMannschaftDO = new DsbMannschaftDO((long)i, i);
            dsbMannschaften.add(dsbMannschaftDO);
        }
        return dsbMannschaften;
    }

    public static List<DsbMannschaftDO> get6MockedDsbMannschaften(){
        List<DsbMannschaftDO> dsbMannschaften = new LinkedList<>();
        for (Long i = 1L; i <= 6L; i++) {
            DsbMannschaftDO dsbMannschaftDO = new DsbMannschaftDO((long)i, i);
            dsbMannschaften.add(dsbMannschaftDO);
        }
        return dsbMannschaften;
    }

    private static List<WettkampfTypDO> getMockedWettkampfTypen() {
        List<WettkampfTypDO> wettkampfTypDOS = new ArrayList<>();
        WettkampfTypDO wettkampfTypDO = new WettkampfTypDO(1L, "Liga Satzsystem");
        wettkampfTypDOS.add(wettkampfTypDO);
        return wettkampfTypDOS;
    }

}
