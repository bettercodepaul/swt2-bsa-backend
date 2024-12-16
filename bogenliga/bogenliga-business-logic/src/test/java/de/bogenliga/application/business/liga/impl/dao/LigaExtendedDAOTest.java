package de.bogenliga.application.business.liga.impl.dao;

import de.bogenliga.application.business.liga.impl.entity.LigaBEext;
import de.bogenliga.application.common.component.dao.BasicDAO;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * I'm testing the LigaExtendedDAO class
 * Author: Justin Klein, justin.klein@student.reutlingen-university.de
 */
public class LigaExtendedDAOTest {

    private static final long LIGA_ID = 1337L;
    private static final String LIGA_NAME = "Test Liga";
    private static final String REGION_NAME = "Region Test";
    private static final String DISZIPLIN_NAME = "Disziplin Test";
    private static final String VERANTWORTLICHER_NAME = "test@example.com";
    private static final String SUCHBEGRIFF = "Liga";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private BasicDAO basicDao;

    @InjectMocks
    private LigaDAOext underTest;

    @Test
    public void findEverything() {
        // prepare test data
        final LigaBEext expectedBE = createLigaExtendedBE();

        // configure mocks
        when(basicDao.selectEntityList(any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<LigaBEext> actual = underTest.findEverything();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();
        assertThat(actual.get(0).getLigaId()).isEqualTo(expectedBE.getLigaId());
        assertThat(actual.get(0).getLigaName()).isEqualTo(expectedBE.getLigaName());
        assertThat(actual.get(0).getRegionName()).isEqualTo(expectedBE.getRegionName());
        assertThat(actual.get(0).getDisziplinName()).isEqualTo(expectedBE.getDisziplinName());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any());
    }

    @Test
    public void findEverything_noResults() {
        // configure mocks
        when(basicDao.selectEntityList(any(), any())).thenReturn(Collections.emptyList());

        // call test method
        final List<LigaBEext> actual = underTest.findEverything();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isEmpty();

        // verify invocations
        verify(basicDao).selectEntityList(any(), any());
    }


    @Test(expected = RuntimeException.class)
    public void findEverything_databaseError() {
        // configure mocks
        when(basicDao.selectEntityList(any(), any())).thenThrow(new RuntimeException("Database error"));

        // call test method
        underTest.findEverything();

        // verify invocations
        verify(basicDao).selectEntityList(any(), any());
    }


    @Test
    public void findBySearch() {
        // prepare test data
        final LigaBEext expectedBE = createLigaExtendedBE();

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<LigaBEext> actual = underTest.findBySearch(SUCHBEGRIFF);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();
        assertThat(actual.get(0).getLigaId()).isEqualTo(expectedBE.getLigaId());
        assertThat(actual.get(0).getLigaName()).isEqualTo(expectedBE.getLigaName());
        assertThat(actual.get(0).getRegionName()).isEqualTo(expectedBE.getRegionName());
        assertThat(actual.get(0).getDisziplinName()).isEqualTo(expectedBE.getDisziplinName());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }

    @Test
    public void findBySearch_unexpectedData() {
        // prepare test data
        final LigaBEext unexpectedBE = new LigaBEext();
        unexpectedBE.setLigaId(9999L); // nicht erwartete ID
        unexpectedBE.setLigaName("Unerwartete Liga");

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(unexpectedBE));

        // call test method
        final List<LigaBEext> actual = underTest.findBySearch("Unexpected");

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0).getLigaId()).isEqualTo(9999L);
        assertThat(actual.get(0).getLigaName()).isEqualTo("Unerwartete Liga");

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }

    @Test
    public void findBySearch_sqlInjectionAttempt() {
        // prepare test data
        final String maliciousSearch = "1=1; DROP TABLE liga;";
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.emptyList());

        // call test method
        final List<LigaBEext> actual = underTest.findBySearch(maliciousSearch);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isEmpty();

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }


    @Test
    public void findBySearch_specialCharacters() {
        // prepare test data
        final String specialSearchTerm = "%_\"";
        final LigaBEext expectedBE = createLigaExtendedBE();

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<LigaBEext> actual = underTest.findBySearch(specialSearchTerm);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }

    @Test
    public void findAdditionalDataByLigaId() {
        // prepare test data
        final LigaBEext expectedBE = createLigaExtendedBE();

        // configure mocks
        when(basicDao.selectSingleEntity(any(), any(), eq(LIGA_ID))).thenReturn(expectedBE);

        // call test method
        final LigaBEext actual = underTest.findAdditionalDataByLigaId(LIGA_ID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getLigaId()).isEqualTo(expectedBE.getLigaId());
        assertThat(actual.getLigaName()).isEqualTo(expectedBE.getLigaName());
        assertThat(actual.getRegionName()).isEqualTo(expectedBE.getRegionName());
        assertThat(actual.getDisziplinName()).isEqualTo(expectedBE.getDisziplinName());
        assertThat(actual.getVerantwortlicherName()).isEqualTo(expectedBE.getVerantwortlicherName());

        // verify invocations
        verify(basicDao).selectSingleEntity(any(), any(), eq(LIGA_ID));
    }

    @Test
    public void findAdditionalDataByLigaId_invalidId() {
        // configure mocks
        when(basicDao.selectSingleEntity(any(), any(), eq(LIGA_ID))).thenReturn(null);

        // call test method
        final LigaBEext actual = underTest.findAdditionalDataByLigaId(LIGA_ID);

        // assert result
        assertThat(actual).isNull();

        // verify invocations
        verify(basicDao).selectSingleEntity(any(), any(), eq(LIGA_ID));
    }


    /*
     * Utility method to create a LigaExtendedBE for tests
     */
    private LigaBEext createLigaExtendedBE() {
        final LigaBEext be = new LigaBEext();
        be.setLigaId(LIGA_ID);
        be.setLigaName(LIGA_NAME);
        be.setRegionName(REGION_NAME);
        be.setDisziplinName(DISZIPLIN_NAME);
        be.setVerantwortlicherName(VERANTWORTLICHER_NAME);
        return be;
    }
}
