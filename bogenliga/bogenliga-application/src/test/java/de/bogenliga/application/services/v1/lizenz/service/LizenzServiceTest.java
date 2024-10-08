package de.bogenliga.application.services.v1.lizenz.service;

import java.security.Principal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.bogenliga.application.business.lizenz.api.LizenzComponent;
import de.bogenliga.application.business.lizenz.api.types.LizenzDO;
import de.bogenliga.application.business.lizenz.impl.entity.LizenzBE;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.vereine.impl.entity.VereinBE;
import de.bogenliga.application.services.v1.lizenz.model.LizenzDTO;
import de.bogenliga.application.services.v1.vereine.model.VereineDTO;
import de.bogenliga.application.services.v1.vereine.service.VereineService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class LizenzServiceTest {

    private static final long USER = 0;
    private static final long ID = 0;
    private static final long VERSION = 0;



    private static final long lizenzId = 1;
    private static final String lizenznummer = "WT012251234";
    private static final long lizenzRegionId = 2;
    private static final long lizenzDsbMitgliedId = 3;
    private static final String lizenztyp = "Liga";
    private static final long lizenzDisziplinId = 0;
    private static final OffsetDateTime VEREIN_OFFSETDATETIME = null;
    private static final Logger LOG = LoggerFactory.getLogger(VereineService.class);
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private LizenzComponent lizenzComponent;

    @Mock
    private Principal principal;

    @InjectMocks
    private LizenzService underTest;

    @Captor
    private ArgumentCaptor<LizenzDO> lizenzDOArgumentCaptor;

    /***
     * Utility methods for creating business entities/data objects.
     * Also used by other test classes.
     */
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

    public static LizenzDTO getLizenzDTO() {
        final LizenzDTO lizenzDTO = new LizenzDTO();
        lizenzDTO.setLizenzId(lizenzId);
        lizenzDTO.setLizenznummer(lizenznummer);
        lizenzDTO.setLizenzRegionId(lizenzRegionId);
        lizenzDTO.setLizenzDisziplinId(lizenzDisziplinId);
        lizenzDTO.setLizenztyp(lizenztyp);
        lizenzDTO.setLizenzDsbMitgliedId(lizenzDsbMitgliedId);
        return lizenzDTO;
    }

    @Before
    public void initMocks() {
        when(principal.getName()).thenReturn(String.valueOf(USER));
    }

    @Test
    public void findAll() {
        // prepare test data
        final LizenzDO lizenzDO = getLizenzDO();

        final List<LizenzDO> LizenzDOList = Collections.singletonList(lizenzDO);

        // configure mocks
        when(lizenzComponent.findAll()).thenReturn(LizenzDOList);

        // call test method
        final List<LizenzDTO> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final LizenzDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getLizenzId()).isEqualTo(lizenzDO.getLizenzId());
        assertThat(actualDTO.getLizenzDisziplinId()).isEqualTo(lizenzDO.getLizenzDisziplinId());
        assertThat(actualDTO.getLizenzDsbMitgliedId()).isEqualTo(lizenzDO.getLizenzDsbMitgliedId());
        assertThat(actualDTO.getLizenznummer()).isEqualTo(lizenzDO.getLizenznummer());
        assertThat(actualDTO.getLizenzRegionId()).isEqualTo(lizenzDO.getLizenzRegionId());
        assertThat(actualDTO.getLizenztyp()).isEqualTo(lizenzDO.getLizenztyp());

        // verify invocations
        verify(lizenzComponent).findAll();
    }


    @Test
    public void findByDsbMitgliedId() {


        // prepare test data
        final LizenzDO expectedDO = getLizenzDO();
        expectedDO.setLizenzId((long)0);
        expectedDO.setLizenznummer("WT1234567");
        expectedDO.setLizenzRegionId((long)1);
        expectedDO.setLizenzDsbMitgliedId(lizenzDsbMitgliedId);
        final List<LizenzDO> expectedDOList = Collections.singletonList(expectedDO);
        // configure mocks
        when(lizenzComponent.findByDsbMitgliedId(anyLong())).thenReturn(expectedDOList);
        // call test method
        final List<LizenzDTO> actual = underTest.findByDsbMitgliedId(lizenzDisziplinId);

        // assert result
        Assertions.assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(actual.get(0)).isNotNull();

        Assertions.assertThat(actual.get(0).getLizenzDisziplinId())
                .isEqualTo(expectedDO.getLizenzDisziplinId());
        Assertions.assertThat(actual.get(0).getLizenzDsbMitgliedId())
                .isEqualTo(expectedDO.getLizenzDsbMitgliedId());
        Assertions.assertThat(actual.get(0).getLizenzId())
                .isEqualTo(expectedDO.getLizenzId());
        Assertions.assertThat(actual.get(0).getLizenznummer())
                .isEqualTo(expectedDO.getLizenznummer());
        Assertions.assertThat(actual.get(0).getLizenzRegionId())
                .isEqualTo(expectedDO.getLizenzRegionId());
        Assertions.assertThat(actual.get(0).getLizenztyp())
                .isEqualTo(expectedDO.getLizenztyp());


        // verify invocations
        verify(lizenzComponent).findByDsbMitgliedId(anyLong());
    }



    @Test
    public void create() {
        // prepare test data
        final LizenzDTO input = new LizenzDTO();
        input.setLizenzId(lizenzId);
        input.setLizenztyp("Liga");
        input.setLizenznummer("WT012354");
        input.setLizenzRegionId(1L);
        input.setLizenzDsbMitgliedId(lizenzDsbMitgliedId);
        input.setLizenzDisziplinId(1L);

        final LizenzDO lizenzDO = new LizenzDO();
        lizenzDO.setLizenzId(lizenzId);
        lizenzDO.setLizenzDsbMitgliedId(lizenzDsbMitgliedId);

        // configure mocks
        when(lizenzComponent.create(any(LizenzDO.class), anyLong())).thenReturn(lizenzDO);

        // call test method
        final LizenzDTO actual = underTest.create(input, principal);

        // assert result
        Assertions.assertThat(actual).isNotNull();

        Assertions.assertThat(actual.getLizenzDsbMitgliedId())
                .isEqualTo(input.getLizenzDsbMitgliedId());
        Assertions.assertThat(actual.getLizenzDsbMitgliedId())
                .isEqualTo(input.getLizenzDsbMitgliedId());

        // verify invocations
        verify(lizenzComponent).create(any(LizenzDO.class), anyLong());
    }


    @Test
    public void update() {
        // prepare test data
        final LizenzDTO input = new LizenzDTO();
        input.setLizenzId(lizenzId);
        input.setLizenztyp("Liga");
        input.setLizenznummer("WT012354");
        input.setLizenzRegionId(1L);
        input.setLizenzDsbMitgliedId(lizenzDsbMitgliedId);
        input.setLizenzDisziplinId(1L);

        final LizenzDO lizenzDO = new LizenzDO();
        lizenzDO.setLizenzId(lizenzId);
        lizenzDO.setLizenzDsbMitgliedId(lizenzDsbMitgliedId);

        // configure mocks
        when(lizenzComponent.update(any(LizenzDO.class), anyLong())).thenReturn(lizenzDO);

        // call test method
        final LizenzDTO actual = underTest.update(input, principal);

        // assert result
        Assertions.assertThat(actual).isNotNull();

        Assertions.assertThat(actual.getLizenzDsbMitgliedId())
                .isEqualTo(input.getLizenzDsbMitgliedId());
        Assertions.assertThat(actual.getLizenzDsbMitgliedId())
                .isEqualTo(input.getLizenzDsbMitgliedId());

        // verify invocations
        verify(lizenzComponent).update(any(LizenzDO.class), anyLong());
    }
}