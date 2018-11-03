package de.bogenliga.application.services.v1.competitionclass.service;

import java.security.Principal;
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

/**
 * Test class to test the CompetitionClassService
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
public class CompetitionClassServiceTest {

    private static final long USER = 0;

    private static final long ID = 42;
    private static final String KLASSENAME = "Herren";
    private static final long KLASSEALTERMIN = 10;
    private static final long KLASSEALTERMAX = 50;
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
        assertEquals(input.getKlasseAlterMin(), result.getKlasseAlterMin());
        assertEquals(input.getKlasseNr(), result.getKlasseNr());

        // verify invocations
        verify(competitionClassComponent).create(competitionClassDOArgumentCaptor.capture(), anyLong());

        final CompetitionClassDO createdCompetitionClass = competitionClassDOArgumentCaptor.getValue();

        assertNotNull(createdCompetitionClass);
        assertEquals(input.getId(), createdCompetitionClass.getId());
        assertEquals(input.getKlasseName(), createdCompetitionClass.getKlasseName());
        assertEquals(input.getKlasseAlterMin(), createdCompetitionClass.getKlasseAlterMin());
        assertEquals(input.getKlasseNr(), createdCompetitionClass.getKlasseNr());
    }
}