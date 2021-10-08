package de.bogenliga.application.business.regionen.impl.business;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.liga.api.LigaComponent;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.business.lizenz.api.LizenzComponent;
import de.bogenliga.application.business.lizenz.api.types.LizenzDO;
import de.bogenliga.application.business.lizenz.impl.dao.LizenzDAO;
import de.bogenliga.application.business.lizenz.impl.entity.LizenzBE;
import de.bogenliga.application.business.regionen.api.types.RegionenDO;
import de.bogenliga.application.business.regionen.impl.dao.RegionenDAO;
import de.bogenliga.application.business.regionen.impl.entity.RegionenBE;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

public class RegionenComponentImplTest {

    private static final long REGION_ID = 10;
    private static final long REGION_UEBERGEORDNET = 3;
    private static final String REGION_UEBERGEORDNETASNAME = "Region3";
    private static final String REGION_NAME = "Test";
    private static final String REGION_KUERZEL = "T";
    private static final String REGION_TYP = "TEST";
    private static final OffsetDateTime offsetDateTime = null;
    private static final long USER = 1;
    private static final long VERSION = 2;
    private static final List<VereinDO> EMPTYVEREINLIST = new LinkedList<>();
    private static final List<LigaDO> EMPTYLIGALIST = new LinkedList<>();
    private static final List<LizenzDO> EMPTYLIZENZLIST = new LinkedList<>();



    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private RegionenDAO regionenDAO;
    @Mock
    private VereinComponent vereinComponent;
    @Mock
    private LigaComponent ligaComponent;
    @Mock
    private LizenzComponent lizenzComponent;
    @InjectMocks
    private RegionenComponentImpl underTest;
    @Captor
    private ArgumentCaptor<RegionenBE> regionBEArgumentCaptor;


    public static RegionenBE getRegionenBE() {
        final RegionenBE expectedBE = new RegionenBE();
        expectedBE.setRegionName(REGION_NAME);
        expectedBE.setRegionKuerzel(REGION_KUERZEL);
        expectedBE.setRegionUebergeordnet(REGION_UEBERGEORDNET);
        expectedBE.setRegionId(REGION_ID);
        expectedBE.setRegionTyp(REGION_TYP);

        return expectedBE;
    }


    public static RegionenDO getRegionenDO() {
        return new RegionenDO(REGION_ID,
                REGION_NAME,
                REGION_KUERZEL,
                REGION_TYP,
                REGION_UEBERGEORDNET,
                REGION_UEBERGEORDNETASNAME,
                offsetDateTime,
                USER,
                offsetDateTime,
                USER,
                VERSION);
    }


    @Test
    public void findAll() {
        // prepare test data
        final RegionenBE expectedBE = getRegionenBE();
        final List<RegionenBE> expectedBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(regionenDAO.findAll()).thenReturn(expectedBEList);

        // call test method
        final List<RegionenDO> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getId())
                .isEqualTo(expectedBE.getRegionId());
        assertThat(actual.get(0).getRegionKuerzel())
                .isEqualTo(expectedBE.getRegionKuerzel());
        assertThat(actual.get(0).getRegionName())
                .isEqualTo(expectedBE.getRegionName());
        assertThat(actual.get(0).getRegionTyp())
                .isEqualTo(expectedBE.getRegionTyp());
        assertThat(actual.get(0).getRegionUebergeordnet())
                .isEqualTo(expectedBE.getRegionUebergeordnet());

        // verify invocations
        verify(regionenDAO, times(2)).findAll();
    }


    @Test
    public void findAllByType() {
        // prepare test data
        final RegionenBE expectedBE = getRegionenBE();
        final List<RegionenBE> expectedBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(regionenDAO.findAllByType(REGION_TYP)).thenReturn(expectedBEList);

        // call test method
        final List<RegionenDO> actual = underTest.findAllByType(REGION_TYP);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getId())
                .isEqualTo(expectedBE.getRegionId());
        assertThat(actual.get(0).getRegionKuerzel())
                .isEqualTo(expectedBE.getRegionKuerzel());
        assertThat(actual.get(0).getRegionName())
                .isEqualTo(expectedBE.getRegionName());
        assertThat(actual.get(0).getRegionTyp())
                .isEqualTo(expectedBE.getRegionTyp());
        assertThat(actual.get(0).getRegionUebergeordnet())
                .isEqualTo(expectedBE.getRegionUebergeordnet());

        // verify invocations
        verify(regionenDAO).findAllByType(REGION_TYP);
    }


    @Test
    public void findById_whenEverythingIsSet() {
        // prepare test data
        final RegionenBE expectedBE = getRegionenBE();

        // configure mocks
        when(regionenDAO.findById(REGION_ID)).thenReturn(expectedBE);

        // call test method
        final RegionenDO actual = underTest.findById(REGION_ID);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(expectedBE.getRegionId());
        assertThat(actual.getRegionName()).isEqualTo(expectedBE.getRegionName());
        assertThat(actual.getRegionKuerzel()).isEqualTo(expectedBE.getRegionKuerzel());
        assertThat(actual.getRegionUebergeordnet()).isEqualTo(expectedBE.getRegionUebergeordnet());

        // verify invocations
        verify(regionenDAO).findById(REGION_ID);
    }


    @Test
    public void findById_whenAttributesAreNull() {
        // prepare test data
        final RegionenBE expectedBE = getRegionenBE();

        expectedBE.setRegionName(null);
        expectedBE.setRegionKuerzel(null);

        // configure mocks
        when(regionenDAO.findById(anyLong())).thenReturn(expectedBE);

        // call test method
        final RegionenDO actual = underTest.findById(REGION_ID);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(expectedBE.getRegionId());
        assertThat(actual.getRegionName()).isEqualTo(expectedBE.getRegionName());
        assertThat(actual.getRegionName()).isEqualTo(null);
        assertThat(actual.getRegionKuerzel()).isEqualTo(expectedBE.getRegionKuerzel());
        assertThat(actual.getRegionKuerzel()).isEqualTo(null);
        assertThat(actual.getRegionTyp()).isEqualTo(expectedBE.getRegionTyp());
        assertThat(actual.getRegionUebergeordnet()).isEqualTo(expectedBE.getRegionUebergeordnet());

        // verify invocation
        verify(regionenDAO).findById(REGION_ID);
    }


    @Test
    public void create__whenEverythingIsSet() {
        // prepare test data
        final RegionenDO input = getRegionenDO();
        final RegionenBE expectedBE = getRegionenBE();

        // configure mocks
        when(regionenDAO.create(any(RegionenBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final RegionenDO actual = underTest.create(input, USER);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(input.getId());
        assertThat(actual.getRegionName()).isEqualTo(input.getRegionName());
        assertThat(actual.getRegionKuerzel()).isEqualTo(input.getRegionKuerzel());
        assertThat(actual.getRegionTyp()).isEqualTo(input.getRegionTyp());
        assertThat(actual.getRegionUebergeordnet()).isEqualTo(input.getRegionUebergeordnet());

        // verify invocations
        verify(regionenDAO).create(regionBEArgumentCaptor.capture(), anyLong());
        final RegionenBE persistedBE = regionBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getRegionId()).isEqualTo(input.getId());
        assertThat(persistedBE.getRegionName()).isEqualTo(expectedBE.getRegionName());
        assertThat(persistedBE.getRegionKuerzel()).isEqualTo(expectedBE.getRegionKuerzel());
        assertThat(persistedBE.getRegionTyp()).isEqualTo(expectedBE.getRegionTyp());
        assertThat(persistedBE.getRegionUebergeordnet()).isEqualTo(expectedBE.getRegionUebergeordnet());

        // test mapping of do
        assertThat(actual.getRegionName()).isEqualTo(expectedBE.getRegionName());
        assertThat(actual.getRegionKuerzel()).isEqualTo(expectedBE.getRegionKuerzel());
        assertThat(actual.getRegionTyp()).isEqualTo(expectedBE.getRegionTyp());
    }


    @Test
    public void create_whenArgumentsAreNull() {
        // prepare test data
        final RegionenDO input = getRegionenDO();
        final RegionenBE expectedBE = getRegionenBE();

        expectedBE.setRegionKuerzel(null);
        input.setRegionKuerzel(null);

        // configure mocks
        when(regionenDAO.create(any(RegionenBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final RegionenDO actual = underTest.create(input, USER);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(input.getId());
        assertThat(actual.getRegionName()).isEqualTo(input.getRegionName());
        assertThat(actual.getRegionKuerzel()).isEqualTo(input.getRegionKuerzel());

        // verify invocations
        verify(regionenDAO).create(regionBEArgumentCaptor.capture(), anyLong());
        final RegionenBE persistedBE = regionBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getRegionId()).isEqualTo(input.getId());
        assertThat(persistedBE.getRegionName()).isEqualTo(expectedBE.getRegionName());
        assertThat(persistedBE.getRegionKuerzel()).isEqualTo(null);
    }


    @Test
    public void create_withoutRegionname_shouldThrowException() {
        // prepare test data
        final RegionenDO input = getRegionenDO();
        input.setId(REGION_ID);
        input.setRegionName(null);

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.create(input, USER))
                .withMessageContaining("must not be null")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(regionenDAO);
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
        verifyZeroInteractions(regionenDAO);
    }


    @Test
    public void update() {
        // prepare test data
        final RegionenDO input = getRegionenDO();
        final RegionenBE expectedBE = getRegionenBE();

        // configure mocks
        when(regionenDAO.update(any(RegionenBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final RegionenDO actual = underTest.update(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId()).isEqualTo(input.getId());
        assertThat(actual.getRegionName()).isEqualTo(input.getRegionName());

        // verify invocations
        verify(regionenDAO).update(regionBEArgumentCaptor.capture(), anyLong());

        final RegionenBE persistedBE = regionBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getRegionId()).isEqualTo(input.getId());
        assertThat(persistedBE.getRegionName()).isEqualTo(input.getRegionName());
    }


    @Test
    public void delete() {
        // prepare test data
        final RegionenDO input = getRegionenDO();

        // configure mocks
        when(vereinComponent.findAll()).thenReturn(EMPTYVEREINLIST);
        when(ligaComponent.findAll()).thenReturn(EMPTYLIGALIST);
        when(lizenzComponent.findAll()).thenReturn(EMPTYLIZENZLIST);

        // call test method
        underTest.delete(input, USER);

        // assert result

        // verify invocations
        verify(regionenDAO).delete(regionBEArgumentCaptor.capture(), anyLong());

        final RegionenBE persistedBE = regionBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();
        assertThat(persistedBE.getRegionId()).isEqualTo(input.getId());
        assertThat(persistedBE.getRegionName()).isEqualTo(input.getRegionName());
        assertThat(persistedBE.getRegionKuerzel()).isEqualTo(input.getRegionKuerzel());
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
        verifyZeroInteractions(regionenDAO);
    }

}