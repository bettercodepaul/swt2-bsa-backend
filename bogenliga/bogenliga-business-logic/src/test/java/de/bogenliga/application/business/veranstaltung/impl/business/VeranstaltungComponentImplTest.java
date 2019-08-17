package de.bogenliga.application.business.veranstaltung.impl.business;

import java.sql.Date;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.liga.impl.entity.LigaBE;
import de.bogenliga.application.business.regionen.impl.dao.RegionenDAO;
import de.bogenliga.application.business.regionen.impl.entity.RegionenBE;
import de.bogenliga.application.business.user.impl.entity.UserBE;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.veranstaltung.impl.dao.VeranstaltungDAO;
import de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungBE;
import de.bogenliga.application.business.wettkampf.impl.entity.WettkampfBE;
import de.bogenliga.application.business.wettkampftyp.impl.entity.WettkampfTypBE;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class VeranstaltungComponentImplTest {

    private static final Long USER = 0L;
    private static final Long VERSION = 0L;

    private static final Long VERANSTALTUNG_ID = 0L;
    private static final Long VERANSTALTUNG_WETTKAMPFTYP_ID = 1L;
    private static final String VERANSTALTUNG_NAME = "";
    private static final Long VERANSTALTUNG_SPORTJAHR = 2018L;
    private static final Date VERANSTALTUNG_DSB_IDENTIFIER = new Date(1L);
    private static final Long VERANSTALTUNG_LIGALEITER_ID = 0L;
    private static final Date VERANSTALTUNG_MELDEDEADLINE = new Date(2L);
    private static final Long VERANSTALTUNG_LIGA_ID = 0L;

    private static final String VERANSTALTUNG_LIGALEITER_EMAIL = "a@b.c";
    private static final String VERANSTALTUNG_WETTKAMPFTYP_NAME = "abc";
    private static final String VERANSTALTUNG_LIGA_NAME = "def";


    private static final Long REGION_ID = 5L;
    private static final String REGION_KUERZEL = "asdasd";
    private static final String REGION_TYPE = "lsdf";
    private static final Long REGION_UEBERGEORDNET = 1L;
    private static final String REGION_NAME = "efsf";


    private static  final Long LIGA_REGION_ID= 1L;
    private static  final Long LIGA_UebergeordneteR_LIGA_ID= 2L;
    private static  final Long LIGA_VERANTWORTLICH_ID = 1L;


    private static final Long WETTKAMPF_ID = 0L;
    private static final Date WETTKAMPF_DATUM = new Date(1L);
    private  static final String WETTKAMPF_ORT = "München";
    private static final String WETTKAMPF_BEGINN ="13:00";



    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private VeranstaltungDAO veranstaltungDAO;
    @Mock
    private RegionenDAO regionenDAO;
    @InjectMocks
    private VeranstaltungComponentImpl underTest;
    @Captor
    private ArgumentCaptor<VeranstaltungBE> veranstaltungBEArgumentCaptor;

    private static VeranstaltungBE getVeranstaltungBE() {
        VeranstaltungBE expectedBE = new VeranstaltungBE();
        expectedBE.setVeranstaltung_id(VERANSTALTUNG_ID);
        expectedBE.setVeranstaltung_wettkampftyp_id(VERANSTALTUNG_WETTKAMPFTYP_ID);
        expectedBE.setVeranstaltung_name(VERANSTALTUNG_NAME);
        expectedBE.setVeranstaltung_sportjahr(VERANSTALTUNG_SPORTJAHR);
        expectedBE.setVeranstaltung_meldedeadline(VERANSTALTUNG_MELDEDEADLINE);
        expectedBE.setVeranstaltung_ligaleiter_id(VERANSTALTUNG_LIGALEITER_ID);
        expectedBE.setVeranstaltung_liga_id(VERANSTALTUNG_LIGA_ID);

        return expectedBE;
    }

    public static VeranstaltungDO getVeranstaltungDO() {

        return new VeranstaltungDO(VERANSTALTUNG_ID,
                VERANSTALTUNG_WETTKAMPFTYP_ID,
                VERANSTALTUNG_NAME,
                VERANSTALTUNG_SPORTJAHR,
                VERANSTALTUNG_MELDEDEADLINE,
                VERANSTALTUNG_LIGALEITER_ID,
                VERANSTALTUNG_LIGA_ID,
                VERANSTALTUNG_LIGALEITER_EMAIL,
                VERANSTALTUNG_WETTKAMPFTYP_NAME,
                VERANSTALTUNG_LIGA_NAME);
    }


    public static RegionenBE getRegionenBE() { // da ist die region die der Veranstaltung ID entspricht
        RegionenBE regionenBE = new RegionenBE();
        regionenBE.setRegionId(REGION_ID);
        regionenBE.setRegionKuerzel(REGION_KUERZEL);
        regionenBE.setRegionTyp(REGION_TYPE);
        regionenBE.setRegionUebergeordnet(REGION_UEBERGEORDNET);
        regionenBE.setRegionName(REGION_NAME);

        return regionenBE;
    }

    public static UserBE getUserBE() { // da ist die region die der Veranstaltung ID entspricht
        UserBE UserBE = new UserBE();
        UserBE.setUserId(REGION_ID);
        UserBE.setUserEmail(REGION_KUERZEL);

        return UserBE;
    }

    public static WettkampfTypBE getWettkampfTypBE() {
        WettkampfTypBE wettkampftypBE = new WettkampfTypBE();
        wettkampftypBE.setwettkampftypID(VERANSTALTUNG_WETTKAMPFTYP_ID);
        wettkampftypBE.setwettkampftypname(VERANSTALTUNG_WETTKAMPFTYP_NAME);

        return wettkampftypBE;
    }

    public static LigaBE getLigaBE() {
        LigaBE ligaBE = new LigaBE();
        ligaBE.setLigaId(VERANSTALTUNG_LIGA_ID);
        ligaBE.setLigaName(VERANSTALTUNG_LIGA_NAME);
        ligaBE.setLigaRegionId(LIGA_REGION_ID);
        ligaBE.setLigaUebergeordnetId(LIGA_UebergeordneteR_LIGA_ID);
        ligaBE.setLigaVerantwortlichId(LIGA_VERANTWORTLICH_ID);

        return ligaBE;
    }

    public static WettkampfBE getWettkampfBE(){
        WettkampfBE wettkampfBE = new WettkampfBE();
        wettkampfBE.setId(WETTKAMPF_ID);
        wettkampfBE.setDatum(WETTKAMPF_DATUM);
        wettkampfBE.setWettkampfBeginn(WETTKAMPF_BEGINN);
        wettkampfBE.setWettkampfOrt(WETTKAMPF_ORT);
         return wettkampfBE;
    }


    //TODO Fix
   /* @Test
    public void findAll() {
        // prepare test data
        final VeranstaltungBE expectedBE = getVeranstaltungBE();
        final UserBE expectedUserBE = getUserBE();
        final WettkampfTypBE expectedWettkamofBE = getWettkampfTypBE();
        final LigaBE expectedligaBE = getLigaBE();


        final RegionenBE expectedRegionBE = getRegionenBE();
        final List<RegionenBE> expectedRegionBEList = Collections.singletonList(expectedRegionBE);
        final List<VeranstaltungBE> expectedVeranstaltungBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(veranstaltungDAO.findAll()).thenReturn(expectedVeranstaltungBEList);

        when(regionenDAO.findAll()).thenReturn(expectedRegionBEList);
        // call test method
        final List<VeranstaltungDO> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getVeranstaltungID())
                .isEqualTo(expectedBE.getVeranstaltung_id());

        assertThat(actual.get(0).getVeranstaltungName())
                .isEqualTo(expectedBE.getVeranstaltung_name());

        assertThat(actual.get(0).getVeranstaltungWettkampftypName())
                .isEqualTo(getRegionenBE().getRegionId());

        //assertThat(actual.get(0).getDsbIdentifier())
        //        .isEqualTo(expectedBE.getVeranstaltungDsbIdentifier());

        assertThat(getRegionenBE())
                .isEqualTo(expectedRegionBE.getRegionName());


        // verify invocations
        verify(veranstaltungDAO).findAll();
    }*/
    //TODO Fix
   /* @Test
    public void create() {
        // prepare test data
        final VeranstaltungDO input = getVeranstaltungDO();

        final VeranstaltungBE expectedBE = getVeranstaltungBE();

        // configure mocks
        when(veranstaltungDAO.create(any(VeranstaltungBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final VeranstaltungDO actual = underTest.create(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getVeranstaltungID())
                .isEqualTo(input.getVeranstaltungID());

        // verify invocations
        verify(veranstaltungDAO).create(veranstaltungBEArgumentCaptor.capture(), anyLong());

        final VeranstaltungBE persistedBE = veranstaltungBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getVeranstaltung_id())
                .isEqualTo(input.getVeranstaltungID());
    }
    //TODO Fix
    /*@Test
    public void findById() {
        // prepare test data
        final VeranstaltungBE expectedBE = getVeranstaltungBE();

        // configure mocks
        when(veranstaltungDAO.findById(VERANSTALTUNG_ID)).thenReturn(expectedBE);

        // call test method
        final VeranstaltungDO actual = underTest.findById(VERANSTALTUNG_ID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getVeranstaltungID())
                .isEqualTo(expectedBE.getVeranstaltung_id());

        // verify invocations
        verify(veranstaltungDAO).findById(VERANSTALTUNG_ID);
    }*/
    //TODO Fix
  /*  @Test
  public void update() {
        // prepare test data
        final VeranstaltungDO input = getVeranstaltungDO();

        final VeranstaltungBE expectedBE = getVeranstaltungBE();

        // configure mocks
        when(veranstaltungDAO.update(any(VeranstaltungBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final VeranstaltungDO actual = underTest.update(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getVeranstaltungID())
                .isEqualTo(input.getVeranstaltungID());
        assertThat(actual.getVeranstaltungName())
                .isEqualTo(input.getVeranstaltungName());

        // verify invocations
        verify(veranstaltungDAO).update(veranstaltungBEArgumentCaptor.capture(), anyLong());

        final VeranstaltungBE persistedBE = veranstaltungBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getVeranstaltung_id())
                .isEqualTo(input.getVeranstaltungID());
        assertThat(persistedBE.getVeranstaltung_name())
                .isEqualTo(input.getVeranstaltungName());
    }
*/
    @Test
    public void delete() {
        VeranstaltungDO input = getVeranstaltungDO();

        VeranstaltungBE expectedBE = getVeranstaltungBE();

        // configure mocks

        // call test method
        underTest.delete(input, USER);

        // assert result

        // verify invocations
        verify(veranstaltungDAO).delete(veranstaltungBEArgumentCaptor.capture(), anyLong());

        VeranstaltungBE persistedBE = veranstaltungBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getVeranstaltung_id())
                .isEqualTo(input.getVeranstaltungID());
    }

    @Test
    public void equals(){
         VeranstaltungDO underTest = new VeranstaltungDO(VERANSTALTUNG_ID,
                 VERANSTALTUNG_WETTKAMPFTYP_ID,
                 VERANSTALTUNG_NAME,
                 VERANSTALTUNG_SPORTJAHR,
                 VERANSTALTUNG_MELDEDEADLINE,
                 VERANSTALTUNG_LIGALEITER_ID,
                 VERANSTALTUNG_LIGA_ID,
                 VERANSTALTUNG_LIGALEITER_EMAIL,
                 VERANSTALTUNG_WETTKAMPFTYP_NAME,
                 VERANSTALTUNG_LIGA_NAME);
         assertThat(underTest.getVeranstaltungWettkampftypName()).isEqualTo(getVeranstaltungDO().getVeranstaltungWettkampftypName());
         assertEquals(underTest,getVeranstaltungDO());
    }
}