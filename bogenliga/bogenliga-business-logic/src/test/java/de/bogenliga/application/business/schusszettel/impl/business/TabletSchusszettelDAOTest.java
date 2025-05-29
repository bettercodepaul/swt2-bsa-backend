package de.bogenliga.application.business.schusszettel.impl.business;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.bogenliga.application.business.schusszettel.impl.dao.TabletSchusszettelDAO;
import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;

/**
 * Test class for the TabletSchusszettelDAO
 *
 * @author Marty Lauterbach
 */
public class TabletSchusszettelDAOTest {

    private static final Long ID = 1L;
    private static final String TOKEN = "test-token-123";
    private static final Long TEAM_ID = 10L;
    private static final Long WETTKAMPF_ID = 20L;
    private static final Long MATCH_ID = 30L;
    private static final Integer MATCH_NUMBER = 1;
    private static final Integer PASSE_NUMBER = 1;
    private static final String STATUS = "ACTIVE";
    private static final Long GEGNER_TEAM_ID = 11L;
    private static final Long CURRENT_USER_ID = 100L;

    @Mock
    private BasicDAO basicDao;

    @InjectMocks
    private TabletSchusszettelDAO underTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private TabletSchusszettelEntity getEntity() {
        TabletSchusszettelEntity entity = new TabletSchusszettelEntity();
        entity.setId(ID);
        entity.setToken(TOKEN);
        entity.setTeamId(TEAM_ID);
        entity.setWettkampfId(WETTKAMPF_ID);
        entity.setCurrentMatchId(MATCH_ID);
        entity.setCurrentMatchNumber(MATCH_NUMBER);
        entity.setCurrentPasseNumber(PASSE_NUMBER);
        entity.setStatus(STATUS);
        entity.setLastUpdatedNow();
        entity.setGegnerTeamId(GEGNER_TEAM_ID);
        return entity;
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindByTokenWettkampfUndTeam_whenEntryExists_thenReturnEntity() {
        // prepare test data
        TabletSchusszettelEntity expected = getEntity();
        List<TabletSchusszettelEntity> entityList = Collections.singletonList(expected);

        // configure mocks with proper raw type to avoid generic issues
        when(basicDao.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), any(Object.class), any(Object.class), any(Object.class)))
                .thenReturn(entityList);

        // call test method
        final var actual = underTest.findByTokenWettkampfUndTeam(WETTKAMPF_ID, TEAM_ID, TOKEN);

        // verify results
        assertThat(actual).isPresent();
        assertThat(actual.get()).isEqualTo(expected);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindByTokenWettkampfUndTeam_whenEntryDoesNotExist_thenReturnEmpty() {
        // configure mocks with empty list and proper raw type
        when(basicDao.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), any(Object.class), any(Object.class), any(Object.class)))
                .thenReturn(Collections.emptyList());

        // call test method
        final var actual = underTest.findByTokenWettkampfUndTeam(WETTKAMPF_ID, TEAM_ID, TOKEN);

        // verify results
        assertThat(actual).isEmpty();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindByWettkampfUndTeam_whenEntryExists_thenReturnEntity() {
        // prepare test data
        TabletSchusszettelEntity expected = getEntity();
        List<TabletSchusszettelEntity> entityList = Collections.singletonList(expected);

        // configure mocks with proper raw type
        when(basicDao.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), any(Object.class), any(Object.class)))
                .thenReturn(entityList);

        // call test method
        final var actual = underTest.findByWettkampfUndTeam(WETTKAMPF_ID, TEAM_ID);

        // verify results
        assertThat(actual).isPresent();
        assertThat(actual.get()).isEqualTo(expected);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindByWettkampfUndTeam_whenEntryDoesNotExist_thenReturnEmpty() {
        // configure mocks with raw types
        when(basicDao.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), any(Object.class), any(Object.class)))
                .thenReturn(Collections.emptyList());

        // call test method
        final var actual = underTest.findByWettkampfUndTeam(WETTKAMPF_ID, TEAM_ID);

        // verify results
        assertThat(actual).isEmpty();
    }

    @Test
    public void testCreateSession() {
        // prepare test data
        TabletSchusszettelEntity input = getEntity();
        TabletSchusszettelEntity expected = getEntity();

        // configure mocks
        when(basicDao.insertEntity(any(), any())).thenReturn(expected);

        // call test method
        final var actual = underTest.createSession(input, CURRENT_USER_ID);

        // verify results
        verify(basicDao).setCreationAttributes(input, CURRENT_USER_ID);
        verify(basicDao).insertEntity(any(), eq(input));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testUpdateStatus() {
        // prepare test data
        TabletSchusszettelEntity input = getEntity();
        TabletSchusszettelEntity expected = getEntity();

        // configure mocks
        when(basicDao.updateEntity(any(), any(), anyString())).thenReturn(expected);

        // call test method
        final var actual = underTest.updateStatus(input, CURRENT_USER_ID);

        // verify results
        verify(basicDao).setModificationAttributes(input, CURRENT_USER_ID);
        verify(basicDao).updateEntity(any(), eq(input), eq("id"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testSetCurrentMatchId_entity() {
        // prepare test data
        TabletSchusszettelEntity input = getEntity();
        TabletSchusszettelEntity expected = getEntity();

        // configure mocks
        when(basicDao.updateEntity(any(), any(), anyString())).thenReturn(expected);

        // call test method
        final var actual = underTest.setCurrentMatchId(input, CURRENT_USER_ID);

        // verify results
        verify(basicDao).setModificationAttributes(input, CURRENT_USER_ID);
        verify(basicDao).updateEntity(any(), eq(input), eq("id"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testSetCurrentMatchNumber_entity() {
        // prepare test data
        TabletSchusszettelEntity input = getEntity();
        TabletSchusszettelEntity expected = getEntity();

        // configure mocks
        when(basicDao.updateEntity(any(), any(), anyString())).thenReturn(expected);

        // call test method
        final var actual = underTest.setCurrentMatchNumber(input, CURRENT_USER_ID);

        // verify results
        verify(basicDao).setModificationAttributes(input, CURRENT_USER_ID);
        verify(basicDao).updateEntity(any(), eq(input), eq("id"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testSetCurrentPasseNumber_entity() {
        // prepare test data
        TabletSchusszettelEntity input = getEntity();
        TabletSchusszettelEntity expected = getEntity();

        // configure mocks
        when(basicDao.updateEntity(any(), any(), anyString())).thenReturn(expected);

        // call test method
        final var actual = underTest.setCurrentPasseNumber(input, CURRENT_USER_ID);

        // verify results
        verify(basicDao).setModificationAttributes(input, CURRENT_USER_ID);
        verify(basicDao).updateEntity(any(), eq(input), eq("id"));
        assertThat(actual).isEqualTo(expected);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeleteByWettkampfId() {
        // prepare test data
        TabletSchusszettelEntity entity = getEntity();
        List<TabletSchusszettelEntity> entityList = Collections.singletonList(entity);

        // configure mocks with raw type
        when(basicDao.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), any(Object.class)))
                .thenReturn(entityList);

        // call test method
        underTest.deleteByWettkampfId(WETTKAMPF_ID);

        // verify results
        verify(basicDao).selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), eq(WETTKAMPF_ID));
        verify(basicDao).deleteEntity(any(), eq(entity), eq("id"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testExistsByWettkampfId_true() {
        // prepare test data
        List<TabletSchusszettelEntity> entityList = Collections.singletonList(getEntity());

        // configure mocks with raw type
        when(basicDao.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), any(Object.class)))
                .thenReturn(entityList);

        // call test method
        boolean result = underTest.existsByWettkampfId(WETTKAMPF_ID);

        // verify results
        assertThat(result).isTrue();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testExistsByWettkampfId_false() {
        // configure mocks with raw type
        when(basicDao.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), any(Object.class)))
                .thenReturn(Collections.emptyList());

        // call test method
        boolean result = underTest.existsByWettkampfId(WETTKAMPF_ID);

        // verify results
        assertThat(result).isFalse();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testExistsByWettkampfIdAndTeamId_true() {
        // prepare test data
        List<TabletSchusszettelEntity> entityList = Collections.singletonList(getEntity());

        // configure mocks with raw type
        when(basicDao.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), any(Object.class), any(Object.class)))
                .thenReturn(entityList);

        // call test method
        boolean result = underTest.existsByWettkampfIdAndTeamId(WETTKAMPF_ID, TEAM_ID);

        // verify results
        assertThat(result).isTrue();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testExistsByWettkampfIdAndTeamId_false() {
        // configure mocks with raw type
        when(basicDao.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), any(Object.class), any(Object.class)))
                .thenReturn(Collections.emptyList());

        // call test method
        boolean result = underTest.existsByWettkampfIdAndTeamId(WETTKAMPF_ID, TEAM_ID);

        // verify results
        assertThat(result).isFalse();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSetToken() {
        // prepare test data
        TabletSchusszettelEntity entity = getEntity();
        String newToken = "new-token-456";

        // configure mocks with raw type
        when(basicDao.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), any(Object.class), any(Object.class)))
                .thenReturn(Collections.singletonList(entity));
        when(basicDao.updateEntity(any(), any(), anyString())).thenReturn(entity);

        // call test method
        final var actual = underTest.setToken(WETTKAMPF_ID, TEAM_ID, newToken, CURRENT_USER_ID);

        // verify results
        ArgumentCaptor<TabletSchusszettelEntity> captor = ArgumentCaptor.forClass(TabletSchusszettelEntity.class);
        verify(basicDao).updateEntity(any(), captor.capture(), eq("id"));
        assertThat(captor.getValue().getToken()).isEqualTo(newToken);
        assertThat(actual).isEqualTo(entity);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSetToken_throwsException_whenEntityNotFound() {
        // configure mocks with raw type
        when(basicDao.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), any(Object.class), any(Object.class)))
                .thenReturn(Collections.emptyList());

        // call test method and verify exception
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> underTest.setToken(WETTKAMPF_ID, TEAM_ID, "new-token", CURRENT_USER_ID))
                .withMessageContaining("Keine Tablet-Session");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSetGegnerTeamId() {
        // prepare test data
        TabletSchusszettelEntity entity = getEntity();
        Long newGegnerTeamId = 12L;

        // configure mocks with raw type
        when(basicDao.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), any(Object.class), any(Object.class)))
                .thenReturn(Collections.singletonList(entity));
        when(basicDao.updateEntity(any(), any(), anyString())).thenReturn(entity);

        // call test method
        final var actual = underTest.setGegnerTeamId(WETTKAMPF_ID, TEAM_ID, newGegnerTeamId, CURRENT_USER_ID);

        // verify results
        ArgumentCaptor<TabletSchusszettelEntity> captor = ArgumentCaptor.forClass(TabletSchusszettelEntity.class);
        verify(basicDao).updateEntity(any(), captor.capture(), eq("id"));
        assertThat(captor.getValue().getGegnerTeamId()).isEqualTo(newGegnerTeamId);
        assertThat(actual).isEqualTo(entity);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSetCurrentMatchId_ids() {
        // prepare test data
        TabletSchusszettelEntity entity = getEntity();
        Long newMatchId = 31L;

        // configure mocks with raw type
        when(basicDao.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), any(Object.class), any(Object.class)))
                .thenReturn(Collections.singletonList(entity));
        when(basicDao.updateEntity(any(), any(), anyString())).thenReturn(entity);

        // call test method
        final var actual = underTest.setCurrentMatchId(WETTKAMPF_ID, TEAM_ID, newMatchId, CURRENT_USER_ID);

        // verify results
        ArgumentCaptor<TabletSchusszettelEntity> captor = ArgumentCaptor.forClass(TabletSchusszettelEntity.class);
        verify(basicDao).updateEntity(any(), captor.capture(), eq("id"));
        assertThat(captor.getValue().getCurrentMatchId()).isEqualTo(newMatchId);
        assertThat(actual).isEqualTo(entity);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSetCurrentMatchNumber_ids() {
        // prepare test data
        TabletSchusszettelEntity entity = getEntity();
        int newMatchNumber = 2;

        // configure mocks with raw type
        when(basicDao.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), any(Object.class), any(Object.class)))
                .thenReturn(Collections.singletonList(entity));
        when(basicDao.updateEntity(any(), any(), anyString())).thenReturn(entity);

        // call test method
        final var actual = underTest.setCurrentMatchNumber(WETTKAMPF_ID, TEAM_ID, newMatchNumber, CURRENT_USER_ID);

        // verify results
        ArgumentCaptor<TabletSchusszettelEntity> captor = ArgumentCaptor.forClass(TabletSchusszettelEntity.class);
        verify(basicDao).updateEntity(any(), captor.capture(), eq("id"));
        assertThat(captor.getValue().getCurrentMatchNumber()).isEqualTo(Long.valueOf(newMatchNumber));
        assertThat(actual).isEqualTo(entity);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSetCurrentPasseNumber_ids() {
        // prepare test data
        TabletSchusszettelEntity entity = getEntity();
        Integer newPasseNumber = 2;

        // configure mocks with raw type
        when(basicDao.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), any(Object.class), any(Object.class)))
                .thenReturn(Collections.singletonList(entity));
        when(basicDao.updateEntity(any(), any(), anyString())).thenReturn(entity);

        // call test method
        final var actual = underTest.setCurrentPasseNumber(WETTKAMPF_ID, TEAM_ID, newPasseNumber, CURRENT_USER_ID);

        // verify results
        ArgumentCaptor<TabletSchusszettelEntity> captor = ArgumentCaptor.forClass(TabletSchusszettelEntity.class);
        verify(basicDao).updateEntity(any(), captor.capture(), eq("id"));
        assertThat(captor.getValue().getCurrentPasseNumber()).isEqualTo(newPasseNumber);
        assertThat(actual).isEqualTo(entity);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSetStatus_ids() {
        // prepare test data
        TabletSchusszettelEntity entity = getEntity();
        String newStatus = "COMPLETED";

        // configure mocks with raw type
        when(basicDao.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), any(Object.class), any(Object.class)))
                .thenReturn(Collections.singletonList(entity));
        when(basicDao.updateEntity(any(), any(), anyString())).thenReturn(entity);

        // call test method
        final var actual = underTest.setStatus(WETTKAMPF_ID, TEAM_ID, newStatus, CURRENT_USER_ID);

        // verify results
        ArgumentCaptor<TabletSchusszettelEntity> captor = ArgumentCaptor.forClass(TabletSchusszettelEntity.class);
        verify(basicDao).updateEntity(any(), captor.capture(), eq("id"));
        assertThat(captor.getValue().getStatus()).isEqualTo(newStatus);
        assertThat(actual).isEqualTo(entity);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUpdateLastUpdated() {
        // prepare test data
        TabletSchusszettelEntity entity = getEntity();

        // configure mocks with raw type
        when(basicDao.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), any(Object.class), any(Object.class)))
                .thenReturn(Collections.singletonList(entity));
        when(basicDao.updateEntity(any(), any(), anyString())).thenReturn(entity);

        // call test method
        final var actual = underTest.updateLastUpdated(WETTKAMPF_ID, TEAM_ID, CURRENT_USER_ID);

        // verify results
        verify(basicDao).setModificationAttributes(any(), eq(CURRENT_USER_ID));
        verify(basicDao).updateEntity(any(), any(), eq("id"));
        assertThat(actual).isEqualTo(entity);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindByWettkampfId() {
        // prepare test data
        TabletSchusszettelEntity entity = getEntity();
        List<TabletSchusszettelEntity> expectedList = Collections.singletonList(entity);

        // configure mocks with raw type
        when(basicDao.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), any(Object.class)))
                .thenReturn(expectedList);

        // call test method
        final var actualList = underTest.findByWettkampfId(WETTKAMPF_ID);

        // verify results
        verify(basicDao).selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), eq(WETTKAMPF_ID));
        assertThat(actualList).isEqualTo(expectedList);
    }
}
