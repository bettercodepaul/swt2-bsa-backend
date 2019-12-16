package de.bogenliga.application.services.v1.competitionclass.service;

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
import de.bogenliga.application.business.competitionclass.api.CompetitionClassComponent;
import de.bogenliga.application.business.competitionclass.api.types.CompetitionClassDO;
import de.bogenliga.application.services.v1.competitionclass.model.CompetitionClassDTO;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class to test the CompetitionClassService
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
public class CompetitionClassServiceTest {

    private static final long USER = 0;

    private static final long ID = 42;
    private static final String KLASSENAME = "Herren";
    private static final long KLASSEALTERMIN = 1994;
    private static final long KLASSEALTERMAX = 1996;
    private static final long KLASSENR = 1337;


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private Principal principal;

    @Mock
    private CompetitionClassComponent competitionClassComponent;

    @InjectMocks
    private CompetitionClassService competitionClassService;

    @Captor
    private ArgumentCaptor<CompetitionClassDO> competitionClassDOArgumentCaptor;


    public static CompetitionClassDTO getCompetitionClassDTO() {
        return new CompetitionClassDTO(
                ID,
                KLASSENAME,
                KLASSEALTERMIN,
                KLASSEALTERMAX,
                KLASSENR
        );
    }


    public static CompetitionClassDO getCompetitionClassDO() {
        return new CompetitionClassDO(
                ID,
                KLASSENAME,
                KLASSEALTERMIN,
                KLASSEALTERMAX,
                KLASSENR
        );
    }


    @Before
    public void initMocks() {
        when(principal.getName()).thenReturn(String.valueOf(USER));
    }


    @Test
    public void create() {
        // Given
        final CompetitionClassDTO input = getCompetitionClassDTO();
        final CompetitionClassDO expected = getCompetitionClassDO();

        when(competitionClassComponent.create(any(), anyLong())).thenReturn(expected);

        // when
        final CompetitionClassDTO result = competitionClassService.create(input, principal);

        // then
        assertNotNull(result);
        assertEquals(input.getId(), result.getId());
        assertEquals(input.getKlasseName(), result.getKlasseName());
        assertEquals(input.getKlasseNr(), result.getKlasseNr());

        // verify invocations
        verify(competitionClassComponent).create(competitionClassDOArgumentCaptor.capture(), anyLong());

        final CompetitionClassDO createdCompetitionClass = competitionClassDOArgumentCaptor.getValue();

        assertNotNull(createdCompetitionClass);
        assertEquals(input.getId(), createdCompetitionClass.getId());
        assertEquals(input.getKlasseName(), createdCompetitionClass.getKlasseName());
        assertEquals(input.getKlasseJahrgangMin(), createdCompetitionClass.getKlasseJahrgangMin());
        assertEquals(input.getKlasseNr(), createdCompetitionClass.getKlasseNr());

        assertTrue(createdCompetitionClass.getKlasseJahrgangMin() >= createdCompetitionClass.getKlasseJahrgangMax());
    }


    @Test
    public void findAll() {
        // given
        final CompetitionClassDO competitionClassDO = getCompetitionClassDO();
        final List<CompetitionClassDO> competitionClassDOList = Collections.singletonList(competitionClassDO);

        when(competitionClassComponent.findAll()).thenReturn(competitionClassDOList);

        // when
        final List<CompetitionClassDTO> result = competitionClassService.findAll();

        // then
        final CompetitionClassDTO resultDTO = result.get(0);

        assertNotNull(resultDTO);
        assertEquals(competitionClassDO.getId(), resultDTO.getId());
        assertEquals(competitionClassDO.getKlasseName(), resultDTO.getKlasseName());
        assertEquals(competitionClassDO.getKlasseJahrgangMax(), resultDTO.getKlasseJahrgangMax());
        assertEquals(competitionClassDO.getKlasseJahrgangMin(), resultDTO.getKlasseJahrgangMin());
        assertEquals(competitionClassDO.getKlasseNr(), resultDTO.getKlasseNr());
    }


    @Test
    public void update() {
        // given
        final CompetitionClassDTO input = getCompetitionClassDTO();
        final CompetitionClassDO expected = getCompetitionClassDO();

        when(competitionClassComponent.update(any(), anyLong())).thenReturn(expected);

        // when
        final CompetitionClassDTO result = competitionClassService.update(input, principal);

        // then
        assertNotNull(result);
        assertEquals(input.getId(), result.getId());
        assertEquals(input.getKlasseName(), result.getKlasseName());
        assertEquals(input.getKlasseNr(), result.getKlasseNr());

        verify(competitionClassComponent).update(competitionClassDOArgumentCaptor.capture(), anyLong());

        final CompetitionClassDO createdCompetitionClassDO = competitionClassDOArgumentCaptor.getValue();

        assertNotNull(createdCompetitionClassDO);
        assertEquals(input.getId(), createdCompetitionClassDO.getId());
        assertEquals(input.getKlasseName(), createdCompetitionClassDO.getKlasseName());
        assertEquals(input.getKlasseJahrgangMin(), createdCompetitionClassDO.getKlasseJahrgangMin());
        assertEquals(input.getKlasseNr(), createdCompetitionClassDO.getKlasseNr());

        assertTrue(createdCompetitionClassDO.getKlasseJahrgangMin() >= createdCompetitionClassDO.getKlasseJahrgangMax());
    }

    @Test
    public void findById(){
        //prepare test data
        final CompetitionClassDO competitionClassDO = getCompetitionClassDO();

        //configure Mocks
        when(competitionClassComponent.findById(anyLong())).thenReturn(competitionClassDO);

        //call test method
        final CompetitionClassDTO actual = competitionClassService.findById(ID);

        //assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(competitionClassDO.getId());
        assertThat(actual.getKlasseName()).isEqualTo(competitionClassDO.getKlasseName());

        //verify invocations
        verify(competitionClassComponent).findById(ID);
    }

}
