package de.bogenliga.application.business.dsbmannschaft.impl.business;

import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.dsbmannschaft.impl.dao.DsbMannschaftDAO;
import de.bogenliga.application.business.dsbmannschaft.impl.entity.DsbMannschaftBE;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.business.MannschaftsmitgliedComponentImpl;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import org.assertj.core.api.Assertions;
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
import java.util.LinkedList;
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
    private static final long WETTKAMPF_ID =30L;
    private static final long CURRENT_VERANSTALTUNG_ID =2L;
    private static final long SORTIERUNG =1L;

    private static final String VERANSTALTUNGNAME ="TEST";
    private static final String VEREINNAME ="SSV REUTLINGEN";
    private static final String WETTKAMPFORT ="SSV REUTLINGEN";
    private static final String WETTKAMPTAG ="1";
    private static final long MANNSCHAFTNUMMER = 0;
    private static final long DB_SORTIERUNG =0L;
    
    private static final String VEREIN_NAME = "Testverein";
    private static final String MA_NAME = VEREIN_NAME+" "+ NUMMER;
    private static final long PLATZHALTER_ID = 99L;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private DsbMannschaftDAO dsbMannschaftDAO ;
    @Mock
    private VereinComponent vereinComponent;
    @Mock
    private MannschaftsmitgliedComponentImpl mannschaftsmitgliedComponent;
    @InjectMocks
    private DsbMannschaftComponentImpl underTest;
    @Captor
    private ArgumentCaptor<DsbMannschaftBE> dsbMannschaftBEArgumentCaptor;
    @Captor
    private ArgumentCaptor<MannschaftsmitgliedDO> mannschaftsmitgliedDOArgumentCaptor;



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
    public static DsbMannschaftBE getDsbMannschaftBEVERANDWETT() {
        return new DsbMannschaftBE(VERANSTALTUNGNAME, WETTKAMPTAG, WETTKAMPFORT, VEREINNAME, MANNSCHAFTNUMMER);
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
    public static DsbMannschaftDO getDsbMannschaftDOVERANDWETT() {
        return new DsbMannschaftDO(VERANSTALTUNGNAME, WETTKAMPTAG, WETTKAMPFORT, VEREINNAME, MANNSCHAFTNUMMER);
    }


    public static DsbMannschaftDO getPlatzhalterDO() {
        return new DsbMannschaftDO(
                ID,
                "Platzhalter",
                PLATZHALTER_ID,
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

    public static VereinDO getVereinDO(){
        VereinDO vereinDO = new VereinDO();
        vereinDO.setId(VEREIN_ID);
        vereinDO.setName(VEREIN_NAME);
        return vereinDO;
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
        verify(vereinComponent).findById(anyLong());
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
        verify(vereinComponent).findById(anyLong());
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
        verify(vereinComponent).findById(anyLong());
    }

    @Test
    public void findAllByWettkampfId() {
        // prepare test data
        final DsbMannschaftBE expectedBE = getDsbMannschaftBE();
        final List<DsbMannschaftBE> expectedBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(dsbMannschaftDAO.findAllByWettkampfId(WETTKAMPF_ID)).thenReturn(expectedBEList);
        when(dsbMannschaftDAO.findAllByWettkampfId(WETTKAMPF_ID+1)).thenReturn(null);

        // call test method
        final List<DsbMannschaftDO> actual = underTest.findAllByWettkampfId(WETTKAMPF_ID);

        // check if expected exception is thrown if id isn't contained
        assertThatThrownBy(()-> underTest.findAllByWettkampfId(WETTKAMPF_ID+1))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(String.format("'%d'", WETTKAMPF_ID+1));

        // check if expected exception is thrown if id is negative
        assertThatThrownBy(()-> underTest.findAllByWettkampfId(-1))
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
        verify(dsbMannschaftDAO).findAllByWettkampfId(WETTKAMPF_ID);
        verify(vereinComponent).findById(anyLong());
    }

    @Test
    public void findAllByName() {
        // prepare test data
        final DsbMannschaftBE expectedBE = getDsbMannschaftBE();
        final List<DsbMannschaftBE> expectedBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(dsbMannschaftDAO.findAllByName(MA_NAME)).thenReturn(expectedBEList);

        // call test method
        final List<DsbMannschaftDO> actual = underTest.findAllByName(MA_NAME);

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
        verify(dsbMannschaftDAO).findAllByName(MA_NAME);
        verify(vereinComponent).findById(anyLong());
    }

    @Test
    public void findAllByWarteschlange() {
        // prepare test data
        final DsbMannschaftBE expectedBE = getDsbMannschaftBE();
        final List<DsbMannschaftBE> expectedBEList = Collections.singletonList(expectedBE);
        expectedBE.setVeranstaltungId(null);

        // configure mocks
        when(dsbMannschaftDAO.findAllByWarteschlange()).thenReturn(expectedBEList);

        // call test method
        final List<DsbMannschaftDO> actual = underTest.findAllByWarteschlange();

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
        verify(dsbMannschaftDAO).findAllByWarteschlange();
        verify(vereinComponent).findById(anyLong());
    }
    @Test
    public void findVeranstaltungAndWettkampfById() {
        // prepare test data
        final DsbMannschaftBE expectedBE = getDsbMannschaftBEVERANDWETT();
        final List<DsbMannschaftBE> expectedBEList = Collections.singletonList(expectedBE);
        expectedBE.setVeranstaltungId(null);

        // configure mocks
        when(dsbMannschaftDAO.findVeranstaltungAndWettkampfById(VEREIN_ID)).thenReturn(expectedBEList);

        // call test method
        final List<DsbMannschaftDO> actual = underTest.findVeranstaltungAndWettkampfByID(VEREIN_ID);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        Assertions.assertThat(actual.get(0).getMannschaft_nummer())
                .isEqualTo(expectedBE.getMannschaftNummer());
        Assertions.assertThat(actual.get(0).getVerein_name())
                .isEqualTo(expectedBE.getVereinName());
        Assertions.assertThat(actual.get(0).getWettkampf_ortsname())
                .isEqualTo(expectedBE.getWettkampfOrtsname());
        Assertions.assertThat(actual.get(0).getWettkampfTag())
                .isEqualTo(expectedBE.getWettkampfTag());
        Assertions.assertThat(actual.get(0).getVeranstaltung_name())
                .isEqualTo(expectedBE.getVeranstaltungName());

        // verify invocations
        verify(dsbMannschaftDAO).findVeranstaltungAndWettkampfById(VEREIN_ID);
    }
    private static final long VALID_ID = 1L;
    private static final long INVALID_ID = -1L;
    private static final String PRECONDITION_MSG_WETTKAMPF_ID = "Wettkampf ID must not be negative";
    private static final String EXCEPTION_NO_RESULTS = "ENTITY_NOT_FOUND_ERROR: No result for ID '1'";

    @Test
    public void findEverythingById_shouldThrowException_whenResultIsNull() {
        // Arrange
        when(dsbMannschaftDAO.findVeranstaltungAndWettkampfById(VALID_ID)).thenReturn(null);

        // Act & Assert
        try {
            underTest.findVeranstaltungAndWettkampfByID(VALID_ID);
        } catch (BusinessException e) {
            assertThat(e.getMessage()).isEqualTo(String.format(EXCEPTION_NO_RESULTS, VALID_ID));
        }
    }

    @Test
    public void findEverythingById_shouldReturnEmptyList_whenResultIsEmpty() {
        // Arrange
        when(dsbMannschaftDAO.findVeranstaltungAndWettkampfById(VALID_ID)).thenReturn(Collections.emptyList());

        // Act
        List<DsbMannschaftDO> actual = underTest.findVeranstaltungAndWettkampfByID(VALID_ID);

        // Assert
        assertThat(actual).isNotNull().isEmpty();
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
        verify(vereinComponent).findById(anyLong());
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
        verify(vereinComponent).findById(anyLong());

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
        verify(vereinComponent).findById(anyLong());

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

        // assert result

        // verify invocations
        verifyZeroInteractions(dsbMannschaftDAO);
        verifyZeroInteractions(vereinComponent);
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
        verify(vereinComponent).findById(anyLong());

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
        verifyZeroInteractions(vereinComponent);
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
        verify(vereinComponent).findById(anyLong());

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
        verifyZeroInteractions(vereinComponent);

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
                .isThrownBy(() -> underTest.delete(input, -1L))
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
        verifyZeroInteractions(vereinComponent);
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
        final VereinDO expectedVerein = getVereinDO();
        final List<DsbMannschaftBE> expectedBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(dsbMannschaftDAO.findAll()).thenReturn(expectedBEList);
        when(vereinComponent.findById(VEREIN_ID)).thenReturn(expectedVerein);

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
        verify(vereinComponent).findById(VEREIN_ID);
    }
    @Test
    public void findEverything(){
        // prepare test data
        final DsbMannschaftBE expectedBE = getDsbMannschaftBEVERANDWETT();
        final List<DsbMannschaftBE> expectedBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(dsbMannschaftDAO.findVeranstaltungAndWettkampfById(VEREIN_ID)).thenReturn(expectedBEList);

        // call test method
        final List<DsbMannschaftDO> actual = underTest.findVeranstaltungAndWettkampfByID(VEREIN_ID);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        Assertions.assertThat(actual.get(0).getMannschaft_nummer())
                .isEqualTo(expectedBE.getMannschaftNummer());
        Assertions.assertThat(actual.get(0).getVerein_name())
                .isEqualTo(expectedBE.getVereinName());
        Assertions.assertThat(actual.get(0).getWettkampf_ortsname())
                .isEqualTo(expectedBE.getWettkampfOrtsname());
        Assertions.assertThat(actual.get(0).getWettkampfTag())
                .isEqualTo(expectedBE.getWettkampfTag());
        Assertions.assertThat(actual.get(0).getVeranstaltung_name())
                .isEqualTo(expectedBE.getVeranstaltungName());


        // verify invocations
        verify(dsbMannschaftDAO).findVeranstaltungAndWettkampfById(VEREIN_ID);
    }
    @Test
    public void fillName_VereinNull(){
        // prepare test data
        final DsbMannschaftBE expectedBE = getDsbMannschaftBE();

        // configure mocks
        when(dsbMannschaftDAO.findById(ID)).thenReturn(expectedBE);
        when(vereinComponent.findById(anyLong())).thenReturn(null);

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
        verify(vereinComponent).findById(anyLong());
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

        MannschaftsmitgliedDO mannschaftsmitgliedDO = new MannschaftsmitgliedDO(1L);
        final List<MannschaftsmitgliedDO> mitglieder = new ArrayList<>();
        mitglieder.add(mannschaftsmitgliedDO);

        // configure mocks
        when(dsbMannschaftDAO.findAllByVeranstaltungsId(VERANSTALTUNG_ID)).thenReturn(lastMannschaftList);
        when(dsbMannschaftDAO.create(any(DsbMannschaftBE.class), anyLong())).thenReturn(mannschaft1);
        when(mannschaftsmitgliedComponent.findByTeamId(anyLong())).thenReturn(mitglieder);
        when(mannschaftsmitgliedComponent.create(any(), anyLong())).thenReturn(null);

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
        verify(vereinComponent).findById(anyLong());
        verify(mannschaftsmitgliedComponent).findByTeamId(anyLong());
        verify(mannschaftsmitgliedComponent).create(any(), anyLong());
    }

    @Test
    public void copyMitgliederFromMannschaftTest(){

        // prepare test data
        final long oldMannschaftId = 0;
        final long newMannschaftId = 1;
        List<MannschaftsmitgliedDO> alteMitglieder = new LinkedList<>();
        alteMitglieder.add(new MannschaftsmitgliedDO(1L));

        // configure mocks
        when(mannschaftsmitgliedComponent.findByTeamId(any())).thenReturn(alteMitglieder);

        // call test method
        underTest.copyMitgliederFromMannschaft(oldMannschaftId, newMannschaftId, 0L);

        // verify result
        verify(mannschaftsmitgliedComponent).create(mannschaftsmitgliedDOArgumentCaptor.capture(), any());
        assertThat(mannschaftsmitgliedDOArgumentCaptor.getValue().getMannschaftId()).isEqualTo(newMannschaftId);
    }
    private DsbMannschaftBE dsbMannschaft;
    @Test
    public void testGetAndSetId() {
        dsbMannschaft = new DsbMannschaftBE();
        Long id = 1L;
        dsbMannschaft.setId(id);
        assertThat(dsbMannschaft.getId()).isEqualTo(id);
    }

    @Test
    public void testGetAndSetVereinId() {
        dsbMannschaft = new DsbMannschaftBE();
        Long vereinId = 10L;
        dsbMannschaft.setVereinId(vereinId);
        assertThat(dsbMannschaft.getVereinId()).isEqualTo(vereinId);
    }

    @Test
    public void testGetAndSetNummer() {
        dsbMannschaft = new DsbMannschaftBE();
        Long nummer = 20L;
        dsbMannschaft.setNummer(nummer);
        assertThat(dsbMannschaft.getNummer()).isEqualTo(nummer);
    }

    @Test
    public void testGetAndSetBenutzerId() {
        dsbMannschaft = new DsbMannschaftBE();
        Long benutzerId = 30L;
        dsbMannschaft.setBenutzerId(benutzerId);
        assertThat(dsbMannschaft.getBenutzerId()).isEqualTo(benutzerId);
    }

    @Test
    public void testGetAndSetVeranstaltungId() {
        dsbMannschaft = new DsbMannschaftBE();
        Long veranstaltungId = 40L;
        dsbMannschaft.setVeranstaltungId(veranstaltungId);
        assertThat(dsbMannschaft.getVeranstaltungId()).isEqualTo(veranstaltungId);
    }

    @Test
    public void testGetAndSetSortierung() {
        dsbMannschaft = new DsbMannschaftBE();
        Long sortierung = 50L;
        dsbMannschaft.setSortierung(sortierung);
        assertThat(dsbMannschaft.getSortierung()).isEqualTo(sortierung);
    }

    @Test
    public void testGetAndSetVeranstaltungName() {
        dsbMannschaft = new DsbMannschaftBE();
        String veranstaltungName = "Meisterschaft";
        dsbMannschaft.setVeranstaltungName(veranstaltungName);
        assertThat(dsbMannschaft.getVeranstaltungName()).isEqualTo(veranstaltungName);
    }

    @Test
    public void testGetAndSetWettkampfTag() {
        dsbMannschaft = new DsbMannschaftBE();
        String wettkampfTag = "2024-07-22";
        dsbMannschaft.setWettkampfTag(wettkampfTag);
        assertThat(dsbMannschaft.getWettkampfTag()).isEqualTo(wettkampfTag);
    }

    @Test
    public void testGetAndSetWettkampfOrtsname() {
        dsbMannschaft = new DsbMannschaftBE();
        String wettkampfOrtsname = "Berlin";
        dsbMannschaft.setWettkampfOrtsname(wettkampfOrtsname);
        assertThat(dsbMannschaft.getWettkampfOrtsname()).isEqualTo(wettkampfOrtsname);
    }

    @Test
    public void testGetAndSetVereinName() {
        dsbMannschaft = new DsbMannschaftBE();
        String vereinName = "Sportverein Berlin";
        dsbMannschaft.setVereinName(vereinName);
        assertThat(dsbMannschaft.getVereinName()).isEqualTo(vereinName);
    }

    @Test
    public void testGetAndSetMannschaftNummer() {
        dsbMannschaft = new DsbMannschaftBE();
        Long mannschaftNummer = 12345L;
        dsbMannschaft.setMannschaftNummer(mannschaftNummer);
        assertThat(dsbMannschaft.getMannschaftNummer()).isEqualTo(mannschaftNummer);
    }
    private DsbMannschaftDO dsbMannschaftDO;
    @Test
    public void testGetAndSetIdDO() {
        dsbMannschaftDO= getDsbMannschaftDOVERANDWETT();
        Long id = 1L;
        dsbMannschaftDO.setId(id);
        assertThat(dsbMannschaftDO.getId()).isEqualTo(id);
    }

    @Test
    public void testGetAndSetVereinIdDO() {
        dsbMannschaftDO= new DsbMannschaftDO();
        Long vereinId = 10L;
        dsbMannschaftDO.setVereinId(vereinId);
        assertThat(dsbMannschaftDO.getVereinId()).isEqualTo(vereinId);
    }

    @Test
    public void testGetAndSetNummerDO() {
        dsbMannschaftDO= new DsbMannschaftDO();
        Long nummer = 20L;
        dsbMannschaftDO.setNummer(nummer);
        assertThat(dsbMannschaftDO.getNummer()).isEqualTo(nummer);
    }

    @Test
    public void testGetAndSetBenutzerIdDO() {
        dsbMannschaftDO= new DsbMannschaftDO();
        Long benutzerId = 30L;
        dsbMannschaftDO.setBenutzerId(benutzerId);
        assertThat(dsbMannschaftDO.getBenutzerId()).isEqualTo(benutzerId);
    }

    @Test
    public void testGetAndSetVeranstaltungIdDO() {
        dsbMannschaftDO= new DsbMannschaftDO();
        Long veranstaltungId = 40L;
        dsbMannschaftDO.setVeranstaltungId(veranstaltungId);
        assertThat(dsbMannschaftDO.getVeranstaltungId()).isEqualTo(veranstaltungId);
    }

    @Test
    public void testGetAndSetSortierungDO() {
        dsbMannschaftDO= new DsbMannschaftDO();
        Long sortierung = 50L;
        dsbMannschaftDO.setSortierung(sortierung);
        assertThat(dsbMannschaftDO.getSortierung()).isEqualTo(sortierung);
    }

    @Test
    public void testGetAndSetVeranstaltungNameDO() {
        dsbMannschaftDO= new DsbMannschaftDO();
        String veranstaltungName = "Meisterschaft";
        dsbMannschaftDO.setVeranstaltung_name(veranstaltungName);
        assertThat(dsbMannschaftDO.getVeranstaltung_name()).isEqualTo(veranstaltungName);
    }

    @Test
    public void testGetAndSetWettkampfTagDO() {
        dsbMannschaftDO= new DsbMannschaftDO();
        String wettkampfTag = "2024-07-22";
        dsbMannschaftDO.setWettkampfTag(wettkampfTag);
        assertThat(dsbMannschaftDO.getWettkampfTag()).isEqualTo(wettkampfTag);
    }

    @Test
    public void testGetAndSetWettkampfOrtsnameDO() {
        dsbMannschaftDO= new DsbMannschaftDO();
        String wettkampfOrtsname = "Berlin";
        dsbMannschaftDO.setWettkampf_ortsname(wettkampfOrtsname);
        assertThat(dsbMannschaftDO.getWettkampf_ortsname()).isEqualTo(wettkampfOrtsname);
    }

    @Test
    public void testGetAndSetVereinNameDO() {
        dsbMannschaftDO= new DsbMannschaftDO();
        String vereinName = "Sportverein Berlin";
        dsbMannschaftDO.setVerein_name(vereinName);
        assertThat(dsbMannschaftDO.getVerein_name()).isEqualTo(vereinName);
    }

    @Test
    public void testGetAndSetMannschaftNummerDO() {
        dsbMannschaftDO= new DsbMannschaftDO();
        Long mannschaftNummer = 12345L;
        dsbMannschaftDO.setMannschaft_nummer(mannschaftNummer);
        assertThat(dsbMannschaftDO.getMannschaft_nummer()).isEqualTo(mannschaftNummer);
    }
}