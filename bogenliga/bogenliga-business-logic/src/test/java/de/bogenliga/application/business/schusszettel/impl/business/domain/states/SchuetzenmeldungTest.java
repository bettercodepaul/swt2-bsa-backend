package de.bogenliga.application.business.schusszettel.impl.business.domain.states;

import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
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
 * Comprehensive tests for the Schuetzenmeldung state class.
 * Tests state behavior, validation, and operation handling.
 */
@RunWith(MockitoJUnitRunner.class)
public class SchuetzenmeldungTest {

    @Mock
    private StateContext mockContext;

    @Mock
    private TabletSchusszettelEntity mockOpponent;

    private Schuetzenmeldung schuetzenmeldungState;

    @Before
    public void setUp() {
        schuetzenmeldungState = new Schuetzenmeldung();
    }

    @Test
    public void stateConstants_shouldHaveCorrectValues() {
        // Assert - test the state constants are available
        assertThat(Schuetzenmeldung.STATUS_SCHUETZENMELDUNG).isEqualTo("SCHUETZENMELDUNG");
        assertThat(Schuetzenmeldung.STATUS_SATZEINGABE).isEqualTo("SATZEINGABE");
    }

    @Test
    public void canNudgeAlong_shouldReturnTrue() {
        // Act
        boolean result = schuetzenmeldungState.canNudgeAlong();
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void isValidState_shouldAlwaysReturnTrue() {
        // Act
        boolean result = schuetzenmeldungState.isValidState(mockContext);
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void canTransitionTo_withSatzeingabe_shouldReturnTrue() {
        // Act
        boolean result = schuetzenmeldungState.canTransitionTo(mockContext, "SATZEINGABE");
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void canTransitionTo_withOtherState_shouldReturnFalse() {
        // Act
        boolean result = schuetzenmeldungState.canTransitionTo(mockContext, "WARTE");
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void validateOperation_withSubmitSchuetzen_validShooters_shouldReturnTrue() {
        // Arrange
        List<Long> validShooters = Arrays.asList(1L, 2L, 3L);
        
        // Act
        boolean result = schuetzenmeldungState.validateOperation(mockContext, "submitSchuetzen", validShooters);
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void validateOperation_withSubmitSchuetzen_twoShooters_shouldReturnFalse() {
        // Arrange
        List<Long> invalidShooters = Arrays.asList(1L, 2L);
        
        // Act
        boolean result = schuetzenmeldungState.validateOperation(mockContext, "submitSchuetzen", invalidShooters);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void validateOperation_withSubmitSchuetzen_fourShooters_shouldReturnFalse() {
        // Arrange
        List<Long> invalidShooters = Arrays.asList(1L, 2L, 3L, 4L);
        
        // Act
        boolean result = schuetzenmeldungState.validateOperation(mockContext, "submitSchuetzen", invalidShooters);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void validateOperation_withSubmitSchuetzen_nullShooters_shouldReturnFalse() {
        // Act
        boolean result = schuetzenmeldungState.validateOperation(mockContext, "submitSchuetzen", null);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void validateOperation_withSubmitSchuetzen_emptyShooters_shouldReturnFalse() {
        // Arrange
        List<Long> emptyShooters = Collections.emptyList();
        
        // Act
        boolean result = schuetzenmeldungState.validateOperation(mockContext, "submitSchuetzen", emptyShooters);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void validateOperation_withOtherOperation_shouldReturnFalse() {
        // Act
        boolean result = schuetzenmeldungState.validateOperation(mockContext, "submitSatz", null);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void handlePostOperation_withValidSubmitSchuetzen_shouldReturnTrue() {
        // Arrange
        List<Long> shooters = Arrays.asList(1L, 2L, 3L);
        
        // Act
        boolean result = schuetzenmeldungState.handlePostOperation(mockContext, "submitSchuetzen", shooters);
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void handlePostOperation_withInvalidOperation_shouldReturnFalse() {
        // Act
        boolean result = schuetzenmeldungState.handlePostOperation(mockContext, "submitSatz", null);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void handleWarteEvaluation_shouldReturnFalse() {
        // Act
        boolean result = schuetzenmeldungState.handleWarteEvaluation(mockContext, mockOpponent);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void prepareResponseData_shouldReturnCorrectData() {
        // Arrange
        MannschaftsmitgliedDO member1 = new MannschaftsmitgliedDO(
            1L,  // id
            100L,  // mannschaftId
            101L,  // dsbMitgliedId
            1,  // dsbMitgliedEingesetzt
            "Vorname1",  // dsbMitgliedVorname
            "Nachname1",  // dsbMitgliedNachname
            1L  // rueckennummer
        );
        List<MannschaftsmitgliedDO> members = List.of(member1);
        
        when(mockContext.getTeamId()).thenReturn(100L);
        when(mockContext.getCurrentMatchId()).thenReturn(200L);
        when(mockContext.getOpponentMatchId()).thenReturn(201L);
        when(mockContext.getCurrentPasseNumber()).thenReturn(1);
        when(mockContext.buildWettkampfInfo()).thenReturn(null);
        when(mockContext.getDeployedTeamMembers()).thenReturn(members);
        
        // Act
        Map<String, Object> result = schuetzenmeldungState.prepareResponseData(mockContext);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.get("currentPasseNumber")).isEqualTo(1);
        assertThat(result.get("eigenesTeamMatchId")).isEqualTo(200L);
        assertThat(result.get("gegnerischesTeamMatchId")).isEqualTo(201L);
        assertThat(result).containsKey("verfuegbareSchuetzen");
        assertThat(result).containsKey("wettkampfInfo");
    }

    @Test
    public void prepareResponseData_withNullContext_shouldHandleGracefully() {
        // Act & Assert
        Map<String, Object> result = schuetzenmeldungState.prepareResponseData(null);
        
        assertThat(result).isNotNull();
        assertThat(result).containsKey("currentPasseNumber");
        assertThat(result.get("currentPasseNumber")).isEqualTo(0);
    }

    @Test
    public void prepareResponseData_withException_shouldReturnEmptyLists() {
        // Arrange
        when(mockContext.getTeamId()).thenReturn(100L);
        when(mockContext.getCurrentMatchId()).thenReturn(200L);
        when(mockContext.getOpponentMatchId()).thenReturn(201L);
        when(mockContext.getCurrentPasseNumber()).thenReturn(1);
        when(mockContext.buildWettkampfInfo()).thenReturn(null);
        when(mockContext.getDeployedTeamMembers()).thenThrow(new RuntimeException("Test exception"));
        
        // Act
        Map<String, Object> result = schuetzenmeldungState.prepareResponseData(mockContext);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.get("verfuegbareSchuetzen")).isEqualTo(Collections.emptyList());
        assertThat(result).containsKey("satzErgebnisse");
        assertThat(result).containsKey("schuetzenMatchPunkte");
        assertThat(result).containsKey("schuetzeStammDaten");
    }

    @Test
    public void canTransitionTo_withNullTargetState_shouldReturnFalse() {
        // Act
        boolean result = schuetzenmeldungState.canTransitionTo(mockContext, null);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void isDatabaseReadyForTransition_shouldAlwaysReturnTrue() {
        // Act
        boolean result = schuetzenmeldungState.isDatabaseReadyForTransition(mockContext, "SATZEINGABE");
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void validateOperation_withNullData_shouldReturnFalse() {
        // Act
        boolean result = schuetzenmeldungState.validateOperation(mockContext, "submitSchuetzen", null);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void validateOperation_withNonListData_shouldReturnFalse() {
        // Act
        boolean result = schuetzenmeldungState.validateOperation(mockContext, "submitSchuetzen", "invalid");
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void handlePostOperation_withNullData_shouldReturnFalse() {
        // Act
        boolean result = schuetzenmeldungState.handlePostOperation(mockContext, "submitSchuetzen", null);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void handlePostOperation_withNonListData_shouldReturnFalse() {
        // Act
        boolean result = schuetzenmeldungState.handlePostOperation(mockContext, "submitSchuetzen", "invalid");
        
        // Assert
        assertThat(result).isFalse();
    }
}