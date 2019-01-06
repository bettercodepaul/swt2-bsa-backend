package de.bogenliga.application.business.competitionClass.impl.business;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.Collections;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.competitionclass.api.types.CompetitionClassDO;
import de.bogenliga.application.business.competitionclass.impl.business.CompetitionClassComponentImpl;
import de.bogenliga.application.business.competitionclass.impl.dao.CompetitionClassDAO;
import de.bogenliga.application.business.competitionclass.impl.entity.CompetitionClassBE;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Java6Assertions.assertThat;




/**
 * @author Dennis Goericke, dennis.goericke@student.reutlingen-university.de
 */

public class CompetitionClassComponentImplTest {

    private static final Long USER = 0L;
    private static final Long VERSION = 0L;

    private static final Long ID = 1337L;
    private static final String NAME ="Herren";
    private static final Long JAHRGANG_MIN = 1998L;
    private static final Long JAHRGANG_MAX = 1996L;
    private static final Long NUMBER = 2L;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private CompetitionClassDAO competitionClassDAO;
    @InjectMocks
    private CompetitionClassComponentImpl underTest;
    @Captor
    private ArgumentCaptor<CompetitionClassBE> competitionClassBEArgumentCaptor;


    /**
     * Utlity methods for creating business entities/data objects
     */
    public static CompetitionClassBE getCompetitionClassBE()
    {
        final CompetitionClassBE expectedBE = new CompetitionClassBE();
        expectedBE.setKlasseId(ID);
        expectedBE.setKlasseName(NAME);
        expectedBE.setKlasseAlterMin(44L);
        expectedBE.setKlasseAlterMax(66L);
        expectedBE.setKlasseNr(NUMBER);
        return expectedBE;
    }


    public static CompetitionClassDO getCompetitionClassDO()
    {
        return new CompetitionClassDO(
                ID,
                NAME,
                JAHRGANG_MIN,
                JAHRGANG_MAX,
                NUMBER);

    }

    @Test
    public void findAll()
    {
        //prepare test data
        final CompetitionClassBE expectedBE = getCompetitionClassBE();
        final List<CompetitionClassBE> epxectedBEList = Collections.singletonList(expectedBE);

        //configure mocks
        when(competitionClassDAO.findAll()).thenReturn(epxectedBEList);

        //call test method
        final List<CompetitionClassDO> actual = underTest.findAll();

        //assert Result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getId())
                .isEqualTo(expectedBE.getKlasseId());
        assertThat(actual.get(0).getKlasseName())
                .isEqualTo(expectedBE.getKlasseName());
        assertThat(actual.get(0).getKlasseJahrgangMax())
                .isEqualTo(expectedBE.getKlasseAlterMax());
        assertThat(actual.get(0).getKlasseJahrgangMin())
                .isEqualTo(expectedBE.getKlasseAlterMin());
        assertThat(actual.get(0).getKlasseNr())
                .isEqualTo(expectedBE.getKlasseNr());


        //verify invocations
        verify(competitionClassDAO).findAll();


    }


    @Test
    public void create()
    {
        //prepare test data
        final CompetitionClassDO input = getCompetitionClassDO();
        final CompetitionClassBE expectedBE = getCompetitionClassBE();

        //configure mocks
        when(competitionClassDAO.create(any(CompetitionClassBE.class), anyLong())).thenReturn(expectedBE);

        //call test method
        final CompetitionClassDO actual = underTest.create(input,USER);

        //assert Result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(expectedBE.getKlasseId());

        //verify invocations
        verify(competitionClassDAO).create(competitionClassBEArgumentCaptor.capture(), anyLong());

        final CompetitionClassBE persistedBE = competitionClassBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getKlasseId())
                .isEqualTo(input.getId());
    }

    @Test
    public void create_with_mandatory_parameters()
    {
        //prepare test data
        final OffsetDateTime dateTime = OffsetDateTime.now();
        final OffsetDateTime timestamp = OffsetDateTime.now();
        final Timestamp lastModified = new Timestamp(System.currentTimeMillis());
        final Timestamp expectedTimestamp = new Timestamp(System.currentTimeMillis());
        final CompetitionClassDO input = new CompetitionClassDO(
                ID,
                NAME,
                JAHRGANG_MIN,
                JAHRGANG_MAX,
                NUMBER,
                dateTime,
                USER,
                timestamp,
                USER,
                VERSION
        );

        final CompetitionClassBE expectedBE = new CompetitionClassBE();
        expectedBE.setKlasseId(ID);
        expectedBE.setKlasseNr(NUMBER);
        expectedBE.setKlasseAlterMax(JAHRGANG_MAX);
        expectedBE.setKlasseAlterMin(JAHRGANG_MIN);
        expectedBE.setKlasseName(NAME);
        expectedBE.setCreatedAtUtc(expectedTimestamp);
        expectedBE.setCreatedByUserId(USER);
        expectedBE.setVersion(VERSION);
        expectedBE.setLastModifiedAtUtc(lastModified);

        //configure mocks
        when(competitionClassDAO.create(any(CompetitionClassBE.class), anyLong())).thenReturn(expectedBE);

        //call test method
        final CompetitionClassDO actual = underTest.create(input, USER);

        //assert Result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(input.getId());

        //verify invocations
        verify(competitionClassDAO).create(competitionClassBEArgumentCaptor.capture(),anyLong());

        final CompetitionClassBE persistedBE = competitionClassBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getKlasseId())
                .isEqualTo(input.getId());
    }

    @Test
    public void create_withoutInput_shouldThrowException()
    {
        //call test method
        Assertions.assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.create(null, USER))
                .withMessageContaining("must not be null")
                .withNoCause();

        //verify invocations
        verifyZeroInteractions(competitionClassDAO);
    }



    @Test
    public void update()
    {
        //prepare test data
        final CompetitionClassDO input = getCompetitionClassDO();

        final CompetitionClassBE expectedBE = getCompetitionClassBE();

        //configure mocks
        when(competitionClassDAO.update(any(CompetitionClassBE.class), anyLong())).thenReturn(expectedBE);

        //call test method
        final CompetitionClassDO actual = underTest.update(input, USER);

        //assert resutl
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(input.getId());
        assertThat(actual.getKlasseName())
                .isEqualTo(input.getKlasseName());

        //verify invocations
        verify(competitionClassDAO).update(competitionClassBEArgumentCaptor.capture(), anyLong());

        final CompetitionClassBE persistedBE = competitionClassBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getKlasseId())
                .isEqualTo(input.getId());
        assertThat(persistedBE.getKlasseName())
                .isEqualTo(input.getKlasseName());


    }

    @Test
    public void update_withoutInput_shouldThrowException()
    {
        // call test method
        Assertions.assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.update(null, USER))
                .withMessageContaining("must not be null")
                .withNoCause();

        //verify invocations
        verifyZeroInteractions(competitionClassDAO);
    }








}