package de.bogenliga.application.services.v1.dsbmannschaft.service;

import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftSortierungComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.dsbmannschaft.impl.entity.DsbMannschaftBE;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.services.v1.dsbmannschaft.mapper.MannschaftSortierungDTOMapper;
import de.bogenliga.application.services.v1.dsbmannschaft.model.DsbMannschaftDTO;
import de.bogenliga.application.services.v1.dsbmannschaft.model.MannschaftSortierungDTO;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.security.Principal;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


public class MannschaftSortierungServiceTest {

    private static final Long USER = 0L;
    private static final Long ID = 1893L;

    private static final Long SORTIERUNG = 1L;
    private static final Long SPORTJAHR = 1L;

    private static final long DB_VEREIN_ID = 111111;
    private static final String DB_NAME = null; //empty
    private static final long DB_NUMMER = 22222;
    private static final long DB_BENUTZER_ID = 33333;
    private static final long DB_VERANSTALTUNG_ID = 44444;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private DsbMannschaftSortierungComponent mannschaftSortierungComponent;

    @Mock
    private Principal principal;

    @InjectMocks
    private MannschaftSortierungService underTest;

    @Captor
    private ArgumentCaptor<DsbMannschaftDO> dsbMannschaftVOArgumentCaptor;




    private static DsbMannschaftDO getDsbMannschaftDO() {
        return new DsbMannschaftDO(
                ID, DB_NAME, DB_VEREIN_ID, DB_NUMMER, DB_BENUTZER_ID, DB_VERANSTALTUNG_ID, SORTIERUNG, SPORTJAHR
        );
    }


    private static MannschaftSortierungDTO getMannschaftSortierungDTO() {
        final MannschaftSortierungDTO mannschaftSortierungDTO = new MannschaftSortierungDTO();
        mannschaftSortierungDTO.setId(ID);
        mannschaftSortierungDTO.setSortierung(SORTIERUNG);

        return mannschaftSortierungDTO;

    }


    @Before
    public void initMocks() {
        when(principal.getName()).thenReturn(String.valueOf(USER));
    }



    @Test
    public void update() {
        // prepare test data
        final MannschaftSortierungDTO inputDTO = getMannschaftSortierungDTO();
        final DsbMannschaftDO expected = getDsbMannschaftDO();

        // configure mocks
        when(mannschaftSortierungComponent.updateSortierung(any(), anyLong())).thenReturn(expected);

        // call test method
        final MannschaftSortierungDTO actual = underTest.update(inputDTO, principal);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(inputDTO.getId());
        assertThat(actual.getSortierung()).isEqualTo(inputDTO.getSortierung());

        // verify invocations
        verify(mannschaftSortierungComponent).updateSortierung(dsbMannschaftVOArgumentCaptor.capture(), anyLong());

        final DsbMannschaftDO updatedDsbMannschaft = dsbMannschaftVOArgumentCaptor.getValue();

        assertThat(updatedDsbMannschaft).isNotNull();
        assertThat(updatedDsbMannschaft.getId()).isEqualTo(inputDTO.getId());
        assertThat(updatedDsbMannschaft.getSortierung()).isEqualTo(inputDTO.getSortierung());
    }

    @Test
    public void update_WrongValueSortierung_expectException() {
        // prepare test data
        final MannschaftSortierungDTO inputDTO = getMannschaftSortierungDTO();
        inputDTO.setSortierung(-1L);
        final DsbMannschaftDO expected = getDsbMannschaftDO();

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(()-> underTest.update(inputDTO, principal))
                .withMessageContaining("must not be null or less than 0");

        // verify invocations
        verifyZeroInteractions(mannschaftSortierungComponent);
    }



}
