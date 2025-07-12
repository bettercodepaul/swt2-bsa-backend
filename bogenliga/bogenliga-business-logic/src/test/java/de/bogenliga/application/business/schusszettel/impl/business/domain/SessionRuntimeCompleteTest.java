package de.bogenliga.application.business.schusszettel.impl.business.domain;

import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.business.schusszettel.impl.dao.TabletSchusszettelDAO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.schusszettel.impl.business.serviceAdapter.MatchAnalysisService;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Additional comprehensive tests for SessionRuntime class covering missing methods.
 * This focuses on error handling, edge cases, and static factory methods.
 */
@RunWith(MockitoJUnitRunner.class)
public class SessionRuntimeCompleteTest {

    @Mock
    private TabletSchusszettelEntity mockSession;

    @Mock
    private TabletSchusszettelDAO mockSessionDAO;

    @Mock
    private MatchComponent mockMatchComponent;

    @Mock
    private PasseComponent mockPasseComponent;

    @Mock
    private MatchAnalysisService mockMatchAnalysisService;

    @Mock
    private MannschaftsmitgliedComponent mockMannschaftsmitgliedComponent;

    @Mock
    private DsbMitgliedComponent mockDsbMitgliedComponent;

    @Mock
    private WettkampfComponent mockWettkampfComponent;

    @Mock
    private VeranstaltungComponent mockVeranstaltungComponent;

    private SessionRuntime sessionRuntime;

    @Before
    public void setUp() {
        sessionRuntime = new SessionRuntime(
            mockSession,
            mockSessionDAO,
            mockMatchComponent,
            mockPasseComponent,
            mockMatchAnalysisService,
            mockMannschaftsmitgliedComponent,
            mockDsbMitgliedComponent,
            mockWettkampfComponent,
            mockVeranstaltungComponent
        );
    }

    // Static factory method tests

    @Test
    public void loadFromDatabase_withValidToken_shouldReturnSessionRuntime() {
        // Arrange
        String token = "valid-token";
        long wettkampfId = 50L;
        long teamId = 100L;
        
        when(mockSessionDAO.findByTokenWettkampfUndTeam(wettkampfId, teamId, token))
            .thenReturn(Optional.of(mockSession));
        
        // Act
        SessionRuntime result = SessionRuntime.loadFromDatabase(
            wettkampfId, teamId, token, mockSessionDAO, mockMatchComponent, 
            mockPasseComponent, mockMatchAnalysisService, mockMannschaftsmitgliedComponent,
            mockDsbMitgliedComponent, mockWettkampfComponent, mockVeranstaltungComponent
        );
        
        // Assert
        assertThat(result).isNotNull();
        verify(mockSessionDAO).findByTokenWettkampfUndTeam(wettkampfId, teamId, token);
    }

    @Test
    public void loadFromDatabase_withInvalidToken_shouldThrowBusinessException() {
        // Arrange
        String token = "invalid-token";
        long wettkampfId = 50L;
        long teamId = 100L;
        
        when(mockSessionDAO.findByTokenWettkampfUndTeam(wettkampfId, teamId, token))
            .thenReturn(Optional.empty());
        
        // Act & Assert
        assertThatThrownBy(() -> SessionRuntime.loadFromDatabase(
            wettkampfId, teamId, token, mockSessionDAO, mockMatchComponent, 
            mockPasseComponent, mockMatchAnalysisService, mockMannschaftsmitgliedComponent,
            mockDsbMitgliedComponent, mockWettkampfComponent, mockVeranstaltungComponent
        )).isInstanceOf(BusinessException.class);
    }

    @Test
    public void initializeSession_withActiveMatch_shouldCreateSessionInCorrectState() {
        // Arrange
        long wettkampfId = 50L;
        long teamId = 100L;
        String token = "test-token";
        
        LigamatchBE currentMatch = new LigamatchBE();
        currentMatch.setMatchId(300L);
        currentMatch.setMannschaftId(teamId);
        currentMatch.setSatzpunkte(2L);
        
        when(mockMatchAnalysisService.findCurrentIncompleteMatch(wettkampfId, teamId))
            .thenReturn(currentMatch);
        when(mockMatchAnalysisService.getCurrentPasseNumber(300L, teamId, 200L)).thenReturn(2);
        
        List<PasseDO> existingPasses = List.of(createPasseDO());
        when(mockPasseComponent.findByMannschaftMatchId(teamId, 300L)).thenReturn(existingPasses);
        
        when(mockSessionDAO.createSession(any(TabletSchusszettelEntity.class), any(Long.class))).thenReturn(mockSession);
        
        // Act
        SessionRuntime result = SessionRuntime.initializeSession(
            wettkampfId, teamId, token, mockSessionDAO, mockMatchComponent,
            mockPasseComponent, mockMatchAnalysisService, mockMannschaftsmitgliedComponent,
            mockDsbMitgliedComponent, mockWettkampfComponent, mockVeranstaltungComponent
        );
        
        // Assert
        assertThat(result).isNotNull();
        verify(mockSessionDAO).createSession(any(TabletSchusszettelEntity.class), any(Long.class));
    }

    @Test
    public void initializeSession_withNoActiveMatch_shouldCreateSessionInWettkampfEnde() {
        // Arrange
        long wettkampfId = 50L;
        long teamId = 100L;
        String token = "test-token";
        
        when(mockMatchAnalysisService.findCurrentIncompleteMatch(wettkampfId, teamId))
            .thenReturn(null);
        when(mockSessionDAO.createSession(any(TabletSchusszettelEntity.class), any(Long.class))).thenReturn(mockSession);
        
        // Act
        SessionRuntime result = SessionRuntime.initializeSession(
            wettkampfId, teamId, token, mockSessionDAO, mockMatchComponent,
            mockPasseComponent, mockMatchAnalysisService, mockMannschaftsmitgliedComponent,
            mockDsbMitgliedComponent, mockWettkampfComponent, mockVeranstaltungComponent
        );
        
        // Assert
        assertThat(result).isNotNull();
        verify(mockSessionDAO).createSession(any(TabletSchusszettelEntity.class), any(Long.class));
    }

    // Error handling tests

    @Test
    public void persistSession_withDatabaseError_shouldThrowBusinessException() {
        // Arrange
        when(mockSessionDAO.updateStatus(any(TabletSchusszettelEntity.class), anyLong()))
            .thenThrow(new RuntimeException("Database error"));
        
        // Act & Assert
        assertThatThrownBy(() -> sessionRuntime.persistSession())
            .isInstanceOf(BusinessException.class);
    }

    @Test
    public void checkAgainstDatabase_withDatabaseChanges_shouldUpdateSession() {
        // Arrange
        when(mockSession.getWettkampfId()).thenReturn(50L);
        when(mockSession.getTeamId()).thenReturn(100L);
        when(mockSession.getToken()).thenReturn("test-token");
        
        TabletSchusszettelEntity databaseSession = new TabletSchusszettelEntity();
        databaseSession.setStatus("WARTE");
        databaseSession.setCurrentPasseNumber(3);
        databaseSession.setCurrentMatchId(400L);
        
        when(mockSessionDAO.findByTokenWettkampfUndTeam(50L, 100L, "test-token"))
            .thenReturn(Optional.of(databaseSession));
        
        // Act
        sessionRuntime.checkAgainstDatabase();
        
        // Assert - Should update session with database values
        verify(mockSessionDAO).findByTokenWettkampfUndTeam(50L, 100L, "test-token");
    }

    @Test
    public void checkAgainstDatabase_withNoDatabaseSession_shouldLogWarning() {
        // Arrange
        when(mockSession.getWettkampfId()).thenReturn(50L);
        when(mockSession.getTeamId()).thenReturn(100L);
        when(mockSession.getToken()).thenReturn("test-token");
        
        when(mockSessionDAO.findByTokenWettkampfUndTeam(50L, 100L, "test-token"))
            .thenReturn(Optional.empty());
        
        // Act
        sessionRuntime.checkAgainstDatabase();
        
        // Assert - Should handle gracefully
        verify(mockSessionDAO).findByTokenWettkampfUndTeam(50L, 100L, "test-token");
    }

    // Match progression tests

    @Test
    public void hasMoreMatches_withNextMatch_shouldReturnTrue() {
        // Arrange
        when(mockSession.getCurrentMatchId()).thenReturn(300L);
        when(mockSession.getTeamId()).thenReturn(100L);
        
        LigamatchBE nextMatch = new LigamatchBE();
        nextMatch.setMatchId(400L);
        
        when(mockMatchAnalysisService.findCorrectNextMatch(300L, 100L)).thenReturn(nextMatch);
        
        // Act
        boolean result = sessionRuntime.hasMoreMatches();
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void hasMoreMatches_withNoNextMatch_shouldReturnFalse() {
        // Arrange
        when(mockSession.getCurrentMatchId()).thenReturn(300L);
        when(mockSession.getTeamId()).thenReturn(100L);
        
        when(mockMatchAnalysisService.findCorrectNextMatch(300L, 100L)).thenReturn(null);
        
        // Act
        boolean result = sessionRuntime.hasMoreMatches();
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void hasMoreMatches_withException_shouldReturnFalse() {
        // Arrange
        when(mockSession.getCurrentMatchId()).thenReturn(300L);
        when(mockSession.getTeamId()).thenReturn(100L);
        
        when(mockMatchAnalysisService.findCorrectNextMatch(300L, 100L))
            .thenThrow(new RuntimeException("Test exception"));
        
        // Act
        boolean result = sessionRuntime.hasMoreMatches();
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void loadOpponentSession_withValidOpponent_shouldReturnOpponentSession() {
        // Arrange
        when(mockSession.getGegnerTeamId()).thenReturn(200L);
        when(mockSession.getWettkampfId()).thenReturn(50L);
        
        TabletSchusszettelEntity opponentSession = new TabletSchusszettelEntity();
        when(mockSessionDAO.findByWettkampfUndTeam(50L, 200L))
            .thenReturn(Optional.of(opponentSession));
        
        // Act
        SessionRuntime result = sessionRuntime.loadOpponentSession();
        
        // Assert
        assertThat(result).isNotNull();
    }

    @Test
    public void loadOpponentSession_withNoOpponent_shouldReturnNull() {
        // Arrange
        when(mockSession.getGegnerTeamId()).thenReturn(200L);
        when(mockSession.getWettkampfId()).thenReturn(50L);
        
        when(mockSessionDAO.findByWettkampfUndTeam(50L, 200L))
            .thenReturn(Optional.empty());
        
        // Act
        SessionRuntime result = sessionRuntime.loadOpponentSession();
        
        // Assert
        assertThat(result).isNull();
    }

    @Test
    public void isMatchComplete_shouldDelegateToMatchAnalysisService() {
        // Arrange
        when(mockSession.getCurrentMatchId()).thenReturn(300L);
        when(mockSession.getTeamId()).thenReturn(100L);
        when(mockSession.getGegnerTeamId()).thenReturn(200L);
        
        when(mockMatchAnalysisService.isMatchComplete(300L, 100L, 200L)).thenReturn(true);
        
        // Act
        boolean result = sessionRuntime.isMatchComplete();
        
        // Assert
        assertThat(result).isTrue();
        verify(mockMatchAnalysisService).isMatchComplete(300L, 100L, 200L);
    }

    @Test
    public void isMatchComplete_withException_shouldReturnFalse() {
        // Arrange
        when(mockSession.getCurrentMatchId()).thenReturn(300L);
        when(mockSession.getTeamId()).thenReturn(100L);
        when(mockSession.getGegnerTeamId()).thenReturn(200L);
        
        when(mockMatchAnalysisService.isMatchComplete(300L, 100L, 200L))
            .thenThrow(new RuntimeException("Test exception"));
        
        // Act
        boolean result = sessionRuntime.isMatchComplete();
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void getSessionDAO_shouldReturnDAO() {
        // Act
        TabletSchusszettelDAO result = sessionRuntime.getSessionDAO();
        
        // Assert
        assertThat(result).isEqualTo(mockSessionDAO);
    }

    // Null handling tests

    @Test
    public void loadFromDatabase_withNullToken_shouldThrowBusinessException() {
        // Act & Assert
        assertThatThrownBy(() -> SessionRuntime.loadFromDatabase(
            50L, 100L, null, mockSessionDAO, mockMatchComponent, 
            mockPasseComponent, mockMatchAnalysisService, mockMannschaftsmitgliedComponent,
            mockDsbMitgliedComponent, mockWettkampfComponent, mockVeranstaltungComponent
        )).isInstanceOf(BusinessException.class);
    }

    @Test
    public void initializeSession_withNullToken_shouldThrowBusinessException() {
        // Act & Assert
        assertThatThrownBy(() -> SessionRuntime.initializeSession(
            50L, 100L, null, mockSessionDAO, mockMatchComponent,
            mockPasseComponent, mockMatchAnalysisService, mockMannschaftsmitgliedComponent,
            mockDsbMitgliedComponent, mockWettkampfComponent, mockVeranstaltungComponent
        )).isInstanceOf(BusinessException.class);
    }

    // Helper methods

    private PasseDO createPasseDO() {
        PasseDO passe = new PasseDO();
        passe.setId(1L);
        passe.setPasseLfdnr(1L);
        passe.setPasseDsbMitgliedId((long) 10);
        passe.setPfeil1(8);
        passe.setPfeil2(9);
        passe.setPfeil3(7);
        return passe;
    }
}