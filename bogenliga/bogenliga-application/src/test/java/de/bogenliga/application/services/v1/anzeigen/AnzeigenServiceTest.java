package de.bogenliga.application.services.v1.anzeigen;

import java.security.Principal;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
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
    private Principal principal;

    private static final long VALID_ID = 42L;
    private static final long VERANSTALTUNGS_ID = 1337L;

    @Before
    public void setUp() {
        anzeigenDO = new AnzeigenDO();
        principal = () -> "99";
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

    @Captor
    private ArgumentCaptor<AnzeigenDO> anzeigenDOCaptor;

    @Captor
    private ArgumentCaptor<Long> userIdCaptor;

    @Test
    public void create_withValidData_shouldReturnSavedId() {
        AnzeigenDTO inputDTO = new AnzeigenDTO(null, "Screen_01", "Tabelle", 1337L, 1);
        AnzeigenDO savedDO = new AnzeigenDO(VALID_ID, "Screen_01", "Tabelle", 1337L, 1);

        when(anzeigenComponent.create(any(AnzeigenDO.class), anyLong())).thenReturn(savedDO);

        long resultId = underTest.create(inputDTO, principal);

        assertThat(resultId).isEqualTo(VALID_ID);

        verify(anzeigenComponent).create(anzeigenDOCaptor.capture(), userIdCaptor.capture());

        AnzeigenDO capturedDO = anzeigenDOCaptor.getValue();
        assertThat(capturedDO).isNotNull();
        assertThat(capturedDO.getPhysischeBildschirmId()).isEqualTo("Screen_01");
        assertThat(capturedDO.getVeranstaltungsId()).isEqualTo(1337L);
    }

    @Test
    public void create_withNullDTO_shouldThrowException() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> underTest.create(null, principal));

        verifyZeroInteractions(anzeigenComponent);
    }

    @Test
    public void update_withNullDTO_shouldThrowException() {
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> underTest.update(null, principal));

        verifyZeroInteractions(anzeigenComponent);

    }

    @Test
    public void update_withValidData_shouldReturnUpdatedId() {
        AnzeigenDTO inputDTO = new AnzeigenDTO(null, "Screen_01", "Tabelle", 1337L, 1);
        AnzeigenDO updatedDO = new AnzeigenDO(VALID_ID, "Screen_01", "Tabelle", 1337L, 1);

        when(anzeigenComponent.update(any(AnzeigenDO.class), anyLong())).thenReturn(updatedDO);

        AnzeigenDTO resultId = underTest.update(inputDTO, principal);

        assertThat(resultId.getId()).isEqualTo(VALID_ID);

        verify(anzeigenComponent).update(anzeigenDOCaptor.capture(), userIdCaptor.capture());

        AnzeigenDO capturedDO = anzeigenDOCaptor.getValue();
        assertThat(capturedDO).isNotNull();
        assertThat(capturedDO.getPhysischeBildschirmId()).isEqualTo("Screen_01");
        assertThat(capturedDO.getVeranstaltungsId()).isEqualTo(1337L);

        assertThat(userIdCaptor.getValue()).isEqualTo(99L);
    }
}