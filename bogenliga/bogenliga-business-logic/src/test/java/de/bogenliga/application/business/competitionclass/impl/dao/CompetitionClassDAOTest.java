package de.bogenliga.application.business.competitionclass.impl.dao;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.competitionclass.impl.entity.CompetitionClassBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static de.bogenliga.application.business.competitionclass.impl.business.CompetitionClassComponentImplTest.getCompetitionClassBE;


import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;



/**
 * @author Leila Taraman, Leila.Taraman@Student.Reutlingen-University.DE
 */
public class CompetitionClassDAOTest {

    private static final long USER = 0L;

    private static final long ID = 42L;
    private static final String KLASSENAME = "Herren";
    private static final long KLASSEJAHRGANGMIN = 10L;
    private static final long KLASSEJAHRGANGMAX = 50L;
    private static final long KLASSENR = 1337L;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private BasicDAO basicDAO;

    @InjectMocks
    private CompetitionClassDAO competitionClassDAO;



    public void create(){

        // prepare test data
        final CompetitionClassBE input = new CompetitionClassBE();
        input.setKlasseId(ID);
        input.setKlasseName(KLASSENAME);

        // configure mocks
        when(basicDAO.insertEntity(any(), any())).thenReturn(input);

        // call test method
        final CompetitionClassBE actual = competitionClassDAO.create(input, USER);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getKlasseId()).isEqualTo(input.getKlasseId());
        assertThat(actual.getKlasseName()).isEqualTo(input.getKlasseName());

        // verify invocations
        verify(basicDAO).insertEntity(any(), eq(input));


    }

    @Test
    public void update(){

        // prepare test data
        final CompetitionClassBE input = new CompetitionClassBE();
        input.setKlasseId(ID);
        input.setKlasseName(KLASSENAME);

        // configure mocks
        when(basicDAO.updateEntity(any(), any(), any())).thenReturn(input);

        // call test method
        final CompetitionClassBE actual = competitionClassDAO.update(input, USER);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getKlasseId()).isEqualTo(input.getKlasseId());
        assertThat(actual.getKlasseName()).isEqualTo(input.getKlasseName());

        // verify invocations
        verify(basicDAO).updateEntity(any(), eq(input), any());

    }

    @Test
    public void findAll(){

        // prepare test data
        final CompetitionClassBE expectedBE = getCompetitionClassBE();

        // configure mocks
        when(basicDAO.selectEntityList(any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<CompetitionClassBE> actual = competitionClassDAO.findAll();

        // assert result
        assertThat(actual).isNotNull().isNotEmpty().hasSize(1);
        assertThat(actual.get(0)).isNotNull();
        assertThat(actual.get(0).getKlasseId()).isEqualTo(expectedBE.getKlasseId());
        assertThat(actual.get(0).getKlasseName()).isEqualTo(expectedBE.getKlasseName());

        // verify invocations
        verify(basicDAO).selectEntityList(any(), any(), any());

    }

}