package de.bogenliga.application.business.Passe.impl.dao;

import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.Passe.impl.entity.PasseBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static de.bogenliga.application.business.Passe.impl.business.PasseComponentImplTest.getPasseBE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class PasseDAOTest {

    @Test
    public void create() {
    }


    private static final String PASSE_ID = "TEST";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private BasicDAO basicDao;
    @InjectMocks
    private PasseDAO underTest;


    @Test
    public void findAll() {
        // prepare test data
        final PasseBE expectedBE = getPasseBE();

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<PasseBE> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getId())
                .isEqualTo(expectedBE.getId());

        assertThat(actual.get(0).getPasseDsbMitgliedId())
                .isEqualTo(expectedBE.getPasseDsbMitgliedId());


        assertThat(actual.get(0).getPasseMannschaftId())
                .isEqualTo(expectedBE.getPasseMannschaftId());

        assertThat(actual.get(0).getPasseLfdnr())
                .isEqualTo(expectedBE.getPasseLfdnr());

        assertThat(actual.get(0).getPasseWettkampfId())
                .isEqualTo(expectedBE.getPasseWettkampfId());


        assertThat(actual.get(0).getPfeil1())
                .isEqualTo(expectedBE.getPfeil1());

        assertThat(actual.get(0).getPfeil2())
                .isEqualTo(expectedBE.getPfeil2());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());

    }

}