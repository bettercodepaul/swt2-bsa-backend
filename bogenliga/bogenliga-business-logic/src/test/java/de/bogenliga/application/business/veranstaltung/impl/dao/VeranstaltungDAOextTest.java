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
import de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungBEext;
import de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungPhase;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class VeranstaltungDAOextTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private BasicDAO basicDao;
    @InjectMocks
    private VeranstaltungDAOext underTest;

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
    public void testfindEverything_0_Phase() {
        VeranstaltungBEext expectedBEext = new VeranstaltungBEext();
        expectedBEext.setVeranstaltungId(VERANSTALTUNGSID);
        expectedBEext.setVeranstaltungName(VERANSTALTUNGSNAME);
        expectedBEext.setVeranstaltungLigaId(LIGAID);
        expectedBEext.setVeranstaltungPhase(PHASE);

        //mock the methode
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBEext));

        // Act
        List<VeranstaltungBEext> actual = underTest.findEverything(PHASELIST_0);

        // Assert
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        VeranstaltungBEext actualBE = actual.get(0);
        assertThat(actualBE.getVeranstaltungId()).isEqualTo(expectedBEext.getVeranstaltungId());
        assertThat(actualBE.getVeranstaltungName()).isEqualTo(expectedBEext.getVeranstaltungName());
        assertThat(actualBE.getVeranstaltungLigaId()).isEqualTo(expectedBEext.getVeranstaltungLigaId());
        assertThat(actualBE.getVeranstaltungPhase()).isEqualTo(expectedBEext.getVeranstaltungPhase());

        assertThat(actualBE.getLigaName()).isEqualTo(expectedBEext.getLigaName());
        assertThat(actualBE.getWettkampftypName()).isEqualTo(expectedBEext.getWettkampftypName());
        assertThat(actualBE.getLigaLeiterEmail()).isEqualTo(expectedBEext.getLigaLeiterEmail());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }

    @Test
    public void testfindEverything_1_Phase() {
        VeranstaltungBEext expectedBEext = new VeranstaltungBEext();
        expectedBEext.setVeranstaltungId(VERANSTALTUNGSID);
        expectedBEext.setVeranstaltungName(VERANSTALTUNGSNAME);
        expectedBEext.setVeranstaltungLigaId(LIGAID);
        expectedBEext.setVeranstaltungPhase(PHASE);

        //mock the methode
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBEext));

        // Act
        List<VeranstaltungBEext> actual = underTest.findEverything(PHASELIST_LAUFEND);

        // Assert
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        VeranstaltungBEext actualBE = actual.get(0);
        assertThat(actualBE.getVeranstaltungId()).isEqualTo(expectedBEext.getVeranstaltungId());
        assertThat(actualBE.getVeranstaltungName()).isEqualTo(expectedBEext.getVeranstaltungName());
        assertThat(actualBE.getVeranstaltungLigaId()).isEqualTo(expectedBEext.getVeranstaltungLigaId());
        assertThat(actualBE.getVeranstaltungPhase()).isEqualTo(expectedBEext.getVeranstaltungPhase());

        assertThat(actualBE.getLigaName()).isEqualTo(expectedBEext.getLigaName());
        assertThat(actualBE.getWettkampftypName()).isEqualTo(expectedBEext.getWettkampftypName());
        assertThat(actualBE.getLigaLeiterEmail()).isEqualTo(expectedBEext.getLigaLeiterEmail());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }

    @Test
    public void testfindEverything_2_Phase() {
        VeranstaltungBEext expectedBEext = new VeranstaltungBEext();
        expectedBEext.setVeranstaltungId(VERANSTALTUNGSID);
        expectedBEext.setVeranstaltungName(VERANSTALTUNGSNAME);
        expectedBEext.setVeranstaltungLigaId(LIGAID);
        expectedBEext.setVeranstaltungPhase(PHASE);

        //mock the methode
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBEext));

        // Act
        List<VeranstaltungBEext> actual = underTest.findEverything(PHASELIST_GEPLANT_LAUFEND);

        // Assert
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        VeranstaltungBEext actualBE = actual.get(0);
        assertThat(actualBE.getVeranstaltungId()).isEqualTo(expectedBEext.getVeranstaltungId());
        assertThat(actualBE.getVeranstaltungName()).isEqualTo(expectedBEext.getVeranstaltungName());
        assertThat(actualBE.getVeranstaltungLigaId()).isEqualTo(expectedBEext.getVeranstaltungLigaId());
        assertThat(actualBE.getVeranstaltungPhase()).isEqualTo(expectedBEext.getVeranstaltungPhase());

        assertThat(actualBE.getLigaName()).isEqualTo(expectedBEext.getLigaName());
        assertThat(actualBE.getWettkampftypName()).isEqualTo(expectedBEext.getWettkampftypName());
        assertThat(actualBE.getLigaLeiterEmail()).isEqualTo(expectedBEext.getLigaLeiterEmail());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }

    @Test
    public void testfindBySporjahr_0_Phase() {
        //create test data
        VeranstaltungBEext expectedBEext = new VeranstaltungBEext();
        expectedBEext.setVeranstaltungId(VERANSTALTUNGSID);
        expectedBEext.setVeranstaltungName(VERANSTALTUNGSNAME);
        expectedBEext.setVeranstaltungLigaId(LIGAID);
        expectedBEext.setVeranstaltungPhase(PHASE);

        //mock the methode
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBEext));
        // call test method
        final List<VeranstaltungBEext> actual = underTest.findBySportjahr(SPORTJAHR, PHASELIST_0);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getVeranstaltungId())
                .isEqualTo(expectedBEext.getVeranstaltungId());
        assertThat(actual.get(0).getVeranstaltungName())
                .isEqualTo(expectedBEext.getVeranstaltungName());
        assertThat(actual.get(0).getVeranstaltungLigaId())
                .isEqualTo(expectedBEext.getVeranstaltungLigaId());
        assertThat(actual.get(0).getVeranstaltungPhase())
                .isEqualTo(expectedBEext.getVeranstaltungPhase());

        assertThat(actual.get(0).getLigaName()).isEqualTo(expectedBEext.getLigaName());
        assertThat(actual.get(0).getWettkampftypName()).isEqualTo(expectedBEext.getWettkampftypName());
        assertThat(actual.get(0).getLigaLeiterEmail()).isEqualTo(expectedBEext.getLigaLeiterEmail());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }


    @Test
    public void testfindBySporjahr_1_Phase() {
        //create test data
        VeranstaltungBEext expectedBEext = new VeranstaltungBEext();
        expectedBEext.setVeranstaltungId(VERANSTALTUNGSID);
        expectedBEext.setVeranstaltungName(VERANSTALTUNGSNAME);
        expectedBEext.setVeranstaltungLigaId(LIGAID);
        expectedBEext.setVeranstaltungPhase(PHASE);

        //mock the methode
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBEext));
        // call test method
        final List<VeranstaltungBEext> actual = underTest.findBySportjahr(SPORTJAHR, PHASELIST_LAUFEND);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getVeranstaltungId())
                .isEqualTo(expectedBEext.getVeranstaltungId());
        assertThat(actual.get(0).getVeranstaltungName())
                .isEqualTo(expectedBEext.getVeranstaltungName());
        assertThat(actual.get(0).getVeranstaltungLigaId())
                .isEqualTo(expectedBEext.getVeranstaltungLigaId());
        assertThat(actual.get(0).getVeranstaltungPhase())
                .isEqualTo(expectedBEext.getVeranstaltungPhase());

        assertThat(actual.get(0).getLigaName()).isEqualTo(expectedBEext.getLigaName());
        assertThat(actual.get(0).getWettkampftypName()).isEqualTo(expectedBEext.getWettkampftypName());
        assertThat(actual.get(0).getLigaLeiterEmail()).isEqualTo(expectedBEext.getLigaLeiterEmail());
        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }


    @Test
    public void testfindBySporjahr_2_Phase() {
        //create test data
        VeranstaltungBEext expectedBEext = new VeranstaltungBEext();
        expectedBEext.setVeranstaltungId(VERANSTALTUNGSID);
        expectedBEext.setVeranstaltungName(VERANSTALTUNGSNAME);
        expectedBEext.setVeranstaltungLigaId(LIGAID);
        expectedBEext.setVeranstaltungPhase(PHASE);

        //mock the methode
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBEext));
        // call test method
        final List<VeranstaltungBEext> actual = underTest.findBySportjahr(SPORTJAHR, PHASELIST_GEPLANT_LAUFEND);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getVeranstaltungId())
                .isEqualTo(expectedBEext.getVeranstaltungId());
        assertThat(actual.get(0).getVeranstaltungName())
                .isEqualTo(expectedBEext.getVeranstaltungName());
        assertThat(actual.get(0).getVeranstaltungLigaId())
                .isEqualTo(expectedBEext.getVeranstaltungLigaId());
        assertThat(actual.get(0).getVeranstaltungPhase())
                .isEqualTo(expectedBEext.getVeranstaltungPhase());

        assertThat(actual.get(0).getLigaName()).isEqualTo(expectedBEext.getLigaName());
        assertThat(actual.get(0).getWettkampftypName()).isEqualTo(expectedBEext.getWettkampftypName());
        assertThat(actual.get(0).getLigaLeiterEmail()).isEqualTo(expectedBEext.getLigaLeiterEmail());
        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }


    @Test
    public void testfindBySporjahr_3_Phase() {
        //create test data
        VeranstaltungBEext expectedBEext = new VeranstaltungBEext();
        expectedBEext.setVeranstaltungId(VERANSTALTUNGSID);
        expectedBEext.setVeranstaltungName(VERANSTALTUNGSNAME);
        expectedBEext.setVeranstaltungLigaId(LIGAID);
        expectedBEext.setVeranstaltungPhase(PHASE);

        //mock the methode
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBEext));
        // call test method
        final List<VeranstaltungBEext> actual = underTest.findBySportjahr(SPORTJAHR,
                PHASELIST_GEPLANT_LAUFEND_ABGESCHLOSSEN);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getVeranstaltungId())
                .isEqualTo(expectedBEext.getVeranstaltungId());
        assertThat(actual.get(0).getVeranstaltungName())
                .isEqualTo(expectedBEext.getVeranstaltungName());
        assertThat(actual.get(0).getVeranstaltungLigaId())
                .isEqualTo(expectedBEext.getVeranstaltungLigaId());
        assertThat(actual.get(0).getVeranstaltungPhase())
                .isEqualTo(expectedBEext.getVeranstaltungPhase());

        assertThat(actual.get(0).getLigaName()).isEqualTo(expectedBEext.getLigaName());
        assertThat(actual.get(0).getWettkampftypName()).isEqualTo(expectedBEext.getWettkampftypName());
        assertThat(actual.get(0).getLigaLeiterEmail()).isEqualTo(expectedBEext.getLigaLeiterEmail());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }

    @Test
    public void testFindByLigaleiterId() {
        //create test data
        VeranstaltungBEext expectedBEext = new VeranstaltungBEext();
        expectedBEext.setVeranstaltungId(VERANSTALTUNGSID);
        expectedBEext.setVeranstaltungName(VERANSTALTUNGSNAME);
        expectedBEext.setVeranstaltungLigaId(LIGAID);
        expectedBEext.setVeranstaltungPhase(PHASE);

        //mock the methode
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBEext));
        // call test method
        final List<VeranstaltungBEext> actual = underTest.findByLigaleiterId(LIGALEITERID);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getVeranstaltungId())
                .isEqualTo(expectedBEext.getVeranstaltungId());
        assertThat(actual.get(0).getVeranstaltungName())
                .isEqualTo(expectedBEext.getVeranstaltungName());
        assertThat(actual.get(0).getVeranstaltungLigaId())
                .isEqualTo(expectedBEext.getVeranstaltungLigaId());
        assertThat(actual.get(0).getVeranstaltungPhase())
                .isEqualTo(expectedBEext.getVeranstaltungPhase());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }
}