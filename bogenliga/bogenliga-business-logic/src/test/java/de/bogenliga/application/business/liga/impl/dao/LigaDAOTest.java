package de.bogenliga.application.business.liga.impl.dao;

import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.liga.impl.entity.LigaBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static de.bogenliga.application.business.liga.impl.business.LigaComponentImplTest.getLigaBE;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * I'm testing the LigaDAO class
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
public class LigaDAOTest {
    private static final long USER = 0;

    private static final long ID = 1337;
    private static final String LIGANAME = "Test Liga";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private BasicDAO basicDao;
    @InjectMocks
    private LigaDAO underTest;


    @Test
    public void findAll() {
        // prepare test data
        final LigaBE expectedBE = getLigaBE();

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<LigaBE> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getLigaId())
                .isEqualTo(expectedBE.getLigaId());
        assertThat(actual.get(0).getLigaName())
                .isEqualTo(expectedBE.getLigaName());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }

    @Test
    public void findByLowest(){
        // prepare test data
        final LigaBE expectedBE = getLigaBE();

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<LigaBE> actual = underTest.findByLowest();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getLigaId())
                .isEqualTo(expectedBE.getLigaId());
        assertThat(actual.get(0).getLigaName())
                .isEqualTo(expectedBE.getLigaName());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());

    }

    @Test
    public void findBySearch() {
        // prepare test data
        final LigaBE expectedBE = getLigaBE();

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<LigaBE> actual = underTest.findBySearch(expectedBE.getLigaName());

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getLigaId())
                .isEqualTo(expectedBE.getLigaId());
        assertThat(actual.get(0).getLigaName())
                .isEqualTo(expectedBE.getLigaName());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }

    @Test
    public void findById() {
        // prepare test data
        final LigaBE expectedBE = new LigaBE();
        expectedBE.setLigaId(ID);
        expectedBE.setLigaName(LIGANAME);

        // configure mocks
        when(basicDao.selectSingleEntity(any(), any(), any())).thenReturn(expectedBE);

        // call test method
        final LigaBE actual = underTest.findById(ID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getLigaId())
                .isEqualTo(expectedBE.getLigaId());
        assertThat(actual.getLigaName())
                .isEqualTo(expectedBE.getLigaName());

        // verify invocations
        verify(basicDao).selectSingleEntity(any(), any(), any());
    }
    @Test
    public void checkExistsLigaName() {
        // prepare test data
        final LigaBE expectedBE = getLigaBE();

        // configure mocks
        when(basicDao.selectSingleEntity(any(), any(), any())).thenReturn((expectedBE));

        // call test method
        final LigaBE actual = underTest.findByLigaName(expectedBE.getLigaName());

        // assert result
        assertThat(actual)
                .isNotNull();

        assertThat(actual).isNotNull();

        assertThat(actual.getLigaId())
                .isEqualTo(expectedBE.getLigaId());
        assertThat(actual.getLigaName())
                .isEqualTo(expectedBE.getLigaName());

        // verify invocations
        verify(basicDao).selectSingleEntity(any(), any(), any());
    }

    @Test
    public void create() {
        // prepare test data
        final LigaBE input = new LigaBE();
        input.setLigaId(ID);
        input.setLigaName(LIGANAME);


        // configure mocks
        when(basicDao.insertEntity(any(), any())).thenReturn(input);

        // call test method
        final LigaBE actual = underTest.create(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getLigaId())
                .isEqualTo(input.getLigaId());
        assertThat(actual.getLigaName())
                .isEqualTo(input.getLigaName());

        // verify invocations
        verify(basicDao).insertEntity(any(), eq(input));
    }

    @Test
    public void update() {
        // prepare test data
        final LigaBE input = new LigaBE();
        input.setLigaId(ID);
        input.setLigaName(LIGANAME);

        // configure mocks
        when(basicDao.updateEntity(any(), any(), any())).thenReturn(input);

        // call test method
        final LigaBE actual = underTest.update(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getLigaId())
                .isEqualTo(input.getLigaId());
        assertThat(actual.getLigaName())
                .isEqualTo(input.getLigaName());

        // verify invocations
        verify(basicDao).updateEntity(any(), eq(input), any());
    }

    @Test
    public void delete() {
        // prepare test data
        final LigaBE input = new LigaBE();
        input.setLigaId(ID);
        input.setLigaName(LIGANAME);

        // configure mocks

        // call test method
        underTest.delete(input, USER);

        // assert result

        // verify invocations
        verify(basicDao).deleteEntity(any(), eq(input), any());
    }
}