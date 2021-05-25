package de.bogenliga.application.business.wettkampf.impl.business;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.sql.Date;
import javax.print.Doc;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.dao.MannschaftsmitgliedDAO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.entity.MannschaftsmitgliedExtendedBE;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.veranstaltung.impl.dao.VeranstaltungDAO;
import de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungBE;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.business.wettkampf.impl.dao.WettkampfDAO;
import de.bogenliga.application.business.wettkampf.impl.entity.WettkampfBE;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Daniel Schott daniel.schott@student.reutlingen-university.de
 */
public class WettkampfComponentImplTest {

    private static final long user_Id = 13;
    private static final OffsetDateTime created_At_Utc = OffsetDateTime.now();
    private static final long version = 1234;

    private static final long wettkampf_Id = 322;
    private static final long wettkampf_Veranstaltung_Id = 12345;
    private static final Date wettkampf_Datum = new Date(20190521L);
    private static final String wettkampf_Strasse = "Reutlingerstr. 6";
    private static final String wettkampf_Plz = "72764";
    private static final String wettkampf_Ortsname = "Reutlingen";
    private static final String wettkampf_Ortsinfo = "Im Keller";
    private static final String wettkampf_Beginn = "8:00";
    private static final long wettkampf_Tag = 7;
    private static final long wettkampf_Disziplin_Id = 3;
    private static final long wettkampf_Wettkampftyp_Id = 1;
    private static final long wettkampf_Ausrichter = 8;
    public static final int PFEIL1 = 10;
    public static final int PFEIL2 = 9;
    public static final int PFEIL3 = 9;
    public static final int PFEIL4 = 7;
    public static final int PFEIL5 = 10;
    public static final int PFEIL6 = 9;
    public static final int PFEIL7 = 10;
    public static final int PFEIL8 = 6;

    private static final long mannschaft_id = 77;


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private VeranstaltungDAO veranstaltungDAO;
    @Mock
    private WettkampfDAO wettkampfDAO;
    @Mock
    private MannschaftsmitgliedDAO mannschaftsmitgliedDAO;
    @Mock
    private PasseComponent passeComponent;
    @Mock
    private DsbMannschaftComponent dsbManschaftComponent;
    @Mock
    private VereinComponent vereinComponent;
    @InjectMocks
    private WettkampfComponentImpl underTest;
    @Captor
    private ArgumentCaptor<WettkampfBE> wettkampfBEArgumentCaptor;


    /***
     * Utility methods for creating business entities/data objects.
     * Also used by other test classes.
     */
    public static WettkampfBE getWettkampfBE() {
        final WettkampfBE expectedBE = new WettkampfBE();
        expectedBE.setId(wettkampf_Id);
        expectedBE.setVeranstaltungsId(wettkampf_Veranstaltung_Id);
        expectedBE.setDatum(wettkampf_Datum);
        expectedBE.setWettkampfStrasse(wettkampf_Strasse);
        expectedBE.setWettkampfPlz(wettkampf_Plz);
        expectedBE.setWettkampfOrtsname(wettkampf_Ortsname);
        expectedBE.setWettkampfOrtsinfo(wettkampf_Ortsinfo);
        expectedBE.setWettkampfBeginn(wettkampf_Beginn);
        expectedBE.setWettkampfTag(wettkampf_Tag);
        expectedBE.setWettkampfDisziplinId(wettkampf_Disziplin_Id);
        expectedBE.setWettkampfTypId(wettkampf_Wettkampftyp_Id);
        expectedBE.setWettkampfAusrichter(wettkampf_Ausrichter);

        return expectedBE;
    }


    public static WettkampfDO getWettkampfDO() {
        return new WettkampfDO(wettkampf_Id,
                wettkampf_Veranstaltung_Id,
                wettkampf_Datum,
                wettkampf_Strasse,
                wettkampf_Plz,
                wettkampf_Ortsname,
                wettkampf_Ortsinfo,
                wettkampf_Beginn,
                wettkampf_Tag,
                wettkampf_Disziplin_Id,
                wettkampf_Wettkampftyp_Id,
                created_At_Utc,
                user_Id,
                version,
                wettkampf_Ausrichter
        );
     }

    public static List<PasseDO> getPassenDO()
    {
        PasseDO passe1 = new PasseDO(null,null, null, 2l, null, null,
                        null, PFEIL1, PFEIL2, null, null, null, null, null,
                            null, null, null, null);
        PasseDO passe2 = new PasseDO(null,null, null, 1l, null, null,
                        null, PFEIL3, PFEIL4, PFEIL5, PFEIL6, PFEIL7, PFEIL8, null,
                            null, null, null, null);
        List<PasseDO> passen = new LinkedList<>();
        passen.add(passe1);
        passen.add(passe2);
        return passen;
    }

    public static VeranstaltungBE getVeranstaltungBE()
    {
        VeranstaltungBE veranstaltung = new VeranstaltungBE();
        veranstaltung.setVeranstaltung_id(wettkampf_Veranstaltung_Id);
        veranstaltung.setVeranstaltung_name("Demo Veranstaltung");

        return veranstaltung;
    }

    public static MannschaftsmitgliedExtendedBE getMannschaftsmitgliedExtendedBE()
    {
        MannschaftsmitgliedExtendedBE newMittglied = new MannschaftsmitgliedExtendedBE();
        newMittglied.setId(0l);
        newMittglied.setDsbMitgliedVorname("Sascha");
        newMittglied.setDsbMitgliedNachname("DeTiris");

        return newMittglied;
    }

    public static DsbMannschaftDO getDsbMannschaftDO()
    {
        DsbMannschaftDO neueMannschaft = new DsbMannschaftDO(1l,"1.Manschaft Muster Hausen",1l,1l,
                0l,wettkampf_Veranstaltung_Id,0l);
        return neueMannschaft;
    }
    public static VereinDO getVereinDO()
    {
        VereinDO neuerVerein = new VereinDO(1l, "Bogensport Muster Hausen", "bmh",0l,"example.com",
                "Test Verein","Test Icon", OffsetDateTime.now(), 0l, 0l);
        return neuerVerein;
    }

        @Test
    public void findAll() {
        // prepare test data
        final WettkampfBE expectedBE = getWettkampfBE();
        final List<WettkampfBE> expectedBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(wettkampfDAO.findAll()).thenReturn(expectedBEList);

        // call test method
        final List<WettkampfDO> actual = underTest.findAll();

        // assert result
        assertThat(actual).isNotNull().isNotEmpty().hasSize(1);

        assertThat(actual.get(0)).isNotNull();
        assertThat(actual.get(0).getId()).isEqualTo(expectedBE.getId());
        assertThat(actual.get(0).getWettkampfVeranstaltungsId()).isEqualTo(expectedBE.getVeranstaltungsId());
        assertThat(actual.get(0).getWettkampfDatum()).isEqualTo(expectedBE.getDatum());
        assertThat(actual.get(0).getWettkampfStrasse()).isEqualTo(expectedBE.getWettkampfStrasse());
        assertThat(actual.get(0).getWettkampfPlz()).isEqualTo(expectedBE.getWettkampfPlz());
        assertThat(actual.get(0).getWettkampfOrtsname()).isEqualTo(expectedBE.getWettkampfOrtsname());
        assertThat(actual.get(0).getWettkampfOrtsinfo()).isEqualTo(expectedBE.getWettkampfOrtsinfo());
        assertThat(actual.get(0).getWettkampfTag()).isEqualTo(expectedBE.getWettkampfTag());
        assertThat(actual.get(0).getWettkampfDisziplinId()).isEqualTo(expectedBE.getWettkampfDisziplinId());
        assertThat(actual.get(0).getWettkampfTypId()).isEqualTo(expectedBE.getWettkampfTypId());
        assertThat(actual.get(0).getWettkampfAusrichter()).isEqualTo(expectedBE.getWettkampfAusrichter());

        // verify invocations
        verify(wettkampfDAO).findAll();
    }


    @Test
    public void findById() {
        // prepare test data
        final WettkampfBE expectedBE = getWettkampfBE();
        final WettkampfDO expectedDO = getWettkampfDO();

        // configure mocks
        when(wettkampfDAO.findById(wettkampf_Id)).thenReturn(expectedBE);

        // call test method
        final WettkampfDO actual = underTest.findById(wettkampf_Id);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId()).isEqualTo(expectedBE.getId());
        assertThat(actual.getId()).isEqualTo(expectedDO.getId());
        assertThat(actual.getWettkampfDatum()).isEqualTo(expectedDO.getWettkampfDatum());
        assertThat(actual.getWettkampfStrasse()).isEqualTo(expectedDO.getWettkampfStrasse());
        assertThat(actual.getWettkampfPlz()).isEqualTo(expectedDO.getWettkampfPlz());
        assertThat(actual.getWettkampfOrtsname()).isEqualTo(expectedDO.getWettkampfOrtsname());
        assertThat(actual.getWettkampfOrtsinfo()).isEqualTo(expectedDO.getWettkampfOrtsinfo());
        assertThat(actual.getWettkampfBeginn()).isEqualTo(expectedDO.getWettkampfBeginn());
        assertThat(actual.getWettkampfTag()).isEqualTo(expectedDO.getWettkampfTag());
        assertThat(actual.getWettkampfDisziplinId()).isEqualTo(expectedDO.getWettkampfDisziplinId());
        assertThat(actual.getWettkampfTypId()).isEqualTo(expectedDO.getWettkampfTypId());
        assertThat(actual.getWettkampfAusrichter()).isEqualTo(expectedDO.getWettkampfAusrichter());

        // verify invocations
        verify(wettkampfDAO).findById(wettkampf_Id);
    }


    @Test
    public void create() {
        // prepare test data
        final WettkampfDO input = getWettkampfDO();
        final WettkampfBE expectedBE = getWettkampfBE();

        // configure mocks
        when(wettkampfDAO.create(any(WettkampfBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final WettkampfDO actual = underTest.create(input, user_Id);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(input.getId());

        // verify invocations
        verify(wettkampfDAO).create(wettkampfBEArgumentCaptor.capture(), anyLong());

        final WettkampfBE persistedBE = wettkampfBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(input.getId());
        assertThat(actual.getWettkampfDatum()).isEqualTo(input.getWettkampfDatum());
        assertThat(actual.getWettkampfStrasse()).isEqualTo(input.getWettkampfStrasse());
        assertThat(actual.getWettkampfPlz()).isEqualTo(input.getWettkampfPlz());
        assertThat(actual.getWettkampfOrtsname()).isEqualTo(input.getWettkampfOrtsname());
        assertThat(actual.getWettkampfOrtsinfo()).isEqualTo(input.getWettkampfOrtsinfo());
        assertThat(actual.getWettkampfBeginn()).isEqualTo(input.getWettkampfBeginn());
        assertThat(actual.getWettkampfTag()).isEqualTo(input.getWettkampfTag());
        assertThat(actual.getWettkampfDisziplinId()).isEqualTo(input.getWettkampfDisziplinId());
        assertThat(actual.getWettkampfTypId()).isEqualTo(input.getWettkampfTypId());
        assertThat(actual.getWettkampfAusrichter()).isEqualTo(input.getWettkampfAusrichter());
    }


    @Test
    public void update() {
        // prepare test data
        final WettkampfDO input = getWettkampfDO();
        final WettkampfBE expectedBE = getWettkampfBE();

        // configure mocks
        when(wettkampfDAO.update(any(WettkampfBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final WettkampfDO actual = underTest.update(input, user_Id);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(input.getId());

        // verify invocations
        verify(wettkampfDAO).update(wettkampfBEArgumentCaptor.capture(), anyLong());

        final WettkampfBE persistedBE = wettkampfBEArgumentCaptor.getValue();

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(input.getId());
        assertThat(actual.getWettkampfDatum()).isEqualTo(input.getWettkampfDatum());
        assertThat(actual.getWettkampfStrasse()).isEqualTo(input.getWettkampfStrasse());
        assertThat(actual.getWettkampfPlz()).isEqualTo(input.getWettkampfPlz());
        assertThat(actual.getWettkampfOrtsname()).isEqualTo(input.getWettkampfOrtsname());
        assertThat(actual.getWettkampfOrtsinfo()).isEqualTo(input.getWettkampfOrtsinfo());
        assertThat(actual.getWettkampfBeginn()).isEqualTo(input.getWettkampfBeginn());
        assertThat(actual.getWettkampfTag()).isEqualTo(input.getWettkampfTag());
        assertThat(actual.getWettkampfDisziplinId()).isEqualTo(input.getWettkampfDisziplinId());
        assertThat(actual.getWettkampfTypId()).isEqualTo(input.getWettkampfTypId());
        assertThat(actual.getWettkampfAusrichter()).isEqualTo(input.getWettkampfAusrichter());
    }


    @Test
    public void delete() {
        // prepare test data
        final WettkampfDO input = getWettkampfDO();
        final WettkampfBE expectedBE = getWettkampfBE();

        // call test method
        underTest.delete(input, user_Id);

        // verify invocations
        verify(wettkampfDAO).delete(wettkampfBEArgumentCaptor.capture(), anyLong());

        final WettkampfBE persistedBE = wettkampfBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();
        assertThat(persistedBE.getId()).isEqualTo(input.getId());
    }


    @Test
    public void testFindAllWettkaempfeByMannschaftsId() {
        // prepare test data
        final WettkampfBE expectedBE = getWettkampfBE();
        final List<WettkampfBE> expectedBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(wettkampfDAO.findAllWettkaempfeByMannschaftsId(anyLong())).thenReturn(expectedBEList);

        // call test method
        final List<WettkampfDO> actual = underTest.findAllWettkaempfeByMannschaftsId(mannschaft_id);

        // assert result
        assertThat(actual).isNotNull().isNotEmpty().hasSize(1);

        assertThat(actual.get(0)).isNotNull();
        assertThat(actual.get(0).getId()).isEqualTo(expectedBE.getId());
        assertThat(actual.get(0).getWettkampfVeranstaltungsId()).isEqualTo(expectedBE.getVeranstaltungsId());
        assertThat(actual.get(0).getWettkampfDatum()).isEqualTo(expectedBE.getDatum());
        assertThat(actual.get(0).getWettkampfStrasse()).isEqualTo(expectedBE.getWettkampfStrasse());
        assertThat(actual.get(0).getWettkampfPlz()).isEqualTo(expectedBE.getWettkampfPlz());
        assertThat(actual.get(0).getWettkampfOrtsname()).isEqualTo(expectedBE.getWettkampfOrtsname());
        assertThat(actual.get(0).getWettkampfOrtsinfo()).isEqualTo(expectedBE.getWettkampfOrtsinfo());
        assertThat(actual.get(0).getWettkampfTag()).isEqualTo(expectedBE.getWettkampfTag());
        assertThat(actual.get(0).getWettkampfDisziplinId()).isEqualTo(expectedBE.getWettkampfDisziplinId());
        assertThat(actual.get(0).getWettkampfTypId()).isEqualTo(expectedBE.getWettkampfTypId());
        assertThat(actual.get(0).getWettkampfAusrichter()).isEqualTo(expectedBE.getWettkampfAusrichter());

        // verify invocations
        verify(wettkampfDAO).findAllWettkaempfeByMannschaftsId(anyLong());
    }


    @Test
    public void testFindAllByVeranstaltungId() {
        // prepare test data
        final WettkampfBE expectedBE = getWettkampfBE();
        final List<WettkampfBE> expectedBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(wettkampfDAO.findAllByVeranstaltungId(anyLong())).thenReturn(expectedBEList);

        // call test method
        final List<WettkampfDO> actual = underTest.findAllByVeranstaltungId(wettkampf_Veranstaltung_Id);

        // assert result
        assertThat(actual).isNotNull().isNotEmpty().hasSize(1);

        assertThat(actual.get(0)).isNotNull();
        assertThat(actual.get(0).getId()).isEqualTo(expectedBE.getId());
        assertThat(actual.get(0).getWettkampfVeranstaltungsId()).isEqualTo(expectedBE.getVeranstaltungsId());
        assertThat(actual.get(0).getWettkampfDatum()).isEqualTo(expectedBE.getDatum());
        assertThat(actual.get(0).getWettkampfStrasse()).isEqualTo(expectedBE.getWettkampfStrasse());
        assertThat(actual.get(0).getWettkampfPlz()).isEqualTo(expectedBE.getWettkampfPlz());
        assertThat(actual.get(0).getWettkampfOrtsname()).isEqualTo(expectedBE.getWettkampfOrtsname());
        assertThat(actual.get(0).getWettkampfOrtsinfo()).isEqualTo(expectedBE.getWettkampfOrtsinfo());
        assertThat(actual.get(0).getWettkampfTag()).isEqualTo(expectedBE.getWettkampfTag());
        assertThat(actual.get(0).getWettkampfDisziplinId()).isEqualTo(expectedBE.getWettkampfDisziplinId());
        assertThat(actual.get(0).getWettkampfTypId()).isEqualTo(expectedBE.getWettkampfTypId());
        assertThat(actual.get(0).getWettkampfAusrichter()).isEqualTo(expectedBE.getWettkampfAusrichter());

        // verify invocations
        verify(wettkampfDAO).findAllByVeranstaltungId(anyLong());
    }


    @Test
    public void testCreateWT0() {
        // prepare test data
        final WettkampfDO expectedDO = getWettkampfDO();
        final WettkampfBE expectedBE = getWettkampfBE();

        // configure mocks
        when(wettkampfDAO.createWettkampftag0(anyLong(), anyLong())).thenReturn(expectedBE);

        // call test method
        final WettkampfDO actual = underTest.createWT0(wettkampf_Veranstaltung_Id, user_Id);

        // assert result
        assertThat(actual).isNotNull();

        // verify invocations
        verify(wettkampfDAO).createWettkampftag0(anyLong(), anyLong());

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(expectedDO.getId());
        assertThat(actual.getWettkampfDatum()).isEqualTo(expectedDO.getWettkampfDatum());
        assertThat(actual.getWettkampfStrasse()).isEqualTo(expectedDO.getWettkampfStrasse());
        assertThat(actual.getWettkampfPlz()).isEqualTo(expectedDO.getWettkampfPlz());
        assertThat(actual.getWettkampfOrtsname()).isEqualTo(expectedDO.getWettkampfOrtsname());
        assertThat(actual.getWettkampfOrtsinfo()).isEqualTo(expectedDO.getWettkampfOrtsinfo());
        assertThat(actual.getWettkampfBeginn()).isEqualTo(expectedDO.getWettkampfBeginn());
        assertThat(actual.getWettkampfTag()).isEqualTo(expectedDO.getWettkampfTag());
        assertThat(actual.getWettkampfDisziplinId()).isEqualTo(expectedDO.getWettkampfDisziplinId());
        assertThat(actual.getWettkampfTypId()).isEqualTo(expectedDO.getWettkampfTypId());
        assertThat(actual.getWettkampfAusrichter()).isEqualTo(expectedDO.getWettkampfAusrichter());
    }

    @Test
    public void testCalcAverage()
    {
    //daten vorbereiten
    List<PasseDO> passen = getPassenDO();
    //Methode aufrufen
    float actual = underTest.calcAverage(passen,1l);
    //haben wir das erwartete ergebnis erhalten
    Assertions.assertThat(actual).isEqualTo(8.5f);
    }

    @Test
    public void testGenerateDoc() throws IOException
    {
        MannschaftsmitgliedExtendedBE exampleMitglied = getMannschaftsmitgliedExtendedBE();
        List<PasseDO> passen = getPassenDO();

        List<WettkampfBE> wettkaempfe = new ArrayList<WettkampfBE>();
        wettkaempfe.add(getWettkampfBE());

        when(wettkampfDAO.findAllWettkaempfeByMannschaftsId(anyLong())).thenReturn(wettkaempfe);
        when(veranstaltungDAO.findById(anyLong())).thenReturn(getVeranstaltungBE());
        when(mannschaftsmitgliedDAO.findAllSchuetzeInTeamEingesetzt(anyLong())).thenReturn(Arrays.asList(exampleMitglied));
        when(dsbManschaftComponent.findById(anyLong())).thenReturn(getDsbMannschaftDO());
        when(passeComponent.findByWettkampfIdAndMitgliedId(anyLong(),anyLong())).thenReturn(passen);
        when(vereinComponent.findById(anyLong())).thenReturn(getVereinDO());

        byte[] pdf = underTest.getEinzelstatistikPDFasByteArray(wettkampf_Veranstaltung_Id,mannschaft_id,2033);

        Assertions.assertThat(pdf).isNotNull().isNotEmpty();

        ByteArrayInputStream serializedPDF = new ByteArrayInputStream(pdf);
        PdfReader reader = new PdfReader(serializedPDF);
        PdfDocument deserialized = new PdfDocument(reader);
    }

    @Test
    public void testGetTeamName()
    {
        //erwartetes ergebnis definieren
        String expected = "Bogensport Muster Hausen 1";
        //mocks vorbereiten
        when(dsbManschaftComponent.findById(anyLong())).thenReturn(getDsbMannschaftDO());
        when(vereinComponent.findById(anyLong())).thenReturn(getVereinDO());
        //methode aufrufen
        String actual = underTest.getTeamName(1);
        //ergebmis prüfen
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testGetNummern()
    {
        //daten vorbereiten
        List<PasseDO> passen = getPassenDO();
        //erwartetes ergebnis
        List<Long> expected = new LinkedList<>();
        expected.add(2l);
        expected.add(1l);
        //Methode aufrufen
        List<Long> actual = underTest.getNummern(passen);
        //haben wir das erwartete ergebnis erhalten
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}
