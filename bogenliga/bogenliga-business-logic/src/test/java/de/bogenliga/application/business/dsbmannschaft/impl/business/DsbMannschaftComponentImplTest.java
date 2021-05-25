package de.bogenliga.application.business.dsbmannschaft.impl.business;

import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.dsbmannschaft.impl.dao.DsbMannschaftDAO;
import de.bogenliga.application.business.dsbmannschaft.impl.entity.DsbMannschaftBE;
import de.bogenliga.application.business.vereine.impl.dao.VereinDAO;
import de.bogenliga.application.business.vereine.impl.entity.VereinBE;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class DsbMannschaftComponentImplTest {
    private static final Long USER = 0L;
    private static final Long VERSION = 0L;

    private static final long ID = 2222L;
    private static final long VEREIN_ID =101010L;
    private static final long LAST_VEREIN_ID =102020L;
    private static final long NUMMER =111L;
    private static final long BENUTZER_ID =12L;
    private static final long VERANSTALTUNG_ID =1L;
    private static final long CURRENT_VERANSTALTUNG_ID =2L;
    private static final long SORTIERUNG =1L;

    private static final long DB_SORTIERUNG =0L;
    
    private static final String VEREIN_NAME = "Testverein";
    private static final String MA_NAME = VEREIN_NAME+" "+ NUMMER;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private DsbMannschaftDAO dsbMannschaftDAO ;
    @Mock
    private VereinDAO vereinDAO;
    @InjectMocks
    private DsbMannschaftComponentImpl underTest;
    @Captor
    private ArgumentCaptor<DsbMannschaftBE> dsbMannschaftBEArgumentCaptor;



    /***
     * Utility methods for creating business entities/data objects.
     * Also used by other test classes.
     */
    public static DsbMannschaftBE getDsbMannschaftBE() {
        final DsbMannschaftBE expectedBE = new DsbMannschaftBE();

        expectedBE.setId(ID);
        expectedBE.setVereinId(VEREIN_ID);
        expectedBE.setNummer(NUMMER);
        expectedBE.setBenutzerId(BENUTZER_ID);
        expectedBE.setVeranstaltungId(VERANSTALTUNG_ID);
        expectedBE.setSortierung(SORTIERUNG);



        return expectedBE;
    }

    public static DsbMannschaftDO getDsbMannschaftDO() {
        return new DsbMannschaftDO(
                ID,
                MA_NAME,
                VEREIN_ID,
                NUMMER,
                BENUTZER_ID,
                VERANSTALTUNG_ID,
                SORTIERUNG);
    }

    public static DsbMannschaftDO getSortierungsDO(){
        return new DsbMannschaftDO(
                ID,
                null,
                0L,
                0L,
                0L,
                0L,
                SORTIERUNG
        );
    }

    public static VereinBE getVereinBE(){
        VereinBE vereinBE = new VereinBE();
        vereinBE.setVereinId(VEREIN_ID);
        vereinBE.setVereinName(VEREIN_NAME);
        return vereinBE;
    }

    @Test
    public void findAll() {
        // prepare test data
        final DsbMannschaftBE expectedBE = getDsbMannschaftBE();
        final List<DsbMannschaftBE> expectedBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(dsbMannschaftDAO.findAll()).thenReturn(expectedBEList);

        // call test method
        final List<DsbMannschaftDO> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getId())
                .isEqualTo(expectedBE.getId());
        assertThat(actual.get(0).getBenutzerId())
                .isEqualTo(expectedBE.getBenutzerId());
        assertThat(actual.get(0).getNummer())
                .isEqualTo(expectedBE.getNummer());
        assertThat(actual.get(0).getVeranstaltungId())
                .isEqualTo(expectedBE.getVeranstaltungId());
        assertThat(actual.get(0).getVereinId())
                .isEqualTo(expectedBE.getVereinId());
        assertThat(actual.get(0).getSortierung())
                .isEqualTo(expectedBE.getSortierung());


        // verify invocations
        verify(dsbMannschaftDAO).findAll();
        verify(vereinDAO).findById(anyLong());
    }

    @Test
    public void findAllByVereinsId() {
        // prepare test data
        final DsbMannschaftBE expectedBE = getDsbMannschaftBE();
        final List<DsbMannschaftBE> expectedBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(dsbMannschaftDAO.findAllByVereinsId(VEREIN_ID)).thenReturn(expectedBEList);
        when(dsbMannschaftDAO.findAllByVereinsId(VEREIN_ID+1)).thenReturn(null);

        // call test method
        final List<DsbMannschaftDO> actual = underTest.findAllByVereinsId(VEREIN_ID);

        // check if expected exception is thrown if id isn't contained
        assertThatThrownBy(()-> underTest.findAllByVereinsId(VEREIN_ID+1))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(String.format("'%d'", VEREIN_ID+1));

        // check if expected exception is thrown if id is negative
        assertThatThrownBy(()-> underTest.findAllByVereinsId(-1))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.INVALID_ARGUMENT_ERROR.getValue());

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getId())
                .isEqualTo(expectedBE.getId());
        assertThat(actual.get(0).getBenutzerId())
                .isEqualTo(expectedBE.getBenutzerId());
        assertThat(actual.get(0).getNummer())
                .isEqualTo(expectedBE.getNummer());
        assertThat(actual.get(0).getVeranstaltungId())
                .isEqualTo(expectedBE.getVeranstaltungId());
        assertThat(actual.get(0).getVereinId())
                .isEqualTo(expectedBE.getVereinId());
        assertThat(actual.get(0).getSortierung())
                .isEqualTo(expectedBE.getSortierung());


        // verify invocations
        verify(dsbMannschaftDAO).findAllByVereinsId(VEREIN_ID);
        verify(vereinDAO).findById(anyLong());
    }

    @Test
    public void findAllByVeranstaltungsId() {
        // prepare test data
        final DsbMannschaftBE expectedBE = getDsbMannschaftBE();
        final List<DsbMannschaftBE> expectedBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(dsbMannschaftDAO.findAllByVeranstaltungsId(VERANSTALTUNG_ID)).thenReturn(expectedBEList);
        when(dsbMannschaftDAO.findAllByVeranstaltungsId(VERANSTALTUNG_ID+1)).thenReturn(null);

        // call test method
        final List<DsbMannschaftDO> actual = underTest.findAllByVeranstaltungsId(VERANSTALTUNG_ID);

        // check if expected exception is thrown if id isn't contained
        assertThatThrownBy(()-> underTest.findAllByVeranstaltungsId(VERANSTALTUNG_ID+1))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(String.format("'%d'", VERANSTALTUNG_ID+1));

        // check if expected exception is thrown if id is negative
        assertThatThrownBy(()-> underTest.findAllByVeranstaltungsId(-1))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.INVALID_ARGUMENT_ERROR.getValue());

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getId())
                .isEqualTo(expectedBE.getId());
        assertThat(actual.get(0).getBenutzerId())
                .isEqualTo(expectedBE.getBenutzerId());
        assertThat(actual.get(0).getNummer())
                .isEqualTo(expectedBE.getNummer());
        assertThat(actual.get(0).getVeranstaltungId())
                .isEqualTo(expectedBE.getVeranstaltungId());
        assertThat(actual.get(0).getVereinId())
                .isEqualTo(expectedBE.getVereinId());
        assertThat(actual.get(0).getSortierung())
                .isEqualTo(expectedBE.getSortierung());


        // verify invocations
        verify(dsbMannschaftDAO).findAllByVeranstaltungsId(VERANSTALTUNG_ID);
        verify(vereinDAO).findById(anyLong());
    }

    @Test
    public void findById() {
        // prepare test data
        final DsbMannschaftBE expectedBE = getDsbMannschaftBE();

        // configure mocks
        when(dsbMannschaftDAO.findById(ID)).thenReturn(expectedBE);

        // call test method
        final DsbMannschaftDO actual = underTest.findById(ID);

        // check if expected exception is thrown if id isn't contained
        assertThatThrownBy(()-> underTest.findById(ID+1))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(String.format("'%d'", ID+1));

        // check if expected exception is thrown if id is negative
        assertThatThrownBy(()-> underTest.findById(-1))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.INVALID_ARGUMENT_ERROR.getValue());

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(expectedBE.getId());

        // verify invocations
        verify(dsbMannschaftDAO).findById(ID);
        verify(vereinDAO).findById(anyLong());
    }

    @Test
    public void create() {
        // prepare test data
        final DsbMannschaftDO input = getDsbMannschaftDO();

        final DsbMannschaftBE expectedBE = getDsbMannschaftBE();

        // configure mocks
        when(dsbMannschaftDAO.create(any(DsbMannschaftBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final DsbMannschaftDO actual = underTest.create(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(input.getId());

        // verify invocations
        verify(dsbMannschaftDAO).create(dsbMannschaftBEArgumentCaptor.capture(), anyLong());
        verify(vereinDAO).findById(anyLong());

        final DsbMannschaftBE persistedBE = dsbMannschaftBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getId())
                .isEqualTo(input.getId());
    }

    @Test
    public void create_with_mandatory_parameters() {
        // prepare test data
        final OffsetDateTime dateTime = OffsetDateTime.now();
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        final DsbMannschaftDO input = new DsbMannschaftDO(
                ID,
                MA_NAME,
                BENUTZER_ID,
                VEREIN_ID,
                NUMMER,
                VERANSTALTUNG_ID,
                SORTIERUNG,
                dateTime,
                USER,
                VERSION);

        final DsbMannschaftBE expectedBE = new DsbMannschaftBE();
        expectedBE.setId(ID);
        expectedBE.setBenutzerId(BENUTZER_ID);
        expectedBE.setNummer(NUMMER);
        expectedBE.setVereinId(VEREIN_ID);
        expectedBE.setVeranstaltungId(VERANSTALTUNG_ID);
        expectedBE.setSortierung(SORTIERUNG);
        expectedBE.setCreatedAtUtc(timestamp);
        expectedBE.setVersion(VERSION);



        // configure mocks
        when(dsbMannschaftDAO.create(any(DsbMannschaftBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final DsbMannschaftDO actual = underTest.create(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(input.getId());

        // verify invocations
        verify(dsbMannschaftDAO).create(dsbMannschaftBEArgumentCaptor.capture(), anyLong());
        verify(vereinDAO).findById(anyLong());

        final DsbMannschaftBE persistedBE = dsbMannschaftBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getId())
                .isEqualTo(input.getId());
    }

    @Test
    public void create_withoutInput_shouldThrowException() {
        // prepare test data
        DsbMannschaftDO tmpMannschaft = new DsbMannschaftDO(ID,-1);

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.create(null, USER))
                .withMessageContaining("must not be null")
                .withNoCause();

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.create(tmpMannschaft, -1))
                .withMessageContaining("must not be negative")
                .withNoCause();

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.create(tmpMannschaft, USER))
                .withMessageContaining("must not be negative")
                .withNoCause();
        tmpMannschaft.setVereinId(VEREIN_ID);

        tmpMannschaft.setNummer(-1);
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.create(tmpMannschaft, USER))
                .withMessageContaining("must not be negative")
                .withNoCause();
        tmpMannschaft.setNummer(NUMMER);

        tmpMannschaft.setBenutzerId(-1);
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.create(tmpMannschaft, USER))
                .withMessageContaining("must not be negative")
                .withNoCause();
        tmpMannschaft.setBenutzerId(BENUTZER_ID);

        tmpMannschaft.setVereinId(-1);
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.create(tmpMannschaft, USER))
                .withMessageContaining("must not be negative")
                .withNoCause();
        tmpMannschaft.setVereinId(VEREIN_ID);

        tmpMannschaft.setVeranstaltungId(-1);
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.create(tmpMannschaft, USER))
                .withMessageContaining("must not be negative")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(dsbMannschaftDAO);
        verifyZeroInteractions(vereinDAO);
    }

   /* @Test
    public void create_withoutNummer_shouldThrowException() {
        // prepare test data
        final DsbMannschaftDO input = getDsbMannschaftDO();
        final Long l = null;
        input.setId(ID);
        input.setNummer(l);

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.create(input, USER))
                .withMessageContaining("must not be null")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(dsbMannschaftDAO);
    }*/

    @Test
    public void update() {
        // prepare test data
        final DsbMannschaftDO input = getDsbMannschaftDO();

        final DsbMannschaftBE expectedBE = getDsbMannschaftBE();

        // configure mocks
        when(dsbMannschaftDAO.update(any(DsbMannschaftBE.class), anyLong())).thenReturn(expectedBE);
        when(dsbMannschaftDAO.findById(anyLong())).thenReturn(expectedBE);

        // call test method
        final DsbMannschaftDO actual = underTest.update(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(input.getId());
        assertThat(actual.getNummer())
                .isEqualTo(input.getNummer());

        // verify invocations
        verify(dsbMannschaftDAO).update(dsbMannschaftBEArgumentCaptor.capture(), anyLong());
        verify(vereinDAO).findById(anyLong());

        final DsbMannschaftBE persistedBE = dsbMannschaftBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getId())
                .isEqualTo(input.getId());
        assertThat(persistedBE.getVeranstaltungId())
                .isEqualTo(input.getVeranstaltungId());
        assertThat(persistedBE.getSortierung())
                .isEqualTo(input.getSortierung());
    }

    @Test
    public void update_withoutInput_shouldThrowException() {
        // prepare test data
        DsbMannschaftDO tmpMannschaft = new DsbMannschaftDO(ID,-1);

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.update(null, USER))
                .withMessageContaining("must not be null")
                .withNoCause();

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.update(tmpMannschaft, -1))
                .withMessageContaining("must not be negative")
                .withNoCause();

        tmpMannschaft.setId(-1L);
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.update(tmpMannschaft, USER))
                .withMessageContaining("must not be negative")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(dsbMannschaftDAO);
        verifyZeroInteractions(vereinDAO);
    }

    @Test
    public void update_checkSortierung() {
        // prepare test data
        //valid input but wrong output in case the sortierung has been set before
        final DsbMannschaftDO input = getDsbMannschaftDO();
        final long wrongValue = 0L;
        input.setSortierung(wrongValue);

        final DsbMannschaftBE expectedBE = getDsbMannschaftBE();

        // configure mocks
        when(dsbMannschaftDAO.update(any(DsbMannschaftBE.class), anyLong())).thenReturn(expectedBE);
        when(dsbMannschaftDAO.findById(anyLong())).thenReturn(expectedBE);

        // call test method
        final DsbMannschaftDO actual = underTest.update(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(input.getId());
        assertThat(actual.getNummer())
                .isEqualTo(input.getNummer());
        //Value from the database shouldn't be changed.
        assertThat(actual.getSortierung())
                .isNotEqualTo(wrongValue);
        assertThat(actual.getSortierung())
                .isEqualTo(expectedBE.getSortierung());

        // verify invocations
        verify(dsbMannschaftDAO).update(dsbMannschaftBEArgumentCaptor.capture(), anyLong());
        verify(vereinDAO).findById(anyLong());

        final DsbMannschaftBE persistedBE = dsbMannschaftBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getId())
                .isEqualTo(input.getId());
        assertThat(persistedBE.getVeranstaltungId())
                .isEqualTo(input.getVeranstaltungId());
        assertThat(persistedBE.getSortierung())
                .isEqualTo(input.getSortierung());
    }

    @Test
    public void delete() {
        // prepare test data
        final DsbMannschaftDO input = getDsbMannschaftDO();

        final DsbMannschaftBE expectedBE = getDsbMannschaftBE();

        // configure mocks

        // call test method
        underTest.delete(input, USER);

        // assert result

        // verify invocations
        verify(dsbMannschaftDAO).delete(dsbMannschaftBEArgumentCaptor.capture(), anyLong());
        verifyZeroInteractions(vereinDAO);

        final DsbMannschaftBE persistedBE = dsbMannschaftBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getId())
                .isEqualTo(input.getId());
    }

    @Test
    public void delete_withoutInput_shouldThrowException() {
        // prepare test data
        final DsbMannschaftDO input = getDsbMannschaftDO();

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.delete(null, USER))
                .withMessageContaining("must not be null")
                .withNoCause();

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.delete(input, -1))
                .withMessageContaining("must not be negative")
                .withNoCause();

        input.setId(-1L);
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.delete(input, USER))
                .withMessageContaining("must not be negative")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(dsbMannschaftDAO);
        verifyZeroInteractions(vereinDAO);
    }

    @Test
    public void updateSortierung(){
        // prepare test data
        final DsbMannschaftDO input = getSortierungsDO();

        final DsbMannschaftBE expectedBE = getDsbMannschaftBE();
        final DsbMannschaftBE expectedDatabaseBE = getDsbMannschaftBE();
        expectedDatabaseBE.setSortierung(DB_SORTIERUNG);

        // configure mocks
        when(dsbMannschaftDAO.update(any(DsbMannschaftBE.class), anyLong())).thenReturn(expectedBE);
        when(dsbMannschaftDAO.findById(anyLong())).thenReturn(expectedDatabaseBE);

        // call test method
        final DsbMannschaftDO actual = underTest.updateSortierung(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(input.getId());

        assertThat(actual.getSortierung())
                .isEqualTo(input.getSortierung());

        //persist Database Values
        assertThat(actual.getId())
                .isEqualTo(expectedDatabaseBE.getId());
        assertThat(actual.getNummer())
                .isEqualTo(expectedDatabaseBE.getNummer());
        assertThat(actual.getVereinId())
                .isEqualTo(expectedDatabaseBE.getVereinId());
        assertThat(actual.getVeranstaltungId())
                .isEqualTo(expectedDatabaseBE.getVeranstaltungId());
        assertThat(actual.getBenutzerId())
                .isEqualTo(expectedDatabaseBE.getBenutzerId());

        // verify invocations
        verify(dsbMannschaftDAO).update(dsbMannschaftBEArgumentCaptor.capture(), anyLong());
        verify(dsbMannschaftDAO).findById(anyLong());

        final DsbMannschaftBE persistedBE = dsbMannschaftBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getId())
                .isEqualTo(input.getId());
        assertThat(persistedBE.getNummer())
                .isEqualTo(input.getNummer());
        assertThat(persistedBE.getBenutzerId())
                .isEqualTo(input.getBenutzerId());
        assertThat(persistedBE.getVeranstaltungId())
                .isEqualTo(input.getVeranstaltungId());
        assertThat(persistedBE.getSortierung())
                .isEqualTo(input.getSortierung());
    }

    @Test
    public void updateSortierung_withWrongInput_shouldThrowException() {
        // prepare test data
        final DsbMannschaftDO input = getSortierungsDO();
        input.setSortierung(-1L);

        // configure mocks

        // call test method
        input.setVereinId(-1L);
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.updateSortierung(input, USER))
                .withMessageContaining("must not be null or negative")
                .withNoCause();
        input.setVereinId(getSortierungsDO().getVereinId());

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.updateSortierung(input, USER))
                .withMessageContaining("must not be null or negative")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(dsbMannschaftDAO);
    }

    @Test
    public void fillAllNames(){
        // prepare test data
        final DsbMannschaftBE expectedBE = getDsbMannschaftBE();
        final VereinBE expectedVerein = getVereinBE();
        final List<DsbMannschaftBE> expectedBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(dsbMannschaftDAO.findAll()).thenReturn(expectedBEList);
        when(vereinDAO.findById(VEREIN_ID)).thenReturn(expectedVerein);

        // call test method
        final List<DsbMannschaftDO> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getId())
                .isEqualTo(expectedBE.getId());
        assertThat(actual.get(0).getBenutzerId())
                .isEqualTo(expectedBE.getBenutzerId());
        assertThat(actual.get(0).getNummer())
                .isEqualTo(expectedBE.getNummer());
        assertThat(actual.get(0).getVeranstaltungId())
                .isEqualTo(expectedBE.getVeranstaltungId());
        assertThat(actual.get(0).getVereinId())
                .isEqualTo(expectedBE.getVereinId());
        assertThat(actual.get(0).getSortierung())
                .isEqualTo(expectedBE.getSortierung());
        assertThat(actual.get(0).getName())
                .isEqualTo(MA_NAME);


        // verify invocations
        verify(dsbMannschaftDAO).findAll();
        verify(vereinDAO).findById(VEREIN_ID);
    }

    @Test
    public void fillName_VereinNull(){
        // prepare test data
        final DsbMannschaftBE expectedBE = getDsbMannschaftBE();

        // configure mocks
        when(dsbMannschaftDAO.findById(ID)).thenReturn(expectedBE);
        when(vereinDAO.findById(anyLong())).thenReturn(null);

        // call test method
        final DsbMannschaftDO actual = underTest.findById(ID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(expectedBE.getId());
        assertThat(actual.getName())
                .isEqualTo(null);

        // verify invocations
        verify(dsbMannschaftDAO).findById(ID);
        verify(vereinDAO).findById(anyLong());
    }


    @Test
    public void getDAO(){
        // assert result
        assertThat(dsbMannschaftDAO).isEqualTo(underTest.getDAO());

        // verify invocations
        verifyZeroInteractions(dsbMannschaftDAO);
    }


    @Test
    public void copyMannschaftFromVeranstaltung_new(){
        // prepare test data
        DsbMannschaftBE mannschaft1 = getDsbMannschaftBE();
        final  List<DsbMannschaftBE> lastMannschaftList = new ArrayList<>();
        lastMannschaftList.add(mannschaft1);

        // configure mocks
        when(dsbMannschaftDAO.findAllByVeranstaltungsId(VERANSTALTUNG_ID)).thenReturn(lastMannschaftList);
        when(dsbMannschaftDAO.create(any(DsbMannschaftBE.class), anyLong())).thenReturn(null);

        //call test method
        final List<DsbMannschaftDO> actual = underTest.copyMannschaftFromVeranstaltung
                (VERANSTALTUNG_ID, CURRENT_VERANSTALTUNG_ID, ID);

        //asserting returns
        assertThat(actual).isNotNull();
        DsbMannschaftDO actualM = actual.get(0);
        assertThat(actualM.getVereinId()).isEqualTo(mannschaft1.getVereinId());
        assertThat(actualM.getId()).isEqualTo(mannschaft1.getId());

        // verify invocations
        verify(dsbMannschaftDAO).findAllByVeranstaltungsId(VERANSTALTUNG_ID);
        verify(dsbMannschaftDAO).create(dsbMannschaftBEArgumentCaptor.capture(), anyLong());
        verify(vereinDAO).findById(anyLong());
    }

}