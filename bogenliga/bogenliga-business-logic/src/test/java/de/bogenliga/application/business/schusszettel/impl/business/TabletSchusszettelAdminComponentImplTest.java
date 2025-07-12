package de.bogenliga.application.business.schusszettel.impl.business;

import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.schusszettel.impl.business.serviceAdapter.MatchAnalysisService;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.schusszettel.api.types.TabletSessionInfoDO;
import de.bogenliga.application.business.schusszettel.impl.dao.TabletSchusszettelDAO;
import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Comprehensive tests for the TabletSchusszettelAdminComponentImpl class.
 * Tests all administrative functionality including session lifecycle management.
 */
@RunWith(MockitoJUnitRunner.class)
public class TabletSchusszettelAdminComponentImplTest {

    @Mock
    private TabletSchusszettelDAO mockSessionDAO;

    @Mock
    private MatchComponent mockMatchComponent;

    @Mock
    private DsbMannschaftComponent mockMannschaftComponent;

    @Mock
    private VereinComponent mockVereinComponent;

    @Mock
    private PasseComponent mockPasseComponent;

    @Mock
    private MatchAnalysisService mockMatchAnalysisService;

    @Mock
    private WettkampfComponent mockWettkampfComponent;

    @Mock
    private VeranstaltungComponent mockVeranstaltungComponent;

    @Mock
    private MannschaftsmitgliedComponent mockMannschaftsmitgliedComponent;

    @Mock
    private DsbMitgliedComponent mockDsbMitgliedComponent;

    private TabletSchusszettelAdminComponentImpl adminComponentImpl;

    @Before
    public void setUp() {
        adminComponentImpl = new TabletSchusszettelAdminComponentImpl(
            mockSessionDAO,
            mockMatchComponent,
            mockMannschaftComponent,
            mockVereinComponent,
            mockPasseComponent,
            mockMatchAnalysisService,
            mockWettkampfComponent,
            mockVeranstaltungComponent,
            mockMannschaftsmitgliedComponent,
            mockDsbMitgliedComponent
        );
    }

    // Test 1: initializeForWettkampf(long wettkampfId)

    @Test
    public void initializeForWettkampf_withValidWettkampfId_shouldCreateSessionsForAllTeams() {
        // Arrange
        long wettkampfId = 50L;
        List<LigamatchBE> matches = createWettkampfMatches();
        
        when(mockMatchComponent.getLigamatchesByWettkampfId(wettkampfId)).thenReturn(matches);
        when(mockMatchAnalysisService.findCurrentIncompleteMatch(anyLong(), anyLong())).thenReturn(matches.get(0));
        when(mockMatchAnalysisService.findOpponentTeamId(anyLong(), anyLong())).thenReturn(200L);
        
        // Act
        adminComponentImpl.initializeForWettkampf(wettkampfId);
        
        // Assert
        verify(mockSessionDAO).deleteByWettkampfId(wettkampfId);
        verify(mockSessionDAO, times(2)).createSession(any(TabletSchusszettelEntity.class), any(Long.class));
    }

    @Test
    public void initializeForWettkampf_withNegativeWettkampfId_shouldThrowBusinessException() {
        // Act & Assert
        assertThatThrownBy(() -> adminComponentImpl.initializeForWettkampf(-1L))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    public void initializeForWettkampf_withInvalidWettkampfId_shouldThrowBusinessException() {
        // Arrange
        long invalidWettkampfId = 0L;
        
        // Act & Assert
        assertThatThrownBy(() -> adminComponentImpl.initializeForWettkampf(invalidWettkampfId))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    public void initializeForWettkampf_withNoMatches_shouldDeleteExistingSessionsOnly() {
        // Arrange
        long wettkampfId = 50L;
        when(mockMatchComponent.getLigamatchesByWettkampfId(wettkampfId)).thenReturn(Collections.emptyList());
        
        // Act
        adminComponentImpl.initializeForWettkampf(wettkampfId);
        
        // Assert
        verify(mockSessionDAO).deleteByWettkampfId(wettkampfId);
        verify(mockSessionDAO, never()).createSession(any(TabletSchusszettelEntity.class), any(Long.class));
    }

    // Test 2: deleteForWettkampf(long wettkampfId)

    @Test
    public void deleteForWettkampf_withValidWettkampfId_shouldDeleteAllSessions() {
        // Arrange
        long wettkampfId = 50L;
        
        // Act
        adminComponentImpl.deleteForWettkampf(wettkampfId);
        
        // Assert
        verify(mockSessionDAO).deleteByWettkampfId(wettkampfId);
    }

    @Test
    public void deleteForWettkampf_withNegativeWettkampfId_shouldThrowBusinessException() {
        // Act & Assert
        assertThatThrownBy(() -> adminComponentImpl.deleteForWettkampf(-1L))
            .isInstanceOf(BusinessException.class);
    }

    // Test 3: existsForWettkampf(long wettkampfId)

    @Test
    public void existsForWettkampf_withExistingSessions_shouldReturnTrue() {
        // Arrange
        long wettkampfId = 50L;
        List<TabletSchusszettelEntity> sessions = Arrays.asList(
            createTestSession(100L, "SCHUETZENMELDUNG"),
            createTestSession(200L, "SATZEINGABE")
        );
        when(mockSessionDAO.findByWettkampfId(wettkampfId)).thenReturn(sessions);
        
        // Act
        boolean result = adminComponentImpl.existsForWettkampf(wettkampfId);
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void existsForWettkampf_withNoSessions_shouldReturnFalse() {
        // Arrange
        long wettkampfId = 50L;
        when(mockSessionDAO.findByWettkampfId(wettkampfId)).thenReturn(Collections.emptyList());
        
        // Act
        boolean result = adminComponentImpl.existsForWettkampf(wettkampfId);
        
        // Assert
        assertThat(result).isFalse();
    }

    // Test 4: reTokenize(long wettkampfId, long teamId)

    @Test
    public void reTokenize_withValidWettkampfAndTeamId_shouldUpdateToken() {
        // Arrange
        long wettkampfId = 50L;
        long teamId = 100L;
        TabletSchusszettelEntity session = createTestSession(teamId, "SCHUETZENMELDUNG");
        session.setWettkampfId(wettkampfId);
        
        when(mockSessionDAO.findByWettkampfUndTeam(wettkampfId, teamId)).thenReturn(Optional.of(session));
        
        // Act
        adminComponentImpl.reTokenize(wettkampfId, teamId);
        
        // Assert
        verify(mockSessionDAO).findByWettkampfUndTeam(wettkampfId, teamId);
        verify(mockSessionDAO).updateStatus(eq(session), any(Long.class));
    }

    @Test
    public void reTokenize_withInvalidWettkampfAndTeamId_shouldThrowBusinessException() {
        // Arrange
        long wettkampfId = 50L;
        long teamId = 100L;
        
        when(mockSessionDAO.findByWettkampfUndTeam(wettkampfId, teamId)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThatThrownBy(() -> adminComponentImpl.reTokenize(wettkampfId, teamId))
            .isInstanceOf(BusinessException.class);
    }

    // Test 5: generateSchusszettelSessions(long wettkampfId)

    @Test
    public void generateSchusszettelSessions_withValidWettkampfId_shouldReturnSessionInfo() {
        // Arrange
        long wettkampfId = 50L;
        List<TabletSchusszettelEntity> sessions = Arrays.asList(
            createTestSession(100L, "SCHUETZENMELDUNG"),
            createTestSession(200L, "SATZEINGABE")
        );
        
        when(mockSessionDAO.findByWettkampfId(wettkampfId)).thenReturn(sessions);
        
        // Mock team and verein data
        setupTeamAndVereinMocks();
        
        // Act
        TabletSessionInfoDO result = adminComponentImpl.generateSchusszettelSessions(wettkampfId);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getWettkampfId()).isEqualTo(wettkampfId);
        assertThat(result.getTabletSessionSingDOs()).isNotNull();
    }

    @Test
    public void generateSchusszettelSessions_withNegativeWettkampfId_shouldThrowBusinessException() {
        // Act & Assert
        assertThatThrownBy(() -> adminComponentImpl.generateSchusszettelSessions(-1L))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    public void generateSchusszettelSessions_withInvalidWettkampfId_shouldThrowBusinessException() {
        // Arrange
        long invalidWettkampfId = 0L;
        
        // Act & Assert
        assertThatThrownBy(() -> adminComponentImpl.generateSchusszettelSessions(invalidWettkampfId))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    public void generateSchusszettelSessions_withNoSessions_shouldReturnEmptyInfo() {
        // Arrange
        long wettkampfId = 50L;
        when(mockSessionDAO.findByWettkampfId(wettkampfId)).thenReturn(Collections.emptyList());
        
        // Act
        TabletSessionInfoDO result = adminComponentImpl.generateSchusszettelSessions(wettkampfId);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getWettkampfId()).isEqualTo(wettkampfId);
    }

    // Helper methods

    private List<LigamatchBE> createWettkampfMatches() {
        List<LigamatchBE> matches = new ArrayList<>();
        
        LigamatchBE match1 = new LigamatchBE();
        match1.setMatchId(300L);
        match1.setMannschaftId(100L);
        match1.setSatzpunkte(2L);
        match1.setWettkampfId(50L);
        matches.add(match1);
        
        LigamatchBE match2 = new LigamatchBE();
        match2.setMatchId(301L);
        match2.setMannschaftId(200L);
        match2.setSatzpunkte(1L);
        match2.setWettkampfId(50L);
        matches.add(match2);
        
        return matches;
    }

    private void setupTeamAndVereinMocks() {
        // Mock team 100
        DsbMannschaftDO team100 = new DsbMannschaftDO();
        team100.setId(100L);
        team100.setName("Team Alpha");
        team100.setVereinId(10L);
        when(mockMannschaftComponent.findById(100L)).thenReturn(team100);
        
        // Mock team 200
        DsbMannschaftDO team200 = new DsbMannschaftDO();
        team200.setId(200L);
        team200.setName("Team Beta");
        team200.setVereinId(20L);
        when(mockMannschaftComponent.findById(200L)).thenReturn(team200);
        
        // Mock verein 10
        VereinDO verein10 = new VereinDO();
        verein10.setId(10L);
        verein10.setName("Verein Alpha");
        when(mockVereinComponent.findById(10L)).thenReturn(verein10);
        
        // Mock verein 20
        VereinDO verein20 = new VereinDO();
        verein20.setId(20L);
        verein20.setName("Verein Beta");
        when(mockVereinComponent.findById(20L)).thenReturn(verein20);
    }

    private TabletSchusszettelEntity createTestSession(Long teamId, String status) {
        TabletSchusszettelEntity session = new TabletSchusszettelEntity();
        session.setTeamId(teamId);
        session.setGegnerTeamId(teamId == 100L ? 200L : 100L);
        session.setWettkampfId(50L);
        session.setCurrentMatchId(300L);
        session.setCurrentPasseNumber(1);
        session.setStatus(status);
        session.setToken("token" + teamId);
        return session;
    }
}