package de.bogenliga.application.business.mannschaftsmitglied.impl.dao;

import java.util.Collections;
import java.util.List;
import org.assertj.core.api.Java6Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.dsbmitglied.impl.dao.DsbMitgliedDAO;
import de.bogenliga.application.business.dsbmitglied.impl.entity.DsbMitgliedBE;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.entity.MannschaftsmitgliedBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static de.bogenliga.application.business.dsbmitglied.impl.business.DsbMitgliedComponentImplTest.getDsbMitgliedBE;
import static de.bogenliga.application.business.mannschaftsmitglied.impl.business.MannschaftsmitgliedComponentImplTest.getMannschatfsmitgliedBE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class MannschaftsmitgliedBasicDAOTest {

    private static final long  USER =0;

    private static final long MANNSCHHAFT_ID = 1111;
    private static final long DSB_MITGLIED_ID = 22222;
    private static final boolean DASB_MITGLIED_EINGESETZT = true;



    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private BasicDAO basicDao;
    @InjectMocks
    private MannschaftsmitgliedDAO underTest;




    @Test
    public void findAll() {


        final MannschaftsmitgliedBE expectedBE = getMannschatfsmitgliedBE();



        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        final List<MannschaftsmitgliedBE> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getMannschaftId())
                .isEqualTo(expectedBE.getMannschaftId());
        assertThat(actual.get(0).getDsbMitgliedId())
                .isEqualTo(expectedBE.getDsbMitgliedId());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());


    }


    @Test
    public void findByMemberAndTeamId(){


        final MannschaftsmitgliedBE expectedBE = new MannschaftsmitgliedBE();
        expectedBE.setMannschaftId(MANNSCHHAFT_ID);
        expectedBE.setDsbMitgliedId(DSB_MITGLIED_ID);
        expectedBE.setDsbMitgliedEingesetzt(true);



        // configure mocks
        when(basicDao.selectSingleEntity(any(), any(), any())).thenReturn(expectedBE);

        // call test method
        final MannschaftsmitgliedBE actual = underTest.findByMemberAndTeamId(MANNSCHHAFT_ID,DSB_MITGLIED_ID);
        System.out.println(actual);
        System.out.println(expectedBE);

        assertThat(actual).isNotNull();

        assertThat(actual.getMannschaftId())
                .isEqualTo(expectedBE.getMannschaftId());
        assertThat(actual.getDsbMitgliedId())
                .isEqualTo(expectedBE.getDsbMitgliedId());

        // verify invocations
        verify(basicDao).selectSingleEntity(any(), any(), any());


    }

    @Test
    public void findByTeamId() {
        // prepare test data
        final MannschaftsmitgliedBE expectedBE = getMannschatfsmitgliedBE();

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<MannschaftsmitgliedBE> actual = underTest.findByTeamId(MANNSCHHAFT_ID);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0).getMannschaftId()).isEqualTo(expectedBE.getMannschaftId());
    }

    @Test
    public void findAllSchuetzeInTeam(){

        final MannschaftsmitgliedBE expectedBE = getMannschatfsmitgliedBE();
        final List<MannschaftsmitgliedBE> expectedBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<MannschaftsmitgliedBE> actual = underTest.findAllSchuetzeInTeam(MANNSCHHAFT_ID);
        // assert result
        Java6Assertions.assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Java6Assertions.assertThat(actual.get(0).getMannschaftId()).isEqualTo(expectedBE.getMannschaftId());
        Java6Assertions.assertThat(actual.get(0).getDsbMitgliedId()).isEqualTo(expectedBE.getDsbMitgliedId());
        Java6Assertions.assertThat(actual.get(0).isDsbMitgliedEingesetzt()).isEqualTo(expectedBE.isDsbMitgliedEingesetzt());

    }





}
