package de.bogenliga.application.business.regionen.impl.business;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.regionen.api.types.RegionenDO;
import de.bogenliga.application.business.regionen.impl.dao.RegionenDAO;
import de.bogenliga.application.business.regionen.impl.entity.RegionenBE;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

public class RegionenComponentImplTest {

    private static final long REGION_ID = 10;
    private static final long REGION_UEBERGEORDNET = 3;
    private static final String REGION_NAME = "Test";
    private static final String REGION_KUERZEL = "T";
    private static final String REGION_TYP = "TEST";
    private static final OffsetDateTime offsetDateTime = null;
    private static final long USER = 1;
    private static final long VERSION = 2;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private RegionenDAO regionenDAO;
    @InjectMocks
    private RegionenComponentImpl underTest;


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
        assertThat(actual.get(0).getRegionType())
                .isEqualTo(expectedBE.getRegionTyp());
        assertThat(actual.get(0).getRegionUebergeordnet())
                .isEqualTo(expectedBE.getRegionUebergeordnet());

        // verify invocations
        verify(regionenDAO).findAll();
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
        assertThat(actual.get(0).getRegionType())
                .isEqualTo(expectedBE.getRegionTyp());
        assertThat(actual.get(0).getRegionUebergeordnet())
                .isEqualTo(expectedBE.getRegionUebergeordnet());

        // verify invocations
        verify(regionenDAO).findAllByType(REGION_TYP);
    }

}