package de.bogenliga.application.business.vereine.impl.business;

import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.vereine.impl.dao.VereinDAO;
import de.bogenliga.application.business.vereine.impl.entity.VereinBE;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VereinComponentImplTest {

    private static final Long USER=0L;
    private static final Long VERSION = 0L;

    private static final String VEREIN="";
    private static final long   VEREIN_ID=0;
    private static final String VEREIN_NAME="";
    private static final String VEREIN_DSB_IDENTIFIER="";
    private static final long   VEREIN_REGION_ID=0;
    private static final String VEREIN_REGION_ID_NOT_NEG="";
    private static final String VEREIN_DSB_MITGLIED_NOT_NEG="";
    private static final long USER_ID=0;
    private static final OffsetDateTime VEREIN_OFFSETDATETIME = null;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private VereinDAO VereinDAO;
    @InjectMocks
    private VereinComponentImpl underTest;
    @Captor
    private ArgumentCaptor<VereinBE> vereinBEArgumentCaptor;

    public static VereinBE getVereinBE(){
        final VereinBE expectedBE = new VereinBE();
        expectedBE.setVereinName(VEREIN_NAME);
        expectedBE.setVereinId(VEREIN_ID);
        expectedBE.setVereinRegionId(VEREIN_REGION_ID);
        //expectedBE.setRegionName();
        expectedBE.setVereinDsbIdentifier(VEREIN_DSB_IDENTIFIER);

        return expectedBE;
    }

    public static VereinDO getVereinDO(){
        return new VereinDO(VEREIN_ID,
                VEREIN_NAME,
                VEREIN_DSB_IDENTIFIER,
                VEREIN_REGION_ID,
                VEREIN_OFFSETDATETIME,
                USER_ID,
                VERSION);
    }

    @Test
    public void findAll() {
        // prepare test data
        final VereinBE expectedBE = getVereinBE();
        final List<VereinBE> expectedBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(VereinDAO.findAll()).thenReturn(expectedBEList);

        // call test method
        final List<VereinDO> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getId())
                .isEqualTo(expectedBE.getVereinId());
        assertThat(actual.get(0).getName())
                .isEqualTo(expectedBE.getVereinName());
        assertThat(actual.get(0).getRegionName())
                .isEqualTo(expectedBE.getRegionName());
        assertThat(actual.get(0).getRegionId())
                .isEqualTo(expectedBE.getVereinRegionId());
        assertThat(actual.get(0).getDsbIdentifier())
                .isEqualTo(expectedBE.getVereinDsbIdentifier());

        // verify invocations
        verify(VereinDAO).findAll();
    }

    @Test
    public void create() {
        // prepare test data
        final VereinDO input = getVereinDO();

        final VereinBE expectedBE = getVereinBE();

        // configure mocks
        when(VereinDAO.create(any(VereinBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final VereinDO actual = underTest.create(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(input.getId());

        // verify invocations
        verify(VereinDAO).create(vereinBEArgumentCaptor.capture(), anyLong());

        final VereinBE persistedBE = vereinBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getVereinId())
                .isEqualTo(input.getId());
    }

    @Test
    public void findById() {
        // prepare test data
        final VereinBE expectedBE = getVereinBE();

        // configure mocks
        when(VereinDAO.findById(VEREIN_ID)).thenReturn(expectedBE);

        // call test method
        final VereinDO actual = underTest.findById(VEREIN_ID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(expectedBE.getVereinId());

        // verify invocations
        verify(VereinDAO).findById(VEREIN_ID);
    }

    @Test
    public void update() {
        // prepare test data
        final VereinDO input = getVereinDO();

        final VereinBE expectedBE = getVereinBE();

        // configure mocks
        when(VereinDAO.update(any(VereinBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final VereinDO actual = underTest.update(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(input.getId());
        assertThat(actual.getName())
                .isEqualTo(input.getName());

        // verify invocations
        verify(VereinDAO).update(vereinBEArgumentCaptor.capture(), anyLong());

        final VereinBE persistedBE = vereinBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getVereinId())
                .isEqualTo(input.getId());
        assertThat(persistedBE.getVereinName())
                .isEqualTo(input.getName());
    }

    @Test
    public void delete() {
        final VereinDO input = getVereinDO();

        final VereinBE expectedBE = getVereinBE();

        // configure mocks

        // call test method
        underTest.delete(input, USER);

        // assert result

        // verify invocations
        verify(VereinDAO).delete(vereinBEArgumentCaptor.capture(), anyLong());

        final VereinBE persistedBE = vereinBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getVereinId())
                .isEqualTo(input.getId());
    }
}