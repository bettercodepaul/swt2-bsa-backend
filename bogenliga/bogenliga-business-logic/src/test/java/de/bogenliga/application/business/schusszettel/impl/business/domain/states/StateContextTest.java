package de.bogenliga.application.business.schusszettel.impl.business.domain.states;

import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.business.schusszettel.impl.business.domain.SessionRuntime;
import de.bogenliga.application.business.schusszettel.impl.business.serviceAdapter.MatchAnalysisService;
import de.bogenliga.application.business.schusszettel.impl.dao.TabletSchusszettelDAO;
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
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Comprehensive tests for the StateContext class.
 * Tests data access, service delegation, and validation functionality.
 */
@RunWith(MockitoJUnitRunner.class)
public class StateContextTest {

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

    @Mock
    private TabletSchusszettelDAO mockSessionDAO;

    private StateContext stateContext;

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

    @Test
    public void getTeamId_shouldReturnSessionTeamId() {
        // Arrange
        when(mockSession.getTeamId()).thenReturn(100L);
        
        // Act
        long result = stateContext.getTeamId();
        
        // Assert
        assertThat(result).isEqualTo(100L);
    }

    @Test
    public void getOpponentTeamId_shouldReturnSessionOpponentTeamId() {
        // Arrange
        when(mockSession.getGegnerTeamId()).thenReturn(200L);
        
        // Act
        long result = stateContext.getOpponentTeamId();
        
        // Assert
        assertThat(result).isEqualTo(200L);
    }

    @Test
    public void getCurrentMatchId_shouldReturnSessionCurrentMatchId() {
        // Arrange
        when(mockSession.getCurrentMatchId()).thenReturn(300L);
        
        // Act
        long result = stateContext.getCurrentMatchId();
        
        // Assert
        assertThat(result).isEqualTo(300L);
    }

    @Test
    public void getCurrentPasseNumber_shouldReturnSessionCurrentPasseNumber() {
        // Arrange
        when(mockSession.getCurrentPasseNumber()).thenReturn(2);
        
        // Act
        int result = stateContext.getCurrentPasseNumber();
        
        // Assert
        assertThat(result).isEqualTo(2);
    }

    @Test
    public void getWettkampfId_shouldReturnSessionWettkampfId() {
        // Arrange
        when(mockSession.getWettkampfId()).thenReturn(50L);
        
        // Act
        long result = stateContext.getWettkampfId();
        
        // Assert
        assertThat(result).isEqualTo(50L);
    }

    @Test
    public void getCurrentStatus_shouldReturnSessionStatus() {
        // Arrange
        when(mockSession.getStatus()).thenReturn("WARTE");
        
        // Act
        String result = stateContext.getCurrentStatus();
        
        // Assert
        assertThat(result).isEqualTo("WARTE");
    }

    @Test
    public void updateSessionStatus_shouldDelegateToSessionRuntime() {
        // Act
        stateContext.updateSessionStatus("MATCH_ENDE");
        
        // Assert
        verify(mockSessionRuntime).updateSessionStatus("MATCH_ENDE");
    }

    @Test
    public void updatePasseNumber_shouldDelegateToSessionRuntime() {
        // Act
        stateContext.updatePasseNumber(3);
        
        // Assert
        verify(mockSessionRuntime).updatePasseNumber(3);
    }

    @Test
    public void advanceToNextMatch_shouldDelegateToSessionRuntime() {
        // Arrange
        LigamatchBE nextMatch = new LigamatchBE();
        nextMatch.setMatchId(400L);
        
        // Act
        stateContext.advanceToNextMatch(nextMatch, 200L);
        
        // Assert
        verify(mockSessionRuntime).advanceToNextMatch(nextMatch, 200L);
    }

    @Test
    public void getSessionDAO_shouldReturnSessionRuntimeDAO() {
        // Arrange
        when(mockSessionRuntime.getSessionDAO()).thenReturn(mockSessionDAO);
        
        // Act
        TabletSchusszettelDAO result = stateContext.getSessionDAO();
        
        // Assert
        assertThat(result).isEqualTo(mockSessionDAO);
    }

    @Test
    public void isMatchComplete_shouldDelegateToMatchAnalysisService() {
        // Arrange
        when(mockSession.getCurrentMatchId()).thenReturn(300L);
        when(mockSession.getTeamId()).thenReturn(100L);
        when(mockSession.getGegnerTeamId()).thenReturn(200L);
        when(mockMatchAnalysisService.isMatchComplete(300L, 100L, 200L)).thenReturn(true);
        
        // Act
        boolean result = stateContext.isMatchComplete();
        
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
        boolean result = stateContext.isMatchComplete();
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void hasMoreMatches_shouldUseMatchAnalysisService() {
        // Arrange
        when(mockSession.getCurrentMatchId()).thenReturn(300L);
        when(mockSession.getTeamId()).thenReturn(100L);
        
        LigamatchBE nextMatch = new LigamatchBE();
        nextMatch.setMatchId(400L);
        when(mockMatchAnalysisService.findCorrectNextMatch(300L, 100L)).thenReturn(nextMatch);
        
        // Act
        boolean result = stateContext.hasMoreMatches();
        
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
        boolean result = stateContext.hasMoreMatches();
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void getNextMatch_shouldUseMatchAnalysisService() {
        // Arrange
        when(mockSession.getCurrentMatchId()).thenReturn(300L);
        when(mockSession.getTeamId()).thenReturn(100L);
        
        LigamatchBE nextMatch = new LigamatchBE();
        nextMatch.setMatchId(400L);
        when(mockMatchAnalysisService.findCorrectNextMatch(300L, 100L)).thenReturn(nextMatch);
        
        // Act
        LigamatchBE result = stateContext.getNextMatch();
        
        // Assert
        assertThat(result).isEqualTo(nextMatch);
    }

    @Test
    public void findOpponentTeamId_shouldUseMatchAnalysisService() {
        // Arrange
        when(mockSession.getTeamId()).thenReturn(100L);
        when(mockMatchAnalysisService.findOpponentTeamId(300L, 100L)).thenReturn(200L);
        
        // Act
        long result = stateContext.findOpponentTeamId(300L);
        
        // Assert
        assertThat(result).isEqualTo(200L);
    }

    @Test
    public void getOpponentMatchId_shouldReturnOpponentMatchId() {
        // Arrange
        when(mockSession.getCurrentMatchId()).thenReturn(300L);
        
        LigamatchBE currentMatch = new LigamatchBE();
        currentMatch.setMatchId(300L);
        currentMatch.setMatchIdGegner(301L);
        
        when(mockMatchComponent.getLigamatchById(300L)).thenReturn(currentMatch);
        
        // Act
        long result = stateContext.getOpponentMatchId();
        
        // Assert
        assertThat(result).isEqualTo(301L);
    }

    @Test
    public void getOpponentMatchId_withNullOpponentMatchId_shouldReturnZero() {
        // Arrange
        when(mockSession.getCurrentMatchId()).thenReturn(300L);
        
        LigamatchBE currentMatch = new LigamatchBE();
        currentMatch.setMatchId(300L);
        currentMatch.setMatchIdGegner(null);
        
        when(mockMatchComponent.getLigamatchById(300L)).thenReturn(currentMatch);
        
        // Act
        long result = stateContext.getOpponentMatchId();
        
        // Assert
        assertThat(result).isEqualTo(0L);
    }

    @Test
    public void getTeamMembers_shouldReturnTeamMembers() {
        // Arrange
        when(mockSession.getTeamId()).thenReturn(100L);
        
        List<MannschaftsmitgliedDO> members = Arrays.asList(
            createMannschaftsmitglied(1L, 1, 1),
            createMannschaftsmitglied(2L, 2, 1)
        );
        
        when(mockMannschaftsmitgliedComponent.findByTeamId(100L)).thenReturn(members);
        
        // Act
        List<MannschaftsmitgliedDO> result = stateContext.getTeamMembers();
        
        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
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
            createPasseDO(3L, 2L)
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

    @Test
    public void loadOpponentSession_shouldDelegateToSessionRuntime() {
        // Arrange
        when(mockSession.getGegnerTeamId()).thenReturn(200L);
        
        TabletSchusszettelEntity opponentSession = new TabletSchusszettelEntity();
        when(mockSessionRuntime.loadOpponentSessionByTeamId(200L)).thenReturn(opponentSession);
        
        // Act
        TabletSchusszettelEntity result = stateContext.loadOpponentSession();
        
        // Assert
        assertThat(result).isEqualTo(opponentSession);
        verify(mockSessionRuntime).loadOpponentSessionByTeamId(200L);
    }

    @Test
    public void loadOpponentSession_withNullOpponentId_shouldReturnNull() {
        // Arrange
        when(mockSession.getGegnerTeamId()).thenReturn(null);
        
        // Act
        TabletSchusszettelEntity result = stateContext.loadOpponentSession();
        
        // Assert
        assertThat(result).isNull();
    }

    @Test
    public void validateArrowValue_withValidValue_shouldReturnValid() {
        // Act
        StateContext.ValidationResult result = stateContext.validateArrowValue(8);
        
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

    @Test
    public void buildWettkampfInfo_shouldReturnWettkampfInfo() {
        // Arrange
        when(mockSession.getWettkampfId()).thenReturn(50L);
        
        WettkampfDO wettkampf = new WettkampfDO();
        wettkampf.setId(50L);
        wettkampf.setWettkampfTag(1L);
        wettkampf.setWettkampfDatum(Date.valueOf(LocalDate.now()));
        wettkampf.setWettkampfBeginn(LocalTime.now().toString());
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

    private MannschaftsmitgliedDO createMannschaftsmitglied(Long id, int rueckennummer, int eingesetzt) {
        MannschaftsmitgliedDO member = new MannschaftsmitgliedDO(
            id,  // id
            100L,  // mannschaftId
            id * 10,  // dsbMitgliedId
            eingesetzt,  // dsbMitgliedEingesetzt
            "Vorname" + id,  // dsbMitgliedVorname
            "Nachname" + id,  // dsbMitgliedNachname
            (long) rueckennummer  // rueckennummer
        );
        return member;
    }

    private PasseDO createPasseDO(Long id, Long passeLfdnr) {
        PasseDO passe = new PasseDO();
        passe.setId(id);
        passe.setPasseLfdnr(passeLfdnr);
        return passe;
    }
}