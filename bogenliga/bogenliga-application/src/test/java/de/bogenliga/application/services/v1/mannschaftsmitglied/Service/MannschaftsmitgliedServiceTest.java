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
    private static final Long ID = 1L;
    private static final Long MANNSCHAFTS_ID = 1L;
    private static final Long DSB_MITGLIED_ID = 100L;
    private static final Integer DSB_MITGLIED_EINGESETZT = 1;
    private static final String DSB_MITGLIED_VORNAME = "Mario";
    private static final String DSB_MITGLIED_NACHNAME = "Gomez";
    private static final Long RUECKENNUMMER = 5L;
    private static final Long WETTKAMP_ID = 30L;
    private static final Long PLATZHALTER_MITGLIED_ID = 1L;
    private static final Long PLATZHALTER_ID = 6969L;
    private static final Long DSB_MITGLIED_PLATZHALTER_ID = 1L;
    private static final String DSB_MITGLIED_PLATZHALTER_VORNAME = "PlatzhalterVorname1";
    private static final String DSB_MITGLIED_PLATZHALTER_NACHNAME = "PlatzhalterNachname1";
    private static final Long RUECKENNUMMER_PLATZHALTER = 1L;

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
                MANNSCHAFTS_ID, "die Mannschaft", ID, 23L,
                ID, ID, 2L, 1L
        );
    }

    public static MannschaftsmitgliedDO getMannschaftsmitgliedDO() {
        return new MannschaftsmitgliedDO(
                ID, MANNSCHAFTS_ID, DSB_MITGLIED_ID, DSB_MITGLIED_EINGESETZT, DSB_MITGLIED_VORNAME,
                DSB_MITGLIED_NACHNAME, RUECKENNUMMER
        );
    }

    public static MannschaftsmitgliedDO getMannschaftsmitgliedPlatzhalterDO() {
        return new MannschaftsmitgliedDO(
                PLATZHALTER_MITGLIED_ID, PLATZHALTER_ID, DSB_MITGLIED_PLATZHALTER_ID, 1,
                DSB_MITGLIED_PLATZHALTER_VORNAME, DSB_MITGLIED_PLATZHALTER_NACHNAME, RUECKENNUMMER_PLATZHALTER
        );
    }

    public static MannschaftsMitgliedDTO getMannschaftsmitgliedPlatzhalterDTO() {
        return new MannschaftsMitgliedDTO(
                PLATZHALTER_MITGLIED_ID, PLATZHALTER_ID, DSB_MITGLIED_PLATZHALTER_ID, 1,
                RUECKENNUMMER_PLATZHALTER
        );
    }


    public static MannschaftsMitgliedDTO getMannschaftsmitgliedDTO() {
        final MannschaftsMitgliedDTO mannschaftsMitgliedDTO = new MannschaftsMitgliedDTO();
        mannschaftsMitgliedDTO.setMannschaftsId(MANNSCHAFTS_ID);
        mannschaftsMitgliedDTO.setDsbMitgliedId(DSB_MITGLIED_ID);
        mannschaftsMitgliedDTO.setDsbMitgliedEingesetzt(DSB_MITGLIED_EINGESETZT);
        mannschaftsMitgliedDTO.setRueckennummer(RUECKENNUMMER);
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
        final List<MannschaftsMitgliedDTO> actual = underTest.findByTeamId(MANNSCHAFTS_ID);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.get(0).getMannschaftsId()).isEqualTo(mannschaftsmitgliedDO.getMannschaftId());

        // verify invocations
        verify(mannschaftsmitgliedComponent).findByTeamId(MANNSCHAFTS_ID);
    }


    @Test
    public void findByMemberAndTeamId() {
        // prepare test data
        final MannschaftsmitgliedDO mannschaftsmitgliedDO = getMannschaftsmitgliedDO();

        // configure mocks
        when(mannschaftsmitgliedComponent.findByMemberAndTeamId(MANNSCHAFTS_ID, DSB_MITGLIED_ID)).thenReturn(
                mannschaftsmitgliedDO);

        final MannschaftsMitgliedDTO actual = underTest.findByMemberAndTeamId(MANNSCHAFTS_ID, DSB_MITGLIED_ID);

        assertThat(actual).isNotNull();
        assertThat(actual.getMannschaftsId()).isEqualTo(actual.getMannschaftsId());
        assertThat(actual.getDsbMitgliedId()).isEqualTo(actual.getDsbMitgliedId());
    }

    @Test
    public void findByTeamIdAndRueckennummer() {
        // prepare test data
        final MannschaftsmitgliedDO mannschaftsmitgliedDO = getMannschaftsmitgliedDO();

        // configure mocks
        when(mannschaftsmitgliedComponent.findByTeamIdAndRueckennummer(MANNSCHAFTS_ID, DSB_MITGLIED_ID)).thenReturn(
                mannschaftsmitgliedDO);

        final MannschaftsMitgliedDTO actual = underTest.findByTeamIdAndRueckennummer(MANNSCHAFTS_ID, DSB_MITGLIED_ID);

        assertThat(actual).isNotNull();
        assertThat(actual.getMannschaftsId()).isEqualTo(actual.getMannschaftsId());
        assertThat(actual.getDsbMitgliedId()).isEqualTo(actual.getDsbMitgliedId());
    }

    @Test
    public void findByTeamIdAndRueckennummerThrowsException() {
        // prepare test data
        final MannschaftsmitgliedDO mannschaftsmitgliedDO = getMannschaftsmitgliedDO();

        // configure mocks
        when(mannschaftsmitgliedComponent.findByTeamIdAndRueckennummer(MANNSCHAFTS_ID, DSB_MITGLIED_ID)).thenReturn(
                mannschaftsmitgliedDO);

        assertThatThrownBy(()->{
            underTest.findByTeamIdAndRueckennummer(0, DSB_MITGLIED_ID);
        }).isInstanceOf(BusinessException.class);

        assertThatThrownBy(()->{
            underTest.findByTeamIdAndRueckennummer(-1, DSB_MITGLIED_ID);
        }).isInstanceOf(BusinessException.class);

        assertThatThrownBy(()->{
            underTest.findByTeamIdAndRueckennummer(MANNSCHAFTS_ID, 0);
        }).isInstanceOf(BusinessException.class);

        assertThatThrownBy(()->{
            underTest.findByTeamIdAndRueckennummer(MANNSCHAFTS_ID, 0);
        }).isInstanceOf(BusinessException.class);
    }

    @Test
    public void findSchuetzenInUebergelegenerLiga(){
        final MannschaftsmitgliedDO mannschaftsmitgliedDO = getMannschaftsmitgliedDO();
        final List<MannschaftsmitgliedDO> mannschaftsmitgliedDOList = Collections.singletonList(mannschaftsmitgliedDO);

        // configure mocks
        when(mannschaftsmitgliedComponent.findSchuetzenInUebergelegenerLiga(MANNSCHAFTS_ID, WETTKAMP_ID)).thenReturn(mannschaftsmitgliedDOList);
        final List<MannschaftsMitgliedDTO> actual = underTest.findSchuetzenInUebergelegenerLiga(MANNSCHAFTS_ID,
                WETTKAMP_ID);

        assertThat(actual.get(0).getDsbMitgliedId()).isEqualTo(mannschaftsmitgliedDOList.get(0).getDsbMitgliedId());
    }

    @Test
    public void findAllSchuetzeInTeam() {
        // prepare test data
        final MannschaftsmitgliedDO mannschaftsmitgliedDO = getMannschaftsmitgliedDO();
        final List<MannschaftsmitgliedDO> mannschaftsmitgliedDOList = Collections.singletonList(mannschaftsmitgliedDO);

        // configure mocks
        when(mannschaftsmitgliedComponent.findAllSchuetzeInTeamEingesetzt(MANNSCHAFTS_ID)).thenReturn(mannschaftsmitgliedDOList);

        // call test method
        final List<MannschaftsMitgliedDTO> actual = underTest.findAllSchuetzeInTeam(MANNSCHAFTS_ID);

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
        final List<MannschaftsMitgliedDTO> actual = underTest.findByMemberId(DSB_MITGLIED_ID);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.get(0).getDsbMitgliedId()).isEqualTo(mannschaftsmitgliedDO.getDsbMitgliedId());

        // verify invocations
        verify(mannschaftsmitgliedComponent).findByMemberId(DSB_MITGLIED_ID);
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
            underTest.delete(MANNSCHAFTS_ID, principal);

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
            underTest.deleteByTeamIdAndMemberId(MANNSCHAFTS_ID, DSB_MITGLIED_ID, principal);

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
            underTest.deleteByTeamIdAndMemberId(MANNSCHAFTS_ID, DSB_MITGLIED_ID, principal);

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
                .isThrownBy(()-> underTest.deleteByTeamIdAndMemberId(MANNSCHAFTS_ID, DSB_MITGLIED_ID, principal));
     }
}