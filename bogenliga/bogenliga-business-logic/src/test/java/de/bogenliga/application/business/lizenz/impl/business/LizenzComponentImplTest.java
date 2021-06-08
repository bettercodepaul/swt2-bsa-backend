package de.bogenliga.application.business.lizenz.impl.business;

import java.io.ByteArrayOutputStream;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import com.itextpdf.layout.Document;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.lizenz.api.types.LizenzDO;
import de.bogenliga.application.business.lizenz.impl.dao.LizenzDAO;
import de.bogenliga.application.business.lizenz.impl.entity.LizenzBE;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

public class LizenzComponentImplTest {

    private static final long lizenzId = 0;
    private static final String lizenznummer = "WT1234567";
    private static final long lizenzRegionId = 1;
    private static final long lizenzDsbMitgliedId = 1337L;
    private static final String lizenztyp = "Liga";
    private static final long lizenzDisziplinId = 0;
    private static final OffsetDateTime offsetDateTime = null;
    private static final long USER = 1;
    private static final long VERSION = 2;
    private static final long RUECKENNUMMER = 7;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private LizenzDAO lizenzDAO;
    @Mock
    private DsbMitgliedComponent dsbMitgliedComponent;
    @Mock
    private DsbMannschaftComponent mannschaftComponent;
    @Mock
    private MannschaftsmitgliedComponent mannschaftsmitgliedComponent;
    @Mock
    private VeranstaltungComponent veranstaltungComponent;
    @Mock
    private WettkampfComponent wettkampfComponent;
    @Mock
    private VereinComponent vereinComponent;

    @InjectMocks
    private LizenzComponentImpl underTest;
    @Captor
    private ArgumentCaptor<LizenzBE> lizenzBEArgumentCaptor;


    public static LizenzBE getLizenzBE() {
        final LizenzBE expectedBE = new LizenzBE();
        expectedBE.setLizenzId(lizenzId);
        expectedBE.setLizenznummer(lizenznummer);
        expectedBE.setLizenzRegionId(lizenzRegionId);
        expectedBE.setLizenzDisziplinId(lizenzDisziplinId);
        expectedBE.setLizenztyp(lizenztyp);
        expectedBE.setLizenzDsbMitgliedId(lizenzDsbMitgliedId);

        return expectedBE;
    }


    public static LizenzDO getLizenzDO() {
        return new LizenzDO(lizenzId,
                lizenznummer,
                lizenzRegionId,
                lizenzDsbMitgliedId,
                lizenztyp,
                lizenzDisziplinId);
    }


    public static VereinDO getVereinDO() {
        VereinDO vereinDo = new VereinDO(1L);
        vereinDo.setName("Testverein");
        return vereinDo;
    }

    public static DsbMannschaftDO getDsbMannschaftDO(Long mannschaftID, Long vereinID) {
        DsbMannschaftDO dsbMannschaftDO = new DsbMannschaftDO(
                mannschaftID, vereinID);

        return dsbMannschaftDO;
    }


    public static MannschaftsmitgliedDO getMannschaftsmitgliedDO(Long mannschaftID, Long dsbMitgliedID) {
         MannschaftsmitgliedDO mannschaftsmitgliedDO = new MannschaftsmitgliedDO(
                mannschaftID, dsbMitgliedID);
        mannschaftsmitgliedDO.setDsbMitgliedNachname("Musterfrau"+ mannschaftID.toString());
        mannschaftsmitgliedDO.setDsbMitgliedVorname("Maxime"+dsbMitgliedID.toString());
        mannschaftsmitgliedDO.setRueckennummer(RUECKENNUMMER);
        return mannschaftsmitgliedDO;
    }



    @Test
    public void findAll() {

        // prepare test data
        final LizenzBE expectedBE = getLizenzBE();
        expectedBE.setLizenzId((long)0);
        expectedBE.setLizenznummer("WT1234567");
        expectedBE.setLizenzRegionId((long)1);
        expectedBE.setLizenzDsbMitgliedId(lizenzDsbMitgliedId);
        final List<LizenzBE> expectedBEList = Collections.singletonList(expectedBE);
        // configure mocks
        when(lizenzDAO.findAll()).thenReturn(expectedBEList);
        // call test method
        final List<LizenzDO> actual = underTest.findAll();

        // assert result
        Assertions.assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(actual.get(0)).isNotNull();

        Assertions.assertThat(actual.get(0).getLizenzDisziplinId())
                .isEqualTo(expectedBE.getLizenzDisziplinId());
        Assertions.assertThat(actual.get(0).getLizenzDsbMitgliedId())
                .isEqualTo(expectedBE.getLizenzDsbMitgliedId());
        Assertions.assertThat(actual.get(0).getLizenzId())
                .isEqualTo(expectedBE.getLizenzId());
        Assertions.assertThat(actual.get(0).getLizenznummer())
                .isEqualTo(expectedBE.getLizenznummer());
        Assertions.assertThat(actual.get(0).getLizenzRegionId())
                .isEqualTo(expectedBE.getLizenzRegionId());
        Assertions.assertThat(actual.get(0).getLizenztyp())
                .isEqualTo(expectedBE.getLizenztyp());


        // verify invocations
        verify(lizenzDAO).findAll();
    }


    @Test
    public void findByDsbMitgliedId() {


        // prepare test data
        final LizenzBE expectedBE = getLizenzBE();
        expectedBE.setLizenzId((long)0);
        expectedBE.setLizenznummer("WT1234567");
        expectedBE.setLizenzRegionId((long)1);
        expectedBE.setLizenzDsbMitgliedId(lizenzDsbMitgliedId);
        final List<LizenzBE> expectedBEList = Collections.singletonList(expectedBE);
        // configure mocks
        when(lizenzDAO.findByDsbMitgliedId(anyLong())).thenReturn(expectedBEList);
        // call test method
        final List<LizenzDO> actual = underTest.findByDsbMitgliedId(lizenzDisziplinId);

        // assert result
        Assertions.assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(actual.get(0)).isNotNull();

        Assertions.assertThat(actual.get(0).getLizenzDisziplinId())
                .isEqualTo(expectedBE.getLizenzDisziplinId());
        Assertions.assertThat(actual.get(0).getLizenzDsbMitgliedId())
                .isEqualTo(expectedBE.getLizenzDsbMitgliedId());
        Assertions.assertThat(actual.get(0).getLizenzId())
                .isEqualTo(expectedBE.getLizenzId());
        Assertions.assertThat(actual.get(0).getLizenznummer())
                .isEqualTo(expectedBE.getLizenznummer());
        Assertions.assertThat(actual.get(0).getLizenzRegionId())
                .isEqualTo(expectedBE.getLizenzRegionId());
        Assertions.assertThat(actual.get(0).getLizenztyp())
                .isEqualTo(expectedBE.getLizenztyp());


        // verify invocations
        verify(lizenzDAO).findByDsbMitgliedId(anyLong());
    }



    @Test
    public void create() {
        // prepare test data
        final LizenzDO input = new LizenzDO();
        input.setLizenzId(lizenzId);
        input.setLizenztyp("Liga");
        input.setLizenznummer("WT012354");
        input.setLizenzRegionId(1L);
        input.setLizenzDsbMitgliedId(lizenzDsbMitgliedId);
        input.setLizenzDisziplinId(1L);

        final LizenzBE lizenzBE = new LizenzBE();
        lizenzBE.setLizenzId(lizenzId);
        lizenzBE.setLizenzDsbMitgliedId(lizenzDsbMitgliedId);

        // configure mocks
        when(lizenzDAO.create(any(LizenzBE.class), anyLong())).thenReturn(lizenzBE);

        // call test method
        final LizenzDO actual = underTest.create(input, lizenzId);

        // assert result
        Assertions.assertThat(actual).isNotNull();

        Assertions.assertThat(actual.getLizenzDsbMitgliedId())
                .isEqualTo(input.getLizenzDsbMitgliedId());
        Assertions.assertThat(actual.getLizenzDsbMitgliedId())
                .isEqualTo(input.getLizenzDsbMitgliedId());

        // verify invocations
        verify(lizenzDAO).create(any(LizenzBE.class), anyLong());
    }


    @Test
    public void update() {
        // prepare test data
        final LizenzDO input = new LizenzDO();
        input.setLizenzId(lizenzId);
        input.setLizenztyp("Liga");
        input.setLizenznummer("WT012354");
        input.setLizenzRegionId(1L);
        input.setLizenzDsbMitgliedId(lizenzDsbMitgliedId);
        input.setLizenzDisziplinId(1L);

        final LizenzBE lizenzBE = new LizenzBE();
        lizenzBE.setLizenzId(lizenzId);
        lizenzBE.setLizenzDsbMitgliedId(lizenzDsbMitgliedId);

        // configure mocks
        when(lizenzDAO.update(any(LizenzBE.class), anyLong())).thenReturn(lizenzBE);

        // call test method
        final LizenzDO actual = underTest.update(input, lizenzId);

        // assert result
        Assertions.assertThat(actual).isNotNull();

        Assertions.assertThat(actual.getLizenzDsbMitgliedId())
                .isEqualTo(input.getLizenzDsbMitgliedId());
        Assertions.assertThat(actual.getLizenzDsbMitgliedId())
                .isEqualTo(input.getLizenzDsbMitgliedId());

        // verify invocations
        verify(lizenzDAO).update(any(LizenzBE.class), anyLong());
    }


    @Test
    public void getLizenzPDFasByteArray(){
        // prepare test data
        final long mitgliedId = 123L;
        final long teamId = 321L;
        final long veranstaltungId = 456L;

        DsbMitgliedDO expectedMitglied = Mockito.mock(DsbMitgliedDO.class);
        expectedMitglied.setId(mitgliedId);
        expectedMitglied.setNachname("Musterfrau");
        expectedMitglied.setVorname("Maxime");
        DsbMannschaftDO expectedMannschaft = Mockito.mock(DsbMannschaftDO.class);
        expectedMannschaft.setId(teamId);
        expectedMannschaft.setVeranstaltungId(veranstaltungId);
        VeranstaltungDO expectedVeranstaltung = new VeranstaltungDO(veranstaltungId);
        expectedVeranstaltung.setVeranstaltungName("TestVeranstaltung");
        expectedVeranstaltung.setVeranstaltungSportJahr(2021L);
        List<WettkampfDO> expectedWettkampfList = new ArrayList<>();
        WettkampfDO wettkampfDO = Mockito.mock(WettkampfDO.class);
        wettkampfDO.setId(654L);
        wettkampfDO.setWettkampfDisziplinId(546L);
        expectedWettkampfList.add(wettkampfDO);
        VereinDO expectedvereinDO = getVereinDO();


        LizenzComponentImpl testClass = Mockito.mock(LizenzComponentImpl.class);

        // configure mocks
        when(dsbMitgliedComponent.findById(mitgliedId)).thenReturn(expectedMitglied);
        when(mannschaftComponent.findById(teamId)).thenReturn(expectedMannschaft);
        when(veranstaltungComponent.findById(expectedMannschaft.getVeranstaltungId())).thenReturn(expectedVeranstaltung);
        when(wettkampfComponent.findAllByVeranstaltungId(anyLong())).thenReturn(expectedWettkampfList);
        when(vereinComponent.findById(anyLong())).thenReturn(expectedvereinDO);
        when(lizenzDAO.findByDsbMitgliedIdAndDisziplinId(
                expectedMitglied.getId(),
                expectedWettkampfList.get(0).getWettkampfDisziplinId())).thenReturn(getLizenzBE());

 //       doNothing().when(testClass).generateLizenzenDoc(any(), any());


        // call test method
        byte[] result = underTest.getLizenzPDFasByteArray(mitgliedId, teamId);


        // assert result
        Assertions.assertThat(result).isNotEmpty();

    }

    @Test
    public void generateLizenzenDoc(){
        // prepare test data
        HashMap<String, List<String>> mapping = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add("Liga");
        list.add("Verein");
        list.add("Schütze");
        list.add("Vorname");
        list.add("2021");
        list.add("1234");
        mapping.put("456", list);

        final ByteArrayOutputStream result = new ByteArrayOutputStream();
        final PdfWriter writer = new PdfWriter(result);
        final PdfDocument pdfDocument = new PdfDocument(writer);
        final Document doc = new Document(pdfDocument, PageSize.A4);

        LizenzComponentImpl testClass = Mockito.mock(LizenzComponentImpl.class);


        // call test method
        underTest.generateLizenzenDoc(doc, mapping);

        // assert result
        Assertions.assertThat(doc).isNotNull();

        // verify invocations

    }

    @Test
    public void delete() {
        // prepare test data
        final LizenzDO input = getLizenzDO();
        final LizenzBE expectedBE = getLizenzBE();

        // call test method
        underTest.delete(input, USER);

        // verify invocations
        verify(lizenzDAO).delete(lizenzBEArgumentCaptor.capture(), anyLong());
        final LizenzBE persistedLizenzBE = lizenzBEArgumentCaptor.getValue();

        assertThat(persistedLizenzBE).isNotNull();

        assertThat(persistedLizenzBE.getLizenzId()).isEqualTo(input.getLizenzId());
    }

    @Test
    public void testGetMannschaftsLizenzenPDFasByteArray() {
        // prepare test data
        final long mitgliedId = 123L;
        final long teamId = 321L;
        final long veranstaltungId = 456L;

        DsbMitgliedDO expectedMitglied = Mockito.mock(DsbMitgliedDO.class);
        expectedMitglied.setId(mitgliedId);
        expectedMitglied.setNachname("Musterfrau");
        expectedMitglied.setVorname("Maxime");
        DsbMannschaftDO expectedMannschaft = Mockito.mock(DsbMannschaftDO.class);
        expectedMannschaft.setId(teamId);
        expectedMannschaft.setVeranstaltungId(veranstaltungId);
        VeranstaltungDO expectedVeranstaltung = new VeranstaltungDO(veranstaltungId);
        expectedVeranstaltung.setVeranstaltungName("TestVeranstaltung");
        expectedVeranstaltung.setVeranstaltungSportJahr(2021L);
        List<WettkampfDO> expectedWettkampfList = new ArrayList<>();
        WettkampfDO wettkampfDO = Mockito.mock(WettkampfDO.class);
        wettkampfDO.setId(654L);
        wettkampfDO.setWettkampfDisziplinId(546L);
        expectedWettkampfList.add(wettkampfDO);
        VereinDO expectedvereinDO = getVereinDO();

        final List<MannschaftsmitgliedDO> mannschaftsmitglieder = Collections.singletonList(getMannschaftsmitgliedDO(teamId, mitgliedId));


        LizenzComponentImpl testClass = Mockito.mock(LizenzComponentImpl.class);

        // configure mocks
        when(mannschaftsmitgliedComponent.findByTeamId(anyLong())).thenReturn(mannschaftsmitglieder);
        when(mannschaftComponent.findById(anyLong())).thenReturn(expectedMannschaft);
        when(veranstaltungComponent.findById(anyLong())).thenReturn(expectedVeranstaltung);
        when(wettkampfComponent.findAllByVeranstaltungId(anyLong())).thenReturn(expectedWettkampfList);
        when(dsbMitgliedComponent.findById(anyLong())).thenReturn(expectedMitglied);
        when(vereinComponent.findById(anyLong())).thenReturn(expectedvereinDO);
        when(lizenzDAO.findByDsbMitgliedIdAndDisziplinId(
                expectedMitglied.getId(),
                expectedWettkampfList.get(0).getWettkampfDisziplinId())).thenReturn(getLizenzBE());

        // call test method
        byte[] result = underTest.getMannschaftsLizenzenPDFasByteArray(teamId);


        // assert result
        Assertions.assertThat(result).isNotEmpty();


    }
}