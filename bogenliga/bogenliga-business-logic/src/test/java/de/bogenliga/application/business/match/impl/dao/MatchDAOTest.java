package de.bogenliga.application.business.match.impl.dao;

import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import de.bogenliga.application.business.baseClass.impl.BasicBETest;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.match.impl.BaseMatchTest;
import de.bogenliga.application.business.match.impl.entity.MatchBE;
import de.bogenliga.application.business.match.impl.mapper.MatchMapper;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;
import static de.bogenliga.application.business.match.impl.business.MatchComponentImplTest.*;
import static de.bogenliga.application.business.match.impl.business.MatchComponentImplTest.getMatchBE;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
public class MatchDAOTest extends BaseMatchTest {

    @Mock
    private BasicDAO basicDao;

    @InjectMocks
    private MatchDAO underTest;
    private MatchBE expectedBE;

    // Implements generic way to test business entities methods
    private BasicBETest<MatchBE> basicDAOTest;


    @Before
    public void testSetup() {
        basicDAOTest = new BasicBETest<>();

        expectedBE = getMatchBE();
        basicDAOTest.setBE(expectedBE);
        // configure mocks
        when(basicDao.selectSingleEntity(any(), any(), anyLong())).thenReturn(expectedBE);

    }


    @Test
    public void findById() {
        basicDAOTest.testMethod(underTest.findById(MATCH_ID));
    }


    @Test
    public void findByPk() {
        basicDAOTest.testMethod(underTest.findByPk(
                MATCH_NR, MATCH_WETTKAMPF_ID,
                MATCH_MANNSCHAFT_ID, MATCH_BEGEGNUNG,
                MATCH_SCHEIBENNUMMER
        ));
    }


    @Test
    public void findAll() {
        basicDAOTest.testMethod(underTest.findAll());
    }


    @Test
    public void findByWettkampfId() {
        basicDAOTest.testMethod(underTest.findByWettkampfId(MATCH_WETTKAMPF_ID));
    }


    @Test
    public void findByMannschaftId() {
        basicDAOTest.testMethod(underTest.findByMannschaftId(MATCH_MANNSCHAFT_ID));
    }


}