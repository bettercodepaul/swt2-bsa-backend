package de.bogenliga.application.business.schusszettel.impl.business.domain;

import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.business.schusszettel.impl.dao.TabletSchusszettelDAO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.schusszettel.impl.business.serviceAdapter.MatchAnalysisService;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Comprehensive tests for the SessionRuntime class.
 * Tests session state management, state machine coordination, and opponent synchronization.
 */
@RunWith(MockitoJUnitRunner.class)
public class SessionRuntimeTest {

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

    @Test
    public void getSessionDAO_shouldReturnSessionDAO() {
        // Act
        TabletSchusszettelDAO result = sessionRuntime.getSessionDAO();
        
        // Assert
        assertThat(result).isEqualTo(mockSessionDAO);
    }

    @Test
    public void getSession_shouldReturnSessionEntity() {
        // Act
        TabletSchusszettelEntity result = sessionRuntime.getSession();
        
        // Assert
        assertThat(result).isEqualTo(mockSession);
    }

    @Test
    public void getTeamId_shouldReturnSessionTeamId() {
        // Arrange
        when(mockSession.getTeamId()).thenReturn(100L);
        
        // Act
        long result = sessionRuntime.getTeamId();
        
        // Assert
        assertThat(result).isEqualTo(100L);
    }

    @Test
    public void getWettkampfId_shouldReturnSessionWettkampfId() {
        // Arrange
        when(mockSession.getWettkampfId()).thenReturn(50L);
        
        // Act
        long result = sessionRuntime.getWettkampfId();
        
        // Assert
        assertThat(result).isEqualTo(50L);
    }

    @Test
    public void getCurrentMatchId_shouldReturnCurrentMatchId() {
        // Arrange
        when(mockSession.getCurrentMatchId()).thenReturn(300L);
        
        // Act
        long result = sessionRuntime.getCurrentMatchId();
        
        // Assert
        assertThat(result).isEqualTo(300L);
    }

    @Test
    public void getCurrentPasseNumber_shouldReturnCurrentPasseNumber() {
        // Arrange
        when(mockSession.getCurrentPasseNumber()).thenReturn(2);
        
        // Act
        int result = sessionRuntime.getCurrentPasseNumber();
        
        // Assert
        assertThat(result).isEqualTo(2);
    }

    @Test
    public void getCurrentState_shouldReturnCurrentStatus() {
        // Arrange
        when(mockSession.getStatus()).thenReturn("WARTE");
        
        // Act
        String result = sessionRuntime.getCurrentState();
        
        // Assert
        assertThat(result).isEqualTo("WARTE");
    }

    @Test
    public void updateSessionStatus_shouldUpdateSessionAndPersist() {
        // Arrange
        when(mockSession.getTeamId()).thenReturn(100L);
        
        // Act
        sessionRuntime.updateSessionStatus("MATCH_ENDE");
        
        // Assert
        verify(mockSession).setStatus("MATCH_ENDE");
        verify(mockSessionDAO).updateStatus(eq(mockSession), anyLong());
    }

    @Test
    public void updatePasseNumber_shouldUpdatePasseAndPersist() {
        // Arrange
        when(mockSession.getTeamId()).thenReturn(100L);
        
        // Act
        sessionRuntime.updatePasseNumber(3);
        
        // Assert
        verify(mockSession).setCurrentPasseNumber(3);
        verify(mockSessionDAO).updateStatus(eq(mockSession), anyLong());
    }

    @Test
    public void advanceToNextMatch_shouldUpdateSessionForNextMatch() {
        // Arrange
        when(mockSession.getTeamId()).thenReturn(100L);
        
        LigamatchBE nextMatch = new LigamatchBE();
        nextMatch.setMatchId(400L);
        
        // Act
        sessionRuntime.advanceToNextMatch(nextMatch, 200L);
        
        // Assert
        verify(mockSession).setCurrentMatchId(400L);
        verify(mockSession).setGegnerTeamId(200L);
        verify(mockSession).setCurrentPasseNumber(1);
        verify(mockSession).setStatus("SCHUETZENMELDUNG");
        verify(mockSessionDAO).updateStatus(eq(mockSession), anyLong());
    }

    @Test
    public void loadOpponentSessionByTeamId_shouldReturnOpponentSession() {
        // Arrange
        when(mockSession.getWettkampfId()).thenReturn(50L);
        
        TabletSchusszettelEntity opponentSession = new TabletSchusszettelEntity();
        when(mockSessionDAO.findByWettkampfUndTeam(50L, 200L)).thenReturn(Optional.of(opponentSession));
        
        // Act
        TabletSchusszettelEntity result = sessionRuntime.loadOpponentSessionByTeamId(200L);
        
        // Assert
        assertThat(result).isEqualTo(opponentSession);
    }

    @Test
    public void loadOpponentSessionByTeamId_withNoOpponent_shouldReturnNull() {
        // Arrange
        when(mockSession.getWettkampfId()).thenReturn(50L);
        when(mockSessionDAO.findByWettkampfUndTeam(50L, 200L)).thenReturn(Optional.empty());
        
        // Act
        TabletSchusszettelEntity result = sessionRuntime.loadOpponentSessionByTeamId(200L);
        
        // Assert
        assertThat(result).isNull();
    }

    @Test
    public void nudgeAlong_withSchuetzenmeldung_shouldTransitionToSatzeingabe() {
        // Arrange
        when(mockSession.getStatus()).thenReturn("SCHUETZENMELDUNG");
        when(mockSession.getTeamId()).thenReturn(100L);
        
        // Act
        sessionRuntime.nudgeAlong();
        
        // Assert
        verify(mockSession).setStatus("SATZEINGABE");
        verify(mockSessionDAO).updateStatus(eq(mockSession), anyLong());
    }

    @Test
    public void nudgeAlong_withSatzeingabe_shouldTransitionToWarte() {
        // Arrange
        when(mockSession.getStatus()).thenReturn("SATZEINGABE");
        when(mockSession.getTeamId()).thenReturn(100L);
        
        // Act
        sessionRuntime.nudgeAlong();
        
        // Assert
        verify(mockSession).setStatus("WARTE");
        verify(mockSessionDAO).updateStatus(eq(mockSession), anyLong());
    }

    @Test
    public void nudgeAlong_withWarte_shouldNotTransition() {
        // Arrange
        when(mockSession.getStatus()).thenReturn("WARTE");
        when(mockSession.getTeamId()).thenReturn(100L);
        
        // Act
        sessionRuntime.nudgeAlong();
        
        // Assert
        verify(mockSession, never()).setStatus(anyString());
        verify(mockSessionDAO, never()).updateStatus(eq(mockSession), anyLong());
    }

    @Test
    public void nudgeAlong_withMatchEnde_shouldNotTransition() {
        // Arrange
        when(mockSession.getStatus()).thenReturn("MATCH_ENDE");
        when(mockSession.getTeamId()).thenReturn(100L);
        
        // Act
        sessionRuntime.nudgeAlong();
        
        // Assert
        verify(mockSession, never()).setStatus(anyString());
        verify(mockSessionDAO, never()).updateStatus(eq(mockSession), anyLong());
    }

    @Test
    public void nudgeAlong_withWettkampfEnde_shouldNotTransition() {
        // Arrange
        when(mockSession.getStatus()).thenReturn("WETTKAMPF_ENDE");
        when(mockSession.getTeamId()).thenReturn(100L);
        
        // Act
        sessionRuntime.nudgeAlong();
        
        // Assert
        verify(mockSession, never()).setStatus(anyString());
        verify(mockSessionDAO, never()).updateStatus(eq(mockSession), anyLong());
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
        when(mockMatchAnalysisService.isMatchComplete(300L, 100L, 200L)).thenThrow(new RuntimeException("Test exception"));
        
        // Act
        boolean result = sessionRuntime.isMatchComplete();
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void evaluateWithOpponentWAITstate_shouldDelegateToStateObject() {
        // Arrange
        when(mockSession.getStatus()).thenReturn("WARTE");
        when(mockSession.getTeamId()).thenReturn(100L);
        when(mockSession.getCurrentPasseNumber()).thenReturn(2);
        when(mockSession.getCurrentMatchId()).thenReturn(300L);
        when(mockSession.getGegnerTeamId()).thenReturn(200L);
        when(mockSession.getWettkampfId()).thenReturn(50L);
        
        TabletSchusszettelEntity opponent = new TabletSchusszettelEntity();
        opponent.setTeamId(200L);
        opponent.setStatus("WARTE");
        opponent.setCurrentPasseNumber(2);
        
        // Mock state evaluation to return true (both teams progress)
        when(mockMatchAnalysisService.isMatchComplete(300L, 100L, 200L)).thenReturn(false);
        
        // Act
        boolean result = sessionRuntime.evaluateWithOpponentWAITstate(opponent);
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void evaluateWithOpponentWAITstate_withNonWarteState_shouldReturnFalse() {
        // Arrange
        when(mockSession.getStatus()).thenReturn("SATZEINGABE");
        when(mockSession.getTeamId()).thenReturn(100L);
        
        TabletSchusszettelEntity opponent = new TabletSchusszettelEntity();
        opponent.setTeamId(200L);
        opponent.setStatus("WARTE");
        
        // Act
        boolean result = sessionRuntime.evaluateWithOpponentWAITstate(opponent);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void toString_shouldReturnFormattedString() {
        // Arrange
        when(mockSession.getTeamId()).thenReturn(100L);
        when(mockSession.getStatus()).thenReturn("WARTE");
        when(mockSession.getCurrentMatchId()).thenReturn(300L);
        when(mockSession.getCurrentPasseNumber()).thenReturn(2);
        
        // Act
        String result = sessionRuntime.toString();
        
        // Assert
        assertThat(result).contains("SessionRuntime");
        assertThat(result).contains("teamId=100");
        assertThat(result).contains("status=WARTE");
        assertThat(result).contains("matchId=300");
        assertThat(result).contains("passe=2");
    }

    @Test
    public void constants_shouldHaveCorrectValues() {
        // Assert
        assertThat(SessionRuntime.STATUS_SCHUETZENMELDUNG).isEqualTo("SCHUETZENMELDUNG");
        assertThat(SessionRuntime.STATUS_SATZEINGABE).isEqualTo("SATZEINGABE");
        assertThat(SessionRuntime.STATUS_WARTE).isEqualTo("WARTE");
        assertThat(SessionRuntime.STATUS_MATCH_ENDE).isEqualTo("MATCH_ENDE");
        assertThat(SessionRuntime.STATUS_WETTKAMPF_ENDE).isEqualTo("WETTKAMPF_ENDE");
    }
}