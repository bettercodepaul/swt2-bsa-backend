package de.bogenliga.application.business.vereine.impl.business;


import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.vereine.impl.dao.VereinDAO;
import de.bogenliga.application.business.vereine.impl.dao.VereinDAOext;
import de.bogenliga.application.business.vereine.impl.entity.VereinBE;
import de.bogenliga.application.business.vereine.impl.entity.VereinBEext;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class VereinComponentImplTest {

    private static final Long USER = 0L;
    private static final Long VERSION = 0L;

    private static final long VEREIN_ID = 0;
    private static final String VEREIN_NAME = "";
    private static final String VEREIN_DSB_IDENTIFIER = "";
    private static final long VEREIN_REGION_ID = 0;
    private static final String VEREIN_WEBSITE = "";
    private static final String VEREIN_DESCRIPTION = "";
    private static final String VEREIN_ICON = "";
    private static final long USER_ID = 0;
    private static final OffsetDateTime VEREIN_OFFSETDATETIME = null;
    private static final String REGION_NAME = "Qualityland";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private VereinDAO vereinDAO;
    @Mock
    private VereinDAOext vereinDAOext;
    @InjectMocks
    private VereinComponentImpl underTest;
    @Captor
    private ArgumentCaptor<VereinBE> vereinBEArgumentCaptor;

    public static VereinBE getVereinBE() {
        final VereinBE expectedBE = new VereinBE();
        expectedBE.setVereinName(VEREIN_NAME);
        expectedBE.setVereinId(VEREIN_ID);
        expectedBE.setVereinRegionId(VEREIN_REGION_ID);
        expectedBE.setVereinDsbIdentifier(VEREIN_DSB_IDENTIFIER);
        expectedBE.setVereinWebsite(VEREIN_WEBSITE);
        expectedBE.setVereinDescription(VEREIN_DESCRIPTION);
        expectedBE.setVereinIcon(VEREIN_ICON);

        return expectedBE;
    }
    public static VereinBEext getVereinBEext() {
        final VereinBEext expectedBE = new VereinBEext();
        expectedBE.setVereinName(VEREIN_NAME);
        expectedBE.setVereinId(VEREIN_ID);
        expectedBE.setVereinRegionId(VEREIN_REGION_ID);
        expectedBE.setVereinDsbIdentifier(VEREIN_DSB_IDENTIFIER);
        expectedBE.setVereinWebsite(VEREIN_WEBSITE);
        expectedBE.setVereinDescription(VEREIN_DESCRIPTION);
        expectedBE.setVereinIcon(VEREIN_ICON);
        expectedBE.setVereinRegionName(REGION_NAME);

        return expectedBE;
    }

    public static VereinDO getVereinDO() {
        return new VereinDO(VEREIN_ID, VEREIN_NAME, VEREIN_DSB_IDENTIFIER, VEREIN_REGION_ID, REGION_NAME,
                VEREIN_WEBSITE, VEREIN_DESCRIPTION, VEREIN_ICON, VEREIN_OFFSETDATETIME, USER_ID,
                VEREIN_OFFSETDATETIME,USER_ID, VERSION);
    }


    @Test
    public void findAll() {
        // prepare test data
        final VereinBEext expectedBE = getVereinBEext();
        final List<VereinBEext> expectedVereinBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(vereinDAOext.findAll()).thenReturn(expectedVereinBEList);

        // call test method
        final List<VereinDO> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getId())
                .isEqualTo(expectedBE.getVereinId());
        assertThat(actual.get(0).getName())
                .isEqualTo(expectedBE.getVereinName());
        assertThat(actual.get(0).getRegionId())
                .isEqualTo(expectedBE.getVereinRegionId());
        assertThat(actual.get(0).getDsbIdentifier())
                .isEqualTo(expectedBE.getVereinDsbIdentifier());
        assertThat(actual.get(0).getWebsite())
                .isEqualTo(expectedBE.getVereinWebsite());
        assertThat(actual.get(0).getDescription())
                .isEqualTo(expectedBE.getVereinDescription());
        assertThat(actual.get(0).getIcon())
                .isEqualTo(expectedBE.getVereinIcon());
        assertThat(actual.get(0).getRegionName())
                .isEqualTo(expectedBE.getVereinRegionName());

        // verify invocations
        verify(vereinDAOext).findAll();
    }

    @Test
    public void findBySearch() {
        // prepare test data
        final VereinBEext expectedBE = getVereinBEext();
        final List<VereinBEext> expectedVereinBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(vereinDAOext.findBySearch(expectedBE.getVereinName())).thenReturn(expectedVereinBEList);
        // call test method
        final List<VereinDO> actual = underTest.findBySearch(expectedBE.getVereinName());

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        // verify invocations
        verify(vereinDAOext).findBySearch(expectedBE.getVereinName());
    }


    @Test
    public void findById() {
        // prepare test data
        final VereinBEext expectedBE = getVereinBEext();

        // configure mocks
        when(vereinDAOext.findById(VEREIN_ID)).thenReturn(expectedBE);

        final VereinDO actual = underTest.findById(VEREIN_ID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(expectedBE.getVereinId());

        assertThat(actual.getRegionId()).isEqualTo(expectedBE.getVereinRegionId());
        assertThat(actual.getRegionName()).isEqualTo(expectedBE.getVereinRegionName());

        // verify invocations
        verify(vereinDAOext).findById(VEREIN_ID);
    }



    @Test
    public void create() {
        // prepare test data
        final VereinDO input = getVereinDO();

        final VereinBE expectedBE = getVereinBE();

        // configure mocks
        when(vereinDAO.create(any(VereinBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final VereinDO actual = underTest.create(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(input.getId());

        // verify invocations
        verify(vereinDAO).create(vereinBEArgumentCaptor.capture(), anyLong());

        final VereinBE persistedBE = vereinBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getVereinId())
                .isEqualTo(input.getId());
    }

    @Test
    public void create_duplicateVereinConstraint_shouldThrowConflict() {
        final VereinDO input = getVereinDO();

        when(vereinDAO.create(any(VereinBE.class), anyLong()))
                .thenThrow(new TechnicalException(ErrorCode.DATABASE_ERROR,
                        "duplicate key value violates unique constraint \"uc_verein_dsb_identifier\""));

        assertThatThrownBy(() -> underTest.create(input, USER))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.ENTITY_CONFLICT_ERROR.getValue());
    }

    @Test
    public void create_duplicateVereinNameConstraint_shouldThrowConflict() {
        final VereinDO input = getVereinDO();

        when(vereinDAO.create(any(VereinBE.class), anyLong()))
                .thenThrow(new TechnicalException(ErrorCode.DATABASE_ERROR,
                        "duplicate key value violates unique constraint \"uc_verein_name\""));

        assertThatThrownBy(() -> underTest.create(input, USER))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.ENTITY_CONFLICT_ERROR.getValue());
    }

    @Test
    public void create_nonDuplicateDatabaseError_shouldRethrowTechnicalException() {
        final VereinDO input = getVereinDO();

        when(vereinDAO.create(any(VereinBE.class), anyLong()))
                .thenThrow(new TechnicalException(ErrorCode.DATABASE_ERROR, "other database error"));

        assertThatThrownBy(() -> underTest.create(input, USER))
                .isInstanceOf(TechnicalException.class)
                .hasMessageContaining("other database error");
    }

    @Test
    public void update() {
        // prepare test data
        final VereinDO input = getVereinDO();

        final VereinBE expectedBE = getVereinBE();

        // configure mocks
        when(vereinDAO.update(any(VereinBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final VereinDO actual = underTest.update(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(input.getId());
        assertThat(actual.getName())
                .isEqualTo(input.getName());

        // verify invocations
        verify(vereinDAO).update(vereinBEArgumentCaptor.capture(), anyLong());

        final VereinBE persistedBE = vereinBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getVereinId())
                .isEqualTo(input.getId());
        assertThat(persistedBE.getVereinName())
                .isEqualTo(input.getName());
    }

    @Test
    public void update_duplicateVereinConstraint_shouldThrowConflict() {
        final VereinDO input = getVereinDO();

        when(vereinDAO.update(any(VereinBE.class), anyLong()))
                .thenThrow(new TechnicalException(ErrorCode.DATABASE_ERROR,
                        "duplicate key value violates unique constraint \"uc_verein_dsb_identifier\""));

        assertThatThrownBy(() -> underTest.update(input, USER))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.ENTITY_CONFLICT_ERROR.getValue());
    }

    @Test
    public void update_duplicateVereinNameConstraint_shouldThrowConflict() {
        final VereinDO input = getVereinDO();

        when(vereinDAO.update(any(VereinBE.class), anyLong()))
                .thenThrow(new TechnicalException(ErrorCode.DATABASE_ERROR,
                        "duplicate key value violates unique constraint \"uc_verein_name\""));

        assertThatThrownBy(() -> underTest.update(input, USER))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.ENTITY_CONFLICT_ERROR.getValue());
    }

    @Test
    public void update_nonDuplicateDatabaseError_shouldRethrowTechnicalException() {
        final VereinDO input = getVereinDO();

        when(vereinDAO.update(any(VereinBE.class), anyLong()))
                .thenThrow(new TechnicalException(ErrorCode.DATABASE_ERROR, "other database error"));

        assertThatThrownBy(() -> underTest.update(input, USER))
                .isInstanceOf(TechnicalException.class)
                .hasMessageContaining("other database error");
    }

    @Test
    public void delete() {
        final VereinDO input = getVereinDO();


        // call test method
        underTest.delete(input, USER);

        // assert result

        // verify invocations
        verify(vereinDAO).delete(vereinBEArgumentCaptor.capture(), anyLong());

        final VereinBE persistedBE = vereinBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getVereinId())
                .isEqualTo(input.getId());
    }

    @Test
    public void equals(){
             VereinDO underTest = new VereinDO(VEREIN_ID, VEREIN_NAME, VEREIN_DSB_IDENTIFIER, VEREIN_REGION_ID,
                REGION_NAME, VEREIN_WEBSITE, VEREIN_DESCRIPTION, VEREIN_ICON,
                VEREIN_OFFSETDATETIME, USER_ID, VEREIN_OFFSETDATETIME, USER_ID,
                VERSION);
         assertThat(underTest.getRegionName()).isEqualTo(getVereinDO().getRegionName());
         assertEquals(underTest,getVereinDO());
    }
}
