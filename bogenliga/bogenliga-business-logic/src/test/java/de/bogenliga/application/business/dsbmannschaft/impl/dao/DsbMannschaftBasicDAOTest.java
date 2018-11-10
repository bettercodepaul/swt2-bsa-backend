package de.bogenliga.application.business.dsbmannschaft.impl.dao;

import de.bogenliga.application.business.dsbmannschaft.impl.entity.DsbMannschaftBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;
import java.util.List;

import static de.bogenliga.application.business.dsbmannschaft.impl.business.DsbMannschaftComponentImplTest.getDsbMannschaftBE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DsbMannschaftBasicDAOTest {

    private static final Long USER = 0L;
    private static final Long VERSION = 0L;

    private static final long id = 2222L;
    private static final long vereinId=101010;
    private static final long nummer=111;
    private static final long benutzerId=12;
    private static final long veranstaltungId=1;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private BasicDAO basicDao;
    @InjectMocks
    private DsbMannschaftDAO underTest;

    @Test
    public void findAll() {
        // prepare test data
        final DsbMannschaftBE expectedBE = getDsbMannschaftBE();

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<DsbMannschaftBE> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getId())
                .isEqualTo(expectedBE.getId());
        assertThat(actual.get(0).getNummer())
                .isEqualTo(expectedBE.getNummer());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());


    }
}
