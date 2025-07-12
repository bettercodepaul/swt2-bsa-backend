package de.bogenliga.application.business.schusszettel.impl.business.domain.states;

import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.business.schusszettel.impl.business.domain.SessionRuntime;
import de.bogenliga.application.business.schusszettel.impl.business.serviceAdapter.MatchAnalysisService;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.WettkampfInfoDO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Complete tests for StateContext class covering all untested methods.
 * This test class focuses on the 25+ methods that were missing coverage.
 */
@RunWith(MockitoJUnitRunner.class)
public class StateContextCompleteTest {

    @Mock
    private TabletSchusszettelEntity mockSession;

    @Mock
    private SessionRuntime mockSessionRuntime;

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

    private StateContext stateContext;


    public StateContextCompleteTest() {
    }


    @Before
    public void setUp() {
        stateContext = new StateContext(
            mockSession,
            mockSessionRuntime,
            mockMatchComponent,
            mockPasseComponent,
            mockMatchAnalysisService,
            mockMannschaftsmitgliedComponent,
            mockDsbMitgliedComponent,
            mockWettkampfComponent,
            mockVeranstaltungComponent
        );
    }

    // Session operations tests

    @Test
    public void updateSessionStatus_withValidStatus_shouldDelegateToSessionRuntime() {
        // Act
        stateContext.updateSessionStatus("WARTE");
        
        // Assert
        verify(mockSessionRuntime).updateSessionStatus("WARTE");
    }

    @Test
    public void updatePasseNumber_withValidNumber_shouldDelegateToSessionRuntime() {
        // Act
        stateContext.updatePasseNumber(3);
        
        // Assert
        verify(mockSessionRuntime).updatePasseNumber(3);
    }

    @Test
    public void advanceToNextMatch_withValidMatch_shouldDelegateToSessionRuntime() {
        // Arrange
        LigamatchBE nextMatch = new LigamatchBE();
        nextMatch.setMatchId(400L);
        
        // Act
        stateContext.advanceToNextMatch(nextMatch, 200L);
        
        // Assert
        verify(mockSessionRuntime).advanceToNextMatch(nextMatch, 200L);
    }

    // Data access method tests

    @Test
    public void getTeamMembers_shouldReturnAllTeamMembers() {
        // Arrange
        when(mockSession.getTeamId()).thenReturn(100L);
        
        List<MannschaftsmitgliedDO> members = Arrays.asList(
            createMannschaftsmitglied(1L, 1, 1),
            createMannschaftsmitglied(2L, 2, 0),
            createMannschaftsmitglied(3L, 3, 1)
        );
        
        when(mockMannschaftsmitgliedComponent.findByTeamId(100L)).thenReturn(members);
        
        // Act
        List<MannschaftsmitgliedDO> result = stateContext.getTeamMembers();
        
        // Assert
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(2).getId()).isEqualTo(3L);
    }

    @Test
    public void getDeployedTeamMembers_shouldReturnOnlyDeployedMembers() {
        // Arrange
        when(mockSession.getTeamId()).thenReturn(100L);
        
        List<MannschaftsmitgliedDO> members = Arrays.asList(
            createMannschaftsmitglied(1L, 1, 1),  // Deployed
            createMannschaftsmitglied(2L, 2, 0),  // Not deployed
            createMannschaftsmitglied(3L, 3, 1)   // Deployed
        );
        
        when(mockMannschaftsmitgliedComponent.findByTeamId(100L)).thenReturn(members);
        
        // Act
        List<MannschaftsmitgliedDO> result = stateContext.getDeployedTeamMembers();
        
        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1).getId()).isEqualTo(3L);
    }

    @Test
    public void getCurrentPasseData_shouldReturnCurrentPasseData() {
        // Arrange
        when(mockSession.getTeamId()).thenReturn(100L);
        when(mockSession.getCurrentMatchId()).thenReturn(300L);
        when(mockSession.getCurrentPasseNumber()).thenReturn(2);
        
        List<PasseDO> allPasses = Arrays.asList(
            createPasseDO(1L, 1L),
            createPasseDO(2L, 2L),
            createPasseDO(3L, 2L),
            createPasseDO(4L, 3L)
        );
        
        when(mockPasseComponent.findByMannschaftMatchId(100L, 300L)).thenReturn(allPasses);
        
        // Act
        List<PasseDO> result = stateContext.getCurrentPasseData();
        
        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getPasseLfdnr()).isEqualTo(2L);
        assertThat(result.get(1).getPasseLfdnr()).isEqualTo(2L);
    }

    @Test
    public void getAllMatchPasses_shouldReturnAllPasses() {
        // Arrange
        when(mockSession.getTeamId()).thenReturn(100L);
        when(mockSession.getCurrentMatchId()).thenReturn(300L);
        
        List<PasseDO> allPasses = Arrays.asList(
            createPasseDO(1L, 1L),
            createPasseDO(2L, 2L),
            createPasseDO(3L, 3L)
        );
        
        when(mockPasseComponent.findByMannschaftMatchId(100L, 300L)).thenReturn(allPasses);
        
        // Act
        List<PasseDO> result = stateContext.getAllMatchPasses();
        
        // Assert
        assertThat(result).hasSize(3);
    }

    @Test
    public void isCurrentPasseComplete_shouldReturnTrueWhenThreeShooters() {
        // Arrange
        when(mockSession.getTeamId()).thenReturn(100L);
        when(mockSession.getCurrentMatchId()).thenReturn(300L);
        when(mockSession.getCurrentPasseNumber()).thenReturn(2);
        
        List<PasseDO> currentPasses = Arrays.asList(
            createPasseDO(1L, 2L),
            createPasseDO(2L, 2L),
            createPasseDO(3L, 2L)
        );
        
        when(mockPasseComponent.findByMannschaftMatchId(100L, 300L)).thenReturn(currentPasses);
        
        // Act
        boolean result = stateContext.isCurrentPasseComplete();
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void isCurrentPasseComplete_shouldReturnFalseWhenLessThanThreeShooters() {
        // Arrange
        when(mockSession.getTeamId()).thenReturn(100L);
        when(mockSession.getCurrentMatchId()).thenReturn(300L);
        when(mockSession.getCurrentPasseNumber()).thenReturn(2);
        
        List<PasseDO> currentPasses = Arrays.asList(
            createPasseDO(1L, 2L),
            createPasseDO(2L, 2L)
        );
        
        when(mockPasseComponent.findByMannschaftMatchId(100L, 300L)).thenReturn(currentPasses);
        
        // Act
        boolean result = stateContext.isCurrentPasseComplete();
        
        // Assert
        assertThat(result).isFalse();
    }

    // Validation method tests

    @Test
    public void validateArrowValue_withValidValue_shouldReturnValid() {
        // Act
        StateContext.ValidationResult result = stateContext.validateArrowValue(8);
        
        // Assert
        assertThat(result.isValid()).isTrue();
        assertThat(result.getErrorMessage()).isNull();
    }

    @Test
    public void validateArrowValue_withMinValue_shouldReturnValid() {
        // Act
        StateContext.ValidationResult result = stateContext.validateArrowValue(0);
        
        // Assert
        assertThat(result.isValid()).isTrue();
        assertThat(result.getErrorMessage()).isNull();
    }

    @Test
    public void validateArrowValue_withMaxValue_shouldReturnValid() {
        // Act
        StateContext.ValidationResult result = stateContext.validateArrowValue(10);
        
        // Assert
        assertThat(result.isValid()).isTrue();
        assertThat(result.getErrorMessage()).isNull();
    }

    @Test
    public void validateArrowValue_withNullValue_shouldReturnInvalid() {
        // Act
        StateContext.ValidationResult result = stateContext.validateArrowValue(null);
        
        // Assert
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrorMessage()).isEqualTo("Arrow value cannot be null");
    }

    @Test
    public void validateArrowValue_withNegativeValue_shouldReturnInvalid() {
        // Act
        StateContext.ValidationResult result = stateContext.validateArrowValue(-1);
        
        // Assert
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrorMessage()).isEqualTo("Arrow value cannot be negative: -1");
    }

    @Test
    public void validateArrowValue_withTooHighValue_shouldReturnInvalid() {
        // Act
        StateContext.ValidationResult result = stateContext.validateArrowValue(11);
        
        // Assert
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrorMessage()).isEqualTo("Arrow value cannot exceed 10: 11");
    }

    @Test
    public void validateSessionState_withValidSession_shouldReturnValid() {
        // Arrange
        when(mockSession.getTeamId()).thenReturn(100L);
        when(mockSession.getWettkampfId()).thenReturn(50L);
        when(mockSession.getCurrentMatchId()).thenReturn(300L);
        when(mockSession.getCurrentPasseNumber()).thenReturn(2);
        when(mockSession.getStatus()).thenReturn("WARTE");
        
        // Act
        StateContext.ValidationResult result = stateContext.validateSessionState();
        
        // Assert
        assertThat(result.isValid()).isTrue();
    }

    @Test
    public void validateSessionState_withInvalidTeamId_shouldReturnInvalid() {
        // Arrange
        when(mockSession.getTeamId()).thenReturn(0L);
        
        // Act
        StateContext.ValidationResult result = stateContext.validateSessionState();
        
        // Assert
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrorMessage()).isEqualTo("Invalid team ID: 0");
    }

    @Test
    public void validateSessionState_withInvalidPasseNumber_shouldReturnInvalid() {
        // Arrange
        when(mockSession.getTeamId()).thenReturn(100L);
        when(mockSession.getWettkampfId()).thenReturn(50L);
        when(mockSession.getCurrentMatchId()).thenReturn(300L);
        when(mockSession.getCurrentPasseNumber()).thenReturn(6);
        when(mockSession.getStatus()).thenReturn("WARTE");
        
        // Act
        StateContext.ValidationResult result = stateContext.validateSessionState();
        
        // Assert
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrorMessage()).isEqualTo("Invalid passe number: 6");
    }

    // Shooter validation tests

    @Test
    public void isShooterRegistered_withRegisteredShooter_shouldReturnTrue() {
        // Arrange
        when(mockSession.getTeamId()).thenReturn(100L);
        when(mockSession.getCurrentMatchId()).thenReturn(300L);
        
        List<PasseDO> passes = List.of(
                createPasseDOWithShooter()
        );
        
        when(mockPasseComponent.findByMannschaftMatchId(100L, 300L)).thenReturn(passes);
        
        // Act
        boolean result = stateContext.isShooterRegistered(42L, 1);
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void isShooterRegistered_withUnregisteredShooter_shouldReturnFalse() {
        // Arrange
        when(mockSession.getTeamId()).thenReturn(100L);
        when(mockSession.getCurrentMatchId()).thenReturn(300L);
        
        List<PasseDO> passes = List.of(
                createPasseDOWithShooter()
        );
        
        when(mockPasseComponent.findByMannschaftMatchId(100L, 300L)).thenReturn(passes);
        
        // Act
        boolean result = stateContext.isShooterRegistered(99L, 1);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void isShooterDeployed_withDeployedShooter_shouldReturnTrue() {
        // Arrange
        when(mockSession.getTeamId()).thenReturn(100L);
        
        List<MannschaftsmitgliedDO> members = List.of(
                createMannschaftsmitglied(42L, 1, 1)  // Deployed
        );
        
        when(mockMannschaftsmitgliedComponent.findByTeamId(100L)).thenReturn(members);
        
        // Act
        boolean result = stateContext.isShooterDeployed(42L);
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void isShooterDeployed_withUndeployedShooter_shouldReturnFalse() {
        // Arrange
        when(mockSession.getTeamId()).thenReturn(100L);
        
        List<MannschaftsmitgliedDO> members = List.of(
                createMannschaftsmitglied(42L, 1, 0)  // Not deployed
        );
        
        when(mockMannschaftsmitgliedComponent.findByTeamId(100L)).thenReturn(members);
        
        // Act
        boolean result = stateContext.isShooterDeployed(42L);
        
        // Assert
        assertThat(result).isFalse();
    }

    // WettkampfInfo tests

    @Test
    public void buildWettkampfInfo_withValidData_shouldReturnWettkampfInfo() {
        // Arrange
        when(mockSession.getWettkampfId()).thenReturn(50L);
        
        WettkampfDO wettkampf = new WettkampfDO();
        wettkampf.setId(50L);
        wettkampf.setWettkampfTag(1L);
        wettkampf.setWettkampfDatum(Date.valueOf(LocalDate.now()));
        wettkampf.setWettkampfBeginn(LocalDate.now().toString());
        wettkampf.setWettkampfOrtsname("Test Ort");
        wettkampf.setWettkampfVeranstaltungsId(10L);
        
        VeranstaltungDO veranstaltung = new VeranstaltungDO();
        veranstaltung.setVeranstaltungID(10L);
        veranstaltung.setVeranstaltungName("Test Veranstaltung");
        veranstaltung.setVeranstaltungSportJahr(2023L);
        
        when(mockWettkampfComponent.findById(50L)).thenReturn(wettkampf);
        when(mockVeranstaltungComponent.findById(10L)).thenReturn(veranstaltung);
        
        // Act
        WettkampfInfoDO result = stateContext.buildWettkampfInfo();
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getWettkampfId()).isEqualTo(50L);
        assertThat(result.getWettkampfTag()).isEqualTo(1);
        assertThat(result.getWettkampfOrtsname()).isEqualTo("Test Ort");
        assertThat(result.getVeranstaltungId()).isEqualTo(10L);
        assertThat(result.getVeranstaltungName()).isEqualTo("Test Veranstaltung");
        assertThat(result.getVeranstaltungSportjahr()).isEqualTo(2023L);
    }

    @Test
    public void buildWettkampfInfo_withException_shouldReturnNull() {
        // Arrange
        when(mockSession.getWettkampfId()).thenReturn(50L);
        when(mockWettkampfComponent.findById(50L)).thenThrow(new RuntimeException("Test exception"));
        
        // Act
        WettkampfInfoDO result = stateContext.buildWettkampfInfo();
        
        // Assert
        assertThat(result).isNull();
    }

    // Component access tests

    @Test
    public void getMatchComponent_shouldReturnMatchComponent() {
        // Act
        MatchComponent result = stateContext.getMatchComponent();
        
        // Assert
        assertThat(result).isEqualTo(mockMatchComponent);
    }

    @Test
    public void getPasseComponent_shouldReturnPasseComponent() {
        // Act
        PasseComponent result = stateContext.getPasseComponent();
        
        // Assert
        assertThat(result).isEqualTo(mockPasseComponent);
    }

    @Test
    public void getMatchAnalysisService_shouldReturnMatchAnalysisService() {
        // Act
        MatchAnalysisService result = stateContext.getMatchAnalysisService();
        
        // Assert
        assertThat(result).isEqualTo(mockMatchAnalysisService);
    }


    // Helper methods for creating test data

    private MannschaftsmitgliedDO createMannschaftsmitglied(Long id, int rueckennummer, int eingesetzt) {
        return new MannschaftsmitgliedDO(
            id,  // id
            100L,  // mannschaftId
            id * 10,  // dsbMitgliedId
            eingesetzt,  // dsbMitgliedEingesetzt
            "Vorname" + id,  // dsbMitgliedVorname
            "Nachname" + id,  // dsbMitgliedNachname
            (long) rueckennummer  // rueckennummer
        );
    }

    private PasseDO createPasseDO(Long id, Long passeLfdnr) {
        PasseDO passe = new PasseDO();
        passe.setId(id);
        passe.setPasseLfdnr(passeLfdnr);
        return passe;
    }

    private PasseDO createPasseDOWithShooter() {
        PasseDO passe = new PasseDO();
        passe.setId(1L);
        passe.setPasseLfdnr(1L);
        passe.setPasseDsbMitgliedId(42L);
        return passe;
    }
}