package de.bogenliga.application.services.v1.mannschaftsmitglied.Service;

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
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.entity.MannschaftsmitgliedBE;
import de.bogenliga.application.services.v1.mannschaftsmitglied.model.MannschaftsMitgliedDTO;
import de.bogenliga.application.services.v1.mannschaftsmitglied.service.MannschaftsMitgliedService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class MannschaftsmitgliedServiceTest {

    private static final Long USER = 0L;
    private static Long id = 1L;
    private static Long mannschaftsId = 1L;
    private static Long dsbMitgliedId = 100L;
    private static Integer dsbMitgliedEingesetzt = 1;
    private static String dsbMitgliedVorname = "Mario";
    private static String dsbMitgliedNachname = "Gomez";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private MannschaftsmitgliedComponent mannschaftsmitgliedComponent;

    @Mock
    private Principal principal;

    @InjectMocks
    private MannschaftsMitgliedService underTest;

    @Captor
    private ArgumentCaptor<MannschaftsmitgliedDO> mannschaftsmitgliedVOArgumentCaptor;


    public static MannschaftsmitgliedBE getMannschaftsmitgliedBE() {
        final MannschaftsmitgliedBE expectedBE = new MannschaftsmitgliedBE();
        expectedBE.setMannschaftId(mannschaftsId);
        expectedBE.setDsbMitgliedId(dsbMitgliedId);
        expectedBE.setDsbMitgliedEingesetzt(dsbMitgliedEingesetzt);
        return expectedBE;
    }


    public static MannschaftsmitgliedDO getMannschaftsmitgliedDO() {
        return new MannschaftsmitgliedDO(
                id, mannschaftsId, dsbMitgliedId, dsbMitgliedEingesetzt, dsbMitgliedVorname, dsbMitgliedNachname
        );
    }


    public static MannschaftsMitgliedDTO getMannschaftsmitgliedDTO() {
        final MannschaftsMitgliedDTO mannschaftsMitgliedDTO = new MannschaftsMitgliedDTO();
        mannschaftsMitgliedDTO.setMannschaftsId(mannschaftsId);
        mannschaftsMitgliedDTO.setDsbMitgliedId(dsbMitgliedId);
        mannschaftsMitgliedDTO.setDsbMitgliedEingesetzt(dsbMitgliedEingesetzt);
        return mannschaftsMitgliedDTO;
    }


    @Before
    public void initMocks() {
        when(principal.getName()).thenReturn(String.valueOf(USER));
    }


    @Test
    public void findAll() {
        final MannschaftsmitgliedDO mannschaftsmitgliedDO = getMannschaftsmitgliedDO();
        final List<MannschaftsmitgliedDO> mannschaftsmitgliedDOList = Collections.singletonList(mannschaftsmitgliedDO);
        //configure Mocks
        when(mannschaftsmitgliedComponent.findAll()).thenReturn(mannschaftsmitgliedDOList);
        // call test method
        final List<MannschaftsMitgliedDTO> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final MannschaftsMitgliedDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getMannschaftsId()).isEqualTo(mannschaftsmitgliedDO.getMannschaftId());
        //assertThat(actualDTO.getVereinId()).isEqualTo(dsbMannschaftDO.getVereinId());

        // verify invocations
        verify(mannschaftsmitgliedComponent).findAll();

    }


    @Test
    public void findByTeamId() {
        // prepare test data
        final MannschaftsmitgliedDO mannschaftsmitgliedDO = getMannschaftsmitgliedDO();
        final List<MannschaftsmitgliedDO> mannschaftsmitgliedDOList = Collections.singletonList(mannschaftsmitgliedDO);

        //configure Mocks
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
    public void findAllSchuetzeInTeam() {

        final MannschaftsmitgliedDO mannschaftsmitgliedDO = getMannschaftsmitgliedDO();
        final List<MannschaftsmitgliedDO> mannschaftsmitgliedDOList = Collections.singletonList(mannschaftsmitgliedDO);

        // configure mocks
        when(mannschaftsmitgliedComponent.findAllSchuetzeInTeam(mannschaftsId)).thenReturn(mannschaftsmitgliedDOList);

        // call test method
        final List<MannschaftsMitgliedDTO> actual = underTest.findAllSchuetzeInTeam(mannschaftsId);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        assertThat(actual.get(0).getMannschaftsId()).isEqualTo(mannschaftsmitgliedDO.getMannschaftId());
        assertThat(actual.get(0).getDsbMitgliedId()).isEqualTo(mannschaftsmitgliedDO.getDsbMitgliedId());
        assertThat(actual.get(0).getDsbMitgliedEingesetzt()).isEqualTo(mannschaftsmitgliedDO.getDsbMitgliedEingesetzt());
    }


    @Test
    public void findByMemberAndTeamId() {
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
    public void update() {
        final MannschaftsMitgliedDTO input = getMannschaftsmitgliedDTO();

        final MannschaftsmitgliedDO expectedDO = getMannschaftsmitgliedDO();

        // configure mocks
        when(mannschaftsmitgliedComponent.update(any(MannschaftsmitgliedDO.class), anyLong())).thenReturn(expectedDO);

        // call test method
        final MannschaftsMitgliedDTO actual = underTest.update(input, principal);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getMannschaftsId())
                .isEqualTo(input.getMannschaftsId());

        // verify invocations
        verify(mannschaftsmitgliedComponent).update(mannschaftsmitgliedVOArgumentCaptor.capture(), anyLong());

        final MannschaftsmitgliedDO persistedDO = mannschaftsmitgliedVOArgumentCaptor.getValue();

        assertThat(persistedDO).isNotNull();

        assertThat(persistedDO.getMannschaftId())
                .isEqualTo(input.getMannschaftsId());
    }


    @Test
    public void create() {
        // prepare test data
        final MannschaftsMitgliedDTO input = getMannschaftsmitgliedDTO();

        final MannschaftsmitgliedDO expected = getMannschaftsmitgliedDO();

        // configure mocks
        when(mannschaftsmitgliedComponent.create(any(), anyLong())).thenReturn(expected);

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
    }


    // @Test
    // public void checkExistingSchuetze() {
    //     final MannschaftsmitgliedDO mannschaftsmitgliedDO = getMannschaftsmitgliedDO();
//
    //     // call test method
    //     final boolean actual = underTest.checkExistingSchuetze(mannschaftsId, dsbMitgliedId, principal);
//
    //     // assert result
    //     assertThat(actual)
    //             .isNotNull();
//
    //     final MannschaftsMitgliedDTO actualDTO = getMannschaftsmitgliedDTO();
//
    //     assertThat(actualDTO).isNotNull();
    //     assertThat(actualDTO.isDsbMitgliedEingesetzt()).isEqualTo(mannschaftsmitgliedDO.getDsbMitgliedEingesetzt());
    //     assertThat(actualDTO.isDsbMitgliedEingesetzt()).isEqualTo(true);
    // }


    @Test
    public void delete() {
        // prepare test data
        final MannschaftsmitgliedDO expected = getMannschaftsmitgliedDO();

        // call test method
        underTest.deleteByTeamIdAndMemberId(mannschaftsId, dsbMitgliedId, principal);

        // verify invocations
        verify(mannschaftsmitgliedComponent).deleteByTeamIdAndMemberId(mannschaftsmitgliedVOArgumentCaptor.capture(), anyLong());

        final MannschaftsmitgliedDO deletedDsbMitglied = mannschaftsmitgliedVOArgumentCaptor.getValue();

        assertThat(deletedDsbMitglied).isNotNull();
        assertThat(deletedDsbMitglied.getMannschaftId()).isEqualTo(expected.getMannschaftId());
        assertThat(deletedDsbMitglied.getDsbMitgliedId()).isEqualTo(expected.getDsbMitgliedId());
    }
}