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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test class for StateContext controlled data access object.
 * Tests context creation, data delegation, and service access methods.
 */
@RunWith(MockitoJUnitRunner.class)
public class StateContextTest {

    @Mock private TabletSchusszettelEntity mockSession;
    @Mock private SessionRuntime mockSessionRuntime;
    @Mock private MatchComponent mockMatchComponent;
    @Mock private PasseComponent mockPasseComponent;
    @Mock private MatchAnalysisService mockMatchAnalysisService;
    @Mock private MannschaftsmitgliedComponent mockMannschaftsmitgliedComponent;
    @Mock private DsbMitgliedComponent mockDsbMitgliedComponent;
    @Mock private WettkampfComponent mockWettkampfComponent;
    @Mock private VeranstaltungComponent mockVeranstaltungComponent;
    @Mock private TabletSchusszettelDAO mockSessionDAO;
    
    private StateContext stateContext;
    private WettkampfDO testWettkampf;
    private VeranstaltungDO testVeranstaltung;
    private List<MannschaftsmitgliedDO> testMembers;
    private List<PasseDO> testPasses;

    @Before
    public void setUp() {
        setupTestData();
        setupMockBehavior();
        
        stateContext = new StateContext(
            mockSession, mockSessionRuntime, mockMatchComponent, mockPasseComponent,
            mockMatchAnalysisService, mockMannschaftsmitgliedComponent, mockDsbMitgliedComponent,
            mockWettkampfComponent, mockVeranstaltungComponent
        );
    }
    
    private void setupTestData() {
        testWettkampf = createMockWettkampf();
        testVeranstaltung = createMockVeranstaltung();
        testMembers = Arrays.asList(
            createMockMember(1L, 1, 1),
            createMockMember(2L, 2, 1),
            createMockMember(3L, 3, 0) // Not deployed
        );
        testPasses = Arrays.asList(
            createMockPasse(1L, 2L, 10L),
            createMockPasse(2L, 2L, 20L),
            createMockPasse(3L, 1L, 30L)
        );
    }
    
    private void setupMockBehavior() {
        when(mockSession.getTeamId()).thenReturn(100L);
        when(mockSession.getGegnerTeamId()).thenReturn(200L);
        when(mockSession.getCurrentMatchId()).thenReturn(300L);
        when(mockSession.getCurrentPasseNumber()).thenReturn(2);
        when(mockSession.getWettkampfId()).thenReturn(50L);
        when(mockSession.getStatus()).thenReturn("WARTE");
        when(mockSessionRuntime.getSessionDAO()).thenReturn(mockSessionDAO);
        
        // Setup default component behavior
        when(mockMannschaftsmitgliedComponent.findByTeamId(100L)).thenReturn(testMembers);
        when(mockPasseComponent.findByMannschaftMatchId(100L, 300L)).thenReturn(testPasses);
        when(mockWettkampfComponent.findById(50L)).thenReturn(testWettkampf);
        when(mockVeranstaltungComponent.findById(10L)).thenReturn(testVeranstaltung);
    }

    // === BASIC GETTER TESTS ===
    
    @Test
    public void getTeamId_returnsSessionTeamId() {
        long result = stateContext.getTeamId();
        assertThat(result).isEqualTo(100L);
    }
    
    @Test
    public void getOpponentTeamId_returnsSessionOpponentId() {
        long result = stateContext.getOpponentTeamId();
        assertThat(result).isEqualTo(200L);
    }
    
    @Test
    public void getCurrentMatchId_returnsSessionMatchId() {
        long result = stateContext.getCurrentMatchId();
        assertThat(result).isEqualTo(300L);
    }
    
    @Test
    public void getCurrentPasseNumber_returnsSessionPasseNumber() {
        int result = stateContext.getCurrentPasseNumber();
        assertThat(result).isEqualTo(2);
    }
    
    @Test
    public void getWettkampfId_returnsSessionWettkampfId() {
        long result = stateContext.getWettkampfId();
        assertThat(result).isEqualTo(50L);
    }
    
    @Test
    public void getCurrentStatus_returnsSessionStatus() {
        String result = stateContext.getCurrentStatus();
        assertThat(result).isEqualTo("WARTE");
    }
    
    // === SERVICE ACCESS TESTS ===
    
    @Test
    public void getMatchAnalysisService_returnsInjectedService() {
        MatchAnalysisService result = stateContext.getMatchAnalysisService();
        assertThat(result).isEqualTo(mockMatchAnalysisService);
    }
    
    @Test
    public void getMatchComponent_returnsInjectedComponent() {
        MatchComponent result = stateContext.getMatchComponent();
        assertThat(result).isEqualTo(mockMatchComponent);
    }
    
    @Test
    public void getPasseComponent_returnsInjectedComponent() {
        PasseComponent result = stateContext.getPasseComponent();
        assertThat(result).isEqualTo(mockPasseComponent);
    }
    
    @Test
    public void getMannschaftsmitgliedComponent_returnsInjectedComponent() {
        MannschaftsmitgliedComponent result = stateContext.getMannschaftsmitgliedComponent();
        assertThat(result).isEqualTo(mockMannschaftsmitgliedComponent);
    }
    
    @Test
    public void getDsbMitgliedComponent_returnsInjectedComponent() {
        DsbMitgliedComponent result = stateContext.getDsbMitgliedComponent();
        assertThat(result).isEqualTo(mockDsbMitgliedComponent);
    }
    
    @Test
    public void getWettkampfComponent_returnsInjectedComponent() {
        WettkampfComponent result = stateContext.getWettkampfComponent();
        assertThat(result).isEqualTo(mockWettkampfComponent);
    }
    
    @Test
    public void getVeranstaltungComponent_returnsInjectedComponent() {
        VeranstaltungComponent result = stateContext.getVeranstaltungComponent();
        assertThat(result).isEqualTo(mockVeranstaltungComponent);
    }
    
    @Test
    public void getSessionDAO_returnsSessionRuntimeDAO() {
        TabletSchusszettelDAO result = stateContext.getSessionDAO();
        assertThat(result).isEqualTo(mockSessionDAO);
    }
    
    // === DELEGATION TESTS ===
    
    @Test
    public void updateSessionStatus_delegatesToSessionRuntime() {
        stateContext.updateSessionStatus("MATCH_ENDE");
        verify(mockSessionRuntime).updateSessionStatus("MATCH_ENDE");
    }
    
    @Test
    public void updatePasseNumber_delegatesToSessionRuntime() {
        stateContext.updatePasseNumber(3);
        verify(mockSessionRuntime).updatePasseNumber(3);
    }
    
    @Test
    public void advanceToNextMatch_delegatesToSessionRuntime() {
        LigamatchBE nextMatch = new LigamatchBE();
        stateContext.advanceToNextMatch(nextMatch, 200L);
        verify(mockSessionRuntime).advanceToNextMatch(nextMatch, 200L);
    }
    
    // === MATCH ANALYSIS TESTS ===
    
    @Test
    public void isMatchComplete_delegatesToMatchAnalysisService() {
        when(mockMatchAnalysisService.isMatchComplete(300L, 100L, 200L)).thenReturn(true);
        
        boolean result = stateContext.isMatchComplete();
        
        assertThat(result).isTrue();
        verify(mockMatchAnalysisService).isMatchComplete(300L, 100L, 200L);
    }
    
    @Test
    public void isMatchComplete_exceptionInService_returnsFalse() {
        when(mockMatchAnalysisService.isMatchComplete(300L, 100L, 200L))
            .thenThrow(new RuntimeException("Service error"));
        
        boolean result = stateContext.isMatchComplete();
        
        assertThat(result).isFalse();
    }
    
    @Test
    public void hasMoreMatches_nextMatchExists_returnsTrue() {
        LigamatchBE nextMatch = new LigamatchBE();
        when(mockMatchAnalysisService.findCorrectNextMatch(300L, 100L)).thenReturn(nextMatch);
        
        boolean result = stateContext.hasMoreMatches();
        
        assertThat(result).isTrue();
    }
    
    @Test
    public void hasMoreMatches_noNextMatch_returnsFalse() {
        when(mockMatchAnalysisService.findCorrectNextMatch(300L, 100L)).thenReturn(null);
        
        boolean result = stateContext.hasMoreMatches();
        
        assertThat(result).isFalse();
    }
    
    @Test
    public void hasMoreMatches_exceptionInService_returnsFalse() {
        when(mockMatchAnalysisService.findCorrectNextMatch(300L, 100L))
            .thenThrow(new RuntimeException("Service error"));
        
        boolean result = stateContext.hasMoreMatches();
        
        assertThat(result).isFalse();
    }
    
    @Test
    public void getNextMatch_returnsFromMatchAnalysisService() {
        LigamatchBE nextMatch = new LigamatchBE();
        nextMatch.setMatchId(400L);
        when(mockMatchAnalysisService.findCorrectNextMatch(300L, 100L)).thenReturn(nextMatch);
        
        LigamatchBE result = stateContext.getNextMatch();
        
        assertThat(result).isEqualTo(nextMatch);
        assertThat(result.getMatchId()).isEqualTo(400L);
    }
    
    @Test
    public void getNextMatch_exceptionInService_returnsNull() {
        when(mockMatchAnalysisService.findCorrectNextMatch(300L, 100L))
            .thenThrow(new RuntimeException("Service error"));
        
        LigamatchBE result = stateContext.getNextMatch();
        
        assertThat(result).isNull();
    }
    
    @Test
    public void findOpponentTeamId_returnsFromMatchAnalysisService() {
        when(mockMatchAnalysisService.findOpponentTeamId(400L, 100L)).thenReturn(250L);
        
        long result = stateContext.findOpponentTeamId(400L);
        
        assertThat(result).isEqualTo(250L);
    }
    
    @Test
    public void findOpponentTeamId_exceptionInService_returnsZero() {
        when(mockMatchAnalysisService.findOpponentTeamId(400L, 100L))
            .thenThrow(new RuntimeException("Service error"));
        
        long result = stateContext.findOpponentTeamId(400L);
        
        assertThat(result).isEqualTo(0L);
    }
    
    @Test
    public void getOpponentMatchId_returnsFromCurrentMatch() {
        LigamatchBE currentMatch = new LigamatchBE();
        currentMatch.setMatchIdGegner(301L);
        when(mockMatchComponent.getLigamatchById(300L)).thenReturn(currentMatch);
        
        long result = stateContext.getOpponentMatchId();
        
        assertThat(result).isEqualTo(301L);
    }
    
    @Test
    public void getOpponentMatchId_noOpponentMatchId_returnsZero() {
        LigamatchBE currentMatch = new LigamatchBE();
        currentMatch.setMatchIdGegner(null);
        when(mockMatchComponent.getLigamatchById(300L)).thenReturn(currentMatch);
        
        long result = stateContext.getOpponentMatchId();
        
        assertThat(result).isEqualTo(0L);
    }
    
    @Test
    public void getOpponentMatchId_noCurrentMatch_returnsZero() {
        when(mockMatchComponent.getLigamatchById(300L)).thenReturn(null);
        
        long result = stateContext.getOpponentMatchId();
        
        assertThat(result).isEqualTo(0L);
    }
    
    @Test
    public void getOpponentMatchId_exceptionInService_returnsZero() {
        when(mockMatchComponent.getLigamatchById(300L))
            .thenThrow(new RuntimeException("Service error"));
        
        long result = stateContext.getOpponentMatchId();
        
        assertThat(result).isEqualTo(0L);
    }
    
    // === TEAM DATA TESTS ===
    
    @Test
    public void getTeamMembers_returnsFromComponent() {
        List<MannschaftsmitgliedDO> result = stateContext.getTeamMembers();
        
        assertThat(result).isEqualTo(testMembers);
        verify(mockMannschaftsmitgliedComponent).findByTeamId(100L);
    }
    
    @Test
    public void getDeployedTeamMembers_filtersDeployedMembers() {
        List<MannschaftsmitgliedDO> result = stateContext.getDeployedTeamMembers();
        
        assertThat(result).hasSize(2); // Only members 1 and 2 are deployed
        assertThat(result.stream().map(MannschaftsmitgliedDO::getDsbMitgliedId))
            .containsExactly(1L, 2L);
    }
    
    @Test
    public void getDeployedTeamMembers_removeDuplicates_keepsFirst() {
        // Add duplicate member
        List<MannschaftsmitgliedDO> membersWithDuplicate = new ArrayList<>(testMembers);
        membersWithDuplicate.add(createMockMember(1L, 4, 1)); // Same ID, different rueckennummer
        when(mockMannschaftsmitgliedComponent.findByTeamId(100L)).thenReturn(membersWithDuplicate);
        
        List<MannschaftsmitgliedDO> result = stateContext.getDeployedTeamMembers();
        
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getRueckennummer()).isEqualTo(1L); // First occurrence kept
    }
    
    @Test
    public void isShooterDeployed_deployedShooter_returnsTrue() {
        boolean result = stateContext.isShooterDeployed(1L);
        
        assertThat(result).isTrue();
    }
    
    @Test
    public void isShooterDeployed_notDeployedShooter_returnsFalse() {
        boolean result = stateContext.isShooterDeployed(3L);
        
        assertThat(result).isFalse();
    }
    
    @Test
    public void isShooterDeployed_nonexistentShooter_returnsFalse() {
        boolean result = stateContext.isShooterDeployed(999L);
        
        assertThat(result).isFalse();
    }
    
    // === PASSE DATA TESTS ===
    
    @Test
    public void getCurrentPasseData_returnsFilteredPasses() {
        List<PasseDO> result = stateContext.getCurrentPasseData();
        
        assertThat(result).hasSize(2); // Passes with passeLfdnr = 2
        assertThat(result.stream().map(PasseDO::getPasseDsbMitgliedId))
            .containsExactly(10L, 20L);
    }
    
    @Test
    public void getCurrentPasseData_exceptionInService_returnsEmptyList() {
        when(mockPasseComponent.findByMannschaftMatchId(100L, 300L))
            .thenThrow(new RuntimeException("Service error"));
        
        List<PasseDO> result = stateContext.getCurrentPasseData();
        
        assertThat(result).isEmpty();
    }
    
    @Test
    public void getAllMatchPasses_returnsAllPasses() {
        List<PasseDO> result = stateContext.getAllMatchPasses();
        
        assertThat(result).isEqualTo(testPasses);
        verify(mockPasseComponent).findByMannschaftMatchId(100L, 300L);
    }
    
    @Test
    public void getAllMatchPasses_exceptionInService_returnsEmptyList() {
        when(mockPasseComponent.findByMannschaftMatchId(100L, 300L))
            .thenThrow(new RuntimeException("Service error"));
        
        List<PasseDO> result = stateContext.getAllMatchPasses();
        
        assertThat(result).isEmpty();
    }
    
    @Test
    public void isCurrentPasseComplete_threeShooters_returnsTrue() {
        // Add third pass to make it complete (3 passes for passe number 2)
        List<PasseDO> completePasses = Arrays.asList(
            createMockPasse(1L, 2L, 1L),
            createMockPasse(2L, 2L, 2L),
            createMockPasse(3L, 2L, 3L) // Third pass for complete set
        );
        when(mockPasseComponent.findByMannschaftMatchId(100L, 300L)).thenReturn(completePasses);
        
        boolean result = stateContext.isCurrentPasseComplete();
        
        assertThat(result).isTrue(); // Has 3 passes for passe number 2, = 3 required
    }
    
    @Test
    public void isCurrentPasseComplete_lessThanThreeShooters_returnsFalse() {
        // Only one pass for current passe number
        List<PasseDO> singlePass = Arrays.asList(createMockPasse(1L, 2L, 10L));
        when(mockPasseComponent.findByMannschaftMatchId(100L, 300L)).thenReturn(singlePass);
        
        boolean result = stateContext.isCurrentPasseComplete();
        
        assertThat(result).isFalse();
    }
    
    @Test
    public void isCurrentPasseComplete_exceptionInService_returnsFalse() {
        when(mockPasseComponent.findByMannschaftMatchId(100L, 300L))
            .thenThrow(new RuntimeException("Service error"));
        
        boolean result = stateContext.isCurrentPasseComplete();
        
        assertThat(result).isFalse();
    }
    
    @Test
    public void isShooterRegistered_registeredShooter_returnsTrue() {
        when(mockPasseComponent.findByMannschaftMatchId(100L, 300L))
            .thenReturn(Arrays.asList(createMockPasse(1L, 2L, 10L)));
        
        boolean result = stateContext.isShooterRegistered(10L, 2);
        
        assertThat(result).isTrue();
    }
    
    @Test
    public void isShooterRegistered_notRegistered_returnsFalse() {
        boolean result = stateContext.isShooterRegistered(999L, 2);
        
        assertThat(result).isFalse();
    }
    
    @Test
    public void isShooterRegistered_exceptionInService_returnsFalse() {
        when(mockPasseComponent.findByMannschaftMatchId(100L, 300L))
            .thenThrow(new RuntimeException("Service error"));
        
        boolean result = stateContext.isShooterRegistered(10L, 2);
        
        assertThat(result).isFalse();
    }
    
    // === OPPONENT SESSION TESTS ===
    
    @Test
    public void loadOpponentSession_validOpponentId_returnsSession() {
        TabletSchusszettelEntity opponentSession = new TabletSchusszettelEntity();
        when(mockSessionRuntime.loadOpponentSessionByTeamId(200L)).thenReturn(opponentSession);
        
        TabletSchusszettelEntity result = stateContext.loadOpponentSession();
        
        assertThat(result).isEqualTo(opponentSession);
        verify(mockSessionRuntime).loadOpponentSessionByTeamId(200L);
    }
    
    @Test
    public void loadOpponentSession_nullOpponentId_returnsNull() {
        when(mockSession.getGegnerTeamId()).thenReturn(null);
        
        TabletSchusszettelEntity result = stateContext.loadOpponentSession();
        
        assertThat(result).isNull();
        verify(mockSessionRuntime, never()).loadOpponentSessionByTeamId(anyLong());
    }
    
    @Test
    public void loadOpponentSession_zeroOpponentId_returnsNull() {
        when(mockSession.getGegnerTeamId()).thenReturn(0L);
        
        TabletSchusszettelEntity result = stateContext.loadOpponentSession();
        
        assertThat(result).isNull();
        verify(mockSessionRuntime, never()).loadOpponentSessionByTeamId(anyLong());
    }
    
    // === VALIDATION TESTS ===
    
    @Test
    public void validationResult_valid_createsValidResult() {
        StateContext.ValidationResult result = StateContext.ValidationResult.valid();
        
        assertThat(result.isValid()).isTrue();
        assertThat(result.getErrorMessage()).isNull();
    }
    
    @Test
    public void validationResult_invalid_createsInvalidResult() {
        StateContext.ValidationResult result = StateContext.ValidationResult.invalid("test error");
        
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrorMessage()).isEqualTo("test error");
    }
    
    @Test
    public void validateArrowValue_validValue_returnsValid() {
        StateContext.ValidationResult result = stateContext.validateArrowValue(5);
        
        assertThat(result.isValid()).isTrue();
    }
    
    @Test
    public void validateArrowValue_nullValue_returnsInvalid() {
        StateContext.ValidationResult result = stateContext.validateArrowValue(null);
        
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrorMessage()).contains("cannot be null");
    }
    
    @Test
    public void validateArrowValue_negativeValue_returnsInvalid() {
        StateContext.ValidationResult result = stateContext.validateArrowValue(-1);
        
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrorMessage()).contains("cannot be negative");
    }
    
    @Test
    public void validateArrowValue_valueAboveTen_returnsInvalid() {
        StateContext.ValidationResult result = stateContext.validateArrowValue(11);
        
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrorMessage()).contains("cannot exceed 10");
    }
    
    @Test
    public void validateArrowValue_boundaryValues_returnsCorrectly() {
        assertThat(stateContext.validateArrowValue(0).isValid()).isTrue();
        assertThat(stateContext.validateArrowValue(10).isValid()).isTrue();
    }
    
    @Test
    public void validateSessionState_validSession_returnsValid() {
        StateContext.ValidationResult result = stateContext.validateSessionState();
        
        assertThat(result.isValid()).isTrue();
    }
    
    @Test
    public void validateSessionState_invalidTeamId_returnsInvalid() {
        when(mockSession.getTeamId()).thenReturn(0L);
        
        StateContext.ValidationResult result = stateContext.validateSessionState();
        
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrorMessage()).contains("Invalid team ID");
    }
    
    @Test
    public void validateSessionState_nullTeamId_returnsInvalid() {
        when(mockSession.getTeamId()).thenReturn(null);
        
        StateContext.ValidationResult result = stateContext.validateSessionState();
        
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrorMessage()).contains("Invalid team ID");
    }
    
    @Test
    public void validateSessionState_invalidWettkampfId_returnsInvalid() {
        when(mockSession.getWettkampfId()).thenReturn(-1L);
        
        StateContext.ValidationResult result = stateContext.validateSessionState();
        
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrorMessage()).contains("Invalid wettkampf ID");
    }
    
    @Test
    public void validateSessionState_invalidMatchId_returnsInvalid() {
        when(mockSession.getCurrentMatchId()).thenReturn(0L);
        
        StateContext.ValidationResult result = stateContext.validateSessionState();
        
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrorMessage()).contains("Invalid current match ID");
    }
    
    @Test
    public void validateSessionState_invalidPasseNumber_returnsInvalid() {
        when(mockSession.getCurrentPasseNumber()).thenReturn(6); // > MAX_SETS_PER_MATCH
        
        StateContext.ValidationResult result = stateContext.validateSessionState();
        
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrorMessage()).contains("Invalid passe number");
    }
    
    @Test
    public void validateSessionState_nullStatus_returnsInvalid() {
        when(mockSession.getStatus()).thenReturn(null);
        
        StateContext.ValidationResult result = stateContext.validateSessionState();
        
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrorMessage()).contains("status cannot be null");
    }
    
    @Test
    public void validateSessionState_emptyStatus_returnsInvalid() {
        when(mockSession.getStatus()).thenReturn("   ");
        
        StateContext.ValidationResult result = stateContext.validateSessionState();
        
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrorMessage()).contains("status cannot be null or empty");
    }
    
    // === WETTKAMPF INFO TESTS ===
    
    @Test
    public void buildWettkampfInfo_validData_returnsInfo() {
        WettkampfInfoDO result = stateContext.buildWettkampfInfo();
        
        assertThat(result).isNotNull();
        assertThat(result.getWettkampfId()).isEqualTo(50L);
        assertThat(result.getVeranstaltungName()).isEqualTo("Test Veranstaltung");
        verify(mockWettkampfComponent).findById(50L);
        verify(mockVeranstaltungComponent).findById(10L);
    }
    
    @Test
    public void buildWettkampfInfo_exceptionInWettkampfComponent_returnsNull() {
        when(mockWettkampfComponent.findById(50L))
            .thenThrow(new RuntimeException("Service error"));
        
        WettkampfInfoDO result = stateContext.buildWettkampfInfo();
        
        assertThat(result).isNull();
    }
    
    @Test
    public void buildWettkampfInfo_exceptionInVeranstaltungComponent_returnsNull() {
        when(mockVeranstaltungComponent.findById(10L))
            .thenThrow(new RuntimeException("Service error"));
        
        WettkampfInfoDO result = stateContext.buildWettkampfInfo();
        
        assertThat(result).isNull();
    }

    private MannschaftsmitgliedDO createMockMember(Long id, int rueckennummer, int eingesetzt) {
        return new MannschaftsmitgliedDO(id, 100L, id, eingesetzt, "Vorname" + id, "Nachname" + id, (long) rueckennummer);
    }

    private PasseDO createMockPasse(Long id, Long passeLfdnr, Long mitgliedId) {
        PasseDO passe = new PasseDO();
        passe.setId(id);
        passe.setPasseLfdnr(passeLfdnr);
        passe.setPasseDsbMitgliedId(mitgliedId);
        return passe;
    }

    private WettkampfDO createMockWettkampf() {
        WettkampfDO wettkampf = new WettkampfDO();
        wettkampf.setId(50L);
        wettkampf.setWettkampfTag(1L);
        wettkampf.setWettkampfDatum(Date.valueOf(LocalDate.now()));
        wettkampf.setWettkampfBeginn(LocalTime.now().toString());
        wettkampf.setWettkampfOrtsname("Test Ort");
        wettkampf.setWettkampfVeranstaltungsId(10L);
        return wettkampf;
    }

    private VeranstaltungDO createMockVeranstaltung() {
        VeranstaltungDO veranstaltung = new VeranstaltungDO();
        veranstaltung.setVeranstaltungID(10L);
        veranstaltung.setVeranstaltungName("Test Veranstaltung");
        veranstaltung.setVeranstaltungSportJahr(2023L);
        return veranstaltung;
    }
}