package de.bogenliga.application.business.veranstaltung.impl.business;

import java.sql.Date;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.naming.NoPermissionException;
import de.bogenliga.application.business.liga.api.LigaComponent;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.business.regionen.api.RegionenComponent;
import de.bogenliga.application.business.user.api.UserComponent;
import de.bogenliga.application.business.user.api.types.UserDO;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.business.wettkampftyp.api.WettkampfTypComponent;
import de.bogenliga.application.business.wettkampftyp.api.types.WettkampfTypDO;
import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.api.ThrowableTypeAssert;
import org.assertj.core.util.CheckReturnValue;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.veranstaltung.impl.dao.VeranstaltungDAO;
import de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungBE;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class VeranstaltungComponentImplTest {

    private static final Long USER = 0L;
    private static final Long VERSION = 0L;

    private static final Long VERANSTALTUNG_ID = 0L;
    private static final Long LAST_VERANSTALTUNG_ID = 1L;
    private static final Long VERANSTALTUNG_WETTKAMPFTYP_ID = 1L;
    private static final String VERANSTALTUNG_NAME = "";
    private static final Long VERANSTALTUNG_SPORTJAHR = 2018L;
    private static final Long LAST_VERANSTALTUNG_SPORTJAHR = 2017L;
    private static final Date VERANSTALTUNG_DSB_IDENTIFIER = new Date(1L);
    private static final Long VERANSTALTUNG_LIGALEITER_ID = 0L;
    private static final Date VERANSTALTUNG_MELDEDEADLINE = new Date(2L);
    private static final Long VERANSTALTUNG_LIGA_ID = 0L;

    private static final String VERANSTALTUNG_LIGALEITER_EMAIL = "a@b.c";
    private static final String VERANSTALTUNG_WETTKAMPFTYP_NAME = "abc";
    private static final String VERANSTALTUNG_LIGA_NAME = "def";

    private static  final Long LIGA_REGION_ID= 1L;
    private static  final Long LIGA_UebergeordneteR_LIGA_ID= 2L;
    private static  final Long LIGA_VERANTWORTLICH_ID = 1L;


    private static final Long WETTKAMPF_ID = 0L;
    private static final Date WETTKAMPF_DATUM = new Date(1L);
    private  static final String WETTKAMPF_STRASSE = "Münchenerstr. 4";
    private  static final String WETTKAMPF_PLZ = "80331";
    private  static final String WETTKAMPF_ORTSNAME = "München";
    private  static final String WETTKAMPF_ORTSINFO = "Sporthalle";

    private static final String WETTKAMPF_BEGINN ="13:00";



    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private VeranstaltungDAO veranstaltungDAO;
    @Mock
    private RegionenComponent regionenComponent;
    @Mock
    private LigaComponent ligaComponent;
    @Mock
    private WettkampfComponent wettkampfComponent;
    @Mock
    private WettkampfTypComponent wettkampfTypComponent;
    @Mock
    private UserComponent userComponent;
    @InjectMocks
    private VeranstaltungComponentImpl underTest;
    @Captor
    private ArgumentCaptor<VeranstaltungBE> veranstaltungBEArgumentCaptor;
    @CheckReturnValue
    public static <T extends Throwable> ThrowableTypeAssert<T> assertThatExceptionOfType(Class<? extends T> exceptionType) {
        return AssertionsForClassTypes.assertThatExceptionOfType(exceptionType);
    }

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



    public static UserDO getUserDO() { // da ist der User, der als Leiter der Veranstaltung hinterlegt wird
        UserDO userDO = new UserDO();
        userDO.setId (VERANSTALTUNG_LIGALEITER_ID);
        userDO.setEmail(VERANSTALTUNG_LIGALEITER_EMAIL);

        return userDO;
    }

    public static WettkampfTypDO getWettkampfTypDO() {
        WettkampfTypDO wettkampftypDO = new WettkampfTypDO(0L);
        wettkampftypDO.setId(VERANSTALTUNG_WETTKAMPFTYP_ID);
        wettkampftypDO.setName(VERANSTALTUNG_WETTKAMPFTYP_NAME);

        return wettkampftypDO;
    }

    public static LigaDO getLigaDO() {
        LigaDO ligaDO = new LigaDO();
        ligaDO.setId(VERANSTALTUNG_LIGA_ID);
        ligaDO.setName(VERANSTALTUNG_LIGA_NAME);
        ligaDO.setRegionId(LIGA_REGION_ID);
        ligaDO.setLigaUebergeordnetId(LIGA_UebergeordneteR_LIGA_ID);
        ligaDO.setLigaVerantwortlichId(LIGA_VERANTWORTLICH_ID);

        return ligaDO;
    }

    public static WettkampfDO getWettkampfDO(){
        WettkampfDO wettkampfDO = new WettkampfDO(0L);
        wettkampfDO.setId(WETTKAMPF_ID);
        wettkampfDO.setWettkampfDatum(WETTKAMPF_DATUM);
        wettkampfDO.setWettkampfBeginn(WETTKAMPF_BEGINN);
        wettkampfDO.setWettkampfStrasse(WETTKAMPF_STRASSE);
        wettkampfDO.setWettkampfPlz(WETTKAMPF_PLZ);
        wettkampfDO.setWettkampfOrtsname(WETTKAMPF_ORTSNAME);
        wettkampfDO.setWettkampfOrtsinfo(WETTKAMPF_ORTSINFO);

        return wettkampfDO;
    }


    //TODO Fix
   @Test
    public void findAll() {
        // prepare test data
        final VeranstaltungBE expectedBE = getVeranstaltungBE();
        final UserDO expectedUserDO = getUserDO();
        final WettkampfTypDO expectedWettkampfTypDO = getWettkampfTypDO();
        final LigaDO expectedligaDO = getLigaDO();
       final VeranstaltungDO expectedDO = getVeranstaltungDO();

        final List<VeranstaltungBE> expectedVeranstaltungBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(veranstaltungDAO.findAll()).thenReturn(expectedVeranstaltungBEList);

       when(ligaComponent.findById(anyLong())).thenReturn(expectedligaDO);
       when(wettkampfTypComponent.findById(anyLong())).thenReturn(expectedWettkampfTypDO);
       when(userComponent.findById(anyLong())).thenReturn(expectedUserDO);

       // call test method
        final List<VeranstaltungDO> actual = underTest.findAll();


        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getVeranstaltungID())
                .isEqualTo(expectedDO.getVeranstaltungID());

        assertThat(actual.get(0).getVeranstaltungName())
                .isEqualTo(expectedDO.getVeranstaltungName());

       assertThat(actual.get(0).getVeranstaltungWettkampftypName())
               .isEqualTo(expectedDO.getVeranstaltungWettkampftypName());
       assertThat(actual.get(0).getVeranstaltungLigaleiterEmail())
               .isEqualTo(expectedDO.getVeranstaltungLigaleiterEmail());
       assertThat(actual.get(0).getVeranstaltungLigaName())
               .isEqualTo(expectedDO.getVeranstaltungLigaName());


        // verify invocations
        verify(veranstaltungDAO).findAll();
    }
    //TODO Fix
   @Test
    public void create() {
        // prepare test data
       final VeranstaltungDO input = getVeranstaltungDO();

       final WettkampfDO expectedWettkampfDO = getWettkampfDO();
       final UserDO expectedUserDO = getUserDO();
       final WettkampfTypDO expectedWettkampfTypDO = getWettkampfTypDO();
       final LigaDO expectedligaDO = getLigaDO();
       final VeranstaltungDO expectedDO = getVeranstaltungDO();

        final VeranstaltungBE expectedBE = getVeranstaltungBE();

        // configure mocks
        when(veranstaltungDAO.create(any(VeranstaltungBE.class), anyLong())).thenReturn(expectedBE);
        when(wettkampfComponent.createWT0(anyLong(), anyLong())).thenReturn(expectedWettkampfDO);

       when(ligaComponent.findById(anyLong())).thenReturn(expectedligaDO);
       when(wettkampfTypComponent.findById(anyLong())).thenReturn(expectedWettkampfTypDO);
       when(userComponent.findById(anyLong())).thenReturn(expectedUserDO);

        // call test method
        final VeranstaltungDO actual = underTest.create(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getVeranstaltungID())
                .isEqualTo(expectedDO.getVeranstaltungID());

       assertThat(actual.getVeranstaltungName())
               .isEqualTo(expectedDO.getVeranstaltungName());

       assertThat(actual.getVeranstaltungWettkampftypName())
               .isEqualTo(expectedDO.getVeranstaltungWettkampftypName());
       assertThat(actual.getVeranstaltungLigaleiterEmail())
               .isEqualTo(expectedDO.getVeranstaltungLigaleiterEmail());
       assertThat(actual.getVeranstaltungLigaName())
               .isEqualTo(expectedDO.getVeranstaltungLigaName());


       // verify invocations
        verify(veranstaltungDAO).create(veranstaltungBEArgumentCaptor.capture(), anyLong());

        final VeranstaltungBE persistedBE = veranstaltungBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getVeranstaltung_id())
                .isEqualTo(input.getVeranstaltungID());
    }

    @Test
    public void findById() {
        // prepare test data
        final VeranstaltungBE expectedBE = getVeranstaltungBE();
        final VeranstaltungDO expectedDO = getVeranstaltungDO();
        final UserDO expectedUserDO = getUserDO();
        final WettkampfTypDO expectedWettkampfTypDO = getWettkampfTypDO();
        final LigaDO expectedligaDO = getLigaDO();

        // configure mocks
        when(veranstaltungDAO.findById(VERANSTALTUNG_ID)).thenReturn(expectedBE);
        when(ligaComponent.findById(anyLong())).thenReturn(expectedligaDO);
        when(wettkampfTypComponent.findById(anyLong())).thenReturn(expectedWettkampfTypDO);
        when(userComponent.findById(anyLong())).thenReturn(expectedUserDO);


        // call test method
        final VeranstaltungDO actual = underTest.findById(VERANSTALTUNG_ID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getVeranstaltungID())
                .isEqualTo(expectedDO.getVeranstaltungID());

        assertThat(actual.getVeranstaltungName())
                .isEqualTo(expectedDO.getVeranstaltungName());

        assertThat(actual.getVeranstaltungWettkampftypName())
                .isEqualTo(expectedDO.getVeranstaltungWettkampftypName());
        assertThat(actual.getVeranstaltungLigaleiterEmail())
                .isEqualTo(expectedDO.getVeranstaltungLigaleiterEmail());
        assertThat(actual.getVeranstaltungLigaName())
                .isEqualTo(expectedDO.getVeranstaltungLigaName());

        // verify invocations
        verify(veranstaltungDAO).findById(VERANSTALTUNG_ID);
    }
    //TODO Fix
  @Test
  public void update() {
        // prepare test data
        final VeranstaltungDO input = getVeranstaltungDO();
        final VeranstaltungDO expectedDO = getVeranstaltungDO();
        final UserDO expectedUserDO = getUserDO();
        final WettkampfTypDO expectedWettkampfTypDO = getWettkampfTypDO();
        final LigaDO expectedligaDO = getLigaDO();

        final VeranstaltungBE expectedBE = getVeranstaltungBE();

        // configure mocks
        when(veranstaltungDAO.update(any(VeranstaltungBE.class), anyLong())).thenReturn(expectedBE);
        when(ligaComponent.findById(anyLong())).thenReturn(expectedligaDO);
        when(wettkampfTypComponent.findById(anyLong())).thenReturn(expectedWettkampfTypDO);
        when(userComponent.findById(anyLong())).thenReturn(expectedUserDO);

        // call test method
        final VeranstaltungDO actual = underTest.update(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getVeranstaltungID())
              .isEqualTo(expectedDO.getVeranstaltungID());

        assertThat(actual.getVeranstaltungName())
              .isEqualTo(expectedDO.getVeranstaltungName());

        assertThat(actual.getVeranstaltungWettkampftypName())
              .isEqualTo(expectedDO.getVeranstaltungWettkampftypName());
        assertThat(actual.getVeranstaltungLigaleiterEmail())
              .isEqualTo(expectedDO.getVeranstaltungLigaleiterEmail());
        assertThat(actual.getVeranstaltungLigaName())
              .isEqualTo(expectedDO.getVeranstaltungLigaName());

        // verify invocations
        verify(veranstaltungDAO).update(veranstaltungBEArgumentCaptor.capture(), anyLong());

        final VeranstaltungBE persistedBE = veranstaltungBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getVeranstaltung_id())
                .isEqualTo(input.getVeranstaltungID());
        assertThat(persistedBE.getVeranstaltung_name())
                .isEqualTo(input.getVeranstaltungName());
    }

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

    @Test
    public void testFindByLigaleiterId() {
        // prepare test data
        final VeranstaltungBE expectedBE = getVeranstaltungBE();
        final UserDO expectedUserDO = getUserDO();
        final WettkampfTypDO expectedWettkampfTypDO = getWettkampfTypDO();
        final LigaDO expectedligaDO = getLigaDO();
        final VeranstaltungDO expectedDO = getVeranstaltungDO();

        final List<VeranstaltungBE> expectedVeranstaltungBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(veranstaltungDAO.findByLigaleiterId(VERANSTALTUNG_LIGALEITER_ID)).thenReturn(expectedVeranstaltungBEList);

        when(ligaComponent.findById(anyLong())).thenReturn(expectedligaDO);
        when(wettkampfTypComponent.findById(anyLong())).thenReturn(expectedWettkampfTypDO);
        when(userComponent.findById(anyLong())).thenReturn(expectedUserDO);

        // call test method
        final List<VeranstaltungDO> actual = underTest.findByLigaleiterId(VERANSTALTUNG_LIGALEITER_ID);


        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getVeranstaltungID())
                .isEqualTo(expectedDO.getVeranstaltungID());

        assertThat(actual.get(0).getVeranstaltungName())
                .isEqualTo(expectedDO.getVeranstaltungName());

        assertThat(actual.get(0).getVeranstaltungWettkampftypName())
                .isEqualTo(expectedDO.getVeranstaltungWettkampftypName());
        assertThat(actual.get(0).getVeranstaltungLigaleiterEmail())
                .isEqualTo(expectedDO.getVeranstaltungLigaleiterEmail());
        assertThat(actual.get(0).getVeranstaltungLigaName())
                .isEqualTo(expectedDO.getVeranstaltungLigaName());


        // verify invocations
        verify(veranstaltungDAO).findByLigaleiterId(VERANSTALTUNG_LIGALEITER_ID);
    }

    @Test
    public void testFindBySportjahr() {
        // prepare test data
        final VeranstaltungBE expectedBE = getVeranstaltungBE();
        final UserDO expectedUserDO = getUserDO();
        final WettkampfTypDO expectedWettkampfTypDO = getWettkampfTypDO();
        final LigaDO expectedligaDO = getLigaDO();
        final VeranstaltungDO expectedDO = getVeranstaltungDO();

        final List<VeranstaltungBE> expectedVeranstaltungBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(veranstaltungDAO.findBySportjahr(VERANSTALTUNG_SPORTJAHR)).thenReturn(expectedVeranstaltungBEList);

        when(ligaComponent.findById(anyLong())).thenReturn(expectedligaDO);
        when(wettkampfTypComponent.findById(anyLong())).thenReturn(expectedWettkampfTypDO);
        when(userComponent.findById(anyLong())).thenReturn(expectedUserDO);

        // call test method
        final List<VeranstaltungDO> actual = underTest.findBySportjahr(VERANSTALTUNG_SPORTJAHR);


        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getVeranstaltungID())
                .isEqualTo(expectedDO.getVeranstaltungID());

        assertThat(actual.get(0).getVeranstaltungName())
                .isEqualTo(expectedDO.getVeranstaltungName());

        assertThat(actual.get(0).getVeranstaltungWettkampftypName())
                .isEqualTo(expectedDO.getVeranstaltungWettkampftypName());
        assertThat(actual.get(0).getVeranstaltungLigaleiterEmail())
                .isEqualTo(expectedDO.getVeranstaltungLigaleiterEmail());
        assertThat(actual.get(0).getVeranstaltungLigaName())
                .isEqualTo(expectedDO.getVeranstaltungLigaName());


        // verify invocations
        verify(veranstaltungDAO).findBySportjahr(VERANSTALTUNG_SPORTJAHR);
    }

    @Test
    public void testFindByLigaID() {
        // prepare test data
        final VeranstaltungBE expectedBE = getVeranstaltungBE();
        final UserDO expectedUserDO = getUserDO();
        final WettkampfTypDO expectedWettkampfTypDO = getWettkampfTypDO();
        final LigaDO expectedligaDO = getLigaDO();
        final VeranstaltungDO expectedDO = getVeranstaltungDO();

        final List<VeranstaltungBE> expectedVeranstaltungBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(veranstaltungDAO.findByLigaID(VERANSTALTUNG_LIGA_ID)).thenReturn(expectedVeranstaltungBEList);

        when(ligaComponent.findById(anyLong())).thenReturn(expectedligaDO);
        when(wettkampfTypComponent.findById(anyLong())).thenReturn(expectedWettkampfTypDO);
        when(userComponent.findById(anyLong())).thenReturn(expectedUserDO);

        // call test method
        final List<VeranstaltungDO> actual = underTest.findByLigaID(VERANSTALTUNG_LIGA_ID);


        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getVeranstaltungID())
                .isEqualTo(expectedDO.getVeranstaltungID());

        assertThat(actual.get(0).getVeranstaltungName())
                .isEqualTo(expectedDO.getVeranstaltungName());

        assertThat(actual.get(0).getVeranstaltungWettkampftypName())
                .isEqualTo(expectedDO.getVeranstaltungWettkampftypName());
        assertThat(actual.get(0).getVeranstaltungLigaleiterEmail())
                .isEqualTo(expectedDO.getVeranstaltungLigaleiterEmail());
        assertThat(actual.get(0).getVeranstaltungLigaName())
                .isEqualTo(expectedDO.getVeranstaltungLigaName());


        // verify invocations
        verify(veranstaltungDAO).findByLigaID(VERANSTALTUNG_LIGA_ID);
    }


    /**
     * Test for findBySportjahrDestinct
     *
     * @author Johannes Schänzle, Max Weise; FH Reutlingen
     */
    @Test
    public void testfindBySportjahrDestinct(){
        // prepare test data
        final VeranstaltungBE expectedBE = getVeranstaltungBE();
        final UserDO expectedUserDO = getUserDO();
        final WettkampfTypDO expectedWettkampfTypDO = getWettkampfTypDO();
        final LigaDO expectedligaDO = getLigaDO();
        final VeranstaltungDO expectedDO = getVeranstaltungDO();

        final List<VeranstaltungBE> expectedVeranstaltungBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(veranstaltungDAO.findBySportjahrDestinct(VERANSTALTUNG_SPORTJAHR)).thenReturn(expectedVeranstaltungBEList);

        when(ligaComponent.findById(anyLong())).thenReturn(expectedligaDO);
        when(wettkampfTypComponent.findById(anyLong())).thenReturn(expectedWettkampfTypDO);
        when(userComponent.findById(anyLong())).thenReturn(expectedUserDO);

        // call test method
        final List<VeranstaltungDO> actual = underTest.findBySportjahrDestinct(VERANSTALTUNG_SPORTJAHR);


        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getVeranstaltungID())
                .isEqualTo(expectedDO.getVeranstaltungID());

        assertThat(actual.get(0).getVeranstaltungName())
                .isEqualTo(expectedDO.getVeranstaltungName());

        assertThat(actual.get(0).getVeranstaltungWettkampftypName())
                .isEqualTo(expectedDO.getVeranstaltungWettkampftypName());
        assertThat(actual.get(0).getVeranstaltungLigaleiterEmail())
                .isEqualTo(expectedDO.getVeranstaltungLigaleiterEmail());
        assertThat(actual.get(0).getVeranstaltungLigaName())
                .isEqualTo(expectedDO.getVeranstaltungLigaName());

        // verify invocations
        verify(veranstaltungDAO).findBySportjahrDestinct(VERANSTALTUNG_SPORTJAHR);
    }

    @Test
    public void testFindLastVeranstaltungById() {
        // prepare test data
        VeranstaltungBE currentVeranstaltungBE = getVeranstaltungBE();

        List<VeranstaltungBE> veranstaltungBEList = new LinkedList<>();
        VeranstaltungBE lastVeranstaltungBE = getVeranstaltungBE();
        lastVeranstaltungBE.setVeranstaltung_sportjahr(LAST_VERANSTALTUNG_SPORTJAHR);
        lastVeranstaltungBE.setVeranstaltung_id(LAST_VERANSTALTUNG_ID);
        veranstaltungBEList.add(lastVeranstaltungBE);

        //UserDO user = getUserDO();

        // configure mocks
        when(veranstaltungDAO.findById(anyLong())).thenReturn(currentVeranstaltungBE);
        when(veranstaltungDAO.findBySportjahr(anyLong())).thenReturn(veranstaltungBEList);
        when(userComponent.findById(anyLong())).thenReturn(getUserDO());
        when(wettkampfTypComponent.findById(anyLong())).thenReturn(getWettkampfTypDO());
        when(ligaComponent.findById(anyLong())).thenReturn(getLigaDO());

        // call test method
        final VeranstaltungDO actual = underTest.findLastVeranstaltungById(VERANSTALTUNG_ID);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getVeranstaltungID()).isEqualTo(LAST_VERANSTALTUNG_ID);
        assertThat(actual.getVeranstaltungSportJahr()).isEqualTo(LAST_VERANSTALTUNG_SPORTJAHR);
        assertThat(actual.getVeranstaltungLigaID()).isEqualTo(VERANSTALTUNG_LIGA_ID);
        assertThat(actual.getVeranstaltungWettkampftypID()).isEqualTo(VERANSTALTUNG_WETTKAMPFTYP_ID);

        // verify invocations
        verify(veranstaltungDAO).findById(anyLong());
        verify(veranstaltungDAO).findBySportjahr(anyLong());
    }


    @Test
    public void testFindLastVeranstaltungById_NoLastVeranstaltung() {
        // prepare test data
        VeranstaltungBE currentVeranstaltungBE = getVeranstaltungBE();

        List<VeranstaltungBE> veranstaltungBEList = new LinkedList<>();

        // configure mocks
        when(veranstaltungDAO.findById(anyLong())).thenReturn(currentVeranstaltungBE);
        when(veranstaltungDAO.findBySportjahr(anyLong())).thenReturn(veranstaltungBEList);
        when(userComponent.findById(anyLong())).thenReturn(getUserDO());
        when(wettkampfTypComponent.findById(anyLong())).thenReturn(getWettkampfTypDO());
        when(ligaComponent.findById(anyLong())).thenReturn(getLigaDO());

        // assert exception
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.findLastVeranstaltungById(VERANSTALTUNG_ID));

        // verify invocations
        verify(veranstaltungDAO).findById(anyLong());
        verify(veranstaltungDAO).findBySportjahr(anyLong());
    }



}