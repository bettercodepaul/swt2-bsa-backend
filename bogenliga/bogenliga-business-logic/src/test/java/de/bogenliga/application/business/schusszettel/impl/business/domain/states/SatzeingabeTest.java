package de.bogenliga.application.business.schusszettel.impl.business.domain.states;

import de.bogenliga.application.business.schusszettel.api.types.SatzEingabeDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.SchuetzenSatzDO;
import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Comprehensive tests for the Satzeingabe state class.
 * Tests state behavior, validation, and score submission handling.
 */
@RunWith(MockitoJUnitRunner.class)
public class SatzeingabeTest {

    @Mock
    private StateContext mockContext;

    @Mock
    private TabletSchusszettelEntity mockOpponent;

    private Satzeingabe satzeingabeState;

    @Before
    public void setUp() {
        satzeingabeState = new Satzeingabe();
    }

    @Test
    public void stateConstants_shouldHaveCorrectValues() {
        // Assert - test the state constants are available
        assertThat(Satzeingabe.STATUS_SATZEINGABE).isEqualTo("SATZEINGABE");
        assertThat(Satzeingabe.STATUS_WARTE).isEqualTo("WARTE");
    }

    @Test
    public void canNudgeAlong_shouldReturnTrue() {
        // Act
        boolean result = satzeingabeState.canNudgeAlong();
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void isValidState_withIncompleteMatch_shouldReturnTrue() {
        // Arrange
        when(mockContext.isMatchComplete()).thenReturn(false);
        
        // Act
        boolean result = satzeingabeState.isValidState(mockContext);
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void isValidState_withCompleteMatch_shouldReturnFalse() {
        // Arrange
        when(mockContext.isMatchComplete()).thenReturn(true);
        
        // Act
        boolean result = satzeingabeState.isValidState(mockContext);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void canTransitionTo_withWarte_shouldReturnTrue() {
        // Act
        boolean result = satzeingabeState.canTransitionTo(mockContext, "WARTE");
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void canTransitionTo_withOtherState_shouldReturnFalse() {
        // Act
        boolean result = satzeingabeState.canTransitionTo(mockContext, "SCHUETZENMELDUNG");
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void validateOperation_withSubmitSatz_validData_shouldReturnTrue() {
        // Arrange
        SatzEingabeDO eingabe = createValidSatzEingabe();
        
        // Act
        boolean result = satzeingabeState.validateOperation(mockContext, "submitSatz", eingabe);
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void validateOperation_withSubmitSatz_nullData_shouldReturnFalse() {
        // Act
        boolean result = satzeingabeState.validateOperation(mockContext, "submitSatz", null);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void validateOperation_withSubmitSatz_invalidArrowValue_shouldReturnFalse() {
        // Arrange
        SatzEingabeDO eingabe = createSatzEingabeWithInvalidArrow();
        
        // Act
        boolean result = satzeingabeState.validateOperation(mockContext, "submitSatz", eingabe);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void validateOperation_withOtherOperation_shouldReturnFalse() {
        // Act
        boolean result = satzeingabeState.validateOperation(mockContext, "submitSchuetzen", null);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void handlePostOperation_withValidSubmitSatz_shouldReturnTrue() {
        // Arrange
        SatzEingabeDO eingabe = createValidSatzEingabe();
        
        // Act
        boolean result = satzeingabeState.handlePostOperation(mockContext, "submitSatz", eingabe);
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void handlePostOperation_withInvalidOperation_shouldReturnFalse() {
        // Act
        boolean result = satzeingabeState.handlePostOperation(mockContext, "submitSchuetzen", null);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void handleWarteEvaluation_shouldReturnFalse() {
        // Act
        boolean result = satzeingabeState.handleWarteEvaluation(mockContext, mockOpponent);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void prepareResponseData_shouldReturnCorrectData() {
        // Arrange
        when(mockContext.getTeamId()).thenReturn(100L);
        when(mockContext.getCurrentMatchId()).thenReturn(200L);
        when(mockContext.getOpponentMatchId()).thenReturn(201L);
        when(mockContext.getCurrentPasseNumber()).thenReturn(2);
        when(mockContext.buildWettkampfInfo()).thenReturn(null);
        when(mockContext.getAllMatchPasses()).thenReturn(List.of());
        
        // Act
        Map<String, Object> result = satzeingabeState.prepareResponseData(mockContext);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.get("currentPasseNumber")).isEqualTo(2);
        assertThat(result.get("eigenesTeamMatchId")).isEqualTo(200L);
        assertThat(result.get("gegnerischesTeamMatchId")).isEqualTo(201L);
        assertThat(result).containsKey("schuetzeStammDaten");
        assertThat(result).containsKey("satzErgebnisse");
        assertThat(result).containsKey("schuetzenMatchPunkte");
    }

    private SatzEingabeDO createValidSatzEingabe() {
        SatzEingabeDO eingabe = new SatzEingabeDO();
        
        SchuetzenSatzDO schuetze1 = new SchuetzenSatzDO();
        schuetze1.setSchuetzenId(1L);
        schuetze1.setSchuss1(8);
        schuetze1.setSchuss2(9);
        schuetze1.setSchuss3(7);
        
        SchuetzenSatzDO schuetze2 = new SchuetzenSatzDO();
        schuetze2.setSchuetzenId(2L);
        schuetze2.setSchuss1(10);
        schuetze2.setSchuss2(8);
        schuetze2.setSchuss3(9);
        
        SchuetzenSatzDO schuetze3 = new SchuetzenSatzDO();
        schuetze3.setSchuetzenId(3L);
        schuetze3.setSchuss1(6);
        schuetze3.setSchuss2(7);
        schuetze3.setSchuss3(8);
        
        eingabe.setSatzeingabe(Arrays.asList(schuetze1, schuetze2, schuetze3));
        return eingabe;
    }

    private SatzEingabeDO createSatzEingabeWithInvalidArrow() {
        SatzEingabeDO eingabe = new SatzEingabeDO();
        
        SchuetzenSatzDO schuetze1 = new SchuetzenSatzDO();
        schuetze1.setSchuetzenId(1L);
        schuetze1.setSchuss1(11); // Invalid: > 10
        schuetze1.setSchuss2(9);
        schuetze1.setSchuss3(7);
        
        eingabe.setSatzeingabe(List.of(schuetze1));
        return eingabe;
    }

    @Test
    public void prepareResponseData_withValidMatch_shouldReturnCompleteData() {
        // Arrange
        when(mockContext.getTeamId()).thenReturn(100L);
        when(mockContext.getCurrentMatchId()).thenReturn(200L);
        when(mockContext.getOpponentMatchId()).thenReturn(201L);
        when(mockContext.getCurrentPasseNumber()).thenReturn(2);
        when(mockContext.buildWettkampfInfo()).thenReturn(null);
        when(mockContext.getAllMatchPasses()).thenReturn(Collections.emptyList());
        
        // Act
        Map<String, Object> result = satzeingabeState.prepareResponseData(mockContext);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.get("currentPasseNumber")).isEqualTo(2);
        assertThat(result.get("eigenesTeamMatchId")).isEqualTo(200L);
        assertThat(result.get("gegnerischesTeamMatchId")).isEqualTo(201L);
        assertThat(result).containsKey("satzErgebnisse");
        assertThat(result).containsKey("schuetzenMatchPunkte");
        assertThat(result).containsKey("schuetzeStammDaten");
        assertThat(result).containsKey("verfuegbareSchuetzen");
    }

    @Test
    public void prepareResponseData_withException_shouldReturnEmptyLists() {
        // Arrange
        when(mockContext.getTeamId()).thenReturn(100L);
        when(mockContext.getCurrentMatchId()).thenReturn(200L);
        when(mockContext.getOpponentMatchId()).thenReturn(201L);
        when(mockContext.getCurrentPasseNumber()).thenReturn(2);
        when(mockContext.buildWettkampfInfo()).thenThrow(new RuntimeException("Test exception"));
        
        // Act
        Map<String, Object> result = satzeingabeState.prepareResponseData(mockContext);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.get("satzErgebnisse")).isEqualTo(Collections.emptyList());
        assertThat(result.get("schuetzenMatchPunkte")).isEqualTo(Collections.emptyList());
        assertThat(result.get("schuetzeStammDaten")).isEqualTo(Collections.emptyList());
        assertThat(result.get("verfuegbareSchuetzen")).isEqualTo(Collections.emptyList());
    }

    @Test
    public void canTransitionTo_withNullTargetState_shouldReturnFalse() {
        // Act
        boolean result = satzeingabeState.canTransitionTo(mockContext, null);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void canTransitionTo_withMatchEnde_shouldReturnFalse() {
        // Act
        boolean result = satzeingabeState.canTransitionTo(mockContext, "MATCH_ENDE");
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void isDatabaseReadyForTransition_shouldAlwaysReturnTrue() {
        // Act
        boolean result = satzeingabeState.isDatabaseReadyForTransition(mockContext, "WARTE");
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void validateOperation_withNonSubmitSatzOperation_shouldReturnFalse() {
        // Act
        boolean result = satzeingabeState.validateOperation(mockContext, "submitSchuetzen", null);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void handlePostOperation_withNonSubmitSatzOperation_shouldReturnFalse() {
        // Act
        boolean result = satzeingabeState.handlePostOperation(mockContext, "submitSchuetzen", null);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void handlePostOperation_withNullData_shouldReturnFalse() {
        // Act
        boolean result = satzeingabeState.handlePostOperation(mockContext, "submitSatz", null);
        
        // Assert
        assertThat(result).isFalse();
    }
}