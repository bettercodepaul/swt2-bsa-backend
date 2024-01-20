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
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.business.regionen.api.RegionenComponent;
import de.bogenliga.application.business.user.api.UserComponent;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemLigaMapperTest {

    // Constants for Mock Data
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
    private UserComponent userComponent;

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

    @Test
    public void testToDO() {
        // prepare test data
        // altsystem DO
        AltsystemLigaDO altsystemLigaDO = new AltsystemLigaDO();
        altsystemLigaDO.setId(1);
        altsystemLigaDO.setIdNextLiga(2);
        altsystemLigaDO.setName("TestLiga");
        // expectedResult
        LigaDO expectedDO = new LigaDO();
        expectedDO.setDisziplinId(RECURVE_ID);
        expectedDO.setName(altsystemLigaDO.getName());
        expectedDO.setLigaUebergeordnetId(altsystemLigaDO.getIdNextLiga());

        // configure mocks
        when(disziplinComponent.findAll()).thenReturn(getMockDisziplinen());

        // call test method
        LigaDO actual = new LigaDO();
        actual = altsystemLigaMapper.toDO(actual, altsystemLigaDO);

        // assert result
        assertThat(actual.getDisziplinId()).isEqualTo(expectedDO.getDisziplinId());

    }
}