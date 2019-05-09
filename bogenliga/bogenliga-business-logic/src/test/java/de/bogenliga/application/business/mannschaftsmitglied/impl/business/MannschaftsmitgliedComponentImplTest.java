package de.bogenliga.application.business.mannschaftsmitglied.impl.business;

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
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.dao.MannschaftsmitgliedDAO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.entity.MannschaftsmitgliedBE;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * @author Philip Dengler,
 */
public class MannschaftsmitgliedComponentImplTest {


    private static final Long USER = 0L;
    private static final Long VERSION = 0L;


    private static final Long MM_ID = 1001L;
    private static final Long MANNSCHAFTSID = 1111L;
    private static final Long DSB_MITGLIED_ID = 2222L;
    private static final Integer DSB_MITGLIED_EINGESETZT = 1;
    private static final String DSB_MITGLIED_VORNSME = "Mario";
    private static final String DSB_MItglied_Nachname = "Gomez";


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private MannschaftsmitgliedDAO mannschaftsmitgliedDAO;
    @InjectMocks
    private MannschaftsmitgliedComponentImpl underTest;
    @Captor
    private ArgumentCaptor<MannschaftsmitgliedBE> mannschaftsmitgliedBEArgumentCaptor;


    /***
     * Utility methods for creating business entities/data objects.
     * Also used by other test classes.
     */
    public static MannschaftsmitgliedBE getMannschatfsmitgliedBE() {
        final MannschaftsmitgliedBE expectedBE = new MannschaftsmitgliedBE();
        expectedBE.setMannschaftId(MANNSCHAFTSID);
        expectedBE.setDsbMitgliedId(DSB_MITGLIED_ID);
        expectedBE.setDsbMitgliedEingesetzt(DSB_MITGLIED_EINGESETZT);

        return expectedBE;

    }


    public static MannschaftsmitgliedDO getMannschatfsmitgliedDO() {
        return new MannschaftsmitgliedDO(
                MM_ID,
                MANNSCHAFTSID,
                DSB_MITGLIED_ID,
                DSB_MITGLIED_EINGESETZT,
                DSB_MITGLIED_VORNSME,
                DSB_MItglied_Nachname);
    }


    @Test
    public void findAll() {
        // prepare test data
        final MannschaftsmitgliedBE expectedBE = getMannschatfsmitgliedBE();
        final List<MannschaftsmitgliedBE> expectedBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(mannschaftsmitgliedDAO.findAll()).thenReturn(expectedBEList);


        // call test method
        final List<MannschaftsmitgliedDO> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();
        assertThat(actual.get(0).getMannschaftId())
                .isEqualTo(expectedBE.getMannschaftId());
        assertThat(actual.get(0).getDsbMitgliedId())
                .isEqualTo(expectedBE.getDsbMitgliedId());
        assertThat(actual.get(0).getDsbMitgliedEingesetzt())
                .isEqualTo(expectedBE.getDsbMitgliedEingesetzt());
    }


    @Test
    public void findAllSchuetzeInTeam() {

        final MannschaftsmitgliedBE expectedBE = getMannschatfsmitgliedBE();
        final List<MannschaftsmitgliedBE> expectedBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(mannschaftsmitgliedDAO.findAllSchuetzeInTeam(MANNSCHAFTSID)).thenReturn(expectedBEList);

        // call test method
        final List<MannschaftsmitgliedDO> actual = underTest.findAllSchuetzeInTeam(MANNSCHAFTSID);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        assertThat(actual.get(0).getMannschaftId()).isEqualTo(expectedBE.getMannschaftId());
        assertThat(actual.get(0).getDsbMitgliedId()).isEqualTo(expectedBE.getDsbMitgliedId());
        assertThat(actual.get(0).getDsbMitgliedEingesetzt()).isEqualTo(expectedBE.getDsbMitgliedEingesetzt());


    }


    @Test
    public void findByMemberAndTeamId() {

        final MannschaftsmitgliedBE expectedBE = getMannschatfsmitgliedBE();


        // configure mocks
        when(mannschaftsmitgliedDAO.findByMemberAndTeamId(MANNSCHAFTSID, DSB_MITGLIED_ID)).thenReturn(expectedBE);
        final MannschaftsmitgliedDO actual = underTest.findByMemberAndTeamId(MANNSCHAFTSID, DSB_MITGLIED_ID);

        assertThat(actual).isNotNull();
        assertThat(actual.getMannschaftId()).isEqualTo(expectedBE.getMannschaftId());
        assertThat(actual.getDsbMitgliedId()).isEqualTo(expectedBE.getDsbMitgliedId());

    }


    @Test
    public void findByTeamId() {
        final MannschaftsmitgliedBE expectedBE = getMannschatfsmitgliedBE();
        final List<MannschaftsmitgliedBE> expectedBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(mannschaftsmitgliedDAO.findByTeamId(MANNSCHAFTSID)).thenReturn(expectedBEList);


        // call test method
        final List<MannschaftsmitgliedDO> actual = underTest.findByTeamId(MANNSCHAFTSID);

        assertThat(actual).isNotNull();
        assertThat(actual.get(0).getMannschaftId()).isEqualTo(expectedBE.getMannschaftId());

    }


    @Test
    public void checkExistingSchuetze() {

        final MannschaftsmitgliedBE expectedBE = getMannschatfsmitgliedBE();
        // configure mocks
        when(mannschaftsmitgliedDAO.findByMemberAndTeamId(MANNSCHAFTSID, DSB_MITGLIED_ID)).thenReturn(expectedBE);
        final boolean actual = underTest.checkExistingSchuetze(MANNSCHAFTSID, DSB_MITGLIED_ID);
        assertThat(actual).isTrue();
    }


    @Test
    public void create() {

        final MannschaftsmitgliedDO input = getMannschatfsmitgliedDO();

        final MannschaftsmitgliedBE expectedBE = getMannschatfsmitgliedBE();

        // configure mocks
        when(mannschaftsmitgliedDAO.create(any(MannschaftsmitgliedBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final MannschaftsmitgliedDO actual = underTest.create(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getMannschaftId())
                .isEqualTo(input.getMannschaftId());

        // verify invocations
        verify(mannschaftsmitgliedDAO).create(mannschaftsmitgliedBEArgumentCaptor.capture(), anyLong());

        final MannschaftsmitgliedBE persistedBE = mannschaftsmitgliedBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getMannschaftId())
                .isEqualTo(input.getMannschaftId());

    }


    @Test
    public void create_with_mandatory_parameters() {

        final OffsetDateTime dateTime = OffsetDateTime.now();
        final MannschaftsmitgliedDO input = new MannschaftsmitgliedDO(
                MM_ID,
                MANNSCHAFTSID,
                DSB_MITGLIED_ID,
                dateTime,
                USER,
                VERSION);

        final MannschaftsmitgliedBE expectedBE = new MannschaftsmitgliedBE();
        expectedBE.setMannschaftId(MANNSCHAFTSID);
        expectedBE.setDsbMitgliedId(DSB_MITGLIED_ID);
        expectedBE.setDsbMitgliedEingesetzt(DSB_MITGLIED_EINGESETZT);


        when(mannschaftsmitgliedDAO.create(any(MannschaftsmitgliedBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final MannschaftsmitgliedDO actual = underTest.create(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getMannschaftId())
                .isEqualTo(input.getMannschaftId());

        // verify invocations
        verify(mannschaftsmitgliedDAO).create(mannschaftsmitgliedBEArgumentCaptor.capture(), anyLong());

        final MannschaftsmitgliedBE persistedBE = mannschaftsmitgliedBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getMannschaftId())
                .isEqualTo(input.getMannschaftId());
    }


    @Test
    public void create_withoutInput_shouldThrowException() {
        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.create(null, USER))
                .withMessageContaining("must not be null")
                .withNoCause();

        // verify invocations
        verifyZeroInteractions(mannschaftsmitgliedDAO);
    }


    @Test
    public void update() {

        final MannschaftsmitgliedDO input = getMannschatfsmitgliedDO();

        final MannschaftsmitgliedBE expectedBE = getMannschatfsmitgliedBE();

        // configure mocks
        when(mannschaftsmitgliedDAO.update(any(MannschaftsmitgliedBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final MannschaftsmitgliedDO actual = underTest.update(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getMannschaftId())
                .isEqualTo(input.getMannschaftId());
        assertThat(actual.getDsbMitgliedId())
                .isEqualTo(input.getDsbMitgliedId());
    }


    @Test
    public void update_withoutInput_shouldThrowException() {
        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.update(null, USER))
                .withMessageContaining("must not be null")
                .withNoCause();

        // verify invocations
        verifyZeroInteractions(mannschaftsmitgliedDAO);
    }


    @Test
    public void delete() {
        // prepare test data
        final MannschaftsmitgliedDO input = getMannschatfsmitgliedDO();

        // call test method
        underTest.delete(input, USER);

        // verify invocations
        verify(mannschaftsmitgliedDAO).delete(mannschaftsmitgliedBEArgumentCaptor.capture(), anyLong());

        final MannschaftsmitgliedBE persistedBE = mannschaftsmitgliedBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getMannschaftId())
                .isEqualTo(input.getMannschaftId());
    }


    @Test
    public void delete_withoutInput_shouldThrowException() {
        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.delete(null, USER))
                .withMessageContaining("must not be null")
                .withNoCause();

        // verify invocations
        verifyZeroInteractions(mannschaftsmitgliedDAO);
    }
}
