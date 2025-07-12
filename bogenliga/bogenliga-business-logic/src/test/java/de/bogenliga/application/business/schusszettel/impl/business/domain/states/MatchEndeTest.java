package de.bogenliga.application.business.schusszettel.impl.business.domain.states;

import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.SatzErgebnisDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.TeamMatchInfoDO;
import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Comprehensive tests for the MatchEnde state class.
 * Tests match completion state behavior and progression logic.
 */
@RunWith(MockitoJUnitRunner.class)
public class MatchEndeTest {

    @Mock
    private StateContext mockContext;

    @Mock
    private TabletSchusszettelEntity mockOpponent;

    @Mock
    private PasseComponent mockPasseComponent;

    private MatchEnde matchEndeState;

    @Before
    public void setUp() {
        matchEndeState = new MatchEnde();
    }

    @Test
    public void stateConstants_shouldHaveCorrectValues() {
        // Assert - test the state constants are available
        assertThat(MatchEnde.STATUS_MATCH_ENDE).isEqualTo("MATCH_ENDE");
        assertThat(MatchEnde.STATUS_SCHUETZENMELDUNG).isEqualTo("SCHUETZENMELDUNG");
        assertThat(MatchEnde.STATUS_WETTKAMPF_ENDE).isEqualTo("WETTKAMPF_ENDE");
    }

    @Test
    public void canNudgeAlong_shouldReturnFalse() {
        // Act
        boolean result = matchEndeState.canNudgeAlong();
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void isValidState_withCompleteMatch_shouldReturnTrue() {
        // Arrange
        when(mockContext.isMatchComplete()).thenReturn(true);
        
        // Act
        boolean result = matchEndeState.isValidState(mockContext);
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void isValidState_withIncompleteMatch_shouldReturnFalse() {
        // Arrange
        when(mockContext.isMatchComplete()).thenReturn(false);
        
        // Act
        boolean result = matchEndeState.isValidState(mockContext);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void canTransitionTo_withSchuetzenmeldung_shouldReturnTrue() {
        // Act
        boolean result = matchEndeState.canTransitionTo(mockContext, "SCHUETZENMELDUNG");
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void canTransitionTo_withWettkampfEnde_shouldReturnTrue() {
        // Act
        boolean result = matchEndeState.canTransitionTo(mockContext, "WETTKAMPF_ENDE");
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void canTransitionTo_withOtherState_shouldReturnFalse() {
        // Act
        boolean result = matchEndeState.canTransitionTo(mockContext, "SATZEINGABE");
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void isDatabaseReadyForTransition_shouldAlwaysReturnTrue() {
        // Act
        boolean result = matchEndeState.isDatabaseReadyForTransition(mockContext, "SCHUETZENMELDUNG");
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void validateOperation_withAnyOperation_shouldReturnFalse() {
        // Act
        boolean result = matchEndeState.validateOperation(mockContext, "submitSatz", null);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void handlePostOperation_withAnyOperation_shouldReturnFalse() {
        // Act
        boolean result = matchEndeState.handlePostOperation(mockContext, "submitSatz", null);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void handleWarteEvaluation_withMoreMatches_shouldAdvanceToNextMatch() {
        // Arrange
        when(mockContext.getTeamId()).thenReturn(100L);
        when(mockContext.getCurrentMatchId()).thenReturn(300L);
        when(mockContext.hasMoreMatches()).thenReturn(true);
        
        LigamatchBE nextMatch = new LigamatchBE();
        nextMatch.setMatchId(400L);
        when(mockContext.getNextMatch()).thenReturn(nextMatch);
        when(mockContext.findOpponentTeamId(400L)).thenReturn(200L);
        
        // Act
        boolean result = matchEndeState.handleWarteEvaluation(mockContext, mockOpponent);
        
        // Assert
        assertThat(result).isTrue();
        verify(mockContext).advanceToNextMatch(nextMatch, 200L);
    }

    @Test
    public void handleWarteEvaluation_withNoMoreMatches_shouldTransitionToWettkampfEnde() {
        // Arrange
        when(mockContext.getTeamId()).thenReturn(100L);
        when(mockContext.getCurrentMatchId()).thenReturn(300L);
        when(mockContext.hasMoreMatches()).thenReturn(false);
        
        // Act
        boolean result = matchEndeState.handleWarteEvaluation(mockContext, mockOpponent);
        
        // Assert
        assertThat(result).isTrue();
        verify(mockContext).updateSessionStatus("WETTKAMPF_ENDE");
    }

    @Test
    public void handleWarteEvaluation_withMoreMatchesButNullNextMatch_shouldTransitionToWettkampfEnde() {
        // Arrange
        when(mockContext.getTeamId()).thenReturn(100L);
        when(mockContext.getCurrentMatchId()).thenReturn(300L);
        when(mockContext.hasMoreMatches()).thenReturn(true);
        when(mockContext.getNextMatch()).thenReturn(null);
        
        // Act
        boolean result = matchEndeState.handleWarteEvaluation(mockContext, mockOpponent);
        
        // Assert
        assertThat(result).isTrue();
        verify(mockContext).updateSessionStatus("WETTKAMPF_ENDE");
    }

    @Test
    public void handleWarteEvaluation_withException_shouldReturnFalse() {
        // Arrange
        when(mockContext.getTeamId()).thenReturn(100L);
        when(mockContext.getCurrentMatchId()).thenReturn(300L);
        when(mockContext.hasMoreMatches()).thenThrow(new RuntimeException("Test exception"));
        
        // Act
        boolean result = matchEndeState.handleWarteEvaluation(mockContext, mockOpponent);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void prepareResponseData_withValidMatchData_shouldReturnCompleteData() {
        // Arrange
        when(mockContext.getTeamId()).thenReturn(100L);
        when(mockContext.getCurrentMatchId()).thenReturn(300L);
        when(mockContext.getOpponentTeamId()).thenReturn(200L);
        when(mockContext.getCurrentPasseNumber()).thenReturn(5);
        when(mockContext.getOpponentMatchId()).thenReturn(301L);
        when(mockContext.buildWettkampfInfo()).thenReturn(null);
        
        // Setup match passes
        List<PasseDO> teamPasses = createTeamPasses();
        List<PasseDO> opponentPasses = createOpponentPasses();
        
        when(mockContext.getAllMatchPasses()).thenReturn(teamPasses);
        when(mockContext.getPasseComponent()).thenReturn(mockPasseComponent);
        when(mockPasseComponent.findByMannschaftMatchId(200L, 300L)).thenReturn(opponentPasses);
        
        // Act
        Map<String, Object> result = matchEndeState.prepareResponseData(mockContext);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.get("currentPasseNumber")).isEqualTo(5);
        assertThat(result.get("eigenesTeamMatchId")).isEqualTo(300L);
        assertThat(result.get("gegnerischesTeamMatchId")).isEqualTo(301L);
        assertThat(result).containsKey("satzErgebnisse");
        assertThat(result).containsKey("matchErgebnis");
        assertThat(result.get("schuetzenMatchPunkte")).isEqualTo(Collections.emptyList());
        assertThat(result.get("schuetzeStammDaten")).isEqualTo(Collections.emptyList());
        assertThat(result.get("verfuegbareSchuetzen")).isEqualTo(Collections.emptyList());
        
        // Verify set results
        @SuppressWarnings("unchecked")
        List<SatzErgebnisDO> satzErgebnisse = (List<SatzErgebnisDO>) result.get("satzErgebnisse");
        assertThat(satzErgebnisse).hasSize(2);
        
        // Verify match info
        @SuppressWarnings("unchecked")
        List<TeamMatchInfoDO> matchInfo = (List<TeamMatchInfoDO>) result.get("matchErgebnis");
        assertThat(matchInfo).hasSize(2);
    }

    @Test
    public void prepareResponseData_withException_shouldReturnEmptyLists() {
        // Arrange
        when(mockContext.getTeamId()).thenReturn(100L);
        when(mockContext.getCurrentMatchId()).thenReturn(300L);
        when(mockContext.getOpponentTeamId()).thenReturn(200L);
        when(mockContext.getCurrentPasseNumber()).thenReturn(5);
        when(mockContext.getOpponentMatchId()).thenReturn(301L);
        when(mockContext.buildWettkampfInfo()).thenReturn(null);
        when(mockContext.getAllMatchPasses()).thenThrow(new RuntimeException("Test exception"));
        
        // Act
        Map<String, Object> result = matchEndeState.prepareResponseData(mockContext);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.get("satzErgebnisse")).isEqualTo(Collections.emptyList());
        assertThat(result.get("matchErgebnis")).isEqualTo(Collections.emptyList());
        assertThat(result.get("schuetzenMatchPunkte")).isEqualTo(Collections.emptyList());
        assertThat(result.get("schuetzeStammDaten")).isEqualTo(Collections.emptyList());
        assertThat(result.get("verfuegbareSchuetzen")).isEqualTo(Collections.emptyList());
    }

    private List<PasseDO> createTeamPasses() {
        List<PasseDO> passes = new ArrayList<>();
        
        // Set 1: Team scores 28 points (9+9+10)
        PasseDO pass1 = new PasseDO();
        pass1.setPasseMannschaftId(100L);
        pass1.setPasseMatchId(300L);
        pass1.setPasseLfdnr(1L);
        pass1.setPasseDsbMitgliedId(1L);
        pass1.setPfeil1(9);
        pass1.setPfeil2(9);
        pass1.setPfeil3(10);
        passes.add(pass1);
        
        // Set 2: Team scores 25 points (8+8+9)
        PasseDO pass2 = new PasseDO();
        pass2.setPasseMannschaftId(100L);
        pass2.setPasseMatchId(300L);
        pass2.setPasseLfdnr(2L);
        pass2.setPasseDsbMitgliedId(1L);
        pass2.setPfeil1(8);
        pass2.setPfeil2(8);
        pass2.setPfeil3(9);
        passes.add(pass2);
        
        return passes;
    }

    private List<PasseDO> createOpponentPasses() {
        List<PasseDO> passes = new ArrayList<>();
        
        // Set 1: Opponent scores 26 points (8+9+9)
        PasseDO pass1 = new PasseDO();
        pass1.setPasseMannschaftId(200L);
        pass1.setPasseMatchId(301L);
        pass1.setPasseLfdnr(1L);
        pass1.setPasseDsbMitgliedId(2L);
        pass1.setPfeil1(8);
        pass1.setPfeil2(9);
        pass1.setPfeil3(9);
        passes.add(pass1);
        
        // Set 2: Opponent scores 27 points (9+9+9)
        PasseDO pass2 = new PasseDO();
        pass2.setPasseMannschaftId(200L);
        pass2.setPasseMatchId(301L);
        pass2.setPasseLfdnr(2L);
        pass2.setPasseDsbMitgliedId(2L);
        pass2.setPfeil1(9);
        pass2.setPfeil2(9);
        pass2.setPfeil3(9);
        passes.add(pass2);
        
        return passes;
    }

    @Test
    public void handleWarteEvaluation_withNullOpponent_shouldReturnFalse() {
        // Arrange
        when(mockContext.getTeamId()).thenReturn(100L);
        when(mockContext.getCurrentMatchId()).thenReturn(300L);
        when(mockContext.hasMoreMatches()).thenReturn(true);
        
        // Act
        boolean result = matchEndeState.handleWarteEvaluation(mockContext, null);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void prepareResponseData_withNullContext_shouldHandleGracefully() {
        // Act
        Map<String, Object> result = matchEndeState.prepareResponseData(null);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.get("satzErgebnisse")).isEqualTo(Collections.emptyList());
        assertThat(result.get("matchErgebnis")).isEqualTo(Collections.emptyList());
    }

    @Test
    public void isValidState_withNullContext_shouldReturnFalse() {
        // Act
        boolean result = matchEndeState.isValidState(null);
        
        // Assert
        assertThat(result).isFalse();
    }
}