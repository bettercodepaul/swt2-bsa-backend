package de.bogenliga.application.business.disziplin.impl.business;

import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import de.bogenliga.application.business.disziplin.api.types.DisziplinDO;
import de.bogenliga.application.business.disziplin.impl.BaseDisziplinTest;
import de.bogenliga.application.business.disziplin.impl.dao.DisziplinDAO;
import de.bogenliga.application.business.disziplin.impl.entity.DisziplinBE;
import de.bogenliga.application.business.disziplin.impl.mapper.DisziplinMapper;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * @author Nick Kerschagel
 */
public class DisziplinComponentImplTest extends BaseDisziplinTest {

    @Mock
    private DisziplinDAO disziplinDAO;

    @InjectMocks
    private DisziplinComponentImpl underTest;


    private void validateObjectList (List<DisziplinDO> actual) {
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();
    }

    private void assertValid(DisziplinBE expectedDisziplinBE, DisziplinDO actual) {
        assertThat(actual).isNotNull();
        assertThat(actual.getDisziplinID()).isEqualTo(expectedDisziplinBE.getId()).isEqualTo(DISZIPLIN_ID);
        assertThat(actual.getDisziplinName()).isEqualTo(expectedDisziplinBE.getName()).isEqualTo(DISZIPLIN_NAME);
    }


    @Test
    public void findAll() {
        DisziplinBE expectedDisziplinBE = getDisziplinBE();

        final List<DisziplinBE> expectedBEList = Collections.singletonList(expectedDisziplinBE);

        // configure mocks
        when(disziplinDAO.findAll()).thenReturn(expectedBEList);

        // call test method
        final List<DisziplinDO> actual = underTest.findAll();

        // assert result
        validateObjectList(actual);
    }


    @Test
    public void findById() {
        DisziplinBE expectedDisziplinBE = getDisziplinBE();

        // configure mocks to return the expectedMatchBE when findByPK is called
        when(disziplinDAO.findById(anyLong())).thenReturn(expectedDisziplinBE);

        final DisziplinDO actual = underTest.findById(DISZIPLIN_ID);

        // assert result
        assertValid(expectedDisziplinBE, actual);

        verify(disziplinDAO).findById(DISZIPLIN_ID);
    }


    @Test
    public void create() {
        DisziplinBE expectedDisziplinBE = getDisziplinBE();
        when(disziplinDAO.create(any(DisziplinBE.class), anyLong())).thenReturn(expectedDisziplinBE);
        DisziplinDO disziplinDO = DisziplinMapper.toDisziplinDO.apply(expectedDisziplinBE);
        DisziplinDO actual = underTest.create(disziplinDO, CURRENT_USER_ID);

        // assert result
        assertValid(expectedDisziplinBE, actual);

        // cannot verify invocation as BE is different in component impl method (from mapper)
    }


    @Test
    public void update() {
        DisziplinBE expectedDisziplinBE = getDisziplinBE();
        // configure mocks to return the expectedMatchBE when findByPK is called
        when(disziplinDAO.findById(anyLong())).thenReturn(expectedDisziplinBE);
        when(disziplinDAO.update(any(DisziplinBE.class), anyLong())).thenReturn(expectedDisziplinBE);
        DisziplinDO disziplinDO = DisziplinMapper.toDisziplinDO.apply(expectedDisziplinBE);
        DisziplinDO actual = underTest.update(disziplinDO, CURRENT_USER_ID);

        // assert result
        assertValid(expectedDisziplinBE, actual);

        // cannot verify invocation as BE is different in component impl method (from mapper)
    }


    @Test
    public void delete() {
        DisziplinBE expectedDisziplinBE = getDisziplinBE();
        DisziplinDO disziplinDO = DisziplinMapper.toDisziplinDO.apply(expectedDisziplinBE);
        underTest.delete(disziplinDO, CURRENT_USER_ID);
    }
}