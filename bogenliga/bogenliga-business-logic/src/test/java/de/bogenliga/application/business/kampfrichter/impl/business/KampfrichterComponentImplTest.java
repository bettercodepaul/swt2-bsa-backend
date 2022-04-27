package de.bogenliga.application.business.kampfrichter.impl.business;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
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
import de.bogenliga.application.business.kampfrichter.api.types.KampfrichterDO;
import de.bogenliga.application.business.kampfrichter.impl.dao.KampfrichterDAO;
import de.bogenliga.application.business.kampfrichter.impl.entity.KampfrichterBE;
import de.bogenliga.application.business.kampfrichter.impl.entity.KampfrichterExtendedBE;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Rahul Pöse
 * @see <a href="http://joel-costigliola.github.io/assertj/">
 * AssertJ: Fluent assertions for java</a>
 * @see <a href="https://junit.org/junit4/">
 * JUnit4</a>
 * @see <a href="https://site.mockito.org/">
 * Mockito</a>
 * @see <a href="http://www.vogella.com/tutorials/Mockito/article.html">Using Mockito with JUnit 4</a>
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class KampfrichterComponentImplTest {

    private static final Long USER = 0L;
    private static final Long VERSION = 0L;

    private static final Long USERID = 1337L;
    private static final long WETTKAMPFID = 9999;
    private static final boolean LEITEND = true;

    private static final String VORNAME = "Sorscha";
    private static final String NACHNAME = "Kratikoff";
    private static final String EMAIL = "Sorscha.Kratikoff@test.de";


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private KampfrichterDAO kampfrichterDAO;
    @InjectMocks
    private KampfrichterComponentImpl underTest;
    @Captor
    private ArgumentCaptor<KampfrichterBE> kampfrichterBEArgumentCaptor;


    /***
     * Utility methods for creating business entities/data objects.
     * Also used by other test classes.
     */
    public static KampfrichterBE getKampfrichterBE() {
        final KampfrichterBE expectedBE = new KampfrichterBE();
        expectedBE.setKampfrichterUserId(USERID);
        expectedBE.setKampfrichterWettkampfId(WETTKAMPFID);
        expectedBE.setKampfrichterLeitend(LEITEND);

        return expectedBE;
    }


    /***
     * Utility methods for creating business entities/data objects.
     * If this method is needed in other test classes change
     * its visibility to "public"
     *
     * Author: Max Weise
     */
    private KampfrichterExtendedBE getKampfrichterExtendedBE() {
        final KampfrichterExtendedBE expectedBE = new KampfrichterExtendedBE();
        expectedBE.setKampfrichterExtendedUserID(USERID);
        expectedBE.setKampfrichterExtendedWettkampfID(WETTKAMPFID);
        expectedBE.setKampfrichterExtendedLeitend(LEITEND);
        expectedBE.setKampfrichterExtendedVorname(VORNAME);
        expectedBE.setKampfrichterExtendedNachname(NACHNAME);
        expectedBE.setKampfrichterExtendedEmail(EMAIL);

        return expectedBE;
    }

    public static KampfrichterDO getKampfrichterDO() {
        return new KampfrichterDO(
                USERID,
                WETTKAMPFID,
                LEITEND);
    }


    @Test
    public void findAll() {
        // prepare test data
        final KampfrichterBE expectedBE = getKampfrichterBE();
        final List<KampfrichterBE> expectedBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(kampfrichterDAO.findAll()).thenReturn(expectedBEList);

        // call test method
        final List<KampfrichterDO> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getUserId())
                .isEqualTo(expectedBE.getKampfrichterUserId());
        assertThat(actual.get(0).getWettkampfId())
                .isEqualTo(expectedBE.getKampfrichterWettkampfId());
        assertThat(actual.get(0).isLeitend())
                .isEqualTo(expectedBE.isKampfrichterLeitend());

        // verify invocations
        verify(kampfrichterDAO).findAll();
    }


    @Test
    public void findByWettkampfidNotInWettkampftag() {
        // prepare test data
        final KampfrichterExtendedBE expectedBE = getKampfrichterExtendedBE();
        final List<KampfrichterExtendedBE> expectedBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(kampfrichterDAO.findByWettkampfidNotInWettkampftag(WETTKAMPFID)).thenReturn(expectedBEList);

        // call test method
        final List<KampfrichterDO> actual = underTest.findByWettkampfidNotInWettkampftag(WETTKAMPFID);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getUserId())
                .isEqualTo(expectedBE.getKampfrichterExtendedUserID());
        assertThat(actual.get(0).getWettkampfId())
                .isEqualTo(expectedBE.getKampfrichterExtendedWettkampfID());
        assertThat(actual.get(0).isLeitend())
                .isEqualTo(expectedBE.getKampfrichterExtendedLeitend());

        // verify invocations
        verify(kampfrichterDAO).findByWettkampfidNotInWettkampftag(WETTKAMPFID);
    }

    @Test
    public void findById() {
        // prepare test data
        final KampfrichterBE expectedBE = getKampfrichterBE();

        // configure mocks
        when(kampfrichterDAO.findById(USERID)).thenReturn(expectedBE);

        // call test method
        final KampfrichterDO actual = underTest.findById(USERID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getUserId())
                .isEqualTo(expectedBE.getKampfrichterUserId());

        // verify invocations
        verify(kampfrichterDAO).findById(USERID);
    }


    @Test
    public void create() {
        // prepare test data
        final KampfrichterDO input = getKampfrichterDO();

        final KampfrichterBE expectedBE = getKampfrichterBE();

        // configure mocks
        when(kampfrichterDAO.create(any(KampfrichterBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final KampfrichterDO actual = underTest.create(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getUserId())
                .isEqualTo(input.getUserId());

        // verify invocations
        verify(kampfrichterDAO).create(kampfrichterBEArgumentCaptor.capture(), anyLong());

        final KampfrichterBE persistedBE = kampfrichterBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getKampfrichterUserId())
                .isEqualTo(input.getUserId());
    }


    @Test
    public void create_with_mandatory_parameters() {
        // prepare test data
        final OffsetDateTime dateTime = OffsetDateTime.now();
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        final KampfrichterDO input = new KampfrichterDO(
                USERID,
                WETTKAMPFID,
                LEITEND);

        final KampfrichterBE expectedBE = new KampfrichterBE();
        expectedBE.setKampfrichterUserId(USERID);
        expectedBE.setKampfrichterWettkampfId(WETTKAMPFID);
        expectedBE.setKampfrichterLeitend(LEITEND);
        expectedBE.setCreatedAtUtc(timestamp);
        expectedBE.setVersion(VERSION);


        // configure mocks
        when(kampfrichterDAO.create(any(KampfrichterBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final KampfrichterDO actual = underTest.create(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getUserId())
                .isEqualTo(input.getUserId());

        // verify invocations
        verify(kampfrichterDAO).create(kampfrichterBEArgumentCaptor.capture(), anyLong());

        final KampfrichterBE persistedBE = kampfrichterBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getKampfrichterUserId())
                .isEqualTo(input.getUserId());
    }


    @Test
    public void update() {
        // prepare test data
        final KampfrichterDO input = getKampfrichterDO();

        final KampfrichterBE expectedBE = getKampfrichterBE();

        // configure mocks
        when(kampfrichterDAO.update(any(KampfrichterBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final KampfrichterDO actual = underTest.update(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getUserId())
                .isEqualTo(input.getUserId());

        // verify invocations
        verify(kampfrichterDAO).update(kampfrichterBEArgumentCaptor.capture(), anyLong());

        final KampfrichterBE persistedBE = kampfrichterBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getKampfrichterUserId())
                .isEqualTo(input.getUserId());
    }


    @Test
    public void delete() {
        // prepare test data
        final KampfrichterDO input = getKampfrichterDO();

        final KampfrichterBE expectedBE = getKampfrichterBE();

        // configure mocks

        // call test method
        underTest.delete(input, USER);

        // assert result

        // verify invocations
        verify(kampfrichterDAO).delete(kampfrichterBEArgumentCaptor.capture(), anyLong());

        final KampfrichterBE persistedBE = kampfrichterBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getKampfrichterUserId())
                .isEqualTo(input.getUserId());
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
        verifyZeroInteractions(kampfrichterDAO);
    }

    @Test
    public void findById_Precondition_shouldThrowException()
    {
        //prepare test data
        final Long userId_Test = -1337L;
        final KampfrichterBE kampfrichter_Test = getKampfrichterBE();

        //call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(()-> underTest.findById(userId_Test))
                .withMessageContaining("KampfrichterDO ID must not be negative")
                .withNoCause();
    }

    @Test
    public void findById_resultIsNull_shouldThrowException()
    {
        //prepare test data
        final Long userId_Test = 1337L;
        final KampfrichterBE kampfrichter_Test = getKampfrichterBE();

        //prepare method call
        when(kampfrichterDAO.findById(anyLong())).thenReturn(null);

        //call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(()-> underTest.findById(userId_Test))
                .withMessageContaining("No result found for ID")
                .withNoCause();
    }

    @Test
    public void create_Precondition_shouldThrowException()
    {

        //prepare test data
        final long userId_Test = -1337L;
        final KampfrichterBE kampfrichterBE_Test = getKampfrichterBE();
        final KampfrichterDO kampfrichterDO_Test = getKampfrichterDO();
        kampfrichterDO_Test.setUserId(userId_Test);

        //prepare method call
        when(kampfrichterDAO.create(any(KampfrichterBE.class), anyLong())).thenReturn(kampfrichterBE_Test);

        //call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(()-> underTest.create(kampfrichterDO_Test,userId_Test))
                .withMessageContaining("KampfrichterDO ID must not be negative")
                .withNoCause();
    }

    @Test
    public void update_Precondition_shouldThrowException()
    {

        //prepare test data
        final long userId_Test = -1337L;
        final KampfrichterBE kampfrichterBE_Test = getKampfrichterBE();
        final KampfrichterDO kampfrichterDO_Test = getKampfrichterDO();
        kampfrichterDO_Test.setUserId(userId_Test);

        //prepare method call
        when(kampfrichterDAO.update(any(KampfrichterBE.class), anyLong())).thenReturn(kampfrichterBE_Test);

        //call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(()-> underTest.update(kampfrichterDO_Test,userId_Test))
                .withMessageContaining("KampfrichterDO ID must not be negative")
                .withNoCause();
    }

    @Test
    public void delete_KampfrichterUserID_Precondition_shouldThrowException()
    {

        //prepare test data
        final long userId_Test = -1337L;
        final KampfrichterDO kampfrichterDO_Test = getKampfrichterDO();
        kampfrichterDO_Test.setUserId(userId_Test);

        //call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(()-> underTest.delete(kampfrichterDO_Test,userId_Test))
                .withMessageContaining("KampfrichterDO ID must not be negative")
                .withNoCause();
    }

    @Test
    public void delete_CurrentKampfrichterUserID_Precondition_shouldThrowException()
    {

        //prepare test data
        final long userId_Test = 1337L;
        final long userID_Test_currentKampfrichterUserID = -1337;
        final KampfrichterDO kampfrichterDO_Test = getKampfrichterDO();
        kampfrichterDO_Test.setUserId(userId_Test);

        //call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(()-> underTest.delete(kampfrichterDO_Test,userID_Test_currentKampfrichterUserID))
                .withMessageContaining("Current kampfrichter userId must not be negative")
                .withNoCause();
    }

}