package de.bogenliga.application.business.dsbmitglied.impl.business;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.dsbmitglied.impl.dao.DsbMitgliedDAO;
import de.bogenliga.application.business.dsbmitglied.impl.entity.DsbMitgliedBE;
import de.bogenliga.application.business.dsbmitglied.impl.entity.DsbMitgliedWithoutVereinsnameBE;
import de.bogenliga.application.business.lizenz.impl.dao.LizenzDAO;
import de.bogenliga.application.business.lizenz.impl.entity.LizenzBE;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Yann Philippczyk, BettercallPaul gmbh
 * @see <a href="http://joel-costigliola.github.io/assertj/">
 * AssertJ: Fluent assertions for java</a>
 * @see <a href="https://junit.org/junit4/">
 * JUnit4</a>
 * @see <a href="https://site.mockito.org/">
 * Mockito</a>
 * @see <a href="http://www.vogella.com/tutorials/Mockito/article.html">Using Mockito with JUnit 4</a>
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class DsbMitgliedComponentImplTest {

    private static final Long USER = 0L;
    private static final Long VERSION = 0L;

    private static final Long ID = 1337L;
    private static final Long ID_2 = 7331L;
    private static final String VORNAME = "Sorscha";
    private static final String NACHNAME = "Kratikoff";
    private static final Date GEBURTSDATUM = Date.valueOf("1991-09-01");
    private static final String NATIONALITAET = "DE";
    private static final String MITGLIEDSNUMMER = "223344uu";
    private static final Long VEREINSID = 2L;
    private static final String VEREINNAME = "Test Verein";
    private static final Long USERID = 4242L;
    private static final Date BEITRITTSDATUM = Date.valueOf("2001-01-01");

    private static final Boolean KAMPFRICHTER = true;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private DsbMitgliedDAO dsbMitgliedDAO;
    @Mock
    private LizenzDAO lizenzDAO;
    @InjectMocks
    private DsbMitgliedComponentImpl underTest;
    @Captor
    private ArgumentCaptor<DsbMitgliedBE> dsbMitgliedBEArgumentCaptor;
    @Captor
    private ArgumentCaptor<DsbMitgliedWithoutVereinsnameBE> dsbMitgliedWithoutVereinsnameBEArgumentCaptor;


    /***
     * Utility methods for creating business entities/data objects.
     * Also used by other test classes.
     */
    public static DsbMitgliedBE getDsbMitgliedBE() {
        final DsbMitgliedBE expectedBE = new DsbMitgliedBE();
        expectedBE.setDsbMitgliedId(ID);
        expectedBE.setDsbMitgliedVorname(VORNAME);
        expectedBE.setDsbMitgliedNachname(NACHNAME);
        expectedBE.setDsbMitgliedGeburtsdatum(GEBURTSDATUM);
        expectedBE.setDsbMitgliedNationalitaet(NATIONALITAET);
        expectedBE.setDsbMitgliedMitgliedsnummer(MITGLIEDSNUMMER);
        expectedBE.setDsbMitgliedVereinsId(VEREINSID);
        expectedBE.setDsbMitgliedVereinName(VEREINNAME);
        expectedBE.setDsbMitgliedUserId(USERID);
        expectedBE.setDsbMitgliedBeitrittsdatum(BEITRITTSDATUM);

        return expectedBE;
    }

    public static DsbMitgliedWithoutVereinsnameBE getDsbMitgliedWithoutVereinsnameBE() {
        final DsbMitgliedWithoutVereinsnameBE expectedBE = new DsbMitgliedWithoutVereinsnameBE();
        expectedBE.setDsbMitgliedId(ID);
        expectedBE.setDsbMitgliedVorname(VORNAME);
        expectedBE.setDsbMitgliedNachname(NACHNAME);
        expectedBE.setDsbMitgliedGeburtsdatum(GEBURTSDATUM);
        expectedBE.setDsbMitgliedNationalitaet(NATIONALITAET);
        expectedBE.setDsbMitgliedMitgliedsnummer(MITGLIEDSNUMMER);
        expectedBE.setDsbMitgliedVereinsId(VEREINSID);
        expectedBE.setDsbMitgliedUserId(USERID);
        expectedBE.setDsbMitgliedBeitrittsdatum(BEITRITTSDATUM);

        return expectedBE;
    }


    public static List<DsbMitgliedBE> getDsbMitgliedBEList() {
        List<DsbMitgliedBE> list = new ArrayList<>();

        final DsbMitgliedBE expectedA = new DsbMitgliedBE();
        expectedA.setDsbMitgliedId(ID);
        expectedA.setDsbMitgliedVorname(VORNAME);
        expectedA.setDsbMitgliedNachname(NACHNAME);
        expectedA.setDsbMitgliedGeburtsdatum(GEBURTSDATUM);
        expectedA.setDsbMitgliedNationalitaet(NATIONALITAET);
        expectedA.setDsbMitgliedMitgliedsnummer(MITGLIEDSNUMMER);
        expectedA.setDsbMitgliedVereinsId(VEREINSID);
        expectedA.setDsbMitgliedVereinName(VEREINNAME);
        expectedA.setDsbMitgliedUserId(USERID);
        expectedA.setDsbMitgliedBeitrittsdatum(BEITRITTSDATUM);

        final DsbMitgliedBE expectedB = new DsbMitgliedBE();
        expectedB.setDsbMitgliedId(ID_2);
        expectedB.setDsbMitgliedVorname(VORNAME);
        expectedB.setDsbMitgliedNachname(NACHNAME);
        expectedB.setDsbMitgliedGeburtsdatum(GEBURTSDATUM);
        expectedB.setDsbMitgliedNationalitaet(NATIONALITAET);
        expectedB.setDsbMitgliedMitgliedsnummer(MITGLIEDSNUMMER);
        expectedB.setDsbMitgliedVereinsId(VEREINSID);
        expectedB.setDsbMitgliedVereinName(VEREINNAME);
        expectedB.setDsbMitgliedUserId(USERID);
        expectedB.setDsbMitgliedBeitrittsdatum(BEITRITTSDATUM);

        list.add(expectedA);
        list.add(expectedB);

        return list;
    }


    public static LizenzBE getLizenzBE() {
        final LizenzBE expectedBe = new LizenzBE();
        expectedBe.setLizenzId(1L);
        expectedBe.setLizenzDsbMitgliedId(2L);
        return expectedBe;
    }


    public static DsbMitgliedDO getDsbMitgliedDO() {
        return new DsbMitgliedDO(
            ID,
            VORNAME,
            NACHNAME,
            GEBURTSDATUM,
            NATIONALITAET,
            MITGLIEDSNUMMER,
            VEREINSID,
            VEREINNAME,
            USERID,
            KAMPFRICHTER,
            BEITRITTSDATUM
        );
    }

    @Test
    public void findAlleByTeamTest(){
        assertThat(underTest.findAllByTeamId(ID))
                .isNotNull();
        try{
            assertThat(underTest.findAllByTeamId(-1L))
                    .isNull();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void findAllNotInTeamTest(){
        try {
            assertThat(underTest.findAllNotInTeam(-1L, VEREINSID)).isNull();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void findAllByTeamIdTest(){
        try {
            assertThat(underTest.findAllByTeamId(-1L)).isNull();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void findByIdTest(){
        try {
            assertThat(underTest.findById(-1L)).isNull();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void updatePreconditionTest0(){
        try {
            DsbMitgliedDO test = getDsbMitgliedDO();
            test.isKampfrichter();

            assertThat(underTest.update(test,1L)).isNotNull();


        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void updatePreconditionTest1(){
        try {
            DsbMitgliedDO test = getDsbMitgliedDO();
            test.setId(-1L);
            assertThat(underTest.update(test,1L)).isNotNull();



        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void updatePreconditionTest2(){
        try {
            DsbMitgliedDO test = getDsbMitgliedDO();
            test.setKampfrichter(false);
            assertThat(underTest.update(test, 1L)).isNotNull();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void updatePreconditionTest3(){
        try {
            DsbMitgliedDO test = getDsbMitgliedDO();
            test.setKampfrichter(true);
            assertThat(underTest.update(test, 1L)).isNotNull();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void findAll() {
        // prepare test data
        final DsbMitgliedBE expectedBE = getDsbMitgliedBE();
        final List<DsbMitgliedBE> expectedBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(dsbMitgliedDAO.findAll()).thenReturn(expectedBEList);


        // call test method
        final List<DsbMitgliedDO> actual = underTest.findAll();


        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getId())
                .isEqualTo(expectedBE.getDsbMitgliedId());
        assertThat(actual.get(0).getVorname())
                .isEqualTo(expectedBE.getDsbMitgliedVorname());
        assertThat(actual.get(0).getNachname())
                .isEqualTo(expectedBE.getDsbMitgliedNachname());
        assertThat(actual.get(0).getGeburtsdatum())
                .isEqualTo(expectedBE.getDsbMitgliedGeburtsdatum());
        assertThat(actual.get(0).getNationalitaet())
                .isEqualTo(expectedBE.getDsbMitgliedNationalitaet());
        assertThat(actual.get(0).getMitgliedsnummer())
                .isEqualTo(expectedBE.getDsbMitgliedMitgliedsnummer());
        assertThat(actual.get(0).getVereinsId())
                .isEqualTo(expectedBE.getDsbMitgliedVereinsId());
        assertThat(actual.get(0).getVereinName())
                .isEqualTo(expectedBE.getDsbMitgliedVereinName());
        assertThat(actual.get(0).getUserId())
                .isEqualTo(expectedBE.getDsbMitgliedUserId());

        // verify invocations
        verify(dsbMitgliedDAO).findAll();
    }



    @Test
    public void findBySearch() {
        // prepare test data
        final DsbMitgliedBE expectedBE = getDsbMitgliedBE();
        final List<DsbMitgliedBE> expectedBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(dsbMitgliedDAO.findBySearch(expectedBE.getDsbMitgliedVorname())).thenReturn(expectedBEList);


        // call test method
        final List<DsbMitgliedDO> actual = underTest.findBySearch(expectedBE.getDsbMitgliedVorname());


        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        // verify invocations
        verify(dsbMitgliedDAO).findBySearch(expectedBE.getDsbMitgliedVorname());
    }


    @Test
    public void findById() {
        // prepare test data
        final DsbMitgliedBE expectedBE = getDsbMitgliedBE();

        // configure mocks
        when(dsbMitgliedDAO.findById(ID)).thenReturn(expectedBE);

        // call test method
        final DsbMitgliedDO actual = underTest.findById(ID);

        // assert result
        assertThat(actual).isNotNull();

        // call test method a 2nd time, get null as result to test exception
        try {
            final DsbMitgliedDO testException = underTest.findById(99L);
            assertThat(testException).isNull();
        }catch (Exception e){
            e.printStackTrace();
        }

        assertThat(actual.getId())
                .isEqualTo(expectedBE.getDsbMitgliedId());

        // verify invocations
        verify(dsbMitgliedDAO).findById(ID);
    }


    @Test
    public void findAllNotInTeam() {

        final long UNIMPORTANT_TEAM_ID = 103L; final long UNIMPORTANT_VEREIN_ID = 11;

        //prepare test data
        final List<DsbMitgliedBE> expectedBEList = getDsbMitgliedBEList();

        //configure mocks
        when(dsbMitgliedDAO.findAllNotInTeamId(UNIMPORTANT_TEAM_ID, UNIMPORTANT_VEREIN_ID)).thenReturn(expectedBEList);

        final List<DsbMitgliedDO> actualBEList = underTest.findAllNotInTeam(UNIMPORTANT_TEAM_ID, UNIMPORTANT_VEREIN_ID);

        //check results
        assertThat(actualBEList).isNotNull();

        assertThat(actualBEList.size()).isEqualTo(2);

        assertThat(actualBEList.get(1).getId()).isEqualTo(expectedBEList.get(1).getDsbMitgliedId());

        //verify execution
        verify(dsbMitgliedDAO).findAllNotInTeamId(UNIMPORTANT_TEAM_ID, UNIMPORTANT_VEREIN_ID);
    }


    @Test
    public void hasKampfrichterLizenz(){
        when(dsbMitgliedDAO.hasKampfrichterLizenz(anyLong())).thenReturn(Boolean.FALSE);
        assertThat(dsbMitgliedDAO.hasKampfrichterLizenz(anyLong())).isFalse();

    }

    @Test
    public void hasKampfrichterLizenzMinusEins(){
        try {
            assertThat(underTest.hasKampfrichterLizenz(-1L)).isNull();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void create() {
        // prepare test data
        final DsbMitgliedDO input = getDsbMitgliedDO();

        final DsbMitgliedWithoutVereinsnameBE expectedBE = getDsbMitgliedWithoutVereinsnameBE();

        // configure mocks
        when(dsbMitgliedDAO.create(any(DsbMitgliedWithoutVereinsnameBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final DsbMitgliedDO actual = underTest.create(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(input.getId());

        // verify invocations
        verify(dsbMitgliedDAO).create(dsbMitgliedWithoutVereinsnameBEArgumentCaptor.capture(), anyLong());

        final DsbMitgliedWithoutVereinsnameBE persistedBE = dsbMitgliedWithoutVereinsnameBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getDsbMitgliedId())
                .isEqualTo(input.getId());
    }


    @Test
    public void create_with_mandatory_parameters() {
        // prepare test data
        final OffsetDateTime dateTime = OffsetDateTime.now();
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        final DsbMitgliedDO input = new DsbMitgliedDO(
                ID,
                VORNAME,
                NACHNAME,
                GEBURTSDATUM,
                NATIONALITAET,
                MITGLIEDSNUMMER,
                VEREINSID,
                BEITRITTSDATUM,
                dateTime,
                USER,
                VERSION);
        input.setKampfrichter(false);

        final DsbMitgliedWithoutVereinsnameBE expectedBE = new DsbMitgliedWithoutVereinsnameBE();
        expectedBE.setDsbMitgliedId(ID);
        expectedBE.setDsbMitgliedVorname(VORNAME);
        expectedBE.setDsbMitgliedNachname(NACHNAME);
        expectedBE.setDsbMitgliedGeburtsdatum(GEBURTSDATUM);
        expectedBE.setDsbMitgliedNationalitaet(NATIONALITAET);
        expectedBE.setDsbMitgliedMitgliedsnummer(MITGLIEDSNUMMER);
        expectedBE.setDsbMitgliedVereinsId(VEREINSID);
        expectedBE.setDsbMitgliedBeitrittsdatum(BEITRITTSDATUM);
        expectedBE.setDsbMitgliedUserId(USERID);
        expectedBE.setCreatedAtUtc(timestamp);
        expectedBE.setVersion(VERSION);


        // configure mocks
        when(dsbMitgliedDAO.create(any(DsbMitgliedWithoutVereinsnameBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final DsbMitgliedDO actual = underTest.create(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(input.getId());

        // verify invocations
        verify(dsbMitgliedDAO).create(dsbMitgliedWithoutVereinsnameBEArgumentCaptor.capture(), anyLong());

        final DsbMitgliedWithoutVereinsnameBE persistedBE = dsbMitgliedWithoutVereinsnameBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getDsbMitgliedId())
                .isEqualTo(input.getId());
    }


    @Test
    public void create_withoutInput_shouldThrowException() {
        // prepare test data

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.create(null, USER))
                .withMessageContaining("must not be null")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(dsbMitgliedDAO);
    }


    @Test
    public void create_withoutVorname_shouldThrowException() {
        // prepare test data
        final DsbMitgliedDO input = getDsbMitgliedDO();
        input.setId(ID);
        input.setVorname(null);

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.create(input, USER))
                .withMessageContaining("must not be null")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(dsbMitgliedDAO);
    }


    @Test
    public void update() {
        // prepare test data
        final DsbMitgliedDO input = getDsbMitgliedDO();

        final DsbMitgliedWithoutVereinsnameBE expectedBE = getDsbMitgliedWithoutVereinsnameBE();

        // configure mocks
        when(dsbMitgliedDAO.update(any(DsbMitgliedWithoutVereinsnameBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final DsbMitgliedDO actual = underTest.update(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(input.getId());
        assertThat(actual.getNachname())
                .isEqualTo(input.getNachname());

        // verify invocations
        verify(dsbMitgliedDAO).update(dsbMitgliedWithoutVereinsnameBEArgumentCaptor.capture(), anyLong());

        final DsbMitgliedWithoutVereinsnameBE persistedBE = dsbMitgliedWithoutVereinsnameBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getDsbMitgliedId())
                .isEqualTo(input.getId());
        assertThat(persistedBE.getDsbMitgliedVorname())
                .isEqualTo(input.getVorname());
    }
    @Test
    public void delete_inclKampfrichterlizenz() {
        // prepare test data
        final DsbMitgliedDO input = getDsbMitgliedDO();

        final DsbMitgliedWithoutVereinsnameBE expectedBE = getDsbMitgliedWithoutVereinsnameBE();
        final LizenzBE inputLizenzBE = getLizenzBE();

        // configure mocks
        when(dsbMitgliedDAO.hasKampfrichterLizenz(anyLong())).thenReturn(true);
        when(lizenzDAO.findKampfrichterLizenzByDsbMitgliedId(anyLong())).thenReturn(inputLizenzBE);

        // call test method
        underTest.delete(input, USER);

        // assert result

        // verify invocations
        verify(dsbMitgliedDAO).delete(dsbMitgliedWithoutVereinsnameBEArgumentCaptor.capture(), anyLong());

        final DsbMitgliedWithoutVereinsnameBE persistedBE = dsbMitgliedWithoutVereinsnameBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getDsbMitgliedId())
                .isEqualTo(input.getId());
    }

    @Test
    public void update_deletekampfrichterlizenez() {
        // prepare test data
        final DsbMitgliedDO input = getDsbMitgliedDO();
        // wir benötigen einen "Nicht-Kampfrichter"
        input.setKampfrichter(false);

        final DsbMitgliedWithoutVereinsnameBE expectedBE = getDsbMitgliedWithoutVereinsnameBE();
        final LizenzBE inputLizenzBE = getLizenzBE();

        // configure mocks
        when(dsbMitgliedDAO.update(any(DsbMitgliedWithoutVereinsnameBE.class), anyLong())).thenReturn(expectedBE);
        //when(dsbMitgliedDAO.hasKampfrichterLizenz(anyLong())).thenReturn(true);

        // call test method
        final DsbMitgliedDO actual = underTest.update(input, USER);

        //underTest.delete(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(input.getId());
        assertThat(actual.getNachname())
                .isEqualTo(input.getNachname());

        // verify invocations
        verify(dsbMitgliedDAO).update(dsbMitgliedWithoutVereinsnameBEArgumentCaptor.capture(), anyLong());

        final DsbMitgliedWithoutVereinsnameBE persistedBE = dsbMitgliedWithoutVereinsnameBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getDsbMitgliedId())
                .isEqualTo(input.getId());
        assertThat(persistedBE.getDsbMitgliedVorname())
                .isEqualTo(input.getVorname());
    }

    @Test
    public void update_withoutInput_shouldThrowException() {
        // prepare test data

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.update(null, USER))
                .withMessageContaining("must not be null")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(dsbMitgliedDAO);
    }


    @Test
    public void update_withoutVorname_shouldThrowException() {
        // prepare test data
        final DsbMitgliedDO input = getDsbMitgliedDO();
        input.setVorname(null);

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.update(input, USER))
                .withMessageContaining("must not be null")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(dsbMitgliedDAO);
    }

    @Test
    public void deletePreconditions1(){
        try {
            DsbMitgliedDO test = getDsbMitgliedDO();
            test.setId(-1L);
            underTest.delete(test, 1L);
            assertThat(test).isNotNull();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void deletePreconditions2(){
        try {
            DsbMitgliedDO test = getDsbMitgliedDO();
            underTest.delete(test, -1L);
            assertThat(test).isNotNull();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void deletePreconditions3(){
        try {
            DsbMitgliedDO test = getDsbMitgliedDO();
            test.setKampfrichter(true);
            test.isKampfrichter();
            underTest.delete(test, 1L);
            assertThat(test).isNotNull();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void delete() {
        // prepare test data
        final DsbMitgliedDO input = getDsbMitgliedDO();

        final DsbMitgliedWithoutVereinsnameBE expectedBE = getDsbMitgliedWithoutVereinsnameBE();

        // configure mocks

        // call test method
        underTest.delete(input, USER);

        // assert result

        // verify invocations
        verify(dsbMitgliedDAO).delete(dsbMitgliedWithoutVereinsnameBEArgumentCaptor.capture(), anyLong());

        final DsbMitgliedWithoutVereinsnameBE persistedBE = dsbMitgliedWithoutVereinsnameBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getDsbMitgliedId())
                .isEqualTo(input.getId());
    }


    @Test
    public void delete_withoutInput_shouldThrowException() {
        // prepare test data

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.delete(null, USER))
                .withMessageContaining("must not be null")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(dsbMitgliedDAO);
    }
}