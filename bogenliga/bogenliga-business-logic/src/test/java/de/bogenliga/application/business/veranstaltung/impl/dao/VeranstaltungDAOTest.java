package de.bogenliga.application.business.veranstaltung.impl.dao;

import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.sportjahr.api.types.SportjahrDO;
import de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungBE;
import de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungPhase;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test for VeranstaltungDAO
 *
 * @author Lisa Tochtermann, lisa.tochtermann@student.reutlingen-university.de
 */
public class VeranstaltungDAOTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private BasicDAO basicDao;
    @InjectMocks
    private VeranstaltungDAO underTest;

    private static final long VERANSTALTUNGSID = 1L;
    private static final long LIGAID = 2L;

    private static final long LIGALEITERID = 2L;
    private static final String VERANSTALTUNGSNAME = "Test Veranstaltung";
    private static final long SPORTJAHR = 2018L;

    private static final long SPORTJAHR_ID = 1;

    private static final Integer PHASE = 1;
    private static final VeranstaltungPhase.Phase[] PHASELIST_GEPLANT_LAUFEND = {VeranstaltungPhase.Phase.GEPLANT, VeranstaltungPhase.Phase.LAUFEND};

    private static final VeranstaltungPhase.Phase[] PHASELIST_GEPLANT_LAUFEND_ABGESCHLOSSEN = {VeranstaltungPhase.Phase.GEPLANT, VeranstaltungPhase.Phase.LAUFEND, VeranstaltungPhase.Phase.ABGESCHLOSSEN};

    private static final VeranstaltungPhase.Phase[] PHASELIST_LAUFEND_ABGESCHLOSSEN = {VeranstaltungPhase.Phase.LAUFEND, VeranstaltungPhase.Phase.ABGESCHLOSSEN};

    private static final VeranstaltungPhase.Phase[] PHASELIST_0 = {};

    private static final VeranstaltungPhase.Phase[] PHASELIST_LAUFEND = {VeranstaltungPhase.Phase.LAUFEND};


    @Test
    public void testFindBySportjahrDestinct() {
        //create test data
        VeranstaltungBE expectedBE = new VeranstaltungBE();
        expectedBE.setVeranstaltungId(VERANSTALTUNGSID);
        expectedBE.setVeranstaltungName(VERANSTALTUNGSNAME);
        expectedBE.setVeranstaltungLigaId(LIGAID);
        expectedBE.setVeranstaltungPhase(PHASE);

        //mock the methode
        when(basicDao.selectEntityList(any(),any(), any())).thenReturn(Collections.singletonList(expectedBE));
        // call test method
        final List<VeranstaltungBE> actual = underTest.findBySportjahrDestinct(SPORTJAHR);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getVeranstaltungId())
                .isEqualTo(expectedBE.getVeranstaltungId());
        assertThat(actual.get(0).getVeranstaltungName())
                .isEqualTo(expectedBE.getVeranstaltungName());
        assertThat(actual.get(0).getVeranstaltungLigaId())
                .isEqualTo(expectedBE.getVeranstaltungLigaId());
        assertThat(actual.get(0).getVeranstaltungPhase())
                .isEqualTo(expectedBE.getVeranstaltungPhase());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }


    @Test
    public void testfindAll_0_Phase() {
        //create test data
        VeranstaltungBE expectedBE = new VeranstaltungBE();
        expectedBE.setVeranstaltungId(VERANSTALTUNGSID);
        expectedBE.setVeranstaltungName(VERANSTALTUNGSNAME);
        expectedBE.setVeranstaltungLigaId(LIGAID);
        expectedBE.setVeranstaltungPhase(PHASE);

        //mock the methode
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));
        // call test method
        final List<VeranstaltungBE> actual = underTest.findAll(PHASELIST_0);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getVeranstaltungId())
                .isEqualTo(expectedBE.getVeranstaltungId());
        assertThat(actual.get(0).getVeranstaltungName())
                .isEqualTo(expectedBE.getVeranstaltungName());
        assertThat(actual.get(0).getVeranstaltungLigaId())
                .isEqualTo(expectedBE.getVeranstaltungLigaId());
        assertThat(actual.get(0).getVeranstaltungPhase())
                .isEqualTo(expectedBE.getVeranstaltungPhase());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }


    @Test
    public void testfindAll_1_Phase() {
        //create test data
        VeranstaltungBE expectedBE = new VeranstaltungBE();
        expectedBE.setVeranstaltungId(VERANSTALTUNGSID);
        expectedBE.setVeranstaltungName(VERANSTALTUNGSNAME);
        expectedBE.setVeranstaltungLigaId(LIGAID);
        expectedBE.setVeranstaltungPhase(PHASE);

        //mock the methode
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));
        // call test method
        final List<VeranstaltungBE> actual = underTest.findAll(PHASELIST_LAUFEND);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getVeranstaltungId())
                .isEqualTo(expectedBE.getVeranstaltungId());
        assertThat(actual.get(0).getVeranstaltungName())
                .isEqualTo(expectedBE.getVeranstaltungName());
        assertThat(actual.get(0).getVeranstaltungLigaId())
                .isEqualTo(expectedBE.getVeranstaltungLigaId());
        assertThat(actual.get(0).getVeranstaltungPhase())
                .isEqualTo(expectedBE.getVeranstaltungPhase());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }


    @Test
    public void testfindAll_2_Phase() {
        //create test data
        VeranstaltungBE expectedBE = new VeranstaltungBE();
        expectedBE.setVeranstaltungId(VERANSTALTUNGSID);
        expectedBE.setVeranstaltungName(VERANSTALTUNGSNAME);
        expectedBE.setVeranstaltungLigaId(LIGAID);
        expectedBE.setVeranstaltungPhase(PHASE);

        //mock the methode
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));
        // call test method
        final List<VeranstaltungBE> actual = underTest.findAll(PHASELIST_GEPLANT_LAUFEND);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getVeranstaltungId())
                .isEqualTo(expectedBE.getVeranstaltungId());
        assertThat(actual.get(0).getVeranstaltungName())
                .isEqualTo(expectedBE.getVeranstaltungName());
        assertThat(actual.get(0).getVeranstaltungLigaId())
                .isEqualTo(expectedBE.getVeranstaltungLigaId());
        assertThat(actual.get(0).getVeranstaltungPhase())
                .isEqualTo(expectedBE.getVeranstaltungPhase());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }


    @Test
    public void testfindBySporjahr_0_Phase() {
        //create test data
        VeranstaltungBE expectedBE = new VeranstaltungBE();
        expectedBE.setVeranstaltungId(VERANSTALTUNGSID);
        expectedBE.setVeranstaltungName(VERANSTALTUNGSNAME);
        expectedBE.setVeranstaltungLigaId(LIGAID);
        expectedBE.setVeranstaltungPhase(PHASE);

        //mock the methode
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));
        // call test method
        final List<VeranstaltungBE> actual = underTest.findBySportjahr(SPORTJAHR, PHASELIST_0);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getVeranstaltungId())
                .isEqualTo(expectedBE.getVeranstaltungId());
        assertThat(actual.get(0).getVeranstaltungName())
                .isEqualTo(expectedBE.getVeranstaltungName());
        assertThat(actual.get(0).getVeranstaltungLigaId())
                .isEqualTo(expectedBE.getVeranstaltungLigaId());
        assertThat(actual.get(0).getVeranstaltungPhase())
                .isEqualTo(expectedBE.getVeranstaltungPhase());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }


    @Test
    public void testfindBySporjahr_1_Phase() {
        //create test data
        VeranstaltungBE expectedBE = new VeranstaltungBE();
        expectedBE.setVeranstaltungId(VERANSTALTUNGSID);
        expectedBE.setVeranstaltungName(VERANSTALTUNGSNAME);
        expectedBE.setVeranstaltungLigaId(LIGAID);
        expectedBE.setVeranstaltungPhase(PHASE);

        //mock the methode
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));
        // call test method
        final List<VeranstaltungBE> actual = underTest.findBySportjahr(SPORTJAHR, PHASELIST_LAUFEND);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getVeranstaltungId())
                .isEqualTo(expectedBE.getVeranstaltungId());
        assertThat(actual.get(0).getVeranstaltungName())
                .isEqualTo(expectedBE.getVeranstaltungName());
        assertThat(actual.get(0).getVeranstaltungLigaId())
                .isEqualTo(expectedBE.getVeranstaltungLigaId());
        assertThat(actual.get(0).getVeranstaltungPhase())
                .isEqualTo(expectedBE.getVeranstaltungPhase());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }


    @Test
    public void testfindBySporjahr_2_Phase() {
        //create test data
        VeranstaltungBE expectedBE = new VeranstaltungBE();
        expectedBE.setVeranstaltungId(VERANSTALTUNGSID);
        expectedBE.setVeranstaltungName(VERANSTALTUNGSNAME);
        expectedBE.setVeranstaltungLigaId(LIGAID);
        expectedBE.setVeranstaltungPhase(PHASE);

        //mock the methode
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));
        // call test method
        final List<VeranstaltungBE> actual = underTest.findBySportjahr(SPORTJAHR, PHASELIST_GEPLANT_LAUFEND);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getVeranstaltungId())
                .isEqualTo(expectedBE.getVeranstaltungId());
        assertThat(actual.get(0).getVeranstaltungName())
                .isEqualTo(expectedBE.getVeranstaltungName());
        assertThat(actual.get(0).getVeranstaltungLigaId())
                .isEqualTo(expectedBE.getVeranstaltungLigaId());
        assertThat(actual.get(0).getVeranstaltungPhase())
                .isEqualTo(expectedBE.getVeranstaltungPhase());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }


    @Test
    public void testfindBySporjahr_3_Phase() {
        //create test data
        VeranstaltungBE expectedBE = new VeranstaltungBE();
        expectedBE.setVeranstaltungId(VERANSTALTUNGSID);
        expectedBE.setVeranstaltungName(VERANSTALTUNGSNAME);
        expectedBE.setVeranstaltungLigaId(LIGAID);
        expectedBE.setVeranstaltungPhase(PHASE);

        //mock the methode
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));
        // call test method
        final List<VeranstaltungBE> actual = underTest.findBySportjahr(SPORTJAHR,
                PHASELIST_GEPLANT_LAUFEND_ABGESCHLOSSEN);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getVeranstaltungId())
                .isEqualTo(expectedBE.getVeranstaltungId());
        assertThat(actual.get(0).getVeranstaltungName())
                .isEqualTo(expectedBE.getVeranstaltungName());
        assertThat(actual.get(0).getVeranstaltungLigaId())
                .isEqualTo(expectedBE.getVeranstaltungLigaId());
        assertThat(actual.get(0).getVeranstaltungPhase())
                .isEqualTo(expectedBE.getVeranstaltungPhase());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }


    @Test
    public void testFindByLigaID() {
        //create test data
        VeranstaltungBE expectedBE = new VeranstaltungBE();
        expectedBE.setVeranstaltungId(VERANSTALTUNGSID);
        expectedBE.setVeranstaltungName(VERANSTALTUNGSNAME);
        expectedBE.setVeranstaltungLigaId(LIGAID);
        expectedBE.setVeranstaltungPhase(PHASE);

        //mock the methode
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));
        // call test method
        final List<VeranstaltungBE> actual = underTest.findByLigaID(LIGAID);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getVeranstaltungId())
                .isEqualTo(expectedBE.getVeranstaltungId());
        assertThat(actual.get(0).getVeranstaltungName())
                .isEqualTo(expectedBE.getVeranstaltungName());
        assertThat(actual.get(0).getVeranstaltungLigaId())
                .isEqualTo(expectedBE.getVeranstaltungLigaId());
        assertThat(actual.get(0).getVeranstaltungPhase())
                .isEqualTo(expectedBE.getVeranstaltungPhase());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }


    @Test
    public void testFindById() {
        //create test data
        VeranstaltungBE expectedBE = new VeranstaltungBE();
        expectedBE.setVeranstaltungId(VERANSTALTUNGSID);
        expectedBE.setVeranstaltungName(VERANSTALTUNGSNAME);
        expectedBE.setVeranstaltungLigaId(LIGAID);
        expectedBE.setVeranstaltungPhase(PHASE);

        //mock the methode
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));
        // call test method
        final List<VeranstaltungBE> actual = underTest.findByLigaID(VERANSTALTUNGSID);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getVeranstaltungId())
                .isEqualTo(expectedBE.getVeranstaltungId());
        assertThat(actual.get(0).getVeranstaltungName())
                .isEqualTo(expectedBE.getVeranstaltungName());
        assertThat(actual.get(0).getVeranstaltungLigaId())
                .isEqualTo(expectedBE.getVeranstaltungLigaId());
        assertThat(actual.get(0).getVeranstaltungPhase())
                .isEqualTo(expectedBE.getVeranstaltungPhase());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }


    @Test
    public void testFindByLigaleiterId() {
        //create test data
        VeranstaltungBE expectedBE = new VeranstaltungBE();
        expectedBE.setVeranstaltungId(VERANSTALTUNGSID);
        expectedBE.setVeranstaltungName(VERANSTALTUNGSNAME);
        expectedBE.setVeranstaltungLigaId(LIGAID);
        expectedBE.setVeranstaltungPhase(PHASE);

        //mock the methode
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));
        // call test method
        final List<VeranstaltungBE> actual = underTest.findByLigaleiterId(LIGALEITERID);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getVeranstaltungId())
                .isEqualTo(expectedBE.getVeranstaltungId());
        assertThat(actual.get(0).getVeranstaltungName())
                .isEqualTo(expectedBE.getVeranstaltungName());
        assertThat(actual.get(0).getVeranstaltungLigaId())
                .isEqualTo(expectedBE.getVeranstaltungLigaId());
        assertThat(actual.get(0).getVeranstaltungPhase())
                .isEqualTo(expectedBE.getVeranstaltungPhase());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }


    @Test
    public void testFindAllSportjahrDestinct() {
        //create test data
        SportjahrDO sportjahrDO = new SportjahrDO();
        sportjahrDO.setId(SPORTJAHR_ID);
        sportjahrDO.setSportjahr(SPORTJAHR);

        VeranstaltungBE expectedBE = new VeranstaltungBE();
        expectedBE.setVeranstaltungId(VERANSTALTUNGSID);
        expectedBE.setVeranstaltungName(VERANSTALTUNGSNAME);
        expectedBE.setVeranstaltungLigaId(LIGAID);
        expectedBE.setVeranstaltungPhase(PHASE);
        expectedBE.setVeranstaltungSportjahr(SPORTJAHR);

        //mock the methode
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));
        // call test method
        final List<SportjahrDO> actual = underTest.findAllSportjahreDestinct();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getId())
                .isEqualTo(sportjahrDO.getId());
        assertThat(actual.get(0).getSportjahr())
                .isEqualTo(sportjahrDO.getSportjahr());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }
}