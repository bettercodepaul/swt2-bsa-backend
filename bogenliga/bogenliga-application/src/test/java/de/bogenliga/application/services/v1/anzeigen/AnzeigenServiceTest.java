package de.bogenliga.application.services.v1.anzeigen;

import java.util.Collections;
import java.util.List;

import de.bogenliga.application.services.v1.wettkampf.model.AnzeigenDTO;
import de.bogenliga.application.services.v1.wettkampf.service.AnzeigenService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.wettkampf.api.AnzeigenComponent;
import de.bogenliga.application.business.wettkampf.api.types.AnzeigenDO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AnzeigenServiceTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private AnzeigenComponent anzeigenComponent;

    @InjectMocks
    private AnzeigenService underTest;

    @Captor
    private ArgumentCaptor<Long> idCaptor;

    private AnzeigenDO anzeigenDO;

    private static final long VALID_ID = 42L;
    private static final long VERANSTALTUNGS_ID = 1337L;

    @Before
    public void setUp() {
        anzeigenDO = new AnzeigenDO();
        // Falls AnzeigenDO Pflichtfelder besitzt, hier befüllen,
        // damit der AnzeigenDTOMapper nicht fehlschlägt.
    }

    @Test
    public void findAll_shouldReturnListOfDTOs() {
        List<AnzeigenDO> doList = List.of(anzeigenDO);
        when(anzeigenComponent.findAll()).thenReturn(doList);

        List<AnzeigenDTO> result = underTest.findAll();

        assertThat(result)
                .isNotNull()
                .hasSize(1);

        verify(anzeigenComponent).findAll();
    }

    @Test
    public void findAll_emptyDatabase_shouldReturnEmptyList() {

        when(anzeigenComponent.findAll()).thenReturn(Collections.emptyList());

        List<AnzeigenDTO> result = underTest.findAll();

        assertThat(result)
                .isNotNull()
                .isEmpty();

        verify(anzeigenComponent).findAll();
    }

    @Test
    public void findById_withValidId_shouldReturnDTO() {

        when(anzeigenComponent.findById(VALID_ID)).thenReturn(anzeigenDO);

        AnzeigenDTO result = underTest.findById(VALID_ID);

        assertThat(result).isNotNull();

        verify(anzeigenComponent).findById(idCaptor.capture());
        assertThat(idCaptor.getValue()).isEqualTo(VALID_ID);
    }


    @Test
    public void findByVeranstaltungsId_withValidId_shouldReturnListOfDTOs() {

        List<AnzeigenDO> doList = List.of(anzeigenDO);
        when(anzeigenComponent.findByVeranstaltungsId(VERANSTALTUNGS_ID)).thenReturn(doList);

        List<AnzeigenDTO> result = underTest.findByVeranstaltungsId(VERANSTALTUNGS_ID);

        assertThat(result)
                .isNotNull()
                .hasSize(1);

        verify(anzeigenComponent).findByVeranstaltungsId(VERANSTALTUNGS_ID);
    }

}