package de.bogenliga.application.business.schusszettel.impl.dao;

import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive tests for the TabletSchusszettelDAO class.
 * Tests all actual DAO methods using proven patterns from deprecated tests.
 */
public class TabletSchusszettelDAOTest {

    private static final Long WETTKAMPF_ID = 50L;
    private static final Long TEAM_ID = 100L;
    private static final Long OPPONENT_TEAM_ID = 200L;
    private static final String TOKEN = "validToken123";
    private static final Long CURRENT_USER_ID = 1L;

    @Mock
    private BasicDAO basicDAO;

    @InjectMocks
    private TabletSchusszettelDAO tabletSchusszettelDAO;

    private TabletSchusszettelEntity testEntity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testEntity = createTestEntity();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findByTokenWettkampfUndTeam_withValidParameters_shouldReturnEntity() {
        // Arrange
        when(basicDAO.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), 
                any(Object.class), any(Object.class), any(Object.class)))
            .thenReturn(Collections.singletonList(testEntity));
        
        // Act
        Optional<TabletSchusszettelEntity> result = tabletSchusszettelDAO.findByTokenWettkampfUndTeam(WETTKAMPF_ID, TEAM_ID, TOKEN);
        
        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getToken()).isEqualTo(testEntity.getToken());
        verify(basicDAO).selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), 
                eq(TOKEN), eq(WETTKAMPF_ID), eq(TEAM_ID));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findByTokenWettkampfUndTeam_withNoMatch_shouldReturnEmpty() {
        // Arrange
        when(basicDAO.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), 
                any(Object.class), any(Object.class), any(Object.class)))
            .thenReturn(Collections.emptyList());
        
        // Act
        Optional<TabletSchusszettelEntity> result = tabletSchusszettelDAO.findByTokenWettkampfUndTeam(WETTKAMPF_ID, TEAM_ID, TOKEN);
        
        // Assert
        assertThat(result).isEmpty();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findByWettkampfUndTeam_withValidIds_shouldReturnEntity() {
        // Arrange
        when(basicDAO.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), 
                any(Object.class), any(Object.class)))
            .thenReturn(Collections.singletonList(testEntity));
        
        // Act
        Optional<TabletSchusszettelEntity> result = tabletSchusszettelDAO.findByWettkampfUndTeam(WETTKAMPF_ID, TEAM_ID);
        
        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getWettkampfId()).isEqualTo(testEntity.getWettkampfId());
        assertThat(result.get().getTeamId()).isEqualTo(testEntity.getTeamId());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findByWettkampfUndTeam_withNoMatch_shouldReturnEmpty() {
        // Arrange
        when(basicDAO.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), 
                any(Object.class), any(Object.class)))
            .thenReturn(Collections.emptyList());
        
        // Act
        Optional<TabletSchusszettelEntity> result = tabletSchusszettelDAO.findByWettkampfUndTeam(WETTKAMPF_ID, TEAM_ID);
        
        // Assert
        assertThat(result).isEmpty();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findByWettkampfId_withValidId_shouldReturnEntityList() {
        // Arrange
        TabletSchusszettelEntity entity2 = createTestEntity();
        entity2.setTeamId(TEAM_ID + 1);
        List<TabletSchusszettelEntity> expectedList = Arrays.asList(testEntity, entity2);
        when(basicDAO.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), 
                any(Object.class))).thenReturn(expectedList);
        
        // Act
        List<TabletSchusszettelEntity> result = tabletSchusszettelDAO.findByWettkampfId(WETTKAMPF_ID);
        
        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(expectedList);
        verify(basicDAO).selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), 
                eq(WETTKAMPF_ID));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findByWettkampfId_withNoMatches_shouldReturnEmptyList() {
        // Arrange
        when(basicDAO.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), 
                any(Object.class))).thenReturn(Collections.emptyList());
        
        // Act
        List<TabletSchusszettelEntity> result = tabletSchusszettelDAO.findByWettkampfId(WETTKAMPF_ID);
        
        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    public void createSession_withValidEntity_shouldReturnCreatedEntity() {
        // Arrange
        TabletSchusszettelEntity newEntity = createTestEntity();
        newEntity.setId(null); // New entity should not have ID
        
        TabletSchusszettelEntity createdEntity = createTestEntity();
        createdEntity.setId(1L); // Created entity gets ID from database
        
        when(basicDAO.insertEntity(any(BusinessEntityConfiguration.class), eq(newEntity))).thenReturn(createdEntity);
        
        // Act
        TabletSchusszettelEntity result = tabletSchusszettelDAO.createSession(newEntity, CURRENT_USER_ID);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(basicDAO).setCreationAttributes(eq(newEntity), eq(CURRENT_USER_ID));
        verify(basicDAO).insertEntity(any(BusinessEntityConfiguration.class), eq(newEntity));
    }

    @Test
    public void updateStatus_withValidEntity_shouldReturnUpdatedEntity() {
        // Arrange
        TabletSchusszettelEntity existingEntity = createTestEntity();
        existingEntity.setId(1L);
        existingEntity.setStatus("SATZEINGABE");
        
        when(basicDAO.updateEntity(any(BusinessEntityConfiguration.class), eq(existingEntity), any(String.class))).thenReturn(existingEntity);
        
        // Act
        TabletSchusszettelEntity result = tabletSchusszettelDAO.updateStatus(existingEntity, CURRENT_USER_ID);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo("SATZEINGABE");
        verify(basicDAO).setModificationAttributes(eq(existingEntity), eq(CURRENT_USER_ID));
        verify(basicDAO).updateEntity(any(BusinessEntityConfiguration.class), eq(existingEntity), eq("id"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void setToken_withExistingSession_shouldUpdateToken() {
        // Arrange
        String newToken = "newToken456";
        when(basicDAO.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), 
                any(Object.class), any(Object.class)))
            .thenReturn(Collections.singletonList(testEntity));
        when(basicDAO.updateEntity(any(BusinessEntityConfiguration.class), any(), any(String.class))).thenReturn(testEntity);
        
        // Act
        TabletSchusszettelEntity result = tabletSchusszettelDAO.setToken(WETTKAMPF_ID, TEAM_ID, newToken, CURRENT_USER_ID);
        
        // Assert
        assertThat(result).isNotNull();
        ArgumentCaptor<TabletSchusszettelEntity> captor = ArgumentCaptor.forClass(TabletSchusszettelEntity.class);
        verify(basicDAO).updateEntity(any(BusinessEntityConfiguration.class), captor.capture(), eq("id"));
        assertThat(captor.getValue().getToken()).isEqualTo(newToken);
        assertThat(result).isEqualTo(testEntity);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void setToken_withNonExistingSession_shouldThrowBusinessException() {
        // Arrange
        String newToken = "newToken456";
        when(basicDAO.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), 
                any(Object.class), any(Object.class)))
            .thenReturn(Collections.emptyList());
        
        // Act & Assert
        assertThatThrownBy(() -> tabletSchusszettelDAO.setToken(WETTKAMPF_ID, TEAM_ID, newToken, CURRENT_USER_ID))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("Keine Tablet-Session");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void existsByWettkampfId_withExistingSessions_shouldReturnTrue() {
        // Arrange
        when(basicDAO.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), 
                any(Object.class))).thenReturn(Collections.singletonList(testEntity));
        
        // Act
        boolean result = tabletSchusszettelDAO.existsByWettkampfId(WETTKAMPF_ID);
        
        // Assert
        assertThat(result).isTrue();
        verify(basicDAO).selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), 
                eq(WETTKAMPF_ID));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void existsByWettkampfId_withNoSessions_shouldReturnFalse() {
        // Arrange
        when(basicDAO.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), 
                any(Object.class))).thenReturn(Collections.emptyList());
        
        // Act
        boolean result = tabletSchusszettelDAO.existsByWettkampfId(WETTKAMPF_ID);
        
        // Assert
        assertThat(result).isFalse();
        verify(basicDAO).selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), 
                eq(WETTKAMPF_ID));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void deleteByWettkampfId_withValidId_shouldDeleteAllSessions() {
        // Arrange
        List<TabletSchusszettelEntity> entitiesToDelete = Arrays.asList(testEntity, createTestEntity());
        when(basicDAO.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), 
                any(Object.class))).thenReturn(entitiesToDelete);
        
        // Act
        tabletSchusszettelDAO.deleteByWettkampfId(WETTKAMPF_ID);
        
        // Assert
        verify(basicDAO).selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), 
                eq(WETTKAMPF_ID));
        verify(basicDAO, times(2)).deleteEntity(any(BusinessEntityConfiguration.class), any(), eq("id"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void existsByWettkampfIdAndTeamId_withExistingSession_shouldReturnTrue() {
        // Arrange
        when(basicDAO.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), 
                any(Object.class), any(Object.class)))
            .thenReturn(Collections.singletonList(testEntity));
        
        // Act
        boolean result = tabletSchusszettelDAO.existsByWettkampfIdAndTeamId(WETTKAMPF_ID, TEAM_ID);
        
        // Assert
        assertThat(result).isTrue();
        verify(basicDAO).selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), 
                eq(WETTKAMPF_ID), eq(TEAM_ID));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void existsByWettkampfIdAndTeamId_withNoSession_shouldReturnFalse() {
        // Arrange
        when(basicDAO.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), 
                any(Object.class), any(Object.class)))
            .thenReturn(Collections.emptyList());
        
        // Act
        boolean result = tabletSchusszettelDAO.existsByWettkampfIdAndTeamId(WETTKAMPF_ID, TEAM_ID);
        
        // Assert
        assertThat(result).isFalse();
        verify(basicDAO).selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), 
                eq(WETTKAMPF_ID), eq(TEAM_ID));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void setGegnerTeamId_withExistingSession_shouldUpdateGegnerTeamId() {
        // Arrange
        Long newGegnerTeamId = 300L;
        when(basicDAO.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), 
                any(Object.class), any(Object.class)))
            .thenReturn(Collections.singletonList(testEntity));
        when(basicDAO.updateEntity(any(BusinessEntityConfiguration.class), any(), any(String.class))).thenReturn(testEntity);
        
        // Act
        TabletSchusszettelEntity result = tabletSchusszettelDAO.setGegnerTeamId(WETTKAMPF_ID, TEAM_ID, newGegnerTeamId, CURRENT_USER_ID);
        
        // Assert
        assertThat(result).isNotNull();
        ArgumentCaptor<TabletSchusszettelEntity> captor = ArgumentCaptor.forClass(TabletSchusszettelEntity.class);
        verify(basicDAO).updateEntity(any(BusinessEntityConfiguration.class), captor.capture(), eq("id"));
        assertThat(captor.getValue().getGegnerTeamId()).isEqualTo(newGegnerTeamId);
        assertThat(result).isEqualTo(testEntity);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void setCurrentMatchId_withExistingSession_shouldUpdateCurrentMatchId() {
        // Arrange
        Long newMatchId = 400L;
        when(basicDAO.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), 
                any(Object.class), any(Object.class)))
            .thenReturn(Collections.singletonList(testEntity));
        when(basicDAO.updateEntity(any(BusinessEntityConfiguration.class), any(), any(String.class))).thenReturn(testEntity);
        
        // Act
        TabletSchusszettelEntity result = tabletSchusszettelDAO.setCurrentMatchId(WETTKAMPF_ID, TEAM_ID, newMatchId, CURRENT_USER_ID);
        
        // Assert
        assertThat(result).isNotNull();
        ArgumentCaptor<TabletSchusszettelEntity> captor = ArgumentCaptor.forClass(TabletSchusszettelEntity.class);
        verify(basicDAO).updateEntity(any(BusinessEntityConfiguration.class), captor.capture(), eq("id"));
        assertThat(captor.getValue().getCurrentMatchId()).isEqualTo(newMatchId);
        assertThat(result).isEqualTo(testEntity);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void setStatus_withExistingSession_shouldUpdateStatus() {
        // Arrange
        String newStatus = "WARTE";
        when(basicDAO.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), 
                any(Object.class), any(Object.class)))
            .thenReturn(Collections.singletonList(testEntity));
        when(basicDAO.updateEntity(any(BusinessEntityConfiguration.class), any(), any(String.class))).thenReturn(testEntity);
        
        // Act
        TabletSchusszettelEntity result = tabletSchusszettelDAO.setStatus(WETTKAMPF_ID, TEAM_ID, newStatus, CURRENT_USER_ID);
        
        // Assert
        assertThat(result).isNotNull();
        ArgumentCaptor<TabletSchusszettelEntity> captor = ArgumentCaptor.forClass(TabletSchusszettelEntity.class);
        verify(basicDAO).updateEntity(any(BusinessEntityConfiguration.class), captor.capture(), eq("id"));
        assertThat(captor.getValue().getStatus()).isEqualTo(newStatus);
        assertThat(result).isEqualTo(testEntity);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void updateLastUpdated_withExistingSession_shouldUpdateLastUpdated() {
        // Arrange
        when(basicDAO.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), 
                any(Object.class), any(Object.class)))
            .thenReturn(Collections.singletonList(testEntity));
        when(basicDAO.updateEntity(any(BusinessEntityConfiguration.class), any(), any(String.class))).thenReturn(testEntity);
        
        // Act
        TabletSchusszettelEntity result = tabletSchusszettelDAO.updateLastUpdated(WETTKAMPF_ID, TEAM_ID, CURRENT_USER_ID);
        
        // Assert
        assertThat(result).isNotNull();
        verify(basicDAO).setModificationAttributes(any(TabletSchusszettelEntity.class), eq(CURRENT_USER_ID));
        verify(basicDAO).updateEntity(any(BusinessEntityConfiguration.class), any(TabletSchusszettelEntity.class), eq("id"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void updateLastUpdated_withNonExistingSession_shouldThrowBusinessException() {
        // Arrange
        when(basicDAO.selectEntityList(any(BusinessEntityConfiguration.class), any(String.class), 
                any(Object.class), any(Object.class)))
            .thenReturn(Collections.emptyList());
        
        // Act & Assert
        assertThatThrownBy(() -> tabletSchusszettelDAO.updateLastUpdated(WETTKAMPF_ID, TEAM_ID, CURRENT_USER_ID))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("Keine Tablet-Session");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void createSessionsBatch_withValidEntities_shouldCreateAllSessions() {
        // Arrange
        TabletSchusszettelEntity entity1 = createTestEntity();
        entity1.setId(null);
        TabletSchusszettelEntity entity2 = createTestEntity();
        entity2.setId(null);
        entity2.setTeamId(TEAM_ID + 1);
        
        List<TabletSchusszettelEntity> entities = Arrays.asList(entity1, entity2);
        
        TabletSchusszettelEntity created1 = createTestEntity();
        created1.setId(1L);
        TabletSchusszettelEntity created2 = createTestEntity();
        created2.setId(2L);
        created2.setTeamId(TEAM_ID + 1);
        
        when(basicDAO.insertEntity(any(BusinessEntityConfiguration.class), eq(entity1))).thenReturn(created1);
        when(basicDAO.insertEntity(any(BusinessEntityConfiguration.class), eq(entity2))).thenReturn(created2);
        
        // Act
        List<TabletSchusszettelEntity> result = tabletSchusszettelDAO.createSessionsBatch(entities, CURRENT_USER_ID);
        
        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
        verify(basicDAO, times(2)).setCreationAttributes(any(TabletSchusszettelEntity.class), eq(CURRENT_USER_ID));
        verify(basicDAO, times(2)).insertEntity(any(BusinessEntityConfiguration.class), any(TabletSchusszettelEntity.class));
    }

    @Test
    public void createSessionsBatch_withEmptyList_shouldReturnEmptyList() {
        // Act
        List<TabletSchusszettelEntity> result = tabletSchusszettelDAO.createSessionsBatch(Collections.emptyList(), CURRENT_USER_ID);
        
        // Assert
        assertThat(result).isEmpty();
        verify(basicDAO, never()).insertEntity(any(BusinessEntityConfiguration.class), any(TabletSchusszettelEntity.class));
    }

    @Test
    public void createSessionsBatch_withNullList_shouldReturnEmptyList() {
        // Act
        List<TabletSchusszettelEntity> result = tabletSchusszettelDAO.createSessionsBatch(null, CURRENT_USER_ID);
        
        // Assert
        assertThat(result).isEmpty();
        verify(basicDAO, never()).insertEntity(any(BusinessEntityConfiguration.class), any(TabletSchusszettelEntity.class));
    }

    // Helper method for creating test entities - following deprecated test pattern
    private TabletSchusszettelEntity createTestEntity() {
        TabletSchusszettelEntity entity = new TabletSchusszettelEntity();
        entity.setToken(TOKEN);
        entity.setTeamId(TEAM_ID);
        entity.setWettkampfId(WETTKAMPF_ID);
        entity.setCurrentMatchId(300L);
        entity.setCurrentPasseNumber(1);
        entity.setStatus("SCHUETZENMELDUNG");
        entity.setGegnerTeamId(OPPONENT_TEAM_ID);
        return entity;
    }
}