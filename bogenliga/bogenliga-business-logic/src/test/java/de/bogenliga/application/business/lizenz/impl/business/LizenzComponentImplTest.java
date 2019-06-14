package de.bogenliga.application.business.lizenz.impl.business;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.lizenz.api.types.LizenzDO;
import de.bogenliga.application.business.lizenz.impl.dao.LizenzDAO;
import de.bogenliga.application.business.lizenz.impl.entity.LizenzBE;
import de.bogenliga.application.business.regionen.api.types.RegionenDO;
import de.bogenliga.application.business.regionen.impl.business.RegionenComponentImpl;
import de.bogenliga.application.business.regionen.impl.dao.RegionenDAO;
import de.bogenliga.application.business.regionen.impl.entity.RegionenBE;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

public class LizenzComponentImplTest {

    private static final long lizenzId = 0;
    private static final String lizenznummer = "WT1234567";
    private static final long lizenzRegionId = 1;
    private static final long lizenzDsbMitgliedId = 1337L;
    private static final String lizenztyp = "Liga";
    private static final long lizenzDisziplinId = 0;
    private static final OffsetDateTime offsetDateTime = null;
    private static final long USER = 1;
    private static final long VERSION = 2;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private LizenzDAO lizenzDAO;
    @InjectMocks
    private LizenzComponentImpl underTest;
    @Captor
    private ArgumentCaptor<LizenzBE> lizenzBEArgumentCaptor;


    public static LizenzBE getLizenzBE() {
        final LizenzBE expectedBE = new LizenzBE();
        expectedBE.setLizenzId(lizenzId);
        expectedBE.setLizenznummer(lizenznummer);
        expectedBE.setLizenzRegionId(lizenzRegionId);
        expectedBE.setLizenzDisziplinId(lizenzDisziplinId);
        expectedBE.setLizenztyp(lizenztyp);
        expectedBE.setLizenzDsbMitgliedId(lizenzDsbMitgliedId);

        return expectedBE;
    }


    public static LizenzDO getLizenzDO() {
        return new LizenzDO(lizenzId,
                lizenznummer,
                lizenzRegionId,
                lizenzDsbMitgliedId,
                lizenztyp,
                lizenzDisziplinId);
    }



    @Test
    public void findAll() {

        // prepare test data
        final LizenzBE expectedBE = getLizenzBE();
        expectedBE.setLizenzId((long)0);
        expectedBE.setLizenznummer("WT1234567");
        expectedBE.setLizenzRegionId((long)1);
        expectedBE.setLizenzDsbMitgliedId(lizenzDsbMitgliedId);
        final List<LizenzBE> expectedBEList = Collections.singletonList(expectedBE);
        // configure mocks
        when(lizenzDAO.findAll()).thenReturn(expectedBEList);
        // call test method
        final List<LizenzDO> actual = underTest.findAll();

        // assert result
        Assertions.assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(actual.get(0)).isNotNull();

        Assertions.assertThat(actual.get(0).getLizenzDisziplinId())
                .isEqualTo(expectedBE.getLizenzDisziplinId());
        Assertions.assertThat(actual.get(0).getLizenzDsbMitgliedId())
                .isEqualTo(expectedBE.getLizenzDsbMitgliedId());
        Assertions.assertThat(actual.get(0).getLizenzId())
                .isEqualTo(expectedBE.getLizenzId());
        Assertions.assertThat(actual.get(0).getLizenznummer())
                .isEqualTo(expectedBE.getLizenznummer());
        Assertions.assertThat(actual.get(0).getLizenzRegionId())
                .isEqualTo(expectedBE.getLizenzRegionId());
        Assertions.assertThat(actual.get(0).getLizenztyp())
                .isEqualTo(expectedBE.getLizenztyp());


        // verify invocations
        verify(lizenzDAO).findAll();
    }


    @Test
    public void findByDsbMitgliedId() {


        // prepare test data
        final LizenzBE expectedBE = getLizenzBE();
        expectedBE.setLizenzId((long)0);
        expectedBE.setLizenznummer("WT1234567");
        expectedBE.setLizenzRegionId((long)1);
        expectedBE.setLizenzDsbMitgliedId(lizenzDsbMitgliedId);
        final List<LizenzBE> expectedBEList = Collections.singletonList(expectedBE);
        // configure mocks
        when(lizenzDAO.findByDsbMitgliedId(anyLong())).thenReturn(expectedBEList);
        // call test method
        final List<LizenzDO> actual = underTest.findByDsbMitgliedId(lizenzDisziplinId);

        // assert result
        Assertions.assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(actual.get(0)).isNotNull();

        Assertions.assertThat(actual.get(0).getLizenzDisziplinId())
                .isEqualTo(expectedBE.getLizenzDisziplinId());
        Assertions.assertThat(actual.get(0).getLizenzDsbMitgliedId())
                .isEqualTo(expectedBE.getLizenzDsbMitgliedId());
        Assertions.assertThat(actual.get(0).getLizenzId())
                .isEqualTo(expectedBE.getLizenzId());
        Assertions.assertThat(actual.get(0).getLizenznummer())
                .isEqualTo(expectedBE.getLizenznummer());
        Assertions.assertThat(actual.get(0).getLizenzRegionId())
                .isEqualTo(expectedBE.getLizenzRegionId());
        Assertions.assertThat(actual.get(0).getLizenztyp())
                .isEqualTo(expectedBE.getLizenztyp());


        // verify invocations
        verify(lizenzDAO).findByDsbMitgliedId(anyLong());
    }



    @Test
    public void create() {
        // prepare test data
        final LizenzDO input = new LizenzDO();
        input.setLizenzId(lizenzId);
        input.setLizenztyp("Liga");
        input.setLizenznummer("WT012354");
        input.setLizenzRegionId(1L);
        input.setLizenzDsbMitgliedId(lizenzDsbMitgliedId);
        input.setLizenzDisziplinId(1L);

        final LizenzBE lizenzBE = new LizenzBE();
        lizenzBE.setLizenzId(lizenzId);
        lizenzBE.setLizenzDsbMitgliedId(lizenzDsbMitgliedId);

        // configure mocks
        when(lizenzDAO.create(any(LizenzBE.class), anyLong())).thenReturn(lizenzBE);

        // call test method
        final LizenzDO actual = underTest.create(input, lizenzId);

        // assert result
        Assertions.assertThat(actual).isNotNull();

        Assertions.assertThat(actual.getLizenzDsbMitgliedId())
                .isEqualTo(input.getLizenzDsbMitgliedId());
        Assertions.assertThat(actual.getLizenzDsbMitgliedId())
                .isEqualTo(input.getLizenzDsbMitgliedId());

        // verify invocations
        verify(lizenzDAO).create(any(LizenzBE.class), anyLong());
    }


    @Test
    public void update() {
        // prepare test data
        final LizenzDO input = new LizenzDO();
        input.setLizenzId(lizenzId);
        input.setLizenztyp("Liga");
        input.setLizenznummer("WT012354");
        input.setLizenzRegionId(1L);
        input.setLizenzDsbMitgliedId(lizenzDsbMitgliedId);
        input.setLizenzDisziplinId(1L);

        final LizenzBE lizenzBE = new LizenzBE();
        lizenzBE.setLizenzId(lizenzId);
        lizenzBE.setLizenzDsbMitgliedId(lizenzDsbMitgliedId);

        // configure mocks
        when(lizenzDAO.update(any(LizenzBE.class), anyLong())).thenReturn(lizenzBE);

        // call test method
        final LizenzDO actual = underTest.update(input, lizenzId);

        // assert result
        Assertions.assertThat(actual).isNotNull();

        Assertions.assertThat(actual.getLizenzDsbMitgliedId())
                .isEqualTo(input.getLizenzDsbMitgliedId());
        Assertions.assertThat(actual.getLizenzDsbMitgliedId())
                .isEqualTo(input.getLizenzDsbMitgliedId());

        // verify invocations
        verify(lizenzDAO).update(any(LizenzBE.class), anyLong());
    }





//
//
//
//    @Test
//    public void create__whenEverythingIsSet() {
//        // prepare test data
//        final RegionenDO input = getRegionenDO();
//        final RegionenBE expectedBE = getRegionenBE();
//
//        // configure mocks
//        when(regionenDAO.create(any(RegionenBE.class), anyLong())).thenReturn(expectedBE);
//
//        // call test method
//        final RegionenDO actual = underTest.create(input, USER);
//
//        // assert result
//        assertThat(actual).isNotNull();
//        assertThat(actual.getId()).isEqualTo(input.getId());
//        assertThat(actual.getRegionName()).isEqualTo(input.getRegionName());
//        assertThat(actual.getRegionKuerzel()).isEqualTo(input.getRegionKuerzel());
//        assertThat(actual.getRegionTyp()).isEqualTo(input.getRegionTyp());
//        assertThat(actual.getRegionUebergeordnet()).isEqualTo(input.getRegionUebergeordnet());
//
//        // verify invocations
//        verify(regionenDAO).create(regionBEArgumentCaptor.capture(), anyLong());
//        final RegionenBE persistedBE = regionBEArgumentCaptor.getValue();
//
//        assertThat(persistedBE).isNotNull();
//
//        assertThat(persistedBE.getRegionId()).isEqualTo(input.getId());
//        assertThat(persistedBE.getRegionName()).isEqualTo(expectedBE.getRegionName());
//        assertThat(persistedBE.getRegionKuerzel()).isEqualTo(expectedBE.getRegionKuerzel());
//        assertThat(persistedBE.getRegionTyp()).isEqualTo(expectedBE.getRegionTyp());
//        assertThat(persistedBE.getRegionUebergeordnet()).isEqualTo(expectedBE.getRegionUebergeordnet());
//
//        // test mapping of do
//        assertThat(actual.getRegionName()).isEqualTo(expectedBE.getRegionName());
//        assertThat(actual.getRegionKuerzel()).isEqualTo(expectedBE.getRegionKuerzel());
//        assertThat(actual.getRegionTyp()).isEqualTo(expectedBE.getRegionTyp());
//    }
//
//
//    @Test
//    public void create_whenArgumentsAreNull() {
//        // prepare test data
//        final RegionenDO input = getRegionenDO();
//        final RegionenBE expectedBE = getRegionenBE();
//
//        expectedBE.setRegionKuerzel(null);
//        input.setRegionKuerzel(null);
//
//        // configure mocks
//        when(regionenDAO.create(any(RegionenBE.class), anyLong())).thenReturn(expectedBE);
//
//        // call test method
//        final RegionenDO actual = underTest.create(input, USER);
//
//        // assert result
//        assertThat(actual).isNotNull();
//        assertThat(actual.getId()).isEqualTo(input.getId());
//        assertThat(actual.getRegionName()).isEqualTo(input.getRegionName());
//        assertThat(actual.getRegionKuerzel()).isEqualTo(input.getRegionKuerzel());
//
//        // verify invocations
//        verify(regionenDAO).create(regionBEArgumentCaptor.capture(), anyLong());
//        final RegionenBE persistedBE = regionBEArgumentCaptor.getValue();
//
//        assertThat(persistedBE).isNotNull();
//
//        assertThat(persistedBE.getRegionId()).isEqualTo(input.getId());
//        assertThat(persistedBE.getRegionName()).isEqualTo(expectedBE.getRegionName());
//        assertThat(persistedBE.getRegionKuerzel()).isEqualTo(null);
//    }
//
//
//    @Test
//    public void create_withoutRegionname_shouldThrowException() {
//        // prepare test data
//        final RegionenDO input = getRegionenDO();
//        input.setId(REGION_ID);
//        input.setRegionName(null);
//
//        // configure mocks
//
//        // call test method
//        assertThatExceptionOfType(BusinessException.class)
//                .isThrownBy(() -> underTest.create(input, USER))
//                .withMessageContaining("must not be null")
//                .withNoCause();
//
//        // assert result
//
//        // verify invocations
//        verifyZeroInteractions(regionenDAO);
//    }
//
//
//    @Test
//    public void create_withoutInput_shouldThrowException() {
//        // prepare test data
//
//        // configure mocks
//
//        // call test method
//        assertThatExceptionOfType(BusinessException.class)
//                .isThrownBy(() -> underTest.create(null, USER))
//                .withMessageContaining("must not be null")
//                .withNoCause();
//
//        // assert result
//
//        // verify invocations
//        verifyZeroInteractions(regionenDAO);
//    }
//
//
//
//    @Test
//    public void delete_withoutInput_shouldThrowException() {
//        // prepare test data
//
//        // configure mocks
//
//        // call test method
//        assertThatExceptionOfType(BusinessException.class)
//                .isThrownBy(() -> underTest.delete(null, USER))
//                .withMessageContaining("must not be null")
//                .withNoCause();
//
//        // assert result
//
//        // verify invocations
//        verifyZeroInteractions(regionenDAO);
//    }

}