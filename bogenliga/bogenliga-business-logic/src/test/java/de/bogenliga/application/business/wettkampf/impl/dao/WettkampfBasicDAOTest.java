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

    private static final long user_Id=13;
    private static final OffsetDateTime created_At_Utc = OffsetDateTime.now();
    private static final long version = 1234;

    private static final long wettkampf_Id = 322;
    private static final long wettkampf_Veranstaltung_Id = 0;
    private static final String wettkampf_Datum = "2019-05-21";
    private static final String wettkampf_Strasse = "Teststrasse 4";
    private static final String wettkampf_Plz = "72768";
    private static final String wettkampf_Ortsname = "Testort";
    private static final String wettkampf_Ortsinfo = "hinter dem busch";
    private static final String wettkampf_Beginn ="8:00";
    private static final long wettkampf_Tag = 8;
    private static final long wettkampf_Disziplin_Id = 0;
    private static final long wettkampf_Wettkampftyp_Id = 1;

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
        expectedBE.setId(wettkampf_Id);
        expectedBE.setWettkampfTypId(wettkampf_Wettkampftyp_Id);

        // configure mocks
        when(basicDao.selectSingleEntity(any(), any(), any())).thenReturn(expectedBE);

        // call test method
        final WettkampfBE actual = underTest.findById(wettkampf_Id);

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
        input.setId(wettkampf_Id);
        input.setWettkampfTypId(wettkampf_Wettkampftyp_Id);

        // configure mocks
        when(basicDao.insertEntity(any(), any())).thenReturn(input);

        // call test method
        final WettkampfBE actual = underTest.create(input, user_Id);

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
        input.setId(wettkampf_Id);
        input.setWettkampfTypId(wettkampf_Wettkampftyp_Id);

        // configure mocks
        when(basicDao.updateEntity(any(), any(), any())).thenReturn(input);

        // call test method
        final WettkampfBE actual = underTest.update(input, user_Id);

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
        input.setId(wettkampf_Id);
        input.setWettkampfTypId(wettkampf_Wettkampftyp_Id);

        // configure mocks

        // call test method
        underTest.delete(input, user_Id);

        // assert result

        // verify invocations
        verify(basicDao).deleteEntity(any(), eq(input), any());
    }



}
