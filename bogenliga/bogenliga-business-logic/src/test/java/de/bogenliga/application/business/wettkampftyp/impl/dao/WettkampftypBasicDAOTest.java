package de.bogenliga.application.business.wettkampftyp.impl.dao;


import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.wettkampftyp.impl.entity.WettkampfTypBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static de.bogenliga.application.business.wettkampftyp.impl.business.WettkampfTypComponentImplTest.getWettkampfTypBE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class WettkampftypBasicDAOTest {

    private static final long USER_ID=13;

    private static final long WETTKAMPFTYP_ID = 1;
    private static final String WETTKAMPFTYP_NAME = "Liga Satzsystem";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private BasicDAO basicDao;
    @InjectMocks
    private WettkampfTypDAO underTest;

    @Test
    public void findAll() {
        // prepare test data
        final WettkampfTypBE EXPECTEDBE = getWettkampfTypBE();

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(EXPECTEDBE));

        // call test method
        final List<WettkampfTypBE> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getwettkampftypID())
                .isEqualTo(EXPECTEDBE.getwettkampftypID());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());


    }


    @Test
    public void findById() {
        // prepare test data
        final WettkampfTypBE expectedBE = new WettkampfTypBE();
        expectedBE.setwettkampftypID(WETTKAMPFTYP_ID);
        expectedBE.setwettkampftypname(WETTKAMPFTYP_NAME);

        // configure mocks
        when(basicDao.selectSingleEntity(any(), any(), any())).thenReturn(expectedBE);

        // call test method
        final WettkampfTypBE actual = underTest.findById(WETTKAMPFTYP_ID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getwettkampftypID())
                .isEqualTo(expectedBE.getwettkampftypID());
        assertThat(actual.getwettkampftypname())
                .isEqualTo(expectedBE.getwettkampftypname());

        // verify invocations
        verify(basicDao).selectSingleEntity(any(), any(), any());
    }


    @Test
    public void create() {
        // prepare test data
        final WettkampfTypBE input = new WettkampfTypBE();
        input.setwettkampftypID(WETTKAMPFTYP_ID);
        input.setwettkampftypname(WETTKAMPFTYP_NAME);

        // configure mocks
        when(basicDao.insertEntity(any(), any())).thenReturn(input);

        // call test method
        final WettkampfTypBE actual = underTest.create(input, USER_ID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getwettkampftypID())
                .isEqualTo(input.getwettkampftypID());
        assertThat(actual.getwettkampftypname())
                .isEqualTo(input.getwettkampftypname());

        // verify invocations
        verify(basicDao).insertEntity(any(), eq(input));
    }


    @Test
    public void update() {
        // prepare test data
        final WettkampfTypBE input = new WettkampfTypBE();
        input.setwettkampftypID(WETTKAMPFTYP_ID);
        input.setwettkampftypname(WETTKAMPFTYP_NAME);

        // configure mocks
        when(basicDao.updateEntity(any(), any(), any())).thenReturn(input);

        // call test method
        final WettkampfTypBE actual = underTest.update(input, USER_ID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getwettkampftypID())
                .isEqualTo(input.getwettkampftypID());
        assertThat(actual.getwettkampftypname())
                .isEqualTo(input.getwettkampftypname());

        // verify invocations
        verify(basicDao).updateEntity(any(), eq(input), any());
    }


    @Test
    public void delete() {
        // prepare test data
        final WettkampfTypBE input = new WettkampfTypBE();
        input.setwettkampftypID(WETTKAMPFTYP_ID);
        input.setwettkampftypname(WETTKAMPFTYP_NAME);

        // configure mocks

        // call test method
        underTest.delete(input, USER_ID);

        // assert result

        // verify invocations
        verify(basicDao).deleteEntity(any(), eq(input), any());
    }

}
