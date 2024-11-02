package de.bogenliga.application.services.v1.mannschaftsmitglied.Service;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import javax.naming.NoPermissionException;

import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.services.v1.mannschaftsmitglied.model.MannschaftsMitgliedDTO;
import de.bogenliga.application.services.v1.mannschaftsmitglied.service.MannschaftsMitgliedService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissionAspect;


public class MannschaftsmitgliedServiceTest {

    private static final Long USER = 0L;
    private static final Long id = 1L;
    private static final Long mannschaftsId = 1L;
    private static final Long dsbMitgliedId = 100L;
    private static final Integer dsbMitgliedEingesetzt = 1;
    private static final String dsbMitgliedVorname = "Mario";
    private static final String dsbMitgliedNachname = "Gomez";
    private static final Long rueckennummer = 5L;
    private static final Long wettkampId = 30L;
    private static final Long platzhalterMitgliedId = 1L;
    private static final Long platzhalterId = 6969L;
    private static final Long dsbMitgliedPlatzhalterId = 1L;
    private static final String dsbMitgliedPlatzhalterVorname = "PlatzhalterVorname1";
    private static final String dsbMitgliedPlatzhalterNachname = "PlatzhalterNachname1";
    private static final Long rueckennummerPlatzhalter = 1L;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private MannschaftsmitgliedComponent mannschaftsmitgliedComponent;

    @Mock
    private DsbMannschaftComponent dsbMannschaftComponent;

    @Mock
    private RequiresOnePermissionAspect requiresOnePermissionAspect;

    @Mock
    private Principal principal;

    @InjectMocks
    private MannschaftsMitgliedService underTest;

    @Captor
    private ArgumentCaptor<MannschaftsmitgliedDO> mannschaftsmitgliedVOArgumentCaptor;


    public static DsbMannschaftDO getDsbMannschaftDO() {
        return new DsbMannschaftDO(
                mannschaftsId, "die Mannschaft", id, 23L,
                id, id, 2L, 1L
        );
    }

    public static MannschaftsmitgliedDO getMannschaftsmitgliedDO() {
        return new MannschaftsmitgliedDO(
                id, mannschaftsId, dsbMitgliedId, dsbMitgliedEingesetzt, dsbMitgliedVorname, dsbMitgliedNachname, rueckennummer
        );
    }

    public static MannschaftsmitgliedDO getMannschaftsmitgliedPlatzhalterDO() {
        return new MannschaftsmitgliedDO(
                platzhalterMitgliedId, platzhalterId, dsbMitgliedPlatzhalterId, 1,
                dsbMitgliedPlatzhalterVorname, dsbMitgliedPlatzhalterNachname, rueckennummerPlatzhalter
        );
    }

    public static MannschaftsMitgliedDTO getMannschaftsmitgliedPlatzhalterDTO() {
        return new MannschaftsMitgliedDTO(
                platzhalterMitgliedId, platzhalterId, dsbMitgliedPlatzhalterId, 1,
                rueckennummerPlatzhalter
        );
    }


    public static MannschaftsMitgliedDTO getMannschaftsmitgliedDTO() {
        final MannschaftsMitgliedDTO mannschaftsMitgliedDTO = new MannschaftsMitgliedDTO();
        mannschaftsMitgliedDTO.setMannschaftsId(mannschaftsId);
        mannschaftsMitgliedDTO.setDsbMitgliedId(dsbMitgliedId);
        mannschaftsMitgliedDTO.setDsbMitgliedEingesetzt(dsbMitgliedEingesetzt);
        mannschaftsMitgliedDTO.setRueckennummer(rueckennummer);
        return mannschaftsMitgliedDTO;
    }



    @Before
    public void initMocks() {
        when(principal.getName()).thenReturn(String.valueOf(USER));
    }


    @Test
    public void findAll() {
        // prepare test data
        final MannschaftsmitgliedDO mannschaftsmitgliedDO = getMannschaftsmitgliedDO();
        final List<MannschaftsmitgliedDO> mannschaftsmitgliedDOList = Collections.singletonList(mannschaftsmitgliedDO);

        // configure Mocks
        when(mannschaftsmitgliedComponent.findAll()).thenReturn(mannschaftsmitgliedDOList);

        // call test method
        final List<MannschaftsMitgliedDTO> actual = underTest.findAll();

        // assert result
        assertThat(actual).isNotNull().hasSize(1);

        final MannschaftsMitgliedDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getMannschaftsId()).isEqualTo(mannschaftsmitgliedDO.getMannschaftId());

        // verify invocations
        verify(mannschaftsmitgliedComponent).findAll();
    }


    @Test
    public void findByTeamId() {
        // prepare test data
        final MannschaftsmitgliedDO mannschaftsmitgliedDO = getMannschaftsmitgliedDO();
        final List<MannschaftsmitgliedDO> mannschaftsmitgliedDOList = Collections.singletonList(mannschaftsmitgliedDO);

        // configure Mocks
        when(mannschaftsmitgliedComponent.findByTeamId(anyLong())).thenReturn(mannschaftsmitgliedDOList);

        // call test method
        final List<MannschaftsMitgliedDTO> actual = underTest.findByTeamId(mannschaftsId);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.get(0).getMannschaftsId()).isEqualTo(mannschaftsmitgliedDO.getMannschaftId());

        // verify invocations
        verify(mannschaftsmitgliedComponent).findByTeamId(mannschaftsId);
    }


    @Test
    public void findByMemberAndTeamId() {
        // prepare test data
        final MannschaftsmitgliedDO mannschaftsmitgliedDO = getMannschaftsmitgliedDO();

        // configure mocks
        when(mannschaftsmitgliedComponent.findByMemberAndTeamId(mannschaftsId, dsbMitgliedId)).thenReturn(
                mannschaftsmitgliedDO);

        final MannschaftsMitgliedDTO actual = underTest.findByMemberAndTeamId(mannschaftsId, dsbMitgliedId);

        assertThat(actual).isNotNull();
        assertThat(actual.getMannschaftsId()).isEqualTo(actual.getMannschaftsId());
        assertThat(actual.getDsbMitgliedId()).isEqualTo(actual.getDsbMitgliedId());
    }

    @Test
    public void findByTeamIdAndRueckennummer() {
        // prepare test data
        final MannschaftsmitgliedDO mannschaftsmitgliedDO = getMannschaftsmitgliedDO();

        // configure mocks
        when(mannschaftsmitgliedComponent.findByTeamIdAndRueckennummer(mannschaftsId, dsbMitgliedId)).thenReturn(
                mannschaftsmitgliedDO);

        final MannschaftsMitgliedDTO actual = underTest.findByTeamIdAndRueckennummer(mannschaftsId, dsbMitgliedId);

        assertThat(actual).isNotNull();
        assertThat(actual.getMannschaftsId()).isEqualTo(actual.getMannschaftsId());
        assertThat(actual.getDsbMitgliedId()).isEqualTo(actual.getDsbMitgliedId());
    }

    @Test
    public void findByTeamIdAndRueckennummerThrowsException() {
        // prepare test data
        final MannschaftsmitgliedDO mannschaftsmitgliedDO = getMannschaftsmitgliedDO();

        // configure mocks
        when(mannschaftsmitgliedComponent.findByTeamIdAndRueckennummer(mannschaftsId, dsbMitgliedId)).thenReturn(
                mannschaftsmitgliedDO);

        assertThatThrownBy(()->{
            underTest.findByTeamIdAndRueckennummer(0, dsbMitgliedId);
        }).isInstanceOf(BusinessException.class);

        assertThatThrownBy(()->{
            underTest.findByTeamIdAndRueckennummer(-1, dsbMitgliedId);
        }).isInstanceOf(BusinessException.class);

        assertThatThrownBy(()->{
            underTest.findByTeamIdAndRueckennummer(mannschaftsId, 0);
        }).isInstanceOf(BusinessException.class);

        assertThatThrownBy(()->{
            underTest.findByTeamIdAndRueckennummer(mannschaftsId, 0);
        }).isInstanceOf(BusinessException.class);
    }

    @Test
    public void findSchuetzenInUebergelegenerLiga(){
        final MannschaftsmitgliedDO mannschaftsmitgliedDO = getMannschaftsmitgliedDO();
        final List<MannschaftsmitgliedDO> mannschaftsmitgliedDOList = Collections.singletonList(mannschaftsmitgliedDO);

        // configure mocks
        when(mannschaftsmitgliedComponent.findSchuetzenInUebergelegenerLiga(mannschaftsId, wettkampId)).thenReturn(mannschaftsmitgliedDOList);
        final List<MannschaftsMitgliedDTO> actual = underTest.findSchuetzenInUebergelegenerLiga(mannschaftsId, wettkampId);

        assertThat(actual.get(0).getDsbMitgliedId()).isEqualTo(mannschaftsmitgliedDOList.get(0).getDsbMitgliedId());
    }

    @Test
    public void findAllSchuetzeInTeam() {
        // prepare test data
        final MannschaftsmitgliedDO mannschaftsmitgliedDO = getMannschaftsmitgliedDO();
        final List<MannschaftsmitgliedDO> mannschaftsmitgliedDOList = Collections.singletonList(mannschaftsmitgliedDO);

        // configure mocks
        when(mannschaftsmitgliedComponent.findAllSchuetzeInTeamEingesetzt(mannschaftsId)).thenReturn(mannschaftsmitgliedDOList);

        // call test method
        final List<MannschaftsMitgliedDTO> actual = underTest.findAllSchuetzeInTeam(mannschaftsId);

        // assert result
        assertThat(actual).isNotNull().isNotEmpty().hasSize(1);

        assertThat(actual.get(0).getMannschaftsId()).isEqualTo(mannschaftsmitgliedDO.getMannschaftId());
        assertThat(actual.get(0).getDsbMitgliedId()).isEqualTo(mannschaftsmitgliedDO.getDsbMitgliedId());
        assertThat(actual.get(0).getDsbMitgliedEingesetzt()).isEqualTo(mannschaftsmitgliedDO.getDsbMitgliedEingesetzt());
    }


    @Test
    public void findByMemberId() {
        // prepare test data
        final MannschaftsmitgliedDO mannschaftsmitgliedDO = getMannschaftsmitgliedDO();
        final List<MannschaftsmitgliedDO> mannschaftsmitgliedDOList = Collections.singletonList(mannschaftsmitgliedDO);

        //configure Mocks
        when(mannschaftsmitgliedComponent.findByMemberId(anyLong())).thenReturn(mannschaftsmitgliedDOList);

        // call test method
        final List<MannschaftsMitgliedDTO> actual = underTest.findByMemberId(dsbMitgliedId);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.get(0).getDsbMitgliedId()).isEqualTo(mannschaftsmitgliedDO.getDsbMitgliedId());

        // verify invocations
        verify(mannschaftsmitgliedComponent).findByMemberId(dsbMitgliedId);
    }


    @Test
    public void update() {
        final MannschaftsMitgliedDTO input = getMannschaftsmitgliedDTO();
        final MannschaftsmitgliedDO expectedDO = getMannschaftsmitgliedDO();
        final DsbMannschaftDO dsbMannschaftDO = getDsbMannschaftDO();

        // configure mocks
        when(dsbMannschaftComponent.findById(anyLong())).thenReturn(dsbMannschaftDO);
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(true);
        when(mannschaftsmitgliedComponent.update(any(MannschaftsmitgliedDO.class), anyLong())).thenReturn(expectedDO);

        // call test method
        try {
            final MannschaftsMitgliedDTO actual = underTest.update(input, principal);

            // assert result
            assertThat(actual).isNotNull();
            assertThat(actual.getMannschaftsId()).isEqualTo(input.getMannschaftsId());

            // verify invocations
            verify(mannschaftsmitgliedComponent).update(mannschaftsmitgliedVOArgumentCaptor.capture(), anyLong());

            final MannschaftsmitgliedDO persistedDO = mannschaftsmitgliedVOArgumentCaptor.getValue();

            assertThat(persistedDO).isNotNull();
            assertThat(persistedDO.getMannschaftId()).isEqualTo(input.getMannschaftsId());

        } catch (NoPermissionException | NullPointerException e) { }
    }


    @Test
    public void updateDataSepcificPermission() {
        // prepare test data
        final MannschaftsMitgliedDTO input = getMannschaftsmitgliedDTO();
        final MannschaftsmitgliedDO expectedDO = getMannschaftsmitgliedDO();
        final DsbMannschaftDO dsbMannschaftDO = getDsbMannschaftDO();

        // configure mocks
        when(dsbMannschaftComponent.findById(anyLong())).thenReturn(dsbMannschaftDO);
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(false);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(any(), anyLong())).thenReturn(true);
        when(mannschaftsmitgliedComponent.update(any(MannschaftsmitgliedDO.class), anyLong())).thenReturn(expectedDO);

        // call test method
        try {
            final MannschaftsMitgliedDTO actual = underTest.update(input, principal);

            // assert result
            assertThat(actual).isNotNull();
            assertThat(actual.getMannschaftsId()).isEqualTo(input.getMannschaftsId());

            // verify invocations
            verify(mannschaftsmitgliedComponent).update(mannschaftsmitgliedVOArgumentCaptor.capture(), anyLong());

            final MannschaftsmitgliedDO persistedDO = mannschaftsmitgliedVOArgumentCaptor.getValue();

            assertThat(persistedDO).isNotNull();
            assertThat(persistedDO.getMannschaftId()).isEqualTo(input.getMannschaftsId());

        } catch (NoPermissionException | NullPointerException e) { }
    }


    @Test
    public void updateNoPermission() {
        // prepare test data
        final MannschaftsMitgliedDTO input = getMannschaftsmitgliedDTO();
        final MannschaftsmitgliedDO expectedDO = getMannschaftsmitgliedDO();
        final DsbMannschaftDO dsbMannschaftDO = getDsbMannschaftDO();

        // configure mocks
        when(dsbMannschaftComponent.findById(anyLong())).thenReturn(dsbMannschaftDO);
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(false);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(any(), anyLong())).thenReturn(false);
        when(mannschaftsmitgliedComponent.update(any(MannschaftsmitgliedDO.class), anyLong())).thenReturn(expectedDO);

        assertThatExceptionOfType(NoPermissionException.class)
                .isThrownBy(()-> underTest.create(input, principal));
    }


    @Test
    public void create() {
        // prepare test data
        final MannschaftsMitgliedDTO input = getMannschaftsmitgliedDTO();
        final MannschaftsmitgliedDO expected = getMannschaftsmitgliedDO();
        final DsbMannschaftDO dsbMannschaftDO = getDsbMannschaftDO();

        // configure mocks
        when(dsbMannschaftComponent.findById(anyLong())).thenReturn(dsbMannschaftDO);
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(true);
        when(mannschaftsmitgliedComponent.create(any(), anyLong())).thenReturn(expected);

        try {
            // call test method
            final MannschaftsMitgliedDTO actual = underTest.create(input, principal);

            // assert result
            assertThat(actual).isNotNull();
            assertThat(actual.getMannschaftsId()).isEqualTo(input.getMannschaftsId());
            assertThat(actual.getDsbMitgliedId()).isEqualTo(input.getDsbMitgliedId());

            // verify invocations
            verify(mannschaftsmitgliedComponent).create(mannschaftsmitgliedVOArgumentCaptor.capture(), anyLong());

            final MannschaftsmitgliedDO createdDsbMannschaft = mannschaftsmitgliedVOArgumentCaptor.getValue();

            assertThat(createdDsbMannschaft).isNotNull();
            assertThat(createdDsbMannschaft.getMannschaftId()).isEqualTo(input.getMannschaftsId());
            assertThat(createdDsbMannschaft.getDsbMitgliedId()).isEqualTo(input.getDsbMitgliedId());


        } catch (NoPermissionException | NullPointerException e) { }
    }


    @Test
    public void createOnlyDataSepcificPermission() {
        // prepare test data
        final MannschaftsMitgliedDTO input = getMannschaftsmitgliedDTO();
        final MannschaftsmitgliedDO expected = getMannschaftsmitgliedDO();
        final DsbMannschaftDO dsbMannschaftDO = getDsbMannschaftDO();

        // configure mocks
        when(dsbMannschaftComponent.findById(anyLong())).thenReturn(dsbMannschaftDO);
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(false);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(any(), anyLong())).thenReturn(true);
        when(mannschaftsmitgliedComponent.create(any(), anyLong())).thenReturn(expected);

        try {
            // call test method
            final MannschaftsMitgliedDTO actual = underTest.create(input, principal);

            // assert result
            assertThat(actual).isNotNull();
            assertThat(actual.getMannschaftsId()).isEqualTo(input.getMannschaftsId());
            assertThat(actual.getDsbMitgliedId()).isEqualTo(input.getDsbMitgliedId());

            // verify invocations
            verify(mannschaftsmitgliedComponent).create(mannschaftsmitgliedVOArgumentCaptor.capture(), anyLong());

            final MannschaftsmitgliedDO createdDsbMannschaft = mannschaftsmitgliedVOArgumentCaptor.getValue();

            assertThat(createdDsbMannschaft).isNotNull();
            assertThat(createdDsbMannschaft.getMannschaftId()).isEqualTo(input.getMannschaftsId());
            assertThat(createdDsbMannschaft.getDsbMitgliedId()).isEqualTo(input.getDsbMitgliedId());

        } catch (NoPermissionException | NullPointerException e) { }
    }


    @Test
    public void createNoPermission() {
        // prepare test data
        final MannschaftsMitgliedDTO input = getMannschaftsmitgliedDTO();
        final MannschaftsmitgliedDO expected = getMannschaftsmitgliedDO();
        final DsbMannschaftDO dsbMannschaftDO = getDsbMannschaftDO();

        // configure mocks
        when(dsbMannschaftComponent.findById(anyLong())).thenReturn(dsbMannschaftDO);
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(false);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(any(), anyLong())).thenReturn(false);
        when(mannschaftsmitgliedComponent.create(any(), anyLong())).thenReturn(expected);

        assertThatExceptionOfType(NoPermissionException.class)
           .isThrownBy(()-> underTest.create(input, principal));
    }


    @Test
    public void delete() {
        // prepare test data
        final MannschaftsmitgliedDO expected = getMannschaftsmitgliedDO();

        // configure mocks
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(true);
        doNothing().when(mannschaftsmitgliedComponent).delete(any(), anyLong());

        /* call test method */
        try {
            underTest.delete(mannschaftsId, principal);

            // verify invocations
            verify(mannschaftsmitgliedComponent).delete(mannschaftsmitgliedVOArgumentCaptor.capture(), anyLong());

        } catch (NullPointerException e) { }
    }


    @Test
    public void deleteByTeamMember() {
        // prepare test data
        final MannschaftsmitgliedDO expected = getMannschaftsmitgliedDO();

        // configure mocks
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(true);
        doNothing().when(mannschaftsmitgliedComponent).deleteByTeamIdAndMemberId(any(), anyLong());

        // call test method
        try {
            underTest.deleteByTeamIdAndMemberId(mannschaftsId, dsbMitgliedId, principal);

            // verify invocations
            verify(mannschaftsmitgliedComponent).deleteByTeamIdAndMemberId(mannschaftsmitgliedVOArgumentCaptor.capture(), anyLong());

            final MannschaftsmitgliedDO deletedDsbMitglied = mannschaftsmitgliedVOArgumentCaptor.getValue();

            assertThat(deletedDsbMitglied).isNotNull();
            assertThat(deletedDsbMitglied.getMannschaftId()).isEqualTo(expected.getMannschaftId());
            assertThat(deletedDsbMitglied.getDsbMitgliedId()).isEqualTo(expected.getDsbMitgliedId());

        } catch (NoPermissionException | NullPointerException e) { }
    }


    @Test
    public void deleteByTeamMemberOnlyDataSpecificPermission() {
        // prepare test data
        final MannschaftsmitgliedDO expected = getMannschaftsmitgliedDO();

        // configure mocks
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(false);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(any(), anyLong())).thenReturn(true);

        // call test method
        try {
            underTest.deleteByTeamIdAndMemberId(mannschaftsId, dsbMitgliedId, principal);

            // verify invocations
            verify(mannschaftsmitgliedComponent).deleteByTeamIdAndMemberId(mannschaftsmitgliedVOArgumentCaptor.capture(), anyLong());

            final MannschaftsmitgliedDO deletedDsbMitglied = mannschaftsmitgliedVOArgumentCaptor.getValue();

            assertThat(deletedDsbMitglied).isNotNull();
            assertThat(deletedDsbMitglied.getMannschaftId()).isEqualTo(expected.getMannschaftId());
            assertThat(deletedDsbMitglied.getDsbMitgliedId()).isEqualTo(expected.getDsbMitgliedId());

        } catch (NoPermissionException | NullPointerException e) { }
    }


    @Test
    public void deleteByTeamMemberNoPermission() {
        // prepare test data
        final MannschaftsmitgliedDO expected = getMannschaftsmitgliedDO();

        // configure mocks
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(false);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(any(), anyLong())).thenReturn(false);

        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(()-> underTest.deleteByTeamIdAndMemberId(mannschaftsId, dsbMitgliedId, principal));
     }
}