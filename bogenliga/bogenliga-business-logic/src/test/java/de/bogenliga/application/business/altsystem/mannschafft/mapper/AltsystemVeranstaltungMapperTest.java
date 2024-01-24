package de.bogenliga.application.business.altsystem.mannschafft.mapper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import com.itextpdf.layout.element.Link;
import de.bogenliga.application.business.altsystem.mannschaft.dataobject.AltsystemMannschaftDO;
import de.bogenliga.application.business.altsystem.mannschaft.mapper.AltsystemVeranstaltungMapper;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.liga.api.LigaComponent;
import de.bogenliga.application.business.user.api.UserComponent;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.wettkampftyp.api.WettkampfTypComponent;
import de.bogenliga.application.business.wettkampftyp.api.types.WettkampfTypDO;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test for AltsystemVeranstaltungMapper
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemVeranstaltungMapperTest {

    // Constants for Mock Data
    private static final long CURRENTUSERID = 1L;
    private static final long LIGAID = 2L;
    private static final long SPORTJAHR = 2022L;
    private static final int SPORTJAHRID = 4;
    private static final long VERANSTALTUNGID = 2L;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    private LigaComponent ligaComponent;
    @Mock
    private VeranstaltungComponent veranstaltungComponent;
    @Mock
    private UserComponent userComponent;
    @Mock
    private AltsystemUebersetzung altsystemUebersetzung;
    @Mock
    private WettkampfTypComponent wettkampfTypComponent;
    @Mock
    private DsbMannschaftComponent dsbMannschaftComponent;

    @InjectMocks
    private AltsystemVeranstaltungMapper altsystemVeranstaltungMapper;

    // tests getOrCreateVeranstaltung when in try/catch no exception is thrown
    @Test
    public void testGetOrCreateVeranstaltung() {
        // prepare test data
        // altsystem MannschaftDO
        AltsystemMannschaftDO altsystemMannschaftDO = new AltsystemMannschaftDO(1, (int)LIGAID, "", SPORTJAHRID);

        // expected Result
        VeranstaltungDO expectedVeranstaltungDO = new VeranstaltungDO();
        expectedVeranstaltungDO.setVeranstaltungLigaID(LIGAID);
        expectedVeranstaltungDO.setVeranstaltungSportJahr(SPORTJAHR);

        // Mock behavior of AltsystemUebersetzung.findByAltsystemID
        when(altsystemUebersetzung.findByAltsystemID(eq(AltsystemUebersetzungKategorie.Liga_Liga), anyLong()))
                .thenReturn(new AltsystemUebersetzungDO());

        // Mock behavior of AltsystemUebersetzung.getBogenligaID
        AltsystemUebersetzungDO uebersetzungLigaDOMock = mock(AltsystemUebersetzungDO.class);
        when(uebersetzungLigaDOMock.getBogenliga_id()).thenReturn(1L);

        when(altsystemUebersetzung.findByAltsystemID(eq(AltsystemUebersetzungKategorie.Saison_Sportjahr), anyLong()))
                .thenReturn(new AltsystemUebersetzungDO());

        // Mock behavior of veranstaltungComponent.findByLigaIDAndSportjahr
        when(veranstaltungComponent.findByLigaIDAndSportjahr(anyLong(), anyLong()))
                .thenReturn(new VeranstaltungDO());

        // Mock behavior of groesse
        when(dsbMannschaftComponent.findAllByVeranstaltungsId(anyLong())).thenReturn(getMockedDsbMannschaften());

        // Mock behavior of AltsystemUebersetzung.updateOrInsertUebersetzung
        doNothing().when(altsystemUebersetzung).updateOrInsertUebersetzung(any(), anyLong(), anyLong(), eq("String by matcher"));

        // call test method
        VeranstaltungDO actual = altsystemVeranstaltungMapper.getOrCreateVeranstaltung(altsystemMannschaftDO, CURRENTUSERID);

        // assert result
        assertThat(actual.getVeranstaltungLigaID()).isEqualTo(expectedVeranstaltungDO.getVeranstaltungLigaID());
        assertThat(actual.getVeranstaltungSportJahr()).isEqualTo(expectedVeranstaltungDO.getVeranstaltungSportJahr());
        assertThat(actual.getVeranstaltungGroesse()).isEqualTo(6);


    }

    // tests getOrCreateVeranstaltung when try/catch throws exception
    @Test
    public void testTryCatch() {

        // prepare test data
        // altsystem MannschaftDO
        AltsystemMannschaftDO altsystemMannschaftDO = new AltsystemMannschaftDO(1, (int)LIGAID, "", SPORTJAHRID);

        // expected Result
        VeranstaltungDO expectedVeranstaltungDO = new VeranstaltungDO();
        expectedVeranstaltungDO.setVeranstaltungLigaID(LIGAID);
        expectedVeranstaltungDO.setVeranstaltungSportJahr(SPORTJAHR);

        // Mock behavior of AltsystemUebersetzung.findByAltsystemID
        when(altsystemUebersetzung.findByAltsystemID(eq(AltsystemUebersetzungKategorie.Liga_Liga), anyLong()))
                .thenReturn(new AltsystemUebersetzungDO());

        when(altsystemUebersetzung.findByAltsystemID(eq(AltsystemUebersetzungKategorie.Saison_Sportjahr), anyLong()))
                .thenReturn(new AltsystemUebersetzungDO());

        // Mock behavior of veranstaltungComponent.findByLigaIDAndSportjahr() throws Exception
        when(veranstaltungComponent.findByLigaIDAndSportjahr(anyLong(), anyLong())).thenThrow(new Exception("Test Exception"));

        // Mock behavior of altsystemVeranstaltungMapper.createVeranstaltung
        when(altsystemVeranstaltungMapper.createVeranstaltung(anyLong(), anyLong(), anyLong())).thenReturn(new VeranstaltungDO());

        // Call test method
        VeranstaltungDO actual = altsystemVeranstaltungMapper.getOrCreateVeranstaltung(altsystemMannschaftDO, CURRENTUSERID);

        // assert result
        assertThat(actual.getVeranstaltungLigaID()).isEqualTo(expectedVeranstaltungDO.getVeranstaltungLigaID());
        assertThat(actual.getVeranstaltungSportJahr()).isEqualTo(expectedVeranstaltungDO.getVeranstaltungSportJahr());

    }

    // tests createVeranstaltung
    @Test
    public void testCreateVeranstaltung() {
        // prepare test data
        // Mock Liga
        LigaDO mockedLigaDO = new LigaDO();
        mockedLigaDO.setId(LIGAID);
        mockedLigaDO.setName("TestLiga");

        // Mock behavior of ligaComponent.findById
        when(ligaComponent.findById(LIGAID)).thenReturn(mockedLigaDO);

        // Mock behavior of getSatzSystemDO()
        when(altsystemVeranstaltungMapper.getSatzSystemDO()).thenReturn(createMockedSatzSystemDO());

        //when(wettkampfTypComponent.findAll()).thenReturn(getMockedWettkampftypen());

        // Mock behavior of veranstaltungComponent.create()
        when(veranstaltungComponent.create(any(VeranstaltungDO.class), eq(CURRENTUSERID)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Call test method
        VeranstaltungDO result = altsystemVeranstaltungMapper.createVeranstaltung(LIGAID, SPORTJAHR, CURRENTUSERID);

        // assert result
        assertEquals("TestLiga 2023", result.getVeranstaltungName());
        assertEquals((Long)LIGAID, result.getVeranstaltungLigaID());
        assertEquals((Long)SPORTJAHR, result.getVeranstaltungSportJahr());
        assertEquals(Date.valueOf("2021-10-01"), result.getVeranstaltungMeldeDeadline());
        assertEquals((Integer) 4, result.getVeranstaltungGroesse());
        assertEquals("geplant", result.getVeranstaltungPhase());
        assertEquals((Long)CURRENTUSERID, result.getVeranstaltungLigaleiterID());
        assertEquals("Liga Satzsystem", result.getVeranstaltungWettkampftypName());


    }

    public static List<DsbMannschaftDO> getMockedDsbMannschaften(){
        List<DsbMannschaftDO> dsbMannschaften = new LinkedList<>();
        DsbMannschaftDO mannschaftDO = new DsbMannschaftDO();
        mannschaftDO.setVeranstaltungId(VERANSTALTUNGID);
        dsbMannschaften.add(mannschaftDO);
        return dsbMannschaften;
    }

    private WettkampfTypDO createMockedSatzSystemDO() {
        return new WettkampfTypDO(1L, "liga satzsysten");
    }

    private static List<WettkampfTypDO> getMockedWettkampftypen() {
        List<WettkampfTypDO> wettkampfTypDOS = new ArrayList<>();
        WettkampfTypDO wettkampfTypDO = new WettkampfTypDO(1L, "Liga Satzsystem");
        wettkampfTypDOS.add(wettkampfTypDO);
        return wettkampfTypDOS;
    }

}
