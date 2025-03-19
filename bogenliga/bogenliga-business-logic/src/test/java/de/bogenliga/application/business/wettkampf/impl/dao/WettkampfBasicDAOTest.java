package de.bogenliga.application.business.wettkampf.impl.dao;


import de.bogenliga.application.business.wettkampf.impl.entity.WettkampfBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static de.bogenliga.application.business.wettkampf.impl.business.WettkampfComponentImplTest.getWettkampfBE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class WettkampfBasicDAOTest {

    private static final long USER_ID=13;

    private static final long WETTKAMPF_ID = 322;
    private static final long WETTKAMPF_WETTKAMPFTYP_ID = 1;
    private static final String WETTKAMPF_OFFLINETOKEN = "offlineToken";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private BasicDAO basicDao;
    @InjectMocks
    private WettkampfDAO underTest;

    @Test
    public void findAll() {
        // prepare test data
        final WettkampfBE expectedBE = getWettkampfBE();

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<WettkampfBE> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getId())
                .isEqualTo(expectedBE.getId());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());


    }


    @Test
    public void findById() {
        // prepare test data
        final WettkampfBE expectedBE = new WettkampfBE();
        expectedBE.setId(WETTKAMPF_ID);
        expectedBE.setWettkampfTypId(WETTKAMPF_WETTKAMPFTYP_ID);

        // configure mocks
        when(basicDao.selectSingleEntity(any(), any(), any())).thenReturn(expectedBE);

        // call test method
        final WettkampfBE actual = underTest.findById(WETTKAMPF_ID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(expectedBE.getId());
        assertThat(actual.getWettkampfTypId())
                .isEqualTo(expectedBE.getWettkampfTypId());

        // verify invocations
        verify(basicDao).selectSingleEntity(any(), any(), any());
    }


    @Test
    public void create() {
        // prepare test data
        final WettkampfBE input = new WettkampfBE();
        input.setId(WETTKAMPF_ID);
        input.setWettkampfTypId(WETTKAMPF_WETTKAMPFTYP_ID);

        // configure mocks
        when(basicDao.insertEntity(any(), any())).thenReturn(input);

        // call test method
        final WettkampfBE actual = underTest.create(input, USER_ID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(input.getId());
        assertThat(actual.getWettkampfTypId())
                .isEqualTo(input.getWettkampfTypId());

        // verify invocations
        verify(basicDao).insertEntity(any(), eq(input));
    }


    @Test
    public void update() {
        // prepare test data
        final WettkampfBE input = new WettkampfBE();
        input.setId(WETTKAMPF_ID);
        input.setWettkampfTypId(WETTKAMPF_WETTKAMPFTYP_ID);

        // configure mocks
        when(basicDao.updateEntity(any(), any(), any())).thenReturn(input);

        // call test method
        final WettkampfBE actual = underTest.update(input, USER_ID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(input.getId());
        assertThat(actual.getWettkampfTypId())
                .isEqualTo(input.getWettkampfTypId());

        // verify invocations
        verify(basicDao).updateEntity(any(), eq(input), any());
    }


    @Test
    public void delete() {
        // prepare test data
        final WettkampfBE input = new WettkampfBE();
        input.setId(WETTKAMPF_ID);
        input.setWettkampfTypId(WETTKAMPF_WETTKAMPFTYP_ID);

        // configure mocks

        // call test method
        underTest.delete(input, USER_ID);

        // assert result

        // verify invocations
        verify(basicDao).deleteEntity(any(), eq(input), any());
    }

    @Test
    public void checkOfflineToken() {

        final WettkampfBE expectedBE = getWettkampfBE();
        when(basicDao.selectSingleEntity(any(), any(), any())).thenReturn(expectedBE);

        WettkampfBE actual = underTest.checkOfflineToken(WETTKAMPF_ID, WETTKAMPF_OFFLINETOKEN);

        assertThat(actual).isNotNull();
        assertThat(actual.getOfflineToken()).isEqualTo(WETTKAMPF_OFFLINETOKEN);
        assertThat(actual.getId()).isEqualTo(WETTKAMPF_ID);

        final WettkampfBE invalidToken = getWettkampfBE();
        invalidToken.setOfflineToken(null);
        when(basicDao.selectSingleEntity(any(), any(), any(), any())).thenReturn(null);

        WettkampfBE given = underTest.checkOfflineToken(WETTKAMPF_ID, WETTKAMPF_OFFLINETOKEN);
        assertThat(given).isNull();

        verify(basicDao, times(2)).selectSingleEntity(any(), any(), any());
    }


}
