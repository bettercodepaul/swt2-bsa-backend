package de.bogenliga.application.business.schusszettel.impl.business;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.schusszettel.impl.dao.TabletSchusszettelDAO;
import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

/**
 * Test class for TabletSchusszettelAdminComponentImpl
 * Ensures comprehensive coverage of admin-specific functionality
 */
public class TabletSchusszettelAdminComponentImplTest {

    private static final Long WETTKAMPF_ID = 1L;
    private static final Long TEAM1_ID = 10L;
    private static final Long TEAM2_ID = 20L;
    private static final Long MATCH_ID = 100L;
    private static final Long MATCH2_ID = 200L;

    @Mock
    private TabletSchusszettelDAO sessionDAO;
    @Mock
    private MatchComponent matchComponent;
    @Mock
    private DsbMannschaftComponent mannschaftComponent;
    @Mock
    private VereinComponent vereinComponent;
    @Mock
    private PasseComponent passeComponent;
    @Mock
    private TabletSchusszettelSyncComponent syncComponent;
    @Mock
    private WettkampfComponent wettkampfComponent;
    @Mock
    private VeranstaltungComponent veranstaltungComponent;

    private TabletSchusszettelAdminComponentImpl underTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new TabletSchusszettelAdminComponentImpl(
                sessionDAO, matchComponent, mannschaftComponent, vereinComponent,
                passeComponent, syncComponent, wettkampfComponent, veranstaltungComponent);
    }

    @Test
    public void testInitializeForWettkampf_Success() {
        // Arrange
        List<MatchDO> matches = Arrays.asList(
                createMatch(MATCH_ID, 1L, TEAM1_ID, 1L),
                createMatch(MATCH_ID + 1, 1L, TEAM2_ID, 1L)
        );
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID)).thenReturn(matches);

        TabletSchusszettelSyncComponent.SyncResult syncResult = 
                TabletSchusszettelSyncComponent.SyncResult.success("Sync OK", false);
        when(syncComponent.findCurrentMatchForTeam(anyList(), anyLong())).thenReturn(matches.get(0));
        // Removed deprecated determineCorrectPasseNumber call
        when(passeComponent.findByMannschaftMatchId(anyLong(), anyLong())).thenReturn(Collections.emptyList());

        // Act
        underTest.initializeForWettkampf(WETTKAMPF_ID);

        // Assert
        verify(sessionDAO).deleteByWettkampfId(WETTKAMPF_ID);
        verify(sessionDAO, times(2)).createSession(any(TabletSchusszettelEntity.class), eq(-1L));
    }

    @Test(expected = BusinessException.class)
    public void testInitializeForWettkampf_NoMatches() {
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID)).thenReturn(Collections.emptyList());
        underTest.initializeForWettkampf(WETTKAMPF_ID);
    }

    @Test(expected = BusinessException.class)
    public void testInitializeForWettkampf_NoOpponentFound() {
        // Arrange - single match without opponent
        List<MatchDO> matches = Collections.singletonList(
                createMatch(MATCH_ID, 1L, TEAM1_ID, 1L)
        );
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID)).thenReturn(matches);

        // Act
        underTest.initializeForWettkampf(WETTKAMPF_ID);
    }

    @Test(expected = TechnicalException.class)
    public void testInitializeForWettkampf_TechnicalError() {
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID))
                .thenThrow(new RuntimeException("DB error"));
        
        underTest.initializeForWettkampf(WETTKAMPF_ID);
    }

    @Test
    public void testDeleteForWettkampf_Success() {
        // Act
        underTest.deleteForWettkampf(WETTKAMPF_ID);

        // Assert
        verify(sessionDAO).deleteByWettkampfId(WETTKAMPF_ID);
    }

    @Test(expected = TechnicalException.class)
    public void testDeleteForWettkampf_TechnicalError() {
        doThrow(new RuntimeException("DB error"))
                .when(sessionDAO).deleteByWettkampfId(WETTKAMPF_ID);
        
        underTest.deleteForWettkampf(WETTKAMPF_ID);
    }

    @Test
    public void testExistsForWettkampf_True() {
        when(sessionDAO.existsByWettkampfId(WETTKAMPF_ID)).thenReturn(true);
        
        boolean result = underTest.existsForWettkampf(WETTKAMPF_ID);
        
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void testExistsForWettkampf_False() {
        when(sessionDAO.existsByWettkampfId(WETTKAMPF_ID)).thenReturn(false);
        
        boolean result = underTest.existsForWettkampf(WETTKAMPF_ID);
        
        Assertions.assertThat(result).isFalse();
    }

    @Test(expected = TechnicalException.class)
    public void testExistsForWettkampf_TechnicalError() {
        when(sessionDAO.existsByWettkampfId(WETTKAMPF_ID))
                .thenThrow(new RuntimeException("DB error"));
        
        underTest.existsForWettkampf(WETTKAMPF_ID);
    }

    @Test
    public void testReTokenize_Success() {
        // Arrange
        TabletSchusszettelEntity session = new TabletSchusszettelEntity();
        session.setWettkampfId(WETTKAMPF_ID);
        session.setTeamId(TEAM1_ID);
        session.setToken("old-token");
        
        when(sessionDAO.findByWettkampfUndTeam(WETTKAMPF_ID, TEAM1_ID))
                .thenReturn(Optional.of(session));

        // Act
        underTest.reTokenize(WETTKAMPF_ID, TEAM1_ID);

        // Assert
        verify(sessionDAO).setToken(eq(WETTKAMPF_ID), eq(TEAM1_ID), anyString(), eq(-1L));
    }

    @Test(expected = BusinessException.class)
    public void testReTokenize_SessionNotFound() {
        when(sessionDAO.findByWettkampfUndTeam(WETTKAMPF_ID, TEAM1_ID))
                .thenReturn(Optional.empty());
        
        underTest.reTokenize(WETTKAMPF_ID, TEAM1_ID);
    }

    @Test(expected = TechnicalException.class)
    public void testReTokenize_TechnicalError() {
        when(sessionDAO.findByWettkampfUndTeam(WETTKAMPF_ID, TEAM1_ID))
                .thenThrow(new RuntimeException("DB error"));
        
        underTest.reTokenize(WETTKAMPF_ID, TEAM1_ID);
    }

    @Test
    public void testGenerateSchusszettelSessions_Empty() {
        when(sessionDAO.findByWettkampfId(WETTKAMPF_ID))
                .thenReturn(Collections.emptyList());
        
        var result = underTest.generateSchusszettelSessions(WETTKAMPF_ID);
        
        Assertions.assertThat(result.getTabletSessionSingDOs()).isEmpty();
    }

    @Test
    public void testGenerateSchusszettelSessions_WithSync() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        when(sessionDAO.findByWettkampfId(WETTKAMPF_ID))
                .thenReturn(Collections.singletonList(session));
        
        setupTeamMocks();
        setupWettkampfMocks();
        
        // Mock sync
        TabletSchusszettelSyncComponent.SyncResult syncResult = 
                TabletSchusszettelSyncComponent.SyncResult.success("Synced", true);
        when(syncComponent.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true))
                .thenReturn(syncResult);

        // Act
        var result = underTest.generateSchusszettelSessions(WETTKAMPF_ID);

        // Assert
        Assertions.assertThat(result.getTabletSessionSingDOs()).hasSize(1);
        verify(syncComponent).synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true);
    }

    @Test
    public void testGenerateSchusszettelSessions_SyncFailure() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        when(sessionDAO.findByWettkampfId(WETTKAMPF_ID))
                .thenReturn(Collections.singletonList(session));
        
        setupTeamMocks();
        setupWettkampfMocks();
        
        // Mock sync failure
        TabletSchusszettelSyncComponent.SyncResult syncResult = 
                TabletSchusszettelSyncComponent.SyncResult.failure("Sync failed");
        when(syncComponent.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true))
                .thenReturn(syncResult);

        // Act - should continue despite sync failure
        var result = underTest.generateSchusszettelSessions(WETTKAMPF_ID);

        // Assert
        Assertions.assertThat(result.getTabletSessionSingDOs()).hasSize(1);
    }

    // Helper methods
    
    private MatchDO createMatch(Long id, Long nr, Long teamId, Long begegnung) {
        MatchDO match = new MatchDO();
        match.setId(id);
        match.setNr(nr);
        match.setMannschaftId(teamId);
        match.setBegegnung(begegnung);
        match.setWettkampfId(WETTKAMPF_ID);
        return match;
    }

    private TabletSchusszettelEntity createTestSession() {
        TabletSchusszettelEntity session = new TabletSchusszettelEntity();
        session.setId(1L);
        session.setWettkampfId(WETTKAMPF_ID);
        session.setTeamId(TEAM1_ID);
        session.setCurrentMatchId(MATCH_ID);
        session.setCurrentMatchNumber(1);
        session.setCurrentPasseNumber(1);
        session.setGegnerTeamId(TEAM2_ID);
        session.setStatus("SATZEINGABE");
        session.setToken("test-token");
        return session;
    }

    private void setupTeamMocks() {
        // Team 1
        DsbMannschaftDO team1 = new DsbMannschaftDO();
        team1.setId(TEAM1_ID);
        team1.setVereinId(1L);
        team1.setNummer(1L); // Fix: Add team number to prevent null pointer
        when(mannschaftComponent.findById(TEAM1_ID)).thenReturn(team1);
        
        VereinDO verein1 = new VereinDO();
        verein1.setId(1L);
        verein1.setName("Team 1 Verein");
        when(vereinComponent.findById(1L)).thenReturn(verein1);
        
        // Team 2
        DsbMannschaftDO team2 = new DsbMannschaftDO();
        team2.setId(TEAM2_ID);
        team2.setVereinId(2L);
        team2.setNummer(2L); // Fix: Add team number to prevent null pointer
        when(mannschaftComponent.findById(TEAM2_ID)).thenReturn(team2);
        
        VereinDO verein2 = new VereinDO();
        verein2.setId(2L);
        verein2.setName("Team 2 Verein");
        when(vereinComponent.findById(2L)).thenReturn(verein2);
    }
    
    private void setupWettkampfMocks() {
        // Mock wettkampf
        de.bogenliga.application.business.wettkampf.api.types.WettkampfDO wettkampf = 
            new de.bogenliga.application.business.wettkampf.api.types.WettkampfDO();
        wettkampf.setId(WETTKAMPF_ID);
        wettkampf.setWettkampfVeranstaltungsId(1L);
        wettkampf.setWettkampfTag(1L);
        when(wettkampfComponent.findById(WETTKAMPF_ID)).thenReturn(wettkampf);
        
        // Mock veranstaltung
        de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO veranstaltung = 
            new de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO();
        veranstaltung.setVeranstaltungID(1L);
        veranstaltung.setVeranstaltungName("Test Competition");
        when(veranstaltungComponent.findById(1L)).thenReturn(veranstaltung);
    }

    @Test
    public void testInitializeForWettkampf_OpponentSearchException() {
        // Arrange
        List<MatchDO> matches = Arrays.asList(
                createMatch(MATCH_ID, 1L, TEAM1_ID, 1L)
        );
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID)).thenReturn(matches);
        
        // Mock syncComponent to throw exception when looking for opponent
        when(syncComponent.findCurrentMatchForTeam(anyList(), anyLong()))
                .thenThrow(new BusinessException(
                    de.bogenliga.application.common.errorhandling.ErrorCode.INTERNAL_ERROR, 
                    "Opponent not found"));

        // Act & Assert
        try {
            underTest.initializeForWettkampf(WETTKAMPF_ID);
            Assertions.fail("Expected BusinessException");
        } catch (BusinessException e) {
            Assertions.assertThat(e.getErrorCode())
                    .isEqualTo(de.bogenliga.application.common.errorhandling.ErrorCode.INTERNAL_ERROR);
        }
    }

    @Test
    public void testInitializeForWettkampf_MultipleTeams() {
        // Arrange
        List<MatchDO> matches = Arrays.asList(
                createMatch(MATCH_ID, 1L, TEAM1_ID, 1L),
                createMatch(MATCH_ID + 1, 1L, TEAM2_ID, 1L),
                createMatch(MATCH2_ID, 2L, TEAM1_ID, 1L),
                createMatch(MATCH2_ID + 1, 2L, TEAM2_ID, 1L)
        );
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID)).thenReturn(matches);
        
        // Mock sync component to return valid matches
        when(syncComponent.findCurrentMatchForTeam(anyList(), eq(TEAM1_ID)))
                .thenReturn(matches.get(0));
        when(syncComponent.findCurrentMatchForTeam(anyList(), eq(TEAM2_ID)))
                .thenReturn(matches.get(1));
        
        when(passeComponent.findByMannschaftMatchId(anyLong(), anyLong()))
                .thenReturn(Collections.emptyList());

        // Act
        underTest.initializeForWettkampf(WETTKAMPF_ID);

        // Assert
        verify(sessionDAO).deleteByWettkampfId(WETTKAMPF_ID);
        verify(sessionDAO, times(2)).createSession(any(TabletSchusszettelEntity.class), eq(-1L));
    }

    @Test
    public void testInitializeForWettkampf_SessionCreationFailure() {
        // Arrange
        List<MatchDO> matches = Arrays.asList(
                createMatch(MATCH_ID, 1L, TEAM1_ID, 1L),
                createMatch(MATCH_ID + 1, 1L, TEAM2_ID, 1L)
        );
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID)).thenReturn(matches);
        
        when(syncComponent.findCurrentMatchForTeam(anyList(), anyLong()))
                .thenReturn(matches.get(0));
        when(passeComponent.findByMannschaftMatchId(anyLong(), anyLong()))
                .thenReturn(Collections.emptyList());
        
        // Mock session creation to fail
        doThrow(new RuntimeException("Database error"))
                .when(sessionDAO).createSession(any(TabletSchusszettelEntity.class), eq(-1L));

        // Act & Assert
        try {
            underTest.initializeForWettkampf(WETTKAMPF_ID);
            Assertions.fail("Expected TechnicalException");
        } catch (TechnicalException e) {
            Assertions.assertThat(e.getErrorCode())
                    .isEqualTo(de.bogenliga.application.common.errorhandling.ErrorCode.INTERNAL_ERROR);
        }
    }

    @Test
    public void testGenerateSchusszettelSessions_MultipleTeams() {
        // Arrange
        TabletSchusszettelEntity session1 = createTestSession();
        session1.setTeamId(TEAM1_ID);
        
        TabletSchusszettelEntity session2 = createTestSession();
        session2.setId(2L);
        session2.setTeamId(TEAM2_ID);
        session2.setCurrentMatchId(MATCH_ID + 1);
        
        when(sessionDAO.findByWettkampfId(WETTKAMPF_ID))
                .thenReturn(Arrays.asList(session1, session2));
        
        setupTeamMocks();
        setupWettkampfMocks();
        
        // Mock sync for both teams
        TabletSchusszettelSyncComponent.SyncResult syncResult = 
                TabletSchusszettelSyncComponent.SyncResult.success("Synced", true);
        when(syncComponent.synchronizeSession(session1, WETTKAMPF_ID, TEAM1_ID, true))
                .thenReturn(syncResult);
        when(syncComponent.synchronizeSession(session2, WETTKAMPF_ID, TEAM2_ID, true))
                .thenReturn(syncResult);

        // Act
        var result = underTest.generateSchusszettelSessions(WETTKAMPF_ID);

        // Assert
        Assertions.assertThat(result.getTabletSessionSingDOs()).hasSize(2);
        verify(syncComponent).synchronizeSession(session1, WETTKAMPF_ID, TEAM1_ID, true);
        verify(syncComponent).synchronizeSession(session2, WETTKAMPF_ID, TEAM2_ID, true);
    }

    @Test(expected = RuntimeException.class)
    public void testGenerateSchusszettelSessions_TeamLookupFailure() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        when(sessionDAO.findByWettkampfId(WETTKAMPF_ID))
                .thenReturn(Collections.singletonList(session));
        
        // Mock team lookup to fail
        when(mannschaftComponent.findById(TEAM1_ID))
                .thenThrow(new RuntimeException("Team not found"));
        
        setupWettkampfMocks();
        
        TabletSchusszettelSyncComponent.SyncResult syncResult = 
                TabletSchusszettelSyncComponent.SyncResult.success("Synced", true);
        when(syncComponent.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true))
                .thenReturn(syncResult);

        // Act - Should throw RuntimeException
        underTest.generateSchusszettelSessions(WETTKAMPF_ID);
    }

    @Test(expected = RuntimeException.class)
    public void testGenerateSchusszettelSessions_VereinLookupFailure() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        when(sessionDAO.findByWettkampfId(WETTKAMPF_ID))
                .thenReturn(Collections.singletonList(session));
        
        // Setup team but fail verein lookup
        DsbMannschaftDO team1 = new DsbMannschaftDO();
        team1.setId(TEAM1_ID);
        team1.setVereinId(1L);
        team1.setNummer(1L);
        when(mannschaftComponent.findById(TEAM1_ID)).thenReturn(team1);
        
        when(vereinComponent.findById(1L))
                .thenThrow(new RuntimeException("Verein not found"));
        
        setupWettkampfMocks();
        
        TabletSchusszettelSyncComponent.SyncResult syncResult = 
                TabletSchusszettelSyncComponent.SyncResult.success("Synced", true);
        when(syncComponent.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true))
                .thenReturn(syncResult);

        // Act - Should throw RuntimeException
        underTest.generateSchusszettelSessions(WETTKAMPF_ID);
    }

    @Test
    public void testGenerateSchusszettelSessions_WettkampfLookupFailure() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        when(sessionDAO.findByWettkampfId(WETTKAMPF_ID))
                .thenReturn(Collections.singletonList(session));
        
        setupTeamMocks();
        
        // Mock wettkampf lookup to fail - buildWettkampfInfo handles this gracefully
        when(wettkampfComponent.findById(WETTKAMPF_ID))
                .thenThrow(new RuntimeException("Wettkampf not found"));
        
        TabletSchusszettelSyncComponent.SyncResult syncResult = 
                TabletSchusszettelSyncComponent.SyncResult.success("Synced", true);
        when(syncComponent.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true))
                .thenReturn(syncResult);

        // Act & Assert - Should handle the exception gracefully in buildWettkampfInfo
        var result = underTest.generateSchusszettelSessions(WETTKAMPF_ID);
        
        // Should still return sessions even if wettkampf lookup fails (wettkampfInfo will be null)
        Assertions.assertThat(result.getTabletSessionSingDOs()).hasSize(1);
        Assertions.assertThat(result.getTabletSessionSingDOs()[0].getWettkampfInfo()).isNull();
    }

    @Test
    public void testGenerateSchusszettelSessions_VeranstaltungLookupFailure() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        when(sessionDAO.findByWettkampfId(WETTKAMPF_ID))
                .thenReturn(Collections.singletonList(session));
        
        setupTeamMocks();
        
        // Setup wettkampf but fail veranstaltung lookup - buildWettkampfInfo handles this gracefully
        de.bogenliga.application.business.wettkampf.api.types.WettkampfDO wettkampf = 
            new de.bogenliga.application.business.wettkampf.api.types.WettkampfDO();
        wettkampf.setId(WETTKAMPF_ID);
        wettkampf.setWettkampfVeranstaltungsId(1L);
        wettkampf.setWettkampfTag(1L);
        when(wettkampfComponent.findById(WETTKAMPF_ID)).thenReturn(wettkampf);
        
        when(veranstaltungComponent.findById(1L))
                .thenThrow(new RuntimeException("Veranstaltung not found"));
        
        TabletSchusszettelSyncComponent.SyncResult syncResult = 
                TabletSchusszettelSyncComponent.SyncResult.success("Synced", true);
        when(syncComponent.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true))
                .thenReturn(syncResult);

        // Act & Assert - Should handle the exception gracefully in buildWettkampfInfo
        var result = underTest.generateSchusszettelSessions(WETTKAMPF_ID);
        
        // Should still return sessions even if veranstaltung lookup fails (wettkampfInfo will be null)
        Assertions.assertThat(result.getTabletSessionSingDOs()).hasSize(1);
        Assertions.assertThat(result.getTabletSessionSingDOs()[0].getWettkampfInfo()).isNull();
    }

    @Test(expected = RuntimeException.class)
    public void testGenerateSchusszettelSessions_CriticalError() {
        // Arrange
        when(sessionDAO.findByWettkampfId(WETTKAMPF_ID))
                .thenThrow(new RuntimeException("Critical database error"));

        // Act
        underTest.generateSchusszettelSessions(WETTKAMPF_ID);
    }

    @Test
    public void testReTokenize_UpdateFailure() {
        // Arrange
        TabletSchusszettelEntity session = new TabletSchusszettelEntity();
        session.setWettkampfId(WETTKAMPF_ID);
        session.setTeamId(TEAM1_ID);
        session.setToken("old-token");
        
        when(sessionDAO.findByWettkampfUndTeam(WETTKAMPF_ID, TEAM1_ID))
                .thenReturn(Optional.of(session));
        
        doThrow(new RuntimeException("Update failed"))
                .when(sessionDAO).setToken(eq(WETTKAMPF_ID), eq(TEAM1_ID), anyString(), eq(-1L));

        // Act & Assert
        try {
            underTest.reTokenize(WETTKAMPF_ID, TEAM1_ID);
            Assertions.fail("Expected TechnicalException");
        } catch (TechnicalException e) {
            Assertions.assertThat(e.getErrorCode())
                    .isEqualTo(de.bogenliga.application.common.errorhandling.ErrorCode.INTERNAL_ERROR);
        }
    }
}