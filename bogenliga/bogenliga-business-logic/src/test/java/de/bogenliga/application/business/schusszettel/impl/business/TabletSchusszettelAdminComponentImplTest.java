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
 *
 * @author Test Generator
 */
public class TabletSchusszettelAdminComponentImplTest {

    private static final Long WETTKAMPF_ID = 1L;
    private static final Long TEAM1_ID = 10L;
    private static final Long TEAM2_ID = 20L;
    private static final Long MATCH_ID = 100L;

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
        when(syncComponent.determineCorrectPasseNumber(anyLong(), anyLong())).thenReturn(1);
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
        
        // Mock sync
        TabletSchusszettelSyncComponent.SyncResult syncResult = 
                TabletSchusszettelSyncComponent.SyncResult.success("Synced", true);
        when(syncComponent.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, false))
                .thenReturn(syncResult);

        // Act
        var result = underTest.generateSchusszettelSessions(WETTKAMPF_ID);

        // Assert
        Assertions.assertThat(result.getTabletSessionSingDOs()).hasSize(1);
        verify(syncComponent).synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, false);
    }

    @Test
    public void testGenerateSchusszettelSessions_SyncFailure() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        when(sessionDAO.findByWettkampfId(WETTKAMPF_ID))
                .thenReturn(Collections.singletonList(session));
        
        setupTeamMocks();
        
        // Mock sync failure
        TabletSchusszettelSyncComponent.SyncResult syncResult = 
                TabletSchusszettelSyncComponent.SyncResult.failure("Sync failed");
        when(syncComponent.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, false))
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
        when(mannschaftComponent.findById(TEAM1_ID)).thenReturn(team1);
        
        VereinDO verein1 = new VereinDO();
        verein1.setId(1L);
        verein1.setName("Team 1 Verein");
        when(vereinComponent.findById(1L)).thenReturn(verein1);
        
        // Team 2
        DsbMannschaftDO team2 = new DsbMannschaftDO();
        team2.setId(TEAM2_ID);
        team2.setVereinId(2L);
        when(mannschaftComponent.findById(TEAM2_ID)).thenReturn(team2);
        
        VereinDO verein2 = new VereinDO();
        verein2.setId(2L);
        verein2.setName("Team 2 Verein");
        when(vereinComponent.findById(2L)).thenReturn(verein2);
    }
}