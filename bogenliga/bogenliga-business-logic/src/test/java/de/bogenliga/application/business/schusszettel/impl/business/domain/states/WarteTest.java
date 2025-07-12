package de.bogenliga.application.business.schusszettel.impl.business.domain.states;

import de.bogenliga.application.business.schusszettel.impl.dao.TabletSchusszettelDAO;
import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.schusszettel.impl.business.serviceAdapter.MatchAnalysisService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Comprehensive tests for the Warte state class.
 * Tests complex opponent synchronization logic and edge cases.
 */
@RunWith(MockitoJUnitRunner.class)
public class WarteTest {

    @Mock
    private StateContext mockContext;

    @Mock
    private TabletSchusszettelEntity mockOpponent;

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

    private Warte warteState;

    @Before
    public void setUp() {
        warteState = new Warte();
    }

    @Test
    public void stateConstants_shouldHaveCorrectValues() {
        // Assert - test the state constants are available
        assertThat(Warte.STATUS_WARTE).isEqualTo("WARTE");
        assertThat(Warte.STATUS_SATZEINGABE).isEqualTo("SATZEINGABE");
        assertThat(Warte.STATUS_MATCH_ENDE).isEqualTo("MATCH_ENDE");
    }

    @Test
    public void canNudgeAlong_shouldReturnFalse() {
        // Act
        boolean result = warteState.canNudgeAlong();
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void isValidState_withIncompleteMatch_shouldReturnTrue() {
        // Arrange
        when(mockContext.isMatchComplete()).thenReturn(false);
        
        // Act
        boolean result = warteState.isValidState(mockContext);
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void isValidState_withCompleteMatch_shouldReturnFalse() {
        // Arrange
        when(mockContext.isMatchComplete()).thenReturn(true);
        
        // Act
        boolean result = warteState.isValidState(mockContext);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void handleWarteEvaluation_withNullOpponent_shouldReturnFalse() {
        // Arrange
        when(mockContext.getTeamId()).thenReturn(100L);
        when(mockContext.getCurrentPasseNumber()).thenReturn(2);
        
        // Act
        boolean result = warteState.handleWarteEvaluation(mockContext, null);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void handleWarteEvaluation_withOpponentAhead_shouldAllowSelfProgression() {
        // Arrange
        when(mockContext.getTeamId()).thenReturn(100L);
        when(mockContext.getCurrentPasseNumber()).thenReturn(2);
        when(mockContext.isMatchComplete()).thenReturn(false);
        
        when(mockOpponent.getTeamId()).thenReturn(200L);
        when(mockOpponent.getCurrentPasseNumber()).thenReturn(3);
        when(mockOpponent.getStatus()).thenReturn("WARTE");
        
        // Act
        boolean result = warteState.handleWarteEvaluation(mockContext, mockOpponent);
        
        // Assert
        assertThat(result).isTrue();
        verify(mockContext).updatePasseNumber(3);
        verify(mockContext).updateSessionStatus("SATZEINGABE");
    }

    @Test
    public void handleWarteEvaluation_withCurrentTeamAhead_shouldHelpOpponentCatchUp() {
        // Arrange
        when(mockContext.getTeamId()).thenReturn(100L);
        when(mockContext.getCurrentPasseNumber()).thenReturn(3);
        when(mockContext.isMatchComplete()).thenReturn(false);
        
        when(mockOpponent.getTeamId()).thenReturn(200L);
        when(mockOpponent.getCurrentPasseNumber()).thenReturn(2);
        when(mockOpponent.getStatus()).thenReturn("WARTE");
        when(mockOpponent.getWettkampfId()).thenReturn(50L);
        
        // Setup fresh opponent mock
        TabletSchusszettelEntity freshOpponent = mock(TabletSchusszettelEntity.class);
        when(freshOpponent.getTeamId()).thenReturn(200L);
        when(freshOpponent.getCurrentPasseNumber()).thenReturn(2);
        when(freshOpponent.getStatus()).thenReturn("WARTE");
        
        when(mockContext.getSessionDAO()).thenReturn(mockSessionDAO);
        when(mockSessionDAO.findByWettkampfUndTeam(50L, 200L)).thenReturn(Optional.of(freshOpponent));
        
        // Setup context components
        when(mockContext.getMatchComponent()).thenReturn(mockMatchComponent);
        when(mockContext.getPasseComponent()).thenReturn(mockPasseComponent);
        when(mockContext.getMatchAnalysisService()).thenReturn(mockMatchAnalysisService);
        when(mockContext.getMannschaftsmitgliedComponent()).thenReturn(mockMannschaftsmitgliedComponent);
        when(mockContext.getDsbMitgliedComponent()).thenReturn(mockDsbMitgliedComponent);
        when(mockContext.getWettkampfComponent()).thenReturn(mockWettkampfComponent);
        when(mockContext.getVeranstaltungComponent()).thenReturn(mockVeranstaltungComponent);
        
        // Act
        boolean result = warteState.handleWarteEvaluation(mockContext, mockOpponent);
        
        // Assert
        assertThat(result).isTrue();
        verify(mockContext).updatePasseNumber(4);
        verify(mockContext).updateSessionStatus("SATZEINGABE");
    }

    @Test
    public void handleWarteEvaluation_withOpponentNotInWarte_shouldWait() {
        // Arrange
        when(mockContext.getTeamId()).thenReturn(100L);
        when(mockContext.getCurrentPasseNumber()).thenReturn(2);
        
        when(mockOpponent.getTeamId()).thenReturn(200L);
        when(mockOpponent.getCurrentPasseNumber()).thenReturn(2);
        when(mockOpponent.getStatus()).thenReturn("SATZEINGABE");
        
        // Act
        boolean result = warteState.handleWarteEvaluation(mockContext, mockOpponent);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void handleWarteEvaluation_withBothTeamsInWarte_shouldProgressBoth() {
        // Arrange
        when(mockContext.getTeamId()).thenReturn(100L);
        when(mockContext.getCurrentPasseNumber()).thenReturn(2);
        when(mockContext.isMatchComplete()).thenReturn(false);
        
        when(mockOpponent.getTeamId()).thenReturn(200L);
        when(mockOpponent.getCurrentPasseNumber()).thenReturn(2);
        when(mockOpponent.getStatus()).thenReturn("WARTE");
        when(mockOpponent.getWettkampfId()).thenReturn(50L);
        
        // Setup fresh opponent mock
        TabletSchusszettelEntity freshOpponent = mock(TabletSchusszettelEntity.class);
        when(freshOpponent.getTeamId()).thenReturn(200L);
        when(freshOpponent.getCurrentPasseNumber()).thenReturn(2);
        when(freshOpponent.getStatus()).thenReturn("WARTE");
        
        when(mockContext.getSessionDAO()).thenReturn(mockSessionDAO);
        when(mockSessionDAO.findByWettkampfUndTeam(50L, 200L)).thenReturn(Optional.of(freshOpponent));
        
        // Setup context components
        when(mockContext.getMatchComponent()).thenReturn(mockMatchComponent);
        when(mockContext.getPasseComponent()).thenReturn(mockPasseComponent);
        when(mockContext.getMatchAnalysisService()).thenReturn(mockMatchAnalysisService);
        when(mockContext.getMannschaftsmitgliedComponent()).thenReturn(mockMannschaftsmitgliedComponent);
        when(mockContext.getDsbMitgliedComponent()).thenReturn(mockDsbMitgliedComponent);
        when(mockContext.getWettkampfComponent()).thenReturn(mockWettkampfComponent);
        when(mockContext.getVeranstaltungComponent()).thenReturn(mockVeranstaltungComponent);
        
        // Act
        boolean result = warteState.handleWarteEvaluation(mockContext, mockOpponent);
        
        // Assert
        assertThat(result).isTrue();
        verify(mockContext).updatePasseNumber(3);
        verify(mockContext).updateSessionStatus("SATZEINGABE");
    }

    @Test
    public void handleWarteEvaluation_withMatchComplete_shouldTransitionToMatchEnde() {
        // Arrange
        when(mockContext.getTeamId()).thenReturn(100L);
        when(mockContext.getCurrentPasseNumber()).thenReturn(5);
        when(mockContext.getCurrentMatchId()).thenReturn(300L);
        when(mockContext.isMatchComplete()).thenReturn(true);
        
        when(mockOpponent.getTeamId()).thenReturn(200L);
        when(mockOpponent.getCurrentPasseNumber()).thenReturn(4);
        when(mockOpponent.getStatus()).thenReturn("WARTE");
        
        // Act
        boolean result = warteState.handleWarteEvaluation(mockContext, mockOpponent);
        
        // Assert
        assertThat(result).isTrue();
        verify(mockContext).updateSessionStatus("MATCH_ENDE");
    }

    @Test
    public void handleWarteEvaluation_withInvalidPasseNumber_shouldForceMatchEnde() {
        // Arrange
        when(mockContext.getTeamId()).thenReturn(100L);
        when(mockContext.getCurrentPasseNumber()).thenReturn(5);
        when(mockContext.getCurrentMatchId()).thenReturn(300L);
        when(mockContext.isMatchComplete()).thenReturn(false);
        
        when(mockOpponent.getTeamId()).thenReturn(200L);
        when(mockOpponent.getCurrentPasseNumber()).thenReturn(4);
        when(mockOpponent.getStatus()).thenReturn("WARTE");
        
        // Act
        boolean result = warteState.handleWarteEvaluation(mockContext, mockOpponent);
        
        // Assert
        assertThat(result).isTrue();
        verify(mockContext).updateSessionStatus("MATCH_ENDE");
    }

    @Test
    public void handleWarteEvaluation_withOpponentNotFound_shouldReturnFalse() {
        // Arrange
        when(mockContext.getTeamId()).thenReturn(100L);
        when(mockContext.getCurrentPasseNumber()).thenReturn(2);
        
        when(mockOpponent.getTeamId()).thenReturn(200L);
        when(mockOpponent.getCurrentPasseNumber()).thenReturn(1);
        when(mockOpponent.getStatus()).thenReturn("WARTE");
        when(mockOpponent.getWettkampfId()).thenReturn(50L);
        
        when(mockContext.getSessionDAO()).thenReturn(mockSessionDAO);
        when(mockSessionDAO.findByWettkampfUndTeam(50L, 200L)).thenReturn(Optional.empty());
        
        // Act
        boolean result = warteState.handleWarteEvaluation(mockContext, mockOpponent);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void handleWarteEvaluation_withException_shouldReturnFalse() {
        // Arrange
        when(mockContext.getTeamId()).thenReturn(100L);
        when(mockContext.getCurrentPasseNumber()).thenReturn(2);
        when(mockContext.isMatchComplete()).thenThrow(new RuntimeException("Test exception"));
        
        when(mockOpponent.getTeamId()).thenReturn(200L);
        when(mockOpponent.getCurrentPasseNumber()).thenReturn(1);
        when(mockOpponent.getStatus()).thenReturn("WARTE");
        
        // Act
        boolean result = warteState.handleWarteEvaluation(mockContext, mockOpponent);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void canTransitionTo_withAnyState_shouldReturnTrue() {
        // Act
        boolean result = warteState.canTransitionTo(mockContext, "SATZEINGABE");
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void validateOperation_withAnyOperation_shouldReturnFalse() {
        // Act
        boolean result = warteState.validateOperation(mockContext, "submitSatz", null);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void handlePostOperation_withAnyOperation_shouldReturnFalse() {
        // Act
        boolean result = warteState.handlePostOperation(mockContext, "submitSatz", null);
        
        // Assert
        assertThat(result).isFalse();
    }
}