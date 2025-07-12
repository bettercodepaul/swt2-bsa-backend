package de.bogenliga.application.business.schusszettel.impl.business.domain.states;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Comprehensive tests for the State abstract class and factory method.
 * Tests the state pattern factory implementation and state constants.
 * 
 * @author Test Generator - State pattern factory tests
 */
public class StateTest {

    @Test
    public void fromString_withSchuetzenmeldung_shouldReturnSchuetzenmeldungState() {
        // Act
        State result = State.fromString("SCHUETZENMELDUNG");
        
        // Assert
        assertThat(result).isInstanceOf(Schuetzenmeldung.class);
        assertThat(result).isInstanceOf(Schuetzenmeldung.class);
    }

    @Test
    public void fromString_withSatzeingabe_shouldReturnSatzeingabeState() {
        // Act
        State result = State.fromString("SATZEINGABE");
        
        // Assert
        assertThat(result).isInstanceOf(Satzeingabe.class);
        assertThat(result).isInstanceOf(Satzeingabe.class);
    }

    @Test
    public void fromString_withWarte_shouldReturnWarteState() {
        // Act
        State result = State.fromString("WARTE");
        
        // Assert
        assertThat(result).isInstanceOf(Warte.class);
        assertThat(result).isInstanceOf(Warte.class);
    }

    @Test
    public void fromString_withMatchEnde_shouldReturnMatchEndeState() {
        // Act
        State result = State.fromString("MATCH_ENDE");
        
        // Assert
        assertThat(result).isInstanceOf(MatchEnde.class);
        assertThat(result).isInstanceOf(MatchEnde.class);
    }

    @Test
    public void fromString_withWettkampfEnde_shouldReturnWettkampfEndeState() {
        // Act
        State result = State.fromString("WETTKAMPF_ENDE");
        
        // Assert
        assertThat(result).isInstanceOf(WettkampfEnde.class);
        assertThat(result).isInstanceOf(WettkampfEnde.class);
    }

    @Test
    public void fromString_withUnknownStatus_shouldThrowIllegalArgumentException() {
        // Act & Assert
        assertThatThrownBy(() -> State.fromString("UNKNOWN_STATUS"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void fromString_withNullStatus_shouldThrowIllegalArgumentException() {
        // Act & Assert
        assertThatThrownBy(() -> State.fromString(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void fromString_withEmptyStatus_shouldThrowIllegalArgumentException() {
        // Act & Assert
        assertThatThrownBy(() -> State.fromString(""))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void stateConstants_shouldHaveCorrectValues() {
        // Assert
        assertThat(State.STATUS_SCHUETZENMELDUNG).isEqualTo("SCHUETZENMELDUNG");
        assertThat(State.STATUS_SATZEINGABE).isEqualTo("SATZEINGABE");
        assertThat(State.STATUS_WARTE).isEqualTo("WARTE");
        assertThat(State.STATUS_MATCH_ENDE).isEqualTo("MATCH_ENDE");
        assertThat(State.STATUS_WETTKAMPF_ENDE).isEqualTo("WETTKAMPF_ENDE");
    }

    @Test
    public void defaultMethods_shouldProvideCorrectDefaults() {
        // Arrange
        State state = State.fromString("SCHUETZENMELDUNG");
        
        // Act & Assert - test default implementations
        assertThat(state.canTransitionTo(null, "SATZEINGABE")).isTrue();
        assertThat(state.isDatabaseReadyForTransition(null, "SATZEINGABE")).isTrue();
        assertThat(state.validateOperation(null, "test", null)).isFalse();
        assertThat(state.handlePostOperation(null, "test", null)).isFalse();
        assertThat(state.prepareResponseData(null)).isNotNull();
    }
}