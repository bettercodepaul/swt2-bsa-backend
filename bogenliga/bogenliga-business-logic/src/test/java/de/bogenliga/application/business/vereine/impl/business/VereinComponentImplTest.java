package de.bogenliga.application.business.vereine.impl.business;

import de.bogenliga.application.business.regionen.api.types.RegionenDO;
import de.bogenliga.application.business.regionen.impl.dao.RegionenDAO;
import de.bogenliga.application.business.regionen.impl.entity.RegionenBE;
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
import org.springframework.test.context.TestPropertySource;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class VereinComponentImplTest {

    private static final Long USER = 0L;
    private static final Long VERSION = 0L;

    private static final long VEREIN_ID = 0;
    private static final String VEREIN_NAME = "";
    private static final String VEREIN_DSB_IDENTIFIER = "";
    private static final long VEREIN_REGION_ID = 0;
    private static final String VEREIN_WEBSITE = "";
    private static final String VEREIN_DESCRIPTION = "";
    private static final String VEREIN_ICON = "";
    private static final long USER_ID = 0;
    private static final OffsetDateTime VEREIN_OFFSETDATETIME = null;
    private static final Long REGION_ID = 0L;
    private static final String REGION_NAME = "Test";
    private static final String REGION_KUERZEL = "tt";
    private static final String REGION_TYPE = "TE";
    private static final Long REGION_UEBERGEORDNET = 1L;


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private VereinDAO vereinDAO;
    @Mock
    private RegionenDAO regionenDAO;
    @InjectMocks
    private VereinComponentImpl underTest;
    @Captor
    private ArgumentCaptor<VereinBE> vereinBEArgumentCaptor;

    public static VereinBE getVereinBE() {
        final VereinBE expectedBE = new VereinBE();
        expectedBE.setVereinName(VEREIN_NAME);
        expectedBE.setVereinId(VEREIN_ID);
        expectedBE.setVereinRegionId(VEREIN_REGION_ID);
        //expectedBE.setRegionName();
        expectedBE.setVereinDsbIdentifier(VEREIN_DSB_IDENTIFIER);
        expectedBE.setVereinWebsite(VEREIN_WEBSITE);
        expectedBE.setVereinDescription(VEREIN_DESCRIPTION);
        expectedBE.setVereinIcon(VEREIN_ICON);

        return expectedBE;
    }

    public static VereinDO getVereinDO() {
        return new VereinDO(VEREIN_ID,
                VEREIN_NAME,
                VEREIN_DSB_IDENTIFIER,
                VEREIN_REGION_ID,
                VEREIN_WEBSITE,
                VEREIN_DESCRIPTION,
                VEREIN_ICON,
                VEREIN_OFFSETDATETIME,
                USER_ID,
                VERSION);
    }

    public static RegionenBE getRegionenBE() { // da ist die region die der Verein ID entspricht
        RegionenBE regionenBE = new RegionenBE();
        regionenBE.setRegionId(REGION_ID);
        regionenBE.setRegionKuerzel(REGION_KUERZEL);
        regionenBE.setRegionTyp(REGION_TYPE);
        regionenBE.setRegionUebergeordnet(REGION_UEBERGEORDNET);
        regionenBE.setRegionName(REGION_NAME);

        return regionenBE;
    }

    @Test
    public void findAll() {
        // prepare test data
        final VereinBE expectedBE = getVereinBE();
        final RegionenBE expectedRegionBE = getRegionenBE();
        final List<RegionenBE> expectedRegionBEList = Collections.singletonList(expectedRegionBE);
        final List<VereinBE> expectedVereinBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(vereinDAO.findAll()).thenReturn(expectedVereinBEList);

        when(regionenDAO.findAll()).thenReturn(expectedRegionBEList);
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
        assertThat(actual.get(0).getRegionId())
                .isEqualTo(expectedBE.getVereinRegionId());
        assertThat(actual.get(0).getDsbIdentifier())
                .isEqualTo(expectedBE.getVereinDsbIdentifier());
        assertThat(actual.get(0).getWebsite())
                .isEqualTo(expectedBE.getVereinWebsite());
        assertThat(actual.get(0).getDescription())
                .isEqualTo(expectedBE.getVereinDescription());
        assertThat(actual.get(0).getIcon())
                .isEqualTo(expectedBE.getVereinIcon());
        assertThat(actual.get(0).getRegionName())
                .isEqualTo(expectedRegionBE.getRegionName());

        // verify invocations
        verify(vereinDAO).findAll();
    }

    @Test
    public void create() {
        // prepare test data
        final VereinDO input = getVereinDO();

        final VereinBE expectedBE = getVereinBE();

        // configure mocks
        when(vereinDAO.create(any(VereinBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final VereinDO actual = underTest.create(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(input.getId());

        // verify invocations
        verify(vereinDAO).create(vereinBEArgumentCaptor.capture(), anyLong());

        final VereinBE persistedBE = vereinBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getVereinId())
                .isEqualTo(input.getId());
    }

    @Test
    public void findById() {
        // prepare test data
        final VereinBE expectedBE = getVereinBE();

        final RegionenBE regionbe =getRegionenBE();


        // configure mocks
        when(vereinDAO.findById(VEREIN_ID)).thenReturn(expectedBE);

        when(regionenDAO.findById(expectedBE.getVereinRegionId())).thenReturn(regionbe);

        // call test method


        final VereinDO actual = underTest.findById(VEREIN_ID);


        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(expectedBE.getVereinId());

        assertThat(actual.getRegionId().equals(expectedBE.getVereinRegionId()));
        assertThat(actual.getRegionName().equals(regionbe.getRegionName()));

        // verify invocations
        verify(vereinDAO).findById(VEREIN_ID);
        verify(regionenDAO).findById(expectedBE.getVereinRegionId());
    }

    @Test
    public void update() {
        // prepare test data
        final VereinDO input = getVereinDO();

        final VereinBE expectedBE = getVereinBE();

        // configure mocks
        when(vereinDAO.update(any(VereinBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final VereinDO actual = underTest.update(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(input.getId());
        assertThat(actual.getName())
                .isEqualTo(input.getName());

        // verify invocations
        verify(vereinDAO).update(vereinBEArgumentCaptor.capture(), anyLong());

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
        verify(vereinDAO).delete(vereinBEArgumentCaptor.capture(), anyLong());

        final VereinBE persistedBE = vereinBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getVereinId())
                .isEqualTo(input.getId());
    }

    @Test
    public void equals(){
         VereinDO underTest = new VereinDO(VEREIN_ID,
                VEREIN_NAME,
                VEREIN_DSB_IDENTIFIER,
                VEREIN_REGION_ID,
                VEREIN_WEBSITE,
                VEREIN_DESCRIPTION,
                VEREIN_ICON,
                VEREIN_OFFSETDATETIME,
                USER_ID,
                VERSION);
         assertThat(underTest.getRegionName()).isEqualTo(getVereinDO().getRegionName());
         assertEquals(underTest,getVereinDO());
    }
}