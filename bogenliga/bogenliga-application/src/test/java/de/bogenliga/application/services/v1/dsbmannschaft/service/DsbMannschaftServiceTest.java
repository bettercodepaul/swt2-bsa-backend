package de.bogenliga.application.services.v1.dsbmannschaft.service;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import javax.naming.NoPermissionException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.services.v1.dsbmannschaft.mapper.DsbMannschaftDTOMapper;
import de.bogenliga.application.services.v1.dsbmannschaft.model.DsbMannschaftDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import de.bogenliga.application.services.v1.match.mapper.MatchDTOMapper;
import de.bogenliga.application.services.v1.match.model.MatchDTO;
import de.bogenliga.application.services.v1.match.service.MatchService;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissionAspect;

/**
 * TODO [AL] class documentation
 *
 * @author Philip dengler
 */
public class DsbMannschaftServiceTest {

    private static final long USER = 0;
    private static final long ID = 1893;

    private static final long VEREIN_ID = 111111;
    private static final String NAME = null; //empty
    private static final long NUMMER = 22222;
    private static final long BENUTZER_ID = 33333;
    private static final long VERANSTALTUNG_ID = 44444;
    private static final long CURRENT_VERANSTALTUNG_ID = 55555;
    private static final long SORTIERUNG = 1;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private DsbMannschaftComponent dsbMannschaftComponent;

    @Mock
    private RequiresOnePermissionAspect requiresOnePermissionAspect;

    @Mock
    private Principal principal;

    @InjectMocks
    private DsbMannschaftService underTest;

    @Captor
    private ArgumentCaptor<DsbMannschaftDO> dsbMannschaftVOArgumentCaptor;



    public static DsbMannschaftDO getDsbMannschaftDO() {
        return new DsbMannschaftDO(
                ID, NAME, VEREIN_ID, NUMMER, BENUTZER_ID, VERANSTALTUNG_ID, SORTIERUNG
        );
    }


    private static DsbMannschaftDTO getDsbMannschaftDTO() {
        final DsbMannschaftDTO dsbMannschaftDTO = new DsbMannschaftDTO();
        dsbMannschaftDTO.setId(ID);
        dsbMannschaftDTO.setVereinId(VEREIN_ID);
        dsbMannschaftDTO.setNummer(NUMMER);
        dsbMannschaftDTO.setBenutzerId(BENUTZER_ID);
        dsbMannschaftDTO.setVeranstaltungId(VERANSTALTUNG_ID);
        dsbMannschaftDTO.setSortierung(SORTIERUNG);

        return dsbMannschaftDTO;
    }


    @Before
    public void initMocks() {
        when(principal.getName()).thenReturn(String.valueOf(USER));
    }


    @Test
    public void findAll() {
        // prepare test data
        final DsbMannschaftDO dsbMannschaftDO = getDsbMannschaftDO();
        final List<DsbMannschaftDO> dsbMannschaftDOList = Collections.singletonList(dsbMannschaftDO);

        // configure mocks
        when(dsbMannschaftComponent.findAll()).thenReturn(dsbMannschaftDOList);

        // call test method
        final List<DsbMannschaftDTO> actual = underTest.findAll();

        // assert result
        assertThat(actual).isNotNull().hasSize(1);

        final DsbMannschaftDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getId()).isEqualTo(dsbMannschaftDO.getId());
        assertThat(actualDTO.getVereinId()).isEqualTo(dsbMannschaftDO.getVereinId());
        assertThat(actualDTO.getSortierung()).isEqualTo(dsbMannschaftDO.getSortierung());

        // verify invocations
        verify(dsbMannschaftComponent).findAll();
    }


    @Test
    public void findAllByVereinsId() {
        // prepare test data
        final DsbMannschaftDO dsbMannschaftDO = getDsbMannschaftDO();
        final List<DsbMannschaftDO> dsbMannschaftDOList = Collections.singletonList(dsbMannschaftDO);

        // configure mocks
        when(dsbMannschaftComponent.findAllByVereinsId(anyLong())).thenReturn(dsbMannschaftDOList);

        //call test method
        final List<DsbMannschaftDTO> actual = underTest.findAllByVereinsId(VEREIN_ID);

        //assert result
        assertThat(actual).isNotNull().hasSize(1);

        final DsbMannschaftDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getId()).isEqualTo(dsbMannschaftDO.getId());
        assertThat(actualDTO.getVereinId()).isEqualTo(dsbMannschaftDO.getVereinId());
        assertThat(actualDTO.getSortierung()).isEqualTo(dsbMannschaftDO.getSortierung());

        //verify invocations
        verify(dsbMannschaftComponent).findAllByVereinsId(VEREIN_ID);
    }


    @Test
    public void findAllByVeranstaltungsId() {
        // prepare test data
        final DsbMannschaftDO dsbMannschaftDO = getDsbMannschaftDO();
        final List<DsbMannschaftDO> dsbMannschaftDOList = Collections.singletonList(dsbMannschaftDO);

        // configure mocks
        when(dsbMannschaftComponent.findAllByVeranstaltungsId(anyLong())).thenReturn(dsbMannschaftDOList);

        //call test method
        final List<DsbMannschaftDTO> actual = underTest.findAllByVeranstaltungsId(VERANSTALTUNG_ID);

        //assert result
        assertThat(actual).isNotNull().hasSize(1);

        final DsbMannschaftDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getId()).isEqualTo(dsbMannschaftDO.getId());
        assertThat(actualDTO.getVeranstaltungId()).isEqualTo(dsbMannschaftDO.getVeranstaltungId());
        assertThat(actualDTO.getSortierung()).isEqualTo(dsbMannschaftDO.getSortierung());

        //verify invocations
        verify(dsbMannschaftComponent).findAllByVeranstaltungsId(VERANSTALTUNG_ID);
    }


    @Test
    public void findById() {
        // prepare test data
        final DsbMannschaftDO dsbMannschaftDO = getDsbMannschaftDO();

        // configure mocks
        when(dsbMannschaftComponent.findById(anyLong())).thenReturn(dsbMannschaftDO);

        // call test method
        final DsbMannschaftDTO actual = underTest.findById(ID);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(dsbMannschaftDO.getId());
        assertThat(actual.getVereinId()).isEqualTo(dsbMannschaftDO.getVereinId());
        assertThat(actual.getSortierung()).isEqualTo(dsbMannschaftDO.getSortierung());

        // verify invocations
        verify(dsbMannschaftComponent).findById(ID);
    }


    @Test
    public void create() {
        // prepare test data
        final DsbMannschaftDTO input = getDsbMannschaftDTO();
        final DsbMannschaftDO expected = getDsbMannschaftDO();

        // configure mocks
        when(dsbMannschaftComponent.create(any(), anyLong())).thenReturn(expected);
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(true);

        // call test method
        try {
            final DsbMannschaftDTO actual = underTest.create(input, principal);

            // assert result
            assertThat(actual).isNotNull();
            assertThat(actual.getId()).isEqualTo(input.getId());
            assertThat(actual.getVereinId()).isEqualTo(input.getVereinId());
            assertThat(actual.getSortierung()).isEqualTo(input.getSortierung());

            // verify invocations
            verify(dsbMannschaftComponent).create(dsbMannschaftVOArgumentCaptor.capture(), anyLong());

            final DsbMannschaftDO createdDsbMannschaft = dsbMannschaftVOArgumentCaptor.getValue();

            assertThat(createdDsbMannschaft).isNotNull();
            assertThat(createdDsbMannschaft.getId()).isEqualTo(input.getId());
            assertThat(createdDsbMannschaft.getVereinId()).isEqualTo(input.getVereinId());
            assertThat(createdDsbMannschaft.getSortierung()).isEqualTo(input.getSortierung());

        } catch (NoPermissionException e) { }
    }


    @Test
    public void createNoPermission() {
        // prepare test data
        final DsbMannschaftDTO input = getDsbMannschaftDTO();
        final DsbMannschaftDO expected = getDsbMannschaftDO();

        // configure mocks
        when(dsbMannschaftComponent.create(any(), anyLong())).thenReturn(expected);
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(false);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(any(), anyLong())).thenReturn(false);

        assertThatExceptionOfType(NoPermissionException.class)
                .isThrownBy(()-> underTest.create(input, principal));
    }


    @Test
    public void update() {
        // prepare test data
        final DsbMannschaftDTO input = getDsbMannschaftDTO();
        final DsbMannschaftDO expected = getDsbMannschaftDO();

        // configure mocks
        when(dsbMannschaftComponent.update(any(), anyLong())).thenReturn(expected);
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(true);

        // call test method
        try {
            final DsbMannschaftDTO actual = underTest.update(input, principal);

            // assert result
            assertThat(actual).isNotNull();
            assertThat(actual.getId()).isEqualTo(input.getId());
            assertThat(actual.getVereinId()).isEqualTo(input.getVereinId());
            assertThat(actual.getSortierung()).isEqualTo(input.getSortierung());

            // verify invocations
            verify(dsbMannschaftComponent).update(dsbMannschaftVOArgumentCaptor.capture(), anyLong());

            final DsbMannschaftDO updatedDsbMannschaft = dsbMannschaftVOArgumentCaptor.getValue();

            assertThat(updatedDsbMannschaft).isNotNull();
            assertThat(updatedDsbMannschaft.getId()).isEqualTo(input.getId());
            assertThat(updatedDsbMannschaft.getVereinId()).isEqualTo(input.getVereinId());
            assertThat(updatedDsbMannschaft.getSortierung()).isEqualTo(input.getSortierung());

        } catch (NoPermissionException e) { }
    }


    @Test
    public void updateNoPermission() {
        // prepare test data
        final DsbMannschaftDTO input = getDsbMannschaftDTO();
        final DsbMannschaftDO expected = getDsbMannschaftDO();

        // configure mocks
        when(dsbMannschaftComponent.create(any(), anyLong())).thenReturn(expected);
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(false);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(any(), anyLong())).thenReturn(false);

        assertThatExceptionOfType(NoPermissionException.class)
                .isThrownBy(()-> underTest.update(input, principal));
    }


    @Test
    public void update_Null() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> underTest.update(null, principal));
    }


    @Test
    public void copyMannschaftFromVeranstaltung() {
        // call test method
        underTest.copyMannschaftFromVeranstaltung(VERANSTALTUNG_ID, CURRENT_VERANSTALTUNG_ID, principal);

        // verify invocations
        verify(dsbMannschaftComponent).copyMannschaftFromVeranstaltung(anyLong(), anyLong(), anyLong());

        //assertThat(deletedDsbMannschaft).isNotNull();
    }


    @Test
    public void delete() {
        // prepare test data
        final DsbMannschaftDO expected = getDsbMannschaftDO();

        // configure mocks
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(true);
        when(dsbMannschaftComponent.findById(anyLong())).thenReturn(getDsbMannschaftDO());

        // call test method
        try{
        underTest.delete(ID, principal);

        // verify invocations
        verify(dsbMannschaftComponent).delete(dsbMannschaftVOArgumentCaptor.capture(), anyLong());

        final DsbMannschaftDO deletedDsbMannschaft = dsbMannschaftVOArgumentCaptor.getValue();

        assertThat(deletedDsbMannschaft).isNotNull();
        assertThat(deletedDsbMannschaft.getId()).isEqualTo(expected.getId());
        assertThat(deletedDsbMannschaft.getVereinId()).isNull();
        }catch (NoPermissionException e) { }
    }
}
