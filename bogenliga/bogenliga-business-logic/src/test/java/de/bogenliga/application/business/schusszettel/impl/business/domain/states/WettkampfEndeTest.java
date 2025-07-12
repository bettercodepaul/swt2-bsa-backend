package de.bogenliga.application.business.schusszettel.impl.business.domain.states;

import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.TeamMatchInfoDO;
import de.bogenliga.application.business.schusszettel.impl.business.serviceAdapter.MatchAnalysisService;
import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Comprehensive tests for the WettkampfEnde state class.
 * Tests competition completion state behavior and match recap functionality.
 */
@RunWith(MockitoJUnitRunner.class)
public class WettkampfEndeTest {

    @Mock
    private StateContext mockContext;

    @Mock
    private TabletSchusszettelEntity mockOpponent;

    @Mock
    private MatchComponent mockMatchComponent;

    @Mock
    private PasseComponent mockPasseComponent;

    @Mock
    private MatchAnalysisService mockMatchAnalysisService;

    private WettkampfEnde wettkampfEndeState;

    @Before
    public void setUp() {
        wettkampfEndeState = new WettkampfEnde();
    }

    @Test
    public void stateConstants_shouldHaveCorrectValues() {
        // Assert - test the state constants are available
        assertThat(WettkampfEnde.STATUS_WETTKAMPF_ENDE).isEqualTo("WETTKAMPF_ENDE");
    }

    @Test
    public void canNudgeAlong_shouldReturnFalse() {
        // Act
        boolean result = wettkampfEndeState.canNudgeAlong();
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void isValidState_shouldAlwaysReturnTrue() {
        // Act
        boolean result = wettkampfEndeState.isValidState(mockContext);
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void canTransitionTo_withAnyState_shouldReturnFalse() {
        // Act
        boolean result = wettkampfEndeState.canTransitionTo(mockContext, "SCHUETZENMELDUNG");
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void isDatabaseReadyForTransition_shouldReturnFalse() {
        // Act
        boolean result = wettkampfEndeState.isDatabaseReadyForTransition(mockContext, "SCHUETZENMELDUNG");
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void validateOperation_withAnyOperation_shouldReturnFalse() {
        // Act
        boolean result = wettkampfEndeState.validateOperation(mockContext, "submitSatz", null);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void handlePostOperation_withAnyOperation_shouldReturnFalse() {
        // Act
        boolean result = wettkampfEndeState.handlePostOperation(mockContext, "submitSatz", null);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void prepareResponseData_withCompletedMatches_shouldReturnMatchRecap() {
        // Arrange
        when(mockContext.getTeamId()).thenReturn(100L);
        when(mockContext.getWettkampfId()).thenReturn(50L);
        when(mockContext.getCurrentMatchId()).thenReturn(300L);
        when(mockContext.getOpponentTeamId()).thenReturn(200L);
        when(mockContext.getCurrentPasseNumber()).thenReturn(5);
        when(mockContext.getOpponentMatchId()).thenReturn(301L);
        when(mockContext.buildWettkampfInfo()).thenReturn(null);
        
        // Setup matches
        List<MatchDO> matches = createCompletedMatches();
        when(mockContext.getMatchComponent()).thenReturn(mockMatchComponent);
        when(mockMatchComponent.findByWettkampfId(50L)).thenReturn(matches);
        
        // Setup pass data
        when(mockContext.getPasseComponent()).thenReturn(mockPasseComponent);
        when(mockPasseComponent.findByMannschaftMatchId(100L, 300L)).thenReturn(createTeamPasses());
        when(mockPasseComponent.findByMannschaftMatchId(200L, 300L)).thenReturn(createOpponentPasses());
        
        // Act
        Map<String, Object> result = wettkampfEndeState.prepareResponseData(mockContext);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.get("satzErgebnisse")).isEqualTo(Collections.emptyList());
        assertThat(result.get("schuetzenMatchPunkte")).isEqualTo(Collections.emptyList());
        assertThat(result.get("schuetzeStammDaten")).isEqualTo(Collections.emptyList());
        assertThat(result.get("verfuegbareSchuetzen")).isEqualTo(Collections.emptyList());
        assertThat(result).containsKey("matchErgebnis");
        
        // Verify match recap is populated
        @SuppressWarnings("unchecked")
        List<TeamMatchInfoDO> matchRecap = (List<TeamMatchInfoDO>) result.get("matchErgebnis");
        assertThat(matchRecap).hasSize(2); // 2 teams per match
    }

    @Test
    public void prepareResponseData_withException_shouldReturnEmptyLists() {
        // Arrange
        when(mockContext.getTeamId()).thenReturn(100L);
        when(mockContext.getWettkampfId()).thenReturn(50L);
        when(mockContext.getCurrentPasseNumber()).thenReturn(5);
        when(mockContext.getOpponentMatchId()).thenReturn(301L);
        when(mockContext.buildWettkampfInfo()).thenReturn(null);
        when(mockContext.getMatchComponent()).thenThrow(new RuntimeException("Test exception"));
        
        // Act
        Map<String, Object> result = wettkampfEndeState.prepareResponseData(mockContext);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.get("satzErgebnisse")).isEqualTo(Collections.emptyList());
        assertThat(result.get("schuetzenMatchPunkte")).isEqualTo(Collections.emptyList());
        assertThat(result.get("schuetzeStammDaten")).isEqualTo(Collections.emptyList());
        assertThat(result.get("verfuegbareSchuetzen")).isEqualTo(Collections.emptyList());
        assertThat(result.get("matchErgebnis")).isEqualTo(Collections.emptyList());
    }

    @Test
    public void prepareResponseData_withNoMatches_shouldReturnEmptyMatchRecap() {
        // Arrange
        when(mockContext.getTeamId()).thenReturn(100L);
        when(mockContext.getWettkampfId()).thenReturn(50L);
        when(mockContext.getCurrentPasseNumber()).thenReturn(5);
        when(mockContext.getOpponentMatchId()).thenReturn(301L);
        when(mockContext.buildWettkampfInfo()).thenReturn(null);
        
        when(mockContext.getMatchComponent()).thenReturn(mockMatchComponent);
        when(mockMatchComponent.findByWettkampfId(50L)).thenReturn(Collections.emptyList());
        
        // Act
        Map<String, Object> result = wettkampfEndeState.prepareResponseData(mockContext);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result).containsKey("matchErgebnis");
        
        @SuppressWarnings("unchecked")
        List<TeamMatchInfoDO> matchRecap = (List<TeamMatchInfoDO>) result.get("matchErgebnis");
        assertThat(matchRecap).isEmpty();
    }

    @Test
    public void prepareResponseData_withCurrentMatchData_shouldUseOpponentFromSession() {
        // Arrange
        when(mockContext.getTeamId()).thenReturn(100L);
        when(mockContext.getWettkampfId()).thenReturn(50L);
        when(mockContext.getCurrentMatchId()).thenReturn(300L);
        when(mockContext.getOpponentTeamId()).thenReturn(200L);
        when(mockContext.getCurrentPasseNumber()).thenReturn(5);
        when(mockContext.getOpponentMatchId()).thenReturn(301L);
        when(mockContext.buildWettkampfInfo()).thenReturn(null);
        
        // Setup current match
        MatchDO currentMatch = new MatchDO();
        currentMatch.setId(300L);
        currentMatch.setMannschaftId(100L);
        
        when(mockContext.getMatchComponent()).thenReturn(mockMatchComponent);
        when(mockMatchComponent.findByWettkampfId(50L)).thenReturn(List.of(currentMatch));
        
        // Setup pass data
        when(mockContext.getPasseComponent()).thenReturn(mockPasseComponent);
        when(mockPasseComponent.findByMannschaftMatchId(100L, 300L)).thenReturn(createTeamPasses());
        when(mockPasseComponent.findByMannschaftMatchId(200L, 300L)).thenReturn(createOpponentPasses());
        
        // Act
        Map<String, Object> result = wettkampfEndeState.prepareResponseData(mockContext);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result).containsKey("matchErgebnis");
        
        @SuppressWarnings("unchecked")
        List<TeamMatchInfoDO> matchRecap = (List<TeamMatchInfoDO>) result.get("matchErgebnis");
        assertThat(matchRecap).hasSize(2);
        
        // Verify current match used session opponent data
        verify(mockContext).getOpponentTeamId();
        verify(mockContext, never()).getMatchAnalysisService();
    }

    @Test
    public void prepareResponseData_withNonCurrentMatch_shouldFindOpponentViaAnalysisService() {
        // Arrange
        when(mockContext.getTeamId()).thenReturn(100L);
        when(mockContext.getWettkampfId()).thenReturn(50L);
        when(mockContext.getCurrentMatchId()).thenReturn(999L); // Different from match in list
        when(mockContext.getCurrentPasseNumber()).thenReturn(5);
        when(mockContext.getOpponentMatchId()).thenReturn(301L);
        when(mockContext.buildWettkampfInfo()).thenReturn(null);
        
        // Setup non-current match
        MatchDO otherMatch = new MatchDO();
        otherMatch.setId(300L);
        otherMatch.setMannschaftId(100L);
        
        when(mockContext.getMatchComponent()).thenReturn(mockMatchComponent);
        when(mockMatchComponent.findByWettkampfId(50L)).thenReturn(List.of(otherMatch));
        
        // Setup match analysis service
        when(mockContext.getMatchAnalysisService()).thenReturn(mockMatchAnalysisService);
        when(mockMatchAnalysisService.findOpponentTeamId(300L, 100L)).thenReturn(200L);
        
        // Setup pass data
        when(mockContext.getPasseComponent()).thenReturn(mockPasseComponent);
        when(mockPasseComponent.findByMannschaftMatchId(100L, 300L)).thenReturn(createTeamPasses());
        when(mockPasseComponent.findByMannschaftMatchId(200L, 300L)).thenReturn(createOpponentPasses());
        
        // Act
        Map<String, Object> result = wettkampfEndeState.prepareResponseData(mockContext);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result).containsKey("matchErgebnis");
        
        @SuppressWarnings("unchecked")
        List<TeamMatchInfoDO> matchRecap = (List<TeamMatchInfoDO>) result.get("matchErgebnis");
        assertThat(matchRecap).hasSize(2);
        
        // Verify match analysis service was used
        verify(mockMatchAnalysisService).findOpponentTeamId(300L, 100L);
    }

    @Test
    public void prepareResponseData_withNoOpponentFound_shouldSkipMatch() {
        // Arrange
        when(mockContext.getTeamId()).thenReturn(100L);
        when(mockContext.getWettkampfId()).thenReturn(50L);
        when(mockContext.getCurrentMatchId()).thenReturn(999L);
        when(mockContext.getCurrentPasseNumber()).thenReturn(5);
        when(mockContext.getOpponentMatchId()).thenReturn(301L);
        when(mockContext.buildWettkampfInfo()).thenReturn(null);
        
        // Setup match without opponent
        MatchDO matchWithoutOpponent = new MatchDO();
        matchWithoutOpponent.setId(300L);
        matchWithoutOpponent.setMannschaftId(100L);
        
        when(mockContext.getMatchComponent()).thenReturn(mockMatchComponent);
        when(mockMatchComponent.findByWettkampfId(50L)).thenReturn(List.of(matchWithoutOpponent));
        
        // Setup match analysis service to return no opponent
        when(mockContext.getMatchAnalysisService()).thenReturn(mockMatchAnalysisService);
        when(mockMatchAnalysisService.findOpponentTeamId(300L, 100L)).thenReturn(0L);
        
        // Act
        Map<String, Object> result = wettkampfEndeState.prepareResponseData(mockContext);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result).containsKey("matchErgebnis");
        
        @SuppressWarnings("unchecked")
        List<TeamMatchInfoDO> matchRecap = (List<TeamMatchInfoDO>) result.get("matchErgebnis");
        assertThat(matchRecap).isEmpty(); // Match should be skipped
    }

    private List<MatchDO> createCompletedMatches() {
        MatchDO match1 = new MatchDO();
        match1.setId(300L);
        match1.setMannschaftId(100L);
        
        return List.of(match1);
    }

    private List<PasseDO> createTeamPasses() {
        List<PasseDO> passes = new ArrayList<>();
        
        // Set 1: Team scores 28 points
        PasseDO pass1 = new PasseDO();
        pass1.setPasseMannschaftId(100L);
        pass1.setPasseMatchId(300L);
        pass1.setPasseLfdnr(1L);
        pass1.setPasseDsbMitgliedId(1L);
        pass1.setPfeil1(9);
        pass1.setPfeil2(9);
        pass1.setPfeil3(10);
        passes.add(pass1);
        
        // Set 2: Team scores 25 points
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
        
        // Set 1: Opponent scores 26 points
        PasseDO pass1 = new PasseDO();
        pass1.setPasseMannschaftId(200L);
        pass1.setPasseMatchId(300L);
        pass1.setPasseLfdnr(1L);
        pass1.setPasseDsbMitgliedId(2L);
        pass1.setPfeil1(8);
        pass1.setPfeil2(9);
        pass1.setPfeil3(9);
        passes.add(pass1);
        
        // Set 2: Opponent scores 27 points
        PasseDO pass2 = new PasseDO();
        pass2.setPasseMannschaftId(200L);
        pass2.setPasseMatchId(300L);
        pass2.setPasseLfdnr(2L);
        pass2.setPasseDsbMitgliedId(2L);
        pass2.setPfeil1(9);
        pass2.setPfeil2(9);
        pass2.setPfeil3(9);
        passes.add(pass2);
        
        return passes;
    }
}