package de.bogenliga.application.business.mannschaftsmitglied.impl.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.baseClass.impl.BasicTest;
import de.bogenliga.application.business.mannschaftsmitglied.impl.MannschaftsmitgliedBaseDAOTest;
import de.bogenliga.application.business.mannschaftsmitglied.impl.entity.MannschaftsmitgliedExtendedBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * TODO [AL] class documentation
 *
 * @author Kay Scheerer
 */
public class MannschaftmitgliedDAOTest extends MannschaftsmitgliedBaseDAOTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private BasicDAO basicDao;
    @InjectMocks
    private MannschaftsmitgliedDAO underTest;

    private MannschaftsmitgliedExtendedBE expectedBE;

    // Implements generic way to test business entities methods
    private BasicTest<MannschaftsmitgliedExtendedBE, MannschaftsmitgliedExtendedBE> basicDAOTest;


    @Before
    public void testSetup() {
        expectedBE = getMannschaftsmitgliedExtendedBE();
        basicDAOTest = new BasicTest<>(expectedBE, getValuesToMethodMap());

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));
        when(basicDao.selectSingleEntity(any(), any(), any())).thenReturn(expectedBE);
    }

    public static MannschaftsmitgliedExtendedBE getMannschatfsmitgliedExtendedBE() {
        final MannschaftsmitgliedExtendedBE expectedBE = new MannschaftsmitgliedExtendedBE();
        expectedBE.setMannschaftId(101L);
        expectedBE.setDsbMitgliedId(70L);
        expectedBE.setDsbMitgliedEingesetzt(3);

        return expectedBE;
    }


@Test
public void findAll() throws InvocationTargetException, IllegalAccessException {
        basicDAOTest.testAllFindMethods(underTest);
}

    @Test
    public void testfindAll() {
        try {
            basicDAOTest.testAllFieldsOnEqualToExpectedEntity(underTest.findAll());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void findByMemberAndTeamId() {
        try {
            basicDAOTest.testAllFieldsOnEqualToExpectedEntity(underTest.findByMemberAndTeamId(
            dsbMitgliedId,mannschaftId));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findByTeamIdAndRueckennummer() {
        final MannschaftsmitgliedExtendedBE expectedBE = getMannschatfsmitgliedExtendedBE();

        when(basicDao.selectSingleEntity(any(),any(),any(),any())).thenReturn(expectedBE);

        MannschaftsmitgliedExtendedBE actual = underTest.findByTeamIdAndRueckennummer(101L, 4);

        assertThat(actual).isEqualTo(expectedBE);
    }

    @Test
    public void findAllSchuetzeInTeam() {
        try {
            basicDAOTest.testAllFieldsOnEqualToExpectedEntity(underTest.findAllSchuetzeInTeamEingesetzt(mannschaftId));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void findByTeamId() {
        try {
            basicDAOTest.testAllFieldsOnEqualToExpectedEntity(underTest.findByTeamId(mannschaftId));

        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}