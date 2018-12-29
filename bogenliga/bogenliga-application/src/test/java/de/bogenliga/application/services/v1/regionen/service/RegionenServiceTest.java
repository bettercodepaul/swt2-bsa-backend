package de.bogenliga.application.services.v1.regionen.service;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.regionen.api.RegionenComponent;
import de.bogenliga.application.business.regionen.api.types.RegionenDO;
import de.bogenliga.application.business.regionen.impl.entity.RegionenBE;
import de.bogenliga.application.services.v1.regionen.model.RegionenDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Dennis Goericke, dennis.goericke@student.reutlingen-university.de
 */

public class RegionenServiceTest {
    private static final long USER = 0;

    private static final long ID = 3333;
    private static final String regionName = "Bezirk Reutlingen";
    private static final String regionKuerzel = "RT";
    private static final String regionTyp = "KREIS";
    private static final long regionUebergeordnet = 4;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private RegionenComponent regionenComponent;

    @Mock
    private Principal principal;

    @InjectMocks
    private RegionenService underTest;

    @Captor
    private ArgumentCaptor<RegionenDO> regionenDOArgumentCaptor;


    /***
     * Utility methods for creating business entities/data objects.
     * Also used by other test classes.
     */

    public static RegionenBE getRegionenBE() {
        final RegionenBE expectedRegionenBE = new RegionenBE();
        expectedRegionenBE.setRegionId(ID);
        expectedRegionenBE.setRegionName(regionName);
        expectedRegionenBE.setRegionKuerzel(regionKuerzel);
        expectedRegionenBE.setRegionTyp(regionTyp);
        expectedRegionenBE.setRegionUebergeordnet(regionUebergeordnet);

        return expectedRegionenBE;
    }


    public static RegionenDO getRegionenDO() {
        return new RegionenDO(
                ID,
                regionName,
                regionKuerzel,
                regionTyp,
                regionUebergeordnet);

    }


    private static RegionenDTO getRegionenDTO() {
        final RegionenDTO regionenDTO = new RegionenDTO();
        regionenDTO.setId(ID);
        regionenDTO.setName(regionName);
        regionenDTO.setKuerzel(regionKuerzel);
        regionenDTO.setTyp(regionTyp);
        regionenDTO.setUebergeordnet(regionUebergeordnet);
        return regionenDTO;
    }


    @Before
    public void initMocks() {
        when(principal.getName()).thenReturn(String.valueOf(USER));
    }


    @Test
    public void findAll() {
        //prepare Test
        final RegionenDO regionenDO = getRegionenDO();

        final List<RegionenDO> regionenDOList = Collections.singletonList(regionenDO);

        //configure Mocks
        when(regionenComponent.findAll()).thenReturn(regionenDOList);

        //call test method
        final List<RegionenDTO> actual = underTest.findAll();

        //assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final RegionenDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getId()).isEqualTo(regionenDO.getId());
        assertThat(actualDTO.getName()).isEqualTo(regionenDO.getRegionName());

        //verify invocations
        verify(regionenComponent).findAll();
    }


    @Test
    public void findAllByType() {
        //prepare test
        final RegionenDO regionenDO = getRegionenDO();

        final List<RegionenDO> regionenDOList = Collections.singletonList(regionenDO);

        //Configure mocks
        when(regionenComponent.findAllByType(regionTyp)).thenReturn(regionenDOList);

        //call test method
        final List<RegionenDTO> actual = underTest.findAllByType(regionTyp);

        //assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final RegionenDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getTyp()).isEqualTo(regionenDO.getRegionType());
    }
}
