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

import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.ligatabelle.api.LigatabelleComponent;
import de.bogenliga.application.business.ligatabelle.api.types.LigatabelleDO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.business.wettkampf.impl.dao.WettkampfDAO;
import de.bogenliga.application.business.wettkampf.impl.entity.WettkampfBE;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
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
    private static final long match_Begegnung = 1;
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
    private VeranstaltungComponent veranstaltungComponent;
    @Mock
    private WettkampfDAO wettkampfDAO;
    @Mock
    private MannschaftsmitgliedComponent mannschaftsmitgliedComponent;
    @Mock
    private PasseComponent passeComponent;
    @Mock
    private DsbMannschaftComponent dsbManschaftComponent;
    @Mock
    private VereinComponent vereinComponent;
    @Mock
    private MatchComponent matchComponent;
    @Mock
    private LigatabelleComponent ligatabelleComponent;
    @InjectMocks
    private WettkampfComponentImpl underTest;
    @Captor
    private ArgumentCaptor<WettkampfBE> wettkampfBEArgumentCaptor;
    @Mock
    private MatchDO matchDO;


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

    public static MatchDO getMatchDO()
    {
        final MatchDO expectedDO = new MatchDO(0l,0l,
                wettkampf_Id,
                mannschaft_id,
                match_Begegnung,
                1l,
                8l,
                0l,
                0l,
                0l,
                1l,
                0l,
                0l,
                null,
                0l,
                null,
                0l,
                1l);
        return expectedDO;
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
        PasseDO passe1 = new PasseDO(null,77l, null, 2l, null, 1l,
                        null, PFEIL1, PFEIL2, null, null, null, null, null,
                            null, null, null, null);
        PasseDO passe2 = new PasseDO(null,77l, null, 1l, null, 2l,
                        null, PFEIL3, PFEIL4, PFEIL5, PFEIL6, PFEIL7, PFEIL8, null,
                            null, null, null, null);
        List<PasseDO> passen = new LinkedList<>();
        passen.add(passe1);
        passen.add(passe2);
        return passen;
    }

    public static List<PasseDO> getEmptyPassenDO()
    {
        PasseDO passe1 = new PasseDO(null,null, null, 2l, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null, null);
        PasseDO passe2 = new PasseDO(null,null, null, 1l, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null, null);
        List<PasseDO> passen = new LinkedList<>();
        passen.add(passe1);
        passen.add(passe2);
        return passen;
    }

    public static VeranstaltungDO getVeranstaltungDO()
    {
        VeranstaltungDO veranstaltung = new VeranstaltungDO();
        veranstaltung.setVeranstaltungID(wettkampf_Veranstaltung_Id);
        veranstaltung.setVeranstaltungName("Demo Veranstaltung");

        return veranstaltung;
    }

    public static MannschaftsmitgliedDO getMannschaftsmitgliedDO()
    {
        MannschaftsmitgliedDO newMittglied = new MannschaftsmitgliedDO(1L, 2L);
        newMittglied.setId(1l);
        newMittglied.setDsbMitgliedVorname("Sascha");
        newMittglied.setDsbMitgliedNachname("DeTiris");
        newMittglied.setDsbMitgliedId(2l);

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
    public static LigatabelleDO getLigatabelleDO()
    {
        LigatabelleDO ligaTabelle = new LigatabelleDO(wettkampf_Veranstaltung_Id,"Test Veranstaltung",1l,
                (int)wettkampf_Tag,mannschaft_id,42,42l,"Bogensport Muster Hausen",1,2,
                3,4,5,1,1);
        return ligaTabelle;
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

        wettkampfDAO.findById(-1);
        assertThatThrownBy(() -> underTest.findById(-1)).isInstanceOf(BusinessException.class);

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
    public void testFindByAusrichter(){
        assertThat(underTest.findByAusrichter(0)).isNotNull();
    }

    @Test
    public void testFindById(){
        assertThatThrownBy(() -> underTest.findById(-1)).isInstanceOf(BusinessException.class);

        when(wettkampfDAO.findById(anyLong())).thenReturn(null);
        assertThatThrownBy(() -> underTest.findById(42)).isInstanceOf(BusinessException.class);
    }

    @Test
    public void testFindAllWettkaempfeByMannschaftsId() {
        assertThatThrownBy(() -> underTest.findAllWettkaempfeByMannschaftsId(-1)).isInstanceOf(BusinessException.class);
        
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
        assertThatThrownBy(() -> underTest.findAllByVeranstaltungId(-1)).isInstanceOf(BusinessException.class);

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
        float actual = underTest.calcAverage(passen);
        //haben wir das erwartete ergebnis erhalten
        Assertions.assertThat(actual).isEqualTo(8.75f);

        //Testabdeckung: im Falle dass der Pfeil == null
        passen = getEmptyPassenDO();
        for(PasseDO passe : passen)
        {
            //erwartetes Ergebnis definieren
            Assertions.assertThat(passe.getPfeil1()).isNull();
        }
    }

    @Test
    public void testCalcAverageEinzel()
    {
        //daten vorbereiten
        List<PasseDO> passen = getPassenDO();
        //Methode aufrufen
        float actual = underTest.calcAverageEinzel(passen,1l);
        //haben wir das erwartete ergebnis erhalten
        Assertions.assertThat(actual).isEqualTo(8.5f);
    }

    @Test
    public void testEinzelstatistik() throws IOException
    {
        assertThatThrownBy(() -> underTest.getPDFasByteArray("Einzelstatistik",-1,0,2034)).isInstanceOf(BusinessException.class);

        prepareMocksForPDFTest();

        for(int i = 0; i < 2; i++) {
            byte[] pdf = underTest.getPDFasByteArray("Einzelstatistik",wettkampf_Veranstaltung_Id, mannschaft_id, 2033);

            Assertions.assertThat(pdf).isNotNull().isNotEmpty();

            ByteArrayInputStream serializedPDF = new ByteArrayInputStream(pdf);
            PdfReader reader = new PdfReader(serializedPDF);
            PdfDocument deserialized = new PdfDocument(reader);

            prepare2ndMocksForPDFTest();
        }
    }

    @Test
    public void testGesamtstatistik() throws IOException
    {
        assertThatThrownBy(() -> underTest.getPDFasByteArray("Gesamtstatistik",-1,0,2034)).isInstanceOf(BusinessException.class);

        prepareMocksForPDFTest();

        //Run the Test 2 times
        for(int i = 0; i < 2; i++) {
            byte[] pdf = underTest.getPDFasByteArray("Gesamtstatistik", wettkampf_Veranstaltung_Id, mannschaft_id, 2034);

            Assertions.assertThat(pdf).isNotNull().isNotEmpty();

            ByteArrayInputStream serializedPDF = new ByteArrayInputStream(pdf);
            PdfReader reader = new PdfReader(serializedPDF);
            PdfDocument deserialized = new PdfDocument(reader);

            prepare2ndMocksForPDFTest();
        }

        when(wettkampfDAO.findAllWettkaempfeByMannschaftsId(anyLong())).thenReturn(new ArrayList());
        assertThatThrownBy(() -> underTest.getPDFasByteArray("Gesamtstatistik", wettkampf_Veranstaltung_Id, mannschaft_id, 2034)).isInstanceOf(BusinessException.class);
    }

    private void prepareMocksForPDFTest()
    {
        MannschaftsmitgliedDO exampleMitglied = getMannschaftsmitgliedDO();

        List<WettkampfBE> wettkaempfe = new ArrayList<WettkampfBE>();
        wettkaempfe.add(getWettkampfBE());
        wettkaempfe.add(getWettkampfBE());

        when(wettkampfDAO.findAllWettkaempfeByMannschaftsId(anyLong())).thenReturn(wettkaempfe);
        when(veranstaltungComponent.findById(anyLong())).thenReturn(getVeranstaltungDO());
        when(mannschaftsmitgliedComponent.findAllSchuetzeInTeamEingesetzt(anyLong())).thenReturn(Arrays.asList(exampleMitglied));
        when(dsbManschaftComponent.findById(anyLong())).thenReturn(getDsbMannschaftDO());
        when(passeComponent.findByWettkampfIdAndMitgliedId(anyLong(),anyLong())).thenReturn(getPassenDO());
        when(vereinComponent.findById(anyLong())).thenReturn(getVereinDO());
    }
    private void prepare2ndMocksForPDFTest()
    {
        when(passeComponent.findByWettkampfIdAndMitgliedId(anyLong(),anyLong())).thenReturn(new ArrayList());
    }

    @Test
    public void testGetTeamName()
    {
        assertThatThrownBy(() -> underTest.getTeamName(-1)).isInstanceOf(BusinessException.class);
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
        expected.add(1l);
        //Methode aufrufen
        List<Long> actual = underTest.getNummern(passen);
        expected.remove(2);
        //haben wir das erwartete ergebnis erhalten
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testFindWT0byVeranstaltungsId(){
        WettkampfBE expectedBE = getWettkampfBE();
        final WettkampfDO expectedDO = getWettkampfDO();

        when(wettkampfDAO.findWT0byVeranstaltungsId(wettkampf_Veranstaltung_Id)).thenReturn(expectedBE);
        // call test method
        WettkampfDO actual= underTest.findWT0byVeranstaltungsId(wettkampf_Veranstaltung_Id);

        // assert result
        assertThat(actual).isNotNull();

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
    public void testGetUebersichtPDFasByteArray() throws IOException {
        assertThatThrownBy(() -> underTest.getUebersichtPDFasByteArray(-1,1)).isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> underTest.getUebersichtPDFasByteArray(1,-1)).isInstanceOf(BusinessException.class);

        List<WettkampfBE> wettkampflisteBEList = new ArrayList<WettkampfBE>();
        wettkampflisteBEList.add(getWettkampfBE());
        wettkampflisteBEList.add(getWettkampfBE());

        long expectedWettkampfTag = 7;
        int veranstaltungsid = 1;

        underTest.setMatchComponent(matchComponent);
        underTest.setLigatabelleComponent(ligatabelleComponent);

        when(wettkampfDAO.findAllByVeranstaltungId(anyLong())).thenReturn(wettkampflisteBEList);
        when(matchComponent.findByWettkampfId(anyLong())).thenReturn(Collections.singletonList(getMatchDO()));
        when(veranstaltungComponent.findById(anyLong())).thenReturn(getVeranstaltungDO());
        when(dsbManschaftComponent.findById(anyLong())).thenReturn(getDsbMannschaftDO());
        when(vereinComponent.findById(anyLong())).thenReturn(getVereinDO());
        when(ligatabelleComponent.getLigatabelleWettkampf(anyLong())).thenReturn(Collections.singletonList(getLigatabelleDO()));
        
        assertThat(wettkampflisteBEList.get(veranstaltungsid).getWettkampfTag()).isEqualTo(expectedWettkampfTag);

        byte[] pdf = underTest.getUebersichtPDFasByteArray(veranstaltungsid, expectedWettkampfTag);

        Assertions.assertThat(pdf).isNotNull().isNotEmpty();

        ByteArrayInputStream serializedPDF = new ByteArrayInputStream(pdf);
        PdfReader reader = new PdfReader(serializedPDF);
        PdfDocument deserialized = new PdfDocument(reader);

        when(wettkampfDAO.findAllByVeranstaltungId(anyLong())).thenReturn(Collections.EMPTY_LIST);

        assertThatThrownBy(() -> underTest.getUebersichtPDFasByteArray(veranstaltungsid, expectedWettkampfTag)).isInstanceOf(BusinessException.class);
    }

    @Test
    public void addPassenVonSatz()
    {
        assertThat(underTest.addPassenVonSatz(Collections.EMPTY_LIST,-1,-1,-1)).isEqualTo(-1);
        List<PasseDO> passenList = getPassenDO();
        assertThat(underTest.addPassenVonSatz(passenList,0,0,0)).isEqualTo(-1);
        assertThat(underTest.addPassenVonSatz(passenList,1l,2l,77)).isEqualTo(19);
        assertThat(underTest.addPassenVonSatz(passenList,2l,1l,77)).isEqualTo(51);
    }


    @Test
    public void sortForDisplay()
    {
        List<MatchDO> matches = new ArrayList<>();
        matches.add(getMatchDO());
        matches.add(getMatchDO());
        matches.add(getMatchDO());
        List<MatchDO> result = underTest.sortForDisplay(matches);
        
        assertThat(result).isNotNull();
    }
}

