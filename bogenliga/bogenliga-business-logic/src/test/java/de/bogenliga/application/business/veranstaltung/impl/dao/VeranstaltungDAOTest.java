package de.bogenliga.application.business.veranstaltung.impl.dao;

import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungBE;
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

    private static final long VERANSTALTUNGSID= 1L;
    private static final long LIGAID= 2L;
    private static final String VERANSTALTUNGSNAME= "Test Veranstaltung";
    private static final long SPORTJAHR = 2018L;

    @Test
    public void testFindBySportjahrDestinct() {
        //create test data
        VeranstaltungBE expectedBE = new VeranstaltungBE();
        expectedBE.setVeranstaltungId(VERANSTALTUNGSID);
        expectedBE.setVeranstaltungName(VERANSTALTUNGSNAME);
        expectedBE.setVeranstaltungLigaId(LIGAID);

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

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }
}