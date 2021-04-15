package de.bogenliga.application.business.kampfrichter.impl.business;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.kampfrichter.api.types.KampfrichterDO;
import de.bogenliga.application.business.kampfrichter.impl.dao.KampfrichterDAO;
import de.bogenliga.application.business.kampfrichter.impl.entity.KampfrichterBE;
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
    private static final Long USERID_FALSE = -1337L;
    private static final long WETTKAMPFID = 9999;
    private static final boolean LEITEND = true;


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Rule
    public ExpectedException thrown = ExpectedException.none();
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
    public void findById_PreconditionTest_shouldThrowException() {

        //String that gets used by the exception.
        final String PRECONDITION_MSG_KAMPFRICHTER_ID = "KampfrichterDO ID must not be negative";

        //To prevent the second BusinessException in case USERID_FALSE is positive.
        final KampfrichterBE expectedBE = getKampfrichterBE();
        when(kampfrichterDAO.findById(anyLong())).thenReturn(expectedBE);

        //Set expectations
        thrown.expect(BusinessException.class);
        thrown.expectMessage("INVALID_ARGUMENT_ERROR");

        // call test method
        underTest.findById(USERID_FALSE);

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
    public void findById_When_Result_Null_shouldThrowException(){

        //String that gets used by the exception.
        final String PRECONDITION_MSG_KAMPFRICHTER = "No result found for ID";

        //Set expectations.
        thrown.expect(BusinessException.class);
        thrown.expectMessage(PRECONDITION_MSG_KAMPFRICHTER);

        when(kampfrichterDAO.findById(anyLong())).thenReturn(null);

        // call test method.
        underTest.findById(USERID);

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
    public void create_PreconditionTest_shouldThrowException() {

        //String that gets used by the exception.
        final String PRECONDITION_MSG_KAMPFRICHTER_ID = "KampfrichterDO ID must not be negative";

        // prepare test data
        final KampfrichterDO input = new KampfrichterDO(USERID_FALSE,WETTKAMPFID,LEITEND);

        //Set expectations.
        thrown.expect(BusinessException.class);
        thrown.expectMessage(PRECONDITION_MSG_KAMPFRICHTER_ID);

        // call test method.
        underTest.create(input,anyLong());
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
    public void update_PreconditionTest_shouldThrowException() {

        //String that gets used by the exception.
        final String PRECONDITION_MSG_KAMPFRICHTER_ID = "KampfrichterDO ID must not be negative";

        // prepare test data
        //final KampfrichterDO input = new KampfrichterDO(USERID_FALSE,WETTKAMPFID,LEITEND);
        KampfrichterDO input = getKampfrichterDO();
        input.setUserId(USERID_FALSE);

        //Set expectations.
        thrown.expect(BusinessException.class);
        thrown.expectMessage(PRECONDITION_MSG_KAMPFRICHTER_ID);

        // call test method.
        underTest.update(input,USERID);
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


        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.delete(null, USER))
                .withMessageContaining("must not be null")
                .withNoCause();

        // verify invocations
        verifyZeroInteractions(kampfrichterDAO);
    }

    @Test
    public void delete_getUserIDPrecondition_shouldThrowException() {

        // prepare test data
        final KampfrichterDO input = getKampfrichterDO();
        input.setUserId(USERID_FALSE);
        final String PRECONDITION_MSG_KAMPFRICHTER_ID = "KampfrichterDO ID must not be negative";

        thrown.expect(BusinessException.class);
        thrown.expectMessage(PRECONDITION_MSG_KAMPFRICHTER_ID);

        underTest.delete(input, USERID);

    }

    @Test
    public void delete_getUserIDPreconditionCurrentKampfrichterUserID_shouldThrowException() {
        // prepare test data.
        final KampfrichterDO input = getKampfrichterDO();

        //String that gets used by the exception.
        final String PRECONDITION_MSG_CURRENT_KAMPFRICHTER = "Current kampfrichter userId must not be negative";

        //Set expectations.
        thrown.expect(BusinessException.class);
        thrown.expectMessage(PRECONDITION_MSG_CURRENT_KAMPFRICHTER);

        // call test method.
        underTest.delete(input,USERID_FALSE);

    }
}