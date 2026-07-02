package de.bogenliga.application.business.schusszettel.impl.business.domain.states;

import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.VerfuegbarerSchuetzeDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.SchuetzeStammdatenDO;
import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test class for Schuetzenmeldung state implementation.
 * Tests shooter registration state behavior, transitions, and validation rules.
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class SchuetzenmeldungTest {

    @Mock private StateContext mockContext;
    @Mock private MannschaftsmitgliedComponent mockMmComponent;
    @Mock private DsbMitgliedComponent mockMitgliedComponent;
    @Mock private MatchComponent mockMatchComponent;
    
    private Schuetzenmeldung state;
    private TabletSchusszettelEntity mockSession;
    private MatchDO mockMatch;
    private List<MannschaftsmitgliedDO> testTeamMembers;
    private List<DsbMitgliedDO> testMembers;

    @Before
    public void setUp() {
        state = new Schuetzenmeldung();
        setupTestData();
        setupMockBehavior();
    }
    
    private void setupTestData() {
        mockSession = new TabletSchusszettelEntity();
        mockSession.setTeamId(100L);
        mockSession.setCurrentMatchId(200L);
        
        mockMatch = new MatchDO();
        mockMatch.setId(200L);
        mockMatch.setNr(1L);
        
        testTeamMembers = Arrays.asList(
            createTeamMember(1L, 101L, 1),
            createTeamMember(2L, 102L, 1),
            createTeamMember(3L, 103L, 1),
            createTeamMember(4L, 104L, 0)
        );
        
        testMembers = Arrays.asList(
            createMember(101L, "Max", "Mustermann"),
            createMember(102L, "Anna", "Schmidt"),
            createMember(103L, "Peter", "Mueller"),
            createMember(104L, "Sarah", "Weber")
        );
    }

    private void setupMockBehavior() {
        when(mockContext.validateSessionState()).thenReturn(StateContext.ValidationResult.valid());
        when(mockContext.getTeamId()).thenReturn(100L);
        when(mockContext.getCurrentMatchId()).thenReturn(200L);
        when(mockContext.getMatchComponent()).thenReturn(mockMatchComponent);
        when(mockContext.getMannschaftsmitgliedComponent()).thenReturn(mockMmComponent);
        when(mockContext.getDsbMitgliedComponent()).thenReturn(mockMitgliedComponent);
        when(mockContext.getDeployedTeamMembers()).thenReturn(testTeamMembers.subList(0, 3));
        
        when(mockMatchComponent.findById(200L)).thenReturn(mockMatch);
        when(mockMmComponent.findByTeamId(100L)).thenReturn(testTeamMembers);
        
        for (DsbMitgliedDO member : testMembers) {
            when(mockMitgliedComponent.findById(member.getId())).thenReturn(member);
        }
        
        for (MannschaftsmitgliedDO teamMember : testTeamMembers) {
            when(mockMmComponent.findByMemberAndTeamId(100L, teamMember.getDsbMitgliedId())).thenReturn(teamMember);
            when(mockContext.isShooterDeployed(teamMember.getDsbMitgliedId())).thenReturn(teamMember.getDsbMitgliedEingesetzt() >= 1);
        }
    }

    @Test
    public void isValidState_validSession_returnsTrue() {
        boolean result = state.isValidState(mockContext);
        assertThat(result).isTrue();
        verify(mockContext).validateSessionState();
    }

    @Test
    public void isValidState_invalidSession_returnsFalse() {
        when(mockContext.validateSessionState()).thenReturn(StateContext.ValidationResult.invalid("Test error"));
        
        boolean result = state.isValidState(mockContext);
        assertThat(result).isFalse();
    }

    @Test
    public void canTransitionTo_satzeingabe_returnsTrue() {
        boolean result = state.canTransitionTo(mockContext, State.STATUS_SATZEINGABE);
        assertThat(result).isTrue();
    }

    @Test
    public void canTransitionTo_otherState_returnsFalse() {
        boolean result = state.canTransitionTo(mockContext, State.STATUS_WARTE);
        assertThat(result).isFalse();
    }

    @Test
    public void isDatabaseReadyForTransition_threeShootersDeployed_returnsTrue() {
        // gemeldet = eingesetzt traegt die Match-ID (200), nicht die Match-Nr
        when(mockMmComponent.findByTeamId(100L)).thenReturn(Arrays.asList(
            createTeamMember(1L, 101L, 200),
            createTeamMember(2L, 102L, 200),
            createTeamMember(3L, 103L, 200)
        ));

        boolean result = state.isDatabaseReadyForTransition(mockContext, State.STATUS_SATZEINGABE);
        assertThat(result).isTrue();
    }

    @Test
    public void isDatabaseReadyForTransition_wrongNumberOfShooters_returnsFalse() {
        when(mockMmComponent.findByTeamId(100L)).thenReturn(testTeamMembers.subList(0, 2));
        
        boolean result = state.isDatabaseReadyForTransition(mockContext, State.STATUS_SATZEINGABE);
        assertThat(result).isFalse();
    }

    @Test
    public void isDatabaseReadyForTransition_nullContext_returnsFalse() {
        boolean result = state.isDatabaseReadyForTransition(null, State.STATUS_SATZEINGABE);
        assertThat(result).isFalse();
    }

    @Test
    public void isDatabaseReadyForTransition_wrongTargetState_returnsFalse() {
        boolean result = state.isDatabaseReadyForTransition(mockContext, State.STATUS_WARTE);
        assertThat(result).isFalse();
    }

    @Test
    public void isDatabaseReadyForTransition_exceptionInCheck_returnsFalse() {
        when(mockContext.getMatchComponent()).thenThrow(new RuntimeException("DB error"));
        
        boolean result = state.isDatabaseReadyForTransition(mockContext, State.STATUS_SATZEINGABE);
        assertThat(result).isFalse();
    }

    @Test
    public void prepareResponseData_validContext_returnsCorrectData() {
        Map<String, Object> result = state.prepareResponseData(mockContext);
        
        assertThat(result).isNotNull();
        assertThat(result).containsKey("schuetzeStammDaten");
        assertThat(result).containsKey("verfuegbareSchuetzen");
        
        @SuppressWarnings("unchecked")
        List<SchuetzeStammdatenDO> stammdaten = (List<SchuetzeStammdatenDO>) result.get("schuetzeStammDaten");
        assertThat(stammdaten).hasSize(3);
        
        @SuppressWarnings("unchecked")
        List<VerfuegbarerSchuetzeDO> available = (List<VerfuegbarerSchuetzeDO>) result.get("verfuegbareSchuetzen");
        assertThat(available).hasSize(3);
    }

    @Test
    public void prepareResponseData_exceptionInDataPreparation_returnsEmptyLists() {
        when(mockContext.getDeployedTeamMembers()).thenThrow(new RuntimeException("DB error"));
        
        Map<String, Object> result = state.prepareResponseData(mockContext);
        
        assertThat(result).isNotNull();
        assertThat(result.get("schuetzeStammDaten")).isEqualTo(Collections.emptyList());
        assertThat(result.get("verfuegbareSchuetzen")).isEqualTo(Collections.emptyList());
    }

    @Test
    public void validateOperation_validShooterRegistration_returnsTrue() {
        List<Long> shooterIds = Arrays.asList(101L, 102L, 103L);
        
        boolean result = state.validateOperation(mockContext, "submitSchuetzen", shooterIds);
        assertThat(result).isTrue();
    }

    @Test
    public void validateOperation_wrongOperation_returnsFalse() {
        List<Long> shooterIds = Arrays.asList(101L, 102L, 103L);
        
        boolean result = state.validateOperation(mockContext, "wrongOperation", shooterIds);
        assertThat(result).isFalse();
    }

    @Test
    public void validateOperation_wrongDataType_returnsFalse() {
        boolean result = state.validateOperation(mockContext, "submitSchuetzen", "wrongType");
        assertThat(result).isFalse();
    }

    @Test
    public void validateOperation_wrongNumberOfShooters_returnsFalse() {
        List<Long> shooterIds = Arrays.asList(101L, 102L);
        
        boolean result = state.validateOperation(mockContext, "submitSchuetzen", shooterIds);
        assertThat(result).isFalse();
    }

    @Test
    public void validateOperation_duplicateShooters_returnsFalse() {
        List<Long> shooterIds = Arrays.asList(101L, 102L, 101L);
        
        boolean result = state.validateOperation(mockContext, "submitSchuetzen", shooterIds);
        assertThat(result).isFalse();
    }

    @Test
    public void validateOperation_invalidShooter_returnsFalse() {
        List<Long> shooterIds = Arrays.asList(101L, 102L, 999L);
        when(mockContext.isShooterDeployed(999L)).thenReturn(false);
        
        boolean result = state.validateOperation(mockContext, "submitSchuetzen", shooterIds);
        assertThat(result).isFalse();
    }

    @Test
    public void validateOperation_notDeployedShooter_returnsFalse() {
        List<Long> shooterIds = Arrays.asList(101L, 102L, 104L);
        when(mockContext.isShooterDeployed(104L)).thenReturn(false);
        
        boolean result = state.validateOperation(mockContext, "submitSchuetzen", shooterIds);
        assertThat(result).isFalse();
    }

    @Test
    public void handlePostOperation_validRegistration_succeeds() {
        List<Long> shooterIds = Arrays.asList(101L, 102L, 103L);
        
        boolean result = state.handlePostOperation(mockContext, "submitSchuetzen", shooterIds);
        assertThat(result).isTrue();

        verify(mockContext).updateSessionStatus(State.STATUS_SATZEINGABE);
        verify(mockMmComponent, times(3)).update(any(MannschaftsmitgliedDO.class), eq(0L));

        // eingesetzt muss die eindeutige Match-ID tragen, nicht die Match-Nr
        assertThat(testTeamMembers.get(0).getDsbMitgliedEingesetzt()).isEqualTo(200);
        assertThat(testTeamMembers.get(1).getDsbMitgliedEingesetzt()).isEqualTo(200);
        assertThat(testTeamMembers.get(2).getDsbMitgliedEingesetzt()).isEqualTo(200);
    }

    @Test
    public void handlePostOperation_wrongOperation_returnsFalse() {
        List<Long> shooterIds = Arrays.asList(101L, 102L, 103L);
        
        boolean result = state.handlePostOperation(mockContext, "wrongOperation", shooterIds);
        assertThat(result).isFalse();
    }

    @Test
    public void handlePostOperation_wrongDataType_returnsFalse() {
        boolean result = state.handlePostOperation(mockContext, "submitSchuetzen", "wrongType");
        assertThat(result).isFalse();
    }

    @Test
    public void handlePostOperation_exceptionInProcessing_returnsFalse() {
        List<Long> shooterIds = Arrays.asList(101L, 102L, 103L);
        doThrow(new RuntimeException("DB error")).when(mockContext).updateSessionStatus(anyString());
        
        boolean result = state.handlePostOperation(mockContext, "submitSchuetzen", shooterIds);
        assertThat(result).isFalse();
    }

    @Test
    public void handlePostOperation_warningInShooterUpdate_continuesProcessing() {
        List<Long> shooterIds = Arrays.asList(101L, 102L, 103L);
        when(mockMmComponent.findByMemberAndTeamId(100L, 102L)).thenThrow(new RuntimeException("Member not found"));
        
        boolean result = state.handlePostOperation(mockContext, "submitSchuetzen", shooterIds);
        assertThat(result).isTrue();
        
        verify(mockContext).updateSessionStatus(State.STATUS_SATZEINGABE);
    }

    @Test
    public void markShootersAsDeployed_exceptionInDeployment_throwsException() {
        List<Long> shooterIds = Arrays.asList(101L, 102L, 103L);
        when(mockContext.getCurrentMatchId()).thenThrow(new RuntimeException("DB error"));
        
        boolean result = state.handlePostOperation(mockContext, "submitSchuetzen", shooterIds);
        assertThat(result).isFalse();
    }

    @Test
    public void getDeployedMembersForCurrentMatch_validMatch_returnsFilteredMembers() {
        // Regression zum Ticket "inkonsistente Schuetzen": Kadermitglieder mit
        // blossem Waehlbar-Flag (eingesetzt=1) duerfen nicht als gemeldet zaehlen
        when(mockMmComponent.findByTeamId(100L)).thenReturn(Arrays.asList(
            createTeamMember(1L, 101L, 200),
            createTeamMember(2L, 102L, 200),
            createTeamMember(3L, 103L, 200),
            createTeamMember(4L, 104L, 1)
        ));

        boolean result = state.isDatabaseReadyForTransition(mockContext, State.STATUS_SATZEINGABE);
        assertThat(result).isTrue();
    }

    @Test
    public void getDeployedMembersForCurrentMatch_exceptionInQuery_returnsEmptyList() {
        when(mockContext.getCurrentMatchId()).thenThrow(new RuntimeException("DB error"));
        
        boolean result = state.isDatabaseReadyForTransition(mockContext, State.STATUS_SATZEINGABE);
        assertThat(result).isFalse();
    }

    @Test
    public void handleWarteEvaluation_always_returnsFalse() {
        TabletSchusszettelEntity opponent = new TabletSchusszettelEntity();
        boolean result = state.handleWarteEvaluation(mockContext, opponent);
        assertThat(result).isFalse();
    }

    @Test
    public void canNudgeAlong_always_returnsTrue() {
        boolean result = state.canNudgeAlong();
        assertThat(result).isTrue();
    }

    @Test
    public void toString_returnsCorrectStateName() {
        String result = state.toString();
        assertThat(result).contains("Schuetzenmeldung");
    }

    private MannschaftsmitgliedDO createTeamMember(Long id, Long memberId, int deployed) {
        MannschaftsmitgliedDO member = new MannschaftsmitgliedDO(id);
        member.setMannschaftId(100L);
        member.setDsbMitgliedId(memberId);
        member.setDsbMitgliedEingesetzt(deployed);
        member.setRueckennummer((long) deployed);
        return member;
    }

    private DsbMitgliedDO createMember(Long id, String vorname, String nachname) {
        DsbMitgliedDO member = new DsbMitgliedDO();
        member.setId(id);
        member.setVorname(vorname);
        member.setNachname(nachname);
        return member;
    }
}