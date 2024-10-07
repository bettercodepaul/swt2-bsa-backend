package de.bogenliga.application.business.altsystem.liga.mapper;

import java.util.LinkedList;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.altsystem.liga.dataobject.AltsystemLigaDO;
import de.bogenliga.application.business.disziplin.api.DisziplinComponent;
import de.bogenliga.application.business.disziplin.api.types.DisziplinDO;
import de.bogenliga.application.business.liga.api.LigaComponent;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.business.regionen.api.RegionenComponent;
import de.bogenliga.application.business.regionen.api.types.RegionenDO;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemLigaMapperTest {

    // Constants for Mock Data
    private static final long IMPORT_USER_ID = 1L;
    private static final long DSB_REGION_ID = 1L;
    private static final String COMPOUND_NAME = "Compound";
    private static final long COMPOUND_ID = 1L;
    private static final String RECURVE_NAME = "Recurve";
    private static final long RECURVE_ID = 2L;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    private DisziplinComponent disziplinComponent;
    @Mock
    private RegionenComponent regionenComponent;
    @Mock
    private LigaComponent ligaComponent;

    @InjectMocks
    private AltsystemLigaMapper altsystemLigaMapper;

    public static List<DisziplinDO> getMockDisziplinen(){
        List<DisziplinDO> disziplinen = new LinkedList<>();
        // add recurve
        DisziplinDO recurveDO = new DisziplinDO();
        recurveDO.setDisziplinId(RECURVE_ID);
        recurveDO.setDisziplinName(RECURVE_NAME);
        disziplinen.add(recurveDO);
        // add compound
        DisziplinDO compoundDO = new DisziplinDO();
        compoundDO.setDisziplinId(COMPOUND_ID);
        compoundDO.setDisziplinName(COMPOUND_NAME);
        disziplinen.add(compoundDO);

        return disziplinen;
    }

    public static List<RegionenDO> getMockRegionen(){
        List<RegionenDO> regionen = new LinkedList<>();
        // add dsb element
        RegionenDO dsb = new RegionenDO(DSB_REGION_ID);
        dsb.setId(1L);
        dsb.setRegionKuerzel("DSB");
        regionen.add(dsb);

        return regionen;
    }

    public static LigaDO getMockBundesliga(){
        // add dsb element
        LigaDO bundesliga = new LigaDO();
        bundesliga.setId(10L);
        bundesliga.setName("Bundesliga");
        return bundesliga;
    }

    @Test
    public void testToDO() {
        // prepare test data
        // altsystem DO
        AltsystemLigaDO altsystemLigaDO = new AltsystemLigaDO();
        altsystemLigaDO.setId(1L);
        altsystemLigaDO.setIdNextLiga(2);
        altsystemLigaDO.setName("TestLiga");
        // expectedResult
        LigaDO expectedDO = new LigaDO();
        expectedDO.setDisziplinId(RECURVE_ID);
        expectedDO.setName(altsystemLigaDO.getName());

        // configure mocks
        when(disziplinComponent.findAll()).thenReturn(getMockDisziplinen());

        // call test method
        LigaDO actual = new LigaDO();
        actual = altsystemLigaMapper.toDO(actual, altsystemLigaDO);

        // assert result
        assertThat(actual.getName()).isEqualTo(expectedDO.getName());
        assertThat(actual.getDisziplinId()).isEqualTo(expectedDO.getDisziplinId());
    }

    @Test
    public void testToDO_Compound() {
        // prepare test data
        // altsystem DO
        AltsystemLigaDO altsystemLigaDO = new AltsystemLigaDO();
        altsystemLigaDO.setId(1L);
        altsystemLigaDO.setIdNextLiga(2);
        altsystemLigaDO.setName("TestLiga Compound");
        // expectedResult
        LigaDO expectedDO = new LigaDO();
        expectedDO.setDisziplinId(COMPOUND_ID);
        expectedDO.setName(altsystemLigaDO.getName());
        expectedDO.setLigaUebergeordnetId(altsystemLigaDO.getIdNextLiga());

        // configure mocks
        when(disziplinComponent.findAll()).thenReturn(getMockDisziplinen());

        // call test method
        LigaDO actual = new LigaDO();
        actual = altsystemLigaMapper.toDO(actual, altsystemLigaDO);

        // assert result
        assertThat(actual.getName()).isEqualTo(expectedDO.getName());
        assertThat(actual.getDisziplinId()).isEqualTo(expectedDO.getDisziplinId());
    }

    @Test
    public void testAddDefaultFileds(){
        // prepare test data
        // actual DO
        LigaDO actual = new LigaDO();
        actual.setName("TestName");
        actual.setDisziplinId(1L);
        // expectedResult
        LigaDO expectedDO = new LigaDO();
        expectedDO.setName(actual.getName());
        expectedDO.setDisziplinId(actual.getDisziplinId());
        expectedDO.setLigaVerantwortlichId(IMPORT_USER_ID);
        expectedDO.setRegionId(DSB_REGION_ID);
        expectedDO.setLigaUebergeordnetId(getMockBundesliga().getId());

        // configure mocks
        when(regionenComponent.findAll()).thenReturn(getMockRegionen());
        when(ligaComponent.checkExistsLigaName(any())).thenReturn(getMockBundesliga());

        // call test method
        actual = altsystemLigaMapper.addDefaultFields(actual, IMPORT_USER_ID);

        // assert result
        assertThat(actual.getName()).isEqualTo(expectedDO.getName());
        assertThat(actual.getDisziplinId()).isEqualTo(expectedDO.getDisziplinId());
        assertThat(actual.getLigaVerantwortlichId()).isEqualTo(expectedDO.getLigaVerantwortlichId());
        assertThat(actual.getRegionId()).isEqualTo(expectedDO.getRegionId());
        assertThat(actual.getLigaUebergeordnetId()).isEqualTo(expectedDO.getLigaUebergeordnetId());
    }
}