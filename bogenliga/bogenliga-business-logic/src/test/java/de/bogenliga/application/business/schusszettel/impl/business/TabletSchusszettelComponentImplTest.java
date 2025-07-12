package de.bogenliga.application.business.schusszettel.impl.business;

import de.bogenliga.application.business.schusszettel.api.types.*;
import de.bogenliga.application.business.schusszettel.api.types.inside.*;
import de.bogenliga.application.business.schusszettel.impl.business.serviceAdapter.MatchAnalysisService;
import de.bogenliga.application.business.schusszettel.impl.dao.TabletSchusszettelDAO;
import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
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
 * Comprehensive tests for the TabletSchusszettelComponentImpl class.
 * Tests all public methods for complete coverage including edge cases and error handling.
 */
@RunWith(MockitoJUnitRunner.class)
public class TabletSchusszettelComponentImplTest {

    @Mock
    private TabletSchusszettelDAO mockSessionDAO;

    @Mock
    private PasseComponent mockPasseComponent;

    @Mock
    private MatchComponent mockMatchComponent;

    @Mock
    private MatchAnalysisService mockMatchAnalysisService;

    @Mock
    private MannschaftsmitgliedComponent mockMmComponent;

    @Mock
    private DsbMitgliedComponent mockDsbMitgliedComponent;

    @Mock
    private DsbMannschaftComponent mockMannschaftComponent;

    @Mock
    private VereinComponent mockVereinComponent;

    @Mock
    private WettkampfComponent mockWettkampfComponent;

    @Mock
    private VeranstaltungComponent mockVeranstaltungComponent;

    private TabletSchusszettelComponentImpl componentImpl;

    @Before
    public void setUp() {
        componentImpl = new TabletSchusszettelComponentImpl(
            mockSessionDAO,
            mockPasseComponent,
            mockMatchComponent,
            mockMatchAnalysisService,
            mockMmComponent,
            mockDsbMitgliedComponent,
            mockMannschaftComponent,
            mockVereinComponent,
            mockWettkampfComponent,
            mockVeranstaltungComponent
        );
    }

    // Test 1: getStatus(long wettkampfId, long teamId, String token)

    @Test
    public void getStatus_withValidToken_shouldReturnTabletSchusszettelDO() {
        // Arrange
        long wettkampfId = 50L;
        long teamId = 100L;
        String token = "validToken123";
        TabletSchusszettelEntity session = createValidSession();
        
        when(mockSessionDAO.findByTokenWettkampfUndTeam(wettkampfId, teamId, token)).thenReturn(Optional.of(session));
        
        // Act
        TabletSchusszettelDO result = componentImpl.getStatus(wettkampfId, teamId, token);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isNotNull();
    }

    @Test
    public void getStatus_withInvalidToken_shouldThrowBusinessException() {
        // Arrange
        long wettkampfId = 50L;
        long teamId = 100L;
        String invalidToken = "invalidToken";
        when(mockSessionDAO.findByTokenWettkampfUndTeam(wettkampfId, teamId, invalidToken)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThatThrownBy(() -> componentImpl.getStatus(wettkampfId, teamId, invalidToken))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    public void getStatus_withNullToken_shouldThrowBusinessException() {
        // Act & Assert
        assertThatThrownBy(() -> componentImpl.getStatus(50L, 100L, null))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    public void getStatus_withEmptyToken_shouldThrowBusinessException() {
        // Act & Assert
        assertThatThrownBy(() -> componentImpl.getStatus(50L, 100L, ""))
            .isInstanceOf(BusinessException.class);
    }

    // Test 2: submitSchuetzen(long wettkampfId, long teamId, String token, SchuetzenMeldungDO input)

    @Test
    public void submitSchuetzen_withValidInput_shouldProcessSuccessfully() {
        // Arrange
        long wettkampfId = 50L;
        long teamId = 100L;
        String token = "validToken123";
        TabletSchusszettelEntity session = createValidSession();
        session.setStatus("SCHUETZENMELDUNG");
        
        when(mockSessionDAO.findByTokenWettkampfUndTeam(wettkampfId, teamId, token)).thenReturn(Optional.of(session));
        
        SchuetzenMeldungDO meldung = new SchuetzenMeldungDO();
        meldung.setGemeldeteSchuetzen(Arrays.asList(1L, 2L, 3L));
        
        List<MannschaftsmitgliedDO> deployedMembers = createDeployedMembers();
        when(mockMmComponent.findByTeamId(teamId)).thenReturn(deployedMembers);
        
        // Act
        componentImpl.submitSchuetzen(wettkampfId, teamId, token, meldung);
        
        // Assert
        verify(mockSessionDAO).findByTokenWettkampfUndTeam(wettkampfId, teamId, token);
        verify(mockMmComponent).findByTeamId(teamId);
    }

    @Test
    public void submitSchuetzen_withInvalidToken_shouldThrowBusinessException() {
        // Arrange
        long wettkampfId = 50L;
        long teamId = 100L;
        String invalidToken = "invalidToken";
        when(mockSessionDAO.findByTokenWettkampfUndTeam(wettkampfId, teamId, invalidToken)).thenReturn(Optional.empty());
        
        SchuetzenMeldungDO meldung = new SchuetzenMeldungDO();
        meldung.setGemeldeteSchuetzen(Arrays.asList(1L, 2L, 3L));
        
        // Act & Assert
        assertThatThrownBy(() -> componentImpl.submitSchuetzen(wettkampfId, teamId, invalidToken, meldung))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    public void submitSchuetzen_withWrongStatus_shouldThrowBusinessException() {
        // Arrange
        long wettkampfId = 50L;
        long teamId = 100L;
        String token = "validToken123";
        TabletSchusszettelEntity session = createValidSession();
        session.setStatus("SATZEINGABE"); // Wrong status for shooter submission
        
        when(mockSessionDAO.findByTokenWettkampfUndTeam(wettkampfId, teamId, token)).thenReturn(Optional.of(session));
        
        SchuetzenMeldungDO meldung = new SchuetzenMeldungDO();
        meldung.setGemeldeteSchuetzen(Arrays.asList(1L, 2L, 3L));
        
        // Act & Assert
        assertThatThrownBy(() -> componentImpl.submitSchuetzen(wettkampfId, teamId, token, meldung))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    public void submitSchuetzen_withInvalidShooterCount_shouldThrowBusinessException() {
        // Arrange
        long wettkampfId = 50L;
        long teamId = 100L;
        String token = "validToken123";
        TabletSchusszettelEntity session = createValidSession();
        session.setStatus("SCHUETZENMELDUNG");
        
        when(mockSessionDAO.findByTokenWettkampfUndTeam(wettkampfId, teamId, token)).thenReturn(Optional.of(session));
        
        SchuetzenMeldungDO meldung = new SchuetzenMeldungDO();
        meldung.setGemeldeteSchuetzen(Arrays.asList(1L, 2L)); // Only 2 shooters instead of 3
        
        // Act & Assert
        assertThatThrownBy(() -> componentImpl.submitSchuetzen(wettkampfId, teamId, token, meldung))
            .isInstanceOf(BusinessException.class);
    }

    // Test 3: submitSatz(long wettkampfId, long teamId, String token, SatzEingabeDO eingabe)

    @Test
    public void submitSatz_withValidInput_shouldProcessSuccessfully() {
        // Arrange
        long wettkampfId = 50L;
        long teamId = 100L;
        String token = "validToken123";
        TabletSchusszettelEntity session = createValidSession();
        session.setStatus("SATZEINGABE");
        
        when(mockSessionDAO.findByTokenWettkampfUndTeam(wettkampfId, teamId, token)).thenReturn(Optional.of(session));
        
        SatzEingabeDO eingabe = createValidSatzEingabe();
        
        List<PasseDO> existingPasses = createExistingPasses();
        when(mockPasseComponent.findByMannschaftMatchId(teamId, session.getCurrentMatchId())).thenReturn(existingPasses);
        
        // Act
        componentImpl.submitSatz(wettkampfId, teamId, token, eingabe);
        
        // Assert
        verify(mockSessionDAO).findByTokenWettkampfUndTeam(wettkampfId, teamId, token);
        verify(mockPasseComponent).findByMannschaftMatchId(teamId, session.getCurrentMatchId());
    }

    @Test
    public void submitSatz_withInvalidToken_shouldThrowBusinessException() {
        // Arrange
        long wettkampfId = 50L;
        long teamId = 100L;
        String invalidToken = "invalidToken";
        when(mockSessionDAO.findByTokenWettkampfUndTeam(wettkampfId, teamId, invalidToken)).thenReturn(Optional.empty());
        
        SatzEingabeDO eingabe = createValidSatzEingabe();
        
        // Act & Assert
        assertThatThrownBy(() -> componentImpl.submitSatz(wettkampfId, teamId, invalidToken, eingabe))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    public void submitSatz_withWrongStatus_shouldThrowBusinessException() {
        // Arrange
        long wettkampfId = 50L;
        long teamId = 100L;
        String token = "validToken123";
        TabletSchusszettelEntity session = createValidSession();
        session.setStatus("SCHUETZENMELDUNG"); // Wrong status for score submission
        
        when(mockSessionDAO.findByTokenWettkampfUndTeam(wettkampfId, teamId, token)).thenReturn(Optional.of(session));
        
        SatzEingabeDO eingabe = createValidSatzEingabe();
        
        // Act & Assert
        assertThatThrownBy(() -> componentImpl.submitSatz(wettkampfId, teamId, token, eingabe))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    public void submitSatz_withInvalidArrowValues_shouldThrowBusinessException() {
        // Arrange
        long wettkampfId = 50L;
        long teamId = 100L;
        String token = "validToken123";
        TabletSchusszettelEntity session = createValidSession();
        session.setStatus("SATZEINGABE");
        
        when(mockSessionDAO.findByTokenWettkampfUndTeam(wettkampfId, teamId, token)).thenReturn(Optional.of(session));
        
        SatzEingabeDO eingabe = createInvalidSatzEingabe(); // Contains invalid arrow values
        
        // Act & Assert
        assertThatThrownBy(() -> componentImpl.submitSatz(wettkampfId, teamId, token, eingabe))
            .isInstanceOf(BusinessException.class);
    }

    // Helper methods

    private TabletSchusszettelEntity createValidSession() {
        TabletSchusszettelEntity session = new TabletSchusszettelEntity();
        session.setWettkampfId(50L);
        session.setTeamId(100L);
        session.setGegnerTeamId(200L);
        session.setCurrentMatchId(300L);
        session.setCurrentPasseNumber(1);
        session.setStatus("SCHUETZENMELDUNG");
        session.setToken("validToken123");
        return session;
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

    private SatzEingabeDO createInvalidSatzEingabe() {
        SatzEingabeDO eingabe = new SatzEingabeDO();
        
        SchuetzenSatzDO schuetze1 = new SchuetzenSatzDO();
        schuetze1.setSchuetzenId(1L);
        schuetze1.setSchuss1(11); // Invalid: > 10
        schuetze1.setSchuss2(9);
        schuetze1.setSchuss3(7);
        
        eingabe.setSatzeingabe(Collections.singletonList(schuetze1));
        return eingabe;
    }

    private List<MannschaftsmitgliedDO> createDeployedMembers() {
        List<MannschaftsmitgliedDO> members = new ArrayList<>();
        
        for (int i = 1; i <= 5; i++) {
            MannschaftsmitgliedDO member = new MannschaftsmitgliedDO(
                (long) i,  // id
                100L,  // mannschaftId
                (long) i * 10,  // dsbMitgliedId
                1,  // dsbMitgliedEingesetzt
                "Vorname" + i,  // dsbMitgliedVorname
                "Nachname" + i,  // dsbMitgliedNachname
                (long) i  // rueckennummer
            );
            members.add(member);
        }
        
        return members;
    }

    private List<PasseDO> createExistingPasses() {
        List<PasseDO> passes = new ArrayList<>();
        
        for (int i = 1; i <= 3; i++) {
            PasseDO passe = new PasseDO();
            passe.setPasseMannschaftId(100L);
            passe.setPasseMatchId(300L);
            passe.setPasseLfdnr(1L);
            passe.setPasseDsbMitgliedId((long) i);
            passe.setPfeil1(0);
            passe.setPfeil2(0);
            passe.setPfeil3(0);
            passes.add(passe);
        }
        
        return passes;
    }
}