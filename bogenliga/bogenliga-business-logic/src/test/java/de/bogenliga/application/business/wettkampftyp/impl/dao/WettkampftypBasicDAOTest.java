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

    private static final long user_Id=13;
    private static final OffsetDateTime created_At_Utc = OffsetDateTime.now();
    private static final long version = 1234;

    private static final long wettkampftyp_Id = 1;
    private static final String wettkampftyp_Name = "Liga Satzsystem";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private BasicDAO basicDao;
    @InjectMocks
    private WettkampfTypDAO underTest;

    @Test
    public void findAll() {
        // prepare test data
        final WettkampfTypBE expectedBE = getWettkampfTypBE();

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<WettkampfTypBE> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getwettkampftypID())
                .isEqualTo(expectedBE.getwettkampftypID());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());


    }


    @Test
    public void findById() {
        // prepare test data
        final WettkampfTypBE expectedBE = new WettkampfTypBE();
        expectedBE.setwettkampftypID(wettkampftyp_Id);
        expectedBE.setwettkampftypname(wettkampftyp_Name);

        // configure mocks
        when(basicDao.selectSingleEntity(any(), any(), any())).thenReturn(expectedBE);

        // call test method
        final WettkampfTypBE actual = underTest.findById(wettkampftyp_Id);

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
        input.setwettkampftypID(wettkampftyp_Id);
        input.setwettkampftypname(wettkampftyp_Name);

        // configure mocks
        when(basicDao.insertEntity(any(), any())).thenReturn(input);

        // call test method
        final WettkampfTypBE actual = underTest.create(input, user_Id);

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
        input.setwettkampftypID(wettkampftyp_Id);
        input.setwettkampftypname(wettkampftyp_Name);

        // configure mocks
        when(basicDao.updateEntity(any(), any(), any())).thenReturn(input);

        // call test method
        final WettkampfTypBE actual = underTest.update(input, user_Id);

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
        input.setwettkampftypID(wettkampftyp_Id);
        input.setwettkampftypname(wettkampftyp_Name);

        // configure mocks

        // call test method
        underTest.delete(input, user_Id);

        // assert result

        // verify invocations
        verify(basicDao).deleteEntity(any(), eq(input), any());
    }

}
