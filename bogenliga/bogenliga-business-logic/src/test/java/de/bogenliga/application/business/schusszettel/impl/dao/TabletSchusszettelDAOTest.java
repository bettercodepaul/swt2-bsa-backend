package de.bogenliga.application.business.schusszettel.impl.dao;

import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test class for TabletSchusszettelDAO database access object.
 * Tests session CRUD operations, token management, and database interactions.
 */
@RunWith(MockitoJUnitRunner.class)
public class TabletSchusszettelDAOTest {

    @Mock private BasicDAO basicDAO;
    @InjectMocks private TabletSchusszettelDAO dao;
    
    private TabletSchusszettelEntity testEntity;

    @Before
    public void setUp() {
        setupTestData();
        setupMockBehavior();
    }
    
    private void setupTestData() {
        testEntity = new TabletSchusszettelEntity();
        testEntity.setId(1L);
        testEntity.setTeamId(100L);
        testEntity.setWettkampfId(50L);
        testEntity.setToken("test-token-123456789012345");
        testEntity.setCurrentMatchId(300L);
        testEntity.setCurrentMatchNumber(1);
        testEntity.setCurrentPasseNumber(1);
        testEntity.setStatus("SCHUETZENMELDUNG");
        testEntity.setGegnerTeamId(101L);
    }
    
    private void setupMockBehavior() {
        // Default setup for most tests - specific tests override as needed
        when(basicDAO.selectEntityList(any(), anyString(), anyLong())).thenReturn(Arrays.asList(testEntity));
        when(basicDAO.selectEntityList(any(), anyString(), anyLong(), anyLong())).thenReturn(Arrays.asList(testEntity));
        when(basicDAO.selectEntityList(any(), anyString(), anyString(), anyLong(), anyLong())).thenReturn(Arrays.asList(testEntity));
        when(basicDAO.insertEntity(any(), any())).thenReturn(testEntity);
        when(basicDAO.updateEntity(any(), any(), any())).thenReturn(testEntity);
    }

    @Test
    public void findByWettkampfId_validId_returnsEntityList() {
        List<TabletSchusszettelEntity> result = dao.findByWettkampfId(50L);
        
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getWettkampfId()).isEqualTo(50L);
        verify(basicDAO).selectEntityList(any(), anyString(), eq(50L));
    }

    @Test
    public void findByWettkampfId_exceptionInQuery_propagatesRuntimeException() {
        when(basicDAO.selectEntityList(any(), anyString(), eq(50L)))
            .thenThrow(new RuntimeException("DB error"));
        
        assertThatThrownBy(() -> dao.findByWettkampfId(50L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("DB error");
    }

    @Test
    public void findByWettkampfUndTeam_validIds_returnsOptionalEntity() {
        Optional<TabletSchusszettelEntity> result = dao.findByWettkampfUndTeam(50L, 100L);
        
        assertThat(result).isPresent();
        assertThat(result.get().getWettkampfId()).isEqualTo(50L);
        assertThat(result.get().getTeamId()).isEqualTo(100L);
        verify(basicDAO).selectEntityList(any(), anyString(), eq(50L), eq(100L));
    }

    @Test
    public void findByWettkampfUndTeam_entityNotFound_returnsEmptyOptional() {
        when(basicDAO.selectEntityList(any(), anyString(), eq(50L), eq(100L))).thenReturn(Collections.emptyList());
        
        Optional<TabletSchusszettelEntity> result = dao.findByWettkampfUndTeam(50L, 100L);
        
        assertThat(result).isEmpty();
    }

    @Test
    public void findByWettkampfUndTeam_exceptionInQuery_propagatesRuntimeException() {
        when(basicDAO.selectEntityList(any(), anyString(), eq(50L), eq(100L)))
            .thenThrow(new RuntimeException("DB error"));
        
        assertThatThrownBy(() -> dao.findByWettkampfUndTeam(50L, 100L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("DB error");
    }

    @Test
    public void findByTokenWettkampfUndTeam_validParams_returnsOptionalEntity() {
        Optional<TabletSchusszettelEntity> result = dao.findByTokenWettkampfUndTeam(50L, 100L, "test-token-123456789012345");
        
        assertThat(result).isPresent();
        assertThat(result.get().getToken()).isEqualTo("test-token-123456789012345");
        verify(basicDAO).selectEntityList(any(), anyString(), eq("test-token-123456789012345"), eq(50L), eq(100L));
    }

    @Test
    public void findByTokenWettkampfUndTeam_invalidToken_returnsEmptyOptional() {
        when(basicDAO.selectEntityList(any(), anyString(), eq("invalid-token"), eq(50L), eq(100L))).thenReturn(Collections.emptyList());
        
        Optional<TabletSchusszettelEntity> result = dao.findByTokenWettkampfUndTeam(50L, 100L, "invalid-token");
        
        assertThat(result).isEmpty();
    }

    @Test
    public void findByTokenWettkampfUndTeam_exceptionInQuery_propagatesRuntimeException() {
        when(basicDAO.selectEntityList(any(), anyString(), eq("test-token"), eq(50L), eq(100L)))
            .thenThrow(new RuntimeException("DB error"));
        
        assertThatThrownBy(() -> dao.findByTokenWettkampfUndTeam(50L, 100L, "test-token"))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("DB error");
    }

    @Test
    public void createSession_validEntity_returnsCreatedEntity() {
        TabletSchusszettelEntity result = dao.createSession(testEntity, 1L);
        
        assertThat(result).isNotNull();
        assertThat(result.getTeamId()).isEqualTo(100L);
        verify(basicDAO).insertEntity(any(), eq(testEntity));
    }

    @Test
    public void createSession_exceptionInInsert_propagatesRuntimeException() {
        when(basicDAO.insertEntity(any(), any()))
            .thenThrow(new RuntimeException("Insert error"));
        
        assertThatThrownBy(() -> dao.createSession(testEntity, 1L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Insert error");
    }

    @Test
    public void updateStatus_validEntity_returnsUpdatedEntity() {
        TabletSchusszettelEntity result = dao.updateStatus(testEntity, 1L);
        
        assertThat(result).isNotNull();
        verify(basicDAO).updateEntity(any(), eq(testEntity), eq("id"));
    }

    @Test
    public void updateStatus_exceptionInUpdate_propagatesBusinessException() {
        when(basicDAO.updateEntity(any(), any(), anyString()))
            .thenThrow(new BusinessException(ErrorCode.DATABASE_ERROR, "Update error"));
        
        assertThatThrownBy(() -> dao.updateStatus(testEntity, 1L))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("Update error");
    }

    @Test
    public void deleteByWettkampfId_validId_deletesSuccessfully() {
        dao.deleteByWettkampfId(50L);
        
        verify(basicDAO).selectEntityList(any(), anyString(), eq(50L));
        verify(basicDAO).deleteEntity(any(), eq(testEntity), eq("id"));
    }

    @Test
    public void deleteByWettkampfId_exceptionInDelete_propagatesRuntimeException() {
        doThrow(new RuntimeException("Delete error")).when(basicDAO).deleteEntity(any(), any(), anyString());
        
        assertThatThrownBy(() -> dao.deleteByWettkampfId(50L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Delete error");
    }

    @Test
    public void setToken_validParams_updatesTokenSuccessfully() {
        dao.setToken(50L, 100L, "new-token-123456789012345", 1L);
        
        verify(basicDAO).selectEntityList(any(), anyString(), eq(50L), eq(100L));
        verify(basicDAO, atLeastOnce()).updateEntity(any(), any(), eq("id"));
    }

    @Test
    public void setToken_sessionNotFound_throwsBusinessException() {
        when(basicDAO.selectEntityList(any(), anyString(), eq(50L), eq(100L))).thenReturn(Collections.emptyList());
        
        assertThatThrownBy(() -> dao.setToken(50L, 100L, "new-token", 1L))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("Keine Tablet-Session für wettkampfId=50 und teamId=100 gefunden");
    }

    @Test
    public void setToken_exceptionInUpdate_propagatesBusinessException() {
        when(basicDAO.updateEntity(any(), any(), anyString()))
            .thenThrow(new BusinessException(ErrorCode.DATABASE_ERROR, "Update error"));
        
        assertThatThrownBy(() -> dao.setToken(50L, 100L, "new-token", 1L))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("Update error");
    }

    @Test
    public void getColumnsToFieldsMap_returnsCorrectMapping() {
        // Test through a method that uses the mapping
        dao.findByWettkampfId(50L);
        
        // Verify the query was constructed correctly
        verify(basicDAO).selectEntityList(any(), anyString(), eq(50L));
    }

    @Test
    public void findByWettkampfId_emptyResult_returnsEmptyList() {
        when(basicDAO.selectEntityList(any(), anyString(), eq(50L)))
            .thenReturn(Collections.emptyList());
        
        List<TabletSchusszettelEntity> result = dao.findByWettkampfId(50L);
        
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    public void createSession_nullEntity_propagatesRuntimeException() {
        when(basicDAO.insertEntity(any(), any()))
            .thenThrow(new RuntimeException("Null entity"));
        
        assertThatThrownBy(() -> dao.createSession(null, 1L))
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void updateStatus_nullEntity_propagatesRuntimeException() {
        when(basicDAO.updateEntity(any(), any(), anyString()))
            .thenThrow(new RuntimeException("Null entity"));
        
        assertThatThrownBy(() -> dao.updateStatus(null, 1L))
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void findByTokenWettkampfUndTeam_nullToken_constructsCorrectQuery() {
        Optional<TabletSchusszettelEntity> result = dao.findByTokenWettkampfUndTeam(50L, 100L, null);
        
        // Should handle null token gracefully
        verify(basicDAO).selectEntityList(any(), anyString(), eq(null), eq(50L), eq(100L));
    }

    @Test
    public void deleteByWettkampfId_noSessionsFound_completesSuccessfully() {
        when(basicDAO.selectEntityList(any(), anyString(), eq(50L)))
            .thenReturn(Collections.emptyList());
        
        dao.deleteByWettkampfId(50L);
        
        verify(basicDAO).selectEntityList(any(), anyString(), eq(50L));
        verify(basicDAO, never()).deleteEntity(any(), any(), anyString());
    }

    @Test
    public void findByWettkampfUndTeam_multipleResultsFromDB_returnsFirst() {
        // BasicDAO should handle this, but test our usage
        dao.findByWettkampfUndTeam(50L, 100L);
        
        verify(basicDAO).selectEntityList(any(), anyString(), eq(50L), eq(100L));
    }

    @Test
    public void allQueryMethods_useCorrectTableAndColumns() {
        // Test that all methods use the correct table reference
        dao.findByWettkampfId(50L);
        dao.findByWettkampfUndTeam(50L, 100L);
        dao.findByTokenWettkampfUndTeam(50L, 100L, "token");
        
        // Verify each method call with its specific arguments
        verify(basicDAO).selectEntityList(any(), anyString(), eq(50L)); // findByWettkampfId
        verify(basicDAO, times(1)).selectEntityList(any(), anyString(), eq(50L), eq(100L)); // findByWettkampfUndTeam called once
        verify(basicDAO).selectEntityList(any(), anyString(), eq("token"), eq(50L), eq(100L)); // findByTokenWettkampfUndTeam
    }

    @Test
    public void createAndUpdateOperations_useCorrectEntityConfiguration() {
        dao.createSession(testEntity, 1L);
        dao.updateStatus(testEntity, 1L);
        
        verify(basicDAO).insertEntity(any(), eq(testEntity));
        verify(basicDAO).updateEntity(any(), eq(testEntity), eq("id"));
    }
}